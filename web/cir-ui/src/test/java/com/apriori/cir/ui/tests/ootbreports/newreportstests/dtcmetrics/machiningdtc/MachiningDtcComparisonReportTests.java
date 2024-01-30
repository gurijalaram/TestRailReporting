package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.machiningdtc;

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
}
