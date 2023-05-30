package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.controller.MultiPartResources;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.entity.response.Results;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.part.PartData;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class BatchPartTest {

    private static Batch batch;
    private static Part part = null;
    private static Integer number_of_parts = Integer.parseInt(PropertiesContext.get("number_of_parts"));

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createBatch().getResponseEntity();
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));

        part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        assertThat(part.getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(testCaseId = {"9111"})
    @Description("Test costing scenario" +
        "1. Create a new batch, " +
        "2. Add 10 parts to batch " +
        "3. Wait for the costing process to complete for all parts" +
        "4. Log Parts costing results.")
    public void cost10Parts() {
        List<PartData> partDataList = new TestDataService().getPartsFromCloud(number_of_parts);
        SoftAssertions softAssertions = new SoftAssertions();
        Batch batch = BatchResources.createBatch().getResponseEntity();
        MultiPartResources.addPartsToBatch(partDataList, batch.getIdentity());
        softAssertions.assertThat(MultiPartResources.waitUntilBatchPartsCostingAreCompleted(batch.getIdentity())).isTrue();
        Parts parts = BatchPartResources.getBatchPartById(batch.getIdentity()).getResponseEntity();
        MultiPartResources.summarizeAndLogPartsCostingInfo(parts);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"4280"})
    @Description("Create Batch and Add Part to a batch")
    public void createBatchPart() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();
        Batch batchObject = batchResponse.getResponseEntity();
        assertThat(batchObject.getState(), is(equalTo(BCSState.CREATED.toString())));

        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(batchObject.getIdentity());
        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(testCaseId = {"7954"})
    @Description("Create Batch, Add Part to a batch, wait until part is costed and get results")
    public void createBatchPartAndGetResults() {
        assertTrue("Track and verify Batch Part Costing is completed", BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), part.getIdentity(), BCSState.COMPLETED));
        ResponseWrapper<Results> resultsResponse = HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                BCSAPIEnum.RESULTS_BY_BATCH_PART_IDS,
                batch.getIdentity(),
                part.getIdentity(),
                Results.class)
            .expectedResponseCode(HttpStatus.SC_OK)
        ).get();

        assertThat(resultsResponse.getResponseEntity().getCostingStatus(), is(equalTo("COST_COMPLETE")));
    }

    @Test
    @TestRail(testCaseId = {"9544"})
    @Description("Create 2 Batches, Add Part to a batch1, get the part for batch2 and verify error")
    public void createBatchPartFromMismatchedBatch() {
        Batch batch2 = BatchResources.createBatch().getResponseEntity();
        assertThat(batch2.getState(), is(equalTo(BCSState.CREATED.toString())));

        HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS,
                batch2.getIdentity(),
                part.getIdentity(),
                ErrorMessage.class)
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND)
        ).get();
    }

    @Test
    @TestRail(testCaseId = {"4280", "4279"})
    @Description("Get part to a batch")
    public void getBatchParts() {
        ResponseWrapper<Parts> partsResponse = BatchPartResources.getBatchPartById(batch.getIdentity());
        assertNotEquals(partsResponse.getResponseEntity().getItems().size(), 0);
    }


    @Test
    @TestRail(testCaseId = {"8108"})
    @Description("Create part with invalid form data: annualVolume ")
    public void createBatchPartsInvalidFormDataAnnualVolume() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setAnnualVolume(123);
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());

        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(testCaseId = {"4280"})
    @Description("Create part with Valid UDA Field in form data")
    public void createBatchPartWithValidUDAField() {
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartWithValidUDA(batch.getIdentity());

        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @AfterClass
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
