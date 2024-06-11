package com.apriori.cir.ui.tests.ootbreports.newreportstests.costoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testrail.TestRail;

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
