package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.bcs.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MultiPartCostingScenarioTest extends TestUtil implements Runnable {
    enum CostingElementStatus {
        ERROR, COMPLETE, INCOMPLETE
    }

    private static final Logger logger = LoggerFactory.getLogger(MultiPartCostingScenarioTest.class);

    private static String batchIdentity;
    private static String partIdentity;
    private static List<String> partList = new ArrayList<>();
    private static List<String> partIdentities = new ArrayList();
    private static Part batchPart;
    private static List<String> failedParts = new ArrayList<>();

    @Test
    @TestRail(testCaseId = {"7696"})
    @Description("Test costing scenarion, includes creating a new batch, with multiple parts and waiting for the " +
            "costing process to complete for all parts. Then retrieve costing results.")
    public void costMultipleParts() throws InterruptedException {
        // create batch
        Batch batch = BatchResources.createNewBatch();
        MultiPartCostingScenarioTest.batchIdentity = BcsUtils.getIdentity(batch, Batch.class);

        //Generate Part List & download part file from S3
        this.getPartsList();

        //upload parts
        this.initThreadExecution();

        // start costing
        try {
            BatchResources.startCosting(batchIdentity);
        } catch (Exception ignored) {
            logger.info("Empty response", ignored);

        }

        // poll for part state/batch state
        CostingElementStatus partElementStatus = CostingElementStatus.INCOMPLETE;
        for (String identity : partIdentities) {
            partElementStatus = waitForPartProcessingComplete(identity);
            if (partElementStatus == CostingElementStatus.ERROR) {
                failedParts.add(identity);
            }
        }
        Assert.assertEquals(partElementStatus, CostingElementStatus.COMPLETE);

        CostingElementStatus batchElementStatus = waitingForBatchProcessingComplete();
        Assert.assertEquals(batchElementStatus, CostingElementStatus.COMPLETE);

        BatchResources.startCosting(partIdentity);
    }

    /**
     * Thread run method
     */
    public void run() {
        Random random = new Random();
        int partIndex;
        int count = 1;

        while (count <= Constants.getMaximumPartsUpload()) {
            partIndex = random.nextInt(partList.size());
            String part = MultiPartCostingScenarioTest.partList.get(partIndex);

            // create batch part
            NewPartRequest newPartRequest =
                    (NewPartRequest) JsonManager.deserializeJsonFromInputStream(
                            FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"),
                            NewPartRequest.class);

            // S3 part string: common/Process_Group/File_Name
            String[] partSummary = part.split("/");
            newPartRequest.setFilename(partSummary[2]);
            newPartRequest.setProcessGroup(partSummary[1]);
            batchPart = (Part) BatchPartResources.createNewBatchPart(newPartRequest, batchIdentity);
            try {
                partIdentity = BcsUtils.getIdentity(batchPart, Part.class);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                return;
            }

            partIdentities.add(partIdentity);
            count++;

        }
    }

    /**
     * Wait for the part costing to be COMPLETE or ERRORED
     * @return status
     * @throws InterruptedException
     */
    private CostingElementStatus waitForPartProcessingComplete(String identity) throws InterruptedException {
        Object partDetails;
        Integer pollingInterval = 0;
        Integer maximumPollingIntervals = Constants.getPollingTimeout() * partIdentities.size();
        while (pollingInterval <= maximumPollingIntervals) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, identity).getResponseEntity();
            try {
                CostingElementStatus pollingResult = pollState(partDetails, Part.class);
                if (pollingResult != CostingElementStatus.INCOMPLETE) {
                    return pollingResult;
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                throw e;
            }

            pollingInterval += 1;
        }

        return CostingElementStatus.INCOMPLETE;

    }

    /**
     * Wait for the batch costing to be COMPLETE or ERRORED
     * @return status
     * @throws InterruptedException
     */
    private  CostingElementStatus waitingForBatchProcessingComplete() throws InterruptedException {
        Object batchDetails;
        Integer pollingInterval = 0;

        while (pollingInterval <= Constants.getPollingTimeout()) {
            batchDetails = BatchResources.getBatchRepresentation(batchIdentity).getResponseEntity();
            try {
                CostingElementStatus pollingResult = pollState(batchDetails, Batch.class);
                if (pollingResult != CostingElementStatus.INCOMPLETE) {
                    return pollingResult;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                throw e;

            }

            pollingInterval += 1;
        }

        return CostingElementStatus.INCOMPLETE;
    }

    /**
     * Polls BCS to get a batch/part's costing status
     *
     * @param obj
     * @param klass
     * @return Costing Status
     */
    private CostingElementStatus pollState(Object obj, Class klass) throws InterruptedException {
        String state = BcsUtils.getState(obj, klass);
        if (state.toUpperCase().equals("COMPLETED")) {
            return CostingElementStatus.COMPLETE;
        } else if (state.toUpperCase().equals("ERRORED")) {
            return CostingElementStatus.ERROR;
        } else {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                throw e;
            }
        }

        return CostingElementStatus.INCOMPLETE;
    }

    /**
     * Execute thread pool
     *
     * @throws InterruptedException
     */
    private void initThreadExecution() throws InterruptedException {
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(Constants.getCostingThreads());
        CountDownLatch countDownLatch = new CountDownLatch(Constants.getCostingThreads());

        try {
            for (int i = 0; i < Constants.getCostingThreads(); i++) {
                threadPoolExecutor.execute(new MultiPartCostingScenarioTest());
            }

            countDownLatch.await();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            throw e;

        } finally {
            threadPoolExecutor.shutdown();
        }
    }

    /**
     * Get a list of parts from S3
     *
     */
    private void getPartsList() {
        List<String> parts = FileResourceUtil.getCloudFileList();

        // Filter out path listings with no part file
        partList = parts.stream()
                .filter(part -> part.split("/").length > 2)
                .collect(Collectors.toList());

    }
}
