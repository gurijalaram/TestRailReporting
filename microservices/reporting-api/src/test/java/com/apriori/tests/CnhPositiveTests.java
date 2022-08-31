package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cnh.entity.apicalls.CnhService;
import com.apriori.cnh.entity.request.ExecuteRequest;
import com.apriori.cnh.entity.response.ExecuteResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class CnhPositiveTests {

    @Test
    @TestRail(testCaseId = {"14057"})
    @Description("Verify report-1")
    public void shouldExecuteReport1Test() {
        SoftAssertions soft = new SoftAssertions();
        ExecuteRequest executeRequest = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "ExecuteRequestReport-1.json"
            ).getPath(), ExecuteRequest.class);

        CnhService cnhService = new CnhService();
        ResponseWrapper<ExecuteResponse> responseActual = cnhService.execute(executeRequest,"report-1");

        soft.assertThat(responseActual.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseActual.getResponseEntity().getExecutionId()).isNotEmpty();
    }
}
