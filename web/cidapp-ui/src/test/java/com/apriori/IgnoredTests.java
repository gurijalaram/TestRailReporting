package com.apriori;

import static com.apriori.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_LOCKED_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class IgnoredTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private AdvancedPage advancedPage;
    private CssComponent cssComponent = new CssComponent();
    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemC;
    private ComponentInfoBuilder cidComponentItemD;
    private SoftAssertions softAssertions = new SoftAssertions();

    public IgnoredTests() {
        super();
    }

    @Test
    @Tag(IGNORE)
    @Disabled("At the moment a new scenario name cannot be created from a public scenario")
    @TestRail(id = {5950, 5951, 5952})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;
        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, componentName + ".stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String testNewScenarioName = generateStringUtil.generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, testScenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED)).isEqualTo(true);

        evaluatePage.selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .highlightScenario(componentName, testScenarioName)
            .copyScenario()
            .enterScenarioName(testNewScenarioName)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(testNewScenarioName)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(IGNORE)
    @TestRail(id = {6210, 5435, 6735})
    @Description("Edit & publish Scenario A from the public workspace as Scenario B")
    public void testPublishLockedScenario() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioNameB = new GenerateStringUtil().generateScenarioName();
        String componentName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, ExplorePage.class);

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName, scenarioNameB)).isEqualTo(0);
    }

    @Test
    @TestRail(id = {6212})
    @Description("Load & publish a new single scenario which duplicates an existing locked public workspace scenario")
    public void testDuplicateLockedPublic() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String componentName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class);
        cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName, SCENARIO_LOCKED_EQ.getKey() + " true");

        evaluatePage = new EvaluatePage(driver).uploadComponentAndOpen(componentName, scenarioName2, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .changeName(scenarioName2)
            .publish(cidComponentItem, EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName2)).isEqualTo(true);
    }

    /*    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Disabled("Properties Dialogue not yet available")
    @Test
    @TestRail(id = {1261})
    @Description("Ensure that the Geometry tab section is expandable table of GCDs to third hierarchical level with total at GCD type level")
    public void geometryTest() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        settingsPage = new SettingsPage(driver);
        geometryPage = settingsPage.save(ExplorePage.class)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum)
            .costScenario(3)
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Surfaces", "Planar Faces", "PlanarFace:1");

        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Properties");
        assertThat(propertiesDialogPage.getProperties("Finished Area (mm2)"), containsString("85.62"));
    }*/
}