package com.evaluate;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_CHINA;
import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.utils.enums.ProcessGroupEnum.FORGING;
import static com.apriori.utils.enums.ProcessGroupEnum.POWDER_METAL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.css.entity.response.Item;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishExistingCostedTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private Item cssItem;

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @Issue("MIC-3108")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6209", "5427"})
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    public void testPublishExistingCostedScenario() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "testpart-4";

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        String filterName = generateStringUtil.generateFilterName();

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cssItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cssItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(cssItem, currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(componentName)
            .openScenario(componentName, scenarioName)
            .editScenario()
            .selectDigitalFactory(APRIORI_CHINA)
            .costScenario()
            .publishScenario()
            .override()
            .continues(PublishPage.class)
            .publish(cssItem, currentUser, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", componentName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(greaterThan(0)));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Processing state")
    @TestRail(testCaseId = {"6210", "5435"})
    @Description("Edit & publish Scenario A from the public workspace as Scenario B")
    public void testPublishLockedScenario() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioNameB = new GenerateStringUtil().generateScenarioName();
        String componentName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cssItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cssItem)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(cssItem, currentUser, EvaluatePage.class)
            .editScenario()
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .publishScenario()
            .override()
            .continues(PublishPage.class)
            .publish(cssItem, currentUser, EvaluatePage.class)
            .lock(EvaluatePage.class)
            .publishScenario()
            .publish(cssItem, currentUser, ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioNameB), is(greaterThan(0)));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"6211"})
    @Description("Load & publish a new single scenario which duplicates an existing unlocked public workspace scenario")
    public void testDuplicatePublic() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "PowderMetalShaft";
        final ProcessGroupEnum processGroupEnum = POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cssItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cssItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(cssItem, currentUser, EvaluatePage.class)
            .uploadComponentAndSubmit(scenarioName, FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp"), EvaluatePage.class)
            .selectProcessGroup(FORGING)
            .costScenario()
            .publishScenario()
            .override()
            .continues(PublishPage.class)
            .publish(cssItem, currentUser, EvaluatePage.class);

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Compaction Pressing / Furnace Sintering"));
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
        cssItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cssItem)
            .selectProcessGroup(POWDER_METAL)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(cssItem, currentUser, ExplorePage.class)
            .lock(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName2, resourceFile, currentUser)
            .selectProcessGroup(FORGING)
            .costScenario()
            .publishScenario()
            .changeName(scenarioName2)
            .publish(cssItem, currentUser, EvaluatePage.class);

        assertThat(evaluatePage.isCurrentScenarioNameDisplayed(scenarioName2), is(true));
    }
}