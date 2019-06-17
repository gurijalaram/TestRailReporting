package main.java;

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
import main.java.enums.EndpointEnum;
import main.java.enums.EndpointType;
import main.java.enums.Schema;
import main.java.enums.common.AuthEndpointEnum;
import main.java.enums.common.CommonEndpointEnum;
import main.java.enums.common.ExternalEndpointEnum;
import main.java.enums.common.InternalEndpointEnum;
import main.java.pojo.common.AuthenticateJSON;
import main.java.pojo.common.LoginJSON;
import main.java.pojo.common.PayloadJSON;
import org.apache.http.HttpStatus;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.isOneOf;

/**
 * {@link ConnectionManager} class has the following purposes:
 * - Holds your request parameters what you can set up using {@link ConnectionManagerBuilder} class.
 * - Creates {@link RequestSpecification} using {{@link #createRequestSpecification(List, PayloadJSON)}}
 * - Connects to desired endpoint using
 * GET ({@link #get()}), POST ({@link #post()}), PUT ({@link #put()}), PATCH ({@link #patch()}) and DELETE ({@link #delete()}) methods
 * - Converts response JSON into the desired POJO object
 */
public class ConnectionManager<T> {

    private static Map<String, String> sessionIds = new ConcurrentHashMap<>();
    private static Map<String, String> authTokens = new ConcurrentHashMap<>();

    private ConnectionClass connectionClass;
    private String endpoint;
    private Integer[] statusCode;
    private EndpointType endpointType;
    private boolean autoLogin;
    private boolean useCookie;
    private boolean followRedirection;
    private List<Map<String, ?>> urlParams;
    private List<Map<String, ?>> xwwwwFormUrlEncoded;
    private MultiPartFiles multiPartFiles;
    private PayloadJSON payloadJSON;
    private Class<T> returnType;

    private int connectionTimeout;
    private int socketTimeout;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(ConnectionClass connectionClass,
                             String endpoint,
                             int[] statusCode,
                             boolean autoLogin,
                             boolean useCookie,
                             EndpointType endpointType,
                             boolean followRedirection,
                             List<Map<String, ?>> urlParams,
                             List<Map<String, ?>> xwwwwFormUrlEncoded,
                             MultiPartFiles multiPartFiles,
                             PayloadJSON payloadJSON,
                             Class<T> returnType,
                             int connectionTimeout,
                             int socketTimeout) {
        this.connectionClass = connectionClass;
        this.endpoint = endpoint;
        this.statusCode = Arrays.stream(statusCode).boxed().toArray(Integer[]::new);
        this.useCookie = useCookie;
        this.autoLogin = autoLogin;
        this.endpointType = endpointType;
        this.followRedirection = followRedirection;
        this.urlParams = urlParams;
        this.xwwwwFormUrlEncoded = xwwwwFormUrlEncoded;
        this.multiPartFiles = multiPartFiles;
        this.payloadJSON = payloadJSON;
        this.returnType = returnType;

        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;

        RestAssured.defaultParser = Parser.JSON;
    }

    /**
     * Just a builder class which is designed to make you life easier to setup your Internal API endpoint request.
     * You can setup:
     * {@link #endpoint}           -   internal api endpoint
     * {@link #statusCode}         -   expected response status code
     * {@link #urlParams}          -   inline url params formatted in the following way: ?param1=value1&param2=value2
     * {@link #inlineVariables}    -   inline variables.
     * {@link #payloadJSON}        -   payload JSON what you want to send in the request
     */
    public static class ConnectionManagerBuilder {
        private ConnectionClass connectionClass;
        private EndpointEnum endpoint;
        private int[] statusCode = {HttpStatus.SC_OK};
        private boolean useCookie = false;
        private boolean autoLogin = true;
        private EndpointType endpointType;
        private boolean followRedirection = true;
        private List<Map<String, ?>> urlParams = new ArrayList<>();
        private List<Map<String, ?>> xwwwwFormUrlEncoded = new ArrayList<>();
        private Object[] inlineVariables;
        private MultiPartFiles multiPartFiles;
        private PayloadJSON payloadJSON;
        private Class<?> returnType;
        private int connectionTimeout = 60000;
        private int socketTimeout = 60000;

        private ConnectionManagerBuilder(ConnectionClass connectionClass) {
            this.connectionClass = connectionClass;
        }

        private ConnectionManagerBuilder(boolean useCookie) {
            this.useCookie = useCookie;
        }

        public static ConnectionManagerBuilder builder(ConnectionClass connectionClass) {
            return new ConnectionManagerBuilder(connectionClass);
        }

        public static ConnectionManagerBuilder builder() {
            return new ConnectionManagerBuilder(false);
        }

