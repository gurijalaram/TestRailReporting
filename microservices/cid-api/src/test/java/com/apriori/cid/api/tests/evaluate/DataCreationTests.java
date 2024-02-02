package com.apriori.cid.api.tests.evaluate;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.DataCreationUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.rules.TestRulesAPI;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestRulesAPI.class)
public class DataCreationTests {

    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    public void dataCreateTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final String extension = ".SLDPRT";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + extension);
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ComponentInfoBuilder data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, extension, costingTemplate, currentUser).searchCreateComponent();

        softAssertions.assertThat(data.getScenarioIdentity()).isNotEmpty();
        softAssertions.assertThat(data.getComponentIdentity()).isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    public void dataCreateCostTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final String extension = ".SLDPRT";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + extension);
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, extension, costingTemplate, currentUser).createCostComponent();

        softAssertions.assertThat(data.getScenarioState()).isEqualTo(ScenarioStateEnum.COST_COMPLETE.getState());

        softAssertions.assertAll();
    }

    @Test
    public void dataCreatePublishTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final String extension = ".SLDPRT";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + extension);
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, extension, costingTemplate, currentUser).createPublishComponent();

        softAssertions.assertThat(data.getPublished()).isTrue();

        softAssertions.assertAll();
    }

    @Test
    public void dataCreateCostPublishTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final String extension = ".SLDPRT";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + extension);
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, extension, costingTemplate, currentUser).createCostPublishComponent();

        softAssertions.assertThat(data.getScenarioState()).isEqualTo(ScenarioStateEnum.COST_COMPLETE.getState());
        softAssertions.assertThat(data.getPublished()).isTrue();

        softAssertions.assertAll();
    }
}
