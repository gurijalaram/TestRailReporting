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
}
