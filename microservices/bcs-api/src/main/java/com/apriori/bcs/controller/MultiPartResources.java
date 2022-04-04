package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.parts.NewPartRequest;
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

    private static Map<String, BCSPartBenchmarkingDTO> partsCollector;
    private static final long WAIT_TIME = 1800;

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
        ResponseWrapper<Parts> partsResponse;
        Parts parts;
        do {
            partsResponse = BatchPartResources.getBatchPartById(batchIdentity);
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
     * Formats parts reporting class object and returns formatted string for reporting purpose.
     * Summarize the batch parts costing state and reports the performance metrics for each part in a batch.
     *
     * @param parts
     * @param parts List of parts
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
            identity = (null != partReport.getIdentity()) ? (partReport.getIdentity().length() > identity) ? partReport.getIdentity().length() : identity : null;
            partName = (null != partReport.getPartName()) ? (partReport.getPartName().length() > partName) ? partReport.getPartName().length() : partName : null;
            processGroup = (null != partReport.getProcessGroup()) ? (partReport.getProcessGroup().length() > processGroup) ? partReport.getProcessGroup().length() : processGroup : null;
            state = (null != partReport.getState()) ? (partReport.getState().length() > state) ? partReport.getState().length() : state : null;
            costingResult = (null != partReport.getCostingResults()) ? (partReport.getCostingResults().length() > costingResult) ? partReport.getCostingResults().length() : costingResult : null;
            processingTime = (partReport.getCostingDuration().toString().length() > processingTime) ? partReport.getCostingDuration().toString().length() : processingTime;
            errors = (null != partReport.getErrorMessage()) ? (partReport.getErrorMessage().length() > errors) ? partReport.getErrorMessage().length() : errors : null;
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
}