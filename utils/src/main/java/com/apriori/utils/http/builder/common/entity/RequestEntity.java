package com.apriori.utils.http.builder.common.entity;

import com.apriori.utils.AuthorizationFormUtil;
import com.apriori.utils.http.builder.service.RequestInitService;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.enums.EndpointType;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.users.UserCredentials;

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
 */
public class RequestEntity {

    private String token;

    private WebDriver driver;
    private UserAuthenticationEntity userAuthenticationEntity;
    private RequestInitService requestInitService;

    private String customBody;
    private EndpointEnum endpoint;
    private String customEndpoint;
    private Map<String, String> headers = new HashMap<>();
    private Integer[] statusCode;
    private boolean useCookie = false;
    private boolean autoLogin = false;
    private boolean defaultAuthorizationData = false;
    private EndpointType endpointType;
    private boolean followRedirection = false;
    private List<Map<String, ?>> urlParams = new ArrayList<>();
    private List<Map<String, ?>> xwwwwFormUrlEncoded = new ArrayList<>();
    private Object[] inlineVariables;
    private MultiPartFiles multiPartFiles;
    private FormParams formParams;
    private Object body;
    private Class<?> returnType;
    private int connectionTimeout = 60000;
    private int socketTimeout = 60000;
    private boolean urlEncodingEnabled = true;

    public static RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity(null, null)
                .setEndpoint(endpoint)
                .setReturnType(returnType);
    }

    public static RequestEntity init(String url, Class<?> returnType) {
        return new RequestEntity(null, null)
                .setEndpoint(url)
                .setReturnType(returnType);
    }

    public static RequestEntity init(String endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity(new UserAuthenticationEntity(userCredentials.getUsername(), userCredentials.getPassword()), null)
                .setReturnType(returnType)
                .setEndpoint(endpoint);
    }

    public static RequestEntity init(EndpointEnum endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity(new UserAuthenticationEntity(userCredentials.getUsername(), userCredentials.getPassword()), null)
            .setReturnType(returnType)
            .setEndpoint(endpoint);
    }

    public static RequestEntity initWithToken(EndpointEnum endpoint, final String token, Class<?> returnType) {
        return new RequestEntity()
                .setToken(token)
                .setEndpoint(endpoint)
                .setReturnType(returnType);
    }

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

        if (useFormData) {
            this.initFormUrlUserData();
        }

    }

    private RequestEntity(UserAuthenticationEntity userAuthenticationEntity, WebDriver driver) {
        this.userAuthenticationEntity = userAuthenticationEntity;
        this.driver = driver;
    }

    public RequestEntity() {

    }

    private List<Map<String, ?>> initFormUrlUserData() {
        if (this.defaultAuthorizationData) {

            return this.initDefaultFormAuthorization();
        }

        return this.initCustomFormAuthorization();
    }

    private List<Map<String, ?>> initDefaultFormAuthorization() {
        this.xwwwwFormUrlEncoded.add(AuthorizationFormUtil.getDefaultAuthorizationForm(this.userAuthenticationEntity.getEmailAddress(), this.userAuthenticationEntity.getPassword()));

        return this.xwwwwFormUrlEncoded;
    }

    private List<Map<String, ?>> initCustomFormAuthorization() {
        this.addXwwwwFormUrlEncoded(new HashMap<String, String>() {
            {
                put("grant_type", userAuthenticationEntity.getGrantType());
                put("username", userAuthenticationEntity.getEmailAddress());
                put("password", userAuthenticationEntity.getPassword());
                put("client_id", userAuthenticationEntity.getClientId());
                put("client_secret", userAuthenticationEntity.getClientSecret());
                put("scope", userAuthenticationEntity.getScope());
            }
        });

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

    public RequestEntity setEndpoint(String endpoint) {
        this.customEndpoint = endpoint;
        return this;
    }

    public RequestEntity setEndpoint(EndpointEnum endpoint) {
        this.endpoint = endpoint;
        if (this.endpoint instanceof ExternalEndpointEnum) {
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

    public RequestEntity setUrlParams(List<Map<String, ?>> urlParams) {
        this.urlParams = urlParams;
        return this;
    }

    public RequestEntity addUrlParam(Map<String, ?> urlParams) {
        if (urlParams != null) {
            this.urlParams.add(urlParams);
        }
        return this;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestEntity setHeaders(Map<String, String> headers) {
        if (headers != null) {
            this.headers = headers;
        }
        return this;
    }

    public RequestEntity addHeaders(final String key, final String value) {
        if (headers != null) {
            this.headers.put(key, value);
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

    public FormParams getFormParams() {
        return formParams;
    }

    public RequestEntity setFormParams(FormParams formParams) {
        this.formParams = formParams;
        return this;
    }

    public String getCustomBody() {
        return customBody;
    }

    public RequestEntity setCustomBody(String customBody) {
        this.customBody = customBody;
        return this;
    }

    public Object getBody() {
        return body;
    }

    public RequestEntity setBody(Object body) {
        this.body = body;
        return this;
    }

    public RequestEntity setBody(String node, Object body) {
       this.body = new HashMap<String, Object>() {
           {
                put(node, body);
            }
        };
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

        if (this.customEndpoint != null) {
            return this.customEndpoint;
        }

        return this.inlineVariables != null ? endpoint.getEndpoint(inlineVariables) : endpoint.getEndpoint();
    }

    public String getCustomEndpoint() {
        return customEndpoint;
    }

    public void setCustomEndpoint(String customEndpoint) {
        this.customEndpoint = customEndpoint;
    }

    public boolean isUrlEncodingEnabled() {
        return urlEncodingEnabled;
    }

    public RequestEntity setUrlEncodingEnabled(boolean urlEncodingEnabled) {
        this.urlEncodingEnabled = urlEncodingEnabled;
        return this;
    }

    public String getToken() {
        return token;
    }

    public RequestEntity setToken(String token) {
        this.token = token;
        return this;
    }
}
