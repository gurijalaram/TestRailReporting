package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.navtoolbars.PublishScenarioPage;
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
import org.hamcrest.core.Is;
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
    private PublishPage publishPage;
    private ComponentsListPage componentsListPage;

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
    @TestRail(testCaseId = {"11812"})
    @Description("Verify publish scenario modal appears when publish button is clicked")
    public void testIncludeSubcomponentsAndCost() {
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String message = "Public scenarios will be created for each scenario in your selection." +
            " If you wish to retain existing public scenarios, change the scenario name, otherwise they will be overridden.";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        publishPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(FLANGE + ", " + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsListPage.class)
            .checkSubcomponentState(componentAssembly, FLANGE)
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "")
            .publishSubcomponent();

        assertThat(publishPage.getConflictMessage(), is(equalTo(message)));
    }

    @Test
    @TestRail(testCaseId = "11811")
    @Description("Publish button becomes unavailable when public sub-component selected alongside private sub-component(s)")
    public void testPublishButtonAvailability() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(FLANGE + ", " + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsListPage.class)
            .checkSubcomponentState(componentAssembly, FLANGE)
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "");

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH), is(true));

        componentsListPage = componentsListPage.multiSelectSubcomponents(FLANGE + ", " + scenarioName);

        assertThat(componentsListPage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH), is(false));
    }

    @Test
    @TestRail(testCaseId = "11828")
    @Description("Validate an error message appears if any issues occur")
    public void testPublishWithExistingScenarioName() {
        String preExistingScenarioName = new GenerateStringUtil().generateScenarioName();
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

        String message = "A public scenario with this name already exists. Cancel this operation, or select an option below and continue.";

        ComponentInfoBuilder preExistingComponentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            preExistingScenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(preExistingComponentAssembly)
            .uploadAssembly(preExistingComponentAssembly);
        assemblyUtils.costSubComponents(preExistingComponentAssembly)
            .costAssembly(preExistingComponentAssembly);
        assemblyUtils.publishSubComponents(preExistingComponentAssembly)
            .publishAssembly(preExistingComponentAssembly);

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

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName + "", SMALL_RING + "," + scenarioName + "")
            .publishSubcomponent()
            .changeName(preExistingScenarioName)
            .continues(PublishPage.class)
            .publish(PublishPage.class)
            .close();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(componentsListPage.getListOfScenariosWithStatus(BIG_RING, scenarioName, ScenarioStateEnum.PROCESSING_FAILED)).isEqualTo(true);
        softAssertions.assertThat(componentsListPage.getListOfScenariosWithStatus(SMALL_RING, scenarioName, ScenarioStateEnum.PROCESSING_FAILED)).isEqualTo(true);
    }
}
