package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.CostMetricEnum;
import enums.DtcScoreEnum;
import enums.JasperCirApiPartsEnum;
import enums.MassMetricEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

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
    @Tag(REPORTS_API)
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
    @Tag(REPORTS_API)
    @TestRail(id = 1695)
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 7408)
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 1696)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 7388)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
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
    @Tag(REPORTS_API)
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
    @Tag(REPORTS_API)
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
    //@Tag(REPORTS_API)
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
    //@Tag(REPORTS_API)
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
    @Tag(REPORTS_API)
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
    @Tag(REPORTS_API)
    @TestRail(id = 1700)
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            true
        );
    }
}
