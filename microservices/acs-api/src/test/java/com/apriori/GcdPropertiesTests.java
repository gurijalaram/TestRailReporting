package com.apriori;

import com.apriori.acs.models.response.acs.GcdProperties.GcdPropertiesResponse;
import com.apriori.acs.models.response.acs.GcdProperties.PropertiesToSet;
import com.apriori.acs.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.testrail.TestRail;
import com.apriori.workorders.WorkorderAPITests;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

public class GcdPropertiesTests {

    private void saveGcdPropertiesAssertion(GcdPropertiesResponse gcdPropertiesResponse) {

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(gcdPropertiesResponse.getScenarioInputSet()).isNotNull();
        softAssertions.assertThat(gcdPropertiesResponse.getSuccesses()).isNotNull();
        softAssertions.assertThat(gcdPropertiesResponse.getFailures()).isNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 17203)
    @Description("Get save GCD Properties for Sheet Metal")
    public void testSaveGcdPropertiesSheetMetal() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "SheetMetalTray.SLDPRT", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .roughnessRz("0.4")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "ComplexHole:2",
            propertiesToSet,
            Collections.singletonList("roughness")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17204)
    @Description("Get save GCD Properties for Sheet Metal - Transfer Die")
    public void testSaveGcdPropertiesSheetMetalTransferDie() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "bracket_basic.prt", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .roughnessRz("0.7")
            .positionTolerance("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "SimpleHole:2",
            propertiesToSet,
            Collections.singletonList("totalRunout")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17207)
    @Description("Get save GCD Properties for Sheet Metal - Stretch Forming")
    public void testSaveGcdPropertiesSheetMetalStretchForming() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Hydroforming.stp", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .roughness("0.7")
            .straightness("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "PlanarFace:2",
            propertiesToSet,
            Collections.singletonList("flatness")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17206)
    @Description("Get save GCD Properties for Sheet Metal - Roll Forming")
    public void testSaveGcdPropertiesSheetMetalRollForming() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "z_purlin.prt.1", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .tolerance("0.7")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "CurvedWall:2",
            propertiesToSet,
            Arrays.asList("positionTolerance", "diamTolerance")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17205)
    @Description("Get save GCD Properties for Sheet Metal - Hydroforming")
    public void testSaveGcdPropertiesSheetMetalHydroforming() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "FlangedRound.SLDPRT", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .totalRunout("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "SimpleHole:1",
            propertiesToSet,
            Arrays.asList("straightness", "parallelism")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17193)
    @Description("Get save GCD Properties for Sheet Metal - Additive Manufacturing")
    public void testSaveGcdPropertiesAdditiveManufacturing() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "ADD-LOW-001.SLDPRT", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .circularity("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "CurvedWall:1",
            propertiesToSet,
            Arrays.asList("straightness", "parallelism")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17194)
    @Description("Get save GCD Properties for Sheet Metal - Bar & Tube Fab")
    public void testSaveGcdPropertiesBarAndTubeFab() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "4287081-2.prt.2", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .bendAngleTolerance("1.4")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "Bend:1",
            propertiesToSet,
            Collections.singletonList("parallelism")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17195)
    @Description("Get save GCD Properties for Casting - Die")
    public void testSaveGcdPropertiesCastingDie() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "manifold.prt.1", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .profileOfSurface("0.3")
            .symmetry("0.8")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "CurvedSurface:1",
            propertiesToSet,
            Arrays.asList("runout", "roughness")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17196)
    @Description("Get save GCD Properties for Casting - Sand")
    public void testSaveGcdPropertiesCastingSand() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.CASTING_SAND.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "SandCastIssues.SLDPRT", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .parallelism("0.5")
            .perpendicularity("0.2")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "PlanarFace:1",
            propertiesToSet,
            Arrays.asList("straightness", "symmetry")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17197)
    @Description("Get save GCD Properties for Casting - Investment")
    public void testSaveGcdPropertiesCastingInvestment() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "AP-000-506.prt.1", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .parallelism("0.5")
            .perpendicularity("0.2")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "CurvedWall:11",
            propertiesToSet,
            Arrays.asList("straightness", "symmetry")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17198)
    @Description("Get save GCD Properties for Forging")
    public void testSaveGcdPropertiesForging() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.FORGING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Pin.SLDPRT", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .positionTolerance("0.5")
            .profileOfSurface("0.2")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "PlanarFace:4",
            propertiesToSet,
            null
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17199)
    @Description("Get save GCD Properties for Plastic Molding")
    public void testSaveGcdPropertiesPlasticMolding() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "M3CapScrew.CATPart", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .positionTolerance("0.5")
            .profileOfSurface("0.2")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "PlanarFace:4",
            propertiesToSet,
            null
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17200)
    @Description("Get save GCD Properties for Powder Metal")
    public void testSaveGcdPropertiesPowderMetal() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.POWDER_METAL.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "116-5809.prt.1", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .positionTolerance("1.5")
            .profileOfSurface("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "PlanarFace:2",
            propertiesToSet,
            null
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17202)
    @Description("Get save GCD Properties for Roto & Blow Molding")
    public void testSaveGcdPropertiesRotoBlowMolding() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "blow_mold_duct_1.prt.1", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .roughness("1.5")
            .profileOfSurface("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "CurvedSurface:1",
            propertiesToSet,
            Arrays.asList("profileOfSurface", "symmetry")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17208)
    @Description("Get save GCD Properties for Sheet Plastic")
    public void testSaveGcdPropertiesSheetPlastic() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "r151294.prt.1", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .roughnessRz("1.5")
            .profileOfSurface("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "CurvedSurface:1",
            propertiesToSet,
            Arrays.asList("roughness", "symmetry")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @TestRail(id = 17209)
    @Description("Get save GCD Properties for Stock Machining")
    public void testSaveGcdPropertiesStockMachining() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.STOCK_MACHINING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace.CATPart", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .roughnessRz("1.5")
            .profileOfSurface("0.3")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "CurvedSurface:1",
            propertiesToSet,
            Arrays.asList("roughness", "symmetry")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }
}
