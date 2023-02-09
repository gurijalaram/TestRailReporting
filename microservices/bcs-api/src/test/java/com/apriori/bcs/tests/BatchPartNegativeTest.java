package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.enums.FileType;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BatchPartNegativeTest {
    private static Batch batch;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createBatch().getResponseEntity();
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(testCaseId = {"4979"})
    @Description("Create a part with invalid VPE")
    public void createBatchPartInvalidVPE() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setDigitalFactory("Invalid VPE");
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        assertTrue("Track and wait until part is errored", BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity(), BCSState.ERRORED));
        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());

        Assert.assertEquals("Verify the error when part is created with invalid VPE",
            "Unable to find active digital factory with name 'Invalid VPE'", partResponse.getResponseEntity().getErrors());
    }

    @Test
    @TestRail(testCaseId = {"4978"})
    @Description("Create a part with invalid Process Group")
    public void createBatchPartInvalidPG() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        RequestEntity requestEntity = BatchPartResources.batchPartRequestEntity(newPartRequest, batch.getIdentity());
        requestEntity.queryParams(requestEntity.queryParams().use("ProcessGroup", ProcessGroupEnum.INVALID_PG.toString()));
        ResponseWrapper<Part> partResponse = HTTPRequest.build(requestEntity).postMultipart();

        assertTrue("Track and wait until part is completed", BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity(), BCSState.ERRORED));

        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());

        Assert.assertEquals("Verify the error when part is created with invalid Process Group",
            "Unable to find process group with name 'INVALID_PG'", partResponse.getResponseEntity().getErrors());
    }

    @Test
    @TestRail(testCaseId = {"4983", "8104"})
    @Description("Create a part with non-supported file type (txt, pdf) ")
    public void createBatchPartNonSupportedFile() {
        // Test with txt file
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        ResponseWrapper<ErrorMessage> errorMessageResponse;
        errorMessageResponse = BatchPartResources.createBatchPartWithInvalidData(newPartRequest, batch.getIdentity(), FileType.TXT);

        Assert.assertEquals("Verify the error when part is created with text file",
            "Invalid file type, file type 'txt' is not permitted", errorMessageResponse.getResponseEntity().getMessage());

        // Test with PDF file
        errorMessageResponse = BatchPartResources.createBatchPartWithInvalidData(newPartRequest, batch.getIdentity(), FileType.PDF);

        Assert.assertEquals("Verify the error when part is created with PDF file",
            "Invalid file type, file type 'pdf' is not permitted", errorMessageResponse.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(testCaseId = {"4368"})
    @Description("Create a part with invalid UDA")
    public void createBatchPartInvalidUDA() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setUdas("{\"ProjectName\":\"Invalid Project Name for negative automation test\"}");
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        assertTrue("Track and wait until part is errored", BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity(), BCSState.ERRORED));
        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());

        Assert.assertTrue("Verify the error when part is created with invalid UDA",
                partResponse.getResponseEntity().getErrors().contains("Custom attribute with name 'ProjectName' was not found"));
    }

    @Test
    @TestRail(testCaseId = {"9535", "8099"})
    @Description("Create part with invalid batch Identity")
    public void createBatchPartWithInvalidBatch() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        ResponseWrapper<ErrorMessage> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, "INVALIDBATCHID", ErrorMessage.class);

        assertThat(partResponse.getResponseEntity().getMessage(), is(equalTo("'batchIdentity' is not a valid identity.")));
    }

    @Test
    @TestRail(testCaseId = {"8036", "8095", "8099"})
    @Description("Invalid Customer Identity")
    public void createBatchWithInvalidCustomerID() {
        ResponseWrapper<ErrorMessage> batchResponse = BatchResources.createBatch("INVALIDCUSTOMER", ErrorMessage.class);

        assertThat(batchResponse.getResponseEntity().getMessage(), is(equalTo("'customerIdentity' is not a valid identity.")));
    }

    @Test
    @TestRail(testCaseId = {"8113"})
    @Description("Create part with blank / missing External Identity")
    public void createBatchPartWithExternalIDBlank() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setExternalId("");
        ResponseWrapper<ErrorMessage> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity(), ErrorMessage.class);

        assertThat(partResponse.getResponseEntity().getMessage(), is(containsString("'externalId' should not be empty")));
    }

    @Test
    @TestRail(testCaseId = {"8037"})
    @Description("Get single batch with invalid customer identity")
    public void getBatchWithInvalidCustomerID() {
        ResponseWrapper<ErrorMessage> batchResponse = HTTPRequest.build(
            RequestEntityUtil.init(BCSAPIEnum.BATCH_BY_ID, ErrorMessage.class)
            .inlineVariables("INVALIDCUSTOMER", batch.getIdentity())
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
        ).get();

        assertThat(batchResponse.getResponseEntity().getMessage(), is(equalTo("'customerIdentity' is not a valid identity.")));
    }

    @Test
    @TestRail(testCaseId = {"8049"})
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
    @TestRail(testCaseId = {"8058"})
    @Description("Cancel batch with invalid batch identity")
    public void cancelBatchWithInvalidBatchID() {
        HTTPRequest.build(BatchResources.getBatchRequestEntity(
            BCSAPIEnum.CANCEL_COSTING_BY_ID, "INVALIDBATCH", ErrorMessage.class)
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND)
       ).post();
    }

    @Test
    @TestRail(testCaseId = {"8094"})
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
    @TestRail(testCaseId = {"8123", "9538"})
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
    @TestRail(testCaseId = {"8125"})
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

        assertThat(batchResponse.getResponseEntity().getMessage(), is(equalTo("Can't get cost results. Invalid state.")));
    }

    @Test
    @TestRail(testCaseId = {"8126","9540"})
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
    @TestRail(testCaseId = {"9541"})
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
    @TestRail(testCaseId = {"8128"})
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

        assertThat(batchResponse.getResponseEntity().getError(), is(equalTo("Conflict")));
    }

    @Test
    @TestRail(testCaseId = {"9545", "9550"})
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
    @TestRail(testCaseId = {"8100","9546"})
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

        assertThat(batchResponse.getResponseEntity().getMessage(), is(containsString("'batchIdentity' should not be blank")));

    }

    @Test
    @TestRail(testCaseId = {"9543"})
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

        assertThat(batchResponse.getResponseEntity().getMessage(), is(containsString("'identity' should not be blank")));
    }

    @Test
    @TestRail(testCaseId = {"9539"})
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

        assertThat(batchResponse.getResponseEntity().getMessage(), is(containsString("'batchIdentity' should not be blank")));
    }

    @Test
    @TestRail(testCaseId = {"8096"})
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
    @TestRail(testCaseId = {"8690"})
    @Description("Attempt to add a new part to a batch using empty string values")
    public void createBatchPartWithEmptyStringValues() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setMaterial("");
        newPartRequest.setDigitalFactory("");
        BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8110", "8116"})
    @Description("Attempt to add a new part to a batch with invalid production life")
    public void createBatchPartWithInvalidProductionLife() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setProductionLife("abc");
        BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity(), ErrorMessage.class);
    }

    @AfterClass
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
