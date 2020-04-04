package com.apriori.utils.http.builder.dao;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.isOneOf;

import com.apriori.utils.Util;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.utils.http.builder.common.response.common.LoginJSON;
import com.apriori.utils.http.builder.common.response.common.PayloadJSON;
import com.apriori.utils.http.builder.service.HTTPRequest;
import com.apriori.utils.http.builder.service.RequestAreaClearRequest;
import com.apriori.utils.http.builder.service.RequestInitService;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.http.enums.common.api.AuthEndpointEnum;
import com.apriori.utils.http.enums.common.api.CommonEndpointEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http.utils.URLParams;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ConnectionManager} class has the following purposes:
 * - Holds your request parameters what you can set up using {@link RequestInitService} class.
 * - Creates {@link RequestSpecification}
 * - Connects to desired endpoint using
 * GET ({@link #get()}), POST ({@link #post()}), PUT ({@link #put()}), PATCH ({@link #patch()}) and DELETE ({@link #delete()}) methods
 * - Converts response JSON into the desired POJO object
 */
public class ConnectionManager<T> {

    private static Map<String, String> sessionIds = new ConcurrentHashMap<>();
    private static Map<String, String> authTokens = new ConcurrentHashMap<>();
    private Class<T> returnType;


    private RequestEntity requestEntity;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(RequestEntity requestEntity, Class<T> returnType) {
        this.requestEntity = requestEntity;
        this.returnType = returnType;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.urlEncodingEnabled = requestEntity.isUrlEncodingEnabled();

    }

    private RequestSpecification createRequestSpecification() {
        RequestSpecBuilder builder = new RequestSpecBuilder();

        List<Map<String, ?>> urlParams = requestEntity.getUrlParams();
        MultiPartFiles multiPartFiles = requestEntity.getMultiPartFiles();
        FormParams formParams = requestEntity.getFormParams();

        if (requestEntity.isAutoLogin()) {
            requestEntity.setHeaders(Util.getTokenAuthorizationForm(this.getAuthToken()));
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

        if (requestEntity.getHeaders() != null) {
            builder.addHeaders(requestEntity.getHeaders());
        }

        if (requestEntity.getXwwwwFormUrlEncoded() != null && !requestEntity.getXwwwwFormUrlEncoded().isEmpty()) {
            builder.setContentType(ContentType.URLENC);
            requestEntity.getXwwwwFormUrlEncoded().forEach(builder::addFormParams);
        }

        if (requestEntity.getCustomBody() != null) {
            builder.setBody(requestEntity.getCustomBody());
        }

        if (requestEntity.getBody() != null) {
            builder.setBody(requestEntity.getBody(), ObjectMapperType.JACKSON_2);
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
        builder.setConfig(RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", requestEntity.getConnectionTimeout())
                        .setParam("http.socket.timeout", requestEntity.getSocketTimeout())
        ))
                .setBaseUri(requestEntity.buildEndpoint());


        if (requestEntity.getStatusCode() != null) {
            return RestAssured.given()
                    .spec(builder.build())
                    .expect().statusCode(isOneOf(requestEntity.getStatusCode())).request()
                    .redirects().follow(requestEntity.isFollowRedirection())
                    .log()
                    .all();
        }

        return  RestAssured.given()
                    .spec(builder.build())
                    .redirects().follow(requestEntity.isFollowRedirection())
                    .log()
                    .all();
    }

    private String getSessionId() {
        if (requestEntity.getDriver() != null) {
            StringBuilder cookie = new StringBuilder();
            Set<Cookie> allCookies = requestEntity.getDriver().manage().getCookies();
            for (Cookie c : allCookies) {
                cookie.append(c.getName()).append("=").append(c.getValue()).append(";");
            }
            return cookie.toString();
        } else {
            if (sessionIds.get(requestEntity.getUserAuthenticationEntity().getEmailAddress()) == null) {
                logger.info("Missing session id for: " + requestEntity.getUserAuthenticationEntity().getEmailAddress());

                UserAuthenticationEntity userAuthenticationEntity = requestEntity.getUserAuthenticationEntity();

                String sessionId = LoginJSON.class.cast(
                        new HTTPRequest().userAuthorizationData(userAuthenticationEntity.getEmailAddress(), userAuthenticationEntity.getPassword())
                                .customizeRequest()
                                .setEndpoint(CommonEndpointEnum.POST_SESSIONID)
                                .setAutoLogin(false)
                                .setStatusCode(HttpStatus.SC_OK)
                                .setUrlParams(URLParams.params()
                                        .use("username", requestEntity.getUserAuthenticationEntity().getEmailAddress())
                                        .use("password", requestEntity.getUserAuthenticationEntity().getPassword()))
                                .setReturnType(LoginJSON.class)
                                .commitChanges()
                                .connect()
                                .post()
                ).getSessionId();
                sessionIds.put(requestEntity.getUserAuthenticationEntity().getEmailAddress(), sessionId);
            }
            return sessionIds.get(requestEntity.getUserAuthenticationEntity().getEmailAddress());
        }
    }

    // future: This is for future API support where we could have external API which user can call, get auth token and use end-points
    private String getAuthToken() {

        UserAuthenticationEntity userAuthenticationEntity = requestEntity.getUserAuthenticationEntity();

        if (authTokens.get(userAuthenticationEntity.getEmailAddress()) == null) {
            logger.info("Missing auth id for: " + userAuthenticationEntity.getEmailAddress());
            RequestEntity authEntity = RequestEntity
                    .initDefaultFormAuthorizationData(requestEntity.getUserAuthenticationEntity().getEmailAddress(),
                            requestEntity.getUserAuthenticationEntity().getPassword()
                    )
                    .setReturnType(AuthenticateJSON.class)
                    .setEndpoint(AuthEndpointEnum.POST_AUTH)
                    .setAutoLogin(false)
                    .setFollowRedirection(false);

            String authToken =
                    ((AuthenticateJSON) GenericRequestUtil.post(authEntity, new RequestAreaClearRequest())
                            .getResponseEntity()
                    ).getAccessToken();

            authTokens.put(requestEntity.getUserAuthenticationEntity().getEmailAddress(), authToken);
        }

        return authTokens.get(requestEntity.getUserAuthenticationEntity().getEmailAddress());

    }

    private UserAuthenticationEntity initUserConnectionData(final String username, final String password) {
        return new UserAuthenticationEntity(
                username,
                password,
                null,
                "password",
                "apriori-web-cost",
                "donotusethiskey",
                "tenantGroup%3Ddefault%20tenant%3Ddefault",
                false
        );
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

            final URL resource = Thread.currentThread().getContextClassLoader().getResource(Constants.schemaBasePath + schemaLocation);
            if (resource == null) {
                throw new RuntimeException(
                        String.format("%s has an invalid resource location in its @Schema notation (hint, check the path of the file inside the resources folder)",
                                returnType.getName()));
            }

            T responseEntity = response.assertThat()
                    .body(matchesJsonSchema(resource))
                    .extract()
                    .response()
                    .as((Type) returnType);

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
                        .put(requestEntity.buildEndpoint())
                        .then()
                        .log().all()
        );
    }

    public <T> ResponseWrapper<T> patch() {
        return resultOf(
                createRequestSpecification()
                        .when()
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
                        .delete(requestEntity.buildEndpoint())
                        .then()
                        .log().all()
        );
    }
}

