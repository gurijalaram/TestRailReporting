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
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
public class UpgradePartComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.UPGRADE_PART_COMPARISON.getEndpoint();
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.UPGRADE_PART_COMPARISON;
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13952")
    @TestRail(id = 13952)
    @Description("Input controls - Currency code")
    public void testCurrency() {
        ArrayList<String> gbpAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.GBP.getCurrency(), 2);

        ArrayList<String> usdAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.USD.getCurrency(), 2);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14068")
    @TestRail(id = 14068)
    @Description("Input controls - Use Latest Export")
    public void testUseLatestExportInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentRollup = inputControls.getRollup().getOption("ALL PG").getValue();
        String currentPartNumber = inputControls.getPartNumber().getOption("2X1 CAVITY MOLD").getValue();

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsUseLatestExportScenario =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PART_NUMBER.getInputControlId(),
                currentPartNumber,
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsUseLatestExportScenario.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList.get(8).getOptions().get(0).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(8).getOptions().get(0).getLabel()).isEqualTo("Scenario");
        softAssertions.assertThat(inputControlStateArrayList.get(9).getTotalCount()).isEqualTo("12");

        // repeat as above for all
        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsUseLatestExportAll =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PART_NUMBER.getInputControlId(),
                currentPartNumber,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "All",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList2 = inputControlsUseLatestExportAll.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList2.get(8).getOptions().get(1).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList2.get(8).getOptions().get(1).getLabel()).isEqualTo("All");
        softAssertions.assertThat(inputControlStateArrayList2.get(9).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(10).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(10).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(11).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(11).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(12).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(12).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(13).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(14).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(14).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(15).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList2.get(15).getOptions().get(0).getLabel()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(15).getOptions().get(0).getSelected()).isEqualTo(true);

        // repeat as above for no
        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsUseLatestExportNo =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PART_NUMBER.getInputControlId(),
                currentPartNumber,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "No",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList3 = inputControlsUseLatestExportNo.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList3.get(8).getOptions().get(2).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList3.get(8).getOptions().get(2).getLabel()).isEqualTo("No");
        softAssertions.assertThat(inputControlStateArrayList3.get(9).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateArrayList3.get(10).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList3.get(10).getOptions().get(0).getLabel()).isEqualTo("ALL PG");
        softAssertions.assertThat(inputControlStateArrayList3.get(10).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13951")
    @TestRail(id = 13951)
    @Description("Input controls - Rollup")
    public void testRollupInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetOne = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();
        String currentExportSetTwo = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsRollup =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparisonSetTwoExportSets(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetOne,
                currentExportSetTwo
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsRollup.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList.get(9).getTotalCount()).isEqualTo("12");
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
            ExportSetEnum.ALL_PG_CURRENT.getExportSetName(),
            ExportSetEnum.ALL_PG_NEW.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummaryBothPgSetsSelected).isNotEqualTo(null);
        softAssertions.assertThat(jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()).isEqualTo("2X1 CAVITY MOLD");
        String exportSetValueFromChart = jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Export set:").get(6).siblingElements().get(2).text();
        softAssertions.assertThat(exportSetValueFromChart).contains(ExportSetEnum.ALL_PG_CURRENT.getExportSetName());
        softAssertions.assertThat(exportSetValueFromChart).contains(ExportSetEnum.ALL_PG_NEW.getExportSetName());
        softAssertions.assertThat(jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Part number:").get(6).siblingElements().get(2).text()).contains("2X1 CAVITY MOLD");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14002")
    @TestRail(id = 14002)
    @Description("Input controls - Process Group")
    public void testInputControlsProcessGroup() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String processGroupValue = inputControls.getProcessGroup().getOption(ProcessGroupEnum.FORGING.getProcessGroup()).getValue();
        String currentRollup = inputControls.getRollup().getOption("ALL PG").getValue();
        String currentPartNumber = inputControls.getPartNumber().getOption("2X1 CAVITY MOLD").getValue();

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsProcessGroupSelected =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PART_NUMBER.getInputControlId(),
                currentPartNumber,
                InputControlsEnum.PROCESS_GROUP.getInputControlId(),
                processGroupValue
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsProcessGroupSelected.getResponseEntity().getInputControlState();
        InputControlState processGroupIcState = inputControlStateArrayList.get(13);
        softAssertions.assertThat(processGroupIcState.getTotalCount()).isEqualTo("14");
        softAssertions.assertThat(processGroupIcState.getOptions().get(3).getLabel()).isEqualTo(ProcessGroupEnum.FORGING.getProcessGroup());
        softAssertions.assertThat(processGroupIcState.getOptions().get(3).getSelected()).isEqualTo(true);

        InputControlState partNumberIcState = inputControlStateArrayList.get(14);
        softAssertions.assertThat(partNumberIcState.getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(partNumberIcState.getOptions().get(0).getLabel()).isEqualTo("FORGING_GEAR_CARBURIZE");
        softAssertions.assertThat(partNumberIcState.getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14070")
    @TestRail(id = 14070)
    @Description("Input controls - Change Level")
    public void testChangeLevel() {
        // all levels
        ArrayList<String> changeListArrayList = new ArrayList<>(Arrays.asList("High", "Medium", "Low", "No Change"));
        JasperReportSummary jasperReportSummaryAllChangeLevels = jasperApiUtils.genericTestCoreSetChangeLevel(
            changeListArrayList
        );

        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("High, Low, Medium, No Change");
        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isEqualTo(43);
        ArrayList<Element> percentElementsList = jasperReportSummaryAllChangeLevels.getReportHtmlPart().getElementsByAttributeValue("colspan", "4");
        softAssertions.assertThat(percentElementsList.size()).isEqualTo(32);
        softAssertions.assertThat(percentElementsList.get(22).text()).isEqualTo("0%");
        softAssertions.assertThat(percentElementsList.get(28).text()).isEqualTo("0%");
        softAssertions.assertThat(percentElementsList.get(31).text()).isEqualTo("0%");

        // Low change level only selected
        changeListArrayList.clear();
        changeListArrayList.add("Low");
        JasperReportSummary jasperReportSummaryLowChangeLevel = jasperApiUtils.genericTestCoreSetChangeLevel(
            changeListArrayList
        );

        percentElementsList.clear();
        percentElementsList = jasperReportSummaryLowChangeLevel.getReportHtmlPart().getElementsByAttributeValue("colspan", "4");
        softAssertions.assertThat(jasperReportSummaryAllChangeLevels.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isEqualTo(43);
        softAssertions.assertThat(percentElementsList.size()).isEqualTo(29);
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
            changeListArrayList
        );

        percentElementsList.clear();
        percentElementsList = jasperReportSummaryMediumChangeLevel.getReportHtmlPart().getElementsByAttributeValue("colspan", "4");
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isEqualTo(32);
        softAssertions.assertThat(percentElementsList.size()).isEqualTo(2);
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("Medium");
        softAssertions.assertThat(jasperReportSummaryMediumChangeLevel.getReportHtmlPart().toString().contains("0.0%")).isEqualTo(true);

        // High change level only selected
        changeListArrayList.clear();
        changeListArrayList.add("High");
        JasperReportSummary jasperReportSummaryHighChangeLevel = jasperApiUtils.genericTestCoreSetChangeLevel(
            changeListArrayList
        );

        percentElementsList.clear();
        percentElementsList = jasperReportSummaryHighChangeLevel.getReportHtmlPart().getElementsByAttributeValue("colspan", "4");
        softAssertions.assertThat(jasperReportSummaryHighChangeLevel.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isEqualTo(32);
        softAssertions.assertThat(percentElementsList.size()).isEqualTo(2);
        softAssertions.assertThat(jasperReportSummaryHighChangeLevel.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("High");
        softAssertions.assertThat(jasperReportSummaryHighChangeLevel.getReportHtmlPart().toString().contains("0.0%")).isEqualTo(true);

        // No Change change level only selected
        changeListArrayList.clear();
        changeListArrayList.add("No Change");
        JasperReportSummary jasperReportSummaryNoChangeChangeLevel = jasperApiUtils.genericTestCoreSetChangeLevel(
            changeListArrayList
        );

        percentElementsList.clear();
        percentElementsList = jasperReportSummaryNoChangeChangeLevel.getReportHtmlPart().getElementsByAttributeValue("colspan", "4");
        softAssertions.assertThat(jasperReportSummaryNoChangeChangeLevel.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).children().get(1).children().size()).isEqualTo(39);
        softAssertions.assertThat(percentElementsList.size()).isEqualTo(23);
        softAssertions.assertThat(jasperReportSummaryNoChangeChangeLevel.getReportHtmlPart().getElementsContainingText("Change Level:").get(6).siblingElements().get(6).child(0).text()).isEqualTo("No Change");
        softAssertions.assertThat(jasperReportSummaryNoChangeChangeLevel.getReportHtmlPart().toString().contains("0%")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14072")
    @TestRail(id = 14072)
    @Description("Input controls - Cost Metrics Low and High Thresholds")
    public void testCostMetricsLowAndHighThresholds() {
        // revisit to understand what changes and assert on something in chart
        JasperReportSummary jasperReportSummaryCostMetricThresholds = jasperApiUtils.genericTestCoreSetCostMetricOrTimeMetricsThresholdLevels(
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
    @TmsLink("14076")
    @TestRail(id = 14076)
    @Description("Input controls - Time Metrics Low and High Thresholds")
    public void testTimeMetricsLowAndHighThresholds() {
        // revisit to understand what changes and assert on something in chart
        JasperReportSummary jasperReportSummaryCostMetricThresholds = jasperApiUtils.genericTestCoreSetCostMetricOrTimeMetricsThresholdLevels(
            false,
            "0",
            "40"
        );

        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Time Metrics High Threshold:").get(6).siblingElements().get(14).child(0).text()).isEqualTo("40%");
        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Time Metrics Med Threshold:").get(6).siblingElements().get(14).child(0).text()).isEqualTo("0%");
        softAssertions.assertThat(jasperReportSummaryCostMetricThresholds.getReportHtmlPart().getElementsContainingText("Time Metrics Low Threshold:").get(6).siblingElements().get(14).child(0).text()).isEqualTo("0%");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2399")
    @TestRail(id = 2399)
    @Description("Verify 'Part Number' drop-down works correctly")
    public void testPartNumberDropdownWorksCorrectly() {
        String partNumberToSet = "3DPRINTING";
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericPartNumberUpgradePartComparisonTest(partNumberToSet);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()).isEqualTo(partNumberToSet);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Part number:").get(6).siblingElements().get(2).text()).contains(partNumberToSet);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13953")
    @TestRail(id = 13953)
    @Description("Input controls - Date ranges (Earliest and Latest export date)")
    public void testDateRangeFunctionality() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsSetEarliestExportDate =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId(),
                currentDateTime,
                ""
            );

        softAssertions.assertThat(inputControlsSetEarliestExportDate.getResponseEntity().getInputControlState().get(9).getTotalCount())
            .isEqualTo("0");

        softAssertions.assertAll();
    }
}
