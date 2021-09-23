package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BatchPartResourcesTest extends TestUtil {
    private static Batch batch;
    private static Part part;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createNewBatch();
        NewPartRequest newPartRequest = getNewPartRequest();
        part = (Part)BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity()).getResponseEntity();

    }

    @AfterClass
    public static void testCleanup() {
        BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(testCaseId = {"4280"})
    @Description("Add a new part to a batch")
    public void createBatchParts() {
        NewPartRequest newPartRequest = getNewPartRequest();
        BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());
    }

    @Test
    @Issue("AP-69614")
    @TestRail(testCaseId = {"8690"})
    @Description("Attempt to add a new part to a batch using empty string values")
    public void createBatchPartWithEmptyStringValues() {
        NewPartRequest newPartRequestNull = getNewPartRequest();
        newPartRequestNull.setMaterialName(null);
        newPartRequestNull.setVpeName(null);

        BatchPartResources.createNewBatchPart(newPartRequestNull, batch.getIdentity(),
                BatchPartResources.ProcessGroupValue.USE_NULL);

        NewPartRequest newPartRequestEmptyString = getNewPartRequest();
        newPartRequestEmptyString.setMaterialName("");
        newPartRequestEmptyString.setVpeName("");

        BatchPartResources.createNewBatchPart(newPartRequestEmptyString, batch.getIdentity(),
                BatchPartResources.ProcessGroupValue.USE_EMPTY_STRING);




    }

    @Test
    @TestRail(testCaseId = {"4279"})
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void getBatchParts() {
        BatchPartResources.getBatchParts(batch.getIdentity());
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

    /**
     * Generate a newpartrequest
     *
     * @return newPartRequest
     */
    private static NewPartRequest getNewPartRequest() {
        NewPartRequest newPartRequest =
                (NewPartRequest)JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);
        newPartRequest.setFilename("bracket_form.prt");

        return newPartRequest;
    }


}
