package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.controller.BatchResources;
import com.apriori.bcs.api.controller.MultiPartResources;
import com.apriori.bcs.api.models.request.parts.NewPartRequest;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.bcs.api.models.response.Parts;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class BatchPartTestDemo {

    private static Batch batch;
    private static NewPartRequest newPartRequest = null;
    private static Part part = null;
    private static List<PartData> partDataList = null;

    @BeforeAll
    public static void testSetup() {
        System.out.println("asfadfadsf" + PropertiesContext.get("bcs.number_of_parts"));
        partDataList = new TestDataService().getPartsFromCloud(2);

    }

    @Test
    @TestRail(id = {9111})
    @Description("Test costing scenario" +
        "1. Create a new batch, " +
        "2. Add 10 parts to batch " +
        "3. Wait for the costing process to complete for all parts" +
        "4. Log Parts costing results.")
    public void cost10Parts() {
        SoftAssertions softAssertions = new SoftAssertions();
        Batch batch = BatchResources.createBatch().getResponseEntity();
        MultiPartResources.addPartsToBatch(partDataList, batch.getIdentity());
        softAssertions.assertThat(MultiPartResources.waitUntilBatchPartsCostingAreCompleted(batch.getIdentity())).isTrue();
        Parts parts = BatchPartResources.getBatchPartById(batch.getIdentity()).getResponseEntity();
        MultiPartResources.summarizeAndLogPartsCostingInfo(parts);
        softAssertions.assertAll();
    }

    @AfterAll
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
