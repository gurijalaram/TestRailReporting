package com.apriori.apitests.cis;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.cis.apicalls.PartResources;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

public class CisPartResources extends TestUtil {
    private static PropertyStore propertyStore;

    @BeforeClass
    public static void testSetup() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(Constants.getApitestsResourcePath() +
                "/property-store.json", PropertyStore.class);
    }

    @Test
    @TestRail(testCaseId = "4175")
    @Description("Create a new part using the CIS API")
    public void createNewPart() {
        Object obj = JsonManager.deserializeJsonFromFile(Constants.getApitestsBasePath() +
                "/apitests/cis/testdata/CreatePartData.json", NewPartRequest.class);

        PartResources.createNewPart(obj);

    }

    @Test
    @TestRail(testCaseId = "4174")
    @Description("API returns a list of all the available parts in the CIS DB")
    public void getParts() {
        PartResources.getParts();
    }


    @Test
    @TestRail(testCaseId = "4176")
    @Description("API returns a representation of a single part in the CIS DB")
    public void getPart() {
        PartResources.getPartRepresentation(propertyStore.getPartIdentity());
    }

}
