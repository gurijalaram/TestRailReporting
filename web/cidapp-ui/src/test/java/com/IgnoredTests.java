package com;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.utils.enums.ProcessGroupEnum.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.inputs.AdvancedPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class IgnoredTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private AdvancedPage advancedPage;
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

    @Category(IgnoreTests.class)
    @Test
    @Ignore("At the moment a new scenario name cannot be created from a public scenario")
    @TestRail(testCaseId = {"5950", "5951", "5952"})
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
                .selectMaterial("F-0005")
                .submit(EvaluatePage.class)
                .costScenario()
                .publishScenario(PublishPage.class)
                .publish(cidComponentItem, EvaluatePage.class)
                .clickExplore()
                .selectFilter("Recent")
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
                .clickSearch(componentName)
                .highlightScenario(componentName, testScenarioName)
                .createScenario()
                .enterScenarioName(testNewScenarioName)
                .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(testNewScenarioName)).isEqualTo(true);

        softAssertions.assertAll();
    }
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6210", "5435", "6735"})
    @Description("Edit & publish Scenario A from the public workspace as Scenario B")
    public void testPublishLockedScenario() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioNameB = new GenerateStringUtil().generateScenarioName();
        String componentName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
                .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
                .openMaterialSelectorTable()
                .selectMaterial("F-0005")
                .submit(EvaluatePage.class)
                .costScenario()
                .publishScenario(PublishPage.class)
                .publish(cidComponentItem, EvaluatePage.class)
                .editScenario(EditScenarioStatusPage.class)
                .close(EvaluatePage.class)
                .selectProcessGroup(processGroupEnum)
                .selectDigitalFactory(APRIORI_USA)
                .publishScenario(PublishPage.class)
                .override()
                .clickContinue(PublishPage.class)
                .publish(cidComponentItem, EvaluatePage.class)
                .clickActions()
                .lock(EvaluatePage.class)
                .publishScenario(PublishPage.class)
                .publish(cidComponentItem, ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioNameB), is(greaterThan(0)));
    }
    @Test
    @Category(IgnoreTests.class)
    @Ignore("Processing state")
    @TestRail(testCaseId = {"6212"})
    @Description("Load & publish a new single scenario which duplicates an existing locked public workspace scenario")
    public void testDuplicateLockedPublic() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String componentName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
                .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
                .selectProcessGroup(POWDER_METAL)
                .openMaterialSelectorTable()
                .selectMaterial("F-0005")
                .submit(EvaluatePage.class)
                .costScenario()
                .publishScenario(PublishPage.class)
                .publish(cidComponentItem, ExplorePage.class)
                .clickActions()
                .lock(ExplorePage.class)
                .uploadComponentAndOpen(componentName, scenarioName2, resourceFile, currentUser)
                .selectProcessGroup(FORGING)
                .costScenario()
                .publishScenario(PublishPage.class)
                .changeName(scenarioName2)
                .publish(cidComponentItem, EvaluatePage.class);

        assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName2), is(true));
    }
    @Test
    @Category(IgnoreTests.class)
    @Ignore("Secondary Processes has not went in yet")
    @TestRail(testCaseId = {"5120", "5121", "5123"})
    @Description("Validate zero count when no secondary process is selected and Test secondary process xray")
    public void secondaryProcessXray() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulding";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .selectMaterial("ABS, 10% Glass")
                .submit(EvaluatePage.class)
                .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).isEmpty();

        evaluatePage = advancedPage.openSecondaryProcesses()
                .goToOtherSecProcessesTab()
                .expandSecondaryProcessTree("Testing and Inspection")
                .selectSecondaryProcess("Xray Inspection")
                .submit(EvaluateToolbar.class)
                .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Xray Inspection");

        advancedPage = evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("Xray", " Packaging");

        softAssertions.assertAll();
    }
}