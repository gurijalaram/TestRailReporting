package com.apriori.apitests.cis;

import com.apriori.apibase.services.cis.CisUtils;
import com.apriori.apibase.services.cis.apicalls.BatchResources;
import com.apriori.apibase.services.cis.objects.Batch;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CisBatchResources extends TestUtil {


    @Test
    @TestRail(testCaseId = "4284")
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
    @TestRail(testCaseId = "4275")
    @Description("API returns a list of Batches in the CIS DB")
    public void getBatches() {
        BatchResources.getBatches();
    }

    @Test
    @TestRail(testCaseId = "4277")
    @Description("API returns a representation of a single Batch in the CIS DB")
    public void getBatch() {
        BatchResources.getBatchRepresentation(Constants.getCisBatchIdentity());
    }


}