        public ConnectionManagerBuilder endpoint(EndpointEnum endpoint) {
            this.endpoint = endpoint;
            if (this.endpoint instanceof InternalEndpointEnum) {
                this.endpointType = EndpointType.INTERNAL;
            } else if (this.endpoint instanceof ExternalEndpointEnum) {
                this.endpointType = EndpointType.EXTERNAL;
            }
            return this;
        }

        public ConnectionManagerBuilder statusCode(int... statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ConnectionManagerBuilder useCookie(boolean useCookie) {
            this.useCookie = useCookie;
            return this;
        }

        public ConnectionManagerBuilder useAutoLogin(boolean autoLogin) {
            this.autoLogin = autoLogin;
            return this;
        }

        public ConnectionManagerBuilder followRedirection(boolean followRedirection) {
            this.followRedirection = followRedirection;
            return this;
        }

        public ConnectionManagerBuilder urlParam(Map<String, ?> urlParams) {
            if (urlParams != null) {
                this.urlParams.add(urlParams);
            }
            return this;
        }

        public ConnectionManagerBuilder xwwwwFormUrlEncoded(Map<String, ?> xwwwwFormUrlEncoded) {
            if (xwwwwFormUrlEncoded != null) {
                this.xwwwwFormUrlEncoded.add(xwwwwFormUrlEncoded);
            }
            return this;
        }

        public ConnectionManagerBuilder inlineVariables(Object... inlineVariables) {
            this.inlineVariables = inlineVariables;
            return this;
        }

        public ConnectionManagerBuilder multiPart(MultiPartFiles multiPartFiles) {
            this.multiPartFiles = multiPartFiles;
            return this;
        }

        public ConnectionManagerBuilder payloadJSON(PayloadJSON payloadJSON) {
            this.payloadJSON = payloadJSON;
            return this;
        }

        public ConnectionManagerBuilder returnType(Class<?> returnType) {
            this.returnType = returnType;
            return this;
        }

        public ConnectionManagerBuilder setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public ConnectionManagerBuilder setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        private String buildEndpoint() {
            if (inlineVariables != null) {
                return endpoint.getEndpoint(inlineVariables);
            } else {
                return endpoint.getEndpoint();
            }
        }

        public ConnectionManager<?> connect() {
            return new ConnectionManager<>(
                this.connectionClass,
                this.buildEndpoint(),
                this.statusCode,
                this.autoLogin,
                this.useCookie,
                this.endpointType,
                this.followRedirection,
                this.urlParams,
                this.xwwwwFormUrlEncoded,
                this.multiPartFiles,
                this.payloadJSON,
                this.returnType,
                this.connectionTimeout,
                this.socketTimeout
            );
        }
    }

    private RequestSpecification createRequestSpecification(List<Map<String, ?>> urlParams, PayloadJSON payloadJson) {
        return createRequestSpecification(urlParams, payloadJson, null);
    }

    private RequestSpecification createRequestSpecification(List<Map<String, ?>> urlParams, MultiPartFiles multiPartFiles) {
        return createRequestSpecification(urlParams, null, multiPartFiles);
    }

