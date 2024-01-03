package com.apriori.cid.ui.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_LOCKED_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.IGNORE;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class IgnoredTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private CssComponent cssComponent = new CssComponent();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public IgnoredTests() {
        super();
    }

    @Test
    @Tag(IGNORE)
    @Disabled("At the moment a new scenario name cannot be created from a public scenario")
    @TestRail(id = {5950, 5951, 5952})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {
        String testNewScenarioName = generateStringUtil.generateScenarioName();

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.POWDER_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED)).isEqualTo(true);

        evaluatePage.selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(component.getComponentName())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
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
        String scenarioNameB = new GenerateStringUtil().generateScenarioName();

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.POWDER_METAL);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(component, ExplorePage.class);

        softAssertions.assertThat(explorePage.getListOfScenarios(component.getComponentName(), scenarioNameB)).isEqualTo(0);
    }

    @Test
    @TestRail(id = {6212})
    @Description("Load & publish a new single scenario which duplicates an existing locked public workspace scenario")
    public void testDuplicateLockedPublic() {
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.POWDER_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class);
        cssComponent.getComponentParts(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getComponentName(), SCENARIO_NAME_EQ.getKey() + component.getScenarioName(),
            SCENARIO_LOCKED_EQ.getKey() + " true");

        component.setScenarioName(scenarioName2);
        evaluatePage = new EvaluatePage(driver).uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .changeName(scenarioName2)
            .publish(component, EvaluatePage.class);

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