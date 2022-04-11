package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.enums.FileType;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
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
        newPartRequest.setVpeName("Invalid VPE");
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
        requestEntity.formParams(requestEntity.formParams().use("ProcessGroup", ProcessGroupEnum.INVALID_PG.toString()));
        ResponseWrapper<Part> partResponse = HTTPRequest.build(requestEntity).postMultipart();

        assertTrue("Track and wait until part is completed", BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity()));

        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());

        Assert.assertEquals("Verify the error when part is created with invalid Process Group",
            "COSTING_FAILED", partResponse.getResponseEntity().getCostingResult());
    }

    @Test
    @TestRail(testCaseId = {"4983"})
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
        newPartRequest.setUdas("{\"UDARegion\":\"Invalid UDA\"}");
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        assertTrue("Track and wait until part is errored", BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), partResponse.getResponseEntity().getIdentity(), BCSState.ERRORED));
        partResponse = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), partResponse.getResponseEntity().getIdentity());

        Assert.assertEquals("Verify the error when part is created with invalid UDA",
            "Custom attribute with name 'UDARegion' was not found", partResponse.getResponseEntity().getErrors());
    }

    @AfterClass
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
