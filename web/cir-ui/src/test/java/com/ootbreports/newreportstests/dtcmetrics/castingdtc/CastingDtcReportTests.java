package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

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
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsApiTest;
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

public class CastingDtcReportTests extends TestBase {

    private static String jSessionId = "";
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static final String usdCurrency = CurrencyEnum.USD.getCurrency();
    private static final String gbpCurrency = CurrencyEnum.GBP.getCurrency();
    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String reportCurrencyTestPartName = "40137441.MLDES.0002 (Initial)";

    private static final String reportsJsonFileName = "ReportCastingDTCRequest";
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
    }

    @Test
    @Category(ReportsApiTest.class)
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        String currencyCode = "currencyCode";
        reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);

        InputControl inputControl = JasperReportUtil.init(jSessionId)
            .getInputControls();
        String exportSetValue = inputControl.getExportSetName().getOption(exportSetName).getValue();

        String currentDateTime = DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now());

        reportRequest = setReportParameterByName(reportRequest, currencyCode, usdCurrency);
        reportRequest = setReportParameterByName(reportRequest, "exportSetName", exportSetValue);
        reportRequest = setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        ChartDataPoint usdChartDataPoint = generateReportAndGetChartDataPoint(reportRequest);

        String usdFullyBurdenedCost = getFullyBurdenedCostFromChartDataPoint(usdChartDataPoint);
        double usdAnnualSpend = getAnnualSpendFromChartDataPoint(usdChartDataPoint);

        reportRequest = setReportParameterByName(reportRequest, currencyCode, gbpCurrency);

        ChartDataPoint gbpChartDataPoint = generateReportAndGetChartDataPoint(reportRequest);

        String gbpFullyBurdenedCost = getFullyBurdenedCostFromChartDataPoint(gbpChartDataPoint);
        double gbpAnnualSpend = getAnnualSpendFromChartDataPoint(gbpChartDataPoint);

        assertThat(usdFullyBurdenedCost.equals(gbpFullyBurdenedCost), equalTo(false));
        assertThat(gbpAnnualSpend, is(not(equalTo(usdAnnualSpend))));
    }

    @Test
    @Category(ReportsApiTest.class)
    @TestRail(testCaseId = {"1695"})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Report")
    public void testCostMetricInputControlPpc() {
        inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsApiTest.class)
    @TestRail(testCaseId = {"7408"})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        inputControlGenericTest(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"1696"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7388"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlGenericTest(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"7454"})
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieCastingOnly() {
        inputControlGenericTest(
            "Process Group",
            ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
    }

    @Test
    @TestRail(testCaseId = {"7453"})
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Report")
    public void testProcessGroupInputControlSandCastingOnly() {
        inputControlGenericTest(
            "Process Group",
            ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );
    }

    @Test
    @TestRail(testCaseId = {"7455"})
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Report")
    public void testProcessGroupInputControlDieAndSandCasting() {
        inputControlGenericTest(
            "Process Group",
            ""
        );
    }

    @Test
    @TestRail(testCaseId = "7505")
    @Description("Verify DTC Score Input Control - No Selection - Casting DTC Report")
    public void testDtcScoreNoSelection() {
        inputControlGenericTest(
            "DTC Score",
            ""
        );
    }

    @Test
    @TestRail(testCaseId = "7508")
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Report")
    public void testDtcScoreLow() {
        inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7511")
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Report")
    public void testDtcScoreMedium() {
        inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "7514")
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Report")
    public void testDtcScoreHigh() {
        inputControlGenericTest(
            "DTC Score",
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "1700")
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Report")
    public void testMinimumAnnualSpend() {
        String minimumAnnualSpendValue = "7820000";
        inputControlGenericTest(
            "Minimum Annual Spend",
            minimumAnnualSpendValue
        );

        JasperReportSummary reportSummary = generateReportSummary(reportRequest);
        ChartDataPoint chartDataPoint = reportSummary.getChartDataPointByPartName("E3-241-4-N (Initial)");
        List<ChartDataPoint> chartDataPointList = reportSummary.getChartDataPoints();

        assertThat(chartDataPoint.getAnnualSpend(), is(not(equalTo(minimumAnnualSpendValue))));
        assertThat(chartDataPointList.size(), is(equalTo(1)));
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

    private ChartDataPoint generateReportAndGetChartDataPoint(ReportRequest reportRequest) {
        JasperReportSummary jasperReportSummary = generateReportSummary(reportRequest);
        return jasperReportSummary.getChartDataPointByPartName(reportCurrencyTestPartName);
    }

    private JasperReportSummary generateReportSummary(ReportRequest reportRequest) {
        return JasperReportUtil.init(jSessionId).generateJasperReportSummary(reportRequest);
    }

    private Double getAnnualSpendFromChartDataPoint(ChartDataPoint dataToUse) {
        return dataToUse.getAnnualSpend();
    }

    private String getFullyBurdenedCostFromChartDataPoint(ChartDataPoint dataToUse) {
        return dataToUse.getFullyBurdenedCost();
    }
}
