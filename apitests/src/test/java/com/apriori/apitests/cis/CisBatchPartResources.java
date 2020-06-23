package com.apriori.apitests.cis;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.cis.apicalls.BatchPartResources;
import com.apriori.apibase.services.cis.apicalls.BatchResources;
import com.apriori.apibase.services.cis.objects.Batch;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

public class CisBatchPartResources extends TestUtil {
    private static PropertyStore propertyStore;
    private static Batch batch;

    @BeforeClass
    public static void testSetup() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(Constants.getApitestsResourcePath() +
                "/property-store.json", PropertyStore.class);

        batch = BatchResources.createNewBatch();
    }

    @Test
    @TestRail(testCaseId = "4280")
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void createBatchParts() {

        NewPartRequest newPartRequest =
                (NewPartRequest)JsonManager.deserializeJsonFromFile(Constants.getApitestsBasePath() +
                        "/apitests/cis/testdata/CreatePartData.json", NewPartRequest.class);
        BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());

    }

    @Test
    @TestRail(testCaseId = "4279")
    @Description("API returns a list of Parts per Batch in the CIS DB")
    public void getBatchParts() {

        BatchPartResources.getBatchParts();
    }

    @Test
    @TestRail(testCaseId = "4281")
    @Description("API returns a representation of a single Batch-Part in the CIS DB")
    public void getBatchPart() {
        BatchPartResources.getBatchPartRepresentation(propertyStore.getBatchIdentity(), propertyStore.getPartIdentity());
    }
}
