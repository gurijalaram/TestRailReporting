package com.apriori.http.builder.entity;

import com.apriori.enums.EndpointType;
import com.apriori.http.utils.MultiPartFiles;
import com.apriori.http.utils.QueryParams;
import com.apriori.interfaces.EndpointEnum;

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
    private String apUserContext;
    private Object body;
    private int connectionTimeout = 60000;
    private String customBody;
    private boolean defaultAuthorizationData = false;
    private WebDriver driver;
    private EndpointEnum endpoint;
    private EndpointType endpointType = EndpointType.EXTERNAL;
    private Integer expectedResponseCode;
    private boolean followRedirection = true;
    private QueryParams queryParams;
    @Singular
    private Map<String, String> headers = new HashMap<>();
    private Object[] inlineVariablesArray;
    private MultiPartFiles multiPartFiles;
    private Class<?> returnType;
    private int socketTimeout = 60000;
    private String token;
    private boolean urlEncodingEnabled = true;
    @Singular
    private List<Map<String, ?>> urlParams = new ArrayList<>();
    private boolean useCookie = false;
    private UserAuthenticationEntity userAuthenticationEntity;
    @Singular
    private List<Map<String, ?>> xwwwwFormUrlEncodeds = new ArrayList<>();

    public RequestEntity body(String node, Object body) {
        this.body = new HashMap<String, Object>() {
            {
                put(node, body);
            }
        };
        return this;
    }

    public RequestEntity inlineVariables(String... inlineVariables) {
        this.inlineVariablesArray = inlineVariables;
        return this;
    }

    public String buildEndpoint() {
        return this.inlineVariablesArray != null ? endpoint.getEndpoint(inlineVariablesArray) : endpoint.getEndpoint();
    }
}
