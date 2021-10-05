package com.apriori.integration.tests;

import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.sds.controller.IterationController;
import com.apriori.utils.TestRail;

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
    private static Part part;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createNewBatch();
    }

    @AfterClass
    public static void testCleanup() {
        BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(testCaseId = {"9127"})
    @Description("BCS: Set material mode when the material name is not blank")
    public void testMaterialMode() {
        NewPartRequest newPartRequest;
        String materialMode;
        Map<String, String> identities;
        Part newPart;

        // Material Name is sent
        newPartRequest = BatchPartResources.getNewPartRequest();
        part = (Part) BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity()).getResponseEntity();

        BcsUtils.waitForCostingState(batch.getIdentity(), part.getIdentity());

        newPart = (Part) BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
                part.getIdentity()).getResponseEntity();

        identities = BcsUtils.getScenarioAndComponent(newPart.getUrl());
        materialMode = IterationController.getMaterialMode(
                identities.get("scenario"),
                identities.get("component"));
        Assert.assertEquals(materialMode.toLowerCase(), "manual");
    }

    @Test
    @TestRail(testCaseId = {"9112"})
    @Description("BCS: Set material mode when the material name is null")
    public void testMaterialModeIsNull() {
        NewPartRequest newPartRequest;
        String materialMode;
        Map<String, String> identities;
        Part newPart;

        newPartRequest = BatchPartResources.getNewPartRequest();
        newPartRequest.setMaterialName(null);
        part = (Part) BatchPartResources.createNewBatchPartNoMaterial(newPartRequest, batch.getIdentity()).getResponseEntity();

        BcsUtils.waitForCostingState(batch.getIdentity(), part.getIdentity());

        newPart = (Part) BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
                part.getIdentity()).getResponseEntity();

        identities = BcsUtils.getScenarioAndComponent(newPart.getUrl());
        materialMode = IterationController.getMaterialMode(
                identities.get("scenario"),
                identities.get("component"));
        Assert.assertEquals(materialMode.toLowerCase(), "cad");
    }
}
