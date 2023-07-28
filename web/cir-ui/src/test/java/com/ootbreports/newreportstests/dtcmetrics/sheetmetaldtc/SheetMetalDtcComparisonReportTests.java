package com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import static com.apriori.TestSuiteType.TestSuite.REPORTS;

import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.CostMetricEnum;
import enums.JasperCirApiPartsEnum;
import enums.MassMetricEnum;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class SheetMetalDtcComparisonReportTests extends JasperApiAuthenticationUtil {
    private final List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.P_1271576.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V1_REV_1.getPartName(),
        JasperCirApiPartsEnum.BRACKET_V2_REV_1.getPartName()
    );
    private static final String reportsJsonFileName = JasperApiEnum.SHEET_METAL_DTC_COMPARISON.getEndpoint();
    private static final String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7419})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Comparison Report")
    public void testCostMetricPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7420})
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Comparison Report")
    public void testCostMetricFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7399})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7400})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
