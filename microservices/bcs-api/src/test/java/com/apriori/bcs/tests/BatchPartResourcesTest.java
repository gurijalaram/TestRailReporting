package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.utils.BCSTestUtils;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.bcs.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class BatchPartResourcesTest extends BCSTestUtils {
    private static Batch batch;
    private static Batch batch2;
    private static Part part;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createNewBatch();
        batch2 = BatchResources.createNewBatch();
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
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
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();;
        BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8690"})
    @Description("Attempt to add a new part to a batch using empty string values")
    public void createBatchPartWithEmptyStringValues() {
        NewPartRequest newPartRequestNull = BatchPartResources.getNewPartRequest();
        newPartRequestNull.setMaterialName(null);
        newPartRequestNull.setVpeName(null);

        BatchPartResources.createNewBatchPart(newPartRequestNull, batch.getIdentity(),
                BatchPartResources.ProcessGroupValue.USE_NULL);

        NewPartRequest newPartRequestEmptyString = BatchPartResources.getNewPartRequest();
        newPartRequestEmptyString.setMaterialName("");
        newPartRequestEmptyString.setVpeName("");

        BatchPartResources.createNewBatchPart(newPartRequestEmptyString, batch.getIdentity(),
                BatchPartResources.ProcessGroupValue.USE_EMPTY_STRING);
    }


    @Ignore("Need to discuss what an invalid external id is.")
    @Test
    @TestRail(testCaseId = {"8107"})
    @Description("Create part with invalid form data: externalId ")
    public void createBatchPartsInvalidFormDataExternalId() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setExternalId("@@@@@@@@");
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8108"})
    @Description("Create part with invalid form data: annualVolume ")
    public void createBatchPartsInvalidFormDataAnnualVolume() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setAnnualVolume("abc");
        testBadFormData(newPartRequest);
    }

    @Test
    @TestRail(testCaseId = {"8109"})
    @Description("Create part with invalid form data: batchSize ")
    public void createBatchPartsInvalidFormDataBatchSize() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setBatchSize("abc");
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8110"})
    @Description("Create part with invalid form data: productionLife ")
    public void createBatchPartsInvalidFormDataProductionLife() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setProductionLife("abc");
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8113"})
    @Description("Create part with missing form data: externalId ")
    public void createBatchPartsMissingFormDataExternalId() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setExternalId("");
        testBadFormData(newPartRequest);

    }


    @Ignore("Need to discuss what missing means. Is the value missing or the field itself missing.")
    @Test
    @TestRail(testCaseId = {"8114"})
    @Description("Create part with missing form data: annualVolume")
    public void createBatchPartsMissingFormDataAnnualVolume() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setAnnualVolume("");
        testBadFormData(newPartRequest);

    }


    @Ignore("Need to discuss what missing means. Is the value missing or the field itself missing.")
    @Test
    @TestRail(testCaseId = {"8115"})
    @Description("Create part with missing form data: batchSize")
    public void createBatchPartsMissingFormDataBatchSize() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setBatchSize("");
        testBadFormData(newPartRequest);

    }


    @Ignore("Need to discuss what missing means. Is the value missing or the field itself missing.")
    @Test
    @TestRail(testCaseId = {"8116"})
    @Description("Create part with missing form data: productionLife")
    public void createBatchPartsMissingFormDataProductionLife() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setProductionLife("");
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8100"})
    @Description("Create parts with a missing batch")
    public void createBatchPartsMissingBatch() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
                "", BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, null);
        Assert.assertEquals("Repsponse code didn't match expected code", partResponseWrapper.getStatusCode(),
                HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    @TestRail(testCaseId = {"8099"})
    @Description("Create part with an invalid customer")
    public void createBatchPartsInvalidBatch() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
                Constants.INVALID_IDENTITY,
                BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, null
        );
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9535"})
    @Description("Create part with an invalid customer")
    public void createBatchPartsInvalidCustomer() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
                batch.getIdentity(),
                BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, Constants.INVALID_IDENTITY
                );
        Assert.assertEquals("Repsponse code didn't match expected code",
                        partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8101"})
    @Description("Create part with a missing customer")
    public void createBatchPartsMissingCustomer() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
                batch.getIdentity(),
                BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, ""
        );
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Ignore("Only one customer exists in CID-Perf")
    @Test
    @TestRail(testCaseId = {"9551"})
    @Description("Create part with mismatching identities")
    public void createBatchPartsIdentityMismatch() {
        NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
                batch2.getIdentity(),
                BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, ""
        );
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"4279"})
    @Description("Return a list of Parts for a specified Batch")
    public void getBatchParts() {
        BatchPartResources.getBatchParts(batch.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8096"})
    @Description("API return a list of Parts with a missing batch")
    public void getBatchPartsMissingBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchParts("", null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    @TestRail(testCaseId = {"8094"})
    @Description("Attempt to return a list of Parts using an invalid batch")
    public void getBatchPartsInvalidBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchParts(Constants.INVALID_IDENTITY, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8063"})
    @Description("Attempt to return a list of Parts using an invalid customer")
    public void getBatchPartsInvalidCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchParts(batch.getIdentity(), null,
                Constants.INVALID_IDENTITY);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    @TestRail(testCaseId = {"8095"})
    @Description("Attempt to return a list of Parts using a missing customer")
    public void getBatchPartsMissingCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchParts(batch.getIdentity(), null,
                "");
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Ignore("Only one customer exists in CID-Perf")
    @Test
    @TestRail(testCaseId = {"9552"})
    @Description("Return a list of Parts with mismatched identities")
    public void getBatchPartsMismatchedIdentities() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchParts(batch2.getIdentity(), null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    @TestRail(testCaseId = {"4281"})
    @Description("Return a a single Part")
    public void getBatchPart() {
        BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
            part.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8123"})
    @Description("Return a single Batch-Part using an invalid part")
    public void getBatchPartInvalidPart() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
                Constants.INVALID_IDENTITY, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9538"})
    @Description("Return a single Batch-Part using an invalid batch")
    public void getBatchPartInvalidBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchPartRepresentation(Constants.INVALID_IDENTITY,
                part.getIdentity(), null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9539"})
    @Description("Return single Batch-Part using a missing batch")
    public void getBatchPartMissingBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchPartRepresentation("",
                part.getIdentity(), null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9536"})
    @Description("Return a single Batch-Part using an invalid customer")
    public void getBatchPartInvalidCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
                part.getIdentity(), null, Constants.INVALID_IDENTITY);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9537"})
    @Description("Return a single Batch-Part using a missing customer")
    public void getBatchPartMissingCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
                part.getIdentity(), null, "");
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9553"})
    @Description("Returna single Batch-Part using mismatched identities")
    public void getBatchPartMismatchIdentities() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchPartRepresentation(batch2.getIdentity(),
                part.getIdentity(), null, "");
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }


    @Test
    @TestRail(testCaseId = {"7954"})
    @Description("Return the costing results for a part")
    public void getResults() {
        BatchPartResources.getResults(batch.getIdentity(), part.getIdentity());
    }


    @Test
    @TestRail(testCaseId = {"8125"})
    @Description("Return the costing results for a part with no results")
    public void getResultsNoResults() {
        setBatchPartProperties()
                .createBatchPart();
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(getBatchIdentity(),
                getPartIdentity(),
                null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(),HttpStatus.SC_CONFLICT);
    }

    @Test
    @TestRail(testCaseId = {"9540"})
    @Description("Return the costing results for a part with an invalid batch")
    public void getResultsInvalidBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(Constants.INVALID_IDENTITY,
                part.getIdentity(), null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9541"})
    @Description("Return the costing results for a part with a missing batch")
    public void getResultsMissingBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll("", part.getIdentity(),
                null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9542"})
    @Description("Return the costing results for a part with an invalid customer")
    public void getResultsInvalidCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(batch.getIdentity(),
                part.getIdentity(),
                null, Constants.INVALID_IDENTITY);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8127"})
    @Description("Return the costing results for a part with a missing customer")
    public void getResultsInvalidPart() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(batch.getIdentity(),
                Constants.INVALID_IDENTITY,
                null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                HttpStatus.SC_BAD_REQUEST, partResponseWrapper.getStatusCode());
    }

    @Ignore(" This is not neccessarily a negative. A url wih a missing part " +
            "will just return all parts for the specified batch. Need more info.")
    @Test
    @TestRail(testCaseId = {"8128"})
    @Description("Return the costing results for a part with a missing part")
    public void getResultsMissingPart() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(batch.getIdentity(),
                part.getIdentity(),
                null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9543"})
    @Description("Return the costing results for a part with a missing customer")
    public void getResultsMissingCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(batch.getIdentity(),
                part.getIdentity(),
                null, "");
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9544"})
    @Description("Return the costing results for a part with mismaiched identities")
    public void getResultsMismatchedIdentities() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(batch2.getIdentity(),
                part.getIdentity(), null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"7958"})
    @Description("Return the watchpoint report for a part")
    public void getPartReport() {
        BatchPartResources.getPartReport(batch.getIdentity(), part.getIdentity());
    }


    @Test
    @TestRail(testCaseId = {"9546"})
    @Description("Return the watchpoint report for a part with a missing batch")
    public void getPartReporMissingBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll("", part.getIdentity(),
                null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9545"})
    @Description("Return the watchpoint report for a part with an invalid batch")
    public void getPartReportInvalidBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(Constants.INVALID_IDENTITY,
                part.getIdentity(),
                null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9548"})
    @Description("Return the watchpoint report for a part with a missing customer")
    public void getPartReportMissingCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(batch.getIdentity(),
                part.getIdentity(), null, "");
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9547"})
    @Description("Return the watchpoint report for a part with an invalid customer")
    public void getPartReportInvalidCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(batch.getIdentity(), part.getIdentity(),
                null, Constants.INVALID_IDENTITY);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9550"})
    @Description("Return the watchpoint report for a part with a missing part")
    public void getPartReportMissingPart() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(batch.getIdentity(),
                "", null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                HttpStatus.SC_BAD_REQUEST, partResponseWrapper.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"9549"})
    @Description("Return the watchpoint report for a part with an invalid part")
    public void getPartReportInvalidPart() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(batch.getIdentity(),
                Constants.INVALID_IDENTITY,
                null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8128"})
    @Description("Return the watchpoint report for a part with no report")
    public void getPartReportNoReport() {
        setBatchPartProperties()
                .createBatchPart();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(getBatchIdentity(),
                getPartIdentity(), null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                partResponseWrapper.getStatusCode(), HttpStatus.SC_CONFLICT);
    }

    private void testBadFormData(NewPartRequest newPartRequest) {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
                batch.getIdentity(), BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
                HttpStatus.SC_BAD_REQUEST, partResponseWrapper.getStatusCode());
    }



}
