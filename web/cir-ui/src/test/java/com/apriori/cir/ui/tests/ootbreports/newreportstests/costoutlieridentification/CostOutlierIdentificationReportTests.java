package com.apriori.cir.ui.tests.ootbreports.newreportstests.costoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawData;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.ReportComponentsResponse;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemCostOutlierIdentification;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblycost.GenericAssemblyCostTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.testrail.TestRail;

import com.google.common.base.Stopwatch;
import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CostOutlierIdentificationReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.COST_OUTLIER_IDENTIFICATION;
    private SoftAssertions softAssertions = new SoftAssertions();
    private Document genericHtmlResponse;
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13925")
    @TestRail(id = 13925)
    @Description("Input controls - Cost Metric - FBC")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList(InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName())
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29651")
    @TestRail(id = 29651)
    @Description("Input controls - Cost Metric - PPC")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList(InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.PIECE_PART_COST.getCostMetricName())
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1958")
    @TestRail(id = 1958)
    @Description("Percent difference threshold filter works - main report")
    public void testAnnualisedPotentialSavingsThresholdFilter() {
        JasperReportSummaryIncRawDataAsString jasperReportSummary = jasperApiUtils.genericTestCoreRawAsString(
            InputControlsEnum.PERCENT_DIFFERENCE_THRESHOLD.getInputControlId(),
            "100"
        );

        Document jasperReportSummaryHtml = jasperReportSummary.getReportHtmlPart();
        softAssertions.assertThat(jasperReportSummaryHtml.getElementsContainingText("Annualized Potential Savings Threshold:")
            .get(6).siblingElements()
            .get(1).children()
            .get(0).text()
        ).isEqualTo("n/a");
        softAssertions.assertThat(jasperReportSummaryHtml.getElementsContainingText("Percent Difference Threshold:")
            .get(6).siblingElements()
            .get(6).children()
            .get(0).text()
        ).isEqualTo("100.0%");

        softAssertions.assertThat(jasperApiUtils.getChartUuidCount(jasperReportSummary.getChartDataRawAsString())).isEqualTo(2);
        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1956")
    @TestRail(id = 1956)
    @Description("Min & Max costs filter works")
    public void testMinMaxAprioriCost() {
        jasperApiUtils.genericTestCore(
            InputControlsEnum.COMPONENT_COST_MIN.getInputControlId(),
            "1"
        );

        JasperReportSummary jasperReportSummaryBothCostValuesSet = jasperApiUtils.genericTestCore(
            InputControlsEnum.COMPONENT_COST_MAX.getInputControlId(),
            "9"
        );

        JasperReportSummaryIncRawDataAsString jasperReportSummaryBothCostValuesSetRawString = jasperApiUtils.genericTestCoreRawAsString(
            "",
            ""
        );

        softAssertions.assertThat(getCostMinOrMaxValue(jasperReportSummaryBothCostValuesSet, "Min")).isEqualTo("1.00");
        softAssertions.assertThat(getCostMinOrMaxValue(jasperReportSummaryBothCostValuesSet, "Max")).isEqualTo("9.00");

        String chartDataRaw = jasperReportSummaryBothCostValuesSetRawString.getChartDataRawAsString();

        softAssertions.assertThat(jasperApiUtils.getChartUuidCount(chartDataRaw)).isEqualTo(2);

        softAssertions.assertThat(chartDataRaw.contains(
            JasperCirApiPartsEnum.SM_CLEVIS_2207240161.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(chartDataRaw.contains(
            JasperCirApiPartsEnum.DASHBOARD_PART2.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(chartDataRaw.contains(
            JasperCirApiPartsEnum.DASHBOARD_PART1.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(chartDataRaw.contains("1.58")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("8.58")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("5.81")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("28473")
    @TestRail(id = 28473)
    @Description("Validate report details align with aP Pro / CID")
    public void validateReportDetailsAlignWithApProAndCID() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);

        InputControl inputControl = jasperReportUtil.getInputControls(reportsNameForInputControls);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(),
            CurrencyEnum.USD.getCurrency());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControl.getExportSetName()
                .getOption(ExportSetEnum.SHEET_METAL_DTC.getExportSetName()).getValue());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId(),
            inputControl.getRollup()
                .getOption(RollupEnum.SHEET_METAL_DTC.getRollupName()).getValue());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        JasperReportSummaryIncRawDataAsString jasperReportSummaryIncRawDataAsString = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());

        String chartDataRaw = jasperReportSummaryIncRawDataAsString.getChartDataRawAsString();
        softAssertions.assertThat(chartDataRaw.contains("188724.64")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("14199846.01")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("-14011121.37")).isEqualTo(true);

        softAssertions.assertThat(chartDataRaw.contains("12.58")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("946.66")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("-7424.11")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7569")
    @TestRail(id = 7569)
    @Description("Validate potential savings calculation")
    public void validatePotentialSavingsCalculation() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);

        InputControl inputControl = jasperReportUtil.getInputControls(reportsNameForInputControls);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(),
            CurrencyEnum.USD.getCurrency());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControl.getExportSetName()
                .getOption(ExportSetEnum.SHEET_METAL_DTC.getExportSetName()).getValue());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId(),
            inputControl.getRollup()
                .getOption(RollupEnum.SHEET_METAL_DTC.getRollupName()).getValue());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        JasperReportSummaryIncRawDataAsString jasperReportSummaryIncRawDataAsString = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());

        String chartDataRaw = jasperReportSummaryIncRawDataAsString.getChartDataRawAsString();

        softAssertions.assertThat(chartDataRaw.contains(":3.2,")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("2.12")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("12.58")).isEqualTo(true);
        softAssertions.assertThat(chartDataRaw.contains("946.66")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7570")
    @TestRail(id = 7570)
    @Description("Validate percent difference calculation")
    public void validatePercentDifferenceCalculation() {
        // Cost Outlier Identification
        String chartDataRaw = genericCostOutlierReportGeneration(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());

        double quotedCostOne = 3.2;
        double aprioriCostOne = 2.12;
        double firstTwoSumsResultOne = (quotedCostOne - aprioriCostOne) * 100;
        double thirdSumResultOne = firstTwoSumsResultOne / quotedCostOne;

        softAssertions.assertThat(chartDataRaw.contains(Double.toString(thirdSumResultOne).substring(0, 3))).isEqualTo(true);

        double firstTwoSumsResultTwo = (0.26 - 0.39) * 100;
        double thirdSumResultTwo = firstTwoSumsResultTwo / 0.26;

        softAssertions.assertThat(chartDataRaw.contains(Double.toString(thirdSumResultTwo).substring(0, 2))).isEqualTo(true);

        // Cost Outlier Identification Details
        genericCostOutlierReportGeneration(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName());

        double quotedCostThree = Double.parseDouble(genericHtmlResponse.getElementsByAttributeValue("colspan", "4").get(5).child(0).text());
        double aprioriCostThree = Double.parseDouble(genericHtmlResponse.getElementsByAttributeValue("colspan", "4").get(6).child(0).text());
        String expectedPercentDifferenceCostThree = genericHtmlResponse.getElementsByAttributeValue("colspan", "2").get(16).child(0).text();
        double firstTwoSumsResultThree = (quotedCostThree - aprioriCostThree) * 100;
        double thirdSumResultThree = firstTwoSumsResultThree / quotedCostThree;

        softAssertions.assertThat(Double.toString(thirdSumResultThree)).startsWith(expectedPercentDifferenceCostThree.substring(0, 3));

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7571")
    @TestRail(id = 7571)
    @Description("Validate annualised potential savings calculation")
    public void validateAnnualisedPotentialSavingsCalculation() {
        // Cost Outlier Identification
        String chartDataRaw = genericCostOutlierReportGeneration(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());

        double potentialSavings = 3.2 - 2.12;
        double annualisedPotentialSavings = potentialSavings * 190000;

        softAssertions.assertThat(Double.toString(annualisedPotentialSavings)).startsWith("205");
        softAssertions.assertThat(chartDataRaw.contains("205857.23")).isEqualTo(true);

        // Cost Outlier Identification Details
        genericCostOutlierReportGeneration(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName());
        ArrayList<Element> colSpanTwoElementList = genericHtmlResponse.getElementsByAttributeValue("colspan", "2");

        double annualVolumeTwo = Double.parseDouble(colSpanTwoElementList.get(122).child(0).text().replace(",", ""));
        double potentialSavingsTwo = Double.parseDouble(colSpanTwoElementList.get(123).child(0).text().substring(1, 5));
        double actualAnnualisedPotentialSavings = Double.parseDouble(colSpanTwoElementList.get(125).child(0).text().substring(1, 10).replace(",", ""));
        double expectedAnnualisedPotentialSavings = annualVolumeTwo * potentialSavingsTwo;

        softAssertions.assertThat(Double.toString(expectedAnnualisedPotentialSavings)).startsWith(Double.toString(actualAnnualisedPotentialSavings).substring(0, 2));
        softAssertions.assertThat(areValuesAlmostEqual(expectedAnnualisedPotentialSavings, actualAnnualisedPotentialSavings)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14031")
    @TestRail(id = 14031)
    @Description("Input controls - Use Latest Export")
    public void testUseLatestExportInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentRollup = inputControls.getRollup().getOption("QA TEST 1 (Base)").getValue();
        String currentExportSet = inputControls.getExportSetName()
            .getOption(ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemCostOutlierIdentification> inputControlsUseLatestExportScenario =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCostOutlierIdentification.class,
                false,
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSet,
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsUseLatestExportScenario.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(0).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(0).getLabel()).isEqualTo("Scenario");
        softAssertions.assertThat(inputControlStateArrayList.get(10).getTotalCount()).isEqualTo("12");

        // repeat as above for all
        ResponseWrapper<UpdatedInputControlsRootItemCostOutlierIdentification> inputControlsUseLatestExportAll =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCostOutlierIdentification.class,
                false,
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                exportSetName,
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "All",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList2 = inputControlsUseLatestExportAll.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList2.get(9).getOptions().get(1).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList2.get(9).getOptions().get(1).getLabel()).isEqualTo("All");
        softAssertions.assertThat(inputControlStateArrayList2.get(10).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(11).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(11).getError()).isEqualTo("This field is mandatory so you must enter data.");

        // repeat as above for no
        ResponseWrapper<UpdatedInputControlsRootItemCostOutlierIdentification> inputControlsUseLatestExportNo =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCostOutlierIdentification.class,
                false,
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                exportSetName,
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "No",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList3 = inputControlsUseLatestExportNo.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList3.get(9).getOptions().get(2).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList3.get(9).getOptions().get(2).getLabel()).isEqualTo("No");
        softAssertions.assertThat(inputControlStateArrayList3.get(10).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateArrayList3.get(11).getTotalCount()).isEqualTo("16");
        softAssertions.assertThat(inputControlStateArrayList3.get(11).getOptions().get(0).getLabel()).isEqualTo(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName().concat(" (value tracking)"));
        softAssertions.assertThat(inputControlStateArrayList3.get(11).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13958")
    @TestRail(id = 13958)
    @Description("Input controls - Date ranges (Earliest and Latest export date)")
    public void testDateRangesInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        ResponseWrapper<UpdatedInputControlsRootItemCostOutlierIdentification> inputControlsSetEarliestExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCostOutlierIdentification.class,
                false,
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId(),
                currentDateTime,
                ""
            );

        softAssertions.assertThat(inputControlsSetEarliestExportDate.getResponseEntity().getInputControlState().get(10).getTotalCount())
            .isEqualTo("0");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13922")
    @TestRail(id = 13922)
    @Description("Input controls - Rollup")
    public void testRollupInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemCostOutlierIdentification> inputControlsRollup =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCostOutlierIdentification.class,
                false,
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetValue,
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsRollup.getResponseEntity().getInputControlState();
        String costOutlierExportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
        softAssertions.assertThat(Integer.parseInt(inputControlStateArrayList.get(10).getTotalCount())).isGreaterThanOrEqualTo(12);
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(costOutlierExportSetName).getLabel()).isEqualTo(
            costOutlierExportSetName
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(costOutlierExportSetName).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(costOutlierExportSetName).getLabel()).isEqualTo(
            costOutlierExportSetName
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(costOutlierExportSetName).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(11).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(11).getOptions().get(0).getLabel()).isEqualTo(
            RollupEnum.QA_TEST_ONE.getRollupName().concat(" (Base)")
        );
        softAssertions.assertThat(inputControlStateArrayList.get(11).getOptions().get(0).getSelected()).isEqualTo(true);

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()
        ).isEqualTo(RollupEnum.QA_TEST_ONE.getRollupName());

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13926")
    @TestRail(id = 13926)
    @Description("Input controls - Currency Code")
    public void testCurrencyCode() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        String currentExportSet = jasperReportUtil.getInputControls(reportsNameForInputControls)
            .getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);

        JasperReportSummaryIncRawDataAsString jasperReportSummaryGBP = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.USD.getCurrency());
        JasperReportSummaryIncRawDataAsString jasperReportSummaryUSD = jasperReportUtil.generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());

        String currencyValueGBP = getCurrencyFromHTML(jasperReportSummaryGBP);
        String currencyValueUSD = getCurrencyFromHTML(jasperReportSummaryUSD);

        String gbpChartDataRaw = jasperReportSummaryGBP.getChartDataRawAsString();
        softAssertions.assertThat(gbpChartDataRaw.contains("10467.3")).isEqualTo(true);
        softAssertions.assertThat(gbpChartDataRaw.contains("0.36")).isEqualTo(true);
        softAssertions.assertThat(gbpChartDataRaw.contains("205857.23")).isEqualTo(false);
        softAssertions.assertThat(gbpChartDataRaw.contains("0.48")).isEqualTo(false);

        String usdChartDataRaw = jasperReportSummaryUSD.getChartDataRawAsString();
        softAssertions.assertThat(usdChartDataRaw.contains("205857.23")).isEqualTo(true);
        softAssertions.assertThat(usdChartDataRaw.contains("0.48")).isEqualTo(true);
        softAssertions.assertThat(usdChartDataRaw.contains("0.36")).isEqualTo(false);
        softAssertions.assertThat(usdChartDataRaw.contains("0.36")).isEqualTo(false);

        softAssertions.assertThat(currencyValueGBP).isNotEqualTo(currencyValueUSD);
        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14030")
    @TestRail(id = 14030)
    @Description("Input controls - Sort Order")
    public void testSortOrderInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        String currentExportSet = jasperReportUtil.getInputControls(reportsNameForInputControls)
            .getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.SORT_ORDER.getInputControlId(), "Percent Difference");

        JasperReportSummaryIncRawDataAsString jasperReportSummary = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());

        softAssertions.assertThat(jasperReportSummary.getChartDataRawAsString().contains("Percent Difference")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getChartDataRawAsString()
                .replace("\"", "")
                .contains("xCategories:[-12,-18_1,-_4[1],SM_CLEVIS_2207240161,-_4[3],TAPE_HUB,DASHBOARD_PART2,DASHBOARD_PART3,".concat(
                    "AIR_FILTER_COVER,DASHBOARD_PART1]")))
            .isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("28472")
    @TestRail(id = 28472)
    @Description("Input controls - Minimum and Maximum aPriori Cost")
    public void testMinMaxAprioriCostInputControls() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        String currentExportSet = jasperReportUtil.getInputControls(reportsNameForInputControls)
            .getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.USD.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.COMPONENT_COST_MIN.getInputControlId(), "0.3");
        jasperApiUtils.setReportParameterByName(InputControlsEnum.COMPONENT_COST_MAX.getInputControlId(), "11.4");

        JasperReportSummaryIncRawDataAsString jasperReportSummary = jasperReportUtil.generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());
        String chartDataRawNoQuotes = jasperReportSummary.getChartDataRawAsString()
            .replace("\"", "");

        softAssertions.assertThat(
            chartDataRawNoQuotes.contains("xCategories:[SM_CLEVIS_2207240161,AIR_FILTER_COVER,TAPE_HUB,DASHBOARD_PART1]")
        ).isEqualTo(true);

        softAssertions.assertThat(
            chartDataRawNoQuotes.contains("componentCostMin:0.3,componentCostMax:11.4")
        ).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("28474")
    @TestRail(id = 28474)
    @Description("Input controls - Annualised potential savings threshold")
    public void testAnnualisedPotentialSavingsThresholdInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        String currentExportSet = jasperReportUtil.getInputControls(reportsNameForInputControls)
            .getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.USD.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.ANNUALIZED_POTENTIAL_THRESHOLD.getInputControlId(), "205000");

        JasperReportSummaryIncRawDataAsString jasperReportSummary = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());
        String chartDataRawNoQuotes = jasperReportSummary.getChartDataRawAsString()
            .replace("\"", "");

        softAssertions.assertThat(
            chartDataRawNoQuotes.contains("xCategories:[SM_CLEVIS_2207240161]")
        ).isEqualTo(true);

        softAssertions.assertThat(
            chartDataRawNoQuotes.contains("annualizedPotentialThreshold:205000")
        ).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("28475")
    @TestRail(id = 28475)
    @Description("Input controls - Percent difference threshold filter")
    public void testPercentDifferenceThresholdInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        String currentExportSet = jasperReportUtil.getInputControls(reportsNameForInputControls)
            .getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.USD.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.PERCENT_DIFFERENCE_THRESHOLD.getInputControlId(), "99");

        JasperReportSummaryIncRawDataAsString jasperReportSummary = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(jasperApiUtils.getReportRequest());
        String chartDataRawNoQuotes = jasperReportSummary.getChartDataRawAsString()
            .replace("\"", "");

        softAssertions.assertThat(
            chartDataRawNoQuotes.contains("xCategories:[-12]")
        ).isEqualTo(true);

        softAssertions.assertThat(
            chartDataRawNoQuotes.contains("percentDifferenceThreshold:99")
        ).isEqualTo(true);

        softAssertions.assertAll();
    }

    private String getCurrencyFromHTML(JasperReportSummaryIncRawDataAsString jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsContainingText("Currency").get(6).parent().child(9).text();
    }

    private String getCostMinOrMaxValue(JasperReportSummary jasperReportSummary, String valueToGet) {
        return jasperReportSummary.getReportHtmlPart().getElementsContainingText(String.format("Cost %s", valueToGet))
            .get(6).siblingElements()
            .get(4).children()
            .get(0).text();
    }

    private boolean areValuesAlmostEqual(Double largerValue, Double smallerValue) {
        Double difference = largerValue - smallerValue;
        return difference.compareTo(0.00) >= 0 &&
            difference.compareTo(2.00) <= 0;
    }

    private String genericCostOutlierReportGeneration(String reportName) {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);
        InputControl inputControl = jasperReportUtil.getInputControls(reportsNameForInputControls);
        JasperApiUtils jasperApiUtilsToUse = jasperApiUtils;

        if (reportName.equals(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName())) {
            JasperApiUtils jasperApiUtilsDetails = new JasperApiUtils(jSessionId, exportSetName,
                JasperApiEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getEndpoint(), JasperApiInputControlsPathEnum.COST_OUTLIER_IDENTIFICATION_DETAILS);
            inputControl = jasperReportUtil.getInputControls(JasperApiInputControlsPathEnum.COST_OUTLIER_IDENTIFICATION_DETAILS);
            jasperApiUtilsToUse = jasperApiUtilsDetails;
        }

        jasperApiUtilsToUse.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(),
            CurrencyEnum.USD.getCurrency());

        jasperApiUtilsToUse.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControl.getExportSetName()
                .getOption(ExportSetEnum.SHEET_METAL_DTC.getExportSetName()).getValue());

        jasperApiUtilsToUse.setReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId(),
            inputControl.getRollup()
                .getOption(RollupEnum.SHEET_METAL_DTC.getRollupName()).getValue());

        jasperApiUtilsToUse.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        JasperReportSummaryIncRawDataAsString jasperReportSummaryIncRawDataAsString = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(jasperApiUtilsToUse.getReportRequest());

        if (!jasperReportSummaryIncRawDataAsString.getReportHtmlPart().toString().isEmpty()) {
            this.genericHtmlResponse = jasperReportSummaryIncRawDataAsString.getReportHtmlPart();
        }

        return jasperReportSummaryIncRawDataAsString.getChartDataRawAsString();
    }
}
