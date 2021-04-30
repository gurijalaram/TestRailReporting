package com.apriori.utils.http2.builder.common.entity;

import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.enums.EndpointType;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Accessors;
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
@SuppressWarnings("checkstyle:RightCurly")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Accessors(fluent = true)
public class RequestEntity {

    private String token;

    private WebDriver driver;
    private UserAuthenticationEntity userAuthenticationEntity;

    private String customBody;

    // TODO z: remove
    private com.apriori.utils.http.enums.EndpointEnum endpointOld;

    private EndpointEnum endpoint;
    private String customEndpoint;
    private Integer[] statusCode;
    private boolean useCookie = false;
    private boolean autoLogin = false;
    private boolean defaultAuthorizationData = false;
    private EndpointType endpointType = EndpointType.EXTERNAL;
    private boolean followRedirection = false;
    @Singular private Map<String, String> headers = new HashMap<>();
    @Singular private List<Map<String, ?>> urlParams = new ArrayList<>();
    private List<Map<String, ?>> xwwwwFormUrlEncoded = new ArrayList<>();
    private Object[] inlineVariables;
    private MultiPartFiles multiPartFiles;
    private FormParams formParams;
    private Object body;
    private Class<?> returnType;
    private int connectionTimeout = 60000;
    private int socketTimeout = 60000;
    private boolean urlEncodingEnabled = true;

    public RequestEntity addXwwwwFormUrlEncoded(Map<String, ?> xwwwwFormUrlEncoded) {
        if (xwwwwFormUrlEncoded != null) {
            this.xwwwwFormUrlEncoded.add(xwwwwFormUrlEncoded);
        }
        return this;
    }

    public RequestEntity body(String node, Object body) {
        this.body = new HashMap<String, Object>() {
            {
                put(node, body);
            }
        };
        return this;
    }

    public String buildEndpoint() {

        if (this.customEndpoint != null) {
            return this.customEndpoint;
        }

        return this.inlineVariables != null ? endpoint.getEndpoint(inlineVariables) : endpoint.getEndpoint();
    }
}
