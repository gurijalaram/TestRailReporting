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

public class SheetMetalDtcComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SHEET_METAL_DTC_COMPARISON.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.SHEET_METAL_DTC_COMPARISON;
    private String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.P_1271576.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V1_REV_1.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V2_REV_1.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("7419")
    @TestRail(id = 7419)
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Comparison Report")
    public void testCostMetricPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7420")
    @TestRail(id = 7420)
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Comparison Report")
    public void testCostMetricFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7399")
    @TestRail(id = 7399)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7400")
    @TestRail(id = 7400)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7378")
    @TestRail(id = 7378)
    @Description("Verify Currency Code Input Control - Sheet Metal DTC Details Report")
    public void currencyCodeTest() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            false,
            true
        );
    }

    @Test
    @TmsLink("29702")
    @TestRail(id = 29702)
    @Description("Verify Minimum Annual Spend input control functions correctly - Sheet Metal DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            3
        );
    }

    @Test
    @TmsLink("7533")
    @TestRail(id = 7533)
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_1684443_OUTRIGGER_CAM.getPartName().substring(0, 21),
            JasperCirApiPartsEnum.SM_CLEVIS_2207240161_BULKLOAD.getPartName().substring(0, 20)
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7536")
    @TestRail(id = 7536)
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_3574715.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName().substring(0, 13)
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7539")
    @TestRail(id = 7539)
    @Description("Verify DTC Score Input Control - High Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BRACKET_V1.getPartName().substring(0, 10),
            JasperCirApiPartsEnum.BRACKET_SHORTENED_ISSUES.getPartName().substring(0, 24)
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7541")
    @TestRail(id = 7541)
    @Description("Verify DTC Score Input Control - All Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreAll() {
        List<String> partNames1 = Arrays.asList(
            JasperCirApiPartsEnum.SM_CLEVIS_2207240161.getPartName().substring(0, 20),
            JasperCirApiPartsEnum.P_3574715.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.BRACKET_V1.getPartName().substring(0, 10)
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames1,
            "DTC Score", ""
        );
    }

    @Test
    @TmsLink("7449")
    @TestRail(id = {7449})
    @Description("Verify process group input control functionality - Single Selection - Sheet Metal DTC Comparison Report")
    public void testProcessGroupFunctionalitySheetMetal() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BRACKET_V1.getPartName().substring(0, 10),
            JasperCirApiPartsEnum.SM_CLEVIS_2207240161_BULKLOAD.getPartName().substring(0, 20)
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
    }

    @Test
    @TmsLink("7672")
    @TestRail(id = {7672})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Comparison Report")
    public void testSortOrderManufacturingIssues() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V1_REV_1.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            "Sort Order", SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7673")
    @TestRail(id = {7673})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Comparison Report")
    public void testSortOrderBends() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BRACKET_SHORTENED_REV1.getPartName(),
            JasperCirApiPartsEnum.BRACKET_SHORTENED_ISSUES.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            "Sort Order", SortOrderEnum.BENDS.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7674")
    @TestRail(id = {7674})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Comparison Report")
    public void testSortOrderTolerances() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BRACKET_V1_REV_1.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V2_REV_1.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            "Sort Order", SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7675")
    @TestRail(id = {7675})
    @Description("Verify Sort Order input control functions correctly - Machining Times - Sheet Metal DTC Comparison Report")
    public void testSortOrderMachiningTimes() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            JasperCirApiPartsEnum.BRACKET_V3_REV1.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            "Sort Order", SortOrderEnum.MACHINING_TIME.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("7676")
    @TestRail(id = {7676})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Comparison Report")
    public void testSortOrderAnnualSpend() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            JasperCirApiPartsEnum.P_3575137.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            "Sort Order", SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }
}
