package tests;

import com.apriori.acs.entity.response.getactivedimensionsbyscenarioiterationkey.GetActiveDimensionsResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.upload.FileResponse;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.entity.response.upload.ScenarioIterationKey;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.FileUploadResources;
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
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "CreatePartData.json"
            ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.CASTING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
            "Casting.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, testScenarioName);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup
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
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyValueMap().getLength() == 51.5405);
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getLength().getName().equals("length"));
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getLength().getUnitTypeName().equals("mm"));

        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyValueMap().getWidth() == 30.2973);
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getWidth().getName().equals("width"));
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getWidth().getUnitTypeName().equals("mm"));

        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyValueMap().getHeight() == 25.0);
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getHeight().getName().equals("height"));
        softAssertions.assertThat(getActiveDimensionsResponse.getPropertyInfoMap().getHeight().getUnitTypeName().equals("mm"));

        softAssertions.assertAll();
    }
}
