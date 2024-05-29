package com.apriori.cir.ui.tests.ootbreports.newreportstests.upgradecomparison;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemUpgradeComparison;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemUpgradePartComparison;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class UpgradeComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.UPGRADE_COMPARISON.getEndpoint();
    // Export set name is not relevant for this report
    private String exportSetName = "";
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.UPGRADE_COMPARISON;
    private final SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13944")
    @TestRail(id = 13944)
    @Description("Input controls - Currency code")
    public void testCurrency() {
        ArrayList<String> gbpAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.GBP.getCurrency(), 3);

        ArrayList<String> usdAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.USD.getCurrency(), 3);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13654")
    @TestRail(id = 13654)
    @Description("Validate report accessibility")
    public void testReportAccessibility() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.GBP.getCurrency());

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "23").get(0).child(0).text()
            .equals("Upgrade Comparison")).isEqualTo(true);
        softAssertions.assertThat(!jasperReportSummary.getChartData().get(0).getChartDataPoints().isEmpty()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13656")
    @TestRail(id = 13656)
    @Description("Validate report display")
    public void testReportDisplay() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.GBP.getCurrency());

        softAssertions.assertThat(jasperReportSummary).isNotEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()).isEqualTo("ALL PG");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "highcharts_parent_container")
            .toString().contains("div")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByTag("img").get(0).attributes().get("style")
            .contains("width: 280px")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(13).child(0)
            .child(0).text()).isEqualTo("2X1 CAVITY MOLD");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(228).child(0)
            .text()).isEqualTo("-33%");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13657")
    @TestRail(id = 13657)
    @Description("Validate report details align with aP Pro / CID")
    public void validateReportDetailsAlignWithApProAndCID() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.GBP.getCurrency());

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "4").get(8)
            .child(0).text()).isEqualTo("14.31");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(11)
            .child(0).text()).isEqualTo("13.96");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2374")
    @TestRail(id = 2374)
    @Description("Verify percentage difference calculations are correct")
    public void verifyPercentDiffCalculationsAreCorrect() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.GBP.getCurrency());

        ArrayList<Element> colSpanTwoElements = jasperApiUtils.getElementsByColumnSpan(jasperReportSummary, "2");
        ArrayList<Element> colSpanThreeElements = jasperApiUtils.getElementsByColumnSpan(jasperReportSummary, "3");
        ArrayList<Element> colSpanFourElements = jasperApiUtils.getElementsByColumnSpan(jasperReportSummary, "4");
        ArrayList<Element> colSpanFiveElements = jasperApiUtils.getElementsByColumnSpan(jasperReportSummary, "5");

        // FBC % diff calculation
        Double fbcOldValue = Double.parseDouble((colSpanFourElements.get(7).text()));
        Double fbcNewValue = Double.parseDouble((colSpanThreeElements.get(8).text()));
        String actualFbcPercentDiffValue = colSpanTwoElements.get(10).text().replace("%", "");
        softAssertions.assertThat(jasperApiUtils.getExpectedPercentDiffValue(fbcOldValue, fbcNewValue)).startsWith(actualFbcPercentDiffValue);

        // PPC % diff calculation
        Double ppcOldValue = Double.parseDouble((colSpanFourElements.get(8).text()));
        Double ppcNewValue = Double.parseDouble((colSpanTwoElements.get(11).text()));
        String actualPpcPercentDiffValue = colSpanTwoElements.get(12).text().replace("%", "");
        softAssertions.assertThat(jasperApiUtils.getExpectedPercentDiffValue(ppcOldValue, ppcNewValue)).startsWith(actualPpcPercentDiffValue);

        // Hard Tooling Cost % diff calculation
        Double htcOldValue = Double.parseDouble((colSpanFourElements.get(33).text().replace(",", "")));
        Double htcNewValue = Double.parseDouble((colSpanTwoElements.get(79).text().replace(",", "")));
        String actualHtcPercentDiffValue = colSpanTwoElements.get(80).text().replace("%", "");
        softAssertions.assertThat(jasperApiUtils.getExpectedPercentDiffValue(htcOldValue, htcNewValue)).startsWith(actualHtcPercentDiffValue);

        // Material Cost % diff calculation
        Double mcOldValue = Double.parseDouble((colSpanFourElements.get(54).text()));
        Double mcNewValue = Double.parseDouble((colSpanThreeElements.get(64).text()));
        String actualMcPercentDiffValue = colSpanTwoElements.get(136).text().replace("%", "");
        softAssertions.assertThat(jasperApiUtils.getExpectedPercentDiffValue(mcOldValue, mcNewValue)).startsWith(actualMcPercentDiffValue);

        // Total Cycle Time % diff calculation
        Double ctOldValue = Double.parseDouble((colSpanFiveElements.get(71).text()));
        Double ctNewValue = Double.parseDouble((colSpanTwoElements.get(225).text()));
        String actualCtPercentDiffValue = colSpanThreeElements.get(105).text().replace("%", "");
        softAssertions.assertThat(jasperApiUtils.getExpectedPercentDiffValue(ctOldValue, ctNewValue)).startsWith(actualCtPercentDiffValue);

        // Total Labour Time % diff calculation
        Double labourTimeOldValue = Double.parseDouble((colSpanFiveElements.get(42).text()));
        Double labourTimeNewValue = Double.parseDouble((colSpanThreeElements.get(56).text()));
        String actualLabourTimePercentDiffValue = colSpanTwoElements.get(116).text().replace("%", "");
        softAssertions.assertThat(jasperApiUtils.getExpectedPercentDiffValue(labourTimeOldValue, labourTimeNewValue)).startsWith(actualLabourTimePercentDiffValue);

        // Batch Setup Time % diff calculation
        Double batchSetupTimeOldValue = Double.parseDouble((colSpanTwoElements.get(40).text()));
        Double batchSetupTimeNewValue = Double.parseDouble((colSpanThreeElements.get(22).text()));
        String actualBatchSetupTimePercentDiffValue = colSpanTwoElements.get(41).text().replace("%", "");
        softAssertions.assertThat(jasperApiUtils.getExpectedPercentDiffValue(batchSetupTimeOldValue, batchSetupTimeNewValue)).startsWith(actualBatchSetupTimePercentDiffValue);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2379")
    @TestRail(id = 2379)
    @Description("Verify box and whisker chart diagram is present and easily understood")
    public void verifyBoxAndWhiskerChartIsPresent() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.USD.getCurrency());

        // The two assertions below verify the present of the chart
        // To verify that it is easily understood you'd need either a UI test or a manual test (not really suitable for programmatic testing)
        Attributes imageAttributeList = jasperReportSummary.getReportHtmlPart().getElementsByTag("img").get(0).attributes();
        softAssertions.assertThat(imageAttributeList.get("style")).isEqualTo("width: 280px");
        softAssertions.assertThat(imageAttributeList.get("src")).endsWith(".png");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14080")
    @TestRail(id = 14080)
    @Description("Report display - validate costing status")
    public void reportDisplayValidateCostingStatus() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.USD.getCurrency());

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("CASE_06_ARVINMERITOR_321_ASCAST")
            .size()).isEqualTo(0);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("CASE_06_ARVINMERITOR_321_ASMACHINED")
            .size()).isEqualTo(0);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("HYDROFORM SAMPLE TOPHAT 2")
            .size()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13999")
    @TestRail(id = 13999)
    @Description("Input controls - Use Latest Export")
    public void inputControlsUseLatestExport() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentRollup = inputControls.getRollup().getOption("ALL PG").getValue();

        // Scenario
        ResponseWrapper<UpdatedInputControlsRootItemUpgradeComparison> inputControlsUseLatestExportScenario =
            jasperReportUtil.getInputControlsModifiedUpgradeComparison(
                UpdatedInputControlsRootItemUpgradeComparison.class,
                false,
                ReportNamesEnum.UPGRADE_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                ""
            );

        InputControlState useLatestExportDateScenarioIcState = inputControlsUseLatestExportScenario.getResponseEntity().getInputControlState().get(8);
        softAssertions.assertThat(useLatestExportDateScenarioIcState.getTotalCount()).isEqualTo("3");
        softAssertions.assertThat(useLatestExportDateScenarioIcState.getOptions().get(0).getLabel()).isEqualTo("Scenario");
        softAssertions.assertThat(useLatestExportDateScenarioIcState.getOptions().get(0).getSelected()).isEqualTo(true);
        softAssertions.assertThat(Integer.parseInt(inputControlsUseLatestExportScenario.getResponseEntity().getInputControlState().get(9).getTotalCount())).isGreaterThan(0);

        // All
        ResponseWrapper<UpdatedInputControlsRootItemUpgradeComparison> inputControlsUseLatestExportAll =
            jasperReportUtil.getInputControlsModifiedUpgradeComparison(
                UpdatedInputControlsRootItemUpgradeComparison.class,
                false,
                ReportNamesEnum.UPGRADE_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "All",
                ""
            );

        InputControlState useLatestExportDateAllIcState = inputControlsUseLatestExportScenario.getResponseEntity().getInputControlState().get(8);
        softAssertions.assertThat(useLatestExportDateAllIcState.getTotalCount()).isEqualTo("3");
        softAssertions.assertThat(useLatestExportDateAllIcState.getOptions().get(1).getLabel()).isEqualTo("All");
        softAssertions.assertThat(useLatestExportDateAllIcState.getOptions().get(1).getSelected()).isEqualTo(true);
        softAssertions.assertThat(Integer.parseInt(inputControlsUseLatestExportAll.getResponseEntity().getInputControlState().get(9).getTotalCount())).isEqualTo(0);

        // No
        ResponseWrapper<UpdatedInputControlsRootItemUpgradeComparison> inputControlsUseLatestExportNo =
            jasperReportUtil.getInputControlsModifiedUpgradeComparison(
                UpdatedInputControlsRootItemUpgradeComparison.class,
                false,
                ReportNamesEnum.UPGRADE_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "No",
                ""
            );

        InputControlState useLatestExportDateNoIcState = inputControlsUseLatestExportScenario.getResponseEntity().getInputControlState().get(8);
        softAssertions.assertThat(useLatestExportDateNoIcState.getTotalCount()).isEqualTo("3");
        softAssertions.assertThat(useLatestExportDateNoIcState.getOptions().get(2).getLabel()).isEqualTo("No");
        softAssertions.assertThat(useLatestExportDateNoIcState.getOptions().get(2).getSelected()).isEqualTo(true);
        softAssertions.assertThat(Integer.parseInt(inputControlsUseLatestExportNo.getResponseEntity().getInputControlState().get(9).getTotalCount())).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13947")
    @TestRail(id = 13947)
    @Description("Input controls - Date ranges (Earliest and Latest export date)")
    public void verifyDateRangeInputControlsFunctionality() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsSetEarliestExportDate =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_COMPARISON.getReportName(),
                InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId(),
                currentDateTime,
                ""
            );

        softAssertions.assertThat(inputControlsSetEarliestExportDate.getResponseEntity().getInputControlState().get(9).getTotalCount())
            .isEqualTo("0");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13948")
    @TestRail(id = 13948)
    @Description("Input controls - Rollup")
    public void inputControlsRollupFunctionality() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetOne = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();
        String currentExportSetTwo = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsRollup =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparisonSetTwoExportSets(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                ReportNamesEnum.UPGRADE_COMPARISON.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetOne,
                currentExportSetTwo
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsRollup.getResponseEntity().getInputControlState();
        softAssertions.assertThat(Integer.parseInt(inputControlStateArrayList.get(9).getTotalCount())).isGreaterThanOrEqualTo(12);
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(0).getLabel()).isEqualTo(
            ExportSetEnum.ALL_PG_CURRENT.getExportSetName()
        );
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(0).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(1).getLabel()).isEqualTo(
            ExportSetEnum.ALL_PG_NEW.getExportSetName()
        );
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(1).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(10).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getLabel()).isEqualTo(
            RollupEnum.ALL_PG.getRollupName()
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getSelected()).isEqualTo(false);

        JasperReportSummary jasperReportSummaryBothPgSetsSelected = jasperApiUtils.genericTestCoreSetTwoExportSetsAndAllPgRollup(
            false,
            ExportSetEnum.ALL_PG_CURRENT.getExportSetName(),
            ExportSetEnum.ALL_PG_NEW.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()).isEqualTo("ALL PG");
        String exportSetValueFromChart = jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Export set:").get(6).siblingElements().get(2).text();
        softAssertions.assertThat(exportSetValueFromChart).contains(ExportSetEnum.ALL_PG_CURRENT.getExportSetName());
        softAssertions.assertThat(exportSetValueFromChart).contains(ExportSetEnum.ALL_PG_NEW.getExportSetName());

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14001")
    @TestRail(id = 14001)
    @Description("Input controls - Process Group")
    public void inputControlsProcessGroupFunctionality() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String processGroupValue = inputControls.getProcessGroup().getOption(ProcessGroupEnum.FORGING.getProcessGroup()).getValue();
        String currentRollup = inputControls.getRollup().getOption("ALL PG").getValue();

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsProcessGroupSelected =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PROCESS_GROUP.getInputControlId(),
                processGroupValue
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsProcessGroupSelected.getResponseEntity().getInputControlState();
        InputControlState processGroupIcState = inputControlStateArrayList.get(13);
        softAssertions.assertThat(processGroupIcState.getTotalCount()).isEqualTo("14");
        softAssertions.assertThat(processGroupIcState.getOptions().get(3).getLabel()).isEqualTo(ProcessGroupEnum.FORGING.getProcessGroup());
        softAssertions.assertThat(processGroupIcState.getOptions().get(3).getSelected()).isEqualTo(true);

        String forgingProcessGroup = ProcessGroupEnum.FORGING.getProcessGroup();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreProcessGroupOnlyUpgradeComparisonTests(forgingProcessGroup);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Process Group:").get(6).siblingElements().get(6).text()).isEqualTo(forgingProcessGroup);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("FORGING_GEAR_CARBURIZE")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14069")
    @TestRail(id = 14069)
    @Description("Input controls - Change Level")
    public void verifyInputControlsChangeLevel() {
        // all levels
        ArrayList<String> changeListArrayList = new ArrayList<>(Arrays.asList("High", "Medium", "Low", "No Change"));
        JasperReportSummary jasperReportSummaryAllChangeLevels = jasperApiUtils.genericTestCoreSetChangeLevel(
            false,
            changeListArrayList
        );

        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("High, Low, Medium, No Change");
        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isGreaterThanOrEqualTo(43);
        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().toString().contains("300%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().toString().contains("-11%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().toString().contains("-3%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().toString().contains("0%")).isEqualTo(true);

        // Low change level only selected
        changeListArrayList.clear();
        changeListArrayList.add("Low");
        JasperReportSummary jasperReportSummaryLowChangeLevel = jasperApiUtils.genericTestCoreSetChangeLevel(
            false,
            changeListArrayList
        );

        softAssertions.assertThat(jasperReportSummaryLowChangeLevel.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isEqualTo(29);
        softAssertions.assertThat(jasperReportSummaryLowChangeLevel.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("Low");
        softAssertions.assertThat(jasperReportSummaryLowChangeLevel.getReportHtmlPart().toString().contains("0%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryLowChangeLevel.getReportHtmlPart().toString().contains("-0%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryLowChangeLevel.getReportHtmlPart().toString().contains("-1%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryLowChangeLevel.getReportHtmlPart().toString().contains("-2%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryLowChangeLevel.getReportHtmlPart().toString().contains("-4%")).isEqualTo(true);

        // Medium change level only selected
        changeListArrayList.clear();
        changeListArrayList.add("Medium");
        JasperReportSummary jasperReportSummaryMediumChangeLevel = jasperApiUtils.genericTestCoreSetChangeLevel(
            false,
            changeListArrayList
        );

        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isGreaterThanOrEqualTo(26);
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("Medium");
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().toString().contains("-17%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().toString().contains("-5%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().toString().contains("-3%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().toString().contains("0%")).isEqualTo(true);

        // High change level only selected
        changeListArrayList.clear();
        changeListArrayList.add("High");
        JasperReportSummary jasperReportSummaryHighChangeLevel = jasperApiUtils.genericTestCoreSetChangeLevel(
            false,
            changeListArrayList
        );

        softAssertions.assertThat(jasperReportSummaryHighChangeLevel.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isEqualTo(34);
        softAssertions.assertThat(jasperReportSummaryHighChangeLevel.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("High");
        softAssertions.assertThat(jasperReportSummaryHighChangeLevel.getReportHtmlPart().toString().contains("300%")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryHighChangeLevel.getReportHtmlPart().toString().contains("-23%")).isEqualTo(true);

        // No Change change level only selected
        changeListArrayList.clear();
        changeListArrayList.add("No Change");
        JasperReportSummary jasperReportSummaryNoChangeChangeLevel = jasperApiUtils.genericTestCoreSetChangeLevel(
            false,
            changeListArrayList
        );

        softAssertions.assertThat(jasperReportSummaryNoChangeChangeLevel.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isGreaterThanOrEqualTo(18);
        softAssertions.assertThat(jasperReportSummaryNoChangeChangeLevel.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("No Change");
        softAssertions.assertThat(jasperReportSummaryNoChangeChangeLevel.getReportHtmlPart().toString().contains(">0%")).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14071")
    @TestRail(id = 14071)
    @Description("Input controls - Cost Metrics Low and High Thresholds")
    public void testCostMetricsLowAndHighThresholds() {
        // TODO: revisit to understand what changes (not clear) and assert on something in chart
        JasperReportSummary jasperReportSummaryCostMetricThresholds = jasperApiUtils.genericTestCoreSetCostMetricOrTimeMetricsThresholdLevels(
            false,
            true,
            "0",
            "40"
        );

        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Cost Metrics High Threshold:").get(6).siblingElements().get(10).child(0).text()).isEqualTo("40%");
        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Cost Metrics Med Threshold:").get(6).siblingElements().get(10).child(0).text()).isEqualTo("0%");
        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Cost Metrics Low Threshold:").get(6).siblingElements().get(10).child(0).text()).isEqualTo("0%");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14075")
    @TestRail(id = 14075)
    @Description("Input controls - Time Metrics Low and High Thresholds")
    public void testTimeMetricsLowAndHighThresholds() {
        // TODO: revisit to understand what changes (not clear) and assert on something in chart
        JasperReportSummary jasperReportSummaryCostMetricThresholds = jasperApiUtils.genericTestCoreSetCostMetricOrTimeMetricsThresholdLevels(
            false,
            false,
            "0",
            "40"
        );

        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Time Metrics High Threshold:").get(6).siblingElements().get(14).child(0).text()).isEqualTo("40%");
        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Time Metrics Med Threshold:").get(6).siblingElements().get(14).child(0).text()).isEqualTo("0%");
        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Time Metrics Low Threshold:").get(6).siblingElements().get(14).child(0).text()).isEqualTo("0%");

        softAssertions.assertAll();
    }
}
