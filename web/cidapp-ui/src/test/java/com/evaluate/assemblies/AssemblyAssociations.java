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
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblyAssociations extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
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

    @After
    public void deleteScenarios() {
        if (currentUser != null) {
            assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
            componentAssembly = null;
        }
        if (cidComponentItemA != null) {
            scenariosUtil.deleteScenario(cidComponentItemA.getComponentIdentity(), cidComponentItemA.getScenarioIdentity(), currentUser);
            cidComponentItemA = null;
        }
        if (cidComponentItemB != null) {
            scenariosUtil.deleteScenario(cidComponentItemB.getComponentIdentity(), cidComponentItemB.getScenarioIdentity(), currentUser);
            cidComponentItemB = null;
        }
        if (cidComponentItemC != null) {
            scenariosUtil.deleteScenario(cidComponentItemC.getComponentIdentity(), cidComponentItemC.getScenarioIdentity(), currentUser);
            cidComponentItemC = null;
        }
        if (cidComponentItemD != null) {
            scenariosUtil.deleteScenario(cidComponentItemD.getComponentIdentity(), cidComponentItemD.getScenarioIdentity(), currentUser);
            cidComponentItemD = null;
        }
        if (cidComponentItemE != null) {
            scenariosUtil.deleteScenario(cidComponentItemE.getComponentIdentity(), cidComponentItemE.getScenarioIdentity(), currentUser);
            cidComponentItemE = null;
        }
        if (cidComponentItemF != null) {
            scenariosUtil.deleteScenario(cidComponentItemF.getComponentIdentity(), cidComponentItemF.getScenarioIdentity(), currentUser);
            cidComponentItemF = null;
        }
        if (cidComponentItemG != null) {
            scenariosUtil.deleteScenario(cidComponentItemG.getComponentIdentity(), cidComponentItemG.getScenarioIdentity(), currentUser);
            cidComponentItemG = null;
        }
        if (cidComponentItemH != null) {
            scenariosUtil.deleteScenario(cidComponentItemH.getComponentIdentity(), cidComponentItemH.getScenarioIdentity(), currentUser);
            cidComponentItemH = null;
        }
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"11955", "11956", "11957", "6522", "6605"})
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

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
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

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
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
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
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
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"11960", "6600"})
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

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
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

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
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

        cidComponentItemA = loginPage.login(currentUser)
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

    @Test
    @TestRail(testCaseId = {"21669", "21670"})
    @Description("Validate, with Prefer Maturity strategy, private sub-components with same scenario name are associated to assembly")
    public void testMaturityPresetPrivateWithSameNameAndMissing() {
        final String fuse_block_asm = "Auto_Fuse_Block_Asm";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".CATProduct";
        final String conductor = "Auto_Conductor";
        final String housing = "Auto_Fuse_Connector_Housing";
        final String subComponentExtension = ".CATPart";
        final List<String> subComponentNames = Arrays.asList(housing);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final File resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, fuse_block_asm + assemblyExtension);

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        String asmStrategy = "PREFER_HIGH_MATURITY";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            fuse_block_asm,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser)
            .uploadComponent(fuse_block_asm, newScenarioName, resourceFile, currentUser);

        componentsTreePage = new EvaluatePage(driver).refresh()
            .navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.SCENARIO_TYPE)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());

        componentsTreePage.closePanel()
            .navigateToScenario(cidComponentItemA)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, newScenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21671", "21687", "21688"})
    @Description("Validate, with Prefer Maturity strategy, private sub-components with low and medium maturity")
    public void testMaturityPresetPrivateLowAndMedium() {
        final String fuse_block_asm = "Auto_Fuse_Block_Asm";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".CATProduct";
        final String conductor = "Auto_Conductor";
        final String housing = "Auto_Fuse_Connector_Housing";
        final String subComponentExtension = ".CATPart";
        final List<String> subComponentNames = Arrays.asList(conductor, housing);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final File resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, fuse_block_asm + assemblyExtension);
        final File resourceFile2 = FileResourceUtil.getCloudFile(subComponentProcessGroup, conductor + subComponentExtension);

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName2 = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName3 = new GenerateStringUtil().generateScenarioName();

        String asmStrategy = "PREFER_HIGH_MATURITY";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            fuse_block_asm,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios(conductor + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("Low")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .multiSelectScenarios(housing + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("Low")
            .submit(ExplorePage.class)
            .addColumn(ColumnsEnum.COST_MATURITY);

        softAssertions.assertThat(explorePage.getRowDetails(conductor, scenarioName)).contains("Low");
        softAssertions.assertThat(explorePage.getRowDetails(housing, scenarioName)).contains("Low");

        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuse_block_asm, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY)
            .addColumn(ColumnsEnum.SCENARIO_TYPE)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains("Low");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("Low");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("Low");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemC = new ExplorePage(driver).uploadComponent(conductor, newScenarioName2, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("Low");

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21689", "21690", "21691"})
    @Description("Validate, with Prefer Maturity strategy, private sub-components with high maturity and cost complete status")
    public void testMaturityPresetPrivateHighComplete() {
        final String fuse_block_asm = "Auto_Fuse_Block_Asm";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".CATProduct";
        final String conductor = "Auto_Conductor";
        final String housing = "Auto_Fuse_Connector_Housing";
        final String subComponentExtension = ".CATPart";
        final List<String> subComponentNames = Arrays.asList(conductor, housing);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final File resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, fuse_block_asm + assemblyExtension);
        final File resourceFile2 = FileResourceUtil.getCloudFile(subComponentProcessGroup, conductor + subComponentExtension);

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName2 = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName3 = new GenerateStringUtil().generateScenarioName();

        String asmStrategy = "PREFER_HIGH_MATURITY";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            fuse_block_asm,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios(conductor + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .multiSelectScenarios(housing + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .addColumn(ColumnsEnum.COST_MATURITY);

        softAssertions.assertThat(explorePage.getRowDetails(conductor, scenarioName)).contains("High");
        softAssertions.assertThat(explorePage.getRowDetails(housing, scenarioName)).contains("High");

        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuse_block_asm, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY)
            .addColumn(ColumnsEnum.SCENARIO_TYPE)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains("High");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("High");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .selectStatus("Complete")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents()
            .addColumn(ColumnsEnum.STATUS);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("High");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemC = new ExplorePage(driver).uploadComponent(conductor, newScenarioName2, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .selectStatus("Complete")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains("High");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("High");

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21692", "21693", "21694", "21695"})
    @Description("Validate, with Prefer Maturity strategy, public sub-components with same scenario name and low then medium maturity")
    public void testMaturityPresetPublicWithSameNameLowAndMedium() {
        final String fuse_block_asm = "Auto_Fuse_Block_Asm";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".CATProduct";
        final String conductor = "Auto_Conductor";
        final String housing = "Auto_Fuse_Connector_Housing";
        final String subComponentExtension = ".CATPart";
        final List<String> subComponentNames = Arrays.asList(conductor, housing);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final File resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, fuse_block_asm + assemblyExtension);
        final File resourceFile2 = FileResourceUtil.getCloudFile(subComponentProcessGroup, conductor + subComponentExtension);

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName2 = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName3 = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName4 = new GenerateStringUtil().generateScenarioName();

        String asmStrategy = "PREFER_HIGH_MATURITY";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            fuse_block_asm,
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
        explorePage = loginPage.login(currentUser);

        cidComponentItemH = new ExplorePage(driver).uploadComponent(conductor, scenarioName, resourceFile2, currentUser);
        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuse_block_asm, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Low")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Low");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTreePage.closePanel()
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemD = new ExplorePage(driver).uploadComponent(conductor, newScenarioName3, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemD)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTreePage.closePanel()
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemF = new ExplorePage(driver).uploadComponent(conductor, newScenarioName4, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemF)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName4)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName4)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21696", "21697", "21698"})
    @Description("Validate, with Prefer Maturity strategy, public sub-components with high maturity and cost complete status")
    public void testMaturityPresetPublicHighComplete() {
        final String fuse_block_asm = "Auto_Fuse_Block_Asm";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".CATProduct";
        final String conductor = "Auto_Conductor";
        final String housing = "Auto_Fuse_Connector_Housing";
        final String subComponentExtension = ".CATPart";
        final List<String> subComponentNames = Arrays.asList(conductor, housing);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final File resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, fuse_block_asm + assemblyExtension);
        final File resourceFile2 = FileResourceUtil.getCloudFile(subComponentProcessGroup, conductor + subComponentExtension);

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName2 = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName3 = new GenerateStringUtil().generateScenarioName();

        String asmStrategy = "PREFER_HIGH_MATURITY";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            fuse_block_asm,
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
        explorePage = loginPage.login(currentUser)
            .selectFilter("Public")
            .multiSelectScenarios(conductor + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .selectFilter("Public")
            .multiSelectScenarios(housing + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .selectFilter("Private")
            .multiSelectScenarios(fuse_block_asm + "," + scenarioName)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuse_block_asm, scenarioName, resourceFile, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .selectStatus("Complete")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY)
            .addColumn(ColumnsEnum.STATUS);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTreePage.closePanel()
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemD = new ExplorePage(driver).uploadComponent(conductor, newScenarioName3, resourceFile2, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemD)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .selectStatus("Complete")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(fuse_block_asm, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains("High");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }
}
