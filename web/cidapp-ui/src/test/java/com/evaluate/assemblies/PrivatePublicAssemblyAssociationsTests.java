package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.PreferencesEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivatePublicAssemblyAssociationsTests extends TestBase {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder cidComponentItemA;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemC;
    private ComponentInfoBuilder cidComponentItemD;
    private ComponentInfoBuilder cidComponentItemE;
    private ComponentInfoBuilder cidComponentItemF;
    private ComponentInfoBuilder cidComponentItemG;
    private ComponentInfoBuilder cidComponentItemH;
    private List<ComponentInfoBuilder> listOfSubcomponents;
    private List<ComponentInfoBuilder> listOfSubcomponentsB;

    private String bigRing;
    private String pin;
    private String smallRing;
    private String hingeAssembly;
    private ProcessGroupEnum assemblyProcessGroup;
    private ProcessGroupEnum subComponentProcessGroup;
    private String assemblyExtension;
    private String subComponentExtension;
    private String scenarioName;
    private String newScenarioName;
    private String newScenarioName2;
    private String newScenarioName3;
    private String newScenarioName4;
    private List<String> subComponentNames;
    private File resourceFile;
    private File resourceFile2;

    @Before
    public void testSetup() {
        hingeAssembly = "auto hinge assembly";
        assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        assemblyExtension = ".SLDASM";
        bigRing = "auto big ring";
        pin = "auto pin";
        smallRing = "auto small ring";
        subComponentExtension = ".SLDPRT";
        subComponentNames = Arrays.asList(bigRing, pin, smallRing);
        subComponentProcessGroup = ProcessGroupEnum.FORGING;
        resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, hingeAssembly + assemblyExtension);
        resourceFile2 = FileResourceUtil.getCloudFile(subComponentProcessGroup, bigRing + subComponentExtension);

        currentUser = UserUtil.getUser();

        scenarioName = new GenerateStringUtil().generateScenarioName();
        newScenarioName = new GenerateStringUtil().generateScenarioName();
        newScenarioName2 = new GenerateStringUtil().generateScenarioName();
        newScenarioName3 = new GenerateStringUtil().generateScenarioName();
        newScenarioName4 = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hingeAssembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
    }

    @After
    public void deleteScenarios() {
        if (currentUser != null) {
            userPreferencesUtil.resetSettings(currentUser);
        }

        listOfSubcomponents = Arrays.asList(cidComponentItemA, cidComponentItemC, cidComponentItemE,
            cidComponentItemG);

        listOfSubcomponents.forEach(subcomponent -> {
            if (subcomponent != null) {
                scenariosUtil.deleteScenario(subcomponent.getComponentIdentity(), subcomponent.getScenarioIdentity(), currentUser);
            }
        });

        listOfSubcomponentsB = Arrays.asList(cidComponentItemB, cidComponentItemD, cidComponentItemF);

        listOfSubcomponentsB.forEach(subcomponent -> {
            if (subcomponent != null) {
                scenariosUtil.deleteScenario(subcomponent.getComponentIdentity(), subcomponent.getScenarioIdentity(), currentUser);
            }
        });

        assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"11955", "11956"})
    @Description("Validate assembly associations takes preference for private sub-components")
    public void testDefaultAssemblyAssociationsPrivatePreference() {

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(bigRing + "," + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        componentsTreePage.selectTableView()
            .multiSelectSubcomponents(bigRing + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        componentsTablePage.closePanel()
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(bigRing + "," + scenarioName)
            .editScenario(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        componentsTreePage.selectTableView()
            .multiSelectSubcomponents(bigRing + "," + scenarioName)
            .deleteSubcomponent()
            .clickDelete(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.SCENARIO_TYPE);

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon(),
            StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11958", "6522"})
    @Description("Validate assembly association with different named sub-component when publishing from private workspace")
    public void testDefaultAssemblyAssociationsNewScenarioName() {

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(bigRing + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        componentsTablePage.openAssembly(bigRing, scenarioName)
            .publishScenario(PublishPage.class)
            .changeName(newScenarioName)
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, newScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(newScenarioName);

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"11960", "6600"})
    @Description("Validate a private sub component will take preference over a public iteration when editing a public assembly")
    public void testEditDefaultAssemblyAssociationsPrivatePreference() {

        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly).publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(bigRing + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        componentsTreePage.closePanel()
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11961"})
    @Description("Validate a new private sub component will take preference over a public iteration when editing a public assembly")
    public void testEditDefaultAssemblyAssociationsPrivateNewScenarioPreference() {
        final File resourceFile = FileResourceUtil.getCloudFile(subComponentProcessGroup, bigRing + subComponentExtension);

        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly).publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser)
            .uploadComponent(bigRing, newScenarioName, resourceFile, currentUser);

        evaluatePage = new EvaluatePage(driver).refresh()
            .navigateToScenario(componentAssembly);

        componentsTreePage = evaluatePage.openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(bigRing + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, bigRing + "," + pin + "," + smallRing)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        componentsTreePage.closePanel()
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21707", "21708", "21709", "21710"})
    @Description("Validate assembly association priority for Default strategy")
    public void testDefaultAssemblyAssociationsWorkflow() {

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, currentUser);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, currentUser);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21699", "21701", "21702", "21703"})
    @Description("Validate assembly association priority for Prefer Private strategy")
    public void testPreferPrivateAssemblyAssociationsWorkflow() {

        String asmStrategy = "PREFER_PRIVATE";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, currentUser);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, currentUser);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21700", "21704", "21705", "21706"})
    @Description("Validate assembly association priority for Prefer Public strategy")
    public void testPreferPublicAssemblyAssociationsWorkflow() {

        String asmStrategy = "PREFER_PUBLIC";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, currentUser);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemF)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }
}
