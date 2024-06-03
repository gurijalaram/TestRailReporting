package com.apriori.cir.ui.tests.ootbreports.newreportstests.costoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class CostOutlierIdentificationReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.COST_OUTLIER_IDENTIFICATION;
    private SoftAssertions softAssertions = new SoftAssertions();
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

    private String getCostMinOrMaxValue(JasperReportSummary jasperReportSummary, String valueToGet) {
        return jasperReportSummary.getReportHtmlPart().getElementsContainingText(String.format("Cost %s", valueToGet))
            .get(6).siblingElements()
            .get(4).children()
            .get(0).text();
    }
}
