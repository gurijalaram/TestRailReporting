package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CastingDtcDetailsReportTests extends TestBase {

    private static String jSessionId = "";

    private static final String reportsJsonFileName = "schemas/api-test-reports-schemas/castingdtc/CastingDtcDetailsReportRequest";
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static final String usdCurrency = CurrencyEnum.USD.getCurrency();
    private static final String gbpCurrency = CurrencyEnum.GBP.getCurrency();
    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String reportCurrencyTestPartName = "40137441.MLDES.0002 (Initial)";
    private static ReportRequest reportRequest;

    private Map<String, String> inputControlNames = new HashMap<>();

    /**
     * This before class method skips the invalid ssl cert issue we have with on prem installs
     *
     * @throws IOException - potential exception
     * @throws NoSuchAlgorithmException - potential exception
     * @throws KeyManagementException - potential exception
     */
    @Before
    public void setupSession() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        JasperApiAuthenticationUtil auth = new JasperApiAuthenticationUtil();
        jSessionId = auth.authenticateJasperApi(driver);

        inputControlNames.put("Cost Metric", "costMetric");
        inputControlNames.put("Mass Metric", "massMetric");
        inputControlNames.put("Process Group", "processGroup");
        inputControlNames.put("DTC Score", "dtcScore");
        inputControlNames.put("Minimum Annual Spend", "annualSpendMin");
        inputControlNames.put("Sort Order", "sortOrder");
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testCostMetricInputControlPpc() {
        inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7412")
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Details Report")
    public void testCostMetricInputControlFbc() {
        inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7411")
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "7507")
    @Description("Verify DTC Score Input Control - No Selection - Casting DTC Details Report")
    public void testDtcScoreNoSelection() {
        inputControlGenericTest(
            "DTC Score",
            ""
        );
    }

    @Test
    @TestRail(testCaseId = "7510")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Details Report")
    public void testDtcScoreLow() {
        inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7513")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Details Report")
    public void testDtcScoreMedium() {
        inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7516")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Details Report")
    public void testDtcScoreHigh() {
        inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7657")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        String minimumAnnualSpendValue = "7820000";
        inputControlGenericTest(
            "Minimum Annual Spend",
            minimumAnnualSpendValue
        );

        JasperReportSummary reportSummary = generateReportSummary(reportRequest);
        String annualSpendValue = reportSummary.getReportHtmlPart().getElementsContainingText("E3-241-4-N").get(5).child(19).text();

        assertThat(annualSpendValue, is(not(equalTo(minimumAnnualSpendValue))));
    }

    @Test
    @TestRail(testCaseId = "7629")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingCasting() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.CASTING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7630")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingMachining() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7631")
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Details Report")
    public void testSortOrderInputControlMaterialScrap() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7632")
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.TOLERANCES.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7633")
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7634")
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Details Report")
    public void testSortOrderInputControlSpecialTooling() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7635")
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum()
        );
    }

    @Test
    @TestRail(testCaseId = "7636")
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Casting DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        inputControlGenericTest(
            "Sort Order",
            SortOrderEnum.DTC_RANK.getSortOrderEnum()
        );
    }

    private void inputControlGenericTest(String inputControlToSet, String valueToSet) {
        reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);

        InputControl inputControl = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControl.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now());

        reportRequest = !valueToSet.isEmpty()
            ? setReportParameterByName(reportRequest, inputControlNames.get(inputControlToSet), valueToSet) :
            reportRequest;
        reportRequest = setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        valueToSet = valueToSet.equals("7820000") ? "7,820,000.00" : valueToSet;

        List<Element> elements = generateReportSummary(reportRequest).getReportHtmlPart().getElementsContainingText(valueToSet);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        assertThat(tdResultElements.toString().contains(valueToSet), is(equalTo(true)));
    }

    private ReportRequest setReportParameterByName(ReportRequest reportRequest, String valueToGet, String valueToSet) {
        reportRequest.getParameters().getReportParameterByName(valueToGet)
            .setValue(Collections.singletonList(valueToSet));
        return reportRequest;
    }

    private JasperReportSummary generateReportSummary(ReportRequest reportRequest) {
        return JasperReportUtil.init(jSessionId).generateJasperReportSummary(reportRequest);
    }
}