    private RequestSpecification createRequestSpecification(List<Map<String, ?>> urlParams, PayloadJSON payloadJson, MultiPartFiles multiPartFiles) {
        RequestSpecBuilder builder = new RequestSpecBuilder();

        if (autoLogin) {
            switch (endpointType) {
                case INTERNAL:
                    if (useCookie) {
                        builder.setSessionId(getSessionId());
                    }
                    break;
                case EXTERNAL:
                    String authToken = setAuthToken(); // future: This is for future API support where we could have external API which user can call, get auth token and use end-points
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

        if (xwwwwFormUrlEncoded != null && !xwwwwFormUrlEncoded.isEmpty()) {
            builder.setContentType(ContentType.URLENC);
            xwwwwFormUrlEncoded.forEach(builder::addFormParams);
        }

        if (payloadJson != null) {
            builder.setBody(payloadJson, ObjectMapperType.JACKSON_2);
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
                .setParam("http.connection.timeout", connectionTimeout)
                .setParam("http.socket.timeout", socketTimeout)
        ));

        return RestAssured.given()
            .spec(builder.build())
            .redirects().follow(followRedirection)
            .log()
            .all();
    }

    private String getSessionId() {
        if (connectionClass.getDriver() != null) {
            StringBuilder cookie = new StringBuilder();
            Set<Cookie> allCookies = connectionClass.getDriver().manage().getCookies();
            for (Cookie c : allCookies) {
                cookie.append(c.getName()).append("=").append(c.getValue()).append(";");
            }
            return cookie.toString();
        } else {
            if (sessionIds.get(connectionClass.getUserForAPIConnection().getEmailAddress()) == null) {
                logger.info("Missing session id for: " + connectionClass.getUserForAPIConnection().getEmailAddress());

                String sessionId = LoginJSON.class.cast(
                    ConnectionManager.ConnectionManagerBuilder
                        .builder()
                        .endpoint(CommonEndpointEnum.POST_SESSIONID)
                        .useAutoLogin(false)
                        .urlParam(URLParams.params()
                            .use("username", connectionClass.getUserForAPIConnection().getEmailAddress())
                            .use("password", connectionClass.getUserForAPIConnection().getPassword()))
                        .returnType(LoginJSON.class)
                        .connect()
                        .post()
                ).getSessionId();

                sessionIds.put(connectionClass.getUserForAPIConnection().getEmailAddress(), sessionId);
            }
            return sessionIds.get(connectionClass.getUserForAPIConnection().getEmailAddress());
        }
    }

    // future: This is for future API support where we could have external API which user can call, get auth token and use end-points
    private String setAuthToken() {
        if (authTokens.get(connectionClass.getUserForAPIConnection().getEmailAddress()) == null) {
            logger.info("Missing auth id for: " + connectionClass.getUserForAPIConnection().getEmailAddress());
            String authToken = AuthenticateJSON.class.cast(
                ConnectionManager.ConnectionManagerBuilder
                    .builder()
                    .endpoint(AuthEndpointEnum.POST_AUTH)
                    .urlParam(URLParams.params()
                        .use("username", connectionClass.getUserForAPIConnection().getEmailAddress())
                        .use("password", connectionClass.getUserForAPIConnection().getPassword()))
                    .useAutoLogin(false)
                    .returnType(AuthenticateJSON.class)
                    .connect()
                    .post()
            ).getAuth();

            authTokens.put(connectionClass.getUserForAPIConnection().getEmailAddress(), authToken);
        }

        return authTokens.get(connectionClass.getUserForAPIConnection().getEmailAddress());

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

            final URL resource = Thread.currentThread().getContextClassLoader().getResource(schemaLocation);
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
            createRequestSpecification(urlParams, payloadJSON)
                .expect()
                .statusCode(isOneOf(statusCode))
                .when()
                .get(endpoint)
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
        return createRequestSpecification(urlParams, payloadJSON)
            .expect()
            .statusCode(isOneOf(statusCode))
            .when()
            .get(endpoint)
            .asString();
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP GET method
     *
     * @return Headers object instance from response
     */
    public Headers getHeader() {

        return createRequestSpecification(urlParams, payloadJSON)
            .expect()
            .statusCode(isOneOf(statusCode))
            .when()
            .get(endpoint).headers();
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP POST method
     *
     * @return JSON POJO object instance of @returnType
     */
    public T post() {
        return resultOf(
            createRequestSpecification(urlParams, payloadJSON)
                .expect()
                .statusCode(isOneOf(statusCode))
                .when()
                .post(endpoint)
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
        return createRequestSpecification(urlParams, payloadJSON)
            .expect()
            .statusCode(isOneOf(statusCode))
            .when()
            .post(endpoint).headers();
    }

    /**
     * gets header from PATCH request
     * @return header
     */
    public Headers patchHeader() {
        return createRequestSpecification(urlParams, payloadJSON)
            .expect()
            .statusCode(isOneOf(statusCode))
            .when()
            .patch(endpoint)
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
            createRequestSpecification(urlParams, multiPartFiles)
                .expect()
                .statusCode(isOneOf(statusCode))
                .when()
                .post(endpoint)
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
            createRequestSpecification(urlParams, payloadJSON)
                .expect()
                .statusCode(isOneOf(statusCode))
                .when()
                .put(endpoint)
                .then()
                .log().all()
        );
    }

    public T patch() {
        return resultOf(
            createRequestSpecification(urlParams, payloadJSON)
                .expect()
                .statusCode(isOneOf(statusCode))
                .when()
                .patch(endpoint)
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
        return createRequestSpecification(urlParams, payloadJSON)
            .expect()
            .statusCode(isOneOf(statusCode))
            .when()
            .put(endpoint).headers();
    }

    /**
     * Sends request to desired endpoint with the desired specifications using HTTP DELETE method
     *
     * @return JSON POJO object instance of @returnType
     */
    public T delete() {
        return resultOf(
            createRequestSpecification(urlParams, payloadJSON)
                .expect()
                .statusCode(isOneOf(statusCode))
                .when()
                .delete(endpoint)
                .then()
                .log().all()
        );
    }
}