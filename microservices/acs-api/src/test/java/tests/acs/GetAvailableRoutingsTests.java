package tests.acs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.acs.getavailableroutings.GetAvailableRoutingsResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.AcsTest;

public class GetAvailableRoutingsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14814")
    @Description("Get available routings after Cost")
    public void testGetAvailableRoutingsCosted() {
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

        GetAvailableRoutingsResponse response = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey(),
            "aPriori USA",
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        assertThat(response.getName(), is(notNullValue()));
        assertThat(response.getDisplayName(), is(notNullValue()));
        assertThat(response.getPlantName(), is(notNullValue()));
        assertThat(response.getProcessGroupName(), is(notNullValue()));
        assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus(), is(notNullValue()));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "14812")
    @Description("Get available routings before Cost")
    public void testGetAvailableRoutingsUncosted() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

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

        GetAvailableRoutingsResponse response = acsResources.getAvailableRoutings(
                fileUploadOutputs.getScenarioIterationKey(),
                "aPriori USA",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        assertThat(response.getName(), is(notNullValue()));
        assertThat(response.getDisplayName(), is(notNullValue()));
        assertThat(response.getPlantName(), is(notNullValue()));
        assertThat(response.getProcessGroupName(), is(notNullValue()));
        assertThat(response.getChildren().get(0).getChildren().get(0).getCostStatus(), is("UNCOSTED"));
    }
}