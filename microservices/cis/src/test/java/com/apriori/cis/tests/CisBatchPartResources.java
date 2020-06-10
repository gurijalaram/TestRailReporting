package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cis.controller.BatchPartResources;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;

import org.junit.Test;

public class CisBatchPartResources extends TestUtil {
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
        BatchPartResources.getBatchPartRepresentation();
    }
}
