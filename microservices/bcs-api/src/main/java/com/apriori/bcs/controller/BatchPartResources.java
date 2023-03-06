package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.PartReport;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.entity.response.Results;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.enums.FileType;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.part.PartData;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class contains the methods related to batch-parts-controller APIs
 */
@Slf4j
public class BatchPartResources {

    private static RequestEntity requestEntity = null;
    private static final long WAIT_TIME = 600;
    private static String batchID;
    static ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);

    /**
     * Creates a new batch part for specific batch ID.
     *
     * @param batchIdentity - batch id
     * @return Response
     */
    public static ResponseWrapper<Part> createNewBatchPartByID(String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest(), batchIdentity);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Creates a new batch part for specific batch ID and custom NewPartRequest POJO
     *
     * @param newPartRequest - Deserialized NewPartRequest Object
     * @param batchIdentity  - batch Identity
     * @return Response of type part object
     */
    public static ResponseWrapper<Part> createNewBatchPartByID(NewPartRequest newPartRequest, String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest, batchIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Creates a new batch part for specific batch ID and custom NewPartRequest POJO
     *
     * @param partData      - PartData Object retrieved from cloud
     * @param batchIdentity - batch Identity
     * @return Response of type part object
     */
    public static ResponseWrapper<Part> createNewBatchPartByID(PartData partData, String batchIdentity) {
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_CUSTOMER_BATCH_ID, Part.class).inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity);
        Map<String, String> header = new HashMap<>();
        QueryParams queryParams = new QueryParams();
        partData.setScenarioName("Scenario" + System.currentTimeMillis());
        partData.setExternalId("External" + System.currentTimeMillis());
        partData.setDescription("Description" + System.currentTimeMillis());
        queryParams = (partData.getFilename() != null) ? queryParams.use("filename", partData.getFilename()) : queryParams;
        queryParams = (partData.getExternalId() != null) ? queryParams.use("externalId", String.format(partData.getExternalId(), System.currentTimeMillis())) : queryParams;
        queryParams = (partData.getAnnualVolume() != null) ? queryParams.use("AnnualVolume", partData.getAnnualVolume().toString()) : queryParams;
        queryParams = (partData.getBatchSize() != null) ? queryParams.use("BatchSize", partData.getBatchSize().toString()) : queryParams;
        queryParams = (partData.getDescription() != null) ? queryParams.use("Description", partData.getDescription()) : queryParams;
        queryParams = (partData.getProcessGroup() != null) ? queryParams.use("ProcessGroup", partData.getProcessGroup()) : queryParams;
        queryParams = (partData.getProductionLife() != null) ? queryParams.use("ProductionLife", partData.getProductionLife().toString()) : queryParams;
        queryParams = (partData.getScenarioName() != null) ? queryParams.use("ScenarioName", partData.getScenarioName()) : queryParams;
        queryParams = (partData.getDigitalFactory() != null) ? queryParams.use("VpeName", partData.getDigitalFactory()) : queryParams;
        queryParams = (partData.getMaterial() != null) ? queryParams.use("MaterialName", partData.getMaterial()) : queryParams;
        queryParams = (partData.getGenerateWatchPointReport() != null) ? queryParams.use("generateWatchpointReport", partData.getGenerateWatchPointReport()) : queryParams;
        queryParams = (partData.getUdas() != null) ? queryParams.use("udas", partData.getUdas()) : queryParams;

        header.put("Accept", "*/*");
        header.put("Content-Type", "multipart/form-data");
        requestEntity.headers(header)
            .multiPartFiles(new MultiPartFiles()
                .use("data", partData.getFile()))
            .queryParams(queryParams);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Creates a new batch part for specific batch ID and custom NewPartRequest POJO
     *
     * @param newPartRequest - Deserialized NewPartRequest Object
     * @param batchIdentity  - batch Identity
     * @return Response of type part object
     * @Param return class name
     */
    public static <T> ResponseWrapper<T> createNewBatchPartByID(NewPartRequest newPartRequest, String batchIdentity, Class<T> klass) {
        requestEntity = batchPartRequestEntity(newPartRequest, batchIdentity, klass)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * This overloaded method is to create Batch Part request entity for Batch ID.
     *
     * @param endPoint      BCSAPIEnum
     * @param batchIdentity - batch id
     * @param partIdentity  - part id
     * @param klass         - return class name
     * @return RequestEntity object
     */
    public static <T> RequestEntity getBatchPartRequestEntity(BCSAPIEnum endPoint, String batchIdentity, String partIdentity, Class<T> klass) {
        return RequestEntityUtil.init(endPoint, klass).inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity, partIdentity);
    }

    /**
     * Creates a new batch part for specific batch ID by passing uda field.
     *
     * @param batchIdentity - batch id
     * @return Response
     */
    public static ResponseWrapper<Part> createNewBatchPartWithValidUDA(String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest("schemas/testdata/CreatePartDataWithUda.json"), batchIdentity);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Create new batch part for a specific customer, batch ID and custom NewPartRequest POJO.
     *
     * @param batchIdentity    - Batch id
     * @param customerIdentity - customer id
     * @return - Response
     */
    public static ResponseWrapper<Part> createNewBatchPartByBatchCustomerID(NewPartRequest newPartRequest, String batchIdentity, String customerIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest, batchIdentity, customerIdentity);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Get all parts for a batch
     *
     * @param batchIdentity - batch id
     * @return - Response
     */
    public static ResponseWrapper<Parts> getBatchPartById(String batchIdentity) {
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, Parts.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity)
            .queryParams(new QueryParams().use("pageSize", PropertiesContext.get("number_of_parts")))
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get all parts for a batch with batch id and
     * log information only when error occurs
     *
     * @param batchIdentity - batch id
     * @return - Response
     */
    public static ResponseWrapper<Parts> getPartsByBatchId(String batchIdentity) {
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, Parts.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity)
            .queryParams(new QueryParams().use("pageSize", PropertiesContext.get("number_of_parts")));
        return HTTPRequest.build(requestEntity).getMultipart();
    }

    /**
     * Gets a list of all parts associated to a specific batch
     *
     * @param batchIdentity - The batch identity
     * @param partIdentity  - The identity of the part
     * @return The response returned from the server
     */
    public static ResponseWrapper<Part> getBatchPartRepresentation(String batchIdentity, String partIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS, Part.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity, partIdentity);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets a batch part report for completed part.
     *
     * @param batchIdentity - Batch ID
     * @param partIdentity  - Part ID
     * @return - Response
     */
    public static ResponseWrapper<PartReport> getPartReport(String batchIdentity, String partIdentity) {
        if (BatchPartResources.waitUntilPartStateIsCompleted(batchIdentity, partIdentity)) {
            log.info("Batch Part State is => " + BCSState.COMPLETED);
            RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.PART_REPORT_BY_BATCH_PART_IDS, PartReport.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity, partIdentity)
                .expectedResponseCode(HttpStatus.SC_OK);
            return HTTPRequest.build(requestEntity).get();
        }
        // TODO: 27/09/2022 if null is returned and the test fails you will get a null pointer. this should be coded to catch the npe or recoded
        return null;
    }

    /**
     * Create batch part with non-supported file types.
     *
     * @param newPartRequest - deserialized pojo.
     * @param batchIdentity  - batch Identity
     * @param fileType       - (txt or pdf)
     * @return - response of class (ErrorMessage) object
     */
    public static ResponseWrapper<ErrorMessage> createBatchPartWithInvalidData(NewPartRequest newPartRequest, String batchIdentity, FileType fileType) {
        RequestEntity requestEntity = BatchPartResources.batchPartRequestEntity(newPartRequest, batchIdentity, ErrorMessage.class);
        switch (fileType.toString()) {
            case "TXT":
                requestEntity.multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getLocalResourceFile("schemas/partfiles/Testfile.txt")));
                break;
            case "PDF":
                requestEntity.multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getLocalResourceFile("schemas/partfiles/TestFile.pdf")));
                break;
            default:
                requestEntity.multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getLocalResourceFile("schemas/partfiles/TestFile.jpeg")));
        }
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Gets costing results a specific batch & part
     *
     * @param batchIdentity The batch identity to retrieve parts for
     * @param partIdentity  The identity of the part to retrieve
     * @return The response returned from the server
     */
    public static ResponseWrapper<Results> getBatchPartResults(String batchIdentity, String partIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.RESULTS_BY_BATCH_PART_IDS, Results.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity, partIdentity);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Deserialize CreatePartData.json schema and returns POJO.
     *
     * @return NewPartRequest POJO.
     */
    public static NewPartRequest newPartRequest() {
        NewPartRequest newPartRequest =
            JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream("schemas/testdata/CreatePartData.json"), NewPartRequest.class);
        return newPartRequest;
    }

    /**
     * Deserialize custom json schema and returns POJO
     *
     * @param jsonSchema can be customized version of CreatePartData.json
     * @return NewPartRequest POJO.
     */
    public static NewPartRequest newPartRequest(String jsonSchema) {
        NewPartRequest newPartRequest =
            JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream(jsonSchema), NewPartRequest.class);
        return newPartRequest;
    }

    /**
     * This overloaded method is to create Batch Part request entity for Batch ID.
     *
     * @param newPartRequest - Deserialized NewPartRequest POJO.
     * @param batchIdentity  - Batch ID
     * @return RequestEntity - Batch Part complete RequestEntity
     */
    public static RequestEntity batchPartRequestEntity(NewPartRequest newPartRequest, String batchIdentity) {
        requestEntity = RequestEntityUtil.init(
            BCSAPIEnum.BATCH_PARTS_BY_CUSTOMER_BATCH_ID, Part.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED);
        return setPartRequestFormParams(newPartRequest);
    }

    /**
     * This overloaded method is to create Batch Part request entity for Batch ID.
     *
     * @param newPartRequest - Deserialized NewPartRequest POJO.
     * @param batchIdentity  - Batch ID
     * @param klass          - Response class
     * @return RequestEntity - Batch Part complete RequestEntity
     */
    public static <T> RequestEntity batchPartRequestEntity(NewPartRequest newPartRequest, String batchIdentity, Class<T> klass) {
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, klass).inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity);
        return setPartRequestFormParams(newPartRequest);
    }

    /**
     * This overloaded method is to create Batch Part request entity for Batch ID and Customer ID.
     *
     * @param newPartRequest   - Deserialized NewPartRequest POJO.
     * @param batchIdentity    - Batch ID
     * @param customerIdentity - Customer ID
     * @return RequestEntity - Batch Part complete RequestEntity
     */
    public static RequestEntity batchPartRequestEntity(NewPartRequest newPartRequest, String batchIdentity, String customerIdentity) {
        return RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_CUSTOMER_BATCH_ID, Part.class)
            .inlineVariables(customerIdentity, batchIdentity);
    }

    /**
     * Checks an wait until the batch part status is completed
     *
     * @param batchIdentity - Batch ID to send
     * @param partIdentity  - Part ID to send
     * @return BCSState
     */
    public static boolean waitUntilPartStateIsCompleted(String batchIdentity, String partIdentity) {
        long initialTime = System.currentTimeMillis() / 1000;
        Part part;
        do {
            requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS, Part.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity, partIdentity);
            part = (Part) HTTPRequest.build(requestEntity).get().getResponseEntity();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!part.getState().equals(BCSState.COMPLETED.toString())
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return (part.getState().equals(BCSState.COMPLETED.toString())) ? true : false;
    }

    /**
     * Checks an wait until the batch part status is completed
     *
     * @param batchIdentity    - Batch ID to send
     * @param partIdentity     - Part ID to send
     * @param bcsExpectedState - Expected State
     * @return boolean - true (expected state matches actual) or false
     */
    public static boolean waitUntilPartStateIsCompleted(String batchIdentity, String partIdentity, BCSState bcsExpectedState) {
        long initialTime = System.currentTimeMillis() / 1000;
        Part part;
        do {
            requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS, Part.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), batchIdentity, partIdentity);
            part = (Part) HTTPRequest.build(requestEntity).get().getResponseEntity();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!part.getState().equals(bcsExpectedState.toString())
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return (part.getState().equals(bcsExpectedState.toString())) ? true : false;
    }


    /**
     * This is private method to set the form parameters used in creating batch part
     * request for batch id and customer id.
     *
     * @param newPartRequest - NewPartRequest POJO
     * @return RequestEntity -
     */
    private static RequestEntity setPartRequestFormParams(NewPartRequest newPartRequest) {
        File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(newPartRequest.getProcessGroup()), newPartRequest.getFilename());
        Map<String, String> header = new HashMap<>();
        QueryParams queryParams = new QueryParams();
        if (newPartRequest.getScenarioName().equals("Unique")) {
            newPartRequest.setScenarioName("Scenario" + System.currentTimeMillis());
        }
        queryParams = (newPartRequest.getFilename() != null) ? queryParams.use("filename", newPartRequest.getFilename()) : queryParams;
        queryParams = (newPartRequest.getExternalId() != null) ? queryParams.use("externalId", String.format(newPartRequest.getExternalId(), System.currentTimeMillis())) : queryParams;
        queryParams = (newPartRequest.getAnnualVolume() != null) ? queryParams.use("AnnualVolume", newPartRequest.getAnnualVolume().toString()) : queryParams;
        queryParams = (newPartRequest.getBatchSize() != null) ? queryParams.use("BatchSize", newPartRequest.getBatchSize().toString()) : queryParams;
        queryParams = (newPartRequest.getDescription() != null) ? queryParams.use("Description", newPartRequest.getDescription()) : queryParams;
        queryParams = (newPartRequest.getProcessGroup() != null) ? queryParams.use("ProcessGroup", newPartRequest.getProcessGroup()) : queryParams;
        queryParams = (newPartRequest.getProductionLife() != null) ? queryParams.use("ProductionLife", newPartRequest.getProductionLife().toString()) : queryParams;
        queryParams = (newPartRequest.getScenarioName() != null) ? queryParams.use("ScenarioName", newPartRequest.getScenarioName()) : queryParams;
        queryParams = (newPartRequest.getDigitalFactory() != null) ? queryParams.use("VpeName", newPartRequest.getDigitalFactory()) : queryParams;
        queryParams = (newPartRequest.getMaterial() != null) ? queryParams.use("MaterialName", newPartRequest.getMaterial()) : queryParams;
        queryParams = (newPartRequest.getGenerateWatchPointReport() != null) ? queryParams.use("generateWatchpointReport", newPartRequest.getGenerateWatchPointReport()) : queryParams;
        queryParams = (newPartRequest.getUdas() != null) ? queryParams.use("udas", newPartRequest.getUdas()) : queryParams;

        header.put("Accept", "*/*");
        header.put("Content-Type", "multipart/form-data");
        requestEntity.headers(header)
            .multiPartFiles(new MultiPartFiles()
                .use("data", partFile))
            .queryParams(queryParams);
        return requestEntity;
    }
}
