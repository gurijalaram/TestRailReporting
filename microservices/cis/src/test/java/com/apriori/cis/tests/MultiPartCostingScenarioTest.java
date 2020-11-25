package com.apriori.cis.tests;

import static org.junit.Assert.fail;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cis.controller.BatchPartResources;
import com.apriori.cis.controller.BatchResources;
import com.apriori.cis.controller.PartResources;
import com.apriori.cis.entity.request.NewPartRequest;
import com.apriori.cis.entity.response.Batch;
import com.apriori.cis.entity.response.Part;
import com.apriori.cis.utils.CisProperties;
import com.apriori.cis.utils.CisUtils;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class MultiPartCostingScenarioTest extends TestUtil implements Runnable {
    enum CostingElementStatus {
        ERROR, COMPLETE, INCOMPLETE
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiPartCostingScenarioTest.class);
    
    private static Boolean exitTest = false;
    private static String batchIdentity;
    private static String partIdentity;
    private static Stack<String> partList = new Stack<>();
    private static List<String> partIdentities = new ArrayList();
    private static Part batchPart;

    @Test
    @TestRail(testCaseId = {"4278", "4284", "4280", "4177"})
    @Description("Test costing scenarion, includes creating a new batch, a new part and waiting for the costing " +
            "process to complete. Then retrieve costing results.")
    public void costMultipleParts() throws InterruptedException {
        // create batch
        Batch batch = BatchResources.createNewBatch();
        MultiPartCostingScenarioTest.batchIdentity = CisUtils.getIdentity(batch, Batch.class);

        //Generate Part List
        this.getPartsFromFileSystem();

        //upload parts
        Integer threads = CisProperties.getCostingThreads();
        this.initThreadExecution(threads, partList.size());

        // start costing
        try {
            BatchResources.startCosting(batchIdentity);
        } catch (Exception ignored) {
            LOGGER.info("Empty response", ignored);

        }

        // poll for part state/batch state
        Integer maximumPollingIntervals = CisProperties.getPollingTimeout() * partIdentities.size();
        CostingElementStatus partElementStatus = CostingElementStatus.INCOMPLETE;
        for (String identity : partIdentities) {

            partElementStatus = waitForPartProcessingComplete(identity, maximumPollingIntervals);
            switch (partElementStatus) {
                case COMPLETE: break;
                case ERROR: return;
                default:  break;
            }
        }
        Assert.assertEquals(partElementStatus, CostingElementStatus.COMPLETE);

        CostingElementStatus batchElementStatus = CostingElementStatus.INCOMPLETE;
        batchElementStatus = waitingForBatchProcessingComplete(CisProperties.getPollingTimeout());
        Assert.assertEquals(batchElementStatus, CostingElementStatus.COMPLETE);

        PartResources.getPartCosting(partIdentity);

        initPropertyStore();



    }

    public void run() {
        while (!MultiPartCostingScenarioTest.partList.isEmpty()) {
            String part = MultiPartCostingScenarioTest.partList.peek();
            MultiPartCostingScenarioTest.partList.pop();


            // create batch part
            NewPartRequest newPartRequest =
                    (NewPartRequest)JsonManager.deserializeJsonFromStream(
                            FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);

            newPartRequest.setFilename(part);
            batchPart = (Part)BatchPartResources.createNewBatchPart(newPartRequest, batchIdentity);

            try {
                partIdentity = CisUtils.getIdentity(batchPart, Part.class);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                LOGGER.error(Arrays.toString(e.getStackTrace()));
            }

            partIdentities.add(partIdentity);

        }
    }

    private void initPropertyStore() {
        PartResources.getPartCosting(partIdentity);
        PropertyStore propertyStore = new PropertyStore();
        propertyStore.setBatchIdentity(batchIdentity);
        propertyStore.setPartIdentity(partIdentity);
        JsonManager.serializeJsonToFile(
                Thread.currentThread().getContextClassLoader().getResource("property-store.json").getPath(), propertyStore);
    }

    private CostingElementStatus waitForPartProcessingComplete(String identity, Integer maximumPollingIntervals) throws InterruptedException {
        Object partDetails;
        Integer pollingInterval = 0;
        while (pollingInterval <= maximumPollingIntervals) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, identity).getResponseEntity();
            try {
                if (pollState(partDetails, Part.class)) {
                    return CostingElementStatus.COMPLETE;
                }
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
                LOGGER.error(Arrays.toString(e.getStackTrace()));
                throw e;
            }

            if (exitTest) {
                String errors = CisUtils.getErrors(batchPart, Part.class);
                LOGGER.error(errors);
                fail("Part was in state 'ERRORED'");
                return CostingElementStatus.ERROR;
            }

            pollingInterval += 1;
        }

        return CostingElementStatus.INCOMPLETE;

    }

    private  CostingElementStatus waitingForBatchProcessingComplete(Integer maximumPollingIntervals) throws InterruptedException {
        Object batchDetails;
        Integer pollingInterval = 0;
        while (pollingInterval <= maximumPollingIntervals) {
            batchDetails = BatchResources.getBatchRepresentation(batchIdentity).getResponseEntity();
            try {
                if (pollState(batchDetails, Batch.class)) {
                    return CostingElementStatus.COMPLETE;
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                LOGGER.error(Arrays.toString(e.getStackTrace()));
                throw e;

            }

            if (exitTest) {
                fail("Batch was in state 'ERRORED'");
                return CostingElementStatus.ERROR;
            }

            pollingInterval += 1;
        }

        return CostingElementStatus.INCOMPLETE;
    }

    private Boolean pollState(Object obj, Class klass) throws InterruptedException {
        String state = "";
        state = CisUtils.getState(obj, klass);

        if (state.toUpperCase().equals("ERRORED")) {
            exitTest = true;
            return null;
        }


        if (state.toUpperCase().equals("COMPLETED")) {
            return true;
        } else {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                LOGGER.error(Arrays.toString(e.getStackTrace()));
                throw e;
            }
        }

        return false;

    }

    private void getPartsFromFileSystem() {
        InputStream dir = FileResourceUtil.getResourceFileStream("parts/parts-list.txt");
        String text = "";
        try {
            text = IOUtils.toString(dir);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> tmpPartList = new ArrayList<>();
        Collections.addAll(tmpPartList, Objects.requireNonNull(text.split("/n")));
        partList.addAll(tmpPartList.stream().map(p -> "parts/" + p).collect(Collectors.toList()));
    }

    private void initThreadExecution(int threadCount, int numberOfParts) throws InterruptedException {
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfParts);

        try {
            for (int i = 0; i <= numberOfParts; i++) {
                threadPoolExecutor.execute(new MultiPartCostingScenarioTest());
            }

            countDownLatch.await();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            throw e;

        } finally {
            threadPoolExecutor.shutdown();
        }
    }


}
