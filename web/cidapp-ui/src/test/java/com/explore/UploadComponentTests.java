package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.explore.CadFileStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.MultiUpload;
import com.utils.SortOrderEnum;
import com.utils.UploadStatusEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadComponentTests extends TestBase {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private UserCredentials currentUser;
    private CadFileStatusPage cadFileStatusPage;
    private ImportCadFilePage importCadFilePage;

    @Test
    @Category(SanityTests.class)
    @Description("Test uploading a component")
    public void testUploadComponent() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        final String componentName = "Case_17";
        final String extension = ".stp";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .waitForUploadStatus(componentName + extension, UploadStatusEnum.UPLOADED)
            .submit()
            .close()
            .clickSearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "11879")
    @Description("Validate messaging upon successful upload of multiple files")
    public void testMultiUploadSuccessMessage() {
        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), new GenerateStringUtil().generateScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents);

        cadFileStatusPage = importCadFilePage.submit();

        assertThat(cadFileStatusPage.getImportMessage(), is(containsString(String.format("%s file(s) imported successfully.", multiComponents.size()))));
    }

    @Test
    @TestRail(testCaseId = "11884")
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

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentDetails(multiComponents)
            .submit()
            .close();

        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName()), is(equalTo(1))));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "{11878},{11883}")
    @Description("Validate multi-upload through explorer menu")
    public void testMultiUploadWithSameScenarioName() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .inputScenarioName(scenarioName)
            .submit()
            .close();

        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName()), is(equalTo(1))));
    }

    @Test
    @TestRail(testCaseId = "{11881},{11882}")
    @Description("Validate prompt if invalid files are submitted")
    public void testInvalidFileUpload() {
        currentUser = UserUtil.getUser();
        resourceFile = FileResourceUtil.getResourceAsFile("auto_api_upload.csv");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String message = "The file type of the selected file is not supported." +
            " Supported file types are: .asat, .asm, .asm.#, .catpart, .catproduct, .iam, .ipt," +
            " .jt, .model, .par, .prt, .prt.#, .psm, .sab, .sat, .sldasm, .sldprt, .step, .stp, .x_b, .x_t, .xas, .xpr";

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile);

        assertThat(importCadFilePage.getFileInputErrorMessage(), is(message));
    }

    @Test
    @TestRail(testCaseId = "11891")
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

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .cadFilesToDelete(componentsToDelete);

        importCadFilePage.getComponentsInDropZone().forEach(component ->
            assertThat(componentsToDelete.contains(component), is(false)));
    }

    @Test
    @TestRail(testCaseId = "11898")
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

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .inputScenarioName(scenarioName)
            .submit()
            .close()
            .setPagination();

        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName()), is(equalTo(1))));
    }

    @Test
    @TestRail(testCaseId = "11889")
    @Description("Validate override existing scenario leads to processing failure if unchecked and there are duplicate scenarios")
    public void testOverrideExistingScenarioFailure() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_pin.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston.prt.5"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "piston_assembly.asm.1"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .close()
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .close();

        //API assertion that components are Processing Failed
        multiComponents.forEach(component ->
            assertThat(explorePage.getProcessingFailedState(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), currentUser), is("PROCESSING_FAILED")));

        explorePage.refresh();

        //UI Assertion that the explore page shows the Processing Failed Icon
        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.PROCESSING_FAILED), is(1)));
    }

    @Test
    @TestRail(testCaseId = "11901")
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

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .enterMultiFilePath(resourceFile);

        assertThat(importCadFilePage.getAlertWarning(), containsString("Exceeds maximum file count. Add up to 20 files for import at a time"));
    }

    @Test
    @TestRail(testCaseId = "11888")
    @Description("Validate override existing scenario is successful through multiple uploads when checked")
    public void testOverrideExistingScenarioSuccess() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "piston_assembly";
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston_pin.prt.1"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "piston.prt.5"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "piston_assembly.asm.1"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .close()
            .importCadFile()
            .tick("Override existing scenario")
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .close()
            .openComponent(componentName, scenarioName, currentUser)
            .clickExplore();

        multiComponents.forEach(component ->
            assertThat(explorePage.getListOfScenariosWithStatus(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), ScenarioStateEnum.NOT_COSTED), is(1)));
    }
}
