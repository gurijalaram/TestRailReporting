package com.apriori.apitests.cis;

import com.apriori.apibase.services.cis.apicalls.PartResources;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class CisPartResources extends TestUtil {

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
        PartResources.getPartRepresentation();
    }

    @Test
    @TestRail(testCaseId = "4175")
    @Description("Create a new part using the CIS API")
    public void createNewPart() {
        Object obj = JsonManager.serializeJsonFromFile(Constants.getApitestsBasePath() +
                "/apitests/cis/testdata/CreatePartData.json", NewPartRequest.class);

        PartResources.createNewPart(obj);

    }
}
