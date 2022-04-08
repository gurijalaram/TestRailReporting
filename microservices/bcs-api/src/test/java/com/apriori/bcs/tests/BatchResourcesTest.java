package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Batches;
import com.apriori.bcs.entity.response.Cancel;
import com.apriori.bcs.enums.BCSState;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class BatchResourcesTest {

    private static Batch batch1;
    private static ResponseWrapper<Batch> response;

    @BeforeClass
    public static void testSetup() {
        response = BatchResources.createBatch();
        batch1 = response.getResponseEntity();
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batch1.getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @Description("API creates and returns Batch in the CIS DB")
    public void createBatch() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();
        assertThat(batchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batchResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(testCaseId = {"4275"})
    @Description("API returns a list of Batches in the CIS DB")
    public void getBatches() {
        ResponseWrapper<Batches> batchesResponse = BatchResources.getBatches();
        assertThat(batchesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotEquals(batchesResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4277"})
    @Description("API returns a representation of a single Batch in the CIS DB")
    public void getBatch() {
        ResponseWrapper<Batch> batchResponse = BatchResources.getBatchRepresentation(batch1.getIdentity());
        assertThat(batchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(batch1.getIdentity(), is(equalTo(batchResponse.getResponseEntity().getIdentity().toString())));
    }

    @Test
    @TestRail(testCaseId = {"7906"})
    @Description("Cancel batch processing")
    public void batchProcessingCancel() {
        Batch batch = BatchResources.createBatch().getResponseEntity();
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));

        ResponseWrapper<Cancel> cancelResponse = BatchResources.cancelBatchProcessing(batch.getIdentity());
        assertThat(cancelResponse.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
        assertThat(cancelResponse.getResponseEntity().getState(), is(equalTo(BCSState.CANCELLED.toString())));
    }

    @Test
    @TestRail(testCaseId = {"4988"})
    @Description("Create and Cost the empty batch")
    public void createBatchCosting() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();
        assertThat(batchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batchResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));

        ResponseWrapper<String> batchCostingResponse = BatchResources.startBatchCosting(batchResponse.getResponseEntity());
        assertThat(batchCostingResponse.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
        assertTrue("Verify Batch costing state is completed", BatchResources.waitUntilBatchCostingReachedExpected(batchResponse.getResponseEntity().getIdentity(), BCSState.COMPLETED));
    }

    @AfterClass
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch1);
    }
}
