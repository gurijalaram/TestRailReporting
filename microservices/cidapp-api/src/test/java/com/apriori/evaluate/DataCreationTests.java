package com.apriori.evaluate;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.DataCreationUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestRulesAPI.class)
public class DataCreationTests {

    private SoftAssertions softAssertions;

    @Test
    public void dataCreateTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ComponentInfoBuilder data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, costingTemplate, currentUser).searchCreateComponent();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(data.getScenarioIdentity()).isNotEmpty();
        softAssertions.assertThat(data.getComponentIdentity()).isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    public void dataCreateCostTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, costingTemplate, currentUser).createCostComponent();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(data.getScenarioState()).isEqualTo(ScenarioStateEnum.COST_COMPLETE.getState());

        softAssertions.assertAll();
    }

    @Test
    public void dataCreatePublishTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, costingTemplate, currentUser).createPublishComponent();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(data.getPublished()).isTrue();

        softAssertions.assertAll();
    }

    @Test
    public void dataCreateCostPublishTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroup.getProcessGroup()).build();
        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, costingTemplate, currentUser).createCostPublishComponent();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(data.getScenarioState()).isEqualTo(ScenarioStateEnum.COST_COMPLETE.getState());
        softAssertions.assertThat(data.getPublished()).isTrue();

        softAssertions.assertAll();
    }
}
