package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getactiveaxesbyscenarioiterationkey.GetActiveAxesByScenarioIterationKeyResponse;
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
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

import java.util.ArrayList;
import java.util.List;

public class GetActiveAxesByScenarioIterationKeyTests {

    @Test
    @Issue("COST-173")
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10980")
    @Description("Validate Get Active Axes by Scenario Iteration Key")
    public void testGetActiveAxesByScenarioIterationKey() {
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

        List<String> infoForUrl = new ArrayList<>();
        ScenarioIterationKey scenarioIterationKey = costOutputs.getScenarioIterationKey();
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getWorkspaceId().toString());
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getTypeName());
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getMasterName());
        infoForUrl.add(scenarioIterationKey.getScenarioKey().getStateName());
        infoForUrl.add(scenarioIterationKey.getIteration().toString());

        GetActiveAxesByScenarioIterationKeyResponse getActiveAxesResponse = acsResources.getActiveAxesByScenarioIterationKey(infoForUrl);

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
