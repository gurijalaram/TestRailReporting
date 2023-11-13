package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.controller.BatchResources;
import com.apriori.bcs.api.controller.MultiPartResources;
import com.apriori.bcs.api.enums.BCSState;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Parts;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@Slf4j
@ExtendWith(TestRulesAPI.class)
public class MultiPartCostingTest {
    private static Batch batch;
    private static final Integer NUMBER_OF_PARTS = Integer.parseInt(PropertiesContext.get("bcs.number_of_parts"));
    private static List<PartData> partDataList = null;

    @BeforeAll
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
        MultiPartResources.summarizeAndLogPartsCostingInfo(parts)
            .stream().forEach(part -> softAssertions.assertThat(part.getState()).isEqualTo(BCSState.COMPLETED.toString()));
        softAssertions.assertAll();
    }
}