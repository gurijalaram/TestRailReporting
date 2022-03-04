package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.PartReport;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.entity.response.Results;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class contains the methods related to batch-parts-controller APIs
 */
@Slf4j
public class BatchPartResources {
    private static RequestEntity requestEntity = null;
    private static final long WAIT_TIME = 300;

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
     * @param batchIdentity - batch id
     * @return Response
     */
    public static ResponseWrapper<Part> createNewBatchPartByID(NewPartRequest newPartRequest, String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest, batchIdentity);
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
            .inlineVariables(batchIdentity);
        return HTTPRequest.build(requestEntity).get();
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
            .inlineVariables(batchIdentity, partIdentity);
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
                .inlineVariables(batchIdentity, partIdentity);
            return HTTPRequest.build(requestEntity).get();
        }
        return null;
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
            .inlineVariables(batchIdentity, partIdentity);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Deserialize CreatePartData.json schema and returns POJO.
     *
     * @return NewPartRequest POJO.
     */
    public static NewPartRequest newPartRequest() {
        NewPartRequest newPartRequest =
            (NewPartRequest) JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);
        newPartRequest.setFilename("bracket_form.prt");
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
            (NewPartRequest) JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream(jsonSchema), NewPartRequest.class);
        newPartRequest.setFilename("bracket_form.prt");
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
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, Part.class).inlineVariables(batchIdentity);
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
                .inlineVariables(batchIdentity, partIdentity);
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
     * This is private method to set the form parameters used in creating batch part
     * request for batch id and customer id.
     *
     * @param newPartRequest - NewPartRequest POJO
     * @return RequestEntity -
     */
    private static RequestEntity setPartRequestFormParams(NewPartRequest newPartRequest) {
        File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(newPartRequest.getProcessGroup()), newPartRequest.getFilename());
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "multipart/form-data");
        requestEntity.headers(header)
            .multiPartFiles(new MultiPartFiles()
                .use("data", partFile))
            .formParams(new FormParams()
                .use("filename", newPartRequest.getFilename())
                .use("externalId", String.format(newPartRequest.getExternalId(), System.currentTimeMillis()))
                .use("AnnualVolume", newPartRequest.getAnnualVolume().toString())
                .use("BatchSize", newPartRequest.getBatchSize().toString())
                .use("Description", newPartRequest.getDescription())
                .use("ProcessGroup", newPartRequest.getProcessGroup())
                .use("ProductionLife", newPartRequest.getProductionLife().toString())
                .use("ScenarioName", newPartRequest.getScenarioName() + System.currentTimeMillis())
                .use("VpeName", newPartRequest.getVpeName())
                .use("MaterialName", newPartRequest.getMaterialName())
                .use("generateWatchpointReport", "true")
            );
        return requestEntity;
    }
}
