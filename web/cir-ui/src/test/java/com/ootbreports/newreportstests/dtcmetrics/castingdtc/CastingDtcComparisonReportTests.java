package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class CastingDtcComparisonReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcComparisonReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = "7409")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7410")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7489")
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7390")
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7509")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Comparison Report")
    public void testDtcScoreLow() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_40128483_MLDES_0001.getPartName(),
            JasperCirApiPartsEnum.P_40089252_MLDES_0004_REDRAW.getPartName(),
            JasperCirApiPartsEnum.DU100024720_G.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7512")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Comparison Report")
    public void testDtcScoreMedium() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.P_40116211_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7515")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Comparison Report")
    public void testDtcScoreHigh() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7544")
    @Description("Verify DTC Score Input Control - All Selection - Casting DTC Comparison Report")
    public void testDtcScoreAll() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            ""
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.JEEP_WJ_FRONT_BRAKE_DISC_99_04.getPartName(),
            JasperCirApiPartsEnum.GEAR_HOUSING.getPartName(),
            JasperCirApiPartsEnum.CYLINDER_HEAD.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @TestRail(testCaseId = "7656")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            false
        );
    }
}