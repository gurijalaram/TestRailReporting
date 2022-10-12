package tests.acs;

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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetAvailableRoutingsTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "15429")
    @Description("Get available routings after Cost")
    public void testGetAvailableRoutings() {
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

        GetAvailableRoutingsResponse getAvailableRoutingsResponse = acsResources.getAvailableRoutings(
            costOutputs.getScenarioIterationKey()
        );

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }
}
