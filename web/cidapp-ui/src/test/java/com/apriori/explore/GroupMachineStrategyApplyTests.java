package com.apriori.explore;

import static com.apriori.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupMachineStrategyApplyTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private File resourceFile2;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();
    private ComponentBasicPage componentBasicPage;

    @Test
    @TestRail(id = {15433, 14939})
    @Description("Explore page - Machinable PG can be selected and part can be costed with \"Do not machine this part\" checked")
    public void testMachiningStrategyOptionFromExplorePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "big ring";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName2 = "small ring";
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".SLDPRT");
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);
        cidComponentItemB = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), scenarioName2));

        componentBasicPage = new ExplorePage(driver).refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class);

        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxDisplayed()).isEqualTo(false);

        componentBasicPage.selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING);

        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxDisplayed()).isEqualTo(true);
        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxSelected()).isEqualTo(false);

        explorePage = componentBasicPage.selectMachineOptionCheckBox()
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_INCOMPLETE)).hasSizeGreaterThan(0));

        componentBasicPage = explorePage.refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class);

        softAssertions.assertThat(componentBasicPage.isMachineOptionsCheckBoxSelected()).isEqualTo(true);

        explorePage = componentBasicPage.selectMachineOptionCheckBox()
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_COMPLETE)).hasSizeGreaterThan(0));

        softAssertions.assertAll();
    }
}
