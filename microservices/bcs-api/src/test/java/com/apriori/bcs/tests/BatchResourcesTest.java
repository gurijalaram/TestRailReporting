package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Cancel;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.utils.BcsTestUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



public class BatchResourcesTest {

    private static Batch batch1;
    private static ResponseWrapper<Object> response;

    @BeforeClass
    public static void testSetup() {
        response = BatchResources.createBatch();
        batch1 = (Batch)response.getResponseEntity();
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batch1.getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @Description("API creates and returns Batch in the CIS DB")
    public void createBatch() {
        ResponseWrapper<Object> batchResponse = BatchResources.createBatch();
        Batch batch = (Batch)batchResponse.getResponseEntity();
        assertThat(batchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(testCaseId = {"4275"})
    @Description("API returns a list of Batches in the CIS DB")
    public void getBatches() {
        response = BatchResources.getBatches();
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }

    @Test
    @TestRail(testCaseId = {"4277"})
    @Description("API returns a representation of a single Batch in the CIS DB")
    public void getBatch() {
        response = BatchResources.getBatchRepresentation(batch1.getIdentity());
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }

    @Test
    @TestRail(testCaseId = {"7906"})
    @Description("Cancel batch processing")
    public void batchProcessingCancel() {
        Batch batch = (Batch) BatchResources.createBatch().getResponseEntity();
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));
        ResponseWrapper<Object> cancelResponse = BatchResources.cancelBatchProcessing(batch.getIdentity());
        Cancel cancel = (Cancel) cancelResponse.getResponseEntity();
        assertThat(cancelResponse.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
        assertThat(cancel.getState(), is(equalTo(BCSState.CANCELLED.toString())));
    }

    @Test
    @Description("Create and Cost the batch")
    public void createBatchCosting() {
        response = BatchResources.createBatch();
        Batch batch = (Batch)response.getResponseEntity();
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));
        response = BatchResources.startBatchCosting(batch);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
    }

    @AfterClass
    public static void testCleanup() {
        BcsTestUtils.checkAndCancelBatch(batch1);
    }
}
