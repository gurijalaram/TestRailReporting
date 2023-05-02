package com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class SheetMetalDtcDetailsTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/sheetmetaldtc/SheetMetalDtcDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7421"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricPpc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36)
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7422"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricFbc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36)
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"7401"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36)
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7402"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36)
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7682"})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Details Report")
    public void testSortOrderManufacturingIssues() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            partNames,
            assertFigures
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7681"})
    @Description("Verify Sort Order input control functions correctly - Bends- Sheet Metal DTC Details Report")
    public void testSortOrderBends() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.BENDS.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(37),
            Constants.PART_NAMES_FOR_INPUT.get(33),
            Constants.PART_NAMES_FOR_INPUT.get(38)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            partNames,
            assertFigures
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7677"})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Details Report")
    public void testSortOrderTolerances() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36),
            Constants.PART_NAMES_FOR_INPUT.get(39)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            partNames,
            assertFigures
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7678"})
    @Description("Verify Sort Order input control functions correctly - Machining Time - Sheet Metal DTC Details Report")
    public void testSortOrderMachiningTime() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.MACHINING_TIME.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(39),
            Constants.PART_NAMES_FOR_INPUT.get(40)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            partNames,
            assertFigures
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7679"})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Details Report")
    public void testSortOrderAnnualSpend() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(34),
            Constants.PART_NAMES_FOR_INPUT.get(41)
        );
        List<String> assertFigures = Arrays.asList(
            "10,616,713.67",
            "8,264,352.31"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            partNames,
            assertFigures
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7680"})
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Sheet Metal DTC Details Report")
    public void testSortOrderDtcRank() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(37),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(38)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            partNames,
            assertFigures
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7379"})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Details Report")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.genericDtcCurrencyTest(
            "",
            false
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3048"})
    @Description("Verify Process Group input control functions correctly - Single Selection")
    public void testSingleProcessGroup() {
        List<String> miscData = Arrays.asList(
            "Process Group",
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36)
        );
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7534"})
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreLow() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(30),
            Constants.PART_NAMES_FOR_INPUT.get(42),
            Constants.PART_NAMES_FOR_INPUT.get(43)
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7537"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreMedium() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(44),
            Constants.PART_NAMES_FOR_INPUT.get(45),
            Constants.PART_NAMES_FOR_INPUT.get(31)
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7540"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreHigh() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(25),
            Constants.PART_NAMES_FOR_INPUT.get(35),
            Constants.PART_NAMES_FOR_INPUT.get(36)
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }
}
