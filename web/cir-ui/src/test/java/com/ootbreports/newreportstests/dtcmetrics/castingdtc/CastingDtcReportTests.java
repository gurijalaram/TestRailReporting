package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.List;
import java.util.stream.Collectors;

public class CastingDtcReportTests extends TestBase {

    private static String jSessionId = "";
    private static final String exportSetName = ExportSetEnum.CASTING_DTC.getExportSetName();
    private static final String usdCurrency = CurrencyEnum.USD.getCurrency();
    private static final String gbpCurrency = CurrencyEnum.GBP.getCurrency();
    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String reportCurrencyTestPartName = "40137441.MLDES.0002 (Initial)";

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
    }

    @Test
    @Category(ReportsApiTest.class)
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        ReportRequest reportRequest = ReportRequest.initFromJsonFile("ReportCastingDTCRequest");

        InputControl inputControl = JasperReportUtil.init(jSessionId)
            .getInputControls();
        String exportSetValue = inputControl.getExportSetName().getOption(exportSetName).getValue();

        // 1 - Generate report with USD currency setting
        /*reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Collections.singletonList(usdCurrency));

        reportRequest.getParameters().getReportParameterByName("exportSetName")
            .setValue(Collections.singletonList(exportSetValue));

        reportRequest.getParameters().getReportParameterByName("latestExportDate")
            .setValue(Collections.singletonList(currentDateTime));*/

        String currentDateTime = DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now());

        reportRequest = setReportParameterByName(reportRequest, "currencyCode", usdCurrency);
        reportRequest = setReportParameterByName(reportRequest, "exportSetName", exportSetValue);
        reportRequest = setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        ChartDataPoint usdChartDataPoint = generateReportAndGetSummary(reportRequest);

        // 2 - Get values from USD report
        String usdFullyBurdenedCost = usdChartDataPoint.getFullyBurdenedCost();
        double usdAnnualSpend = usdChartDataPoint.getAnnualSpend();

        // 3- Change currency to GBP and re-generate report
        assertThat(usdFullyBurdenedCost, is(notNullValue()));
        assertThat(usdAnnualSpend, is(notNullValue()));

        /*reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Collections.singletonList(gbpCurrency));*/
        reportRequest = setReportParameterByName(reportRequest, "currencyCode", gbpCurrency);

        ChartDataPoint gbpChartDataPoint = generateReportAndGetSummary(reportRequest);

        // 4 - Get values from GBP report
        String gbpFullyBurdenedCost = gbpChartDataPoint.getFullyBurdenedCost();
        double gbpAnnualSpend = gbpChartDataPoint.getAnnualSpend();

        // 5 - Assert that USD values are not equal to GBP values
        assertThat(usdFullyBurdenedCost.equals(gbpFullyBurdenedCost), equalTo(false));
        assertThat(gbpAnnualSpend, is(not(equalTo(usdAnnualSpend))));
    }

    @Test
    @Category(ReportsApiTest.class)
    @TestRail(testCaseId = {"1695"})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Report")
    public void testCostMetricInputControlPpc() {
        costMetricInputControlGenericTest(CostMetricEnum.PIECE_PART_COST.getCostMetricName());
    }

    @Test
    @Category(ReportsApiTest.class)
    @TestRail(testCaseId = {"7408"})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        costMetricInputControlGenericTest(CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName());
    }

    private void costMetricInputControlGenericTest(String costMetricValue) {
        ReportRequest reportRequest = ReportRequest.initFromJsonFile("ReportCastingDTCRequest");

        InputControl inputControl = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControl.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now());

        reportRequest = setReportParameterByName(reportRequest, "costMetric", costMetricValue);
        reportRequest = setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        List<Element> elements = JasperReportUtil.init(jSessionId).generateJasperReportSummary(reportRequest).getReportHtmlPart().getElementsContainingText(costMetricValue);
        List<Element> tdPPCElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        assertThat(tdPPCElements.toString().contains(costMetricValue), is(equalTo(true)));
    }

    private ReportRequest setReportParameterByName(ReportRequest reportRequest, String valueToGet, String valueToSet) {
        reportRequest.getParameters().getReportParameterByName(valueToGet)
            .setValue(Collections.singletonList(valueToSet));
        return reportRequest;
    }

    private ChartDataPoint generateReportAndGetSummary(ReportRequest reportRequest) {
        JasperReportSummary jasperReportSummary = JasperReportUtil.init(jSessionId)
            .generateJasperReportSummary(reportRequest);
        return jasperReportSummary.getChartDataPointByPartName(reportCurrencyTestPartName);
    }

    /**
     * Example, created by Vlad Z
     */
    @Test
    @Ignore
    public void exampleOfInputParamsUsage() {
        InputControl inputControl =  JasperReportUtil.init(jSessionId)
            .getInputControls();

        inputControl.getExportSetName().getOption("---01-dtc-casting").getValue();
        inputControl.getCostMetric().getValue();
        inputControl.getInputControlStateByName("requiredInputControl").getOption("requiredOptionInInputControl");
    }
}
