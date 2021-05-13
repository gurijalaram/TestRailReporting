package com.apriori.utils.http2.builder.service;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.isOneOf;

import com.apriori.utils.AuthorizationFormUtil;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.utils.http.builder.common.response.common.PayloadJSON;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.http.enums.common.api.AuthEndpointEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.json.utils.JsonManager;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ConnectionManager} class has the following purposes:
 * - Creates {@link RequestSpecification}
 * - Connects to desired endpoint using
 * GET ({@link #get()}), POST ({@link #post()}), PUT ({@link #put()}), PATCH ({@link #patch()}) and DELETE ({@link #delete()}) methods
 * - Converts response JSON into the desired POJO object
 */
class ConnectionManager<T> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static Map<String, String> sessionIds = new ConcurrentHashMap<>();
    private static Map<String, String> authTokens = new ConcurrentHashMap<>();
    private Class<T> returnType;
    private RequestEntity requestEntity;

    public ConnectionManager(RequestEntity requestEntity, Class<T> returnType) {
        this.requestEntity = requestEntity;
        this.returnType = returnType;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.urlEncodingEnabled = requestEntity.urlEncodingEnabled();

    }

    private RequestSpecification createRequestSpecification() {
        RequestSpecBuilder builder = new RequestSpecBuilder();

        List<Map<String, ?>> urlParams = requestEntity.urlParams();
        MultiPartFiles multiPartFiles = requestEntity.multiPartFiles();
        FormParams formParams = requestEntity.formParams();

        if (requestEntity.autoLogin()) {
            requestEntity.headers(AuthorizationFormUtil.getTokenAuthorizationForm(this.getAuthToken()));
        }

        if (multiPartFiles != null) {
            builder.setContentType("multipart/form-data");
            multiPartFiles.forEach(builder::addMultiPart);
        } else {
            builder.setContentType(ContentType.JSON);
        }

        if (formParams != null) {
            formParams.forEach(builder::addFormParam);
        }

        if (urlParams != null) {
            urlParams.forEach(builder::addQueryParams);
        }

        if (requestEntity.headers() != null) {
            builder.addHeaders(requestEntity.headers());
        }

        if (requestEntity.token() != null) {
            builder.addHeader("Authorization", "Bearer " + requestEntity.token());
        }

        if (requestEntity.xwwwwFormUrlEncodeds() != null && !requestEntity.xwwwwFormUrlEncodeds().isEmpty()) {
            builder.setContentType(ContentType.URLENC);
            requestEntity.xwwwwFormUrlEncodeds().forEach(builder::addFormParams);
        }

        if (requestEntity.customBody() != null) {
            builder.setBody(requestEntity.customBody());
        }

        if (requestEntity.body() != null) {
            builder.setBody(requestEntity.body(), ObjectMapperType.JACKSON_2);
        }

        /*
         * /http.connection.timeout/
         * setConnectionTimeout :   Client tries to connect to the server. This denotes the time elapsed before the
         *                          connection established or Server responded to connection request.
         *
         * /http.socket.timeout/
         * setSoTimeout :           After establishing the connection, the client socket waits for response after
         *                          sending the request. This is the elapsed time since the client has sent request to
         *                          the server before server responds. Please note that this is not same as
         *                          HTTP Error 408 which the server sends to the client. In other words its maximum
         *                          period inactivity between two consecutive data packets arriving at client side
         *                          after connection is established.
         */
        builder
            .setConfig(RestAssuredConfig.config()
                .httpClient(
                    HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", requestEntity.connectionTimeout())
                        .setParam("http.socket.timeout", requestEntity.socketTimeout())
                )
                .sslConfig(ignoreSslCheck() ? new SSLConfig().allowAllHostnames() : new SSLConfig())
            )
            .setBaseUri(requestEntity.buildEndpoint());


        if (requestEntity.statusCode() != null) {
            return RestAssured.given()
                .spec(builder.build())
                .expect().statusCode(isOneOf(requestEntity.statusCode())).request()
                .redirects().follow(requestEntity.followRedirection())
                .log()
                .all();
        }

        return RestAssured.given()
            .spec(builder.build())

            .redirects().follow(requestEntity.followRedirection())
            .log()
            .all();
    }

    private boolean ignoreSslCheck() {
        return !StringUtils.isEmpty(System.getProperty("ignoreSslCheck")) && Boolean.parseBoolean(System.getProperty("ignoreSslCheck"));
    }

    // future: This is for future API support where we could have external API which user can call, get auth token and use end-points
    private String getAuthToken() {

        UserAuthenticationEntity userAuthenticationEntity = requestEntity.userAuthenticationEntity();

        if (requestEntity.token() != null) {
            return requestEntity.token();
        }

        if (authTokens.get(userAuthenticationEntity.getEmailAddress()) == null) {
            logger.info("Missing auth id for: " + userAuthenticationEntity.getEmailAddress());
            RequestEntity authEntity = RequestEntityUtil.initBuilder(AuthEndpointEnum.POST_AUTH, AuthenticateJSON.class)
                .xwwwwFormUrlEncoded(
                    AuthorizationFormUtil.getDefaultAuthorizationForm(requestEntity.userAuthenticationEntity().getEmailAddress(),
                        requestEntity.userAuthenticationEntity().getPassword()
                ))
                .autoLogin(false)
                .followRedirection(false)
                .build();

            String authToken =
                ((AuthenticateJSON) HTTP2Request.build(authEntity).post()
                    .getResponseEntity()
                ).getAccessToken();

            authTokens.put(requestEntity.userAuthenticationEntity().getEmailAddress(), authToken);
        }

        return authTokens.get(requestEntity.userAuthenticationEntity().getEmailAddress());

    }

    private <T> ResponseWrapper<T> resultOf(ValidatableResponse response) {

        final int responseCode = response.extract().statusCode();
        final String responseBody = response.extract().body().asString();
        final Headers responseHeaders = response.extract().headers();

        if (returnType != null) {
            String schemaLocation;

            try {
                schemaLocation = returnType.getAnnotation(Schema.class).location();
            } catch (NullPointerException ex) {
                throw new RuntimeException(String.format("Your returnType %s is not annotated with @Schema annotation", returnType.getName()));
            }

            final URL resource = Thread.currentThread().getContextClassLoader().getResource(CommonConstants.schemaBasePath + schemaLocation);
            if (resource == null) {
                throw new RuntimeException(
                    String.format("%s has an invalid resource location in its @Schema notation (hint, check the path of the file inside the resources folder)",
                        returnType.getName()));
            }

            ObjectMapper objectMapper = new Jackson2Mapper(((type, charset) ->
                new com.apriori.utils.http.builder.dao.ObjectMapper())
            );

            T responseEntity = response.assertThat()
                .body(matchesJsonSchema(resource))
                .extract()
                .response()
                .as((Type) returnType, objectMapper);

            return ResponseWrapper.build(responseCode, responseHeaders, responseBody, responseEntity);
        } else {
            return ResponseWrapper.build(responseCode, responseHeaders, responseBody, null);
        }
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP GET method
     *
     * @return JSON POJO object instance of @returnType
     */
    public <T> ResponseWrapper<T> get() {
        return resultOf(
            createRequestSpecification()
                .when()
                .relaxedHTTPSValidation()
                .get(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP POST method
     *
     * @return JSON POJO object instance of @returnType
     */
    public <T> ResponseWrapper<T> post() {
        return resultOf(
            createRequestSpecification()
                .when()
                .relaxedHTTPSValidation()
                .post(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP POST method
     * As a request it sends {@link MultiPartFiles} instead of {@link PayloadJSON}
     *
     * @return JSON POJO object instance of @returnType
     */
    public <T> ResponseWrapper<T> postMultiPart() {
        return resultOf(

            createRequestSpecification()
                .given()

                .config(
                    RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data",
                        ContentType.TEXT)))
                .relaxedHTTPSValidation()
                .expect()
                .when()
                .post(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP PUT method
     *
     * @return JSON POJO object instance of @returnType
     */
    public <T> ResponseWrapper<T> put() {
        return resultOf(
            createRequestSpecification()
                .when()
                .relaxedHTTPSValidation()
                .put(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP PATCH method
     *
     * @return JSON POJO object instance of @returnType
     */
    public <T> ResponseWrapper<T> patch() {
        return resultOf(
            createRequestSpecification()
                .when()
                .relaxedHTTPSValidation()
                .patch(requestEntity.buildEndpoint())
                .then()
                .log()
                .all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP DELETE method
     *
     * @return JSON POJO object instance of @returnType
     */
    public <T> ResponseWrapper<T> delete() {
        return resultOf(
            createRequestSpecification()
                .when()
                .relaxedHTTPSValidation()
                .delete(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }
}