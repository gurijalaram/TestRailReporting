package utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class JasperApiAuthenticationUtil extends TestBase {

    public static String jSessionId;

    @BeforeClass
    public static void setupSession() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        JasperApiAuthenticationUtil auth = new JasperApiAuthenticationUtil();
        auth.authenticateJasperApi();
    }

    /**
     * Authenticates jasper api, opening session
     *
     * @throws NoSuchAlgorithmException - potentially thrown by on prem auth
     * @throws IOException - potentially thrown by on prem auth
     * @throws KeyManagementException - potentially thrown by on prem auth
     */
    public void authenticateJasperApi() throws NoSuchAlgorithmException, IOException, KeyManagementException {
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
        String urlLink = PropertiesContext.get("${env}.reports.ui_url")
            .concat(
                String.format(
                    "j_spring_security_check?j_username=%s&j_password=%s",
                    usernamePassword,
                    usernamePassword
                )
            );
        URL url = new URL(urlLink);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        System.out.println("Login response code :" + con.getResponseCode());
        String sessionId = con + "";
        jSessionId = sessionId.split(";")[1].substring(11, 43);
    }

    /**
     * Authenticate jasper api for cloud, opening session
     */
    public void authenticateCloud() {
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
