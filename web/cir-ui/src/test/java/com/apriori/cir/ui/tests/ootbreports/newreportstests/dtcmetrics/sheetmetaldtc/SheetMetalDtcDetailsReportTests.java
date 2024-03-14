package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
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

public class SheetMetalDtcDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SHEET_METAL_DTC_DETAILS.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.SHEET_METAL_DTC_DETAILS;
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
    @Tag(JASPER_API)
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
    @Tag(JASPER_API)
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
    @Tag(JASPER_API)
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
    @Tag(JASPER_API)
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
    @Tag(JASPER_API)
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
    @Tag(JASPER_API)
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
    @Tag(JASPER_API)
    @TmsLink("7534")
    @TestRail(id = 7534)
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_0903238.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.P_1100149.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.P_1684443_OUTRIGGER_CAM.getPartName().substring(0, 21)
        );
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7537")
    @TestRail(id = 7537)
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_3574715.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.P_3574688.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName().substring(0, 13)
        );
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7540")
    @TestRail(id = 7540)
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreHigh() {
        jasperApiUtils.genericDtcDetailsTest(
            mostCommonPartNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("29706")
    @TestRail(id = 29706)
    @Description("Verify Minimum Annual Spend input control functions correctly - Sheet Metal DTC Details Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcDetailsTest(true);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7677")
    @TestRail(id = 7677)
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BRACKET_V1.getPartName().substring(0, 10),
            JasperCirApiPartsEnum.BRACKET_V2.getPartName().substring(0, 10)
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
    @TmsLink("7678")
    @TestRail(id = 7678)
    @Description("Verify Sort Order input control functions correctly - Machining Time - Sheet Metal DTC Details Report")
    public void testSortOrderInputControlMachiningTime() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_1271576.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.BRACKET_V3.getPartName().substring(0, 10)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.MACHINING_TIME.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7679")
    @TestRail(id = 7679)
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_1271576.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.P_3575137.getPartName().substring(0, 7)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
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
    @TmsLink("7680")
    @TestRail(id = 7680)
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Sheet Metal DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BRACKET_SHORTENED.getPartName().substring(0, 17),
            JasperCirApiPartsEnum.BRACKET_V1.getPartName().substring(0, 10)
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
    @TmsLink("7681")
    @TestRail(id = 7681)
    @Description("Verify Sort Order input control functions correctly - Bends - Sheet Metal DTC Details Report")
    public void testSortOrderInputControlBends() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BRACKET_SHORTENED.getPartName().substring(0, 17),
            JasperCirApiPartsEnum.BRACKET_SHORTENED_ISSUES.getPartName().substring(0, 24)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.BENDS.getSortOrderEnum()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7682")
    @TestRail(id = 7682)
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Details Report")
    public void testSortOrderInputControlManufacturingIssues() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_1271576.getPartName().substring(0, 7),
            JasperCirApiPartsEnum.BRACKET_V1.getPartName().substring(0, 10)
        );
        List<String> assertFigures = Arrays.asList(
            "0.0",
            "0.0"
        );
        jasperApiUtils.genericSortOrderDtcDetailsTest(
            partNames,
            assertFigures,
            InputControlsEnum.SORT_ORDER.getInputControlId(),
            SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum()
        );
    }
}
