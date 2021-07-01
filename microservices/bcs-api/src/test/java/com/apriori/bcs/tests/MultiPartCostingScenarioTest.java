package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.bcs.utils.Constants;
import com.apriori.bcs.utils.Materials;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.apache.commons.lang.time.StopWatch;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class MultiPartCostingScenarioTest extends TestUtil {
    enum CostingElementStatus {
        ERROR, COMPLETE, INCOMPLETE
    }

    private static final Logger logger = LoggerFactory.getLogger(MultiPartCostingScenarioTest.class);

    private static Batch batch;
    private static NewPartRequest newPartRequest;
    private static String batchIdentity;
    private static String partIdentity;
    private static List<String> partList = new ArrayList<>();
    private static List<String> partIdentities = new ArrayList();
    private static Part batchPart;
    private static List<String> failedParts = new ArrayList<>();
    private static Map<String, String> parts = new HashMap<>();
    private static CountDownLatch countDownLatch;
    private static Materials materials = new Materials();

    @BeforeClass
    public static void testSetup() {
        getPartsList();
        batch = BatchResources.createNewBatch();
    }

    public class LoadParts implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            int partIndex;


            partIndex = random.nextInt(partList.size());
            String part = MultiPartCostingScenarioTest.partList.get(partIndex);

            NewPartRequest newPartRequest =
                    (NewPartRequest) JsonManager.deserializeJsonFromInputStream(
                            FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"),
                            NewPartRequest.class);


            // S3 part string: common/Process_Group/File_Name
            String[] partSummary = part.split("/");
            newPartRequest.setFilename(partSummary[2]);
            newPartRequest.setExternalId("Auto-part-" + UUID.randomUUID().toString());
            String material = materials.getProcessGroupMaterial(partSummary[2]);
            String processGroup = partSummary[1];
            if (material == null) {
                material = "";
            }

            newPartRequest.setProcessGroup(processGroup);
            newPartRequest.setMaterialName(material);


            try {
                batchPart = (Part) BatchPartResources.createNewBatchPart(newPartRequest,
                        batch.getIdentity()
                );

                synchronized (this) {
                    parts.put(batchPart.getIdentity(), batchPart.getState());
                }
            } catch (Exception e) {
                logger.debug(e.getMessage());
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    @AfterClass
    public static void testCleanup() {
        BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(testCaseId = {"7696"})
    @Description("Test costing scenarion, includes creating a new batch, with multiple parts and waiting for the " +
            "costing process to complete for all parts. Then retrieve costing results.")
    public void costMultipleParts() throws InterruptedException {
        batchIdentity = BcsUtils.getIdentity(batch, Batch.class);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Constants.MULTIPART_THREAD_COUNT);
        countDownLatch = new CountDownLatch(Constants.MAX_NUMBER_OF_PARTS);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // Load all parts, set in the constant MAX_NUMBER_OF_PARTS. Parts
        // will begin costing once added to the batch
        try {
            for (int i = 1; i <= Constants.MAX_NUMBER_OF_PARTS; i++) {
                LoadParts loadParts = new LoadParts();
                threadPoolExecutor.execute(loadParts);

            }

            countDownLatch.await();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        } finally {
            threadPoolExecutor.shutdown();
        }

        System.out.println("Parts added: " + parts.size());
        // Start Costing the batch, which means no more parts can be added
        try {
            BatchResources.startCosting(batchIdentity);
        } catch (Exception ignored) {
            logger.info("Empty response", ignored);

        }


        // Wait until the batch processing is complete. Parts belonging
        // to this batch may still being processed
        //TODO: What do we with the final polled batch status. Where to report?
        CostingElementStatus batchElementStatus = waitingForBatchProcessingComplete();

        // Poll all parts statuses until all parts are in a COMPLETED or ERRORED
        // state or the maximum number of intervals are met
        int interval = 0;
        int completed;
        int errored;
        int rejected;
        int terminal;
        while (interval <= Constants.MULTIPART_POLLING_INTERVALS) {
            updatePartStatus();
            completed = Collections.frequency(parts.values(), "COMPLETED");
            errored = Collections.frequency(parts.values(), "ERRORED");
            rejected = Collections.frequency(parts.values(), "REJECTED");

            System.out.println("Completed: " + completed);
            System.out.println("Errored: " + errored);
            System.out.println("Rejected: " + rejected);
            System.out.println("No. of Parts: " + parts.size());
            System.out.println("Missing Parts: " + (Constants.MAX_NUMBER_OF_PARTS - parts.size()));

            terminal = completed + errored + rejected;
            if (terminal >= parts.size()) {
                break;
            }

            interval++;
            Thread.sleep(Constants.MULTIPART_POLLING_WAIT);
        }

        // TODO: where to report final polled part states and timings?
        stopWatch.stop();
        long runTime = stopWatch.getTime();
        System.out.println("Total Run Time: " + runTime);





    }

    /**
     * Updates the part state for every part in Hashmap
     */
    private void updatePartStatus() {
        // using iterators
        Iterator<Map.Entry<String, String>> iterator = parts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            ResponseWrapper<Part> responseWrapper = BatchPartResources.getBatchPartRepresentation(
                    batchIdentity,
                    entry.getKey());
            Part currentPart = responseWrapper.getResponseEntity();
            parts.put(currentPart.getIdentity(), currentPart.getState());
            System.out.println(currentPart.getIdentity() + " :: " + currentPart.getState());
        }
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

                Thread.sleep(Constants.MULTIPART_POLLING_WAIT);
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
    private synchronized CostingElementStatus pollState(Object obj, Class klass) throws InterruptedException {
        String state = BcsUtils.getState(obj, klass);
        if (state.toUpperCase().equals("COMPLETED")) {
            return CostingElementStatus.COMPLETE;
        } else if (state.toUpperCase().equals("ERRORED")) {
            return CostingElementStatus.ERROR;
        } else {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                throw e;
            }
        }

        return CostingElementStatus.INCOMPLETE;
    }


    /**
     * Get a list of parts from S3
     *
     */
    private static void getPartsList() {
        List<String> parts = FileResourceUtil.getCloudFileList();

        // Filter out path listings with no part file
        partList = parts.stream()
                .filter(part -> part.split("/").length > 2)
                .filter(part -> !part.toLowerCase().contains("without pg"))
                .filter(part -> !part.toLowerCase().contains("assembly"))
                .collect(Collectors.toList());
    }
}
