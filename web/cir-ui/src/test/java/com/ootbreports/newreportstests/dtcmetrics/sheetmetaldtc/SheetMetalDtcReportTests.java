package com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.utils.TestRail;
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

import java.util.Arrays;
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
        jasperApiUtils.genericDtcCurrencyTest("2980123_CLAMP (Bulkload)", true);
    }

    @Test
    @TestRail(testCaseId = {"3043"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Report")
    public void testCostMetricInputControlPpc() {
        List<String> partNames = Arrays.asList("2980123_CLAMP (Bulkload)", "AP_SHEETMETAL_EXERCISE (Initial)", "BRACKET_V4 (rev1)");
        List<String> miscData = Arrays.asList("Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName(), "Cost", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7418"})
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Report")
    public void testCostMetricInputControlFbc() {
        List<String> partNames = Arrays.asList("2980123_CLAMP (Bulkload)", "AP_SHEETMETAL_EXERCISE (Initial)", "BRACKET_V4 (rev1)");
        List<String> miscData = Arrays.asList("Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName(), "Cost", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"3044"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> partNames = Arrays.asList("2980123_CLAMP (Bulkload)", "AP_SHEETMETAL_EXERCISE (Initial)", "BRACKET_V4 (rev1)");
        List<String> miscData = Arrays.asList("Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName(), "Mass", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7398"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> partNames = Arrays.asList("2980123_CLAMP (Bulkload)", "AP_SHEETMETAL_EXERCISE (Initial)", "BRACKET_V4 (rev1)");
        List<String> miscData = Arrays.asList("Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName(), "Mass", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7448"})
    @Description("Verify process group input control functionality - Single Selection - Sheet Metal DTC Report")
    public void testSingleProcessGroup() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Process Group", ProcessGroupEnum.SHEET_METAL.getProcessGroup());

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
        List<String> partNames = Arrays.asList("2980123_CLAMP (Bulkload)", "AP_SHEETMETAL_EXERCISE (Initial)", "BRACKET_V4 (rev1)");
        List<String> miscData = Arrays.asList("DTC Score", DtcScoreEnum.LOW.getDtcScoreName(), "DTC", "<td");
        jasperApiUtils.genericDtcScoreTest(miscData, partNames);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7535"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList("2980123_CLAMP (Bulkload)", "AP_SHEETMETAL_EXERCISE (Initial)", "BRACKET_V4 (rev1)");
        List<String> miscData = Arrays.asList("DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName(), "DTC", "<td");
        jasperApiUtils.genericDtcScoreTest(miscData, partNames);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7538"})
    @Description("Verify DTC Score Input Control - High Selection - Sheet Metal DTC Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList("2980123_CLAMP (Bulkload)", "AP_SHEETMETAL_EXERCISE (Initial)", "BRACKET_V4 (rev1)");
        List<String> miscData = Arrays.asList("DTC Score", DtcScoreEnum.HIGH.getDtcScoreName(), "DTC", "<td");
        jasperApiUtils.genericDtcScoreTest(miscData, partNames);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3045"})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Report")
    public void testSortOrderAnnualSpend() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum());

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("2980123_CLAMP (Bulkload)").getPropertyByName("annualSpend").getValue()).isEqualTo(4406.160458693279);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName("3575137 (Bulkload)").getPropertyByName("annualSpend").getValue()).isEqualTo(8264352.305448065);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains("Annual Spend")).isEqualTo(true);

        softAssertions.assertAll();
    }
}
