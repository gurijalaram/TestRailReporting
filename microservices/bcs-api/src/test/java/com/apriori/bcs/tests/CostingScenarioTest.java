package com.apriori.bcs.tests;

import static org.junit.Assert.fail;

import com.apriori.apibase.services.PropertyStore;
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
import io.qameta.allure.Issue;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CostingScenarioTest extends TestUtil {

    private static final Logger logger = LoggerFactory.getLogger(CostingScenarioTest.class);
    private static Boolean exitTest = false;

    @Issue("AP-70043")
    @Test
    @TestRail(testCaseId = {"4278", "4177"})
    @Description("Test costing scenarion, includes creating a new batch, a new part and waiting for the costing " +
            "process to complete. Then retrieve costing results.")
    public void costPart() {
        Integer defaultTimeout = Constants.getPollingTimeout();

        // create batch
        Batch batch = BatchResources.createNewBatch();
        String batchIdentity = batch.getIdentity();

        // create batch part
        NewPartRequest newPartRequest =
                (NewPartRequest)JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);

        Part batchPart = (Part)BatchPartResources.createNewBatchPart(newPartRequest, batchIdentity);

        String partIdentity = batchPart.getIdentity();

        // start costing
        try {
            BatchResources.startCosting(batchIdentity);
        } catch (Exception ignored) {

        }

        // poll for part state/batch state
        Object partDetails;
        Boolean isPartComplete = false;
        Integer count = 0;
        while (count <= defaultTimeout) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
            isPartComplete = pollState(partDetails, Part.class);

            if (exitTest) {
                String errors = BcsUtils.getErrors(batchPart, Part.class);
                logger.error(errors);
                fail("Part was in state 'ERRORED'");
                return;
            }

            if (isPartComplete) {
                break;
            }
            count += 1;
        }
        Assert.assertEquals(true, isPartComplete);

        Object batchDetails;
        Boolean isBatchComplete = false;
        count = 0;
        while (count <= defaultTimeout) {
            batchDetails = BatchResources.getBatchRepresentation(batchIdentity).getResponseEntity();
            isBatchComplete = pollState(batchDetails, Batch.class);


            if (exitTest) {
                fail("Batch was in state 'ERRORED'");
                return;
            }

            if (isBatchComplete) {
                break;
            }
            count += 1;
        }
        Assert.assertEquals(true, isBatchComplete);

        BatchPartResources.getResults(batchIdentity, partIdentity);

        PropertyStore propertyStore = new PropertyStore();
        propertyStore.setBatchIdentity(batchIdentity);
        propertyStore.setPartIdentity(partIdentity);

        JsonManager.serializeJsonToFile(
            FileResourceUtil.getResourceAsFile("property-store.json").getPath(), propertyStore);

    }

    private Boolean pollState(Object obj, Class klass) {
        String state = "";
        try {
            state = BcsUtils.getState(obj, klass);
            if (state.equalsIgnoreCase("ERRORED")) {
                exitTest = true;
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        if (state.equalsIgnoreCase("COMPLETED")) {
            return true;
        } else {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
            }
        }

        return false;

    }
}
