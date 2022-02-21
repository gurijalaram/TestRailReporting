package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;

import com.apriori.utils.properties.PropertiesContext;
import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class CastingDtcReportTests {

    private ChartDataPoint chartDataPoint;
    private static String jSessionId = "";

    /**
     * This before class method skips the invalid ssl cert issue we have with on prem installs
     *
     * @throws IOException - potential exception
     * @throws NoSuchAlgorithmException - potential exception
     * @throws KeyManagementException - potential exception
     */
    @BeforeClass
    public static void setupSession() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        skipSslCheck();

        String urlLink = PropertiesContext.get("${env}.reports.on_prem_vm_url").concat("j_spring_security_check?j_username=bhegan&j_password=bhegan");
        URL url = new URL(urlLink);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        System.out.println("Login response code :" + con.getResponseCode());
        String sessionId = con + "";
        jSessionId = sessionId.split(";")[1].substring(11, 43);
        assertThat(jSessionId, is(notNullValue()));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        ReportRequest reportRequest = ReportRequest.initFromJsonFile("ReportCastingDTCRequest");

        /*
        values for each export set (set it to change it)
        ---01-cost-outlier-threshold-rollup     64490
        ---01-cycle-time-value-tracking         37050
        ---01-dtc-casting                       528
        ---01-dtc-machiningdataset              233
        ---01-piston-assembly                   3
        ---01-roll-up-a                         1485
        ---01-sheet-metal-dtc                   19657
        ---01-sub-sub-asm                       18492
        ---01-top-level                         1
        ---01-top-level-multi-vpe               34986
         */

        // 1 - Generate report with USD currency setting
        reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Collections.singletonList("USD"));
        ChartDataPoint usdChartDataPoint = generateReportAndGetSummary(reportRequest);

        // 2 - Get values from USD report
        String usdFullyBurdenedCost = usdChartDataPoint.getFullyBurdenedCost();
        double usdAnnualSpend = usdChartDataPoint.getAnnualSpend();

        // 3- Change currency to GBP and re-generate report
        reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Collections.singletonList("GBP"));

        ChartDataPoint gbpChartDataPoint = generateReportAndGetSummary(reportRequest);

        // 4 - Get values from GBP report
        String gbpFullyBurdenedCost = gbpChartDataPoint.getFullyBurdenedCost();
        double gbpAnnualSpend = gbpChartDataPoint.getAnnualSpend();

        // 5 - Assert that USD values are not equal to GBP values
        assertThat(usdFullyBurdenedCost.equals(gbpFullyBurdenedCost), equalTo(false));
        assertThat(gbpAnnualSpend == usdAnnualSpend, is(equalTo(false)));
    }

    private static void skipSslCheck() throws NoSuchAlgorithmException, KeyManagementException {
        // Source: https://stackoverflow.com/questions/19723415/java-overriding-function-to-disable-ssl-certificate-check
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
    }

    private ChartDataPoint generateReportAndGetSummary(ReportRequest reportRequest) {
        JasperReportSummary jasperReportSummary = JasperReportUtil.init(jSessionId)
                .generateJasperReportSummary(reportRequest);
        return jasperReportSummary.getChartDataPointByPartName("40137441.MLDES.0002 (Initial)");
    }
}
