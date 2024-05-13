package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
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
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class SheetMetalDtcReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SHEET_METAL_DTC.getEndpoint();
    private String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.SHEET_METAL_DTC;
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
        JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName(),
        JasperCirApiPartsEnum.DS73_F04604_PIA1.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tags({
        @Tag(JASPER_API),
        @Tag(API_SANITY)
    })
    @TmsLink("3046")
    @TestRail(id = 3046)
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Report")
    public void testCurrencyCode() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            true,
            false
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3043")
    @TestRail(id = 3043)
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7418")
    @TestRail(id = 7418)
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.COST_METRIC.getInputControlId(), CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3044")
    @TestRail(id = 3044)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7398")
    @TestRail(id = 7398)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7448")
    @TestRail(id = 7448)
    @Description("Verify process group input control functionality - Single Selection - Sheet Metal DTC Report")
    public void testSingleProcessGroup() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            InputControlsEnum.PROCESS_GROUP.getInputControlId(), ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7532")
    @TestRail(id = 7532)
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_LINK.getPartName(),
            JasperCirApiPartsEnum.P_2551580.getPartName(),
            JasperCirApiPartsEnum.P_0903238.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7535")
    @TestRail(id = 7535)
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.P_1684402_TOP_BRACKET.getPartName(),
            JasperCirApiPartsEnum.P_2840020_BRACKET.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7538")
    @TestRail(id = 7538)
    @Description("Verify DTC Score Input Control - High Selection - Sheet Metal DTC Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName(),
            JasperCirApiPartsEnum.DS73_F04604_PIA1.getPartName(),
            JasperCirApiPartsEnum.BRACKET_SHORTENED_ISSUES.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            InputControlsEnum.DTC_SCORE.getInputControlId(), DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3045")
    @TestRail(id = 3045)
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Report")
    public void testSortOrderAnnualSpend() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName()
        );
        List<Double> assertFigures = Arrays.asList(
            4406.160458693279,
            6753.127001618986
        );
        jasperApiUtils.genericSortOrderAnnualSpendDtcTest(
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29818")
    @TestRail(id = 29818)
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Report")
    public void testSortOrderManufacturingIssues() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29819")
    @TestRail(id = 29819)
    @Description("Verify Sort Order input control functions correctly - Bends - Sheet Metal DTC Report")
    public void testSortOrderBends() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.BENDS.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29820")
    @TestRail(id = 29820)
    @Description("Verify Sort Order input control functions correctly - Bends - Sheet Metal DTC Report")
    public void testSortOrderTolerances() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29821")
    @TestRail(id = 29821)
    @Description("Verify Sort Order input control functions correctly - Machining Time - Sheet Metal DTC Report")
    public void testSortOrderMachiningTime() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName()
        );
        jasperApiUtils.genericSortOrderDtcComparisonTest(
            partNames,
            InputControlsEnum.SORT_ORDER.getInputControlId(), SortOrderEnum.MACHINING_TIME.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29704")
    @TestRail(id = 29704)
    @Description("Verify Minimum Annual Spend input control functions correctly - Sheet Metal DTC Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            3
        );
    }
}
