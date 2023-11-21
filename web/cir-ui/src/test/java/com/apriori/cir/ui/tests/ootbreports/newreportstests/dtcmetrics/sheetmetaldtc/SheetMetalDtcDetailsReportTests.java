package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.enums.SortOrderEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class SheetMetalDtcDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SHEET_METAL_DTC_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.SHEET_METAL_DTC_DETAILS;
    private String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_1271576.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V1.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V2.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("7421")
    @TestRail(id = 7421)
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricPpc() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7422")
    @TestRail(id = 7422)
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricFbc() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7401")
    @TestRail(id = 7401)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7402")
    @TestRail(id = 7402)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7682")
    @TestRail(id = 7682)
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Details Report")
    public void testSortOrderManufacturingIssues() {
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            mostCommonPartNames,
            assertFigures,
            "Sort Order", SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7681")
    @TestRail(id = 7681)
    @Description("Verify Sort Order input control functions correctly - Bends- Sheet Metal DTC Details Report")
    public void testSortOrderBends() {
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
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.BENDS.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7677")
    @TestRail(id = 7677)
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Details Report")
    public void testSortOrderTolerances() {
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
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7678")
    @TestRail(id = 7678)
    @Description("Verify Sort Order input control functions correctly - Machining Time - Sheet Metal DTC Details Report")
    public void testSortOrderMachiningTime() {
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
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.MACHINING_TIME.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7679")
    @TestRail(id = 7679)
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Details Report")
    public void testSortOrderAnnualSpend() {
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
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7680")
    @TestRail(id = 7680)
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Sheet Metal DTC Details Report")
    public void testSortOrderDtcRank() {
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
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7379")
    @TestRail(id = 7379)
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Details Report")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.genericDtcCurrencyTest(
            "",
            false,
            true
        );
    }

    @Test
    @TmsLink("3048")
    @TestRail(id = 3048)
    @Description("Verify Process Group input control functions correctly - Single Selection")
    public void testSingleProcessGroup() {
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            mostCommonPartNames,
            "Process Group",
            ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
    }

    @Test
    @TmsLink("7534")
    @TestRail(id = 7534)
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_0903238.getPartName(),
            JasperCirApiPartsEnum.P_1100149.getPartName(),
            JasperCirApiPartsEnum.P_1684443_OUTRIGGER_CAM.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7537")
    @TestRail(id = 7537)
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_3574715.getPartName(),
            JasperCirApiPartsEnum.P_3574688.getPartName(),
            JasperCirApiPartsEnum.P_1684402_TOP_BRACKET.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7540")
    @TestRail(id = 7540)
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreHigh() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }
}
