package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.DataCreationUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class DataCreationTests {

    private SoftAssertions softAssertions;

    @Test
    public void dataCreateTest() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder data = new DataCreationUtil(componentName, "AutoScenario521-1308300483105300", processGroup, resourceFile, currentUser).searchCreateComponent();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(data.getComponentName()).isEqualTo(componentName);
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

        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup, resourceFile, currentUser).createCostComponent();

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

        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup,resourceFile, currentUser).createPublishComponent();

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

        ScenarioResponse data = new DataCreationUtil(componentName, scenarioName, processGroup,resourceFile, currentUser).createCostPublishComponent();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(data.getScenarioState()).isEqualTo(ScenarioStateEnum.COST_COMPLETE.getState());
        softAssertions.assertThat(data.getPublished()).isTrue();

        softAssertions.assertAll();
    }
}
