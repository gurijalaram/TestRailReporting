package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import com.apriori.utils.TestRail;
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
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class CastingDtcDetailsReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testCostMetricInputControlPpc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7412")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Details Report")
    public void testCostMetricInputControlFbc() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7510")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Details Report")
    public void testDtcScoreLow() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_40128483_MLDES_0001.getPartName(),
            JasperCirApiPartsEnum.P_40089252_MLDES_0004_REDRAW.getPartName(),
            JasperCirApiPartsEnum.DU100024720_G.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7513")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Details Report")
    public void testDtcScoreMedium() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.P_40116211_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7516")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Details Report")
    public void testDtcScoreHigh() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7657")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcDetailsTest();
    }

    @Test
    @TestRail(testCaseId = "7629")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingCasting() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.CASTING_ISSUES.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
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
    @TestRail(testCaseId = "7630")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingMachining() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_SC.getPartName(),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName(),
            JasperCirApiPartsEnum.P_1205DU1017494_K.getPartName()
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
    @TestRail(testCaseId = "7631")
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Details Report")
    public void testSortOrderInputControlMaterialScrap() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.OBSTRUCTED_MACHINING.getPartName(),
            JasperCirApiPartsEnum.B2315.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName()
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
    @TestRail(testCaseId = "7632")
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName(),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_SC.getPartName(),
            JasperCirApiPartsEnum.P_1205DU1017494_K.getPartName()
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
    @TestRail(testCaseId = "7633")
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName(),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_SC.getPartName(),
            JasperCirApiPartsEnum.P_1205DU1017494_K.getPartName()
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
    @TestRail(testCaseId = "7634")
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Details Report")
    public void testSortOrderInputControlSpecialTooling() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DU600051458.getPartName(),
            JasperCirApiPartsEnum.DU200068073_B.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName()
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
    @TestRail(testCaseId = "7635")
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.E3_241_4_N.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName(),
            JasperCirApiPartsEnum.P_1205DU1017494_K.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "10,013,204.23",
            "7,819,806.44"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            miscData,
            partNames,
            assertFigures
        );
    }

    @Test
    @TestRail(testCaseId = "7636")
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Casting DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        List<String> miscData = Arrays.asList(
            "Sort Order",
            SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BARCO_R8761310.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
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
}
