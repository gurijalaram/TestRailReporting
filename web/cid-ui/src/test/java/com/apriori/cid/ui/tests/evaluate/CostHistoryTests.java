package com.apriori.cid.ui.tests.evaluate;

import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.evaluate.ChangeSummaryPage;
import com.apriori.cid.ui.pageobjects.evaluate.CostHistoryPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.models.response.component.CostRollupOverrides;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.SecondaryDigitalFactories;
import com.apriori.shared.util.models.response.component.SecondaryProcesses;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CostHistoryTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private CostHistoryPage costHistoryPage;

    private static ScenariosUtil scenariosUtil = new ScenariosUtil();
    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    private static ComponentInfoBuilder castingPart;

    public CostHistoryTests() {
        super();
    }

    @BeforeAll
    public static void createMultiCostedScenario() {
        castingPart = new ComponentRequestUtil().getComponentWithProcessGroup("Casting", ProcessGroupEnum.CASTING_DIE);

        // Iteration 1
        componentsUtil.postComponent(castingPart);

        // Iteration 2
        scenariosUtil.postGroupCostScenarios(castingPart);

        //Iteration 3
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        //Iteration 4
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
                .annualVolume(4096)
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        // Iteration 5
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .materialName(MaterialNameEnum.COPPER_UNS_C11000.getMaterialName())
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        // Iteration 6
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .secondaryProcesses(SecondaryProcesses.builder()
                    .heatTreatment(List.of("Certification"))
                    .otherSecondaryProcesses(List.of(""))
                    .machining(List.of(""))
                    .surfaceTreatment(List.of(""))
                    .castingDie(List.of(""))
                    .build())
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        //Iteration 7
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .vpeName(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory())
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        // Iteration 8
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .annualVolume(289)
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        // Iteration 9
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .batchSize(275)
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        // Iteration 10
        SecondaryDigitalFactories secondaryDF = new SecondaryDigitalFactories();
        secondaryDF.setHeatTreatment(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory());
        secondaryDF.setMachining(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());
        secondaryDF.setSurfaceTreatment(DigitalFactoryEnum.APRIORI_FINLAND.getDigitalFactory());
        secondaryDF.setOtherSecondaryProcesses(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory());
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories(Boolean.FALSE)
                .secondaryDigitalFactories(secondaryDF)
                .build());
        scenariosUtil.postGroupCostScenarios(castingPart);

        // Iteration 11
        castingPart.setCostingTemplate(CostingTemplate.builder()
            .costMode("MANUAL")
            .costRollupOverrides(CostRollupOverrides.builder()
                .piecePartCost(2.3)
                .totalCapitalInvestment(1.9)
                .build())
            .build());
        scenariosUtil.postCostScenario(castingPart);
    }

    private final List<String> defaultGraphIterationNames = Arrays.asList("Iteration 2", "Iteration 3", "Iteration 4", "Iteration 5",
        "Iteration 6", "Iteration 7", "Iteration 8", "Iteration 9", "Iteration 10", "Iteration 11");

    @Test
    @TestRail(id = {28442, 28443, 28444, 28447, 31031})
    @Description("Verify Cost History available")
    public void testCostHistory() {

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(castingPart.getUser())
            .openScenario(castingPart.getComponentName(), castingPart.getScenarioName());

        softAssertions.assertThat(evaluatePage.isProgressButtonEnabled()).as("Verify Progress button disabled before initial cost").isTrue();

        costHistoryPage = evaluatePage.clickHistory();

        ChangeSummaryPage changeSummary = costHistoryPage.openChangeSummary(2);

        softAssertions.assertThat(changeSummary.changedFromHeader()).as("Verify Left Column is Iteration 1").isEqualTo("Iteration 1");
        softAssertions.assertThat(changeSummary.changedToHeader()).as("Verify Right Column is Iteration 1").isEqualTo("Iteration 2");

        softAssertions.assertThat(changeSummary.getChangedFrom("Annual Volume")).as("Verify changed from in Annual Volume").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Annual Volume")).as("Verify changed to in Annual Volume").isEqualTo("500");
        softAssertions.assertThat(changeSummary.getChangedFrom("Batch Size")).as("Verify changed from in Batch Size").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Batch Size")).as("Verify changed to in Batch Size").isEqualTo("458");
        softAssertions.assertThat(changeSummary.getChangedFrom("Machining Mode")).as("Verify changed from in Machining Mode").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Machining Mode")).as("Verify changed to in Machining Mode").isEqualTo("MAY_BE_MACHINED");
        softAssertions.assertThat(changeSummary.getChangedFrom("Material")).as("Verify changed from in Material").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Material"))
            .as("Verify changed to in Material").isEqualTo(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName());
        softAssertions.assertThat(changeSummary.getChangedFrom("Process Group")).as("Verify changed from in Process Group").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Process Group"))
            .as("Verify changed to in Process Group").isEqualTo(ProcessGroupEnum.CASTING_DIE.getProcessGroup());
        softAssertions.assertThat(changeSummary.getChangedFrom("Years")).as("Verify changed from in Years").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Years")).as("Verify changed to in Years").isEqualTo("5");
        softAssertions.assertThat(changeSummary.getChangedFrom("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed from in Primary Digital Factory = Secondary Digital Factory").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed to in Primary Digital Factory = Secondary Digital Factory").isEqualTo("TRUE");
        softAssertions.assertThat(changeSummary.getChangedFrom("Digital Factory")).as("Verify changed from in Digital Factory").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Digital Factory")).as("Verify changed to in Digital Factory").isEqualTo("aPriori USA");

        softAssertions.assertThat(costHistoryPage.iterationCount()).as("count").isEqualTo(defaultGraphIterationNames.size() + 1);

        changeSummary = changeSummary.close(CostHistoryPage.class)
            .openChangeSummary(6);

        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Processes-Heat Treatment")).as("Verify changed from in Heat Treatment").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Processes-Heat Treatment")).as("Verify changed to in Heat Treatment").isEqualTo("Certification");

        changeSummary = changeSummary.close(CostHistoryPage.class)
            .openChangeSummary(7);

        softAssertions.assertThat(changeSummary.getChangedFrom("Digital Factory"))
            .as("Verify changed from in Digital Factory").isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedTo("Digital Factory"))
            .as("Verify changed to in Digital Factory").isEqualTo(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory());

        changeSummary = changeSummary.close(CostHistoryPage.class)
            .openChangeSummary(10);

        softAssertions.assertThat(changeSummary.getChangedFrom("Batch Size")).as("Verify changed from in Batch Size").isEqualTo("275");
        softAssertions.assertThat(changeSummary.getChangedTo("Batch Size")).as("Verify changed to in Batch Size").isEqualTo("458");
        softAssertions.assertThat(changeSummary.getChangedFrom("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed from in Primary Digital Factory = Secondary Digital Factory").isEqualTo("TRUE");
        softAssertions.assertThat(changeSummary.getChangedTo("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed to in Primary Digital Factory = Secondary Digital Factory").isEqualTo("FALSE");
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Heat Treatment"))
            .as("Verify changed from in Secondary Digital Factories-Heat Treatment").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Heat Treatment"))
            .as("Verify changed to in Secondary Digital Factories-Heat Treatment").isEqualTo(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Machining"))
            .as("Verify changed from in Secondary Digital Factories-Machining").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Machining"))
            .as("Verify changed to in Secondary Digital Factories-Machining").isEqualTo(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Other Secondary Processes"))
            .as("Verify changed from in Secondary Digital Factories-Other Secondary Processes").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Other Secondary Processes"))
            .as("Verify changed to in Secondary Digital Factories-Other Secondary Processes").isEqualTo(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Surface Treatment"))
            .as("Verify changed from in Secondary Digital Factories-Surface Treatment").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Surface Treatment"))
            .as("Verify changed to in Secondary Digital Factories-Surface Treatment").isEqualTo(DigitalFactoryEnum.APRIORI_FINLAND.getDigitalFactory());

        changeSummary = changeSummary.close(CostHistoryPage.class)
            .openChangeSummary(11);

        softAssertions.assertThat(changeSummary.getChangedFrom("Cost Mode"))
            .as("Verify changed from in Cost Mode").isEqualTo("APRIORI");
        softAssertions.assertThat(changeSummary.getChangedTo("Cost Mode"))
            .as("Verify changed to in Cost Mode").isEqualTo("MANUAL");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29698, 28927})
    @Description("Verify that History Graph axes are retained after closing modal / scenario | Test axes update accordingly when DFM risk selected")
    public void testGraphAxisRetention() {
        String primaryAxis = "Design Warnings";
        String secondaryAxis = "Material Carbon";

        ComponentInfoBuilder secondComponent = new ComponentRequestUtil().getComponent();
        secondComponent.setUser(castingPart.getUser());
        componentsUtil.postComponent(secondComponent);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(castingPart.getUser())
            .openScenario(castingPart.getComponentName(), castingPart.getScenarioName());

        costHistoryPage = evaluatePage.clickHistory();

        costHistoryPage.setPrimaryAxis(primaryAxis);
        costHistoryPage.setSecondaryAxis(secondaryAxis);

        costHistoryPage = costHistoryPage.close()
            .clickHistory();

        softAssertions.assertThat(costHistoryPage.selectedPrimaryAxis()).as("Primary Axis retained after close").isEqualTo(primaryAxis);
        softAssertions.assertThat(costHistoryPage.selectedSecondaryAxis()).as("Secondary Axis retained after close").isEqualTo(secondaryAxis);

        costHistoryPage = costHistoryPage.close()
            .clickExplore()
            .openScenario(secondComponent.getComponentName(), secondComponent.getScenarioName())
            .clickHistory();

        softAssertions.assertThat(costHistoryPage.getPlotAvailableMessage()).as("Verify that message displayed")
            .isEqualTo("Historical plots will be available when the scenario has been successfully costed more than once.");

        costHistoryPage = costHistoryPage.close()
            .clickExplore()
            .openScenario(castingPart.getComponentName(), castingPart.getScenarioName())
            .clickHistory();

        softAssertions.assertThat(costHistoryPage.selectedPrimaryAxis()).as("Primary Axis retained after opening another scenario")
            .isEqualTo(primaryAxis);
        softAssertions.assertThat(costHistoryPage.selectedSecondaryAxis()).as("Secondary Axis retained after opening another scenario")
            .isEqualTo(secondaryAxis);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29940, 29947, 29948, 29971})
    @Description("Verify Iterations can be hidden / displayed in graph")
    public void testShowHideIterations() {

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(castingPart.getUser())
            .openScenario(castingPart.getComponentName(), castingPart.getScenarioName());

        costHistoryPage = evaluatePage.clickHistory();

        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify all 10 expected iterations displayed")
            .isEqualTo(defaultGraphIterationNames.size());
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify all 10 expected iterations displayed")
            .isEqualTo(defaultGraphIterationNames);
        softAssertions.assertThat(costHistoryPage.showHideIconDisplayed(1)).as("Verify No Icon Displayed for Iteration 1").isFalse();

        softAssertions.assertThat(costHistoryPage.iterationDisplayIcon(4)).as("Verify hide iteration icon").isEqualTo("eye");

        costHistoryPage.showHideIteration(4);
        softAssertions.assertThat(costHistoryPage.iterationDisplayIcon(4)).as("Verify show iteration icon").isEqualTo("eye-slash");
        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify only 9 iterations displayed")
            .isEqualTo(defaultGraphIterationNames.size() - 1);
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify iteration 4 removed").doesNotContain("Iteration 4");

        costHistoryPage.showHideIteration(7);
        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify only 8 iterations displayed")
            .isEqualTo(defaultGraphIterationNames.size() - 2);
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify iteration 8 removed")
            .doesNotContain("Iteration 4", "Iteration 8");

        costHistoryPage.showHideIteration(4);
        softAssertions.assertThat(costHistoryPage.iterationDisplayIcon(4)).as("Verify hide iteration icon updated").isEqualTo("eye");
        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify 9 iterations displayed")
            .isEqualTo(defaultGraphIterationNames.size() - 1);
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify iteration 4 displayed").contains("Iteration 4");
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify iteration 8 remains removed").doesNotContain("Iteration 8");

        costHistoryPage.showHideIteration(7);
        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify all 10 expected iterations displayed")
            .isEqualTo(defaultGraphIterationNames.size());
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify all iterations displayed").isEqualTo(defaultGraphIterationNames);

        defaultGraphIterationNames.forEach(iteration -> costHistoryPage.showHideIteration(Integer.parseInt(iteration.split(" ")[1])));
        softAssertions.assertThat(costHistoryPage.getPlotAvailableMessage()).as("Verify that chart is replaced with error")
            .isEqualTo("Plots cannot be displayed with all iterations hidden.");

        defaultGraphIterationNames.forEach(iteration -> costHistoryPage.showHideIteration(Integer.parseInt(iteration.split(" ")[1])));

        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify all 10 expected iterations displayed")
            .isEqualTo(defaultGraphIterationNames.size());
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify all iterations displayed").isEqualTo(defaultGraphIterationNames);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29943, 29944, 29953, 29954, 29965, 29967})
    @Description("Verify Download as Image Preview")
    public void testDownloadAsImagePreview() {
        String testDate = DateTime.now().toString("MMMM d, yyyy");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(castingPart.getUser())
            .openScenario(castingPart.getComponentName(), castingPart.getScenarioName());

        costHistoryPage = evaluatePage.clickHistory();

        costHistoryPage.setPrimaryAxis("Total Cycle Time");
        costHistoryPage.setSecondaryAxis("DFM Risk");
        softAssertions.assertThat(costHistoryPage.selectedPrimaryAxis()).as("Selected Primary Axis").isEqualTo("Total Cycle Time");
        softAssertions.assertThat(costHistoryPage.selectedSecondaryAxis()).as("Selected Secondary Axis").isEqualTo("DFM Risk");

        costHistoryPage.openDownloadView();

        softAssertions.assertThat(costHistoryPage.downloadPreviewTitle()).as("Verify Preview Title")
            .isEqualTo(castingPart.getComponentName() + " / " + castingPart.getScenarioName() + " Total Cycle Time and DFM Risk");
        softAssertions.assertThat(costHistoryPage.downloadPreviewDate()).as("Verify Displayed Date").isEqualTo(testDate);
        softAssertions.assertThat(costHistoryPage.downloadPreviewFirstAxisName()).as("First Axis Name").isEqualTo("Total Cycle Time");
        softAssertions.assertThat(costHistoryPage.downloadPreviewSecondAxisName()).as("Second Axis Name").isEqualTo("DFM Risk");
        softAssertions.assertThat(costHistoryPage.downloadPreviewWatermarkDisplayed()).as("Verify Watermark Displayed").isTrue();

        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify all iterations included in preview")
            .isEqualTo(10);
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify all iterations included in preview")
                .isEqualTo(defaultGraphIterationNames);

        costHistoryPage.clickDataTableCheckbox();
        softAssertions.assertThat(costHistoryPage.dataTableIterationsList()).as("Verify all iterations in graph shown in table")
                .isEqualTo(defaultGraphIterationNames);

        costHistoryPage.clickBackButton();
        costHistoryPage.setPrimaryAxis("Piece Part Cost");
        costHistoryPage.setSecondaryAxis("Finish Mass");
        costHistoryPage.showHideIteration(2);
        costHistoryPage.showHideIteration(5);
        costHistoryPage.showHideIteration(10);
        costHistoryPage.openDownloadView();

        softAssertions.assertThat(costHistoryPage.downloadPreviewTitle()).as("Verify Preview Title")
            .isEqualTo(castingPart.getComponentName() + " / " + castingPart.getScenarioName() + " Piece Part Cost and Finish Mass");
        softAssertions.assertThat(costHistoryPage.downloadPreviewDate()).as("Verify Displayed Date").isEqualTo(testDate);
        softAssertions.assertThat(costHistoryPage.downloadPreviewFirstAxisName()).as("First Axis Name").isEqualTo("Piece Part Cost");
        softAssertions.assertThat(costHistoryPage.downloadPreviewSecondAxisName()).as("Second Axis Name").isEqualTo("Finish Mass");
        softAssertions.assertThat(costHistoryPage.downloadPreviewWatermarkDisplayed()).as("Verify Watermark Displayed").isTrue();
        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify all iterations included in preview")
            .isEqualTo(7);
        softAssertions.assertThat(costHistoryPage.displayedChartIterations()).as("Verify only shown iterations included in preview")
            .doesNotContain("Iteration 2", "Iteration 5", "Iteration 10");

        costHistoryPage.clickDataTableCheckbox();
        softAssertions.assertThat(costHistoryPage.dataTableIterationsList()).as("Verify only shown iterations in graph shown in table")
            .doesNotContain("Iteration 2", "Iteration 5", "Iteration 10");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28926, 29971, 29972})
    @Description("Verify show/hide buttons only displayed when usable")
    public void testShowHideButtonDisplay() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();
        String notEnoughIterationsMessage = "Historical plots will be available when the scenario has been successfully costed more than once.";
        loginPage =  new CidAppLoginPage(driver);
        costHistoryPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .clickHistory();

        softAssertions.assertThat(costHistoryPage.getPlotAvailableMessage()).as("Verify that message displayed instead of chart")
            .isEqualTo(notEnoughIterationsMessage);
        softAssertions.assertThat(costHistoryPage.showHideIconDisplayed(1)).as("Verify Show/Hide icon not displayed for Iteration 1").isFalse();

        evaluatePage = costHistoryPage.close()
            .selectProcessGroup(component.getProcessGroup())
            .costScenario();

        costHistoryPage = evaluatePage.clickHistory();
        softAssertions.assertThat(costHistoryPage.getPlotAvailableMessage()).as("Verify that message displayed instead of chart")
            .isEqualTo(notEnoughIterationsMessage);
        softAssertions.assertThat(costHistoryPage.showHideIconDisplayed(2)).as("Verify Show/Hide icon not displayed for Iteration 2").isFalse();

        evaluatePage = costHistoryPage.close()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_FINLAND)
            .costScenario();

        costHistoryPage = evaluatePage.clickHistory();
        softAssertions.assertThat(costHistoryPage.displayedChartIterations().size()).as("Verify that chart now displayed").isGreaterThan(0);
        softAssertions.assertThat(costHistoryPage.showHideIconDisplayed(1)).as("Verify Show/Hide icon not displayed for Iteration 1").isFalse();
        softAssertions.assertThat(costHistoryPage.showHideIconDisplayed(2)).as("Verify Show/Hide icon not displayed for Iteration 2").isTrue();
        softAssertions.assertThat(costHistoryPage.showHideIconDisplayed(2)).as("Verify Show/Hide icon not displayed for Iteration 3").isTrue();

        softAssertions.assertAll();
    }
}
