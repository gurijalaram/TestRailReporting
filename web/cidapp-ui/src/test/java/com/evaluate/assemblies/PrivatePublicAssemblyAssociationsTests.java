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
import com.apriori.utils.enums.CostingIconEnum;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivatePublicAssemblyAssociationsTests extends TestBase {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder cidComponentItemA;
    private List<ComponentInfoBuilder> listOfSubcomponents;

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
    private List<String> subComponentNames;

    @Before
    public void testSetup() {
        hingeAssembly = "Hinge assembly";
        assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        assemblyExtension = ".SLDASM";
        bigRing = "big ring";
        pin = "Pin";
        smallRing = "small ring";
        subComponentExtension = ".SLDPRT";
        subComponentNames = Arrays.asList(bigRing, pin, smallRing);
        subComponentProcessGroup = ProcessGroupEnum.FORGING;

        currentUser = UserUtil.getUser();
        scenarioName = new GenerateStringUtil().generateScenarioName();
        newScenarioName = new GenerateStringUtil().generateScenarioName();

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

            assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
        }

        listOfSubcomponents = Collections.singletonList(cidComponentItemA);

        listOfSubcomponents.forEach(subcomponent -> {
            if (subcomponent != null) {
                scenariosUtil.deleteScenario(subcomponent.getComponentIdentity(), subcomponent.getScenarioIdentity(), currentUser);
            }
        });
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"11955", "11956", "11957", "6522", "6605"})
    @Description("Validate assembly associations takes preference for private sub-components")
    public void testPrivateAssemblyAssociationsPrivatePreference() {

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
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(scenarioName);

        componentsTablePage.openAssembly(bigRing, scenarioName)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .selectFilter("Private")
            .openScenario(bigRing, newScenarioName)
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, newScenarioName)).contains(CostingIconEnum.COSTED.getIcon(),
            StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(bigRing)).contains(newScenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11958"})
    @Description("Validate assembly association with different named sub-component when publishing from private workspace")
    public void testPrivateAssemblyAssociationsNewScenarioName() {

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
    public void testEditPublicAssemblyAssociationsPrivatePreference() {

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
    public void testEditPublicAssemblyAssociationsPrivateNewScenarioPreference() {
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
}
