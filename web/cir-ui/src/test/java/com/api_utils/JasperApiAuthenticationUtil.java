package com.api_utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.driver.TestBase;
import com.ootbreports.newreportstests.dtcmetrics.castingdtc.TrustAllX509TrustManager;
import org.junit.Before;
import org.junit.Test;

public class JasperApiAuthenticationUtil extends TestBase {

    private static String jSessionId = "";

    public String authenticateJasperApi() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        String envToUse = PropertiesContext.get("env");
        if (envToUse.equals("onprem")) {
            authenticateOnPrem();
        } else {
            authenticateCloud();
        }

        assertThat(jSessionId, is(notNullValue()));
        return jSessionId;
    }

    private void authenticateOnPrem() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        skipSslCheck();

        String urlLink = PropertiesContext.get("${env}.reports.api_url").concat("j_spring_security_check?j_username=bhegan&j_password=bhegan");
        URL url = new URL(urlLink);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        System.out.println("Login response code :" + con.getResponseCode());
        String sessionId = con + "";
        jSessionId = sessionId.split(";")[1].substring(11, 43);
    }

    @Test
    public void authenticateCloud() {
        new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        jSessionId = driver.manage().getCookieNamed("JSESSIONID").getValue();
    }

    private void skipSslCheck() throws NoSuchAlgorithmException, KeyManagementException {
        // Source: https://stackoverflow.com/questions/19723415/java-overriding-function-to-disable-ssl-certificate-check
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
    }
}
