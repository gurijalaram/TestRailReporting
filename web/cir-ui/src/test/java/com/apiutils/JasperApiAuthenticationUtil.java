package com.apiutils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.ootbreports.newreportstests.dtcmetrics.castingdtc.TrustAllX509TrustManager;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class JasperApiAuthenticationUtil extends TestBase {

    public String authenticateJasperApi(WebDriver driver) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        this.driver = driver;

        String jSessionId = PropertiesContext.get("env").equals("onprem") ? authenticateOnPrem() : authenticateCloud();
        assertThat(jSessionId, is(notNullValue()));

        return jSessionId;
    }

    private String authenticateOnPrem() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        skipSslCheck();

        UserCredentials user = UserUtil.getUser();
        String urlLink = PropertiesContext.get("${env}.reports.api_url")
            .concat(
                String.format(
                    "j_spring_security_check?j_username=%s&j_password=%s",
                    user.getUsername(),
                    user.getUsername()
                )
            );
        URL url = new URL(urlLink);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        System.out.println("Login response code :" + con.getResponseCode());
        String sessionId = con + "";
        return sessionId.split(";")[1].substring(11, 43);
    }

    @Test
    public String authenticateCloud() {
        new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        return driver.manage().getCookieNamed("JSESSIONID").getValue();
    }

    private void skipSslCheck() throws NoSuchAlgorithmException, KeyManagementException {
        // Source: https://stackoverflow.com/questions/19723415/java-overriding-function-to-disable-ssl-certificate-check
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
    }
}
