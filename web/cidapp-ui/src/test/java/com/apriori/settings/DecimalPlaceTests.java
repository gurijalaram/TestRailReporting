package com.apriori.settings;

import static com.apriori.TestSuiteType.TestSuite.SMOKE;
import static com.apriori.enums.DigitalFactoryEnum.APRIORI_UNITED_KINGDOM;
import static com.apriori.enums.DigitalFactoryEnum.APRIORI_USA;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.evaluate.CostDetailsPage;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.explore.PreviewPage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.DecimalPlaceEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DecimalPlaceTests extends TestBaseUI {
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private MaterialProcessPage materialProcessPage;
    private CostDetailsPage costDetailsPage;
    private PreviewPage previewPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public DecimalPlaceTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Tag(SMOKE)
    @Test
    @TestRail(id = {5287, 5288, 5291, 5297, 5290, 5295, 6633})
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
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Finish Mass")).split("\\.")[1].length())
            .as("Finish Mass shown to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Utilization")).split("\\.")[1].length())
            .as("Utilization shown to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getProcessesResultText("Total Cycle Time").split("\\.")[1].length())
            .as("Total Cycle Time shown to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Material Cost")).split("\\.")[1].length())
            .as("Material Cost shown to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Fully Burdened Cost")).split("\\.")[1].length())
            .as("Fully Burdened Cost shown to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Capital Investment").split("\\.")[1].length())
            .as("Total Capital Investment shown to 6 decimal places").isEqualTo(6);

        evaluatePage.openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.ONE)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Finish Mass")).split("\\.")[1].length())
            .as("Finish Mass shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Utilization")).split("\\.")[1].length())
            .as("Utilization shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(evaluatePage.getProcessesResultText("Total Cycle Time").split("\\.")[1].length())
            .as("Total Cycle Time shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Material Cost")).split("\\.")[1].length())
            .as("Material Cost shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Fully Burdened Cost")).split("\\.")[1].length())
            .as("Fully Burdened Cost shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Capital Investment").split("\\.")[1].length())
            .as("Total Capital Investment shown to 1 decimal place").isEqualTo(1);

        materialProcessPage = evaluatePage.openMaterialProcess();

        softAssertions.assertThat(Double.toString(materialProcessPage.getTotalResult("Cycle Time")).split("\\.")[1].length())
            .as("Cycle Time shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(materialProcessPage.getTotalResult("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(materialProcessPage.getTotalResult("Fully Burdened Cost")).split("\\.")[1].length())
            .as("Fully Burdened Cost shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(materialProcessPage.getTotalResult("Total Capital Investment")).split("\\.")[1].length())
            .as("Total Capital Investment shown to 1 decimal place").isEqualTo(1);

        costDetailsPage = evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        softAssertions.assertThat(Double.toString(costDetailsPage.getCostSumValue("Total Variable Cost")).split("\\.")[1].length())
            .as("Total Variable Cost shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(costDetailsPage.getCostContributionValue("Indirect Overhead")).split("\\.")[1].length())
            .as("Indirect Overhead shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(costDetailsPage.getCostContributionValue("SG&A")).split("\\.")[1].length())
            .as("SG&A shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(costDetailsPage.getCostContributionValue("Margin")).split("\\.")[1].length())
            .as("Margin shown to 1 decimal place").isEqualTo(1);
        softAssertions.assertThat(Double.toString(costDetailsPage.getCostSumValue("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 1 decimal place").isEqualTo(1);

        evaluatePage.openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.FOUR)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Finish Mass")).split("\\.")[1].length())
            .as("Finish Mass shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Utilization")).split("\\.")[1].length())
            .as("Utilization shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(evaluatePage.getProcessesResultText("Total Cycle Time").split("\\.")[1].length())
            .as("Total Cycle Time shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Material Cost")).split("\\.")[1].length())
            .as("Material Cost shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Fully Burdened Cost")).split("\\.")[1].length())
            .as("Fully Burdened Cost shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Capital Investment").split("\\.")[1].length())
            .as("Total Capital Investment shown to 4 decimal places").isEqualTo(4);

        materialProcessPage = evaluatePage.openMaterialProcess();

        softAssertions.assertThat(materialProcessPage.getTotalResultText("Cycle Time").split("\\.")[1].length())
            .as("Cycle Time shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(materialProcessPage.getTotalResult("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(materialProcessPage.getTotalResult("Fully Burdened Cost")).split("\\.")[1].length())
            .as("Fully Burdened Cost shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(materialProcessPage.getTotalResultText("Total Capital Investment").split("\\.")[1].length())
            .as("Total Capital Investment shown to 4 decimal places").isEqualTo(4);

        costDetailsPage = evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        softAssertions.assertThat(Double.toString(costDetailsPage.getCostSumValue("Total Variable Cost")).split("\\.")[1].length())
            .as("Total Variable Cost shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(costDetailsPage.getCostContributionValue("Indirect Overhead")).split("\\.")[1].length())
            .as("Indirect Overhead shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(costDetailsPage.getCostContributionValue("SG&A")).split("\\.")[1].length())
            .as("SG&A shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(costDetailsPage.getCostContributionValueText("Margin").split("\\.")[1].length())
            .as("Margin shown to 4 decimal places").isEqualTo(4);
        softAssertions.assertThat(Double.toString(costDetailsPage.getCostSumValue("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 4 decimal places").isEqualTo(4);

        costDetailsPage.closePanel();

        evaluatePage.openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.FIVE)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Finish Mass")).split("\\.")[1].length())
            .as("Finish Mass shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Utilization")).split("\\.")[1].length())
            .as("Utilization shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(evaluatePage.getProcessesResultText("Total Cycle Time").split("\\.")[1].length())
            .as("Total Cycle Time shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Material Cost")).split("\\.")[1].length())
            .as("Material Cost shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Fully Burdened Cost")).split("\\.")[1].length())
            .as("Fully Burdened Cost shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Capital Investment").split("\\.")[1].length())
            .as("Total Capital Investment shown to 5 decimal places").isEqualTo(5);

        evaluatePage.selectDigitalFactory(APRIORI_UNITED_KINGDOM)
            .costScenario();

        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Finish Mass")).split("\\.")[1].length())
            .as("Finish Mass shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(Double.toString(evaluatePage.getMaterialResult("Utilization")).split("\\.")[1].length())
            .as("Utilization shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(evaluatePage.getProcessesResultText("Total Cycle Time").split("\\.")[1].length())
            .as("Total Cycle Time shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Material Cost").split("\\.")[1].length())
            .as("Material Cost shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Piece Part Cost")).split("\\.")[1].length())
            .as("Piece Part Cost shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(Double.toString(evaluatePage.getCostResults("Fully Burdened Cost")).split("\\.")[1].length())
            .as("Fully Burdened Cost shown to 5 decimal places").isEqualTo(5);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Capital Investment").split("\\.")[1].length())
            .as("Total Capital Investment shown to 5 decimal places").isEqualTo(5);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5293})
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

        softAssertions.assertThat(previewPage.getMaterialResultText("Piece Part Cost").split("\\.")[1].length())
            .as("Piece Part Cost to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(previewPage.getMaterialResultText("Fully Burdened Cost").split("\\.")[1].length())
            .as("Fully Burdened Cost to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(previewPage.getMaterialResultText("Total Capital Investment").split("\\.")[1].length())
            .as("Total Capital Investment to 6 decimal places").isEqualTo(6);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5294})
    @Description("Ensure number of decimal places is respected in Assemblies")
    public void decimalPlacesForAssembly() {
        final String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("flange", "nut", "bolt");
        final String subComponentExtension = ".CATPart";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;

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
        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(subComponentProcessGroup.getProcessGroup())
                .build()));
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.SIX)
            .submit(ExplorePage.class)
            .openScenario(assemblyName, scenarioName);

        softAssertions.assertThat(evaluatePage.getCostResultsText("Assembly Process Cost").split("\\.")[1].length())
            .as("Verify Assembly Process Cost displayed to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Cost").split("\\.")[1].length())
            .as("Verify Total Cost displayed to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Investments").split("\\.")[1].length())
            .as("Verify Total Investments displayed to 6 decimal places").isEqualTo(6);
        ;
        softAssertions.assertThat(evaluatePage.getMaterialResultText("Finish Mass").split("\\.")[1].length())
            .as("Verify Finish Mass displayed to 6 decimal places").isEqualTo(6);
        ;
        softAssertions.assertThat(evaluatePage.getMaterialResultText("Assembly Time").split("\\.")[1].length())
            .as("Verify Assembly Time displayed to 6 decimal places").isEqualTo(6);
        ;

        softAssertions.assertAll();
    }
}