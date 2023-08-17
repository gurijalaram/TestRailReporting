package utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.login.ReportsLoginPage;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserUtil;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class JasperApiAuthenticationUtil extends TestBaseUI {

    public static String jSessionId;

    @BeforeEach
    public void setupSession() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        if (PropertiesContext.get("env").equals("onprem")) {
            authenticateOnPrem();
        } else {
            authenticateCloud();
        }
        assertThat(jSessionId, is(notNullValue()));
    }

    private void authenticateOnPrem() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        skipSslCheck();

        String usernamePassword = UserUtil.getUserOnPrem().getUsername();
        String urlLink = PropertiesContext.get("reports.ui_url")
            .concat(
                String.format(
                    "rest_v2/login?j_username=%s&j_password=%s",
                    usernamePassword,
                    usernamePassword
                )
            );
        URL url = new URL(urlLink);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.connect();
        System.out.println("Login response code :" + con.getResponseCode());
        jSessionId = con.getHeaderField(2).substring(11, 43);
    }

    private void authenticateCloud() {
        new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        jSessionId = driver.manage().getCookieNamed("JSESSIONID").getValue();
    }

    private void skipSslCheck() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
    }
}
