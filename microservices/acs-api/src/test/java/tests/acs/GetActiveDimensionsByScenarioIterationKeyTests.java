package tests.acs;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.acs.getactivedimensionsbyscenarioiterationkey.GetActiveDimensionsResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

import java.util.ArrayList;
import java.util.List;

public class GetActiveDimensionsByScenarioIterationKeyTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10941")
    @Description("Validate Get Active Dimensions by Scenario Iteration Key Endpoint")
    public void testGetActiveDimensionsByScenarioIterationKeyEndpoint() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        NewPartRequest productionInfoInputs = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "CreatePartData.json"
            ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.CASTING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "Casting.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, testScenarioName);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        List<String> infoToGetDimensions = new ArrayList<>();
        ScenarioIterationKey scenarioIterationKey = costOutputs.getScenarioIterationKey();
        infoToGetDimensions.add(scenarioIterationKey.getScenarioKey().getWorkspaceId().toString());
        infoToGetDimensions.add(scenarioIterationKey.getScenarioKey().getTypeName());
        infoToGetDimensions.add(scenarioIterationKey.getScenarioKey().getMasterName());
        infoToGetDimensions.add(scenarioIterationKey.getScenarioKey().getStateName());
        infoToGetDimensions.add(scenarioIterationKey.getIteration().toString());

        GetActiveDimensionsResponse getActiveDimensionsResponse = acsResources.getActiveDimensionsByScenarioIterationKeyEndpoint(infoToGetDimensions);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyValueMap().getLength()).isEqualTo(51.5405);
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getLength().getName()).isEqualTo("length");
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getLength().getUnitTypeName()).isEqualTo("mm");

        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyValueMap().getWidth()).isEqualTo(30.2973);
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getWidth().getName()).isEqualTo("width");
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getWidth().getUnitTypeName()).isEqualTo("mm");

        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyValueMap().getHeight()).isEqualTo(25.0);
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getHeight().getName()).isEqualTo("height");
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getHeight().getUnitTypeName()).isEqualTo("mm");

        softAssertions.assertAll();
    }
}
