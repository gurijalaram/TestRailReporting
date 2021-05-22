package com.apriori.bcs.tests;

import com.apriori.apibase.services.CustomerBase;
import com.apriori.bcs.controller.CustomerResources;
import com.apriori.bcs.entity.request.PatchCostingPreferenceRequest;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Random;

public class CustomerResourcesTest extends CustomerBase {

    @Test
    @TestRail(testCaseId = {"4169"})
    @Description("API returns a list of tolerances associated with a specific CIS customer")
    public void getCostingPreferences() {
        CustomerResources.getCostingPreferences();
    }

    @Test
    @TestRail(testCaseId = {"4171"})
    @Description("API returns a list of process groups associated with a specific CIS customer")
    public void getProcessGroups() {
        CustomerResources.getProcessGroups();
    }

    @Test
    @TestRail(testCaseId = {"4172"})
    @Description("API returns a list of user defined attributes associated with a specific CIS customer")
    public void getUserDefinedAttributes() {
        CustomerResources.getUserDefinedAttributes();
    }


    @Test
    @TestRail(testCaseId = {"4173"})
    @Description("API returns a list of VPEs associated with a specific CIS customer")
    public void getVirtualProductionEnvironments() {
        CustomerResources.getVirtualProductEnvironments();
    }

    @Test
    @TestRail(testCaseId = {"4170"})
    @Description("Update a customer's costing preferences")
    public void patchCostingPreferences() {
        Random rand = new Random();
        PatchCostingPreferenceRequest cp =
                (PatchCostingPreferenceRequest) JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/UpdateCostingPreferences.json"), PatchCostingPreferenceRequest.class);
        Double value = rand.nextDouble();
        cp.setCadToleranceReplacement(100.00);
        cp.setMinCadToleranceThreshold(value);
        CustomerResources.patchCostingPreferences(cp);
    }

    @Test
    @TestRail(testCaseId = {"7955"})
    @Description("Return a list of digital factories")
    public void getDigitalFactories() {
        CustomerResources.getDigitalFactories();
    }

    @Test
    @TestRail(testCaseId = {"7956"})
    @Description("Return a customer's custom attributes")
    public void getCustomAttributes() {
        CustomerResources.getCustomAttributes();
    }
}