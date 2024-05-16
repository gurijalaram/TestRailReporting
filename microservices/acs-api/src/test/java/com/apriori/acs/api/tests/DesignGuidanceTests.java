package com.apriori.acs.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_EXPERT;

import com.apriori.acs.api.models.response.acs.designGuidance.DesignGuidanceResponse;
import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.api.tests.workorders.WorkorderApiUtils;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class DesignGuidanceTests {
    private final AcsResources acsResources;
    private final WorkorderApiUtils workorderApiUtils;

    public DesignGuidanceTests() {
        UserCredentials userCredentials = UserUtil.getUser(APRIORI_EXPERT);
        acsResources = new AcsResources(userCredentials);
        workorderApiUtils = new WorkorderApiUtils(userCredentials);
    }

    private void designGuidanceAssertion(DesignGuidanceResponse designGuidanceResponse, String guidanceTopics) {

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(designGuidanceResponse.getCostingFailed()).isEqualTo(false);
        softAssertions.assertThat(designGuidanceResponse.getInfosByTopics().toString().contains(guidanceTopics)).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 28350)
    @Description("Test Get Design Guidance for Casting - Die")
    public void testGetDesignGuidanceCastingDie() {
        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "DTCCastingIssues.catpart", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(id = 28351)
    @Description("Test Get Design Guidance for Casting - Sand")
    public void testGetDesignGuidanceCastingSand() {
        String processGroup = ProcessGroupEnum.CASTING_SAND.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "DTCCastingIssues.catpart", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(id = 28352)
    @Description("Test Get Design Guidance for Casting - Investment")
    public void testGetDesignGuidanceCastingInvestment() {
        String processGroup = ProcessGroupEnum.CASTING_INVESTMENT.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "piston_model1.prt", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(id = 28353)
    @Description("Test Get Design Guidance for Sheet Metal")
    public void testGetDesignGuidanceSheetMetal() {
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "flanged_hole.prt", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(id = 28354)
    @Description("Test Get Design Guidance for Sheet Metal - Hydroforming")
    public void testGetDesignGuidanceSheetMetalHydroforming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "FlangedRound.SLDPRT", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(id = 28355)
    @Description("Test Get Design Guidance for Sheet Metal - Transfer Die")
    public void testGetDesignGuidanceSheetMetalTransferDie() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "SheetMetal.prt", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(id = 28357)
    @Description("Test Get Design Guidance for Sheet Metal - Roll Forming")
    public void testGetDesignGuidanceSheetMetalRollForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_ROLLFORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "z_purlin.prt.2", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }

    @Test
    @TestRail(id = 28356)
    @Description("Test Get Design Guidance for Sheet Metal - Stretch Forming")
    public void testGetDesignGuidanceSheetMetalStretchForming() {
        String processGroup = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "Hydroforming.stp", workorderApiUtils.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES");

        designGuidanceAssertion(designGuidanceResponse, "DTC_MESSAGES");
    }
}
