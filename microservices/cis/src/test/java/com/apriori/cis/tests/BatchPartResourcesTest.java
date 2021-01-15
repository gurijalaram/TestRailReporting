package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cis.controller.BatchPartResources;
import com.apriori.cis.controller.BatchResources;
import com.apriori.cis.controller.PartResources;
import com.apriori.cis.entity.request.NewPartRequest;
import com.apriori.cis.entity.response.Batch;
import com.apriori.cis.entity.response.Part;
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

        part = BatchPartResources.createNewBatchPart(newPartRequest, batch.getResponse().getIdentity());

    }

    @Test
    @TestRail(testCaseId = "4280")
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void createBatchParts() {
        NewPartRequest newPartRequest =
            (NewPartRequest)JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);

        BatchPartResources.createNewBatchPart(newPartRequest, batch.getResponse().getIdentity());
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
        BatchPartResources.getBatchPartRepresentation(batch.getResponse().getIdentity(),
            part.getResponse().getIdentity());
    }
}
