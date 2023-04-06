package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CastingDtcComparisonReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcComparisonReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();

    private static final SoftAssertions softAssertions = new SoftAssertions();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = "7409")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        String costMetricAssertValue = CostMetricEnum.PIECE_PART_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        performDtcPositiveAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "CYLINDER HEAD (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "40137441.MLDES.0002 (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7410")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        String costMetricAssertValue = CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        performDtcPositiveAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "CYLINDER HEAD (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "40137441.MLDES.0002 (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7489")
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        String massMetricAssertValue = MassMetricEnum.FINISH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        performDtcPositiveAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "CYLINDER HEAD (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "40137441.MLDES.0002 (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7390")
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        String massMetricAssertValue = MassMetricEnum.ROUGH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        performDtcPositiveAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "CYLINDER HEAD (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "40137441.MLDES.0002 (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7509")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Comparison Report")
    public void testDtcScoreLow() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.LOW.getDtcScoreName());

        performDtcPositiveAsserts(jasperReportSummary, "40128483.MLDES.0001 (Initial)");
        performDtcNegativeAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcNegativeAsserts(jasperReportSummary, "GEAR HOUSING (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Low");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("Low")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7512")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Comparison Report")
    public void testDtcScoreMedium() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName());

        performDtcNegativeAsserts(jasperReportSummary, "40128483.MLDES.0001 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcNegativeAsserts(jasperReportSummary, "GEAR HOUSING (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Medium");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("Medium")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7515")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Comparison Report")
    public void testDtcScoreHigh() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.HIGH.getDtcScoreName());

        performDtcNegativeAsserts(jasperReportSummary, "40128483.MLDES.0001 (Initial)");
        performDtcNegativeAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "GEAR HOUSING (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("High");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("High")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7544")
    @Description("Verify DTC Score Input Control - All Selection - Casting DTC Comparison Report")
    public void testDtcScoreAll() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", "");

        performDtcPositiveAsserts(jasperReportSummary, "40128483.MLDES.0001 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        performDtcPositiveAsserts(jasperReportSummary, "GEAR HOUSING (Initial)");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("High");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        assertThat(tdResultElements.get(0).toString().contains("High, Low, Medium"), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "7656")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Minimum Annual Spend", "7820000");

        String negativePartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String positivePartName = "E3-241-4-N (Initial)";

        performDtcNegativeAsserts(jasperReportSummary, negativePartName);

        ChartDataPoint chartDataPoint = jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName);
        softAssertions.assertThat(chartDataPoint.getHoleIssues()).isEqualTo(10);
        softAssertions.assertThat(chartDataPoint.getMaterialIssues()).isEqualTo(0);
        softAssertions.assertThat(chartDataPoint.getRadiusIssues()).isEqualTo(18);
        softAssertions.assertThat(chartDataPoint.getDraftIssues()).isEqualTo(15);

        String minAnnualSpendAssertValue = "7,820,000.00";
        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(minAnnualSpendAssertValue);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.toString().contains(minAnnualSpendAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    private void performDtcPositiveAsserts(JasperReportSummary jasperReportSummary, String partName) {
        for (int i = 0; i < 6; i++) {
            softAssertions.assertThat(jasperReportSummary.getChartData().get(i).getChartDataPointByPartName(partName).getPartName()).isEqualTo(partName);
        }
    }

    private void performDtcNegativeAsserts(JasperReportSummary jasperReportSummary, String partName) {
        for (int i = 0; i < 6; i++) {
            softAssertions.assertThat(jasperReportSummary.getChartData().get(i).getChartDataPointByPartName(partName)).isEqualTo(null);
        }
    }
}
