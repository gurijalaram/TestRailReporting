package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.apibase.services.PropertyStore;

import com.apriori.cis.controller.BatchPartResources;
import com.apriori.cis.controller.BatchResources;
import com.apriori.cis.entity.response.Batch;
import com.apriori.cis.entity.request.NewPartRequest;

import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;

import org.junit.BeforeClass;
import org.junit.Test;

public class BatchPartResourcesTest extends TestUtil {
    private static PropertyStore propertyStore;
    private static Batch batch;

    @BeforeClass
    public static void testSetup() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(
        Thread.currentThread().getContextClassLoader().getResource("property-store.json").getPath(), PropertyStore.class);
        batch = BatchResources.createNewBatch();
    }

    @Test
    @TestRail(testCaseId = "4280")
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void createBatchParts() {

        NewPartRequest newPartRequest =
                (NewPartRequest)JsonManager.deserializeJsonFromFile(
                Thread.currentThread().getContextClassLoader().getResource("CreatePartData.json").getPath(), NewPartRequest.class);

        BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());
    }

    @Test
    @TestRail(testCaseId = "4279")
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void getBatchParts() {
        BatchPartResources.getBatchParts();
    }

    @Test
    @TestRail(testCaseId = "4281")
    @Description("API returns a representation of a single Batch-Part in the CIS DB")
    public void getBatchPart() {
        BatchPartResources.getBatchPartRepresentation(propertyStore.getBatchIdentity(), propertyStore.getPartIdentity());
    }
}
