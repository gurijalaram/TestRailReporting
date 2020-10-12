package com.apriori.utils.web.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.logging.Level;

/**
 * @author cfrith
 */

public class QaDriver {

    private static final Logger LOGGER_QA_DRIVER = LoggerFactory.getLogger(QaDriver.class);

    private String server;
    private String browser;
    private Proxy proxy;
    private String downloadPath;
    private String remoteDownloadPath;
    private String locale;

    public QaDriver(String server, String browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.server = server;
        this.browser = browser;
        this.proxy = proxy;
        this.downloadPath = downloadPath;
        this.remoteDownloadPath = remoteDownloadPath;
        this.locale = locale;
    }

    public RemoteWebDriver getQADriver() throws MalformedURLException {
        LOGGER_QA_DRIVER.info("server: " + server);

        String uuid = StringUtils.isEmpty(System.getProperty("uuid")) ? "ParallelTestsRun" : System.getProperty("uuid");

        RemoteWebDriver result;
        DesiredCapabilities dc = new DesiredCapabilities();

        if (downloadPath != null) {
            File downloadDir = new File(downloadPath);
            if (!downloadDir.exists()) {
                downloadDir.mkdir();
            }
        }

        if (proxy != null) {
            dc.setCapability(CapabilityType.PROXY, proxy);
        }

        setDriverBrowserLogging(dc);
        dc.setCapability("uuid", uuid.toLowerCase());

        switch (browser) {
            case "firefox":
                FirefoxProfile fp = new FirefoxProfile();
                dc.setCapability(FirefoxDriver.PROFILE, fp);
                dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
                result = new RemoteWebDriver(new URL(server), dc);
                LOGGER_QA_DRIVER.info("Full list of Capabilities: " + result.getCapabilities().toString());
                break;
            case "iexplorer11":
                dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                dc.setVersion("11");
                result = new RemoteWebDriver(new URL(server), dc);
                break;
            case "chrome":
                LOGGER_QA_DRIVER.info("Starting ChromeDriver........ ");
                ChromeOptions options = new ChromeDriverOptions(remoteDownloadPath, locale).getChromeOptions();
                options.addArguments("--no-sandbox");

                dc.setCapability(ChromeOptions.CAPABILITY, options);
                dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                result = new RemoteWebDriver(new URL(server), dc);
                LOGGER_QA_DRIVER.info("Full list of Capabilities: " + (result).getCapabilities().toString());
                break;
            case "edge":
                dc.setBrowserName(DesiredCapabilities.edge().getBrowserName());
                result = new RemoteWebDriver(new URL(server), dc);
                LOGGER_QA_DRIVER.info("Full list of Capabilities: " + (result).getCapabilities().toString());
                break;
            default:
                throw new InvalidParameterException("Unexpected browser type: " + browser);
        }
        result.setFileDetector(new LocalFileDetector());
        return result;
    }

    public void setDriverBrowserLogging(DesiredCapabilities dc) {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        logs.enable(LogType.PERFORMANCE, Level.ALL);
        dc.setCapability(CapabilityType.LOGGING_PREFS, logs);
    }
}