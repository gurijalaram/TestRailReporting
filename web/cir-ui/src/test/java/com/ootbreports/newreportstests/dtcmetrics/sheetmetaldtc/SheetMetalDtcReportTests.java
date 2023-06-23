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
import testsuites.suiteinterface.JasperApiTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class SheetMetalDtcReportTests extends JasperApiAuthenticationUtil {
    private final List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
        JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName(),
        JasperCirApiPartsEnum.DS73_F04604_PIA1.getPartName()
    );
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/sheetmetaldtc/SheetMetalDtcReportRequest");
    private static final String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"3046"})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Report")
    public void testCurrencyCode() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.P_1271576.getPartName(),
            true
        );
    }

    @Test
    @TestRail(testCaseId = {"3043"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7418"})
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"3044"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7398"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7448"})
    @Description("Verify process group input control functionality - Single Selection - Sheet Metal DTC Report")
    public void testSingleProcessGroup() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_2980123_CLAMP.getPartName(),
            JasperCirApiPartsEnum.AP_BRACKET_HANGER.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7532"})
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
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7535"})
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
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7538"})
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
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category({ReportsTest.class, JasperApiTest.class})
    @TestRail(testCaseId = {"3045"})
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
        jasperApiUtils.genericSortOrderDtcTest(
            partNames,
            assertFigures,
            "Sort Order", SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }
}
