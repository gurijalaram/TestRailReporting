package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CastingDtcComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.CASTING_DTC_COMPARISON.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.CASTING_DTC_COMPARISON;
    private String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
        JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
        JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7409")
    @TestRail(id = 7409)
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7410")
    @TestRail(id = 7410)
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7389")
    @TestRail(id = 7389)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7390")
    @TestRail(id = 7390)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7509")
    @TestRail(id = 7509)
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Comparison Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_40128483_MLDES_0001.getPartName(),
            JasperCirApiPartsEnum.P_40089252_MLDES_0004_REDRAW.getPartName(),
            JasperCirApiPartsEnum.DU100024720_G.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7512")
    @TestRail(id = 7512)
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Comparison Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.P_40116211_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7515")
    @TestRail(id = 7515)
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Comparison Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7544")
    @TestRail(id = 7544)
    @Description("Verify DTC Score Input Control - All Selection - Casting DTC Comparison Report")
    public void testDtcScoreAll() {
        jasperApiUtils.genericDtcScoreTest(
            true,
            mostCommonPartNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), ""
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7656")
    @TestRail(id = 7656)
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            1
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("10009")
    @TestRail(id = 10009)
    @Description("Verify Currency Code input control functions correctly - Casting DTC Comparison Report")
    public void currencyCodeTest() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            false,
            true
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29727")
    @TestRail(id = {29727})
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Comparison Report")
    public void testProcessGroupFunctionalityDieCasting() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            InputControlsEnum.PROCESS_GROUP.getInputControlId(), ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29728")
    @TestRail(id = {29728})
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Comparison Report")
    public void testProcessGroupFunctionalitySandCasting() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName().substring(0, 12),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName().substring(0, 13)
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            InputControlsEnum.PROCESS_GROUP.getInputControlId(), ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29729")
    @TestRail(id = {29729})
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Comparison Report")
    public void testProcessGroupFunctionalityBothProcessGroups() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            InputControlsEnum.PROCESS_GROUP.getInputControlId(), ""
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7637")
    @TestRail(id = {7637})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Comparison Report")
    public void testSortOrderManufacturingMachining() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_SC.getPartName(),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7638")
    @TestRail(id = {7638})
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Comparison Report")
    public void testSortOrderMaterialScrap() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.OBSTRUCTED_MACHINING.getPartName(),
            JasperCirApiPartsEnum.B2315.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7639")
    @TestRail(id = {7639})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Comparison Report")
    public void testSortOrderTolerances() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName(),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_SC.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7640")
    @TestRail(id = {7640})
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Comparison Report")
    public void testSortOrderSlowOperations() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName(),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_SC.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7641")
    @TestRail(id = {7641})
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Comparison Report")
    public void testSortOrderSpecialTooling() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DU600051458.getPartName(),
            JasperCirApiPartsEnum.DU200068073_B.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7642")
    @TestRail(id = {7642})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Comparison Report")
    public void testSortOrderAnnualSpend() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.E3_241_4_N.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7643")
    @TestRail(id = {7643})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Sheet Metal DTC Comparison Report")
    public void testSortOrderManufacturingCasting() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.CASTING_ISSUES.getSortOrderEnum()
        );
    }
}
