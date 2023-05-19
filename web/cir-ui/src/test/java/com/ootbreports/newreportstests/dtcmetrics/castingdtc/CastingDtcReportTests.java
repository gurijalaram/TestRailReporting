package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
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

public class CastingDtcReportTests extends JasperApiAuthenticationUtil {
    private final List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.B2315.getPartName(),
        JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
        JasperCirApiPartsEnum.CASE_08.getPartName()
    );

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
            true
        );
    }

    @Test
    @TestRail(testCaseId = {"1695"})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Report")
    public void testCostMetricInputControlPpc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            mostCommonPartNames,
            true
        );
    }

    @Test
    @TestRail(testCaseId = {"7408"})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            mostCommonPartNames,
            true
        );
    }

    @Test
    @TestRail(testCaseId = {"1696"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            mostCommonPartNames,
            true
        );
    }

    @Test
    @TestRail(testCaseId = {"7388"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            mostCommonPartNames,
            true
        );
    }

    @Test
    @TestRail(testCaseId = {"7454"})
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieCastingOnly() {
        List<String> miscData = Arrays.asList(
            "Process Group",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.CASE_08.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = {"7453"})
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Report")
    public void testProcessGroupInputControlSandCastingOnly() {
        List<String> miscData = Arrays.asList(
            "Process Group",
            ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.B2315.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8762839_ORIGIN.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = {"7455"})
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieAndSandCasting() {
        List<String> miscData = Arrays.asList(
            "Process Group",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup().concat(", ").concat(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.B2315.getPartName(),
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            miscData,
            partNames
        );
    }

    @Test
    @TestRail(testCaseId = "7508")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Report")
    public void testDtcScoreLow() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.B2315.getPartName(),
            JasperCirApiPartsEnum.P_40090936_MLDES_0004.getPartName(),
            JasperCirApiPartsEnum.P_40089252_MLDES_0004_REDRAW.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            miscData,
            partNames,
            true
        );
    }

    @Test
    @TestRail(testCaseId = "7511")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Report")
    public void testDtcScoreMedium() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.CASE_08.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8762839_ORIGIN.getPartName(),
            JasperCirApiPartsEnum.C192308.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            miscData,
            partNames,
            true
        );
    }

    @Test
    @TestRail(testCaseId = "7514")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Report")
    public void testDtcScoreHigh() {
        List<String> miscData = Arrays.asList(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.BARCO_R8552931.getPartName(),
            JasperCirApiPartsEnum.P_40144122_MLDES_0002.getPartName(),
            JasperCirApiPartsEnum.BARCO_R8761310.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            miscData,
            partNames,
            true
        );
    }

    @Test
    @TestRail(testCaseId = "1700")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Report")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            true
        );
    }
}
