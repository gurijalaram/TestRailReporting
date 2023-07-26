package com.apriori.bcs.tests;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.controller.MultiPartResources;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.dataservice.TestDataService;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.part.PartData;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class BatchPartTestDemo {

    private static Batch batch;
    private static NewPartRequest newPartRequest = null;
    private static Part part = null;
    private static List<PartData> partDataList = null;

    @BeforeClass
    public static void testSetup() {
        System.out.println("asfadfadsf" + PropertiesContext.get("number_of_parts"));
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

    @AfterClass
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
