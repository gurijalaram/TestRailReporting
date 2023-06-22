package tests.acs;

import com.apriori.acs.entity.response.acs.designGuidance.DesignGuidanceResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.junit.Test;
import tests.workorders.WorkorderAPITests;

public class DesignGuidanceTests {
    private AcsResources acsResources = new AcsResources();
    private WorkorderAPITests workorderAPITests = new WorkorderAPITests();

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Casting - Die")
    public void testGetDesignGuidanceCastingDie() {
        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();

        CostOrderStatusOutputs costOutputs = acsResources.uploadAndCost(processGroup, "DTCCastingIssues.catpart", workorderAPITests.setupProductionInfoInputs());
        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(costOutputs.getScenarioIterationKey(), "DTC_MESSAGES","FAILED_GCDS");
    }
}
