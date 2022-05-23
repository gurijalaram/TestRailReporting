package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.util.Arrays;
import java.util.List;

public class EditAssembliesTest extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InfoPage infoPage;
    private ComponentsListPage componentsListPage;
    private ExplorePage explorePage;
    private EditComponentsPage editComponentsPage;

    private SoftAssertions softAssertions = new SoftAssertions();

    final AssemblyUtils assemblyUtils = new AssemblyUtils();

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10768"})
    @Description("Shallow Publish assembly and scenarios costed in CI Design")
    public void testUploadPublishCostedAssemblyComponents() {
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
            .multiSelectSubcomponents(big_ring + "," + scenarioName, pin + "," + scenarioName, small_ring + "," + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsListPage.class)
            .checkSubcomponentState(componentAssembly, big_ring + "," + pin + "," + small_ring)
            .closePanel()
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10799"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testUploadCostPublishAssemblyAndEditAddNotes() {
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

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC)).isTrue();

        evaluatePage.editScenario()
            .close(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION)).isTrue();
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10804"})
    @Description("Shallow Edit keeps original assembly intact on Public Workspace")
    public void testUploadEditAssemblyDuplicate() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.uploadAssembly(ComponentInfoBuilder.builder()
            .componentName(assemblyName)
            .extension(assemblyExtension)
            .scenarioName(scenarioName)
            .processGroup(assemblyProcessGroup)
            .user(currentUser)
            .build());

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .editScenario()
            .close(EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(2);

        explorePage.selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(2);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10801", "10802", "10803", "10806", "10807", "10809", "10835"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testUploadCostPublishAssemblyAndOverrideLockNotes() {
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

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class)
            .editScenario()
            .close(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(EvaluatePage.class);

        infoPage = evaluatePage.info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing QA notes");

        evaluatePage = infoPage.cancel(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC)).isTrue();

        explorePage = evaluatePage.clickExplore()
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isGreaterThanOrEqualTo(1);

        evaluatePage = explorePage.navigateToScenario(componentAssembly)
            .lock(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK)).isTrue();

        evaluatePage.unlock(EvaluatePage.class);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK)).isTrue();

        evaluatePage.clickExplore()
            .selectFilter("Private")
            .openScenario(assemblyName, scenarioName)
            .openComponents();

        subComponentNames.forEach(subcomponent -> assertThat(componentsListPage.getListOfSubcomponents(), hasItem(subcomponent.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"10802"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testUploadCostPublishAssemblyAndModifyNotes() {
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

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class)
            .editScenario()
            .close(EvaluatePage.class)
            .info()
            .selectStatus("Analysis")
            .inputCostMaturity("Medium")
            .inputDescription("QA Modified Test Description")
            .inputNotes("Testing Modified QA notes")
            .submit(EvaluatePage.class);

        infoPage = evaluatePage.info();
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Analysis");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Medium");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Modified Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing Modified QA notes");
    }

    @Test
    @TestRail(testCaseId = {"10836", "10811"})
    @Description("Shallow Edit assembly and scenarios that was costed in CI Design")
    public void testUploadCostPublishAssemblyLargeSubcomponentSet() {
        final String assemblyName = "FUSELAGE_SUBASSEMBLY";
        final String assemblyExtension = ".CATProduct";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String subcomponent1 = "505-04426-001 1 1 ---";
        final String subcomponent2 = "550-05628-401 PRIMARY 1 ---";
        final String subcomponent3 = "550-05676-001 1 1 ---";
        final List<String> uploadedSubcomponents = Arrays.asList(subcomponent1, subcomponent2, subcomponent3);
        final String subComponentExtension = ".CATPart";
        final List<String> allSubComponents = Arrays.asList(subcomponent1, subcomponent2, subcomponent3, "MS14108-3 1 ---", "505-04596-001 1 1 ---", "550-05526-001 1 1 --A", "550-05629-401 PRIMARY 1 ---", "550-05673-401 PRIMARY 1 ---",
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

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents();

        allSubComponents.forEach(subcomponent -> assertThat(componentsListPage.getListOfSubcomponents(), hasItem(subcomponent.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"10810", "10813", "10814"})
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

        componentsListPage = evaluatePage.openComponents();

        softAssertions.assertThat(componentsListPage.getRowDetails(pin, scenarioName)).contains("circle-minus");
        softAssertions.assertThat(componentsListPage.getRowDetails(bigRing, scenarioName)).contains("circle-minus");
        softAssertions.assertThat(componentsListPage.getRowDetails(smallRing, scenarioName)).contains("circle-minus");

        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        editComponentsPage = componentsListPage.closePanel()
            .publishScenario(PublishPage.class)
            .publish(componentAssembly, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(assemblyName, scenarioName)
            .editScenario()
            .close(EvaluatePage.class)
            .lock(EvaluatePage.class)
            .publishScenario(EditComponentsPage.class);

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("A public scenario with this name already exists. Cancel this operation, or select an option below and continue.");

        evaluatePage = editComponentsPage.overrideScenarios()
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getCurrentScenarioName()).isEqualTo(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10815"})
    @Description("Shallow Edit assembly and scenarios that was uncosted in CI Design")
    public void testUploadUncostedAssemblySubcomponentRename() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

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
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .publishScenario(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(PublishPage.class)
            .publish(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(equalTo(newScenarioName)));
    }
}
