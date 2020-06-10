package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cis.controller.BatchResources;
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
