package com.apriori.utils.web.driver;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
import java.security.InvalidParameterException;

/**
 * @author cfrith
 */

@Slf4j
public class RemoteWebDriverService extends BrowserManager {


    private RemoteWebDriver result;
    private MutableCapabilities capabilities = new MutableCapabilities();
    private BrowserTypes browser;
    private String server;
    private Proxy proxy;
    private String remoteDownloadPath;
    private String downloadPath;
    private String locale;

    public RemoteWebDriverService(BrowserTypes browser, String server, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.browser = browser;
        this.server = server;
        this.proxy = proxy;
        this.remoteDownloadPath = remoteDownloadPath;
        this.downloadPath = downloadPath;
        this.locale = locale;
    }

    @Override
    public WebDriver startService() {
        log.info("server: " + server);

        String uuid = StringUtils.isEmpty(System.getProperty("uuid")) ? "ParallelTestsRun" : System.getProperty("uuid");

        setDownloadFolder(downloadPath);
        setProxy(proxy);
        capabilities.setCapability("uuid", uuid.toLowerCase());

        try {
            switch (browser) {
                case CHROME:
                    log.info("Starting ChromeDriver........ ");

                    ChromeOptions options = new ChromeDriverOptions(remoteDownloadPath, locale).getChromeOptions();
                    options.setAcceptInsecureCerts(true);
                    dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    dc.setCapability(ChromeOptions.CAPABILITY, options);
                    dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                    break;

                case FIREFOX:
                    log.info("Starting GeckoDriver........ ");

                    FirefoxProfile fp = new FirefoxProfile();
                    fp.setAcceptUntrustedCertificates(true);
                    dc.setCapability(FirefoxDriver.PROFILE, fp);
                    dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
                    break;

                case EDGE:
                    log.info("Starting EdgeDriver........ ");

                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                    dc.setBrowserName(DesiredCapabilities.edge().getBrowserName());
                    break;

                default:
                    throw new InvalidParameterException(String.format("Unexpected browser type: '%s' ", browser));
            }
            result = new RemoteWebDriver(new URL(server), capabilities);
            result.setFileDetector(new LocalFileDetector());
            log.info("Full list of Capabilities: " + (result).getCapabilities().toString());

        } catch (Exception e) {
            log.info(String.format("Exception caught. Driver is: '%s'.  Could not start a new session, possible causes are invalid address or Selenium Grid ran out of nodes", result));
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void setDownloadFolder(String downloadPath) {
        if (downloadPath != null) {
            File downloadDir = new File(downloadPath);
            if (!downloadDir.exists()) {
                downloadDir.mkdir();
            }
        }
    }
}
