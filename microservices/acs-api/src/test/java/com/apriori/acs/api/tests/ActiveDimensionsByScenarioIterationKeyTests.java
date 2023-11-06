package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.request.workorders.NewPartRequest;
import com.apriori.acs.api.models.response.acs.activedimensionsbyscenarioiterationkey.ActiveDimensionsResponse;
import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.api.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.acs.api.utils.workorders.FileUploadResources;
import com.apriori.fms.api.models.response.FileResponse;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class ActiveDimensionsByScenarioIterationKeyTests extends TestUtil {

    @Test
    @TestRail(id = 10941)
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

        ActiveDimensionsResponse getActiveDimensionsResponse = acsResources.getActiveDimensionsByScenarioIterationKeyEndpoint(infoToGetDimensions);

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
