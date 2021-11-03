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
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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

@Slf4j
public class MultiPartCostingScenarioTest extends TestUtil {
    enum CostingElementStatus {
        ERROR, COMPLETE, INCOMPLETE
    }

    private static Batch batch;
    private static String batchIdentity;
    private static List<String> partList = new ArrayList<>();
    private static Part batchPart;
    private static Map<String, String> parts = new HashMap<>();
    private static CountDownLatch countDownLatch;
    private static Materials materials = new Materials();
    private static int numberOfParts;
    private static Map<String, Map<String, Object>> partsSummaries = new HashMap<>();
    private static StringBuilder content;
    private static StringBuilder runSummary;


    @BeforeClass
    public static void testSetup() {
        getPartsList();
        numberOfParts = Integer.parseInt(PropertiesContext.get("${env}.bcs.number_of_parts"));
        batch = BatchResources.createNewBatch();
        content = new StringBuilder();
        runSummary = new StringBuilder();
    }

    public class LoadParts implements Runnable {
        private Map<String, Object> partInformation = new HashMap<>();

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
            newPartRequest.setExternalId("Auto-part-" + UUID.randomUUID());
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
                ).getResponseEntity();

                partInformation.put("process_group", processGroup);
                partInformation.put("start_time", batchPart.getCreatedAt());
                partInformation.put("name", batchPart.getPartName());
                partInformation.put("state", batchPart.getState());
                partInformation.put("identity", batchPart.getIdentity());
                partInformation.put("end_time", batchPart.getUpdatedAt());
                partInformation.put("updated", false);
                partInformation.put("errors", "");

                synchronized (this) {
                    partsSummaries.put(batchPart.getIdentity(), partInformation);
                    parts.put(batchPart.getIdentity(), batchPart.getState());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
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
    @TestRail(testCaseId = {"9111"})
    @Description("Test costing scenarion, includes creating a new batch, with multiple parts and waiting for the " +
            "costing process to complete for all parts. Then retrieve costing results.")
    public void costMultipleParts() throws InterruptedException {
        batchIdentity = BcsUtils.getIdentity(batch, Batch.class);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Constants.MULTIPART_THREAD_COUNT);
        countDownLatch = new CountDownLatch(numberOfParts);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date runDate = DateTime.now().toDate();
        String startDate = format.format(runDate);
        
        // Load all parts, set in the constant MAX_NUMBER_OF_PARTS. Parts
        // will begin costing once added to the batch
        try {
            for (int i = 1; i <= numberOfParts; i++) {
                LoadParts loadParts = new LoadParts();
                threadPoolExecutor.execute(loadParts);

            }

            countDownLatch.await();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            threadPoolExecutor.shutdown();
        }

        // Start Costing the batch, which means no more parts can be added
        try {
            BatchResources.startCosting(batchIdentity);
        } catch (Exception ignored) {
            log.error("Empty response", ignored);
        }

        // Wait until the batch processing is complete. Parts belonging
        // to this batch may still being processed
        //TODO: What do we with the final polled batch status. Where to report?
        CostingElementStatus batchElementStatus = waitingForBatchProcessingComplete();

        // Poll all parts statuses until all parts are in a COMPLETED or ERRORED
        // state or the maximum number of intervals are met
        int interval = 0;
        int completed = 0;
        int errored = 0;
        int rejected = 0;
        int terminal;
        while (interval <= Constants.MULTIPART_POLLING_INTERVALS) {
            updatePartStatus();
            completed = Collections.frequency(parts.values(), "COMPLETED");
            errored = Collections.frequency(parts.values(), "ERRORED");
            rejected = Collections.frequency(parts.values(), "REJECTED");

            terminal = completed + errored + rejected;
            if (terminal >= parts.size()) {
                break;
            }

            interval++;
            Thread.sleep(Constants.MULTIPART_POLLING_WAIT);
        }

        batch = (Batch)BatchResources.getBatchRepresentation(batchIdentity).getResponseEntity();

        long hours = ChronoUnit.HOURS.between(batch.getCreatedAt(), batch.getUpdatedAt()) % 24;
        long minutes = ChronoUnit.MINUTES.between(batch.getCreatedAt(), batch.getUpdatedAt()) % 60;
        long seconds = ChronoUnit.SECONDS.between(batch.getCreatedAt(), batch.getUpdatedAt()) % 60;

        runSummary.append("Run Date: " + startDate).append(System.lineSeparator());
        runSummary.append("Batch Identity: " + batch.getIdentity()).append(System.lineSeparator());
        runSummary.append("Batch State: " + batch.getState()).append(System.lineSeparator());
        runSummary.append(String.join("", Collections.nCopies(25, "="))).append(System.lineSeparator());
        runSummary.append("Completed: " + completed).append(System.lineSeparator());
        runSummary.append("Errored: " + errored).append(System.lineSeparator());
        runSummary.append("Rejected: " + rejected).append(System.lineSeparator());
        runSummary.append("No. of Parts: " + parts.size()).append(System.lineSeparator());
        runSummary.append("Missing Parts: " + (numberOfParts - parts.size())).append(System.lineSeparator());
        runSummary.append("Total Run Time: " + hours + " hours " + minutes + " minutes " + seconds + " seconds");

        buildPartSummaryReport();
        createReports(runDate);
    }

