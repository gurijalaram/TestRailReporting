package main.java.http.builder.common.entity;

import main.java.http.builder.common.response.common.PayloadJSON;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.builder.service.RequestInitService;
import main.java.http.enums.EndpointEnum;
import main.java.http.enums.EndpointType;
import main.java.http.enums.common.ExternalEndpointEnum;
import main.java.http.enums.common.InternalEndpointEnum;
import main.java.utils.MultiPartFiles;
import main.java.utils.Util;
import org.apache.http.HttpStatus;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity class which is designed to contain and transfer data for Internal API request.
 * You can setup:
 * {@link #endpoint}           -   internal api endpoint
 * {@link #statusCode}         -   expected response status code
 * {@link #urlParams}          -   inline url params formatted in the following way: ?param1=value1&param2=value2
 * {@link #inlineVariables}    -   inline variables.
 * {@link #payloadJSON}        -   payload JSON what you want to send in the request
 */
public class RequestEntity {

    private WebDriver driver;
    private UserAuthenticationEntity userAuthenticationEntity;
    private RequestInitService requestInitService;


    private EndpointEnum endpoint;
    private Integer[] statusCode = {HttpStatus.SC_OK};
    private boolean useCookie = false;
    private boolean autoLogin = true;
    private boolean defaultAuthorizationData = false;
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

    public static RequestEntity initDefaultFormAuthorizationData(final String username, final String password) {
        return new RequestEntity(new UserAuthenticationEntity(username, password), null, true, true);
    }

    public static RequestEntity initCustomFormAuthorizationData(UserAuthenticationEntity userAuthenticationEntity) {
        return new RequestEntity(userAuthenticationEntity, null, false, true);
    }

    public static RequestEntity initSessionAuthorizationData(UserAuthenticationEntity userAuthenticationEntity, WebDriver driver) {
        return new RequestEntity(userAuthenticationEntity, driver);
    }

    public static RequestEntity initUrlAuthorizationData(final String username, final String password) {
        return new RequestEntity(new UserAuthenticationEntity(username, password), null);
    }

    public static RequestEntity unAuthorized() {
        return new RequestEntity(new UserAuthenticationEntity(), null).setAutoLogin(false);
    }

    private RequestEntity(UserAuthenticationEntity userAuthenticationEntity, WebDriver driver, boolean defaultAuthorizationData, boolean useFormData) {
        this.userAuthenticationEntity = userAuthenticationEntity;
        this.driver = driver;
        this.defaultAuthorizationData = defaultAuthorizationData;

        if(useFormData) {
            this.initFormUrlUserData();
        }

    }

    private RequestEntity(UserAuthenticationEntity userAuthenticationEntity, WebDriver driver) {
        this.userAuthenticationEntity = userAuthenticationEntity;
        this.driver = driver;
    }

    private List<Map<String, ?>> initFormUrlUserData() {
        if (this.defaultAuthorizationData) {

            return this.initDefaultFormAuthorization();
        }

        return this.initCustomFormAuthorization();
    }

    private List<Map<String, ?>> initDefaultFormAuthorization() {
        this.xwwwwFormUrlEncoded.add(Util.getDefaultAuthorizationForm(this.userAuthenticationEntity.getEmailAddress(), this.userAuthenticationEntity.getPassword()));

        return this.xwwwwFormUrlEncoded;
    }

    private List<Map<String, ?>> initCustomFormAuthorization() {
        this.addXwwwwFormUrlEncoded(new HashMap<String, String>() {{
            put("grant_type", userAuthenticationEntity.getGrant_type());
            put("username", userAuthenticationEntity.getEmailAddress());
            put("password", userAuthenticationEntity.getPassword());
            put("client_id", userAuthenticationEntity.getClient_id());
            put("client_secret", userAuthenticationEntity.getClient_secret());
            put("scope", userAuthenticationEntity.getScope());
        }});

        return this.xwwwwFormUrlEncoded;
    }


    public WebDriver getDriver() {
        return driver;
    }

    public RequestEntity setDriver(WebDriver driver) {
        this.driver = driver;
        return this;
    }

    public UserAuthenticationEntity getUserAuthenticationEntity() {
        return userAuthenticationEntity;
    }

    public RequestEntity setUserAuthenticationEntity(UserAuthenticationEntity userAuthenticationEntity) {
        this.userAuthenticationEntity = userAuthenticationEntity;
        return this;
    }

    public EndpointEnum getEndpoint() {
        return endpoint;
    }

//    public RequestEntity setCustomEndpoint(final String url, EndpointType endpointType) {
//        this.end
//    }


    public RequestEntity setEndpoint(EndpointEnum endpoint) {
        this.endpoint = endpoint;
        if (this.endpoint instanceof InternalEndpointEnum) {
            this.endpointType = EndpointType.INTERNAL;
        } else if (this.endpoint instanceof ExternalEndpointEnum) {
            this.endpointType = EndpointType.EXTERNAL;
        }
        return this;
    }

    public RequestInitService getRequestInitService() {
        return requestInitService;
    }

    public Integer[] getStatusCode() {
        return statusCode;
    }

    public RequestEntity setStatusCode(Integer... statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public boolean isUseCookie() {
        return useCookie;
    }

    public RequestEntity setUseCookie(boolean useCookie) {
        this.useCookie = useCookie;
        return this;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public RequestEntity setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
        return this;
    }

    public boolean isDefaultAuthorizationData() {
        return defaultAuthorizationData;
    }

    public RequestEntity setDefaultAuthorizationData(boolean defaultAuthorizationData) {
        this.defaultAuthorizationData = defaultAuthorizationData;
        return this;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public RequestEntity setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
        return this;
    }

    public boolean isFollowRedirection() {
        return followRedirection;
    }

    public RequestEntity setFollowRedirection(boolean followRedirection) {
        this.followRedirection = followRedirection;
        return this;
    }

    public List<Map<String, ?>> getUrlParams() {
        return urlParams;
    }

    public RequestEntity setUrlParams(Map<String, ?> urlParams) {
        if (urlParams != null) {
            this.urlParams.add(urlParams);
        }
        return this;
    }

    public RequestEntity addUrlParam(Map<String, ?> urlParams) {
        if (urlParams != null) {
            this.urlParams.add(urlParams);
        }
        return this;
    }


    public List<Map<String, ?>> getXwwwwFormUrlEncoded() {
        return xwwwwFormUrlEncoded;
    }

    public RequestEntity setXwwwwFormUrlEncoded(List<Map<String, ?>> xwwwwFormUrlEncoded) {
        if (xwwwwFormUrlEncoded != null) {
            this.xwwwwFormUrlEncoded = xwwwwFormUrlEncoded;
        }

        return this;
    }

    public RequestEntity addXwwwwFormUrlEncoded(Map<String, ?> xwwwwFormUrlEncoded) {
        if (xwwwwFormUrlEncoded != null) {
            this.xwwwwFormUrlEncoded.add(xwwwwFormUrlEncoded);
        }
        return this;
    }


    public Object[] getInlineVariables() {
        return inlineVariables;
    }

    public RequestEntity setInlineVariables(Object... inlineVariables) {
        this.inlineVariables = inlineVariables;
        return this;
    }

    public MultiPartFiles getMultiPartFiles() {
        return multiPartFiles;
    }

    public RequestEntity setMultiPartFiles(MultiPartFiles multiPartFiles) {
        this.multiPartFiles = multiPartFiles;
        return this;
    }

    public PayloadJSON getPayloadJSON() {
        return payloadJSON;
    }

    public RequestEntity setPayloadJSON(PayloadJSON payloadJSON) {
        this.payloadJSON = payloadJSON;
        return this;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public RequestEntity setReturnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public RequestEntity setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public RequestEntity setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public RequestEntity setRequestInitService(RequestInitService requestInitService) {
        this.requestInitService = requestInitService;
        return this;
    }

    public RequestInitService commitChanges() {
        return requestInitService;
    }

    public String buildEndpoint() {
        if (inlineVariables != null) {
            return endpoint.getEndpoint(inlineVariables);
        } else {
            return endpoint.getEndpoint();
        }
    }
}
