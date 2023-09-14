package com.apriori.http.models.request;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import com.apriori.annotations.Schema;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.properties.PropertiesContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * {@link ConnectionManager} class has the following purposes:
 * - Creates {@link RequestSpecification}
 * - Connects to desired endpoint using
 * GET ({@link #get()}), POST ({@link #post()}), PUT ({@link #put()}), PATCH ({@link #patch()}) and DELETE ({@link #delete()}) methods
 * - Converts response JSON into the desired POJO object
 */
@Slf4j
class ConnectionManager<T> {

    private static final Boolean IS_JENKINS_BUILD = System.getProperty("mode") != null;

    private Class<T> returnType;
    private RequestEntity requestEntity;

    public ConnectionManager(RequestEntity requestEntity, Class<T> returnType) {
        this.requestEntity = requestEntity;
        this.returnType = returnType;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.urlEncodingEnabled = requestEntity.urlEncodingEnabled();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.BODY);
    }

    private RequestSpecification createRequestSpecification() {
        RequestSpecBuilder builder = new RequestSpecBuilder();

        List<Map<String, ?>> urlParams = requestEntity.urlParams();
        MultiPartFiles multiPartFiles = requestEntity.multiPartFiles();
        QueryParams queryParams = requestEntity.queryParams();

        if (multiPartFiles != null) {
            builder.setContentType("multipart/form-data");
            multiPartFiles.getMultiPartsFileData().forEach(builder::addMultiPart);
            multiPartFiles.getMultiPartsTextData().forEach(builder::addMultiPart);
            multiPartFiles.getMultiPartsFilesData().forEach(builder::addMultiPart);
        } else {
            builder.setContentType(ContentType.JSON);
        }

        if (queryParams != null) {
            queryParams.forEach(builder::addFormParam);
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

        if (requestEntity.apUserContext() != null) {
            builder.addHeader("ap-user-context", requestEntity.apUserContext());
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
        try {
            builder
                .setConfig(RestAssuredConfig.config()
                    .httpClient(
                        HttpClientConfig.httpClientConfig()
                            .setParam("http.connection.timeout", requestEntity.connectionTimeout())
                            .setParam("http.socket.timeout", requestEntity.socketTimeout())
                    )
                    .sslConfig(ignoreSslCheck() ? new SSLConfig().allowAllHostnames() : new SSLConfig())
                )
                .setBaseUri(URLEncoder.encode(requestEntity.buildEndpoint(), StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            log.error("Error with URI" + e.getMessage());
        }

        RequestSpecification requestSpecification = RestAssured.given()
            .spec(builder.build())
            .redirects().follow(requestEntity.followRedirection());

        if (requestEntity.expectedResponseCode() != null) {
            requestSpecification = requestSpecification.expect().statusCode(requestEntity.expectedResponseCode())
                .request();
        }

        return requestSpecification
            .log()
            .all();
    }

    private boolean ignoreSslCheck() {
        return !StringUtils.isEmpty(System.getProperty("ignoreSslCheck")) && Boolean.parseBoolean(System.getProperty("ignoreSslCheck"));
    }

    private <T> ResponseWrapper<T> resultOf(ValidatableResponse response) {

        final int responseCode = response.extract().statusCode();
        final String responseBody = response.extract().body().asString();
        final Headers responseHeaders = response.extract().headers();

        if (returnType != null) {
            Class<InputStream> testClass = InputStream.class;
            if (returnType == testClass) {
                T responseEntity = (T) response.extract().asInputStream();
                return ResponseWrapper.build(responseCode, responseHeaders, responseBody, responseEntity);
            }
            String schemaLocation;

            try {
                schemaLocation = returnType.getAnnotation(Schema.class).location();
            } catch (NullPointerException ex) {
                throw new RuntimeException(String.format("Your returnType %s is not annotated with @Schema annotation", returnType.getName()));
            }

            final URL resource = Thread.currentThread().getContextClassLoader().getResource(PropertiesContext.get("global.schema_base_path") + schemaLocation);
            if (resource == null) {
                throw new RuntimeException(
                    String.format("%s has an invalid resource location in its @Schema notation (hint, check the path of the file inside the resources folder)",
                        returnType.getName()));
            }

            ObjectMapper objectMapper = new Jackson2Mapper(((type, charset) ->
                new com.apriori.http.models.request.ObjectMapper()
//                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            )
            );

            Response extractedResponse = response.assertThat()
                    .body(matchesJsonSchema(resource))
                    .extract()
                    .response();

            T responseEntity;

            try {
                responseEntity = extractedResponse.as((Type) returnType, objectMapper);

            } catch (Exception e) {
                log.error("Response contains MappingException. \n ***Exception message: {} \n ***Response: {}", e.getMessage(), extractedResponse.asPrettyString());

                responseEntity = extractedResponse.as((Type) returnType, new Jackson2Mapper(((type, charset) ->
                        new com.apriori.http.models.request.ObjectMapper()
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                )));

            }

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
     * Sends request to desired endpoint with the desired specifications using HTTP GET method
     *
     * @return JSON POJO object instance of @returnType
     */
    public <T> ResponseWrapper<T> getWithNoLogInfo() {
        return resultOf(
            createRequestSpecification()
                .when()
                .relaxedHTTPSValidation()
                .get(requestEntity.buildEndpoint())
                .then()
                .log().ifError()
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
     * As a request it sends {@link MultiPartFiles}
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