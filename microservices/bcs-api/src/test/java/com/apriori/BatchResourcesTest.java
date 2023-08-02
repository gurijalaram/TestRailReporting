package com.apriori;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.models.response.Batch;
import com.apriori.bcs.models.response.Batches;
import com.apriori.bcs.models.response.Cancel;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BatchResourcesTest {

    private static Batch batch1;
    private static ResponseWrapper<Batch> response;

    @BeforeAll
    public static void testSetup() {
        response = BatchResources.createBatch();
        batch1 = response.getResponseEntity();
        assertThat(batch1.getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @AfterAll
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch1);
    }

    @Test
    @Description("API creates and returns Batch in the CIS DB")
    public void createBatch() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();

        assertThat(batchResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(id = {4275})
    @Description("API returns a list of Batches in the CIS DB")
    public void getBatches() {
        ResponseWrapper<Batches> batchesResponse = BatchResources.getBatches();

        assertNotEquals(batchesResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(id = {4277})
    @Description("API returns a representation of a single Batch in the CIS DB")
    public void getBatch() {
        ResponseWrapper<Batch> batchResponse = BatchResources.getBatchRepresentation(batch1.getIdentity());

        assertThat(batch1.getIdentity(), is(equalTo(batchResponse.getResponseEntity().getIdentity())));
    }

    @Test
    @TestRail(id = {7906})
    @Description("Cancel batch processing")
    public void batchProcessingCancel() {
        Batch batch = BatchResources.createBatch().getResponseEntity();

        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));

        ResponseWrapper<Cancel> cancelResponse = BatchResources.cancelBatchProcessing(batch.getIdentity());

        assertThat(cancelResponse.getResponseEntity().getState(), is(equalTo(BCSState.CANCELLED.toString())));
    }

    @Test
    @TestRail(id = {4988})
    @Description("Create and Cost the empty batch")
    public void createBatchCosting() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();

        assertThat(batchResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));

        BatchResources.startBatchCosting(batchResponse.getResponseEntity());

        assertTrue(BatchResources.waitUntilBatchCostingReachedExpected(batchResponse.getResponseEntity().getIdentity(), BCSState.COMPLETED), "Verify Batch costing state is completed");
    }
}
