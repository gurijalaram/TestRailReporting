package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.castingdtc;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CastingDtcReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.CASTING_DTC.getEndpoint();
    private String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.CASTING_DTC;
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.B2315.getPartName(),
        JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
        JasperCirApiPartsEnum.CASE_08.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 1699)
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
            true,
            false
        );
    }

    @Test
    @TestRail(id = 1695)
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 7408)
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 1696)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(id = 7388)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(id = 7454)
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieCastingOnly() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.CASE_08.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
    }

    @Test
    @TestRail(id = 7453)
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Report")
    public void testProcessGroupInputControlSandCastingOnly() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.B2315.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8762839_ORIGIN.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );
    }

    @Test
    @TestRail(id = 7455)
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieAndSandCasting() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.B2315.getPartName(),
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup().concat(", ").concat(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
        );
    }

    @Test
    @TestRail(id = 7508)
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.B2315.getPartName(),
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.P_40089252_MLDES_0004_REDRAW.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TestRail(id = 7511)
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.CASE_08.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8762839_ORIGIN.getPartName(),
            JasperCirApiPartsEnum.C192308.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TestRail(id = 7514)
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName(),
            JasperCirApiPartsEnum.P_40144122_MLDES_0002.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8761310.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TestRail(id = 1700)
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            true
        );
    }
}
