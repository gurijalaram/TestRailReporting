package com.settings;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_UNITED_KINGDOM;
import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.PreviewPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.DecimalPlaceEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DecimalPlaceTests extends TestBase {
    File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private MaterialProcessPage materialProcessPage;
    private CostDetailsPage costDetailsPage;
    private PreviewPage previewPage;
    private ExplorePage explorePage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();

    public DecimalPlaceTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Category({SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"5287", "5288", "5291", "5297", "5290", "5295", "6633"})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaults() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
                .openSettings()
                .selectDecimalPlaces(DecimalPlaceEnum.SIX)
                .submit(ExplorePage.class)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .selectDigitalFactory(APRIORI_USA)
                .openMaterialSelectorTable()
                .search("AISI 1020")
                .selectMaterial("Steel, Cold Worked, AISI 1020")
                .submit(EvaluatePage.class)
                .costScenario();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5.309458), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Utilization")).isCloseTo(Double.valueOf(81.163688), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.400000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(33.87099), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(40.444918), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.444918), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(0.000000), Offset.offset(15.0));

        evaluatePage.openSettings()
                .selectDecimalPlaces(DecimalPlaceEnum.ONE)
                .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5.3), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Utilization")).isCloseTo(Double.valueOf(81.1), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.4), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(33.8), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(0.0), Offset.offset(15.0));
        materialProcessPage = evaluatePage.openMaterialProcess();

        softAssertions.assertThat(materialProcessPage.getTotalResult("Cycle Time")).isCloseTo(Double.valueOf(109.4), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Piece Part Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Total Capital Investment")).isCloseTo(Double.valueOf(0.0), Offset.offset(15.0));

        costDetailsPage = evaluatePage.openCostDetails()
                .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        softAssertions.assertThat(costDetailsPage.getCostSumValue("Total Variable Cost")).isCloseTo(Double.valueOf(46.2), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead")).isCloseTo(Double.valueOf(0.3), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("SG&A")).isCloseTo(Double.valueOf(4.6), Offset.offset(5.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Margin")).isCloseTo(Double.valueOf(0.0), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostSumValue("Piece Part Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));

        evaluatePage.openSettings()
                .selectDecimalPlaces(DecimalPlaceEnum.FOUR)
                .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5.309458), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Utilization")).isCloseTo(Double.valueOf(81.163688), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.4000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(33.871), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(0.0000), Offset.offset(15.0));

        materialProcessPage = evaluatePage.openMaterialProcess();

        softAssertions.assertThat(materialProcessPage.getTotalResult("Cycle Time")).isCloseTo(Double.valueOf(109.4000), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Piece Part Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Total Capital Investment")).isCloseTo(Double.valueOf(0.0000), Offset.offset(15.0));

        costDetailsPage = evaluatePage.openCostDetails()
                .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        softAssertions.assertThat(costDetailsPage.getCostSumValue("Total Variable Cost")).isCloseTo(Double.valueOf(46.1543), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead")).isCloseTo(Double.valueOf(0.3179), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("SG&A")).isCloseTo(Double.valueOf(4.5720), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Margin")).isCloseTo(Double.valueOf(0.0000), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostSumValue("Piece Part Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        costDetailsPage.closePanel();

        evaluatePage.openSettings()
                .selectDecimalPlaces(DecimalPlaceEnum.FIVE)
                .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5.30946), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Utilization")).isCloseTo(Double.valueOf(81.16369), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.40000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(33.87099), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(40.44494), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.44494), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(0.00000), Offset.offset(15.0));

        evaluatePage.selectDigitalFactory(APRIORI_UNITED_KINGDOM)
                .costScenario();

        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5.30946), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Utilization")).isCloseTo(Double.valueOf(81.16369), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.40000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(21.91453), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(29.01580), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(29.01580), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(00.00000), Offset.offset(15.0));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5293"})
    @Description("Ensure number of decimal places is respected in Preview Panel")
    public void decimalPlacesInPreviewPanel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(currentUser)
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.SIX)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .highlightScenario(componentName, scenarioName)
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.getMaterialResult("Piece Part Cost")).as("Piece Part Cost").isCloseTo(Double.valueOf(40.444918), Offset.offset(15.0));
        softAssertions.assertThat(previewPage.getMaterialResult("Fully Burdened Cost")).as("Fully Burdened Cost").isCloseTo(Double.valueOf(40.444918), Offset.offset(15.0));
        softAssertions.assertThat(previewPage.getMaterialResult("Total Capital Investment")).as("Total Capital Investment").isCloseTo(Double.valueOf(0.000000), Offset.offset(15.0));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5294"})
    @Description("Ensure number of decimal places is respected in Assemblies")
    public void decimalPlacesForAssembly() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.SIX)
            .submit(ExplorePage.class);

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

        evaluatePage = explorePage.refresh()
            .openScenario(assemblyName, scenarioName)
            .selectDigitalFactory(APRIORI_USA)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Assembly Process Cost")).isCloseTo(Double.valueOf(0.523424), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Cost")).isCloseTo(Double.valueOf(0.523424), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Investments")).isCloseTo(Double.valueOf(0.000000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(0.000000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Assembly Time")).isCloseTo(Double.valueOf(40.000000), Offset.offset(15.0));

        softAssertions.assertAll();

    }

}