    /**
     * Generate test reports
     *
     * @param runDate - Date of test
     */
    private void createReports(Date runDate) {
        SimpleDateFormat formatName = new SimpleDateFormat("MMddyyyyHHmmss");
        FileWriter partSummaryReport = null;
        FileWriter runSummaryReport = null;

        try {
            String dateString = formatName.format(runDate);

            log.info(content.toString());
            new File("target").mkdir();
            partSummaryReport = new FileWriter("target/PartSummaryReport_" + dateString + ".csv");
            partSummaryReport.write(content.toString());

            log.info(runSummary.toString());
            runSummaryReport = new FileWriter("target/RunSummaryReport_" + dateString + ".txt");
            runSummaryReport.write(runSummary.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                runSummaryReport.close();
                partSummaryReport.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }

        }

    }


    /**
     * Build the part summary report
     */
    private void buildPartSummaryReport() {
        content.append("Part Name, Part Identity, Process Group, Final State, Processing Time, Errors").append(System.lineSeparator());

        try {
            partsSummaries.entrySet().stream()
                    .forEach(es -> buildPartSummaryContent(es.getValue()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     *
     Generate comma seperated part information
      */
    private void buildPartSummaryContent(Map<String, Object> information) {
        content.append(information.get("name")).append(",");
        content.append(information.get("identity")).append(",");
        content.append(information.get("process_group")).append(",");
        content.append(information.get("state")).append(",");

        long hours = ChronoUnit.HOURS.between((LocalDateTime)information.get("start_time"),
                (LocalDateTime)information.get("end_time")) % 24;
        long minutes = ChronoUnit.MINUTES.between((LocalDateTime)information.get("start_time"),
                (LocalDateTime)information.get("end_time")) % 60;
        long seconds = ChronoUnit.SECONDS.between((LocalDateTime)information.get("start_time"),
                (LocalDateTime)information.get("end_time")) % 60;
        content.append(hours).append(" hours ");
        content.append(minutes).append(" minutes ");
        content.append(seconds).append(" seconds").append(",");
        content.append(information.get("errors"));
        content.append(System.lineSeparator());
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

            Map<String, Object> summary = partsSummaries.get(currentPart.getIdentity());
            if (!BcsUtils.TerminalState.contains(currentPart.getState())) {
                summary.put("end_time", currentPart.getUpdatedAt());
            } else {
                if (!(Boolean)summary.get("updated")) {
                    summary.put("end_time", currentPart.getUpdatedAt());
                    summary.put("updated", true);
                }

                if (BcsUtils.TerminalState.valueOf(currentPart.getState()) == BcsUtils.TerminalState.ERRORED ||
                        BcsUtils.TerminalState.valueOf(currentPart.getState()) == BcsUtils.TerminalState.REJECTED) {
                    if (currentPart.getErrors() != null) {
                        summary.put("errors", currentPart.getErrors());
                    }
                }
            }
            summary.put("state", currentPart.getState());
            partsSummaries.put(currentPart.getIdentity(), summary);

        }
    }

    /**
     * Wait for the batch costing to be COMPLETE or ERRORED
     * @return status
     * @throws InterruptedException
     */
    private  CostingElementStatus waitingForBatchProcessingComplete() throws InterruptedException {
        Object batchDetails;
        int pollingInterval = 0;

        while (pollingInterval <= Constants.BATCH_POLLING_TIMEOUT) {
            batchDetails = BatchResources.getBatchRepresentation(batchIdentity).getResponseEntity();
            try {
                CostingElementStatus pollingResult = pollState(batchDetails, Batch.class);
                if (pollingResult != CostingElementStatus.INCOMPLETE) {
                    return pollingResult;
                }

                Thread.sleep(Constants.MULTIPART_POLLING_WAIT);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error(Arrays.toString(e.getStackTrace()));
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
        if (state.equalsIgnoreCase("COMPLETED")) {
            return CostingElementStatus.COMPLETE;
        } else if (state.equalsIgnoreCase("ERRORED")) {
            return CostingElementStatus.ERROR;
        } else {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error(Arrays.toString(e.getStackTrace()));
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
                .filter(part -> !part.contains("2-Model Machining"))
                .collect(Collectors.toList());
    }
}
