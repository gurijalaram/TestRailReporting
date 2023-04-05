package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CastingDtcComparisonReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcComparisonReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();

    private static final SoftAssertions softAssertions = new SoftAssertions();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = "7409")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        String costMetricAssertValue = CostMetricEnum.PIECE_PART_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        String firstDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String secondDtcPartName = "CYLINDER HEAD (Initial)";
        String thirdDtcPartName = "40137441.MLDES.0002 (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7410")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        String costMetricAssertValue = CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        String firstDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String secondDtcPartName = "CYLINDER HEAD (Initial)";
        String thirdDtcPartName = "40137441.MLDES.0002 (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7489")
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        String massMetricAssertValue = MassMetricEnum.FINISH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        String firstDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String secondDtcPartName = "CYLINDER HEAD (Initial)";
        String thirdDtcPartName = "40137441.MLDES.0002 (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7390")
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        String massMetricAssertValue = MassMetricEnum.ROUGH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        String firstDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String secondDtcPartName = "CYLINDER HEAD (Initial)";
        String thirdDtcPartName = "40137441.MLDES.0002 (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(firstDtcPartName).getPartName()).isEqualTo(firstDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(secondDtcPartName).getPartName()).isEqualTo(secondDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(thirdDtcPartName).getPartName()).isEqualTo(thirdDtcPartName);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7509")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Comparison Report")
    public void testDtcScoreLow() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.LOW.getDtcScoreName());

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
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Low");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("Low")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7512")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Comparison Report")
    public void testDtcScoreMedium() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName());

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
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName)).isEqualTo(null);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Medium");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("Medium")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7515")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Comparison Report")
    public void testDtcScoreHigh() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", DtcScoreEnum.HIGH.getDtcScoreName());

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
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName)).isEqualTo(null);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName)).isEqualTo(null);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("High");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).toString().contains("High")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7544")
    @Description("Verify DTC Score Input Control - All Selection - Casting DTC Comparison Report")
    public void testDtcScoreAll() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("DTC Score", "");

        /**
         * Gear Housing (Initial) is high dtc
         * JEEP WJ FRONT BRAKE DISC 99-04 (Initial) is medium dtc
         * 40128483.MLDES.0001 (Initial) is low dtc
          */

        /*String lowDtcPartName = "40128483.MLDES.0001 (Initial)";
        String mediumDtcPartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String highDtcPartName = "GEAR HOUSING (Initial)";*/
        HashMap<String, String> partNamesForAssertions = new HashMap<>();
        partNamesForAssertions.put("Low", "40128483.MLDES.0001 (Initial)");
        partNamesForAssertions.put("Medium", "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)");
        partNamesForAssertions.put("High", "GEAR HOUSING (Initial)");

        jasperApiUtils.performChartAsserts(jasperReportSummary, partNamesForAssertions);

        /*softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(lowDtcPartName).getPartName()).isEqualTo(lowDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(mediumDtcPartName).getPartName()).isEqualTo(mediumDtcPartName);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(highDtcPartName).getPartName()).isEqualTo(highDtcPartName);*/

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("High");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        assertThat(tdResultElements.get(0).toString().contains("High, Low, Medium"), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "7656")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Minimum Annual Spend", "7820000");

        String negativePartName = "JEEP WJ FRONT BRAKE DISC 99-04 (Initial)";
        String positivePartName = "E3-241-4-N (Initial)";

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(1).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(2).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(3).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(4).getChartDataPointByPartName(negativePartName)).isEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getChartData().get(5).getChartDataPointByPartName(negativePartName)).isEqualTo(null);

        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getHoleIssues()).isEqualTo(10);
        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getMaterialIssues()).isEqualTo(0);
        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getRadiusIssues()).isEqualTo(18);
        softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPointByPartName(positivePartName).getDraftIssues()).isEqualTo(15);

        String minAnnualSpendAssertValue = "7,820,000.00";
        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(minAnnualSpendAssertValue);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.toString().contains(minAnnualSpendAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }
}
