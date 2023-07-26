package com.integration.tests;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.parts.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.sds.controller.IterationController;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

@Slf4j
public class BcsSdsIntegrationTests {
    private static Batch batch;

    @BeforeClass
    public static void testSetup() {
        batch = (Batch) BatchResources.createBatch().getResponseEntity();
    }

    @AfterClass
    public static void testCleanup() {
    }

    @Test
    @TestRail(id = 9127)
    @Description("BCS: Set material mode when the material name is not blank")
    public void testMaterialMode() {
        Map<String, String> identities;
        String materialMode;
        // Material Name is sent
        Part part = (Part) BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        BcsUtils.waitForCostingState(batch.getIdentity(), part.getIdentity());

        Part newPart = (Part) BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
            part.getIdentity()).getResponseEntity();

        identities = BcsUtils.getScenarioAndComponent(newPart.getUrl());
        materialMode = IterationController.getMaterialMode(
            identities.get("scenario"),
            identities.get("component"));
        Assert.assertEquals(materialMode.toLowerCase(), "manual");
    }

    @Test
    @TestRail(id = 9112)
    @Description("BCS: Set material mode when the material name is null")
    public void testMaterialModeIsNull() {
        Map<String, String> identities;
        Part part;
        String materialMode;
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setMaterial("");
        ResponseWrapper<Part> response = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());
        part = response.getResponseEntity();
        BcsUtils.waitForCostingState(batch.getIdentity(), part.getIdentity());

        part = (Part) BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
            part.getIdentity()).getResponseEntity();

        identities = BcsUtils.getScenarioAndComponent(part.getUrl());
        materialMode = IterationController.getMaterialMode(
            identities.get("scenario"),
            identities.get("component"));
        Assert.assertEquals(materialMode.toLowerCase(), "cad");
    }
}
