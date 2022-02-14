package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Parts;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.utils.Constants;
import com.apriori.database.dao.BCSDao;
import com.apriori.database.dto.BCSBatchDTO;
import com.apriori.database.dto.BCSPartBenchmarkingDTO;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.part.PartUtil;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MultiPartCostingScenarioTest extends TestUtil {

    private static Batch batch;
    private static String batchIdentity;
    private static BCSBatchDTO batchData;
    private static final Integer numberOfParts = Integer.parseInt(PropertiesContext.get("${env}.bcs.number_of_parts"));
    private static final Boolean doDbRecording = Boolean.parseBoolean(PropertiesContext.get("global.db_recording"));
    private static final List<BCSState> failedStates = Arrays.asList(BCSState.ERRORED, BCSState.REJECTED);
    private static final List<BCSState> completedStates = Arrays.asList(BCSState.COMPLETED);

    @BeforeClass
    public static void testSetup() {
        ResponseWrapper<Object> responseWrapper = BatchResources.createBatch();
        Batch batch = (Batch)BatchResources.createBatch().getResponseEntity();
        batchIdentity = batch.getIdentity();
        batchData = BCSBatchDTO.builder()
            .batchId(batchIdentity)
            .environment(PropertiesContext.get("env"))
            .externalId(batch.getExternalId())
            .customerIdentity(batch.getCustomerIdentity())
            .rollupName(batch.getRollupName())
            .rollupScenarioName(batch.getRollupScenarioName())
            .build();
    }

    @AfterClass
    public static void testCleanup() {
     //   BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @SneakyThrows
    @TestRail(testCaseId = {"9111"})
    @Description("Test costing scenarion, includes creating a new batch, with multiple parts and waiting for the " +
        "costing process to complete for all parts. Then retrieve costing results.")
    public void costParts() {
        Map<String, BCSPartBenchmarkingDTO> partsCollector = this.addPartsToBatchAndInitPartsMap();

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.BATCH_PARTS_BY_ID, Parts.class)
            .inlineVariables(batch.getIdentity());

        int countOfAttempts = 0;
        List<Part> parts;

        do {
            Thread.sleep(Constants.POLLING_WAIT);

            ResponseWrapper<Parts> partResponseWrapper = HTTPRequest.build(requestEntity).get();
            parts = partResponseWrapper.getResponseEntity().getItems();

            countOfAttempts++;

        } while (this.findCountOfFinishedParts(parts) != parts.size() || countOfAttempts == Constants.POLLING_INTERVALS);

        this.summarizePartsData(parts, partsCollector);

        if (doDbRecording) {
            BCSDao.insertCostingData(batchData, partsCollector.values());
        } else {
            this.logCostingInfo(batchData, partsCollector.values());
        }

        long countOfFailedParts = this.getCountOfFailedParts(parts);

        Assert.assertFalse(
            String.format("The number of finished parts with error status is critical.\nCount of parts: %s\nCount of failed part: %s\n",
                numberOfParts, countOfFailedParts
            ), this.isTestFailedBasedOnPartsStatus(countOfFailedParts));
    }

    private long getCountOfFailedParts(List<Part> parts) {
        return parts.stream().filter(part ->
            failedStates.contains(BCSState.valueOf(part.getState()))
        ).count();
    }

    private boolean isTestFailedBasedOnPartsStatus(long failedPartsCount) {
        final int partsCountForPercentageValidation = 10;

        if (numberOfParts < partsCountForPercentageValidation) {
            return failedPartsCount >= 3;
        } else {
            return failedPartsCount >= numberOfParts / 2;
        }

    }

    private long findCountOfFinishedParts(List<Part> parts) {
        return parts.stream()
            .filter(part ->
                completedStates.contains(BCSState.valueOf(part.getState()))
                    || failedStates.contains(BCSState.valueOf(part.getState()))
            )
            .count();
    }

    private void summarizePartsData(List<Part> parts, Map<String, BCSPartBenchmarkingDTO> partsCollector) {
        parts.forEach(part -> {
            BCSPartBenchmarkingDTO benchData = partsCollector.get(part.getIdentity());
            benchData.setState(part.getState());
            benchData.setCostingResults(part.getCostingResult());
            benchData.setErrorMessage(part.getErrors());

            if (part.getUpdatedAt() != null) {
                benchData.setCostingDuration(part.getUpdatedAt());
            }

            partsCollector.put(part.getIdentity(), benchData);
        });
    }

    private Map<String, BCSPartBenchmarkingDTO> addPartsToBatchAndInitPartsMap() {
        Map<String, BCSPartBenchmarkingDTO> partsCollector = new HashMap<>();

        for (int i = 0; i < numberOfParts; i++) {
            NewPartRequest newPartRequest = this.getNewPartRequestAndOverrideByPartData(
                BatchPartResources.newPartRequest()
            );

            Part batchPart = (Part) BatchPartResources.createNewBatchPartByID(batchIdentity).getResponseEntity();

            BCSPartBenchmarkingDTO benchData = this.convertPartToPartBenchDTOAndAddRequestData(batchPart, newPartRequest);

            partsCollector.put(batchPart.getIdentity(), benchData);
        }

        return partsCollector;
    }

    private NewPartRequest getNewPartRequestAndOverrideByPartData(NewPartRequest newPartRequest) {
        PartData partData = PartUtil.getPartData();

        newPartRequest.setFilename(partData.getFileName());
        newPartRequest.setProcessGroup(partData.getProcessGroup());
        newPartRequest.setMaterialName(partData.getMaterial());
        newPartRequest.setAnnualVolume(partData.getAnnualVolume());
        newPartRequest.setBatchSize(partData.getBatchSize());

        return newPartRequest;
    }

    private BCSPartBenchmarkingDTO convertPartToPartBenchDTOAndAddRequestData(Part batchPart, NewPartRequest newPartRequest) {
        BCSPartBenchmarkingDTO benchData = batchPart.convertToBCSPartBenchData();
        benchData.setFilename(newPartRequest.getFilename());
        benchData.setProcessGroup(newPartRequest.getProcessGroup());
        benchData.setMaterialName(newPartRequest.getMaterialName());
        benchData.setAnnualVolume(newPartRequest.getAnnualVolume());
        benchData.setBatchSize(newPartRequest.getBatchSize());

        return benchData;
    }

    private void logCostingInfo(BCSBatchDTO batchData, Collection<BCSPartBenchmarkingDTO> partsBenchmarkingData) {
        log.info("================== Batch Parts Benchmarking Data ==================");
        log.info("Batch: " + batchData.toString());

        StringBuilder partsData = new StringBuilder("Parts: \n");
        partsBenchmarkingData.forEach(data -> {
            partsData.append(data.toString())
                .append("\n");
        });

        log.info(partsData.toString());
    }
}
