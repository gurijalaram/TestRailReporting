package com.apriori.acs.api.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.api.models.request.workorders.NewPartRequest;
import com.apriori.acs.api.models.response.acs.activeaxesbyscenarioiterationkey.ActiveAxesByScenarioIterationKeyResponse;
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
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class ActiveAxesByScenarioIterationKeyTests extends TestUtil {

    @Test
    @Disabled("Jira bug ticket (COST-173) has been close with the reason of won't do")
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
