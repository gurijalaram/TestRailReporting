package com.apriori.cnh.api.apicalls;

import com.apriori.cnh.api.enums.CNHAPIEnum;
import com.apriori.cnh.api.request.ExecuteRequest;
import com.apriori.cnh.api.response.ExecuteResponse;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;

public class CnhService {

    private final Map<String, String> headers = new HashMap<String, String>() {
        {
            put("x-token", "0vMhnlo3T52QgiHBWDesYaDKwGUhFE579y70nPyI");
            put("x-api-key", "qKIDWEB6UQlDCK5Pc2S3a8jntrqPzc53sdLGuD50");
        }
    };

    /**
     * calls 'execute' POST endpoint
     *
     * @param executeRequest - pass the body
     */
    public ResponseWrapper<ExecuteResponse> execute(ExecuteRequest executeRequest, String... inlineVariables) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CNHAPIEnum.EXECUTE, ExecuteResponse.class)
            .headers(headers)
            .body(executeRequest)
            .inlineVariables(inlineVariables);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * overload method to pass extra headers, calls 'execute' POST endpoint
     *
     * @param executeRequest - pass the body
     */
    public ResponseWrapper<ExecuteResponse> execute(Map<String, String> headers, ExecuteRequest executeRequest,String... inlineVariables) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CNHAPIEnum.EXECUTE, ExecuteResponse.class)
            .headers(headers)
            .body(executeRequest)
            .inlineVariables(inlineVariables);

        return HTTPRequest.build(requestEntity).post();
    }



    /**
     * calls 'execution' GET endpoint
     *
     */
    public ResponseWrapper<ExecuteResponse> status(String... inlineVariables) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CNHAPIEnum.STATUS, ExecuteResponse.class)
            .headers(headers)
            .inlineVariables(inlineVariables);

        return HTTPRequest.build(requestEntity).get();
    }
}
