package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CastingDtcReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    private static final SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        jasperApiUtils.genericDtcCurrencyTest("84C602281P1_D (Initial)", true);
    }

    @Test
    @TestRail(testCaseId = {"1695"})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Report")
    public void testCostMetricInputControlPpc() {
        List<String> partNames = Arrays.asList("40137441.MLDES.0002 (Initial)", "1205DU1017494_K (Initial)", "CASE_03 (Initial)");
        List<String> miscData = Arrays.asList("Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName(), "Cost", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7408"})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        List<String> partNames = Arrays.asList("40137441.MLDES.0002 (Initial)", "1205DU1017494_K (Initial)", "CASE_03 (Initial)");
        List<String> miscData = Arrays.asList("Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName(), "Cost", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"1696"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> partNames = Arrays.asList("40137441.MLDES.0002 (Initial)", "1205DU1017494_K (Initial)", "CASE_03 (Initial)");
        List<String> miscData = Arrays.asList("Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName(), "Mass", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7388"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> partNames = Arrays.asList("40137441.MLDES.0002 (Initial)", "1205DU1017494_K (Initial)", "CASE_03 (Initial)");
        List<String> miscData = Arrays.asList("Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName(), "Mass", "<td");
        jasperApiUtils.genericDtcTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7454"})
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieCastingOnly() {
        List<String> partNames = Arrays.asList("40137441.MLDES.0002 (Initial)", "1205DU1017494_K");
        List<String> miscData = Arrays.asList(
            "Process Group",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup(),
            "Process",
            "<td",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup());
        jasperApiUtils.genericProcessGroupTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7453"})
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Report")
    public void testProcessGroupInputControlSandCastingOnly() {
        List<String> partNames = Arrays.asList("40137441.MLDES.0002 (Initial)", "1205DU1017494_K");
        List<String> miscData = Arrays.asList(
            "Process Group",
            ProcessGroupEnum.CASTING_SAND.getProcessGroup(),
            "Process",
            "<td",
            ProcessGroupEnum.CASTING_SAND.getProcessGroup());
        jasperApiUtils.genericProcessGroupTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = {"7455"})
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieAndSandCasting() {
        List<String> partNames = Arrays.asList("40137441.MLDES.0002 (Initial)", "1205DU1017494_K");
        List<String> miscData = Arrays.asList(
            "Process Group",
            "",
            "Process",
            "<td",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup().concat(", ").concat(ProcessGroupEnum.CASTING_SAND.getProcessGroup()));
        jasperApiUtils.genericProcessGroupTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = "7508")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList("84C602281P1_D (Initial)", "CASE_13 (Initial)", "GEAR HOUSING (Initial)");
        List<String> miscData = Arrays.asList("DTC Score", DtcScoreEnum.LOW.getDtcScoreName(), "DTC", "<td");
        jasperApiUtils.genericDtcScoreTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = "7511")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList("84C602281P1_D (Initial)", "CASE_13 (Initial)", "GEAR HOUSING (Initial)");
        List<String> miscData = Arrays.asList("DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName(), "DTC", "<td");
        jasperApiUtils.genericDtcScoreTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = "7514")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList("84C602281P1_D (Initial)", "CASE_13 (Initial)", "GEAR HOUSING (Initial)");
        List<String> miscData = Arrays.asList("DTC Score", DtcScoreEnum.HIGH.getDtcScoreName(), "DTC", "<td");
        jasperApiUtils.genericDtcScoreTest(miscData, partNames);
    }

    @Test
    @TestRail(testCaseId = "1700")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Report")
    public void testMinimumAnnualSpend() {
        String minimumAnnualSpendValue = "7820000";
        jasperApiUtils.inputControlGenericTest(
            "Minimum Annual Spend",
            minimumAnnualSpendValue
        );

        JasperReportSummary reportSummary = jasperApiUtils.generateReportSummary(reportRequest);
        ChartDataPoint chartDataPoint = reportSummary.getFirstChartData().getChartDataPointByPartName("E3-241-4-N (Initial)");
        List<ChartDataPoint> chartDataPointList = reportSummary.getFirstChartData().getChartDataPoints();

        assertThat(chartDataPoint.getAnnualSpend(), is(not(equalTo(minimumAnnualSpendValue))));
        assertThat(chartDataPointList.size(), is(equalTo(1)));
    }
}
