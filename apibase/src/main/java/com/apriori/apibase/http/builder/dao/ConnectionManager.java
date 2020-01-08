package com.apriori.apibase.http.builder.dao;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.isOneOf;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.common.response.common.LoginJSON;
import com.apriori.apibase.http.builder.common.response.common.PayloadJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.builder.service.RequestInitService;
import com.apriori.apibase.http.enums.Schema;
import com.apriori.apibase.http.enums.common.api.AuthEndpointEnum;
import com.apriori.apibase.http.enums.common.api.CommonEndpointEnum;
import com.apriori.apibase.utils.MultiPartFiles;
import com.apriori.apibase.utils.URLParams;
import com.apriori.utils.constants.Constants;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ConnectionManager} class has the following purposes:
 * - Holds your request parameters what you can set up using {@link RequestInitService} class.
 * - Creates {@link RequestSpecification} using {{@link #createRequestSpecification(List, PayloadJSON)}}
 * - Connects to desired endpoint using
 * GET ({@link #get()}), POST ({@link #post()}), PUT ({@link #put()}), PATCH ({@link #patch()}) and DELETE ({@link #delete()}) methods
 * - Converts response JSON into the desired POJO object
 */
//INFO z: Now ALL operations are going with requestEntity....
// because of this, when somebody now all request data, he may write e.g new ConnectionManager<Response>(requestEntity, returnType).post() ;
// because of this ConnectionManager has not references to another objects and all that we need is only RequestEntity
public class ConnectionManager<T> {

    private static Map<String, String> sessionIds = new ConcurrentHashMap<>();
    private static Map<String, String> authTokens = new ConcurrentHashMap<>();
    Class<T> returnType;


    private RequestEntity requestEntity;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(RequestEntity requestEntity, Class<T> returnType) {
        this.requestEntity = requestEntity;
        this.returnType = returnType;
        RestAssured.defaultParser = Parser.JSON;
    }

    private RequestSpecification createRequestSpecification(List<Map<String, ?>> urlParams, Object body, String customBody) {
        return createRequestSpecification(urlParams, body, customBody, null);
    }

    private RequestSpecification createRequestSpecification(List<Map<String, ?>> urlParams, Object body) {

        return createRequestSpecification(urlParams, body, null);
    }

    private RequestSpecification createRequestSpecification(List<Map<String, ?>> urlParams, MultiPartFiles multiPartFiles) {

        return createRequestSpecification(urlParams, null, null, multiPartFiles);
    }

    private RequestSpecification createRequestSpecification(List<Map<String, ?>> urlParams, Object body, String customBody, MultiPartFiles multiPartFiles) {
        RequestSpecBuilder builder = new RequestSpecBuilder();

        if (requestEntity.isAutoLogin()) {
            switch (requestEntity.getEndpointType()) {
                case INTERNAL:
                    if (requestEntity.isUseCookie()) {
                        builder.setSessionId(getSessionId());
                    }
                    break;
                case EXTERNAL:
                    String authToken = setAuthToken();
                    // future: This is for future API support where we could have external API which user can call, get auth token and use end-points
                    //TODO handle null
                    Map<String, String> auth = new HashMap<>();
                    auth.put("auth", authToken);
                    urlParams.add(auth);
                    break;
                default:
                    //TODO handle default;
            }
        }

        if (multiPartFiles != null) {
            builder.setContentType("multipart/form-data");
        } else {
            builder.setContentType(ContentType.JSON);
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

        if (customBody != null) {
            builder.setBody(customBody);
        }

        if (body != null) {
            builder.setBody(body, ObjectMapperType.JACKSON_2);
        }

        if (multiPartFiles != null) {
            multiPartFiles.forEach(builder::addMultiPart);
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

        return RestAssured.given()
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
    private String setAuthToken() {

        UserAuthenticationEntity userAuthenticationEntity = requestEntity.getUserAuthenticationEntity();

        if (authTokens.get(userAuthenticationEntity.getEmailAddress()) == null) {
            logger.info("Missing auth id for: " + userAuthenticationEntity.getEmailAddress());

            String authToken = AuthenticateJSON.class.cast(
                new HTTPRequest().defaultFormAuthorization(userAuthenticationEntity.getEmailAddress(), userAuthenticationEntity.getPassword())
                    .customizeRequest()
                    .setEndpoint(AuthEndpointEnum.POST_AUTH)
                    .setAutoLogin(false)
                    .setFollowRedirection(false)
                    .setReturnType(AuthenticateJSON.class)
                    .commitChanges()
                    .connect()
                    .post()
            ).getAccessToken();

            authTokens.put(requestEntity.getUserAuthenticationEntity().getEmailAddress(), authToken);
        }

        return authTokens.get(requestEntity.getUserAuthenticationEntity().getEmailAddress());

    }

    private T resultOf(ValidatableResponse response) {

        if (returnType != null) {

            if (returnType.isAssignableFrom(Boolean.class)) {
                return response.extract().response().as(returnType);
            }

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

            return response.assertThat().body(matchesJsonSchema(resource))
                .extract()
                .response().as(returnType);
        }
        return null;
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP GET method
     *
     * @return JSON POJO object instance of @returnType
     */
    public T get() {


        return resultOf(
            createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody(), requestEntity.getCustomBody())
                .expect()
                .statusCode(isOneOf(requestEntity.getStatusCode()))
                .when()
                .get(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP GET method
     *
     * @return raw JSON string
     */
    public String getJSON() {
        return createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
            .expect()
            .statusCode(isOneOf(requestEntity.getStatusCode()))
            .when()
            .get(requestEntity.buildEndpoint())
            .asString();
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP GET method
     *
     * @return Headers object instance from response
     */
    public Headers getHeader() {

        return createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
            .expect()
            .statusCode(isOneOf(requestEntity.getStatusCode()))
            .when()
            .get(requestEntity.buildEndpoint()).headers();
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP POST method
     *
     * @return JSON POJO object instance of @returnType
     */
    public T post() {
        return resultOf(
            createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody(), requestEntity.getCustomBody())
                .expect()
                .statusCode(isOneOf(requestEntity.getStatusCode()))
                .when()
                .post(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP POST method
     *
     * @return Headers object instance from response
     */
    public Headers postHeader() {
        return createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
            .expect()
            .statusCode(isOneOf(requestEntity.getStatusCode()))
            .when()
            .post(requestEntity.buildEndpoint()).headers();
    }

    /**
     * gets header from PATCH request
     *
     * @return header
     */
    public Headers patchHeader() {
        return createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
            .expect()
            .statusCode(isOneOf(requestEntity.getStatusCode()))
            .when()
            .patch(requestEntity.buildEndpoint())
            .headers();
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP POST method
     * As a request it sends {@link MultiPartFiles} instead of {@link PayloadJSON}
     *
     * @return JSON POJO object instance of @returnType
     */
    public T postMultiPart() {
        return resultOf(
            createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getMultiPartFiles())
                .expect()
                .statusCode(isOneOf(requestEntity.getStatusCode()))
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
    public T put() {
        return resultOf(
            createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
                .expect()
                .statusCode(isOneOf(requestEntity.getStatusCode()))
                .when()
                .put(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }

    public T patch() {
        return resultOf(
            createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
                .expect()
                .statusCode(isOneOf(requestEntity.getStatusCode()))
                .when()
                .patch(requestEntity.buildEndpoint())
                .then()
                .log()
                .all()
        );
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP PUT method
     *
     * @return Headers object instance from response
     */
    public Headers putHeader() {
        return createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
            .expect()
            .statusCode(isOneOf(requestEntity.getStatusCode()))
            .when()
            .put(requestEntity.buildEndpoint()).headers();
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP DELETE method
     *
     * @return JSON POJO object instance of @returnType
     */
    public T delete() {
        return resultOf(
            createRequestSpecification(requestEntity.getUrlParams(), requestEntity.getBody())
                .expect()
                .statusCode(isOneOf(requestEntity.getStatusCode()))
                .when()
                .delete(requestEntity.buildEndpoint())
                .then()
                .log().all()
        );
    }
}