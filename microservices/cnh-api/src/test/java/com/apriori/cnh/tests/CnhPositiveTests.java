package com.apriori.cnh.tests;

import java.util.Arrays;

import com.apriori.cnh.entity.apicalls.CnhService;
import com.apriori.cnh.entity.request.ExecuteRequest;
import com.apriori.cnh.entity.request.Params;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import io.qameta.allure.Description;
import org.junit.Test;

public class CnhPositiveTests {

    @Test
    @TestRail(testCaseId = {"14057"})
    @Description("Verify report-1")
    public void shouldExecuteReport1Test() {

        ExecuteRequest executeRequest = ExecuteRequest
            .builder()
            .params(Arrays.asList(Params.builder()
                    .name("time")
                    .value("2022-08-01T00:00:00Z")
                    .type("timestamp")
                .build()))
            .build();

        CnhService cnhService = new CnhService();
        //ResponseWrapper<ExecuteResponse> responseWrapper = cnhService.execute(executeRequest);
         cnhService.execute(executeRequest);

        System.out.println("ff");

    }


}
