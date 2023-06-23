package tests.acs;

import com.apriori.acs.entity.response.acs.designGuidance.DesignGuidanceResponse;
import com.apriori.acs.entity.response.acs.designGuidance.InfosByTopics;
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

        InfosByTopics infosByTopics = designGuidanceResponse.getInfosByTopics();
//        ProcessInstanceKey processInstanceKey = costResultsRootItem.getProcessInstanceKey();
//        ResultMapBean resultMapBean = costResultsRootItem.getResultMapBean();
//        PropertyValueMap propertyValueMap = resultMapBean.getPropertyValueMap();

        softAssertions.assertThat(designGuidanceResponse.getInfosByTopics()).isNotNull();
        softAssertions.assertThat(designGuidanceResponse.getCostingFailed()).isEqualTo(false);
//        softAssertions.assertThat(infosByTopics.getDTC_MESSAGES()).isEqualTo(guidanceTopics);

        softAssertions.assertThat(designGuidanceResponse.getInfosByTopics()).toString().contains(guidanceTopics);

//        softAssertions.assertThat(costResultsRootItem.getCostingFailed()).isEqualTo(false);
//        softAssertions.assertThat(costResultsRootItem.getDepth()).isEqualTo("ROOT");
//        softAssertions.assertThat(costResultsRootItem.getSecondaryProcess()).isEqualTo(false);
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
