package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.plasticdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS_API;

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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlasticDtcComparisonReportTests extends JasperApiAuthenticationUtil {

    private List<String> partNames = Collections.singletonList(JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName());
    private String reportsJsonFileName = JasperApiEnum.PLASTIC_DTC_COMPARISON.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.PLASTIC_DTC_COMPARISON;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7404")
    @TestRail(id = 7404)
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7405")
    @TestRail(id = 7405)
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7583")
    @TestRail(id = 7383)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7384")
    @TestRail(id = 7384)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("1370")
    @TestRail(id = {1370})
    @Description("Verify currency code functionality works correctly - Plastic DTC Comparison Report")
    public void testCurrencyCodeFunctionality() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName(),
            false,
            true
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7521")
    @TestRail(id = 7521)
    @Description("Verify DTC Score Input Control - Low Selection - Plastic DTC Comparison Report")
    public void testDtcScoreLow() {
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7524")
    @TestRail(id = 7524)
    @Description("Verify DTC Score Input Control - Medium Selection - Plastic DTC Comparison Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            "", ""
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7527")
    @TestRail(id = 7527)
    @Description("Verify DTC Score Input Control - High Selection - Plastic DTC Comparison Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            "", ""
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("7542")
    @TestRail(id = 7542)
    @Description("Verify DTC Score Input Control - All Selection - Plastic DTC Comparison Report")
    public void testDtcScoreAll() {
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", ""
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("29684")
    @TestRail(id = {29684})
    @Description("Test process group input control works correctly - Plastic DTC Comparison Report")
    public void testProcessGroupFunctionality() {
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("29703")
    @TestRail(id = 29703)
    @Description("Verify Minimum Annual Spend input control functions correctly - Plastic DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            0
        );
    }
}
