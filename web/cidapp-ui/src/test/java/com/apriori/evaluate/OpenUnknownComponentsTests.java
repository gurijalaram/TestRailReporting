package com.apriori.evaluate;

import static com.apriori.TestSuiteType.TestSuite.REGRESSION;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.MultiUpload;
import com.utils.UploadStatusEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.jupiter.api.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenUnknownComponentsTests extends TestBaseUI {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14203)
    @Description("Test opening an 'unknown' component immediately after upload")
    public void testOpenUnknownPart() {
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
            .clickClose();

        softAssertions.assertThat(explorePage.getScenarioState(componentName.toUpperCase(), scenarioName))
            .as("Verify scenario in processing state").isEqualTo("gear");

        evaluatePage = explorePage.openScenario(componentName, scenarioName);

        softAssertions.assertThat(evaluatePage.getPartName())
            .as("Verify Part Name").isEqualTo(componentName.toUpperCase());
        softAssertions.assertThat(evaluatePage.getCurrentScenarioName())
            .as("Verify Scenario Name").isEqualTo(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14204)
    @Description("Test opening an 'unknown' component immediately after upload")
    public void testOpenUnknownAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        final String componentName = "top-level";
        final String extension = ".asm.1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .waitForUploadStatus(componentName + extension, UploadStatusEnum.UPLOADED)
            .submit()
            .clickClose();

        softAssertions.assertThat(explorePage.getScenarioState(componentName.toUpperCase(), scenarioName))
            .as("Verify scenario in processing state").isEqualTo("gear");

        evaluatePage = explorePage.openScenario(componentName, scenarioName);

        softAssertions.assertThat(evaluatePage.getPartName())
            .as("Verify Part Name").isEqualTo(componentName.toUpperCase());
        softAssertions.assertThat(evaluatePage.getCurrentScenarioName())
            .as("Verify Scenario Name").isEqualTo(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 15038)
    @Description("Test opening a single part component from import modal")
    public void testOpenPartFromImportModal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        final String componentName = "Case_17";
        final String extension = ".stp";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .waitForUploadStatus(componentName + extension, UploadStatusEnum.UPLOADED)
            .submit()
            .openComponent(componentName + extension);

        softAssertions.assertThat(evaluatePage.getPartName())
            .as("Verify Part Name").isEqualTo(componentName.toUpperCase());
        softAssertions.assertThat(evaluatePage.getCurrentScenarioName())
            .as("Verify Scenario Name").isEqualTo(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 15038)
    @Description("Test opening an 'unknown' component from import modal")
    public void testOpenAssemblyFromImportModal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        final String componentName = "top-level";
        final String extension = ".asm.1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .waitForUploadStatus(componentName + extension, UploadStatusEnum.UPLOADED)
            .submit()
            .openComponent(componentName + extension);

        softAssertions.assertThat(evaluatePage.getPartName())
            .as("Verify Part Name").isEqualTo(componentName.toUpperCase());
        softAssertions.assertThat(evaluatePage.getCurrentScenarioName())
            .as("Verify Scenario Name").isEqualTo(scenarioName);

        softAssertions.assertAll();
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 15039)
    @Description("Test opening a single part component from import modal after multi file upload")
    public void testOpenFromImportModalMultiUpload() {
        final String targetComponentName = "Case_17";
        final String extension = ".stp";

        String scenarioName = new GenerateStringUtil().generateScenarioName();

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "nut.CATPart"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING, "Case_17.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "bolt.CATPart"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputMultiComponents(multiComponents)
            .submit()
            .openComponent(targetComponentName + extension);

        softAssertions.assertThat(evaluatePage.getPartName())
            .as("Verify Part Name").isEqualTo(targetComponentName.toUpperCase());

        softAssertions.assertAll();
    }
}
