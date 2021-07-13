package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Cancel;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BatchResourcesTest extends TestUtil {
    private static Batch batch;

    @BeforeClass
    public static void testSetup() {

        batch = BatchResources.createNewBatch();
    }

    @AfterClass
    public static void testCleanup() {
        BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(testCaseId = {"4284"})
    @Description("API returns a list of Batches in the CIS DB")
    public void createNewBatches() {
        Batch batch = BatchResources.createNewBatch();
        boolean canceled = BcsUtils.checkAndCancelBatch(batch);
        Assert.assertTrue("Batch was canceled", canceled);
    }


    @Test
    @TestRail(testCaseId = {"4275"})
    @Description("API returns a list of Batches in the CIS DB")
    public void getBatches() {
        BatchResources.getBatches();
    }

    @Test
    @TestRail(testCaseId = {"4277"})
    @Description("API returns a representation of a single Batch in the CIS DB")
    public void getBatch() {
        BatchResources.getBatchRepresentation(batch.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"7906"})
    @Description("Cancel batch processing")
    public void cancelBatchProcessing() {
        ResponseWrapper<Cancel> responseWrapper = BatchResources.cancelBatchProccessing();
        String identity = responseWrapper.getResponseEntity().getIdentity();

        ResponseWrapper<Batch> batchResponseWrapper = BatchResources.getBatchRepresentation(identity);
        Assert.assertEquals("CANCELLED", batchResponseWrapper.getResponseEntity()
                .getState().toUpperCase());
    }


}
