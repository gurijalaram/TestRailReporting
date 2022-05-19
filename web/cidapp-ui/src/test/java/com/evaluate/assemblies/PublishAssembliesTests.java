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
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
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
            .publish(cidComponentItem, currentUser, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(subComponentBName, scenarioName, subComponentB, currentUser);

        cidComponentItemC = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING)
            .costScenario(3)
            .publishScenario()
            .publish(cidComponentItemB, currentUser, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(assemblyName, scenarioName, assembly, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .selectProcessGroup(ASSEMBLY)
            .costScenario()
            .publishScenario()
            .publish(cidComponentItemC, currentUser, EvaluatePage.class);

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
            .multiSelectSubcomponents(FLANGE + "," + scenarioName)
            .checkSubcomponentState(componentAssembly, FLANGE)
            .publishSubcomponent()
            .publish(ComponentsListPage.class)
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "")
            .publishSubcomponent();

        assertThat(publishPage.getConflictMessage(), is(message));
    }
}
