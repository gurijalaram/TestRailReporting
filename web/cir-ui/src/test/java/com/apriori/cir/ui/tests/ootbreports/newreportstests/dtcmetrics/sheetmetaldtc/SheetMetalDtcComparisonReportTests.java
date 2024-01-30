package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
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
}
