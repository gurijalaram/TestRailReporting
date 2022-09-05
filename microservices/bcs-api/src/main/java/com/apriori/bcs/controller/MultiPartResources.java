package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.database.dto.BCSPartBenchmarkingDTO;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.part.PartUtil;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class MultiPartResources {

    private static Map<String, PartData> partsCollector;
    private static final long WAIT_TIME = 1800;
    private static String line = "--------------------------------------------------------------------------------------------------------------------------------------\n";


    /**
     * Add number of parts (configured in config file) to batch
     *
     * @param partDataList  - bunch of parts
     * @param batchIdentity - Batch Identity
     */
    public static void addPartsToBatch(List<PartData> partDataList, String batchIdentity) {
        partsCollector = new HashMap<>();
        for (PartData partData : partDataList) {
            Part batchPart = BatchPartResources.createNewBatchPartByID(partData, batchIdentity).getResponseEntity();
            batchPart.convertToBCSPartBenchData(partData);
            partData.setBatchIdentity(batchIdentity);
            partsCollector.put(batchPart.getIdentity(), partData);
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
        ResponseWrapper<Parts> partsResponse;
        Parts parts;
        do {
            partsResponse = BatchPartResources.getPartsByBatchId(batchIdentity);
            parts = partsResponse.getResponseEntity();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            getPartsCountAndLogInfo(parts.getItems());
        } while (getFinishedPartsCount(parts.getItems()) != parts.getItems().size() && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return (getFinishedPartsCount(parts.getItems()) == parts.getItems().size()) ? true : false;
    }

    /**
     * Overides NewPartRequest class object with deserialized json test data with aws cloud data files
     *
     * @param newPartRequest
     * @return newPartRequest - deserialized class object
     */
    private static NewPartRequest getNewPartRequestAndOverridePartData(NewPartRequest newPartRequest) {
        PartData partData = PartUtil.getPartData();

        newPartRequest.setFilename(partData.getFilename());
        newPartRequest.setProcessGroup(partData.getProcessGroup());
        newPartRequest.setMaterial(partData.getMaterial());
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
        benchData.setMaterialName(newPartRequest.getMaterial());
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
        return parts.stream().filter(part -> states.contains(BCSState.valueOf(part.getState()))).count();
    }

    /**
     * Batch parts costing processed count
     *
     * @param parts         - list of part
     * @param expectedState - BCSState enum value
     * @return - count of batch parts costing processed count
     */
    private static long getPartsCount(List<Part> parts, BCSState expectedState) {
        List<BCSState> states = Arrays.asList(expectedState);
        return parts.stream().filter(part -> states.contains(BCSState.valueOf(part.getState()))).count();
    }

    /**
     * Batch parts log information during parts costing process
     *
     * @param parts
     */
    private static void getPartsCountAndLogInfo(List<Part> parts) {
        String[] columnNames = {"PART_STATE", "COUNT"};
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        log.info("=============================================== BATCH PARTS STATE COUNT INFORMATION  =================================================");
        log.info("####################### TOTAL PARTS ADDED TO BATCH ==> " + parts.size() + "###########################################################");

        map.put(BCSState.LOADING.toString(), (int) getPartsCount(parts, BCSState.LOADING));
        map.put(BCSState.COSTING.toString(), (int) getPartsCount(parts, BCSState.COSTING));
        map.put(BCSState.COSTED.toString(), (int) getPartsCount(parts, BCSState.COSTED));
        map.put(BCSState.READY_TO_COST.toString(), (int) getPartsCount(parts, BCSState.READY_TO_COST));
        map.put(BCSState.PUBLISHING.toString(), (int) getPartsCount(parts, BCSState.PUBLISHING));
        map.put(BCSState.PUBLISHED.toString(), (int) getPartsCount(parts, BCSState.PUBLISHED));
        map.put(BCSState.COMPLETED.toString(), (int) getPartsCount(parts, BCSState.COMPLETED));
        map.put(BCSState.REPORTING.toString(), (int) getPartsCount(parts, BCSState.REPORTING));
        map.put(BCSState.ERRORED.toString(), (int) getPartsCount(parts, BCSState.ERRORED));
        map.put(BCSState.REJECTED.toString(), (int) getPartsCount(parts, BCSState.REJECTED));
        map.entrySet().forEach(entry -> {
            log.info(String.format("%13s  => %2d", entry.getKey(), entry.getValue()));
        });
        log.info("======================================================================================================================================");
    }

    /**
     * Formats batch reporting class object and returns formatted string for reporting purpose.
     * Summarize the batch costing state and reports the performance metrics for a batch.
     *
     * @param batch
     * @return String (example : returns formatted string based calculation of data size %6s %4s %10s)
     */
    private static String getBatchFormatString(Batch batch) {
        StringBuilder formattedString = new StringBuilder("");
        int identity = 1;
        int state = 1;
        int processingTime = 1;
        int errors = 1;

        if (null != batch.getIdentity()) {
            identity = (batch.getIdentity().length() > identity) ? batch.getIdentity().length() : identity;
        }

        if ((null != batch.getState())) {
            state = (batch.getState().length() > state) ? batch.getState().length() : state;
        }

        if (batch.getCostingDuration().toString().length() > processingTime) {
            processingTime = batch.getCostingDuration().toString().length();
        }

        if (null != batch.getErrors()) {
            errors = (batch.getErrors().length() > errors) ? batch.getErrors().length() : errors;
        }

        formattedString.append("  %" + identity + "s")
            .append("  %" + state + "s")
            .append("  %" + processingTime + "s")
            .append("  %" + errors + "s");

        return formattedString.toString();
    }

    /**
     * Formats parts reporting class object and returns formatted string for reporting purpose.
     * Summarize the batch parts costing state and reports the performance metrics for each part in a batch.
     *
     * @param parts
     * @param parts List of parts
     * @return String (example : returns formatted string based calculation of data size %6s %4s %10s)
     */
    private static String getFormattedString(List<PartData> parts) {
        StringBuilder formattedString = new StringBuilder("");
        Integer identity = 1;
        Integer partName = 1;
        Integer processGroup = 1;
        Integer state = 1;
        Integer costingResult = 1;
        Integer processingTime = 1;
        Integer errors = 1;

        for (PartData partReport : parts) {
            identity = (null != partReport.getIdentity()) ? (partReport.getIdentity().length() > identity) ? partReport.getIdentity().length() : identity : identity;
            partName = (null != partReport.getPartName()) ? (partReport.getPartName().length() > partName) ? partReport.getPartName().length() : partName : partName;
            processGroup = (null != partReport.getProcessGroup()) ? (partReport.getProcessGroup().length() > processGroup) ? partReport.getProcessGroup().length() : processGroup : processGroup;
            state = (null != partReport.getState()) ? (partReport.getState().length() > state) ? partReport.getState().length() : state : state;
            costingResult = (null != partReport.getCostingResults()) ? (partReport.getCostingResults().length() > costingResult) ? partReport.getCostingResults().length() : costingResult : costingResult;
            processingTime = (partReport.getCostingDuration().toString().length() > processingTime) ? partReport.getCostingDuration().toString().length() : processingTime;
            errors = (null != partReport.getErrorMessage()) ? (partReport.getErrorMessage().length() > errors) ? partReport.getErrorMessage().length() : errors : errors;

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

    public static void summarizeBatchCostingInfo(ResponseWrapper<Batch> batch) {
        StringBuilder batchCostingLogInfo = new StringBuilder();
        batch.getResponseEntity().setCostingDuration(batch.getResponseEntity().getUpdatedAt());
        batchCostingLogInfo.append("================== Batch Benchmarking Data ==================");
        batchCostingLogInfo.append(line);
        batchCostingLogInfo.append(String.format(getBatchFormatString(batch.getResponseEntity()), "BATCH_ID", "BATCH_STATE", "PROCESSING_TIME", "ERRORS"));
        batchCostingLogInfo.append(line);
        batchCostingLogInfo.append(String.format(getBatchFormatString(batch.getResponseEntity()),
            batch.getResponseEntity().getIdentity(),
            batch.getResponseEntity().getState(),
            BcsUtils.convertSecsToMins(batch.getResponseEntity().getCostingDuration()),
            batch.getResponseEntity().getErrors()));
        batchCostingLogInfo.append(line);
        log.info(batchCostingLogInfo.toString());
    }

    /**
     * Summarize the batch parts costing state and reports the performance metrics for each part in a batch.
     *
     * @param parts
     */
    public static void summarizeAndLogPartsCostingInfo(Parts parts) {
        StringBuilder logInfoBuilder = new StringBuilder();
        parts.getItems().forEach(part -> {
            PartData benchData = partsCollector.get(part.getIdentity());
            benchData.setState(part.getState());
            benchData.setCostingResults(part.getCostingResult());
            benchData.setErrorMessage(part.getErrors());
            benchData.setCostingDuration(part.getUpdatedAt());
            partsCollector.put(part.getIdentity(), benchData);
        });

        List<PartData> partsReport = partsCollector.values()
            .stream()
            .collect(Collectors.toList());

        String formattedString = getFormattedString(partsReport);
        logInfoBuilder.append("================== Batch Parts Benchmarking Data ==================\n");
        logInfoBuilder.append(line);
        logInfoBuilder.append(String.format(formattedString, "PART_ID", "PART_NAME", "PROCESS_GROUP", "PART_STATE", "COSTING_RESULT", "PROCESSING_TIME", "ERRORS"));
        logInfoBuilder.append(line);

        for (PartData partDTO : partsReport) {
            logInfoBuilder.append(String.format(formattedString, partDTO.getIdentity(), partDTO.getPartName(),
                partDTO.getProcessGroup(), partDTO.getState(), partDTO.getCostingResults(),
                BcsUtils.convertSecsToMins(partDTO.getCostingDuration()), partDTO.getErrorMessage()));
        }
        logInfoBuilder.append(line);
        log.info(logInfoBuilder.toString());

        for (PartData partDTO : partsReport) {
            Assert.assertEquals("Verify Part State", BCSState.COMPLETED.toString(), partDTO.getState());
        }
    }
}