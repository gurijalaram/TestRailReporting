package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
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
            Constants.PART_NAMES_FOR_INPUT.get(18),
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
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(13),
            Constants.PART_NAMES_FOR_INPUT.get(19),
            Constants.PART_NAMES_FOR_INPUT.get(20)
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
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
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(13),
            Constants.PART_NAMES_FOR_INPUT.get(19),
            Constants.PART_NAMES_FOR_INPUT.get(20)
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
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
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(13),
            Constants.PART_NAMES_FOR_INPUT.get(19),
            Constants.PART_NAMES_FOR_INPUT.get(20)
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
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
        List<String> partNames = Arrays.asList(
            Constants.PART_NAMES_FOR_INPUT.get(13),
            Constants.PART_NAMES_FOR_INPUT.get(19),
            Constants.PART_NAMES_FOR_INPUT.get(20)
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
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
            Constants.PART_NAMES_FOR_INPUT.get(19),
            Constants.PART_NAMES_FOR_INPUT.get(20)
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
            Constants.PART_NAMES_FOR_INPUT.get(13),
            Constants.PART_NAMES_FOR_INPUT.get(21)
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
            Constants.PART_NAMES_FOR_INPUT.get(13),
            Constants.PART_NAMES_FOR_INPUT.get(19)
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
            Constants.PART_NAMES_FOR_INPUT.get(13),
            Constants.PART_NAMES_FOR_INPUT.get(19),
            Constants.PART_NAMES_FOR_INPUT.get(5)
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
            Constants.PART_NAMES_FOR_INPUT.get(20),
            Constants.PART_NAMES_FOR_INPUT.get(21),
            Constants.PART_NAMES_FOR_INPUT.get(22)
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
            Constants.PART_NAMES_FOR_INPUT.get(8),
            Constants.PART_NAMES_FOR_INPUT.get(23),
            Constants.PART_NAMES_FOR_INPUT.get(17)
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
