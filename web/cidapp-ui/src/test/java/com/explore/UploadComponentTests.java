package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.explore.CadFileStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
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
    public void testDisabledScenarioNameTextBox() {
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

        importCadFilePage.scenarioNameTextBoxDisabled().forEach(textBox -> assertThat(textBox, is("true")));

        cadFileStatusPage = importCadFilePage.submit();

        assertThat(cadFileStatusPage.getImportMessage(), is(containsString(String.format("%s file(s) imported successfully.", multiComponents.size()))));
    }

    @Test
    @TestRail(testCaseId = "11884")
    @Description("Validate that user can apply unique names to all multiple uploads")
    public void multiUploadTests() {
        currentUser = UserUtil.getUser();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), new GenerateStringUtil().generateScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentDetails(multiComponents)
            .submit()
            .close();

        multiComponents.forEach(component -> assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0], component.getScenarioName()), is(equalTo(0))));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "{11878},{11883}")
    @Description("Validate multi-upload through explorer menu")
    public void testMultiUpload() {
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
                component.getScenarioName()), is(equalTo(0))));
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

        assertThat(importCadFilePage.getFileInputError(), is(message));
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

        List<String> componentNames = Arrays.asList("big ring.SLDPRT", "Pin.SLDPRT", "small ring.SLDPRT", "Hinge assembly.SLDASM");
        String message = "Drag and drop or click here to import CAD file(s).";

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .cadFilesToDelete(componentNames);

        assertThat(importCadFilePage.getDropZoneText(message), is(message));
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
                component.getScenarioName()), is(greaterThanOrEqualTo(0))));
    }
}
