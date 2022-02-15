package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.http.utils.WebDriverUtils;
import com.apriori.utils.web.driver.DriverFactory;
import com.apriori.utils.web.driver.WebDriverService;
import io.qameta.allure.Description;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;

public class CastingDtcReportTests {

    private static String jSessionId = "";

    @BeforeClass
    public static void setupSession() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        skipSslCheck();

        String urlLink = String.format("https://conqbaci02/jasperserver-pro/j_spring_security_check?j_username=%s&j_password=%s", "bhegan", "bhegan");
        URL url = new URL(urlLink);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        System.out.println("Login response code :" + con.getResponseCode());
        String session_id = con + "";
        jSessionId = session_id.split(";")[1].substring(11, 43);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        assertThat(jSessionId, is(notNullValue()));

        ReportRequest reportRequest = ReportRequest.initFromJsonFile("ReportCastingDTCRequest");
        /*reportRequest.getParameters().getReportParameterByName("exportSetName")
            .setValue(Collections.singletonList("528"));*/
        /*reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Collections.singletonList("GBP"));*/

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

        JasperReportSummary jasperReportSummary =
            JasperReportUtil.init(jSessionId)
                .generateJasperReportSummary(reportRequest);

        ChartDataPoint chartDataPointToTest = jasperReportSummary.getChartDataPointByPartName("40137441.MLDES.0002 (Initial)");

        System.out.println(chartDataPointToTest.getAnnualSpend());
        System.out.println(chartDataPointToTest.getMassMetric());
        System.out.println(chartDataPointToTest.getCostMetric());
        System.out.println(chartDataPointToTest.getDTCScore());

        chartDataPointToTest.getPropertyByName("customPropertyName");

        // Get HTML data to validate

       Document reportDoc = jasperReportSummary.getReportHtmlPart();

       assertThat(reportDoc, is(notNullValue()));
    }

    private static void skipSslCheck() throws NoSuchAlgorithmException, KeyManagementException {
        // Source: https://stackoverflow.com/questions/19723415/java-overriding-function-to-disable-ssl-certificate-check
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
    }
}
