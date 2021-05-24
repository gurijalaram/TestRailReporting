package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

public class BatchPartResourcesTest extends TestUtil {
    private static Batch batch;
    private static Part part;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createNewBatch();

        NewPartRequest newPartRequest =
            (NewPartRequest)JsonManager.deserializeJsonFromInputStream(
                    FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);
        newPartRequest.setFilename("bracket_form.prt");

        part = BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());

    }

    @Test
    @TestRail(testCaseId = {"4280"})
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void createBatchParts() {
        NewPartRequest newPartRequest =
            (NewPartRequest)JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);
        newPartRequest.setFilename("bracket_new.prt");

        BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"4279"})
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void getBatchParts() {
        BatchPartResources.getBatchParts();
    }

    @Test
    @TestRail(testCaseId = {"4281"})
    @Description("API returns a representation of a single Batch-Part in the CIS DB")
    public void getBatchPart() {
        BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
            part.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"7954"})
    @Description("Return the costing results for a part")
    public void getResults() {
        BatchPartResources.getResults(batch.getIdentity(), part.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"7958"})
    @Description("Return the costing results for a part")
    public void getPartReport() {
        BatchPartResources.getPartReport(batch.getIdentity(), part.getIdentity());
    }
}
