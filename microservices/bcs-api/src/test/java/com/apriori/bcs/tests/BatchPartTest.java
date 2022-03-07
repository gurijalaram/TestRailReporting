package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.enums.BCSState;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BatchPartTest {

    private static Batch batch;
    private static NewPartRequest newPartRequest = null;
    private static Part part = null;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createBatch().getResponseEntity();
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));

        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        assertThat(part.getState(), is(equalTo(BCSState.LOADING.toString())));
    }

    @Test
    @TestRail(testCaseId = {"4280"})
    @Description("Create Batch and Add Part to a batch")
    public void createBatchPart() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();
        Batch batchObject = batchResponse.getResponseEntity();
        assertThat(batchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batchObject.getState(), is(equalTo(BCSState.CREATED.toString())));

        ResponseWrapper<Part> partResponse =  BatchPartResources.createNewBatchPartByID(batchObject.getIdentity());
        assertThat(partResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.LOADING.toString())));
    }

    @Test
    @TestRail(testCaseId = {"4280"})
    @Description("Get part to a batch")
    public void getBatchParts() {
        ResponseWrapper<Parts> partsResponse = BatchPartResources.getBatchPartById(batch.getIdentity());
        assertThat(partsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotEquals(partsResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"8690"})
    @Description("Attempt to add a new part to a batch using empty string values")
    public void createBatchPartWithEmptyStringValues() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setMaterialName("");
        newPartRequest.setVpeName("");
        ResponseWrapper<Part> response = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        assertEquals("Create batch with empty material name and vpename", HttpStatus.SC_CREATED, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8108"})
    @Description("Create part with invalid form data: annualVolume ")
    public void createBatchPartsInvalidFormDataAnnualVolume() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setAnnualVolume(123);
        ResponseWrapper<Part> partResponse =  BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        assertEquals("Response code didn't match expected code", HttpStatus.SC_CREATED, partResponse.getStatusCode());
        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.LOADING.toString())));
    }

    @AfterClass
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
