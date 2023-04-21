package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.List;
import java.util.stream.Collectors;

public class CastingDtcDetailsReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/castingdtc/CastingDtcDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    private static final SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testCostMetricInputControlPpc() {
        String costMetricAssertValue = CostMetricEnum.PIECE_PART_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Cost Metric", costMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("GEAR HOUSING")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("CYLINDER_HEAD")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7412")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Details Report")
    public void testCostMetricInputControlFbc() {
        String costMetricAssertValue = CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Cost Metric", costMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("GEAR HOUSING")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("CYLINDER HEAD")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        String massMetricAssertValue = MassMetricEnum.FINISH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Mass Metric", massMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("GEAR HOUSING")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("CYLINDER HEAD")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        String massMetricAssertValue = MassMetricEnum.ROUGH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Mass Metric", massMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("GEAR HOUSING")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("CYLINDER HEAD")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7510")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Details Report")
    public void testDtcScoreLow() {
        String dtcScoreAssertValue = DtcScoreEnum.LOW.getDtcScoreName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("DTC Score", dtcScoreAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("40128483.MLDES.0001")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(false);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("GEAR HOUSING")).isEqualTo(false);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Low");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(dtcScoreAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7513")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Details Report")
    public void testDtcScoreMedium() {
        String dtcScoreAssertValue = DtcScoreEnum.LOW.getDtcScoreName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("DTC Score", dtcScoreAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("40128483.MLDES.0001")).isEqualTo(false);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("GEAR HOUSING")).isEqualTo(false);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Medium");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(dtcScoreAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7516")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Details Report")
    public void testDtcScoreHigh() {
        String dtcScoreAssertValue = DtcScoreEnum.LOW.getDtcScoreName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("DTC Score", dtcScoreAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("40128483.MLDES.0001")).isEqualTo(false);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(false);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("GEAR HOUSING")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("High");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(dtcScoreAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7657")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        String minimumAnnualSpendValue = "7820000";
        jasperApiUtils.inputControlGenericTest(
            "Minimum Annual Spend",
            minimumAnnualSpendValue
        );

        JasperReportSummary reportSummary = jasperApiUtils.generateReportSummary(reportRequest);
        String annualSpendValue = reportSummary.getReportHtmlPart().getElementsContainingText("E3-241-4-N").get(5).child(19).text();

        assertThat(annualSpendValue, is(not(equalTo(minimumAnnualSpendValue))));
    }

    @Test
    @TestRail(testCaseId = "7629")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingCasting() {
        String sortOrderAssertValue = SortOrderEnum.CASTING_ISSUES.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("JEEP WJ FRONT BRAKE DISC 99-04")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("GEAR HOUSING")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("CYLINDER HEAD")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7630")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingMachining() {
        String sortOrderAssertValue = SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("DTCCASTINGISSUES")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("DTCCASTINGISSUES")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("1205DU1017494_K")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7631")
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Details Report")
    public void testSortOrderInputControlMaterialScrap() {
        String sortOrderAssertValue = SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("OBSTRUCTED MACHINING")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("B2315")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BARCO_R8552931")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7632")
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        String sortOrderAssertValue = SortOrderEnum.TOLERANCES.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("DTCCASTINGISSUES")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("DTCCASTINGISSUES")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("1205DU1017494_K")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7633")
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
        String sortOrderAssertValue = SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("DTCCASTINGISSUES")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("DTCCASTINGISSUES")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("1205DU1017494_K")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7634")
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Details Report")
    public void testSortOrderInputControlSpecialTooling() {
        String sortOrderAssertValue = SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("DU600051458")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("DU200068073_B")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("GEAR HOUSING")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7635")
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        String sortOrderAssertValue = SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("E3-241-4-N")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("40137441.MLDES.0002")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("1205DU1017494_K")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "7636")
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Casting DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        String sortOrderAssertValue = SortOrderEnum.DTC_RANK.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("BARCO_R8761310")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BARCO_R8552931")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("CYLINDER HEAD")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }
}
