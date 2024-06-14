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
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CastingDtcDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.CASTING_DTC_DETAILS.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.CASTING_DTC_DETAILS;
    private String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
        JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
        JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
    );
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;


    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7411")
    @TestRail(id = 7411)
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7412")
    @TestRail(id = 7412)
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Details Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7411")
    @TestRail(id = 7411)
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7411")
    @TestRail(id = 7411)
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7510")
    @TestRail(id = 7510)
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Details Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_40128483_MLDES_0001.getPartName(),
            JasperCirApiPartsEnum.P_40089252_MLDES_0004_REDRAW.getPartName(),
            JasperCirApiPartsEnum.DU100024720_G.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7513")
    @TestRail(id = 7513)
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Details Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.P_40116211_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7516")
    @TestRail(id = 7516)
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Details Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7657")
    @TestRail(id = 7657)
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcDetailsTest(true);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7629")
    @TestRail(id = 7629)
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingCasting() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.CASTING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7630")
    @TestRail(id = 7630)
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingMachining() {
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
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7631")
    @TestRail(id = 7631)
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Details Report")
    public void testSortOrderInputControlMaterialScrap() {
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
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7632")
    @TestRail(id = 7632)
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Details Report")
    public void testSortOrderInputControlTolerances() {
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
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7633")
    @TestRail(id = 7633)
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
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
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7634")
    @TestRail(id = 7634)
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Details Report")
    public void testSortOrderInputControlSpecialTooling() {
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
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7635")
    @TestRail(id = 7635)
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.E3_241_4_N.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName(),
            JasperCirApiPartsEnum.P_1205DU1017494_K.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "7,486,512.33",
            "5,846,587.76"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7636")
    @TestRail(id = 7636)
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Casting DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8761310.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29730")
    @TestRail(id = 29730)
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Details Report")
    public void testSingleProcessGroupSandCasting() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName().substring(0, 12),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName().substring(0, 13)
        );
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            partNames,
            InputControlsEnum.PROCESS_GROUP.getInputControlId(),
            ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29731")
    @TestRail(id = 29731)
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Details Report")
    public void testSingleProcessGroupDieCasting() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName().substring(0, 30),
            JasperCirApiPartsEnum.DTC_CASTING_ISSUES_I.getPartName().substring(0, 16)
        );
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            partNames,
            InputControlsEnum.PROCESS_GROUP.getInputControlId(),
            ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29732")
    @TestRail(id = 29732)
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Details Report")
    public void testBothProcessGroups() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName().substring(0, 12),
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName().substring(0, 30)
        );
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            partNames,
            InputControlsEnum.PROCESS_GROUP.getInputControlId(),
            ""
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7644")
    @TestRail(id = 7644)
    @Description("Verify DTC issue counts are correct - Casting DTC Details Report")
    public void testVerifyDtcIssueCountsAreCorrect() {
        List<Element> elementsList = jasperApiUtils.dtcDetailsIssueCountGenericTestReportGeneration(
            "",
            "CYLINDER HEAD"
        );

        softAssertions.assertThat(elementsList.toString().contains("77")).isEqualTo(true);
        softAssertions.assertThat(elementsList.toString().contains("351")).isEqualTo(true);
        softAssertions.assertThat(elementsList.toString().contains("327")).isEqualTo(true);

        softAssertions.assertAll();
    }
}
