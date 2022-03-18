package com.apriori.bcs.controller;

import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.PartReport;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.entity.response.Results;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.enums.FileType;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.database.dto.BCSPartBenchmarkingDTO;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.part.PartUtil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This class contains the methods related to batch-parts-controller APIs
 */
@Slf4j
public class BatchPartResources {

    private static RequestEntity requestEntity = null;
    private static Map<String, BCSPartBenchmarkingDTO> partsCollector;
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
     * @param newPartRequest - Deserialized NewPartRequest Object
     * @param batchIdentity  - batch Identity
     * @return Response of type part object
     */
    public static ResponseWrapper<Part> createNewBatchPartByID(NewPartRequest newPartRequest, String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest, batchIdentity);
        return HTTPRequest.build(requestEntity).postMultipart();
    }

    /**
     * Creates a new batch part for specific batch ID by passing uda field.
     *
     * @param batchIdentity - batch id
     * @return Response
     */
    public static ResponseWrapper<Part> createNewBatchPartWithValidUDA(String batchIdentity) {
        requestEntity = batchPartRequestEntity(newPartRequest("schemas/requests/CreatePartDataWithUda.json"), batchIdentity);
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
     * This overloaded method is to create Batch Part request entity for Batch ID.
     *
     * @param newPartRequest - Deserialized NewPartRequest POJO.
     * @param batchIdentity  - Batch ID
     * @param klass          - Response class
     * @return RequestEntity - Batch Part complete RequestEntity
     */
    public static RequestEntity batchPartRequestEntity(NewPartRequest newPartRequest, String batchIdentity, Class klass) {
        requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, klass).inlineVariables(batchIdentity);
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
                .inlineVariables(batchIdentity, partIdentity);
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
     * Add number of parts (configured in config file) to batch
     *
     * @param numberOfParts- number of parts
     * @param batchIdentity- Batch Identity
     */
    public static void addPartsToBatch(int numberOfParts, String batchIdentity) {
        partsCollector = new HashMap<>();

        for (int i = 0; i < numberOfParts; i++) {
            NewPartRequest newPartRequest = getNewPartRequestAndOverridePartData(BatchPartResources.newPartRequest());
            Part batchPart = BatchPartResources.createNewBatchPartByID(newPartRequest, batchIdentity).getResponseEntity();
            BCSPartBenchmarkingDTO benchData = consolidatePartsStatus(batchPart, newPartRequest);
            benchData.setBatchIdentity(batchIdentity);
            partsCollector.put(batchPart.getIdentity(), benchData);
        }
    }

    /**
     * Checks an wait until the batch part status is completed
     *
     * @param batchIdentity - Batch ID to send
     * @return boolean - true or false (true => all parts are in finished state, false => all parts are not processed in 5 minutes)
     */
    public static boolean waitUntilBatchPartsCostingAreCompleted(String batchIdentity) {
        long initialTime = System.currentTimeMillis() / 1000;
        Parts parts;
        do {
            parts = BatchPartResources.getBatchPartById(batchIdentity).getResponseEntity();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

        } while (getFinishedPartsCount(parts.getItems()) != parts.getItems().size() && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return (getFinishedPartsCount(parts.getItems()) == parts.getItems().size()) ? true : false;
    }

    /**
     * Summarize the batch parts costing state and reports the performance metrics for each part in a batch.
     *
     * @param batchIdentity
     */
    public static void summarizeAndLogPartsCostingInfo(String batchIdentity) {
        Parts parts = BatchPartResources.getBatchPartById(batchIdentity).getResponseEntity();
        parts.getItems().forEach(part -> {
            BCSPartBenchmarkingDTO benchData = partsCollector.get(part.getIdentity());
            benchData.setState(part.getState());
            benchData.setCostingResults(part.getCostingResult());
            benchData.setErrorMessage(part.getErrors());
            benchData.setCostingDuration(part.getUpdatedAt());
            partsCollector.put(part.getIdentity(), benchData);
        });

        List<BCSPartBenchmarkingDTO> partsReport = partsCollector.values()
            .stream()
            .collect(Collectors.toList());

        String formattedString = getFormattedString(partsReport);

        log.info("================== Batch Parts Benchmarking Data ==================");
        log.info("--------------------------------------------------------------------------------------------------------------------------------------");
        log.info(String.format(formattedString, "PART_ID", "PART_NAME", "PROCESS_GROUP", "PART_STATE", "COSTING_RESULT", "PROCESSING_TIME", "ERRORS"));
        log.info("--------------------------------------------------------------------------------------------------------------------------------------");

        for (BCSPartBenchmarkingDTO partDTO : partsReport) {
            log.info(String.format(formattedString, partDTO.getIdentity(), partDTO.getPartName(),
                partDTO.getProcessGroup(), partDTO.getState(), partDTO.getCostingResults(),
                BcsUtils.convertSecsToMins(partDTO.getCostingDuration()), partDTO.getErrorMessage()));
        }
        log.info("--------------------------------------------------------------------------------------------------------------------------------------");

        for (BCSPartBenchmarkingDTO partDTO : partsReport) {
            Assert.assertEquals("Verfiy Part State", BCSState.COMPLETED.toString(), partDTO.getState());
        }
    }

    /**
     * Overides NewPartRequest class object with deserialized json test data with aws cloud data files
     *
     * @param newPartRequest
     * @return newPartRequest - deserialized class object
     */
    private static NewPartRequest getNewPartRequestAndOverridePartData(NewPartRequest newPartRequest) {
        PartData partData = PartUtil.getPartData();

        newPartRequest.setFilename(partData.getFileName());
        newPartRequest.setProcessGroup(partData.getProcessGroup());
        newPartRequest.setMaterialName(partData.getMaterial());
        newPartRequest.setAnnualVolume(partData.getAnnualVolume());
        newPartRequest.setBatchSize(partData.getBatchSize());

        return newPartRequest;
    }

    /**
     * Consolidates Part class data and New Part Request for reporting purpose
     *
     * @param batchPart
     * @param newPartRequest
     * @return BCSPartBenchmarkingDTO object - combined version of Part and NewPartRequest class object
     */
    private static BCSPartBenchmarkingDTO consolidatePartsStatus(Part batchPart, NewPartRequest newPartRequest) {
        BCSPartBenchmarkingDTO benchData = batchPart.convertToBCSPartBenchData();
        benchData.setFilename(newPartRequest.getFilename());
        benchData.setProcessGroup(newPartRequest.getProcessGroup());
        benchData.setMaterialName(newPartRequest.getMaterialName());
        benchData.setAnnualVolume(newPartRequest.getAnnualVolume());
        benchData.setBatchSize(newPartRequest.getBatchSize());

        return benchData;
    }

    /**
     * Batch parts costing processed count
     *
     * @param parts
     * @return - count of batch parts costing processed count (state should in COMPLETED, ERRORED, AND REJECTED)
     */
    private static long getFinishedPartsCount(List<Part> parts) {
        List<BCSState> states = Arrays.asList(BCSState.COMPLETED, BCSState.ERRORED, BCSState.REJECTED);
        return parts.stream()
            .filter(part ->
                states.contains(BCSState.valueOf(part.getState()))
            )
            .count();
    }

    /**
     * Formats parts reporting class object and returns formatted string for reporting purpose.
     *
     * @param parts
     * @return String (example : returns formatted string based calculation of data size %6s %4s %10s)
     */
    private static String getFormattedString(List<BCSPartBenchmarkingDTO> parts) {
        StringBuilder formattedString = new StringBuilder("");
        int identity = 1;
        int partName = 1;
        int processGroup = 1;
        int state = 1;
        int costingResult = 1;
        int processingTime = 1;
        int errors = 1;

        for (BCSPartBenchmarkingDTO partReport : parts) {
            identity = (partReport.getIdentity().length() > identity) ? partReport.getIdentity().length() : identity;
            partName = (partReport.getPartName().length() > partName) ? partReport.getPartName().length() : partName;
            processGroup = (partReport.getProcessGroup().length() > processGroup) ? partReport.getProcessGroup().length() : processGroup;
            state = (partReport.getState().length() > state) ? partReport.getState().length() : state;
            if (null != partReport.getCostingResults()) {
                costingResult = (partReport.getCostingResults().length() > costingResult) ? partReport.getCostingResults().length() : costingResult;
            }
            processingTime = (partReport.getCostingDuration().toString().length() > processingTime) ? partReport.getCostingDuration().toString().length() : processingTime;
            if (null != partReport.getErrorMessage()) {
                errors = (partReport.getErrorMessage().length() > errors) ? partReport.getErrorMessage().length() : errors;
            }

        }
        formattedString.append("  %" + identity + "s")
            .append("  %" + partName + "s")
            .append("  %" + processGroup + "s")
            .append("  %" + state + "s")
            .append("  %" + costingResult + "s")
            .append("  %" + processingTime + "s")
            .append("  %" + errors + "s");

        return formattedString.toString();
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
        FormParams formParams = new FormParams();
        if (newPartRequest.getScenarioName().equals("Unique")) {
            newPartRequest.setScenarioName("Scenario" + System.currentTimeMillis());
        }
        formParams = (newPartRequest.getFilename() != null) ? formParams.use("filename", newPartRequest.getFilename()) : formParams;
        formParams = (newPartRequest.getExternalId() != null) ? formParams.use("externalId", String.format(newPartRequest.getExternalId(), System.currentTimeMillis())) : formParams;
        formParams = (newPartRequest.getAnnualVolume() != null) ? formParams.use("AnnualVolume", newPartRequest.getAnnualVolume().toString()) : formParams;
        formParams = (newPartRequest.getBatchSize() != null) ? formParams.use("BatchSize", newPartRequest.getBatchSize().toString()) : formParams;
        formParams = (newPartRequest.getDescription() != null) ? formParams.use("Description", newPartRequest.getDescription()) : formParams;
        formParams = (newPartRequest.getProcessGroup() != null) ? formParams.use("ProcessGroup", newPartRequest.getProcessGroup()) : formParams;
        formParams = (newPartRequest.getProductionLife() != null) ? formParams.use("ProductionLife", newPartRequest.getProductionLife().toString()) : formParams;
        formParams = (newPartRequest.getScenarioName() != null) ? formParams.use("ScenarioName", newPartRequest.getScenarioName()) : formParams;
        formParams = (newPartRequest.getVpeName() != null) ? formParams.use("VpeName", newPartRequest.getVpeName()) : formParams;
        formParams = (newPartRequest.getMaterialName() != null) ? formParams.use("MaterialName", newPartRequest.getMaterialName()) : formParams;
        formParams = (newPartRequest.getGenerateWatchPointReport() != null) ? formParams.use("generateWatchpointReport", newPartRequest.getGenerateWatchPointReport()) : formParams;
        formParams = (newPartRequest.getUdas() != null) ? formParams.use("udas", newPartRequest.getUdas()) : formParams;

        header.put("Accept", "*/*");
        header.put("Content-Type", "multipart/form-data");
        requestEntity.headers(header)
            .multiPartFiles(new MultiPartFiles()
                .use("data", partFile))
            .formParams(formParams);
        return requestEntity;
    }
}
