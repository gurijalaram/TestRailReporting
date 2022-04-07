package com.apriori.bcs.tests;

import static org.junit.Assert.assertTrue;

import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.controller.MultiPartResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;


@Slf4j
public class MultiPartCostingTest {
    private static final Integer NUMBER_OF_PARTS = Integer.parseInt(PropertiesContext.get("${env}.bcs.number_of_parts"));
    private static Batch batch;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createBatch().getResponseEntity();
    }

    @Test
    @TestRail(testCaseId = {"9111"})
    @Description("Test costing scenario, includes creating a new batch, with multiple parts and waiting for the " +
        "costing process to complete for all parts. Then retrieve costing results.")
    public void costParts() {
        MultiPartResources.addPartsToBatch(NUMBER_OF_PARTS, batch.getIdentity());
        assertTrue("Track and verify All Batch Parts Costing is completed", MultiPartResources.waitUntilBatchPartsCostingAreCompleted(batch.getIdentity()));
        MultiPartResources.summarizeAndLogPartsCostingInfo(batch.getIdentity());
    }
}