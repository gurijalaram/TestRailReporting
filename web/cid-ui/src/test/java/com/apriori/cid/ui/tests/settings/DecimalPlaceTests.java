package com.apriori.cid.ui.tests.settings;

import static com.apriori.shared.util.enums.DigitalFactoryEnum.APRIORI_UNITED_KINGDOM;
import static com.apriori.shared.util.enums.DigitalFactoryEnum.APRIORI_USA;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.CostDetailsPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.PreviewPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.DecimalPlaceEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class DecimalPlaceTests extends TestBaseUI {
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private MaterialProcessPage materialProcessPage;
    private CostDetailsPage costDetailsPage;
    private PreviewPage previewPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;
    private ComponentInfoBuilder componentAssembly;

    public DecimalPlaceTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
        if (componentAssembly != null) {
            new UserPreferencesUtil().resetSettings(componentAssembly.getUser());
        }
    }

    @Tag(SMOKE)
    @Test
    @TestRail(id = {5287, 5288, 5291, 5297, 5290, 5295, 6633})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaults() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.SIX)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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

        String value = Double.toString(costDetailsPage.getCostSumValue("Total Variable Cost"));
        softAssertions.assertThat(costDetailsPage.getCostSumValueAsString("Total Variable Cost").split("\\.")[1].length())
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
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(component.getUser())
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.SIX)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .highlightScenario(component.getComponentName(), component.getScenarioName())
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
        componentAssembly = new AssemblyRequestUtil().getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                .build()));
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.SIX)
            .submit(ExplorePage.class)
            .openScenario(component.getComponentName(), component.getScenarioName());

        softAssertions.assertThat(evaluatePage.getCostResultsText("Assembly Process Cost").split("\\.")[1].length())
            .as("Verify Assembly Process Cost displayed to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Cost").split("\\.")[1].length())
            .as("Verify Total Cost displayed to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getCostResultsText("Total Investments").split("\\.")[1].length())
            .as("Verify Total Investments displayed to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getMaterialResultText("Finish Mass").split("\\.")[1].length())
            .as("Verify Finish Mass displayed to 6 decimal places").isEqualTo(6);
        softAssertions.assertThat(evaluatePage.getMaterialResultText("Assembly Time").split("\\.")[1].length())
            .as("Verify Assembly Time displayed to 6 decimal places").isEqualTo(6);

        softAssertions.assertAll();
    }
}