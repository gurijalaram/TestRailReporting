package com.apriori.tests;

import com.apriori.cnh.entity.apicalls.CnhService;
import com.apriori.cnh.entity.request.ExecuteRequest;
import com.apriori.cnh.entity.response.ExecuteResponse;





import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class CnhNegativeTests {

    private  String customerId = "3KED5H5BKN85";
    private  String invalidCustomerId = "TEST3KED5H5BKN85";

    @Test
    @TestRail(id = {14062})
    @Description("Verify wrong report id provided in /status call")
    public void shouldNotProcessWrongReportId() {
        SoftAssertions soft = new SoftAssertions();
        ExecuteRequest executeRequest = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "ExecuteRequest.json"
            ).getPath(), ExecuteRequest.class);

        CnhService cnhService = new CnhService();
        ResponseWrapper<ExecuteResponse> responseExecuteActual = cnhService.execute(executeRequest, customerId,"report-1");
        String executionId = responseExecuteActual.getResponseEntity().getExecutionId();
        ResponseWrapper<ExecuteResponse> responseStatusActual =
            cnhService.status(customerId,"slow-query",executionId);

        soft.assertThat(responseExecuteActual.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseExecuteActual.getResponseEntity().getExecutionId()).isNotEmpty();
        soft.assertThat(responseStatusActual.getResponseEntity().getError())
            .isEqualTo("Report slow-query is not relevant to specified execution" + " " + executionId);
        soft.assertThat(responseStatusActual.getResponseEntity().getExecutionId()).isEqualTo(executionId);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {14066})
    @Description("Verify both endpoints with wrong client id")
    public void shouldNotProcessWithWrongClientId() {
        SoftAssertions soft = new SoftAssertions();
        ExecuteRequest executeRequest = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "ExecuteRequest.json"
            ).getPath(), ExecuteRequest.class);

        CnhService cnhService = new CnhService();
        ResponseWrapper<ExecuteResponse> responseExecuteActual = cnhService.execute(executeRequest, customerId,"report-1");
        String invalidExecutionId = "123456789";
        ResponseWrapper<ExecuteResponse> responseStatusActual =
            cnhService.status(customerId,"report-1",invalidExecutionId);

        soft.assertThat(responseExecuteActual.getResponseEntity().getStatus()).isEqualTo("RUNNING");
        soft.assertThat(responseExecuteActual.getResponseEntity().getExecutionId()).isNotEmpty();
        soft.assertThat(responseStatusActual.getResponseEntity().getError()).isEqualTo("Execution not found");
        soft.assertThat(responseStatusActual.getResponseEntity().getExecutionId()).isEqualTo(invalidExecutionId);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {14064})
    @Description("Verify missing parameters in the /execute body for report-1")
    public void shouldNotProcessWithMissedParameter() {
        SoftAssertions soft = new SoftAssertions();
        ExecuteRequest executeRequest = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "ExecuteRequest.json"
            ).getPath(), ExecuteRequest.class);

        CnhService cnhService = new CnhService();
        ResponseWrapper<ExecuteResponse> responseExecuteActual = cnhService.execute(executeRequest, invalidCustomerId,"report-1");
        String executionId = responseExecuteActual.getResponseEntity().getExecutionId();
        ResponseWrapper<ExecuteResponse> responseStatusActual =
            cnhService.status(invalidCustomerId,"report-1",executionId);

        soft.assertThat(responseExecuteActual.getResponseEntity().getMessage())
            .isEqualTo("User is not authorized to access this resource with an explicit deny");
        soft.assertThat(responseStatusActual.getResponseEntity().getMessage())
            .isEqualTo("User is not authorized to access this resource with an explicit deny");
        soft.assertAll();
    }


}
