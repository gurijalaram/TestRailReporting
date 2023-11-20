package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.controller.BatchResources;
import com.apriori.bcs.api.enums.BCSAPIEnum;
import com.apriori.bcs.api.enums.BCSState;
import com.apriori.bcs.api.enums.FileType;
import com.apriori.bcs.api.models.request.parts.NewPartRequest;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class BatchPartNegativeTest {
    private static Batch batch;
    private SoftAssertions softAssertions;

    @BeforeAll
    public static void beforeClass() {
        batch = BatchResources.createBatch().getResponseEntity();
    }

    @BeforeEach
    public void setupTest() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {4979})
    @Description("Create a part with invalid VPE")
    public void createBatchPartInvalidVPE() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setDigitalFactory("Invalid VPE");
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        softAssertions.assertThat(BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity(), BCSState.ERRORED)).isTrue();
        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());

        softAssertions.assertThat(partResponse.getResponseEntity().getErrors()).isEqualTo("Unable to find active digital factory with name 'Invalid VPE'");
    }

    @Test
    @TestRail(id = {4978})
    @Description("Create a part with invalid Process Group")
    public void createBatchPartInvalidPG() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        RequestEntity requestEntity = BatchPartResources.batchPartRequestEntity(newPartRequest, batch.getIdentity());
        requestEntity.queryParams(requestEntity.queryParams().use("ProcessGroup", ProcessGroupEnum.INVALID_PG.toString()));
        ResponseWrapper<Part> partResponse = HTTPRequest.build(requestEntity).postMultipart();
        softAssertions.assertThat(BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity(), BCSState.ERRORED)).isTrue();

        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());
        softAssertions.assertThat(partResponse.getResponseEntity().getErrors()).isEqualTo("Unable to find process group with name 'INVALID_PG'");
    }

    @Test
    @TestRail(id = {4983, 8104})
    @Description("Create a part with non-supported file type (txt, pdf) ")
    public void createBatchPartNonSupportedFile() {
        // Test with txt file
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        ResponseWrapper<ErrorMessage> errorMessageResponse;
        errorMessageResponse = BatchPartResources.createBatchPartWithInvalidData(newPartRequest, batch.getIdentity(), FileType.TXT);
        softAssertions.assertThat(errorMessageResponse.getResponseEntity().getMessage()).isEqualTo("Invalid file type, file type 'txt' is not permitted");
        // Test with PDF file
        errorMessageResponse = BatchPartResources.createBatchPartWithInvalidData(newPartRequest, batch.getIdentity(), FileType.PDF);
        softAssertions.assertThat(errorMessageResponse.getResponseEntity().getMessage()).isEqualTo("Invalid file type, file type 'pdf' is not permitted");
    }

    @Test
    @TestRail(id = {4368})
    @Description("Create a part with invalid UDA")
    public void createBatchPartInvalidUDA() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setUdas("{\"ProjectName\":\"Invalid Project Name for negative automation test\"}");
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        softAssertions.assertThat(BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity(), BCSState.ERRORED)).isTrue();
        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());
        softAssertions.assertThat(partResponse.getResponseEntity().getErrors().contains("Custom attribute with name 'ProjectName' was not found")).isTrue();
    }

    @Test
    @TestRail(id = {9535, 8099})
    @Description("Create part with invalid batch Identity")
    public void createBatchPartWithInvalidBatch() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        ResponseWrapper<ErrorMessage> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, "INVALIDBATCHID", ErrorMessage.class);
        softAssertions.assertThat(partResponse.getResponseEntity().getMessage()).isEqualTo("'batchIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {8036, 8095, 8099})
    @Description("Invalid Customer Identity")
    public void createBatchWithInvalidCustomerID() {
        ResponseWrapper<ErrorMessage> batchResponse = BatchResources.createBatch("INVALIDCUSTOMER", ErrorMessage.class);
        softAssertions.assertThat(batchResponse.getResponseEntity().getMessage()).isEqualTo("'customerIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {8113})
    @Description("Create part with blank / missing External Identity")
    public void createBatchPartWithExternalIDBlank() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setExternalId("");
        ResponseWrapper<ErrorMessage> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity(), ErrorMessage.class);
        softAssertions.assertThat(partResponse.getResponseEntity().getMessage().contains("'externalId' should not be empty")).isTrue();
        softAssertions.assertThat(partResponse.getResponseEntity().getMessage().contains("'externalId' should not be blank")).isTrue();
    }

    @Test
    @TestRail(id = {8037})
    @Description("Get single batch with invalid customer identity")
    public void getBatchWithInvalidCustomerID() {
        ResponseWrapper<ErrorMessage> batchResponse = HTTPRequest.build(
            RequestEntityUtil_Old.init(BCSAPIEnum.BATCH_BY_ID, ErrorMessage.class)
                .inlineVariables("INVALIDCUSTOMER", batch.getIdentity())
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();

        softAssertions.assertThat(batchResponse.getResponseEntity().getMessage()).isEqualTo("'customerIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {8049})
    @Description("Get batch with invalid batch identity")
    public void getBatchWithInvalidBatchID() {
        HTTPRequest.build(
            BatchResources.getBatchRequestEntity(
                    BCSAPIEnum.BATCH_PARTS_BY_ID,
                    "INVALIDBATCH",
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_NOT_FOUND)
        ).get();
    }

    @Test
    @TestRail(id = {8058})
    @Description("Cancel batch with invalid batch identity")
    public void cancelBatchWithInvalidBatchID() {
        HTTPRequest.build(BatchResources.getBatchRequestEntity(
                BCSAPIEnum.CANCEL_COSTING_BY_ID, "INVALIDBATCH", ErrorMessage.class)
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND)
        ).post();
    }

    @Test
    @TestRail(id = {8094})
    @Description("get parts with invalid Batch identity")
    public void getBatchPartWithInvalidBatchID() {
        HTTPRequest.build(
            BatchResources.getBatchRequestEntity(
                    BCSAPIEnum.BATCH_PARTS,
                    "InvalidBatchId",
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();
    }

    @Test
    @TestRail(id = {8123, 9538})
    @Description("Return a single Batch-Part using an invalid batch")
    public void getBatchPartWithInvalidPartIdentity() {
        HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS,
                    batch.getIdentity(),
                    "InvalidPartIdentity",
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();
    }

    @Test
    @TestRail(id = {8125})
    @Description("Get Batch part results with UnCosted part identity")
    public void getBatchPartResultWithUnCostedPartIdentity() {
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        ResponseWrapper<ErrorMessage> batchResponse = HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.RESULTS_BY_BATCH_PART_IDS,
                    batch.getIdentity(), part.getIdentity(),
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_CONFLICT)
        ).get();

        softAssertions.assertThat(batchResponse.getResponseEntity().getMessage()).isEqualTo("Can't get cost results. Invalid state.");
    }

    @Test
    @TestRail(id = {8126, 9540})
    @Description("Get Batch part results with invalid part identity")
    public void getBatchPartResultWithInvalidPartIdentity() {
        HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.RESULTS_BY_BATCH_PART_IDS,
                    batch.getIdentity(),
                    "InvalidPartId",
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();
    }

    @Test
    @TestRail(id = {9541})
    @Description("Get Batch part results with missing batch")
    public void getBatchPartResultWithMissingBatchIdentity() {
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.RESULTS_BY_BATCH_PART_IDS,
                    " ",
                    part.getIdentity(),
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();
    }

    @Test
    @TestRail(id = {8128})
    @Description("Get Batch part report with UnCosted part identity")
    public void getBatchPartReportWithUnCostedPartIdentity() {
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        ResponseWrapper<ErrorMessage> batchResponse = HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.PART_REPORT_BY_BATCH_PART_IDS,
                    batch.getIdentity(),
                    part.getIdentity(),
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_CONFLICT)
        ).get();

        softAssertions.assertThat(batchResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Part with identity '%s' could not have a watchpoint report generated. Costing Result is 'null'.", part.getIdentity()));
    }

    @Test
    @TestRail(id = {9545, 9550})
    @Description("Get part report with invalid batch and invalid part identity")
    public void getBatchPartReportWithInvalidBatchId() {
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.PART_REPORT_BY_BATCH_PART_IDS,
                    "INVALIDBATCHID",
                    part.getIdentity(),
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();

        HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.PART_REPORT_BY_BATCH_PART_IDS,
                    batch.getIdentity(),
                    "INVALIDPARTID",
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();
    }

    @Test
    @TestRail(id = {8100, 9546})
    @Description("Get part report with missing batch")
    public void getBatchPartReportWithMissingBatchId() {
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        ResponseWrapper<ErrorMessage> batchResponse = HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.PART_REPORT_BY_BATCH_PART_IDS,
                    " ",
                    part.getIdentity(),
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();

        softAssertions.assertThat(batchResponse.getResponseEntity().getMessage().contains("'batchIdentity' should not be blank")).isTrue();
    }

    @Test
    @TestRail(id = {9543})
    @Description("Get part report with missing Part identity")
    public void getBatchPartReportWithMissingPartId() {
        ResponseWrapper<ErrorMessage> batchResponse = HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.PART_REPORT_BY_BATCH_PART_IDS,
                    batch.getIdentity(),
                    " ",
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();

        softAssertions.assertThat(batchResponse.getResponseEntity().getMessage().contains("'identity' should not be blank")).isTrue();
    }

    @Test
    @TestRail(id = {9539})
    @Description("Get single part with missing batch id")
    public void getSinglePartWithMissingBatchId() {
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        ResponseWrapper<ErrorMessage> batchResponse = HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS,
                    " ",
                    part.getIdentity(),
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();

        softAssertions.assertThat(batchResponse.getResponseEntity().getMessage().contains("'batchIdentity' should not be blank")).isTrue();
    }

    @Test
    @TestRail(id = {8096})
    @Description("Get part to a batch with missing batch identity")
    public void getBatchPartsWithMissingBatchID() {
        HTTPRequest.build(
            BatchResources.getBatchRequestEntity(
                    BCSAPIEnum.BATCH_PARTS,
                    " ",
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();
    }

    @Test
    @TestRail(id = {8690})
    @Description("Attempt to add a new part to a batch using empty string values")
    public void createBatchPartWithEmptyStringValues() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setMaterial("");
        newPartRequest.setDigitalFactory("");
        BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
    }

    @Test
    @TestRail(id = {8110, 8116})
    @Description("Attempt to add a new part to a batch with invalid production life")
    public void createBatchPartWithInvalidProductionLife() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setProductionLife("abc");
        BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity(), ErrorMessage.class);
    }

    @AfterEach
    public void tearTest() {
        softAssertions.assertAll();
    }

    @AfterAll
    public static void afterClass() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
