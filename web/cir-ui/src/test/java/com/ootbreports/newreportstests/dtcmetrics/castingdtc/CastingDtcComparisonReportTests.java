package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.CostMetricEnum;
import enums.DtcScoreEnum;
import enums.JasperCirApiPartsEnum;
import enums.MassMetricEnum;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class CastingDtcComparisonReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.CASTING_DTC_COMPARISON.getEndpoint();
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static JasperApiUtils jasperApiUtils;
    private final List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
        JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
        JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
    );

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(id = 7409)
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 7410)
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 7489)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(id = 7390)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(id = 7509)
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Comparison Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_40128483_MLDES_0001.getPartName(),
            JasperCirApiPartsEnum.P_40089252_MLDES_0004_REDRAW.getPartName(),
            JasperCirApiPartsEnum.DU100024720_G.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TestRail(id = 7512)
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Comparison Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.P_40116211_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TestRail(id = 7515)
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Comparison Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TestRail(id = 7544)
    @Description("Verify DTC Score Input Control - All Selection - Casting DTC Comparison Report")
    public void testDtcScoreAll() {
        jasperApiUtils.genericDtcScoreTest(
            true,
            mostCommonPartNames,
            "DTC Score", ""
        );
    }

    @Test
    @TestRail(id = 7656)
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            true
        );
    }
}
