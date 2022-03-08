package com.apriori.bcs.tests;


public class BatchPartResourcesTest {
   /* private static Batch batch;
    private static Batch batch2;
    private static Part part;
    private static NewPartRequest newPartRequest;

    public static final String INVALID_BATCH_IDENTITY = "12345ABCD";


    @BeforeClass
    public static void testSetup() {
        ResponseWrapper<Object> responseWrapper1 = BatchResources.createBatch();
        Batch batch1 = (Batch)BatchResources.createBatch().getResponseEntity();
        ResponseWrapper<Object> responseWrapper2 = BatchResources.createBatch();
        Batch batch2 = (Batch)BatchResources.createBatch().getResponseEntity();
        newPartRequest = BatchPartRequest.getPartRequest();
        part = (Part) BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity()).getResponseEntity();
    }

    @AfterClass
    public static void testCleanup() {
      //  BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(testCaseId = {"4280"})
    @Description("Add a new part to a batch")
    public void createBatchParts() {
      //  BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8690"})
    @Description("Attempt to add a new part to a batch using empty string values")
    public void createBatchPartWithEmptyStringValues() {
        NewPartRequest newPartRequestEmptyString = BatchPartRequest.getPartRequest();
        newPartRequestEmptyString.setMaterialName("");
        newPartRequestEmptyString.setVpeName("");
        BatchPartResources.createNewBatchPartByID(newPartRequestEmptyString, batch.getIdentity());
    }


    @Ignore("Need to discuss what an invalid external id is.")
    @Test
    @TestRail(testCaseId = {"8107"})
    @Description("Create part with invalid form data: externalId ")
    public void createBatchPartsInvalidFormDataExternalId() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setExternalId("@@@@@@@@");
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8108"})
    @Description("Create part with invalid form data: annualVolume ")
    public void createBatchPartsInvalidFormDataAnnualVolume() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setAnnualVolume(123);
        testBadFormData(newPartRequest);
    }

    @Test
    @TestRail(testCaseId = {"8109"})
    @Description("Create part with invalid form data: batchSize ")
    public void createBatchPartsInvalidFormDataBatchSize() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setBatchSize(1287677863);
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8110"})
    @Description("Create part with invalid form data: productionLife ")
    public void createBatchPartsInvalidFormDataProductionLife() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setProductionLife("abc");
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8113"})
    @Description("Create part with missing form data: externalId ")
    public void createBatchPartsMissingFormDataExternalId() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setExternalId("");
        testBadFormData(newPartRequest);

    }


    @Ignore("Need to discuss what missing means. Is the value missing or the field itself missing.")
    @Test
    @TestRail(testCaseId = {"8114"})
    @Description("Create part with missing form data: annualVolume")
    public void createBatchPartsMissingFormDataAnnualVolume() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setAnnualVolume(null);
        testBadFormData(newPartRequest);

    }


    @Ignore("Need to discuss what missing means. Is the value missing or the field itself missing.")
    @Test
    @TestRail(testCaseId = {"8115"})
    @Description("Create part with missing form data: batchSize")
    public void createBatchPartsMissingFormDataBatchSize() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setBatchSize(null);
        testBadFormData(newPartRequest);

    }


    @Ignore("Need to discuss what missing means. Is the value missing or the field itself missing.")
    @Test
    @TestRail(testCaseId = {"8116"})
    @Description("Create part with missing form data: productionLife")
    public void createBatchPartsMissingFormDataProductionLife() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();
        newPartRequest.setProductionLife("");
        testBadFormData(newPartRequest);

    }

    @Test
    @TestRail(testCaseId = {"8100"})
    @Description("Create parts with a missing batch")
    public void createBatchPartsMissingBatch() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
            "", BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, null);
        Assert.assertEquals("Repsponse code didn't match expected code", partResponseWrapper.getStatusCode(),
            HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    @TestRail(testCaseId = {"8099"})
    @Description("Create part with an invalid customer")
    public void createBatchPartsInvalidBatch() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
            INVALID_BATCH_IDENTITY,
            BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, null
        );
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9535"})
    @Description("Create part with an invalid customer")
    public void createBatchPartsInvalidCustomer() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
            batch.getIdentity(),
            BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, INVALID_BATCH_IDENTITY
        );
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8101"})
    @Description("Create part with a missing customer")
    public void createBatchPartsMissingCustomer() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest();

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPICustomersEnum.POST_BATCH_PARTS_BY_ID, null)
            .inlineVariables(batch.getIdentity());


        requestEntity = this.prepareRequestToCreateBatch(requestEntity, newPartRequest,
            BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP.name()
        );

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity)
            .postMultipart();

        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    private RequestEntity prepareRequestToCreateBatch(RequestEntity requestEntity, NewPartRequest npr, String processGroup) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");

        File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(npr.getProcessGroup()),
            npr.getFilename());

        requestEntity.headers(headers)
            .multiPartFiles(new MultiPartFiles()
                .use("data", partFile)
            )
            .formParams(new FormParams()
                .use("filename", npr.getFilename())
                .use("externalId", String.format(npr.getExternalId(), System.currentTimeMillis()))
                .use("AnnualVolume", npr.getAnnualVolume().toString())
                .use("BatchSize", npr.getBatchSize().toString())
                .use("Description", npr.getDescription())
                //.use("PinnedRouting", npr.getPinnedRouting())
                .use("ProcessGroup", processGroup)
                .use("ProductionLife", npr.getProductionLife().toString())
                .use("ScenarioName", npr.getScenarioName() + System.currentTimeMillis())
                //.use("Udas", npr.getUdas())
                .use("VpeName", npr.getVpeName())
                .use("MaterialName", npr.getMaterialName())
                .use("generateWatchpointReport", "true")
            );

        return requestEntity;
    }

    @Ignore("Only one customer exists in CID-Perf")
    @Test
    @TestRail(testCaseId = {"9551"})
    @Description("Create part with mismatching identities")
    public void createBatchPartsIdentityMismatch() {
        NewPartRequest newPartRequest = BatchPartRequest.getPartRequest()

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
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCH_PARTS_BY_ID, Parts.class)
            .inlineVariables(batch.getIdentity());

        ResponseWrapper<Parts> partResponseWrapper = HTTPRequest.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, partResponseWrapper.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8096"})
    @Description("API return a list of Parts with a missing batch")
    public void getBatchPartsMissingBatch() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS, null);

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    @TestRail(testCaseId = {"8094"})
    @Description("Attempt to return a list of Parts using an invalid batch")
    public void getBatchPartsInvalidBatch() {

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_BY_ID, null)
            .inlineVariables(INVALID_BATCH_IDENTITY);

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8063"})
    @Description("Attempt to return a list of Parts using an invalid customer")
    public void getBatchPartsInvalidCustomer() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPICustomersEnum.GET_BATCH_PARTS_BY_CUSTOMER_ID, null)
            .inlineVariables(INVALID_BATCH_IDENTITY, batch.getIdentity());

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    @TestRail(testCaseId = {"8095"})
    @Description("Attempt to return a list of Parts using a missing customer")
    public void getBatchPartsMissingCustomer() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPICustomersEnum.GET_BATCH_PARTS, null)
            .inlineVariables(batch.getIdentity());

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Ignore("Only one customer exists in CID-Perf")
    @Test
    @TestRail(testCaseId = {"9552"})
    @Description("Return a list of Parts with mismatched identities")
    public void getBatchPartsMismatchedIdentities() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_BATCH_PARTS_BY_ID, null)
            .inlineVariables(batch2.getIdentity());

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

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
            INVALID_BATCH_IDENTITY, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9538"})
    @Description("Return a single Batch-Part using an invalid batch")
    public void getBatchPartInvalidBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getBatchPartRepresentation(INVALID_BATCH_IDENTITY,
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
            part.getIdentity(), null, INVALID_BATCH_IDENTITY);
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9537"})
    @Description("Return a single Batch-Part using a missing customer")
    public void getBatchPartMissingCustomer() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPICustomersEnum.GET_BATCH_PART_BY_BATCH_PART_IDS, null)
            .inlineVariables(batch.getIdentity(), part.getIdentity());

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

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
       *//* setBatchPartProperties()
            .createBatchPart();
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(getBatchIdentity(),
            getPartIdentity(),
            null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_CONFLICT);*//*
    }

    @Test
    @TestRail(testCaseId = {"9540"})
    @Description("Return the costing results for a part with an invalid batch")
    public void getResultsInvalidBatch() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(INVALID_BATCH_IDENTITY,
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
            null, INVALID_BATCH_IDENTITY);
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8127"})
    @Description("Return the costing results for a part with a missing customer")
    public void getResultsInvalidPart() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getResultsNoPoll(batch.getIdentity(),
            INVALID_BATCH_IDENTITY,
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
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPICustomersEnum.GET_PART_REPORT_BY_BATCH_PART_IDS, null)
            .inlineVariables(batch.getIdentity(), part.getIdentity());

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

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

        Object partDetails;
        BcsUtils.State isPartComplete = BcsUtils.State.PROCESSING;
        int count = 0;
        while (count <= Constants.POLLING_TIMEOUT) {
            partDetails =
                BatchPartResources.getBatchPartRepresentation(batch.getIdentity(), part.getIdentity()).getResponseEntity();
            isPartComplete = BcsUtils.pollState(partDetails, Part.class);

            if (isPartComplete.equals(BcsUtils.State.COMPLETED)) {
                break;
            }
            count += 1;
        }
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
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(INVALID_BATCH_IDENTITY,
            part.getIdentity(),
            null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"9548"})
    @Description("Return the watchpoint report for a part with a missing customer")
    public void getPartReportMissingCustomer() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPICustomersEnum.GET_PART_REPORT_BY_BATCH_PART_IDS, null)
            .inlineVariables(batch.getIdentity(), part.getIdentity());

        ResponseWrapper<Part> partResponseWrapper = HTTPRequest.build(requestEntity).get();

        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"9547"})
    @Description("Return the watchpoint report for a part with an invalid customer")
    public void getPartReportInvalidCustomer() {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(batch.getIdentity(), part.getIdentity(),
            null, INVALID_BATCH_IDENTITY);
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
            INVALID_BATCH_IDENTITY,
            null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(testCaseId = {"8128"})
    @Description("Return the watchpoint report for a part with no report")
    public void getPartReportNoReport() {
        *//*setBatchPartProperties()
            .createBatchPart();

        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.getPartReportNoPoll(getBatchIdentity(),
            getPartIdentity(), null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
            partResponseWrapper.getStatusCode(), HttpStatus.SC_CONFLICT);*//*
    }

    private void testBadFormData(NewPartRequest newPartRequest) {
        ResponseWrapper<Part> partResponseWrapper = BatchPartResources.createNewBatchPart(newPartRequest,
            batch.getIdentity(), BatchPartResources.ProcessGroupValue.USE_PROCESS_GROUP, null, null);
        Assert.assertEquals("Repsponse code didn't match expected code",
            HttpStatus.SC_BAD_REQUEST, partResponseWrapper.getStatusCode());
    }

*/
}
