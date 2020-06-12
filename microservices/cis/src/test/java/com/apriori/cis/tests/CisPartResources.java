package com.apriori.cis.tests;

import com.apriori.apibase.utils.TestUtil;

import com.apriori.cis.controller.PartResources;
import com.apriori.cis.entity.request.NewPartRequest;

import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;

import org.junit.Test;

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
        Object obj = JsonManager.serializeJsonFromFile(
                getClass().getClassLoader().getResource("CreatePartData.json").getPath(), NewPartRequest.class);

        PartResources.createNewPart(obj);

    }
}
