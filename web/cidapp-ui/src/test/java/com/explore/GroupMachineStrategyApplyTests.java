package com.explore;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupMachineStrategyApplyTests extends TestBase {

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
    @TestRail(testCaseId = {"15433", "14939"})
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
