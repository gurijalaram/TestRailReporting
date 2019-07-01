package main.java.service;

import main.java.common.HTTPRequest;
import main.java.common.UserForAPIConnection;
import main.java.dao.ConnectionManager;
import main.java.enums.EndpointEnum;
import main.java.enums.EndpointType;
import main.java.enums.common.ExternalEndpointEnum;
import main.java.enums.common.InternalEndpointEnum;
import main.java.pojo.common.PayloadJSON;
import main.java.utils.MultiPartFiles;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Just a builder class which is designed to make you life easier to setup your Internal API endpoint request.
 * You can setup:
 * {@link #endpoint}           -   internal api endpoint
 * {@link #statusCode}         -   expected response status code
 * {@link #urlParams}          -   inline url params formatted in the following way: ?param1=value1&param2=value2
 * {@link #inlineVariables}    -   inline variables.
 * {@link #payloadJSON}        -   payload JSON what you want to send in the request
 */
public class RequestDataInit {
    private HTTPRequest HTTPRequest;
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


    public RequestDataInit(HTTPRequest HTTPRequest) {
        this(HTTPRequest, false);
    }

    public RequestDataInit(HTTPRequest HTTPRequest, boolean useFormData) {
        this.HTTPRequest = HTTPRequest;

        if(useFormData) {
            this.initFormUrlUserData();
        }
    }

    private RequestDataInit(boolean useCookie) {
        this.useCookie = useCookie;
    }

    private void initFormUrlUserData() {
        UserForAPIConnection userForAPIConnection = HTTPRequest.getUserForAPIConnection();

        if (userForAPIConnection != null) {

            this.xwwwwFormUrlEncoded(new HashMap<String, String>() {{
                put("grant_type",userForAPIConnection.getGrant_type());
                put("username", userForAPIConnection.getEmailAddress());
                put("password", userForAPIConnection.getPassword());
                put("client_id",userForAPIConnection.getClient_id());
                put("client_secret",userForAPIConnection.getClient_secret());
                put("scope", userForAPIConnection.getScope());
            }});
        }
    }

    public ConnectionManager<?> connect() {
        return new ConnectionManager<>(
                this.HTTPRequest,
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


    public static RequestDataInit builder(HTTPRequest HTTPRequest) {
        return new RequestDataInit(HTTPRequest);
    }

    public static RequestDataInit fullUserDataBuild(HTTPRequest HTTPRequest) {
        return new RequestDataInit(HTTPRequest, true);
    }

    public static RequestDataInit builder() {
        return new RequestDataInit(false);
    }

    public RequestDataInit endpoint(EndpointEnum endpoint) {
        this.endpoint = endpoint;
        if (this.endpoint instanceof InternalEndpointEnum) {
            this.endpointType = EndpointType.INTERNAL;
        } else if (this.endpoint instanceof ExternalEndpointEnum) {
            this.endpointType = EndpointType.EXTERNAL;
        }
        return this;
    }

    public RequestDataInit statusCode(int... statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public RequestDataInit useCookie(boolean useCookie) {
        this.useCookie = useCookie;
        return this;
    }

    public RequestDataInit useAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
        return this;
    }

    public RequestDataInit followRedirection(boolean followRedirection) {
        this.followRedirection = followRedirection;
        return this;
    }

    public RequestDataInit urlParam(Map<String, ?> urlParams) {
        if (urlParams != null) {
            this.urlParams.add(urlParams);
        }
        return this;
    }

    public RequestDataInit xwwwwFormUrlEncoded(Map<String, ?> xwwwwFormUrlEncoded) {
        if (xwwwwFormUrlEncoded != null) {
            this.xwwwwFormUrlEncoded.add(xwwwwFormUrlEncoded);
        }
        return this;
    }

    public RequestDataInit inlineVariables(Object... inlineVariables) {
        this.inlineVariables = inlineVariables;
        return this;
    }

    public RequestDataInit multiPart(MultiPartFiles multiPartFiles) {
        this.multiPartFiles = multiPartFiles;
        return this;
    }

    public RequestDataInit payloadJSON(PayloadJSON payloadJSON) {
        this.payloadJSON = payloadJSON;
        return this;
    }

    public RequestDataInit returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    public RequestDataInit setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public RequestDataInit setSocketTimeout(int socketTimeout) {
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

}
