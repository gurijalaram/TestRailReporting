package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PublishAssembliesTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemC;
    private File subComponentA;
    private File subComponentB;
    private File assembly;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentsListPage componentsListPage;
    private ExplorePage explorePage;

    public PublishAssembliesTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10763", "10768"})
    @Description("Publish an assembly with no missing sub-components")
    public void shallowPublishAssemblyTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String subComponentAName = "titan battery release";
        String subComponentBName = "titan battery";
        String assemblyName = "titan battery ass";

        subComponentA = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, subComponentAName + ".SLDPRT");
        subComponentB = FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, subComponentBName + ".SLDPRT");
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(subComponentAName, scenarioName, subComponentA, currentUser);

        cidComponentItemB = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario()
            .publishScenario()
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(subComponentBName, scenarioName, subComponentB, currentUser);

        cidComponentItemC = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING)
            .costScenario(3)
            .publishScenario()
            .publish(cidComponentItemB, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(assemblyName, scenarioName, assembly, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .selectProcessGroup(ASSEMBLY)
            .costScenario()
            .publishScenario()
            .publish(cidComponentItemC, EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @TestRail(testCaseId = {"11813", "11814", "11808"})
    @Description("Validate public scenarios are overridden from publish modal")
    public void testOverridePublicScenarios() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        SoftAssertions softAssertions = new SoftAssertions();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(PIN + "," + scenarioName);

        softAssertions.assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EDIT)).isEqualTo(false);

        componentsListPage = componentsListPage.publishSubcomponent()
            .publish(ComponentsListPage.class)
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName + "", SMALL_RING + "," + scenarioName + "")
            .publishSubcomponent()
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsListPage.class);

        softAssertions.assertThat(componentsListPage.getListOfScenariosWithStatus(BIG_RING, scenarioName, ScenarioStateEnum.COST_UP_TO_DATE)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.getListOfScenariosWithStatus(SMALL_RING, scenarioName, ScenarioStateEnum.COST_UP_TO_DATE)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.getRowDetails(BIG_RING, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(SMALL_RING, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        explorePage = componentsListPage.closePanel()
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(assemblyName);

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }
}
