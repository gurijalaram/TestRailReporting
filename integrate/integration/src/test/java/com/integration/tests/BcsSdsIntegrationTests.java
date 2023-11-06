package com.integration.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.controller.BatchResources;
import com.apriori.bcs.api.models.request.parts.NewPartRequest;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.bcs.api.utils.BcsUtils;
import com.apriori.sds.api.controller.IterationController;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class BcsSdsIntegrationTests {
    private static Batch batch;

    @BeforeAll
    public static void testSetup() {
        batch = (Batch) BatchResources.createBatch().getResponseEntity();
    }

    @AfterAll
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
        assertEquals(materialMode.toLowerCase(), "manual");
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
        assertEquals(materialMode.toLowerCase(), "cad");
    }
}
