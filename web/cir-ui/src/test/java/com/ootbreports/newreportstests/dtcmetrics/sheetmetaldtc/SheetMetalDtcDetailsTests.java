package com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
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
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SheetMetalDtcDetailsTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/sheetmetaldtc/SheetMetalDtcDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
    private static ReportRequest reportRequest;
    private static JasperApiUtils jasperApiUtils;

    private static final SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        reportRequest = jasperApiUtils.getReportRequest();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7421"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricPpc() {
        String costMetricAssertValue = CostMetricEnum.PIECE_PART_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V2")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7422"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricFbc() {
        String costMetricAssertValue = CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Cost Metric", costMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V2")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Cost");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(costMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"7401"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        String massMetricAssertValue = MassMetricEnum.FINISH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V2")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7402"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        String massMetricAssertValue = MassMetricEnum.ROUGH_MASS.getMassMetricName();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Mass Metric", massMetricAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V2")).isEqualTo(true);

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Mass");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(massMetricAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7682"})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Details Report")
    public void testSortOrderManufacturingIssues() {
        String sortOrderAssertValue = SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V2")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7681"})
    @Description("Verify Sort Order input control functions correctly - Bends- Sheet Metal DTC Details Report")
    public void testSortOrderBends() {
        String sortOrderAssertValue = SortOrderEnum.BENDS.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("BRACKET_SHORTENED")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_SHORTENED_ISSUES")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V1_HEMS")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7677"})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Details Report")
    public void testSortOrderTolerances() {
        String sortOrderAssertValue = SortOrderEnum.TOLERANCES.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V2")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V3")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7678"})
    @Description("Verify Sort Order input control functions correctly - Machining Time - Sheet Metal DTC Details Report")
    public void testSortOrderMachiningTime() {
        String sortOrderAssertValue = SortOrderEnum.MACHINING_TIME.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V3")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V4")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6).siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7679"})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Details Report")
    public void testSortOrderAnnualSpend() {
        String sortOrderAssertValue = SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("3575137")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("3575136")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6)
            .siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7680"})
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Sheet Metal DTC Details Report")
    public void testSortOrderDtcRank() {
        String sortOrderAssertValue = SortOrderEnum.DTC_RANK.getSortOrderEnum();
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Sort Order", sortOrderAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("BRACKET_SHORTENED")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V1_HEMS")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort").get(6)
            .siblingElements().get(6).toString().contains(sortOrderAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7379"})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Details Report")
    public void testCurrencyCodeInputControl() {
        String currencyAssertValue = CurrencyEnum.USD.getCurrency();
        JasperReportSummary jasperReportSummaryUsd = jasperApiUtils.genericTest("Currency", currencyAssertValue);

        softAssertions.assertThat(jasperReportSummaryUsd.getReportHtmlPart().getElementsByAttributeValue("colspan", "3").get(6).text())
            .isEqualTo(currencyAssertValue);

        String usdAnnualSpend = jasperReportSummaryUsd.getReportHtmlPart().getElementsByAttributeValue("colspan", "4").get(9).text();

        currencyAssertValue = CurrencyEnum.GBP.getCurrency();
        JasperReportSummary jasperReportSummaryGbp = jasperApiUtils.genericTest("Currency", currencyAssertValue);

        softAssertions.assertThat(jasperReportSummaryGbp.getReportHtmlPart().getElementsByAttributeValue("colspan", "3").get(6).text())
            .isEqualTo(currencyAssertValue);

        String gbpAnnualSpend = jasperReportSummaryGbp.getReportHtmlPart().getElementsByAttributeValue("colspan", "4").get(9).text();

        softAssertions.assertThat(usdAnnualSpend).isNotEqualTo(gbpAnnualSpend);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3048"})
    @Description("Verify Process Group input control functions correctly - Single Selection")
    public void testSingleProcessGroup() {
        String processGroupAssertValue = "Sheet Metal";
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTest("Process Group", processGroupAssertValue);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        softAssertions.assertThat(partNumberElements.get(6).toString().contains("1271576")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(7).toString().contains("BRACKET_V1")).isEqualTo(true);
        softAssertions.assertThat(partNumberElements.get(8).toString().contains("BRACKET_V2")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Process Group").get(6)
            .siblingElements().get(10).toString().contains(processGroupAssertValue)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7534"})
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreLow() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7537"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreMedium() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7540"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreHigh() {
        jasperApiUtils.inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }
}
