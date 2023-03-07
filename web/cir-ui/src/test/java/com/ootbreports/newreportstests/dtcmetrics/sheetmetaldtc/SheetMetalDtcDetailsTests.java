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
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SheetMetalDtcDetailsTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/sheetmetaldtc/SheetMetalDtcDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7421"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricPpc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7422"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricFbc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"7401"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7402"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7682"})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Details Report")
    public void testSortOrderManufacturingIssues() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7681"})
    @Description("Verify Sort Order input control functions correctly - Bends- Sheet Metal DTC Details Report")
    public void testSortOrderBends() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.BENDS.getSortOrderEnum()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7677"})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Details Report")
    public void testSortOrderTolerances() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7678"})
    @Description("Verify Sort Order input control functions correctly - Machining Time - Sheet Metal DTC Details Report")
    public void testSortOrderMachiningTime() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.MACHINING_TIME.getSortOrderEnum()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7679"})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Details Report")
    public void testSortOrderAnnualSpend() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7680"})
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Sheet Metal DTC Details Report")
    public void testSortOrderDtcRank() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7379"})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Details Report")
    public void testCurrencyCodeInputControl() {
        String currencyCode = "currencyCode";

        InputControl inputControl = JasperReportUtil.init(jSessionId)
            .getInputControls();
        String exportSetValue = inputControl.getExportSetName().getOption(exportSetName).getValue();

        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, currencyCode, CurrencyEnum.USD.getCurrency());
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", exportSetValue);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        JasperReportSummary jasperReportSummaryUsd = jasperApiUtils.generateReportSummary(reportRequest);

        List<Element> usdAnnualSpendItem = jasperReportSummaryUsd.getReportHtmlPart().getElementsContainingText("14,199,846.01");
        String usdAnnualSpend = usdAnnualSpendItem.get(0).text();

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, currencyCode, CurrencyEnum.GBP.getCurrency());

        JasperReportSummary jasperReportSummaryGbp = jasperApiUtils.generateReportSummary(reportRequest);

        List<Element> gbpAnnualSpendItem = jasperReportSummaryGbp.getReportHtmlPart().getElementsContainingText("");
        String gbpAnnualSpend = gbpAnnualSpendItem.get(0).text();

        assertThat(usdAnnualSpend.equals(gbpAnnualSpend), equalTo(false));
        assertThat(usdAnnualSpend, is(not(equalTo(gbpAnnualSpend))));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3048"})
    @Description("Verify Process Group input control functions correctly - Single Selection")
    public void testSingleProcessGroup() {
        jasperApiUtils.inputControlGenericTest(
            "Process Group",
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7534"})
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreLow() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7537"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreMedium() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7540"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreHigh() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }
}
