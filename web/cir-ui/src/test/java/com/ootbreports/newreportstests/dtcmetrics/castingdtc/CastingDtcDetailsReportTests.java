package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

public class CastingDtcDetailsReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7412")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Details Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7510")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Details Report")
    public void testDtcScoreLow() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7513")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Details Report")
    public void testDtcScoreMedium() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7516")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Details Report")
    public void testDtcScoreHigh() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7657")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        String minimumAnnualSpendValue = "7820000";
        jasperApiUtils.inputControlGenericTest(
            "Minimum Annual Spend",
            minimumAnnualSpendValue
        );

        JasperReportSummary reportSummary = jasperApiUtils.generateReportSummary(reportRequest);
        String annualSpendValue = reportSummary.getReportHtmlPart().getElementsContainingText("E3-241-4-N").get(5).child(19).text();

        assertThat(annualSpendValue, is(not(equalTo(minimumAnnualSpendValue))));
    }

    @Test
    @TestRail(testCaseId = "7629")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingCasting() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.CASTING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7630")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingMachining() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7631")
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Details Report")
    public void testSortOrderInputControlMaterialScrap() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7632")
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7633")
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7634")
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Details Report")
    public void testSortOrderInputControlSpecialTooling() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7635")
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7636")
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Casting DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        jasperApiUtils.inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
    }
}
