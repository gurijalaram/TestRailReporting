package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.PartReport;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.entity.response.Results;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.utils.BcsTestUtils;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * This class contains the methods related to batch-parts-controller APIs
 */
public class BatchPartResources {

    private static final Logger logger = LoggerFactory.getLogger(BatchPartResources.class);
    private static RequestEntity requestEntity = null;


    /**
     * Creates a new batch part for specific batch ID.
     * @param batchIdentity - batch id
     * @param <T> response of object type
     * @return Response
     */
    public static <T> ResponseWrapper<T> createNewBatchPartByID(String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest(),batchIdentity);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Creates a new batch part for specific batch ID and custom NewPartRequest POJO
     * @param batchIdentity - batch id
     * @param <T> response of object type
     * @return Response
     */
    public static <T> ResponseWrapper<T> createNewBatchPartByID(NewPartRequest newPartRequest, String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest(),batchIdentity);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Create new batch part for a specific customer, batch ID and custom NewPartRequest POJO.
     * @param batchIdentity - Batch id
     * @param customerIdentity - customer id
     * @param <T> - Response of Object type
     * @return - Response
     */
    public static <T> ResponseWrapper<T> createNewBatchPartByBatchCustomerID(NewPartRequest newPartRequest,String batchIdentity,String customerIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest(),batchIdentity,customerIdentity);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Get all parts for a batch
     * @param batchIdentity - batch id
     * @param <T> - Response of Object Type
     * @return - Response
     */
    public static <T> ResponseWrapper<T> getBatchPartById(String batchIdentity) {
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, Parts.class)
                .inlineVariables(batchIdentity);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets a list of all parts associated to a specific batch
     * @param batchIdentity - The batch identity
     * @param partIdentity  - The identity of the part
     * @param <T> - The object types in the response
     * @return The response returned from the server
     */
    public static <T> ResponseWrapper<T> getBatchPartRepresentation(String batchIdentity, String partIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS, Part.class)
                .inlineVariables(batchIdentity, partIdentity);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets a batch part report for completed part.
     * @param batchIdentity - Batch ID
     * @param partIdentity - Part ID
     * @param <T> - Response of Object Type
     * @return - Response
     */
    public static <T> ResponseWrapper<T> getPartReport(String batchIdentity, String partIdentity) {
        if (BcsTestUtils.waitUntilPartStateIsCompleted(batchIdentity, partIdentity).toString().equals(BCSState.COMPLETED.toString())) {
            logger.info("Batch costing State is => " + BCSState.COMPLETED);
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
     * @param <T>           The object types in the response
     * @return The response returned from the server
     */
    public static <T> ResponseWrapper<T> getBatchPartResults(String batchIdentity, String partIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.RESULTS_BY_BATCH_PART_IDS, Results.class)
                .inlineVariables(batchIdentity, partIdentity);
        return HTTPRequest.build(requestEntity).get();
    }


    /**
     * Deserialize CreatePartData.json schema and returns POJO.
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
     * @param newPartRequest - Deserialized NewPartRequest POJO.
     * @param batchIdentity - Batch ID
     * @return  RequestEntity - Batch Part complete RequestEntity
     */
    public static RequestEntity batchPartRequestEntity(NewPartRequest newPartRequest, String batchIdentity) {
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, Part.class).inlineVariables(batchIdentity);
        return setPartRequestFormParams(newPartRequest);
    }

    /**
     * This overloaded method is to create Batch Part request entity for Batch ID and Customer ID.
     * @param newPartRequest - Deserialized NewPartRequest POJO.
     * @param batchIdentity - Batch ID
     * @param customerIdentity - Customer ID
     * @return  RequestEntity - Batch Part complete RequestEntity
     */
    public static RequestEntity batchPartRequestEntity(NewPartRequest newPartRequest, String batchIdentity, String customerIdentity) {
        return RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_CUSTOMER_BATCH_ID, Part.class)
                .inlineVariables(customerIdentity, batchIdentity);
    }

    /**
     * This is private method to set the form parameters used in creating batch part
     * request for batch id and customer id.
     * @param newPartRequest - NewPartRequest POJO
     * @return RequestEntity -
     */
    private static RequestEntity setPartRequestFormParams(NewPartRequest newPartRequest) {
        File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(newPartRequest.getProcessGroup()), newPartRequest.getFilename());
        Map<String,String> header = new HashMap<>();
        header.put("Accept","*/*");
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
