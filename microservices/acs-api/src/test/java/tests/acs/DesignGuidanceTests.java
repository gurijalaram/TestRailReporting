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
}
