package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cis.controller.BatchResources;
import com.apriori.cis.entity.response.Batch;
import com.apriori.cis.utils.CisUtils;
import com.apriori.cis.utils.Constants;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BatchResourcesTest extends TestUtil {


    @Test
    @TestRail(testCaseId = {"4284"})
    @Description("API returns a list of Batches in the CIS DB")
    public void createNewBatches() {

        Batch batch = BatchResources.createNewBatch();

        try {
            String batchIdentity = CisUtils.getIdentity(batch, Batch.class);
            Constants.setCisBatchIdentity(batchIdentity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
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
        BatchResources.getBatchRepresentation(Constants.getCisBatchIdentity());
    }


}
