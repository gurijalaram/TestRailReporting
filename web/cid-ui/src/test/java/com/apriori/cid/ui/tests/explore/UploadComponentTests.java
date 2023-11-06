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

import com.apriori.cid.api.models.dto.AssemblyDTORequest;
import com.apriori.cid.api.models.dto.ComponentDTORequest;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.explore.CadFileStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.ImportCadFilePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.MultiUpload;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.cid.ui.utils.UploadStatusEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UploadComponentTests extends TestBaseUI {

    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder component;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private File resourceFile;
    private File resourceFile1;
    private ExplorePage explorePage;
    private UserCredentials currentUser;
    private CadFileStatusPage cadFileStatusPage;
    private ImportCadFilePage importCadFilePage;
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();

    @Test
    @Tag(SANITY)
    @Description("Test uploading a component")
    public void testUploadComponent() {

        component = new ComponentDTORequest().getComponent();

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

        List<ComponentInfoBuilder> components = new ComponentDTORequest().getComponents(3);

        importCadFilePage = new CidAppLoginPage(driver)
            .login(UserUtil.getUser())
            .importCadFile()
            .inputScenarioName(new GenerateStringUtil().generateScenarioName())
            .inputMultiComponentBuilderDetails(components);

        cadFileStatusPage = importCadFilePage.submit();

        assertThat(cadFileStatusPage.getNumberOfSuccesses(), is(equalTo(components.size())));
    }

    @Test
    @TestRail(id = {11884, 11895})
    @Description("Validate that user can apply unique names to all multiple uploads")
    public void testUniqueScenarioNamesMultiUpload() {

        List<ComponentInfoBuilder> components = new ComponentDTORequest().getComponents(3);

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
    @Tag(SMOKE)
    @TestRail(id = {11878, 11883})
    @Description("Validate multi-upload through explorer menu")
    public void testMultiUploadWithSameScenarioName() {

        List<ComponentInfoBuilder> components = new ComponentDTORequest().getComponents(3);

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

        currentUser = UserUtil.getUser();
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

        componentAssembly = new AssemblyDTORequest().getAssembly("Hinge Assembly");

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

        List<ComponentInfoBuilder> components = new ComponentDTORequest().getComponents(20);

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

        componentAssembly = new AssemblyDTORequest().getAssembly();

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .inputScenarioName(componentAssembly.getScenarioName())
            .inputMultiAssemblyBuilder(componentAssembly)
            .submit()
            .clickClose()
            .importCadFile()
            .inputScenarioName(componentAssembly.getScenarioName())
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

        List<ComponentInfoBuilder> components = new ComponentDTORequest().getComponents(21);
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

        componentAssembly = new AssemblyDTORequest().getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .tick("Override existing scenario")
            .inputScenarioName(componentAssembly.getScenarioName())
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

        componentAssembly = new AssemblyDTORequest().getAssembly();

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .inputScenarioName(componentAssembly.getScenarioName())
            .inputMultiAssemblyBuilder(componentAssembly)
            .submit()
            .clickClose()
            .openComponent(componentAssembly.getComponentName().toUpperCase(), componentAssembly.getScenarioName(), componentAssembly.getUser())
            .importCadFile()
            .inputScenarioName(scenarioName2)
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

        component = new ComponentDTORequest().getComponent();

        cidComponentItem = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponent(component.getComponentName(), component.getScenarioName(), component.getResourceFile(), component.getUser());

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem);

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

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_pin.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_pin.prt.1"), scenarioName));

        importCadFilePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents);

        softAssertions.assertThat(importCadFilePage.getAlertWarning()).isEqualTo("piston_pin.prt.1 is already selected.");

        softAssertions.assertThat(importCadFilePage.getTooltipMessage()).isEqualTo("If unchecked, import will fail when the scenario already exists. Delete failed scenarios and repeat import.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11910})
    @Description("Upload different Creo versions of files")
    public void uploadDifferentCreoVersions() {

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String componentName1 = "piston";
        final String extension1 = ".prt.5";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName1 + extension1);
        final String componentName2 = "piston";
        final String extension2 = ".prt.6";
        resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + extension2);

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(componentName1, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile1)
            .tick("Override existing scenario")
            .waitForUploadStatus(componentName2.concat(extension2), UploadStatusEnum.UPLOADED)
            .submit()
            .clickClose()
            .openScenario(componentName2, scenarioName)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_CREATE_ACTION, 2);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.CAD)).isTrue();

        explorePage = evaluatePage.clickExplore()
            .clickSearch(componentName2)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName2, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {12169, 12171, 12172, 12168})
    @Description("Validate race conditions - upload a full assembly with override")
    public void uploadMultiLevelAssemblyWithOverrideAndRename() {

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final File resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "21395.ipt");
        final File resourceFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "33803.ipt");
        final File resourceFile3 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "33804.ipt");
        final File resourceFile4 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "78828.ipt");
        final File resourceFile5 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "78829.ipt");
        final File resourceFile6 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "98241.ipt");
        final File resourceFile7 = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "SubAssembly1.iam");
        final File resourceFile8 = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "SubAssembly2.iam");
        final File resourceFile9 = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "MainAssembly.iam");

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(resourceFile, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile2, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile3, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile4, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile5, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile6, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile7, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile8, scenarioName2));
        multiComponents.add(new MultiUpload(resourceFile9, scenarioName2));

        List<ScenarioItem> componentItems = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadMultiComponentsCSS(
                Arrays.asList(
                    resourceFile,
                    resourceFile2,
                    resourceFile3,
                    resourceFile4,
                    resourceFile5,
                    resourceFile6,
                    resourceFile7,
                    resourceFile8,
                    resourceFile9
                ),
                scenarioName,
                currentUser
            );

        componentItems.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getComponentName(),
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
            .inputScenarioName(scenarioName2)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .selectFilter("Recent");

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
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
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .tick("Override existing scenario")
            .submit()
            .clickClose()
            .selectFilter("Recent");

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
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