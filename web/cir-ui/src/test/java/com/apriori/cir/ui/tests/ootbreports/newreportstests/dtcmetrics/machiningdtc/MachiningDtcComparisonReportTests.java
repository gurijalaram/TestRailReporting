package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.machiningdtc;

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

public class MachiningDtcComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.MACHINING_DTC_COMPARISON.getEndpoint();
    private String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.MACHINING_DTC_COMPARISON;
    private JasperApiUtils jasperApiUtils;
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName(),
        JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
        JasperCirApiPartsEnum.PUNCH_INITIAL.getPartName()
    );

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("7414")
    @TestRail(id = 7414)
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7415")
    @TestRail(id = 7415)
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7394")
    @TestRail(id = 7394)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7395")
    @TestRail(id = 7395)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Comparison Report ")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("10012")
    @TestRail(id = 10012)
    @Description("Verify Currency Code input control functions correctly - Machining DTC Comparison Report")
    public void currencyCodeTest() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName(),
            false,
            true
        );
    }

    @Test
    @TmsLink("29701")
    @TestRail(id = 29701)
    @Description("Verify Minimum Annual Spend input control functions correctly - Machining DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            18
        );
    }

    @Test
    @TmsLink("7496")
    @TestRail(id = 7496)
    @Description("Verify DTC Score Input Control - Low Selection - Machining DTC Comparison Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_0200613_CAD.getPartName(),
            JasperCirApiPartsEnum.PMI_FLATNESS_CREO.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7499")
    @TestRail(id = 7499)
    @Description("Verify DTC Score Input Control - Medium Selection - Machining DTC Comparison Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MULTIPLE_TURNING_AXIS.getPartName(),
            JasperCirApiPartsEnum.P_3572871.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7502")
    @TestRail(id = 7502)
    @Description("Verify DTC Score Input Control - High Selection - Machining DTC Comparison Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.PUNCH.getPartName(),
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7543")
    @TestRail(id = 7543)
    @Description("Verify DTC Score Input Control - All Selection - Machining DTC Comparison Report")
    public void testDtcScoreAll() {
        List<String> partNames1 = Arrays.asList(
            JasperCirApiPartsEnum.P_0200613_CAD.getPartName(),
            JasperCirApiPartsEnum.P_3572871.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames1,
            "DTC Score", ""
        );
    }

    @Test
    @TmsLink("29737")
    @TestRail(id = {29737})
    @Description("Verify process group input control functionality - 2-Model Machining - Machining DTC Comparison Report")
    public void testProcessGroupFunctionalityTwoModelMachining() {
        List<String> partNames = Arrays.asList(
            "",
            ""
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()
        );
    }

    @Test
    @TmsLink("29738")
    @TestRail(id = {29738})
    @Description("Verify process group input control functionality - Stock Machining - Machining DTC Comparison Report")
    public void testProcessGroupFunctionalityStockMachining() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName().substring(0, 16),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        );
    }

    @Test
    @TmsLink("29739")
    @TestRail(id = {29739})
    @Description("Verify process group input control functionality - 2-Model and Stock Machining - Machining DTC Comparison Report")
    public void testProcessGroupFunctionalityBothProcessGroups() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName().substring(0, 16),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ""
        );
    }

    @Test
    @TmsLink("29783")
    @TestRail(id = {29783})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Machining DTC Comparison Report")
    public void testSortOrderManufacturingIssues() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName(),
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName()
        );
        List<Double> assertFigures = Arrays.asList(
            0.0,
            0.0
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29784")
    @TestRail(id = {29784})
    @Description("Verify Sort Order input control functions correctly - Design Standards - Machining DTC Comparison Report")
    public void testSortOrderDesignStandards() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName(),
            JasperCirApiPartsEnum.PUNCH_INITIAL.getPartName()
        );
        List<Double> assertFigures = Arrays.asList(
            0.0,
            0.0
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.DESIGN_STANDARDS.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29784")
    @TestRail(id = {29784})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Machining DTC Comparison Report")
    public void testSortOrderTolerances() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
            JasperCirApiPartsEnum.PARTBODY_1_INITIAL.getPartName()
        );
        List<Double> assertFigures = Arrays.asList(
            0.0,
            0.0
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29785")
    @TestRail(id = {29785})
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Machining DTC Comparison Report")
    public void testSortOrderSlowOperations() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName()
        );
        List<Double> assertFigures = Arrays.asList(
            0.0,
            0.0
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29786")
    @TestRail(id = {29786})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Machining DTC Comparison Report")
    public void testSortOrderAnnualSpend() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.PMI_ROUGHNESS_CREO.getPartName(),
            JasperCirApiPartsEnum.PMI_PROFILE_OF_SURFACE_CREO.getPartName()
        );
        List<Double> assertFigures = Arrays.asList(
            0.0,
            0.0
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }
}
