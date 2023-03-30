package tests.acs;

import com.apriori.acs.entity.response.acs.GcdProperties.GcdPropertiesResponse;
import com.apriori.acs.entity.response.acs.GcdProperties.PropertiesToSet;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.AcsTest;

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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "17203")
    @Description("Get save GCD Properties for Sheet Metal")
    public void testSaveGcdPropertiesSheetMetal() {
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "bracket_basic.prt", workorderAPITests.setupProductionInfoInputs());

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .roughnessRz("0.4")
            .concentricity("0.8")
            .build();

        GcdPropertiesResponse saveGcdPropertiesresponse = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "SimpleHole:2",
            propertiesToSet,
            Collections.singletonList("roughness")
        );

        saveGcdPropertiesAssertion(saveGcdPropertiesresponse);
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "17204")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "17207")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "17206")
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
    @Category(AcsTest.class)
    @TestRail(testCaseId = "17205")
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
}
