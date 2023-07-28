package com.apriori;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.controller.MultiPartResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.enums.BCSState;
import com.apriori.dataservice.TestDataService;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.part.PartData;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

@Slf4j
public class MultiPartCostingTest {
    private static Batch batch;
    private static final Integer NUMBER_OF_PARTS = Integer.parseInt(PropertiesContext.get("number_of_parts"));
    private static List<PartData> partDataList = null;

    @BeforeClass
    public static void testSetup() {
        partDataList = new TestDataService().getPartsFromCloud(NUMBER_OF_PARTS);
    }

    @Test
    @TestRail(id = {9111})
    @Issue("DEVOPS-2159")
    @Description("Test costing scenario, " +
        "1. creating a new batch" +
        "2. Number_of_parts (x number) and add different parts to batch, " +
        "3. Start batch costing and wait for the costing process to complete for all parts" +
        "4. Wait for batch costing process to complete" +
        "5. Log batch costing and parts costing results.")
    public void costBatchParts() {
        SoftAssertions softAssertions = new SoftAssertions();
        Batch batch = BatchResources.createBatch().getResponseEntity();
        MultiPartResources.addPartsToBatch(partDataList, batch.getIdentity());
        BatchResources.startBatchCosting(batch);
        softAssertions.assertThat(MultiPartResources.waitUntilBatchPartsCostingAreCompleted(batch.getIdentity())).isTrue();
        softAssertions.assertThat(BatchResources.waitUntilBatchCostingReachedExpected(batch.getIdentity(), BCSState.COMPLETED)).isTrue();
        ResponseWrapper<Batch> batchResponse = BatchResources.getBatchWithNoLogInfo(batch.getIdentity());
        Parts parts = BatchPartResources.getPartsByBatchId(batch.getIdentity()).getResponseEntity();
        MultiPartResources.summarizeBatchCostingInfo(batchResponse);
        MultiPartResources.summarizeAndLogPartsCostingInfo(parts);
        softAssertions.assertAll();
    }
}