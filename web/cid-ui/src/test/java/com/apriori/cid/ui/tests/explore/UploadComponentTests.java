package com.apriori.cid.ui.tests.explore;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SANITY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.explore.CadFileStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.ImportCadFilePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.cid.ui.utils.UploadStatusEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.SerializationUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UploadComponentTests extends TestBaseUI {

    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder component;
    private File resourceFile;
    private ExplorePage explorePage;
    private CadFileStatusPage cadFileStatusPage;
    private ImportCadFilePage importCadFilePage;
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();

    @Test
    @Tag(SANITY)
    @Description("Test uploading a component")
    public void testUploadComponent() {

        component = new ComponentRequestUtil().getComponent();

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .importCadFile()
            .inputComponentDetails(component.getScenarioName(), component.getResourceFile())
            .waitForUploadStatus(component.getComponentName() + component.getExtension(), UploadStatusEnum.UPLOADED)
            .submit()
            .clickClose()
            .clickSearch(component.getComponentName())
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName()), is(equalTo(1)));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {11879, 11878, 12161})
    @Description("Validate messaging upon successful upload of multiple files")
    public void testMultiUploadSuccessMessage() {

        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(3);

        importCadFilePage = new CidAppLoginPage(driver)
            .login(UserUtil.getUser())
            .importCadFile()
            .inputDefaultScenarioName(new GenerateStringUtil().generateScenarioName())
            .inputMultiComponentBuilderDetails(components);

        cadFileStatusPage = importCadFilePage.submit();

        assertThat(cadFileStatusPage.getNumberOfSuccesses(), is(equalTo(components.size())));
    }

    @Test
    @TestRail(id = {11884, 11895})
    @Description("Validate that user can apply unique names to all multiple uploads")
    public void testUniqueScenarioNamesMultiUpload() {

        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(3);
        components.forEach(component -> component.setScenarioName(new GenerateStringUtil().generateScenarioName()));

        importCadFilePage = new CidAppLoginPage(driver)
            .login(UserUtil.getUser())
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentBuilderDetails(components);

        components.forEach(component -> importCadFilePage.inputFileScenarioName(component.getResourceFile().getName(), component.getScenarioName()));

        importCadFilePage.submit()
            .clickClose();

        components.forEach(component ->
            assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName()), is(equalTo(1))));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {11878, 11883})
    @Description("Validate multi-upload through explorer menu")
    public void testMultiUploadWithSameScenarioName() {

        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(3);

        explorePage = new CidAppLoginPage(driver)
            .login(UserUtil.getUser())
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentBuilderDetails(components)
            .submit()
            .clickClose();

        components.forEach(component ->
            assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName()), is(equalTo(1))));
    }

    @Test
    @TestRail(id = {11881, 11882})
    @Description("Validate prompt if invalid files are submitted")
    public void testInvalidFileUpload() {

        UserCredentials currentUser = UserUtil.getUser();
        resourceFile = FileResourceUtil.getResourceAsFile("auto_api_upload.csv");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String message = "The file type of the selected file is not supported." +
            " Supported file types are: .asat, .asm, .asm.#, .catpart, .catproduct, .iam, .ipt," +
            " .jt, .model, .par, .prt, .prt.#, .psm, .sab, .sat, .sldasm, .sldprt, .step, .stp, .x_b, .x_t, .xas, .xpr";

        importCadFilePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile();

        softAssertions.assertThat(importCadFilePage.getAssociationAlert()).contains("No Assembly Association Strategy has been selected. " +
            "The default strategy: Prefer Private Scenarios will be used until updated in User Preferences.");

        importCadFilePage.inputComponentDetails(scenarioName, resourceFile);

        softAssertions.assertThat(importCadFilePage.getFileInputErrorMessage()).isEqualTo(message);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11891)
    @Description("Validate that user can delete components from the Import CAD File modal")
    public void testDeleteCadFiles() {

        componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge Assembly");

        List<String> componentsToDelete = componentAssembly.getSubComponents().stream().map(ComponentInfoBuilder::getComponentName).collect(Collectors.toList());

        importCadFilePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .inputMultiAssemblyBuilder(componentAssembly)
            .deleteCadFiles(componentAssembly);

        importCadFilePage.getComponentsInDropZone().forEach(component ->
            assertThat(componentsToDelete.contains(component), is(false)));
    }

    @Test
    @TestRail(id = {11898, 11893})
    @Description("Upload 20 different components through the explorer modal")
    public void testTwentyCadFilesMultiUpload() {

        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(20);

        importCadFilePage = new CidAppLoginPage(driver)
            .login(components.stream().findAny().get().getUser())
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentBuilderDetails(components);

        softAssertions.assertThat(importCadFilePage.isTheScrollBarDisplayed()).isTrue();

        explorePage = importCadFilePage
            .submit()
            .clickClose()
            .setPagination()
            .selectFilter("Recent");

        components.forEach(component ->
            softAssertions.assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName())).isEqualTo(1));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11889)
    @Description("Validate override existing scenario leads to processing failure if unchecked and there are duplicate scenarios")
    public void testOverrideExistingScenarioFailure() {

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .inputDefaultScenarioName(componentAssembly.getScenarioName())
            .inputMultiAssemblyBuilder(componentAssembly)
            .submit()
            .clickClose()
            .importCadFile()
            .inputDefaultScenarioName(componentAssembly.getScenarioName())
            .inputMultiAssemblyBuilder(componentAssembly)
            .submit()
            .clickClose();

        SoftAssertions softAssertions = new SoftAssertions();

        //API assertion that components are Processing Failed
        componentAssembly.getSubComponents().forEach(component ->
            softAssertions.assertThat(cssComponent.getWaitBaseCssComponents(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.PROCESSING_FAILED)).hasSizeGreaterThan(0));

        explorePage.refresh();

        //UI Assertion that the explore page shows the Processing Failed Icon
        componentAssembly.getSubComponents().forEach(component ->
            softAssertions.assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.PROCESSING_FAILED)).isTrue());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11901)
    @Description("Validate that user is blocked from adding to a list of 20 uploads")
    public void testExceedingMaximumUpload() {

        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(21);
        List<ComponentInfoBuilder> componentsToUpload = components.subList(0, 20);

        importCadFilePage = new CidAppLoginPage(driver)
            .login(components.get(0).getUser())
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentBuilderDetails(componentsToUpload)
            .enterMultiFilePath(components.remove(components.size() - 1).getResourceFile());

        assertThat(importCadFilePage.getAlertWarning(), containsString("Exceeds maximum file count. Add up to 20 files for import at a time"));
    }

    @Test
    @TestRail(id = {11888, 5618})
    @Description("Validate override existing scenario is successful through multiple uploads when checked")
    public void testOverrideExistingScenarioSuccess() {

        componentAssembly = new AssemblyRequestUtil().getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .tick("Override existing scenario")
            .inputDefaultScenarioName(componentAssembly.getScenarioName())
            .inputMultiComponentBuilderDetails(componentAssembly.getSubComponents())
            .submit()
            .clickClose();

        componentAssembly.getSubComponents().forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(componentAssembly.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh();

        componentAssembly.getSubComponents().forEach(component ->
            assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.NOT_COSTED), is(true)));
    }

    @Test
    @TestRail(id = 10750)
    @Description("Validate updated workflow of importing/uploading an assembly into CID")
    public void testUploadViaExploreAndEvaluatePage() {

        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .inputDefaultScenarioName(componentAssembly.getScenarioName())
            .inputMultiAssemblyBuilder(componentAssembly)
            .submit()
            .clickClose()
            .openComponent(componentAssembly.getComponentName().toUpperCase(), componentAssembly.getScenarioName(), componentAssembly.getUser())
            .importCadFile()
            .inputDefaultScenarioName(scenarioName2)
            .inputMultiAssemblyBuilder(componentAssembly)
            .submit()
            .clickClose()
            .refresh();

        componentAssembly.getSubComponents().forEach(component ->
            assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.NOT_COSTED), is(true)));
    }

    @Test
    @TestRail(id = 6037)
    @Description("Create a New Component.Scenario - user does not have a pre existing private Component.Scenario of that name")
    public void testUploadThenCheckAvailabilityWithNewUser() {

        component = new ComponentRequestUtil().getComponent();

        evaluatePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);

        explorePage = evaluatePage.logout()
            .login(UserUtil.getUser());

        softAssertions.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName())).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11900, 11890})
    @Description("Validate multiple upload of same components is blocked")
    public void multipleUploadOfSameComponents() {
        component = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = SerializationUtils.clone(component);

        importCadFilePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .importCadFile()
            .inputDefaultScenarioName(component.getScenarioName())
            .inputMultiComponentsBuilder(Arrays.asList(component, componentB));

        softAssertions.assertThat(importCadFilePage.getAlertWarning()).isEqualTo(component.getComponentName() + component.getExtension() + " is already selected.");

        softAssertions.assertThat(importCadFilePage.getTooltipMessage()).isEqualTo("If unchecked, import will fail when the scenario already exists. Delete failed scenarios and repeat import.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11910})
    @Description("Upload different Creo versions of files")
    public void uploadDifferentCreoVersions() {
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponentWithExtension("piston", "prt.5");
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentWithExtension("piston", "prt.6");
        componentB.setUser(componentA.getUser());

        evaluatePage = new CidAppLoginPage(driver)
            .login(componentA.getUser())
            .uploadComponentAndOpen(componentA)
            .clickExplore()
            .importCadFile()
            .inputComponentDetails(componentB.getScenarioName(), componentB.getResourceFile())
            .tick("Override existing scenario")
            .waitForUploadStatus(componentB.getComponentName().concat(componentB.getExtension()), UploadStatusEnum.UPLOADED)
            .submit()
            .clickClose()
            .openScenario(componentB.getComponentName(), componentB.getScenarioName())
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.CAD)).isTrue();

        explorePage = evaluatePage.clickExplore()
            .clickSearch(componentB.getComponentName())
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), componentB.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {12169, 12171, 12172, 12168})
    @Description("Validate race conditions - upload a full assembly with override")
    public void uploadMultiLevelAssemblyWithOverrideAndRename() {

        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(9);

        components.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getComponentName(),
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage = new ExplorePage(driver);
        componentsTreePage = explorePage.refresh()
            .setPagination()
            .openScenario("MainAssembly", scenarioName)
            .openComponents()
            .expandSubAssembly("SubAssembly1", scenarioName);

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("98241")).isTrue();
        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("33803")).isTrue();

        componentsTreePage.expandSubAssembly("SubAssembly2", scenarioName);

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("78829")).isTrue();
        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("78828")).isTrue();

        componentsTreePage.closePanel()
            .clickExplore()
            .importCadFile()
            .inputDefaultScenarioName(scenarioName2)
            .inputMultiComponentsBuilder(components)
            .submit()
            .clickClose()
            .selectFilter("Recent");

        components.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .setPagination()
            .openScenario("MainAssembly", scenarioName2)
            .openComponents()
            .expandSubAssembly("SubAssembly1", scenarioName2);

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("98241")).isTrue();
        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("33803")).isTrue();

        componentsTreePage.expandSubAssembly("SubAssembly2", scenarioName2);

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("78829")).isTrue();
        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("78828")).isTrue();

        componentsTreePage.closePanel()
            .clickExplore()
            .importCadFile()
            .inputDefaultScenarioName(scenarioName)
            .inputMultiComponentsBuilder(components)
            .tick("Override existing scenario")
            .submit()
            .clickClose()
            .selectFilter("Recent");

        components.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .setPagination()
            .openScenario("MainAssembly", scenarioName)
            .openComponents()
            .expandSubAssembly("SubAssembly1", scenarioName);

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("98241")).isTrue();
        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("33803")).isTrue();

        componentsTreePage.expandSubAssembly("SubAssembly2", scenarioName);

        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("78829")).isTrue();
        softAssertions.assertThat(componentsTreePage.isComponentNameDisplayedInTreeView("78828")).isTrue();

        softAssertions.assertAll();
    }
}