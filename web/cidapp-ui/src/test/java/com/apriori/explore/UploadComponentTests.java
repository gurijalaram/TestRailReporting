package com.apriori.explore;

import static com.apriori.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SANITY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.request.AssemblyRequest;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.explore.CadFileStatusPage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.explore.ImportCadFilePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadComponentTests extends TestBaseUI {

    private static ComponentInfoBuilder componentAssembly;
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

    /*@Test
    @Tag(SANITY)
    @Description("Test uploading a component")
    public void testUploadComponent() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        final String componentName = "Case_17";
        final String extension = ".stp";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        explorePage = new CidAppLoginPage(driver)
            .login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .waitForUploadStatus(componentName + extension, UploadStatusEnum.UPLOADED)
            .submit()
            .clickClose()
            .clickSearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {11879, 11878, 12161})
    @Description("Validate messaging upon successful upload of multiple files")
    public void testMultiUploadSuccessMessage() {
        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), new GenerateStringUtil().generateScenarioName()));

        importCadFilePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents);

        cadFileStatusPage = importCadFilePage.submit();

        assertThat(cadFileStatusPage.getNumberOfSuccesses(), is(equalTo(multiComponents.size())));
    }

    @Test
    @TestRail(id = {11884, 11895})
    @Description("Validate that user can apply unique names to all multiple uploads")
    public void testUniqueScenarioNamesMultiUpload() {
        currentUser = UserUtil.getUser();
        String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), scenarioName1));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), scenarioName2));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), scenarioName3));

        explorePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentDetails(multiComponents)
            .submit()
            .clickClose();

        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName()), is(equalTo(1))));
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {11878, 11883})
    @Description("Validate multi-upload through explorer menu")
    public void testMultiUploadWithSameScenarioName() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), scenarioName));

        explorePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .inputScenarioName(scenarioName)
            .submit()
            .clickClose();

        multiComponents.forEach(component ->
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
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "Pin.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Hinge assembly.SLDASM"), scenarioName));

        List<String> componentsToDelete = Arrays.asList("big ring.SLDPRT", "Pin.SLDPRT", "small ring.SLDPRT");

        importCadFilePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .deleteCadFiles(componentsToDelete);

        importCadFilePage.getComponentsInDropZone().forEach(component ->
            assertThat(componentsToDelete.contains(component), is(false)));
    }

    @Test
    @TestRail(id = {11898, 11893})
    @Description("Upload 20 different components through the explorer modal")
    public void testTwentyCadFilesMultiUpload() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "Pin.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Hinge assembly.SLDASM"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston cover_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston pin_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston rod_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "v6 piston assembly_asm1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, "225_gasket-1-solid1.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "oldham.asm.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "M3CapScrew.CATPart"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "case_002_006-8611543_prt.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "BasicScenario_Forging.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.RAPID_PROTOTYPING, "Rapid Prototyping.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE, "SheetMetal.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, "Casting.prt"), scenarioName));

        importCadFilePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .inputScenarioName(scenarioName);

        softAssertions.assertThat(importCadFilePage.isTheScrollBarDisplayed()).isTrue();

        explorePage = importCadFilePage
            .submit()
            .clickClose()
            .setPagination()
            .selectFilter("Recent");

        multiComponents.forEach(component ->
            softAssertions.assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName())).isEqualTo(1));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11889)
    @Description("Validate override existing scenario leads to processing failure if unchecked and there are duplicate scenarios")
    public void testOverrideExistingScenarioFailure() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_pin.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston.prt.5"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "piston_assembly.asm.1"), scenarioName));

        explorePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose();

        SoftAssertions softAssertions = new SoftAssertions();

        //API assertion that components are Processing Failed
        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getWaitBaseCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.PROCESSING_FAILED)).hasSizeGreaterThan(0));

        explorePage.refresh();

        //UI Assertion that the explore page shows the Processing Failed Icon
        multiComponents.forEach(component ->
            softAssertions.assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.PROCESSING_FAILED)).isTrue());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11901)
    @Description("Validate that user is blocked from adding to a list of 20 uploads")
    public void testExceedingMaximumUpload() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        String componentName = "Casting.prt";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, componentName);

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "Pin.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Hinge assembly.SLDASM"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston cover_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston pin_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston rod_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "v6 piston assembly_asm1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, "225_gasket-1-solid1.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "oldham.asm.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "M3CapScrew.CATPart"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "case_002_006-8611543_prt.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "BasicScenario_Forging.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.RAPID_PROTOTYPING, "Rapid Prototyping.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE, "SheetMetal.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL_HYDROFORMING, "FlangedRound.SLDPRT"), scenarioName));

        importCadFilePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .enterMultiFilePath(resourceFile);

        assertThat(importCadFilePage.getAlertWarning(), containsString("Exceeds maximum file count. Add up to 20 files for import at a time"));
    }*/

    @Test
    @Tag(SANITY)
    @TestRail(id = {11888, 5618})
    @Description("Validate override existing scenario is successful through multiple uploads when checked")
    public void testOverrideExistingScenarioSuccess() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String sldprt = ".SLDPRT";

        componentAssembly = new AssemblyRequest().getAssemblySubcomponents("Hinge assembly", "Pin", "big ring", "small ring");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring" + sldprt), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "Pin" + sldprt), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring" + sldprt), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Hinge assembly" + ".SLDASM"), scenarioName));

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .importCadFile()
            .tick("Override existing scenario")
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose();

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(componentAssembly.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh();

        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.NOT_COSTED), is(true)));
    }

    /*@Test
    @TestRail(id = 10750)
    @Description("Validate updated workflow of importing/uploading an assembly into CID")
    public void testUploadViaExploreAndEvaluatePage() {
        currentUser = UserUtil.getUser();
        String componentName = "piston_assembly";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_pin.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston.prt.5"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "piston_assembly.asm.1"), scenarioName));

        explorePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .openComponent(componentName.toUpperCase(), scenarioName, currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName2)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .refresh();

        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.NOT_COSTED), is(true)));
    }

    @Test
    @TestRail(id = 6037)
    @Description("Create a New Component.Scenario - user does not have a pre existing private Component.Scenario of that name")
    public void testUploadThenCheckAvailabilityWithNewUser() {
        currentUser = UserUtil.getUser();

        final String componentName = "Locker_bottom_panel";
        final String extension = ".prt";
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        cidComponentItem = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE)).isEqualTo(true);

        explorePage = evaluatePage.logout()
            .login(UserUtil.getUser());

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName, scenarioName)).isEqualTo(0);

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
    }*/
}