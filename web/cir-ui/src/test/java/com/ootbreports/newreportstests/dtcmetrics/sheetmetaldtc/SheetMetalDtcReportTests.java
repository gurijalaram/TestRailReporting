package com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SheetMetalDtcReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/sheetmetaldtc/SheetMetalDtcReportRequest");
    private static final String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    private static final SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @TestRail(testCaseId = {"3046"})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Report")
    public void testCurrencyCode() {
        String currencyCode = "currencyCode";
        //String partToGet = "1271576 (Bulkload)";

        InputControl inputControl = JasperReportUtil.init(jSessionId)
            .getInputControls();
        String exportSetValue = inputControl.getExportSetName().getOption(exportSetName).getValue();

        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, currencyCode, CurrencyEnum.USD.getCurrency());
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", exportSetValue);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        ChartDataPoint usdChartDataPoint = jasperApiUtils.generateReportAndGetChartDataPoint(reportRequest);

        String usdFullyBurdenedCost = jasperApiUtils.getFullyBurdenedCostFromChartDataPoint(usdChartDataPoint);
        double usdAnnualSpend = jasperApiUtils.getAnnualSpendFromChartDataPoint(usdChartDataPoint);

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, currencyCode, CurrencyEnum.GBP.getCurrency());

        ChartDataPoint gbpChartDataPoint = jasperApiUtils.generateReportAndGetChartDataPoint(reportRequest);

        String gbpFullyBurdenedCost = jasperApiUtils.getFullyBurdenedCostFromChartDataPoint(gbpChartDataPoint);
        double gbpAnnualSpend = jasperApiUtils.getAnnualSpendFromChartDataPoint(gbpChartDataPoint);

        assertThat(usdFullyBurdenedCost.equals(gbpFullyBurdenedCost), equalTo(false));
        assertThat(gbpAnnualSpend, is(not(equalTo(usdAnnualSpend))));
    }

    @Test
    @TestRail(testCaseId = {"3043"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Report")
    public void testCostMetricInputControlPpc() {
        String costMetricAssertValue = CostMetricEnum.PIECE_PART_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("2980123_CLAMP (Bulkload)").getPropertyByName("costMetric").getValue()).isEqualTo(costMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("AP_SHEETMETAL_EXERCISE (Initial)").getPropertyByName("costMetric").getValue()).isEqualTo(costMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("BRACKET_V4 (rev1)").getPropertyByName("costMetric").getValue()).isEqualTo(costMetricAssertValue);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7418"})
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Report")
    public void testCostMetricInputControlFbc() {
        String costMetricAssertValue = CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("2980123_CLAMP (Bulkload)").getPropertyByName("costMetric").getValue()).isEqualTo(costMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("AP_SHEETMETAL_EXERCISE (Initial)").getPropertyByName("costMetric").getValue()).isEqualTo(costMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("BRACKET_V4 (rev1)").getPropertyByName("costMetric").getValue()).isEqualTo(costMetricAssertValue);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"3044"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlFinishMass() {
        String massMetricAssertValue = MassMetricEnum.FINISH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("2980123_CLAMP (Bulkload)").getPropertyByName("massMetric").getValue()).isEqualTo(massMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("AP_SHEETMETAL_EXERCISE (Initial)").getPropertyByName("massMetric").getValue()).isEqualTo(massMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("BRACKET_V4 (rev1)").getPropertyByName("massMetric").getValue()).isEqualTo(massMetricAssertValue);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7398"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlRoughMass() {
        String massMetricAssertValue = MassMetricEnum.ROUGH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("2980123_CLAMP (Bulkload)").getPropertyByName("massMetric").getValue()).isEqualTo(massMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("AP_SHEETMETAL_EXERCISE (Initial)").getPropertyByName("massMetric").getValue()).isEqualTo(massMetricAssertValue);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("BRACKET_V4 (rev1)").getPropertyByName("massMetric").getValue()).isEqualTo(massMetricAssertValue);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7448"})
    @Description("Verify process group input control functionality - Single Selection - Sheet Metal DTC Report")
    public void testSingleProcessGroup() {
        jasperApiUtils.inputControlGenericTest(
            "Process Group",
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Process Group", ProcessGroupEnum.SHEET_METAL.getProcessGroup());

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Process");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains("Sheet Metal")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7532"})
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Report")
    public void testDtcScoreLow() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.LOW.getDtcScoreName());

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("3574707 (Bulkload)").getPropertyByName("dtcScore").getValue()).isEqualTo("Low");
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("AP_SHEETMETAL_EXERCISE (Initial)")).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("BRACKET_V4 (rev1)")).isEqualTo(null);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Low");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("Low")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7535"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Report")
    public void testDtcScoreMedium() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName());

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("3574707 (Bulkload)")).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("AP_SHEETMETAL_EXERCISE (Initial)").getPropertyByName("dtcScore").getValue()).isEqualTo("Medium");
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("BRACKET_V4 (rev1)")).isEqualTo(null);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Medium");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("Medium")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7538"})
    @Description("Verify DTC Score Input Control - High Selection - Sheet Metal DTC Report")
    public void testDtcScoreHigh() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.HIGH.getDtcScoreName());

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("3574707 (Bulkload)")).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("AP_SHEETMETAL_EXERCISE (Initial)")).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("BRACKET_V4 (rev1)").getPropertyByName("dtcScore").getValue()).isEqualTo("High");

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("High");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("High")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3045"})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Report")
    public void testSortOrderAnnualSpend() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Sort Order", SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum());

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("2980123_CLAMP (Bulkload)").getPropertyByName("annualSpend").getValue()).isEqualTo(4406.160458693279);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("3575137 (Bulkload)").getPropertyByName("annualSpend").getValue()).isEqualTo(8264352.305448065);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains("Annual Spend")).isEqualTo(true);

        softAssertions.assertAll();
    }
}
