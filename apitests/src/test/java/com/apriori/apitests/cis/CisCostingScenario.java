package com.apriori.apitests.cis;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.cis.CisUtils;
import com.apriori.apibase.services.cis.apicalls.BatchPartResources;
import com.apriori.apibase.services.cis.apicalls.BatchResources;
import com.apriori.apibase.services.cis.apicalls.PartResources;
import com.apriori.apibase.services.cis.objects.Batch;
import com.apriori.apibase.services.cis.objects.Part;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CisCostingScenario extends TestUtil {

    private static final Logger logger = LoggerFactory.getLogger(CisCostingScenario.class);

    @Test
    @TestRail(testCaseId = {"4278", "4284", "4280", "4177"})
    @Description("Test costing scenarion, includes creating a new batch, a new part and waiting for the costing " +
            "process to complete. Then retrieve costing results.")
    public void costPart() {
        Integer defaultTimeout = 18;

        // create batch
        Batch batch = BatchResources.createNewBatch();

        String batchIdentity = "";
        try {
            batchIdentity = CisUtils.getIdentity(batch, Batch.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        // create batch part
        NewPartRequest newPartRequest =
                (NewPartRequest)JsonManager.deserializeJsonFromFile(Constants.getApitestsBasePath() +
                        "/apitests/cis/testdata/CreatePartData.json", NewPartRequest.class);
        Part batchPart = (Part)BatchPartResources.createNewBatchPart(newPartRequest, batchIdentity);

        String partIdentity = "";
        try {
            partIdentity = CisUtils.getIdentity(batchPart, Part.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        // start costing
        try {
            BatchResources.startCosting(batchIdentity);
        } catch (Exception ignored) {

        }

        // poll for part state/batch state
        Object partDetails = null;
        Boolean isPartComplete = false;
        Integer count = 0;
        while (count <= defaultTimeout) {
            partDetails =
                    BatchPartResources.getBatchPartRepresentation(batchIdentity, partIdentity).getResponseEntity();
            isPartComplete = pollState(partDetails, Part.class);

            if (isPartComplete) {
                break;
            }
            count += 1;
        }
        Assert.assertEquals(true, isPartComplete);

        Object batchDetails = null;
        Boolean isBatchComplete = false;
        count = 0;
        while (count <= defaultTimeout) {
            batchDetails = BatchResources.getBatchRepresentation(batchIdentity).getResponseEntity();
            isBatchComplete = pollState(batchDetails, Batch.class);

            if (isBatchComplete) {
                break;
            }
            count += 1;
        }
        Assert.assertEquals(true, isBatchComplete);

        PartResources.getPartCosting(partIdentity);

        PropertyStore propertyStore = new PropertyStore();
        propertyStore.setBatchIdentity(batchIdentity);
        propertyStore.setPartIdentity(partIdentity);

        JsonManager.serializeJsonToFile(Constants.getApitestsResourcePath() +
                "/property-store.json", propertyStore);
    }

    private Boolean pollState(Object obj, Class klass) {
        String state = "";
        try {
            state = CisUtils.getState(obj, klass);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        if (state.toUpperCase().equals("COMPLETED")) {
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
