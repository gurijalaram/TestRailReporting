package com.apriori.cnh.entity.apicalls;

import java.util.HashMap;
import java.util.Map;

import com.apriori.cnh.entity.enums.CNHAPIEnum;
import com.apriori.cnh.entity.request.ExecuteRequest;
import com.apriori.cnh.entity.response.ExecuteResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import org.apache.http.HttpStatus;

public class CnhService {

    private Map<String, String> headers = new HashMap<String, String>() {{
        put("x-token", "0vMhnlo3T52QgiHBWDesYaDKwGUhFE579y70nPyI");
        put("x-api-key", "qKIDWEB6UQlDCK5Pc2S3a8jntrqPzc53sdLGuD50");
    }};

    //public ResponseWrapper<ExecuteResponse> execute(ExecuteRequest executeRequest) {
    public void execute(ExecuteRequest executeRequest) {
        RequestEntity requestEntity = RequestEntityUtil.init(CNHAPIEnum.EXECUTE, ExecuteResponse.class)
            .headers(headers)
            .body(executeRequest);

        HTTPRequest.build(requestEntity).post();
        //ResponseWrapper<ExecuteResponse> response = HTTPRequest.build(requestEntity).get();

        //validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
       // return response;
    }
}
