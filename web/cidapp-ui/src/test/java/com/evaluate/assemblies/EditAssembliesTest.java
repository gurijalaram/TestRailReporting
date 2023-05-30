package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.SetInputStatusPage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.PreviewPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EditAssembliesTest extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private EditScenarioStatusPage editScenarioStatusPage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private InfoPage infoPage;
    private ExplorePage explorePage;
    private EditComponentsPage editComponentsPage;
    private EditScenarioStatusPage editStatusPage;
    private PreviewPage previewPage;

    private SoftAssertions softAssertions = new SoftAssertions();
    private static ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder cidComponentItem;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ScenariosUtil scenariosUtil = new ScenariosUtil();

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10768"})
    @Description("Shallow Publish assembly and scenarios costed in CI Design")
    public void testShallowPublishCostedCID() {
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
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName, pin + "," + scenarioName, small_ring + "," + scenarioName)
            .publishSubcomponent()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10799", "6076", "6515"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testShallowEditCostedCID() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(assemblyName, scenarioName);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC)).isTrue();
        softAssertions.assertThat(evaluatePage.isAnnualVolumeInputEnabled()).isEqualTo(false);
        softAssertions.assertThat(evaluatePage.isAnnualYearsInputEnabled()).isEqualTo(false);

        evaluatePage.editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION)).isTrue();
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10801"})
    @Description("Retain the Status/Cost Maturity/Assignee/Lock during a Shallow Edit")
    public void testShallowEditRetainStatus() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isTrue();

        infoPage = evaluatePage.clickActions()
            .info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing QA notes");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10802", "10803", "10835", "6613"})
    @Description("Modify the Status/Cost Maturity/Lock after a Shallow Edit and ensure subcomponents are associated")
    public void testShallowEditModifyStatusCheckAssociationSmallSetSubcomponents() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isTrue();
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK)).isTrue();

        infoPage = evaluatePage.clickActions()
            .info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing QA notes");

        componentsTablePage = infoPage.cancel(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        subComponentNames.forEach(subcomponent -> assertThat(componentsTablePage.getListOfSubcomponents(), hasItem(subcomponent.toUpperCase())));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10804", "6733", "6594"})
    @Description("Shallow Edit keeps original assembly intact on Public Workspace")
    public void testShallowEditCheckDuplicate() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(2);

        explorePage.selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"10806", "10807", "10809", "6614"})
    @Description("Shallow Edited assemblies and scenarios can be published into Public Workspace and can also add notes and lock/unlock scenario")
    public void testShallowEditPublishPublicWorkspaceLockNotes() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .publishScenario(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC)).isTrue();

        evaluatePage.clickActions()
            .lock(EvaluatePage.class);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK)).isTrue();

        evaluatePage.clickActions()
            .unlock(EvaluatePage.class);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK)).isTrue();

        evaluatePage.clickActions()
            .info()
            .selectStatus("Analysis")
            .inputCostMaturity("Medium")
            .inputDescription("QA Modified Test Description")
            .inputNotes("Testing Modified QA notes")
            .submit(EvaluatePage.class);

        infoPage = evaluatePage.clickActions()
            .info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Analysis");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Medium");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Modified Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing Modified QA notes");

        softAssertions.assertAll();
    }

    @Ignore("A unique assembly is needed to do this and then some post steps to delete this unique assembly and subcomponents")
    @Test
    @TestRail(testCaseId = {"10836", "10811"})
    @Description("Shallow Edit an assembly with larger set of sub-components ")
    public void testUploadCostPublishAssemblyLargeSetSubcomponents() {
        final String assemblyName = "FUSELAGE_SUBASSEMBLY";
        final String assemblyExtension = ".CATProduct";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String subcomponent1 = "505-04426-001 1 1 ---";
        final String subcomponent2 = "550-05628-401 PRIMARY 1 ---";
        final String subcomponent3 = "550-05676-001 1 1 ---";
        final List<String> uploadedSubcomponents = Arrays.asList(subcomponent1, subcomponent2, subcomponent3);
        final String subComponentExtension = ".CATPart";
        final List<String> listOfSubComponents = Arrays.asList(subcomponent1, subcomponent2, subcomponent3, "MS14108-3 1 ---", "505-04596-001 1 1 ---", "550-05526-001 1 1 --A", "550-05629-401 PRIMARY 1 ---", "550-05673-401 PRIMARY 1 ---",
            "550-05676-002 1 1 ---", "550-05682-001 1 1 --A", "550-05683-001 1 1 ---", "550-05683-002 1 1 ---", "550-05689-001 1 1 ---", "550-05690-001 1 1 ---", "CCR244SS-3-2 1 ---",
            "MS14108-15 1 ---", "MS14218AD4-4 1 ---", "MS20392-1C15 1 ---", "MS20470AD4-5 1 ---", "MS20470AD4-6 1 ---", "MS21059L3 1 ---", "MS21059L08 1 ---", "MS24665-132 1 ---", "NAS1789-3 1 ---", "NAS9309M-6-04 1 ---");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            uploadedSubcomponents,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadAssembly(componentAssembly);
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .openComponents()
            .selectTableView();

        listOfSubComponents.forEach(subcomponent -> assertThat(componentsTablePage.getListOfSubcomponents(), hasItem(subcomponent.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"10810"})
    @Description("Shallow Edit assembly and scenarios that was uncosted in CI Design")
    public void testUploadUncostedAssemblySubcomponentOverride() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String bigRing = "big ring";
        final String pin = "Pin";
        final String smallRing = "small ring";
        final List<String> subComponentNames = Arrays.asList(bigRing, pin, smallRing);
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED), is(true));

        componentsTablePage = evaluatePage.openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getRowDetails(pin, scenarioName)).contains("circle-minus");
        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRing, scenarioName)).contains("circle-minus");
        softAssertions.assertThat(componentsTablePage.getRowDetails(smallRing, scenarioName)).contains("circle-minus");

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"10813", "10815", "11032"})
    @Description("Attempt to Shallow Edit over existing Private locked scenarios and renaming")
    public void testShallowEditPrivateLockedRename() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String bigRing = "big ring";
        final String pin = "Pin";
        final String smallRing = "small ring";
        final List<String> subComponentNames = Arrays.asList(bigRing, pin, smallRing);
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        String refreshMessage = "This assembly has uncosted changes. If you continue, these changes will be lost.";

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editComponentsPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickActions()
            .lock(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(assemblyName, scenarioName)
            .editScenario(EditComponentsPage.class);

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("A private scenario with this name already exists. The private scenario is locked and cannot be overridden, " +
            "please supply a different scenario name or cancel the operation.");

        evaluatePage = editComponentsPage.enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCurrentScenarioNameDisplayed(newScenarioName)).isTrue();

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_GERMANY)
            .clickRefresh(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getWarningMessageText()).contains(refreshMessage);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10814", "6596", "6046"})
    @Description("Shallow Edit over existing Private scenarios with override")
    public void testShallowEditPrivateOverride() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String bigRing = "big ring";
        final String pin = "Pin";
        final String smallRing = "small ring";
        final List<String> subComponentNames = Arrays.asList(bigRing, pin, smallRing);
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.shallowPublishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editComponentsPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(assemblyName, scenarioName)
            .editScenario(EditComponentsPage.class)
            .overrideScenarios();

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("A private scenario with this name already exists. Cancel this operation, or select an option below and continue.");

        editStatusPage = editComponentsPage.clickContinue(EditScenarioStatusPage.class);
        softAssertions.assertThat(editStatusPage.getEditScenarioMessage()).contains("Scenario was successfully edited, click here to open in the evaluate view.");

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2965")
    @TestRail(testCaseId = {"10895", "10897"})
    @Description("Edit public sub-component with Private counterpart (Override)")
    public void testEditPublicAndOverridePrivateSubcomponent() {
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

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(subComponentProcessGroup.getProcessGroup())
                .build()));

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName, SMALL_RING + "," + scenarioName)
            .editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTreePage = editScenarioStatusPage.close(ComponentsTreePage.class);

        subComponentNames.forEach(subcomponentName ->
            assertThat(componentsTreePage.getScenarioState(subcomponentName, scenarioName, currentUser, ScenarioStateEnum.COST_COMPLETE),
                is(ScenarioStateEnum.COST_COMPLETE.getState())));

        softAssertions.assertThat(componentsTreePage.getRowDetails(BIG_RING, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(SMALL_RING, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10896", "10898", "5619", "5428"})
    @Description("Edit public sub-component with Private counterpart (Override)")
    public void testEditPublicAndRenamePrivateSubcomponent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

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
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName, SMALL_RING + "," + scenarioName)
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTreePage = editScenarioStatusPage.close(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getListOfScenarios(BIG_RING, newScenarioName)).isEqualTo(1);
        softAssertions.assertThat(componentsTreePage.getListOfScenarios(SMALL_RING, newScenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "10899")
    @Description("Edit multiple public sub-components with mixture of Public & Private counterparts (Override)")
    public void testEditPublicSubcomponentsMixedWithPrivateThenOverride() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

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
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(FLANGE + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .multiSelectSubcomponents(BOLT + "," + scenarioName, NUT + "," + scenarioName)
            .editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTablePage = editScenarioStatusPage.close(ComponentsTablePage.class);

        subComponentNames.forEach(componentName ->
            assertThat(componentsTablePage.getScenarioState(componentName, scenarioName, currentUser, ScenarioStateEnum.COST_COMPLETE),
                is(ScenarioStateEnum.COST_COMPLETE.getState())));

        softAssertions.assertThat(componentsTablePage.getListOfScenarios(BOLT, scenarioName)).isEqualTo(1);
        softAssertions.assertThat(componentsTablePage.getListOfScenarios(NUT, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "10900")
    @Description("Edit multiple public sub-components with mixture of Public & Private counterparts (Rename)")
    public void testEditPublicSubcomponentsMixedWithPrivateThenRename() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

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
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(FLANGE + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTreePage.class)
            .multiSelectSubcomponents(BOLT + "," + scenarioName, NUT + "," + scenarioName)
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsTreePage = editScenarioStatusPage.close(ComponentsTreePage.class);

        softAssertions.assertThat(componentsTreePage.getListOfScenarios(BOLT, newScenarioName)).isEqualTo(1);
        softAssertions.assertThat(componentsTreePage.getListOfScenarios(NUT, newScenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11142")
    @Description("Validate an error message appears if any issues occur")
    public void testEditWithExistingPrivateScenarioName() {
        String preExistingScenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        ComponentInfoBuilder preExistingComponentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            preExistingScenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(preExistingComponentAssembly)
            .uploadAssembly(preExistingComponentAssembly);
        assemblyUtils.costSubComponents(preExistingComponentAssembly)
            .costAssembly(preExistingComponentAssembly);

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
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "")
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(preExistingScenarioName)
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioErrorMessage(), containsString("failed while attempting to create private scenario(s)."));
    }

    @Test
    @TestRail(testCaseId = "10810")
    @Description("Shallow Edit an assembly with uncosted scenarios")
    public void testShallowEditCostedAssemblyWithUncostedSubComponents() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

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
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isEqualTo(true);

        componentsTreePage = evaluatePage.openComponents()
            .checkSubcomponentState(componentAssembly, BIG_RING + "," + SMALL_RING + "," + PIN);

        subComponentNames.forEach(subcomponent ->
            assertThat(componentsTreePage.getRowDetails(subcomponent, scenarioName), hasItem(StatusIconEnum.PUBLIC.getStatusIcon())));

        subComponentNames.forEach(subcomponent ->
            softAssertions.assertThat(componentsTreePage.getListOfScenariosWithStatus(subcomponent, scenarioName, ScenarioStateEnum.NOT_COSTED)).isEqualTo(true));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12040", "11954", "6521", "10874", "11027"})
    @Description("Validate I can switch between public sub components when private iteration is deleted")
    public void testSwitchingPublicSubcomponentsWithDeletedPrivateIteration() {
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

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
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(BOLT + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, BOLT)
            .multiSelectSubcomponents(BOLT + "," + scenarioName)
            .setInputs()
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .applyAndCost(SetInputStatusPage.class)
            .close(ComponentsTablePage.class)
            .closePanel()
            .clickExplore()
            .getCssComponents(currentUser, "componentName[EQ], " + BOLT, "scenarioName[EQ], " + scenarioName, "scenarioState[EQ], " + ScenarioStateEnum.COST_COMPLETE,
                "scenarioPublished[EQ], false", "iteration[EQ], 1")
            .refresh()
            .selectFilter("Private")
            .clickSearch(BOLT)
            .multiSelectScenarios("" + BOLT + ", " + scenarioName + "")
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .navigateToScenario(componentAssembly)
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(BOLT, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2965")
    @TestRail(testCaseId = {"12037"})
    @Description("Validate I can switch between public sub components")
    public void testSwitchBetweenPublicSubcomponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String editedComponentScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

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

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(subComponentProcessGroup.getProcessGroup())
                .build()));

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly);

        double initialTotalCost = evaluatePage.getCostResults("Total Cost");
        double initialComponentsCost = evaluatePage.getCostResults("Components Cost");

        componentsTablePage = evaluatePage.openComponents()
            .selectTableView()
            .multiSelectSubcomponents(PIN + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, PIN)
            .multiSelectSubcomponents(PIN + "," + scenarioName)
            .setInputs()
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .applyAndCost(SetInputStatusPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, PIN)
            .multiSelectSubcomponents(PIN + "," + scenarioName)
            .publishSubcomponent()
            .changeName(editedComponentScenarioName)
            .clickContinue(PublishPage.class)
            .publish(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, PIN)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTablePage.getRowDetails(PIN, editedComponentScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        evaluatePage = componentsTablePage.closePanel()
            .clickCostButton()
            .confirmCost("Yes")
            .waitForCostLabelNotContain(NewCostingLabelEnum.COSTING_IN_PROGRESS, 2);

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(initialTotalCost).isGreaterThan(modifiedTotalCost);
        softAssertions.assertThat(initialComponentsCost).isGreaterThan(modifiedComponentsCost);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11133", "11141"})
    @Description("Validate the edit button will only be enabled when top level sub components are selected")
    public void testEditButtonSubAssembly() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String SUB_ASSEMBLY = "assy03A";
        final String TOP_LEVEL = "assy03";

        List<String> subAssemblyComponentNames = Arrays.asList("Part0005a", "Part0005b");
        List<String> subComponentNames = Arrays.asList("Part0004", "Part0003", "Part0002");

        final String componentExtension = ".ipt";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(
            SUB_ASSEMBLY, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subAssemblyComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        ComponentInfoBuilder componentAssembly2 = assemblyUtils.associateAssemblyAndSubComponents(TOP_LEVEL, assemblyExtension, ProcessGroupEnum.ASSEMBLY, subComponentNames,
            componentExtension, processGroupEnum, scenarioName, currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);
        assemblyUtils.costSubComponents(componentAssembly1).costAssembly(componentAssembly1);
        assemblyUtils.uploadSubComponents(componentAssembly2).uploadAssembly(componentAssembly2);
        assemblyUtils.costSubComponents(componentAssembly2).costAssembly(componentAssembly2);
        assemblyUtils.publishSubComponents(componentAssembly1).publishAssembly(componentAssembly1);
        assemblyUtils.publishSubComponents(componentAssembly2).publishAssembly(componentAssembly2);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly2)
            .openComponents()
            .expandSubAssembly(SUB_ASSEMBLY, scenarioName)
            .multiSelectSubcomponents("assy03A" + "," + scenarioName + "", "Part0005a" + "," + scenarioName + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EDIT)).isEqualTo(false);

        componentsTreePage.multiSelectSubcomponents("Part0005a" + "," + scenarioName);

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EDIT)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "6595")
    @Description("Validate that ONLY the selected assembly scenario is copied to the private workspace and not the components within it")
    public void testEditPublicAssembly() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

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
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .openScenario(assemblyName, scenarioName)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .setPagination();

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(2);
        softAssertions.assertThat(explorePage.getListOfScenarios(FLANGE, scenarioName)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(NUT, scenarioName)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(BOLT, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6601", "6602", "11869", "12022", "12023", "6522"})
    @Description("Validate user can open a public component from a private workspace")
    public void testOpeningPublicComponentFromPrivateWorkspace() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

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
        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isEqualTo(true);

        componentsTreePage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails("PIN", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        previewPage = componentsTreePage.clickScenarioCheckbox(PIN)
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.isPreviewPanelDisplayed()).isTrue();

        componentsTreePage = previewPage.closePreviewPanelOnComponentsPage();

        softAssertions.assertThat(componentsTreePage.isPreviewPanelDisplayed()).isFalse();

        evaluatePage = componentsTreePage.openAssembly(PIN, scenarioName);

        softAssertions.assertThat(evaluatePage.getTabTitle()).contains("PIN");
        softAssertions.assertThat(evaluatePage.getTabCount()).isEqualTo(2);

        evaluatePage.editScenario(EditScenarioStatusPage.class)
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);

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
    @TestRail(testCaseId = {"11961", "11956"})
    @Description("Validate a new private sub component will take preference over a public iteration when editing a public assembly")
    public void testEditPublicAssemblyAssociationsPrivateNewScenarioPreferenceAndDelete() {
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

        componentsTreePage.selectTableView()
            .multiSelectSubcomponents(big_ring + "," + scenarioName)
            .deleteSubcomponent()
            .clickDelete(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .clickRefresh(EvaluatePage.class)
            .clickCostButton()
            .confirmCost("Yes")
            .openComponents()
            .selectTableView()
            .addColumn(ColumnsEnum.SCENARIO_TYPE);

        softAssertions.assertThat(componentsTablePage.getRowDetails(big_ring, scenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon(),
            StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10843"})
    @Description("Validate assembly explorer table updates when sub-components changed")
    public void testAssemblyExplorerTableUpdates() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

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
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        componentsTreePage.multiSelectSubcomponents(PIN + "," + scenarioName)
            .setInputs()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .enterAnnualVolume("1234")
            .clickApplyAndCost(SetInputStatusPage.class)
            .close(ComponentsTreePage.class);

        scenariosUtil.getScenarioCompleted(componentAssembly.getSubComponents().get(1));
        componentsTreePage = componentsTreePage.closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(PIN, scenarioName)).as("Verify details updated")
            .contains(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory(), "1,234");

        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        evaluatePage = componentsTreePage.closePanel()
            .clickRefresh(EvaluatePage.class)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .clickHere()
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_EDIT_ACTION, 2);

        evaluatePage.enterAnnualVolume("7777")
            .goToAdvancedTab()
            .enterBatchSize("612")
            .goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2);

        componentsTreePage = evaluatePage.clickExplore()
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(BIG_RING, scenarioName)).as("Verify details updated")
            .contains("7,777", "612");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10860", "21552"})
    @Description("Validate 'missing' scenario created if sub-component deleted'")
    public void testMissingSubComponentOnDeletion() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        softAssertions = new SoftAssertions();

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

        ComponentInfoBuilder smallRing = assemblyUtils.getSubComponent(componentAssembly, SMALL_RING);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(SMALL_RING + "," + scenarioName)
            .deleteSubcomponent()
            .clickDelete(ComponentsTreePage.class);

        scenariosUtil.checkComponentDeleted(smallRing.getComponentIdentity(), smallRing.getScenarioIdentity(), currentUser);

        componentsTreePage = componentsTreePage.closePanel().clickRefresh(EvaluatePage.class).openComponents();

        softAssertions.assertThat(componentsTreePage.isTextDecorationStruckOut(SMALL_RING)).as("Verify 'missing' Small Ring Struck Out").isTrue();
        softAssertions.assertThat(componentsTreePage.getRowDetails(SMALL_RING, scenarioName))
            .as("Verify that 'missing' Small Ring is CAD DIsconnected").contains(StatusIconEnum.DISCONNECTED.getStatusIcon());

        softAssertions.assertAll();
    }
}
