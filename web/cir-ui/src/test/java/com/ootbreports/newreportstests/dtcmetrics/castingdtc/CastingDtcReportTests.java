package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

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

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CastingDtcReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        String currencyCode = "currencyCode";

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
    @TestRail(testCaseId = {"1695"})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7408"})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"1696"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7388"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7454"})
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieCastingOnly() {
        jasperApiUtils.inputControlGenericTest(
            "Process Group",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
    }

    @Test
    @TestRail(testCaseId = {"7453"})
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Report")
    public void testProcessGroupInputControlSandCastingOnly() {
        jasperApiUtils.inputControlGenericTest(
            "Process Group",
            ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );
    }

    @Test
    @TestRail(testCaseId = {"7455"})
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieAndSandCasting() {
        jasperApiUtils.inputControlGenericTest(
            "Process Group",
            ""
        );
    }

    @Test
    @TestRail(testCaseId = "7508")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Report")
    public void testDtcScoreLow() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7511")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Report")
    public void testDtcScoreMedium() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7514")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Report")
    public void testDtcScoreHigh() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
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
