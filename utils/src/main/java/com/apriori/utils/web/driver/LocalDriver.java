package com.apriori.utils.web.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.logging.Level;

/**
 * @author cfrith
 */

public class LocalDriver {

    private static final Logger LOGGER_LOCAL_DRIVER = LoggerFactory.getLogger(LocalDriver.class);

    private String browser;
    private Proxy proxy;
    private String downloadPath;
    private String locale;

    public LocalDriver(String browser, Proxy proxy, String downloadPath, String locale) {
    this.browser = browser;
    this.proxy = proxy;
    this.downloadPath = downloadPath;
    this.locale = locale;
    }

    public WebDriver getLocalDriver() {
        WebDriver result;
        DesiredCapabilities dc = new DesiredCapabilities();

        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdir();
        }

        if (proxy != null) {
            dc.setCapability(CapabilityType.PROXY, proxy);
        }

        setDriverBrowserLogging(dc);

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile fp = new FirefoxProfile();
                System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, WebDriverManager.firefoxdriver().getDownloadedDriverPath());
                fp.setPreference("browser.search.geoip.url", "http://127.0.0.1");
                fp.setPreference("browser.download.folderList", 2);
                fp.setPreference("browser.download.manager.showWhenStarting", false);
                fp.setPreference("browser.download.dir", downloadPath);
                fp.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, "
                    + "application/vnd.ms-powerpoint, application/ris, text/csv, image/png, application/pdf, "
                    + "text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, "
                    + "application/download, application/octet-stream, application/xls, application/vnd.ms-excel, "
                    + "application/x-xls, application/x-ms-excel, application/msexcel, application/x-msexcel, "
                    + "application/x-excel");

                if (StringUtils.isNotEmpty(locale)) {
                    fp.setPreference("intl.accept_languages", locale);
                }

                dc.setCapability(FirefoxDriver.PROFILE, fp);
                dc.merge(DesiredCapabilities.firefox());

                result = new FirefoxDriver(dc);
                LOGGER_LOCAL_DRIVER.info("Full list of Capabilities: " + ((FirefoxDriver) result).getCapabilities().toString());
                break;
            case "iexplorer11":
                dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                dc.setVersion("11");
                result = new InternetExplorerDriver(dc);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                result = new EdgeDriver(dc);
                LOGGER_LOCAL_DRIVER.info("Full list of Capabilities: " + ((EdgeDriver) result).getCapabilities().toString());
                break;
            default:
            case "chrome":
                WebDriverManager.chromedriver().setup();
                System.setProperty("webdriver.chrome.logfile", System.getProperty("java.io.tmpdir") + File.separator + "chromedriver.log");
                System.setProperty("webdriver.chrome.verboseLogging", "true");

                ChromeOptions options = new ChromeDriverOptions(downloadPath, locale).getChromeOptions();
                dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                dc.setCapability(ChromeOptions.CAPABILITY, options);
                result = new ChromeDriver(dc);
                LOGGER_LOCAL_DRIVER.info("Full list of Capabilities: " + ((ChromeDriver) result).getCapabilities().toString());
                break;
        }
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
