package com.apriori.apitests.cis;

import com.apriori.apibase.services.cis.apicalls.BatchResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

public class CisBatchResources extends TestUtil {

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
        BatchResources.getBatchRepresentation();
    }

}
