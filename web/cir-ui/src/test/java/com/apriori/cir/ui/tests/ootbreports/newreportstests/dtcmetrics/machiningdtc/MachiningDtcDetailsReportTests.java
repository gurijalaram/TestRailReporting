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

public class MachiningDtcDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.MACHINING_DTC_DETAILS.getEndpoint();
    private String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.MACHINING_DTC_DETAILS;
    private JasperApiUtils jasperApiUtils;
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
        JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
        JasperCirApiPartsEnum.PUNCH_INITIAL.getPartName()
    );

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("7416")
    @TestRail(id = 7416)
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Details Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7417")
    @TestRail(id = 7417)
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Details Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7396")
    @TestRail(id = 7396)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7397")
    @TestRail(id = 7397)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("10011")
    @TestRail(id = {10011})
    @Description("Verify currency code input control function correctly - Machining DTC Details Report")
    public void testCurrencyCodeFunctionality() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.PUNCH.getPartName(),
            false,
            true
        );
    }

    @Test
    @TmsLink("29700")
    @TestRail(id = 29700)
    @Description("Verify Minimum Annual Spend input control functions correctly - Machining DTC Details Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcDetailsTest(false);
    }

    @Test
    @TmsLink("7497")
    @TestRail(id = 7497)
    @Description("Verify DTC Score Input Control - Low Selection - Machining DTC Details Report")
    public void testDtcScoreLow() {
        List<String> partNames1 = Arrays.asList(
            JasperCirApiPartsEnum.PMI_FLATNESS_CREO.getPartName(),
            JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames1,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7500")
    @TestRail(id = 7500)
    @Description("Verify DTC Score Input Control - Medium Selection - Machining DTC Details Report")
    public void testDtcScoreMedium() {
        List<String> partNames1 = Arrays.asList(
            JasperCirApiPartsEnum.MULTIPLE_TURNING_AXIS.getPartName(),
            JasperCirApiPartsEnum.P_3538968_CAD_INITIAL.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames1,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7503")
    @TestRail(id = 7503)
    @Description("Verify DTC Score Input Control - High Selection - Machining DTC Details Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.DTCMACHINING_001_TOLERANCED.getPartName().substring(0, 16)
        );
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("29725")
    @TestRail(id = 29725)
    @Description("Verify process group input control functionality - Stock Machining - Machining DTC Details Report")
    public void testSingleProcessGroupStockMachining() {
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            partNames,
            "Process Group",
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        );
    }

    @Test
    @TmsLink("29724")
    @TestRail(id = 29724)
    @Description("Verify process group input control functionality - 2 Model Machining - Machining DTC Details Report")
    public void testSingleProcessGroupTwoModelMachining() {
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            partNames,
            "Process Group",
            ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()
        );
    }

    @Test
    @TmsLink("29726")
    @TestRail(id = 29726)
    @Description("Verify process group input control functionality - 2 Model and Stock Machining - Machining DTC Details Report")
    public void testBothProcessGroups() {
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            partNames,
            "Process Group",
            ""
        );
    }

    @Test
    @TmsLink("29788")
    @TestRail(id = 29788)
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Machining DTC Details Report")
    public void testSortOrderInputControlManufacturingIssues() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            "Sort Order",
            SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29789")
    @TestRail(id = 29789)
    @Description("Verify Sort Order input control functions correctly - Design Standards - Machining DTC Details Report")
    public void testSortOrderInputControlDesignStandards() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            "Sort Order",
            SortOrderEnum.DESIGN_STANDARDS.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29790")
    @TestRail(id = 29790)
    @Description("Verify Sort Order input control functions correctly - Tolerances - Machining DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.PARTBODY_1.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            "Sort Order",
            SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29791")
    @TestRail(id = 29791)
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Machining DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.DTCMACHINING_001.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            "Sort Order",
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29792")
    @TestRail(id = 29792)
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Machining DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.PMI_ROUGHNESS_CREO.getPartName().substring(0, 17),
            JasperCirApiPartsEnum.PMI_PROFILE_OF_SURFACE_CREO.getPartName().substring(0, 24)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            "Sort Order",
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @TmsLink("29793")
    @TestRail(id = 29793)
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Machining DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            "Sort Order",
            SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
    }
}
