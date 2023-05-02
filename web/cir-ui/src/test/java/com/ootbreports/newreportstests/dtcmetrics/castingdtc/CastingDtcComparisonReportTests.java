package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import com.apriori.utils.TestRail;
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
            Constants.PART_NAMES_FOR_INPUT.get(2),
            Constants.PART_NAMES_FOR_INPUT.get(4),
            Constants.PART_NAMES_FOR_INPUT.get(1)
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
            Constants.PART_NAMES_FOR_INPUT.get(2),
            Constants.PART_NAMES_FOR_INPUT.get(4),
            Constants.PART_NAMES_FOR_INPUT.get(1)
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
            Constants.PART_NAMES_FOR_INPUT.get(2),
            Constants.PART_NAMES_FOR_INPUT.get(4),
            Constants.PART_NAMES_FOR_INPUT.get(1)
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
            Constants.PART_NAMES_FOR_INPUT.get(2),
            Constants.PART_NAMES_FOR_INPUT.get(4),
            Constants.PART_NAMES_FOR_INPUT.get(1)
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
            Constants.PART_NAMES_FOR_INPUT.get(3),
            Constants.PART_NAMES_FOR_INPUT.get(5),
            Constants.PART_NAMES_FOR_INPUT.get(6)
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
            Constants.PART_NAMES_FOR_INPUT.get(2),
            Constants.PART_NAMES_FOR_INPUT.get(7),
            Constants.PART_NAMES_FOR_INPUT.get(0)
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
            Constants.PART_NAMES_FOR_INPUT.get(4),
            Constants.PART_NAMES_FOR_INPUT.get(1),
            Constants.PART_NAMES_FOR_INPUT.get(8)
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
            Constants.PART_NAMES_FOR_INPUT.get(2),
            Constants.PART_NAMES_FOR_INPUT.get(4),
            Constants.PART_NAMES_FOR_INPUT.get(1)
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
