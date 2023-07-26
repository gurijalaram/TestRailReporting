package com.apriori.tests;

import com.apriori.cnh.entity.apicalls.CnhService;
import com.apriori.cnh.entity.request.ExecuteRequest;
import com.apriori.cnh.entity.response.ExecuteResponse;





import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class CnhPositiveTests {

    private String customerId = "3KED5H5BKN85";

    @Test
    @TestRail(id = {14057})
    @Description("Verify report-1")
    public void shouldExecuteReport1Test() {
        SoftAssertions soft = new SoftAssertions();
        ExecuteRequest executeRequest = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "ExecuteRequest.json"
            ).getPath(), ExecuteRequest.class);

        CnhService cnhService = new CnhService();
        ResponseWrapper<ExecuteResponse> responseExecuteActual = cnhService.execute(executeRequest, customerId,"report-1");
        String executionId = responseExecuteActual.getResponseEntity().getExecutionId();
        ResponseWrapper<ExecuteResponse> responseStatusActual =
            cnhService.status(customerId,"report-1",executionId);

        soft.assertThat(responseExecuteActual.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseExecuteActual.getResponseEntity().getExecutionId()).isNotEmpty();
        soft.assertThat(responseStatusActual.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseStatusActual.getResponseEntity().getExecutionId()).isEqualTo(executionId);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {14058})
    @Description("Verify slow-query")
    public void shouldExecuteSlowQueryTest() {
        SoftAssertions soft = new SoftAssertions();
        ExecuteRequest executeRequest = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "ExecuteRequest.json"
            ).getPath(), ExecuteRequest.class);

        CnhService cnhService = new CnhService();
        ResponseWrapper<ExecuteResponse> responseExecuteActual = cnhService.execute(executeRequest, customerId,"slow-query");
        String executionId = responseExecuteActual.getResponseEntity().getExecutionId();
        ResponseWrapper<ExecuteResponse> responseStatusActual =
            cnhService.status(customerId,"slow-query",executionId);

        soft.assertThat(responseExecuteActual.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseExecuteActual.getResponseEntity().getExecutionId()).isNotEmpty();
        soft.assertThat(responseStatusActual.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseStatusActual.getResponseEntity().getExecutionId()).isEqualTo(executionId);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {14059})
    @Description("Verify a few calls run at once")
    public void shouldExecuteFewCallsTest() {
        SoftAssertions soft = new SoftAssertions();
        ExecuteRequest executeRequest = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "ExecuteRequest.json"
            ).getPath(), ExecuteRequest.class);

        CnhService cnhService = new CnhService();
        ResponseWrapper<ExecuteResponse> responseExecuteActual1 = cnhService.execute(executeRequest, customerId,"report-1");
        String executionId1 = responseExecuteActual1.getResponseEntity().getExecutionId();
        ResponseWrapper<ExecuteResponse> responseStatusActual1 =
            cnhService.status(customerId,"report-1",executionId1);

        ResponseWrapper<ExecuteResponse> responseExecuteActual2 = cnhService.execute(executeRequest, customerId,"report-1");
        String executionId2 = responseExecuteActual2.getResponseEntity().getExecutionId();
        ResponseWrapper<ExecuteResponse> responseStatusActual2 =
            cnhService.status(customerId,"report-1",executionId2);

        soft.assertThat(responseExecuteActual1.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseExecuteActual1.getResponseEntity().getExecutionId()).isNotEmpty();
        soft.assertThat(responseStatusActual1.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseStatusActual1.getResponseEntity().getExecutionId()).isEqualTo(executionId1);

        soft.assertThat(responseExecuteActual2.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseExecuteActual2.getResponseEntity().getExecutionId()).isNotEmpty();
        soft.assertThat(responseStatusActual2.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseStatusActual2.getResponseEntity().getExecutionId()).isEqualTo(executionId2);
        soft.assertAll();
    }
}
