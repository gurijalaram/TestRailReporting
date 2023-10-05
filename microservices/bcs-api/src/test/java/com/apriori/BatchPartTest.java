package com.apriori;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.controller.MultiPartResources;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.models.request.parts.NewPartRequest;
import com.apriori.bcs.models.response.Batch;
import com.apriori.bcs.models.response.Part;
import com.apriori.bcs.models.response.Parts;
import com.apriori.bcs.models.response.Results;
import com.apriori.dataservice.TestDataService;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.ErrorMessage;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.part.PartData;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesApi.class)
public class BatchPartTest {

    private static Batch batch;
    private static Part part = null;
    private static Integer number_of_parts = Integer.parseInt(PropertiesContext.get("number_of_parts"));

    @BeforeAll
    public static void testSetup() {
        batch = BatchResources.createBatch().getResponseEntity();
        assertThat(batch.getState(), is(equalTo(BCSState.CREATED.toString())));

        part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        assertThat(part.getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(id = {9111})
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
    @TestRail(id = {4280})
    @Description("Create Batch and Add Part to a batch")
    public void createBatchPart() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();
        Batch batchObject = batchResponse.getResponseEntity();
        assertThat(batchObject.getState(), is(equalTo(BCSState.CREATED.toString())));

        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(batchObject.getIdentity());
        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(id = {7954})
    @Description("Create Batch, Add Part to a batch, wait until part is costed and get results")
    public void createBatchPartAndGetResults() {
        assertTrue(BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), part.getIdentity(), BCSState.COMPLETED), "Track and verify Batch Part Costing is completed");
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
    @TestRail(id = {9544})
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
    @TestRail(id = {4280, 4279})
    @Description("Get part to a batch")
    public void getBatchParts() {
        ResponseWrapper<Parts> partsResponse = BatchPartResources.getBatchPartById(batch.getIdentity());
        assertNotEquals(partsResponse.getResponseEntity().getItems().size(), 0);
    }

    @Test
    @TestRail(id = {8108})
    @Description("Create part with invalid form data: annualVolume ")
    public void createBatchPartsInvalidFormDataAnnualVolume() {
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setAnnualVolume(123);
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());

        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @Test
    @TestRail(id = {4280})
    @Description("Create part with Valid UDA Field in form data")
    public void createBatchPartWithValidUDAField() {
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartWithValidUDA(batch.getIdentity());

        assertThat(partResponse.getResponseEntity().getState(), is(equalTo(BCSState.CREATED.toString())));
    }

    @AfterAll
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
