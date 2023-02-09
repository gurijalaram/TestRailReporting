package tests.acs;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.acs.costresults.CostResultsRootResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.AcsTest;

import java.util.LinkedHashMap;

public class CostResultsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "21579")
    @Description("Get Cost Results after Costing Sheet Metal")
    public void testGetCostRootResultsSheetMetal() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        CostResultsRootResponse response = acsResources.getCostResultsRoot(
            costOutputs.getScenarioIterationKey(),
            "ROOT"
        );

        SoftAssertions softAssertions = new SoftAssertions();

        //TODO: Assert on sustainability values once BA-2858 complete
        Object processInstanceKey = ((LinkedHashMap<String, String>) response.get(0)).get("processInstanceKey");
        Object resultMapBean = ((LinkedHashMap<String, String>) response.get(0)).get("resultMapBean");
        softAssertions.assertThat(((LinkedHashMap<String, String>) processInstanceKey).get("processGroupName")).isEqualTo("Sheet Metal");
        softAssertions.assertThat(((LinkedHashMap<String, String>) resultMapBean)).isNotNull();
        softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("costingFailed")).isEqualTo(false);
        softAssertions.assertThat(((LinkedHashMap<String, String>) response.get(0)).get("depth")).isEqualTo("ROOT");
        softAssertions.assertThat(((LinkedHashMap<String, Boolean>) response.get(0)).get("secondaryProcess")).isEqualTo(false);
        softAssertions.assertAll();
    }
}
