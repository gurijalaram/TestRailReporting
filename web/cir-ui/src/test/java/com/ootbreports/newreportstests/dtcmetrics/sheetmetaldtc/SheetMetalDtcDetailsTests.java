package com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class SheetMetalDtcDetailsTests extends JasperApiAuthenticationUtil {
    private final List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_1271576.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V1.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V2.getPartName()
    );
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
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            mostCommonPartNames
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
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            mostCommonPartNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7401"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            mostCommonPartNames
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
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            mostCommonPartNames
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
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            mostCommonPartNames,
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
            JasperCirApiPartsEnum.BRACKET_SHORTENED.getPartName(),
            JasperCirApiPartsEnum.BRACKET_SHORTENED_ISSUES.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V1_HEMS.getPartName()
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
            JasperCirApiPartsEnum.BRACKET_V1.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V2.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V3.getPartName()
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
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V3.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V4.getPartName()
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
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            JasperCirApiPartsEnum.P_3575137.getPartName(),
            JasperCirApiPartsEnum.P_3575136.getPartName()
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
            JasperCirApiPartsEnum.BRACKET_SHORTENED.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V1.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V1_HEMS.getPartName()
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
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            miscData,
            mostCommonPartNames
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
            JasperCirApiPartsEnum.P_0903238.getPartName(),
            JasperCirApiPartsEnum.P_1100149.getPartName(),
            JasperCirApiPartsEnum.P_1684443_OUTRIGGER_CAM.getPartName()
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
            JasperCirApiPartsEnum.P_3574715.getPartName(),
            JasperCirApiPartsEnum.P_3574688.getPartName(),
            JasperCirApiPartsEnum.P_1684402_TOP_BRACKET.getPartName()
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
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            mostCommonPartNames
        );
    }
}
