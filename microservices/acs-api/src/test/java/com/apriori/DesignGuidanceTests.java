package tests.acs;

import com.apriori.acs.entity.response.acs.designGuidance.DesignGuidanceResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import tests.workorders.WorkorderAPITests;

public class DesignGuidanceTests {
    private AcsResources acsResources = new AcsResources();
    private WorkorderAPITests workorderAPITests = new WorkorderAPITests();

    private void designGuidanceAssertion(DesignGuidanceResponse designGuidanceResponse, String guidanceTopics) {

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(designGuidanceResponse.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(designGuidanceResponse.getInfosByTopics().toString().contains(guidanceTopics)).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Casting - Die")
    public void testGetDesignGuidanceCastingDie() {
        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "DTCCastingIssues.catpart", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Casting - Sand")
    public void testGetDesignGuidanceCastingSand() {
        String processGroup = ProcessGroupEnum.CASTING_SAND.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "DTCCastingIssues.catpart", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Casting - Investment")
    public void testGetDesignGuidanceCastingInvestment() {
        String processGroup = ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "piston_model1.prt", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Sheet Metal")
    public void testGetDesignGuidanceSheetMetal() {
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "SheMetDTC.SLDPRT", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Sheet Metal - Hydroforming")
    public void testGetDesignGuidanceSheetMetalHydroforming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "FlangedRound.SLDPRT", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Sheet Metal - Transfer Die")
    public void testGetDesignGuidanceSheetMetalTransferDie() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "SheetMetal.prt", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Sheet Metal - Roll Forming")
    public void testGetDesignGuidanceSheetMetalRollForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "z_purlin.prt.2", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Sheet Metal - Stretch Forming")
    public void testGetDesignGuidanceSheetMetalStretchForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Hydroforming.stp", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }
}
