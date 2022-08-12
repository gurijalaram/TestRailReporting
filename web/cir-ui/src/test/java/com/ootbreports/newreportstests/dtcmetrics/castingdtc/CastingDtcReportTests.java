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
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;
import utils.JasperApiAuthenticationUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class CastingDtcReportTests extends TestBase {

    private static String jSessionId = "";

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
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        ReportRequest reportRequest = ReportRequest.initFromJsonFile("ReportCastingDTCRequest");

        InputControl inputControl = JasperReportUtil.init(jSessionId)
            .getInputControls();
        String value = inputControl.getExportSetName().getOption("- - - 0 0 0-dtc-casting").getValue();

        // 1 - Generate report with USD currency setting
        reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Collections.singletonList(CurrencyEnum.USD.getCurrency()));

        reportRequest.getParameters().getReportParameterByName("exportSetName")
            .setValue(Collections.singletonList(value));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String currentDateTime = dtf.format(LocalDateTime.now());
        reportRequest.getParameters().getReportParameterByName("latestExportDate")
            .setValue(Collections.singletonList(currentDateTime));

        ChartDataPoint usdChartDataPoint = generateReportAndGetSummary(reportRequest);

        // 2 - Get values from USD report
        String usdFullyBurdenedCost = usdChartDataPoint.getFullyBurdenedCost();
        double usdAnnualSpend = usdChartDataPoint.getAnnualSpend();

        // 3- Change currency to GBP and re-generate report
        assertThat(usdFullyBurdenedCost, is(notNullValue()));
        assertThat(usdAnnualSpend, is(notNullValue()));

        reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Collections.singletonList(CurrencyEnum.GBP.getCurrency()));

        ChartDataPoint gbpChartDataPoint = generateReportAndGetSummary(reportRequest);

        // 4 - Get values from GBP report
        String gbpFullyBurdenedCost = gbpChartDataPoint.getFullyBurdenedCost();
        double gbpAnnualSpend = gbpChartDataPoint.getAnnualSpend();

        // 5 - Assert that USD values are not equal to GBP values
        assertThat(usdFullyBurdenedCost.equals(gbpFullyBurdenedCost), equalTo(false));
        assertThat(gbpAnnualSpend, is(not(equalTo(usdAnnualSpend))));
    }

    private ChartDataPoint generateReportAndGetSummary(ReportRequest reportRequest) {
        JasperReportSummary jasperReportSummary = JasperReportUtil.init(jSessionId)
            .generateJasperReportSummary(reportRequest);
        return jasperReportSummary.getChartDataPointByPartName("40137441.MLDES.0002 (Initial)");
    }

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
