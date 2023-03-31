package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartData;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

public class CastingDtcComparisonReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcComparisonReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    private static SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @TestRail(testCaseId = "7409")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7410")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7489")
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7390")
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        /*jasperApiUtils.inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );*/

        JasperReportSummary jasperReportSummary = jasperApiUtils.massMetricGenericTest(MassMetricEnum.ROUGH_MASS.getMassMetricName());

        /**
         * Gear Housing (Initial) is high dtc
         * JEEP WJ FRONT BRAKE DISC 99-04 (Initial) is medium dtc
         * 40128483.MLDES.0001 (Initial) is low dtc
         */

        String lowDtcPartName = "40128483.MLDES.0001 (Initial)";
        String mediumDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String highDtcPartName = "GEAR HOUSING (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);*/
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7509")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Comparison Report")
    public void testDtcScoreLow() {
        /*jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );*/

        JasperReportSummary jasperReportSummary = jasperApiUtils.dtcScoreGenericTest(DtcScoreEnum.LOW.getDtcScoreName());

        /**
         * Gear Housing (Initial) is high dtc
         * JEEP WJ FRONT BRAKE DISC 99-04 (Initial) is medium dtc
         * 40128483.MLDES.0001 (Initial) is low dtc
         */

        String lowDtcPartName = "40128483.MLDES.0001 (Initial)";
        String mediumDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String highDtcPartName = "GEAR HOUSING (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);*/
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7512")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Comparison Report")
    public void testDtcScoreMedium() {
        /*jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );*/

        JasperReportSummary jasperReportSummary = jasperApiUtils.dtcScoreGenericTest(DtcScoreEnum.MEDIUM.getDtcScoreName());

        /**
         * Gear Housing (Initial) is high dtc
         * JEEP WJ FRONT BRAKE DISC 99-04 (Initial) is medium dtc
         * 40128483.MLDES.0001 (Initial) is low dtc
         */

        String lowDtcPartName = "40128483.MLDES.0001 (Initial)";
        String mediumDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String highDtcPartName = "GEAR HOUSING (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);*/
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7515")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Comparison Report")
    public void testDtcScoreHigh() {
        /*jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );*/

        JasperReportSummary jasperReportSummary = jasperApiUtils.dtcScoreGenericTest(DtcScoreEnum.HIGH.getDtcScoreName());

        /**
         * Gear Housing (Initial) is high dtc
         * JEEP WJ FRONT BRAKE DISC 99-04 (Initial) is medium dtc
         * 40128483.MLDES.0001 (Initial) is low dtc
         */

        String lowDtcPartName = "40128483.MLDES.0001 (Initial)";
        String mediumDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String highDtcPartName = "GEAR HOUSING (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);*/
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7544")
    @Description("Verify DTC Score Input Control - All Selection - Casting DTC Comparison Report")
    public void testDtcScoreAll() {
        /*jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            ""
        );*/
        JasperReportSummary jasperReportSummary = jasperApiUtils.minAnnualSpendGenericTest();

        /**
         * Gear Housing (Initial) is high dtc
         * JEEP WJ FRONT BRAKE DISC 99-04 (Initial) is medium dtc
         * 40128483.MLDES.0001 (Initial) is low dtc
          */

        String lowDtcPartName = "40128483.MLDES.0001 (Initial)";
        String mediumDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String highDtcPartName = "Gear Housing (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);*/

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7656")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.minAnnualSpendGenericTest();

        String negativePartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String positivePartName = "E3-241-4-N (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        /*softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(negativePartName)).isEqualTo(null);*/

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getHoleIssues()).isEqualTo(10);
        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getMaterialIssues()).isEqualTo(0);
        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getRadiusIssues()).isEqualTo(18);
        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getDraftIssues()).isEqualTo(15);
        softAssertions.assertAll();
    }
}
