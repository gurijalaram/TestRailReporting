package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
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
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AssemblyAssociations extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;

    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder cidComponentItem;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();

    @Test
    @TestRail(testCaseId = {"11955", "11956", "11957", "6522"})
    @Description("Validate assembly associations takes preference for private sub-components")
    public void testPrivateAssemblyAssociationsPrivatePreference() {
        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".SLDASM";
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";
        final String subComponentExtension = ".SLDPRT";
        final List<String> subComponentNames = Arrays.asList(big_ring, pin, small_ring);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hinge_assembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        componentsTablePage.closePanel()
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(big_ring + "," + scenarioName)
            .editScenario(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName)
            .deleteSubcomponent()
            .clickDelete(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        componentsTablePage.openAssembly(big_ring, scenarioName)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .selectFilter("Private")
            .openScenario(big_ring, newScenarioName)
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, newScenarioName)).contains(CostingIconEnum.COSTED.getIcon(),
            StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(newScenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11958"})
    @Description("Validate assembly association with different named sub-component when publishing from private workspace")
    public void testPrivateAssemblyAssociationsNewScenarioName() {
        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".SLDASM";
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";
        final String subComponentExtension = ".SLDPRT";
        final List<String> subComponentNames = Arrays.asList(big_ring, pin, small_ring);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hinge_assembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        componentsTablePage.openAssembly(big_ring, scenarioName)
            .publishScenario(PublishPage.class)
            .changeName(newScenarioName)
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, newScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(newScenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11960"})
    @Description("Validate a private sub component will take preference over a public iteration when editing a public assembly")
    public void testEditPublicAssemblyAssociationsPrivatePreference() {
        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".SLDASM";
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";
        final String subComponentExtension = ".SLDPRT";
        final List<String> subComponentNames = Arrays.asList(big_ring, pin, small_ring);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hinge_assembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly).publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        componentsTreePage.closePanel()
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11961"})
    @Description("Validate a new private sub component will take preference over a public iteration when editing a public assembly")
    public void testEditPublicAssemblyAssociationsPrivateNewScenarioPreference() {
        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".SLDASM";
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";
        final String subComponentExtension = ".SLDPRT";
        final List<String> subComponentNames = Arrays.asList(big_ring, pin, small_ring);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final File resourceFile = FileResourceUtil.getCloudFile(subComponentProcessGroup, big_ring + subComponentExtension);

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hinge_assembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly).publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(big_ring, newScenarioName, resourceFile, currentUser);

        evaluatePage = new EvaluatePage(driver).refresh()
            .navigateToScenario(componentAssembly);

        componentsTreePage = evaluatePage.openComponents();

        componentsTablePage = componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        componentsTreePage.closePanel()
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        componentsTablePage.selectTreeView();

        softAssertions.assertThat(componentsTreePage.getSubcomponentScenarioName(big_ring)).contains(scenarioName);

        softAssertions.assertAll();
    }
}
