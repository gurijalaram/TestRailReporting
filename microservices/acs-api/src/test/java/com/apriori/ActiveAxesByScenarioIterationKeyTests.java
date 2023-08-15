package com.apriori;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.models.request.workorders.NewPartRequest;
import com.apriori.acs.models.response.acs.activeaxesbyscenarioiterationkey.ActiveAxesByScenarioIterationKeyResponse;
import com.apriori.acs.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.json.JsonManager;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ActiveAxesByScenarioIterationKeyTests extends TestUtil {

    @Test
    @Issue("COST-173")
    @TestRail(id = 10980)
    @Description("Validate Get Active Axes by Scenario Iteration Key")
    public void testGetActiveAxesByScenarioIterationKey() {
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

        List<String> infoForUrl = new ArrayList<>();
        ScenarioIterationKey scenarioIterationKey = costOutputs.getScenarioIterationKey();
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getWorkspaceId().toString());
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getTypeName());
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getMasterName());
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getStateName());
        infoForUrl.add(scenarioIterationKey.getIteration().toString());

        ActiveAxesByScenarioIterationKeyResponse getActiveAxesResponse = acsResources.getActiveAxesByScenarioIterationKey(infoForUrl);

        String artifactTypeName = "Setup Axis";
        assertThat(getActiveAxesResponse.getItemList().get(0).getArtifactTypeName(), is(equalTo(artifactTypeName)));
        assertThat(getActiveAxesResponse.getItemList().get(0).getSequenceNumber(), is(equalTo(0)));
        assertThat(getActiveAxesResponse.getItemList().get(0).getDisplayName(), is(equalTo(artifactTypeName.concat(":1"))));

        assertThat(getActiveAxesResponse.getItemList().get(1).getArtifactTypeName(), is(equalTo(artifactTypeName)));
        assertThat(getActiveAxesResponse.getItemList().get(1).getSequenceNumber(), is(equalTo(2)));
        assertThat(getActiveAxesResponse.getItemList().get(1).getDisplayName(), is(equalTo(artifactTypeName.concat(":3"))));

        assertThat(getActiveAxesResponse.getItemList().get(2).getArtifactTypeName(), is(equalTo(artifactTypeName)));
        assertThat(getActiveAxesResponse.getItemList().get(2).getSequenceNumber(), is(equalTo(3)));
        assertThat(getActiveAxesResponse.getItemList().get(2).getDisplayName(), is(equalTo(artifactTypeName.concat(":4"))));
    }
}
