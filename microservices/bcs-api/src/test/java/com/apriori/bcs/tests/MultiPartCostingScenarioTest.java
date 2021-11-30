package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.enums.BCSState;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.bcs.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MultiPartCostingScenarioTest extends TestUtil {

    private static Batch batch;
    private static String batchIdentity;
    private final List<String> batchPartsInProcess = new ArrayList<>();
    private final List<Part> erroredBatchParts = new ArrayList<>();
    private static final int numberOfParts = Integer.parseInt(PropertiesContext.get("${env}.bcs.number_of_parts"));

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createNewBatch();
        batchIdentity = batch.getIdentity();
    }

    @AfterClass
    public static void testCleanup() {
        BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @SneakyThrows
    @TestRail(testCaseId = {"9111"})
    @Description("Test costing scenarion, includes creating a new batch, with multiple parts and waiting for the " +
        "costing process to complete for all parts. Then retrieve costing results.")
    public void costParts() {
        int countOfAttempts = 0;

        this.addPartsToBatch();

        do {
            for (int i = 0; i < batchPartsInProcess.size(); i++) {
                checkPartStatus(batchPartsInProcess.get(i));
            }

            Thread.sleep(Constants.POLLING_WAIT);
            countOfAttempts++;

        } while (batchPartsInProcess.size() != 0 || countOfAttempts == Constants.POLLING_INTERVALS);


        if (erroredBatchParts.size() != 0) {
            StringBuilder errorLog = new StringBuilder("Errored parts: \n");
            erroredBatchParts.forEach(part -> {
                errorLog.append("Part identity: ")
                    .append(part.getIdentity())
                    .append(" error message: ")
                    .append(part.getErrors())
                    .append("\n");
            });

            Assert.fail("Errored parts.\n" + errorLog);
        }
    }

    private void addPartsToBatch() {
        for (int i = 0; i < numberOfParts; i++) {
            NewPartRequest newPartRequest = BatchPartResources.getNewPartRequest();
            newPartRequest.setMaterialName("");

            Part batchPart = (Part) BatchPartResources.createNewBatchPart(newPartRequest,
                batchIdentity
            ).getResponseEntity();

            batchPartsInProcess.add(batchPart.getIdentity());
        }
    }

    private void checkPartStatus(final String batchPartId) {
        final Part part = ((Part) BatchPartResources.getBatchPartRepresentation(batchIdentity, batchPartId, Part.class)
            .getResponseEntity());

        final String elementState = part.getState();

        if (elementState.equals(BCSState.ERRORED.getState())) {
            erroredBatchParts.add(part);
            batchPartsInProcess.remove(batchPartId);
        }

        if (elementState.equals(BCSState.COMPLETED.getState())) {
            batchPartsInProcess.remove(batchPartId);
        }
    }
}
