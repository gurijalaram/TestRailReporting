package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.plasticdtc;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlasticDtcDetailsReportTests extends JasperApiAuthenticationUtil {
    private List<String> partNames = Collections.singletonList(JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName());
    private String reportsJsonFileName = JasperApiEnum.PLASTIC_DTC_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.PLASTIC_DTC_DETAILS;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("7406")
    @TestRail(id = 7406)
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Details Report ")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7407")
    @TestRail(id = 7407)
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Details Report ")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("7381")
    @TestRail(id = 7381)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7382")
    @TestRail(id = 7382)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Details Report ")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TmsLink("7522")
    @TestRail(id = 7522)
    @Description("Verify DTC Score Input Control - Low Selection - Plastic DTC Details Report")
    public void testDtcScoreLow() {
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7525")
    @TestRail(id = 7525)
    @Description("Verify DTC Score Input Control - Medium Selection - Plastic DTC Details Report")
    public void testDtcScoreMedium() {
        List<String> partNames1 = Arrays.asList(
            "", ""
        );
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames1,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("7528")
    @TestRail(id = 7528)
    @Description("Verify DTC Score Input Control - High Selection - Plastic DTC Details Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            "", ""
        );
        jasperApiUtils.genericDtcDetailsTest(
            false,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TmsLink("10013")
    @TestRail(id = {10013})
    @Description("Verify currency code functionality works correctly - Plastic DTC Details Report")
    public void testCurrencyCodeFunctionality() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName(),
            false,
            true
        );
    }

    @Test
    @TmsLink("29683")
    @TestRail(id = {29683})
    @Description("Test process group input control works correctly - Plastic DTC Details Report")
    public void testProcessGroupFunctionality() {
        jasperApiUtils.genericProcessGroupDtcDetailsTest(
            partNames,
            "Process Group", ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()
        );
    }

    @Test
    @TmsLink("29702")
    @TestRail(id = 29702)
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcDetailsTest(true);
    }
}
