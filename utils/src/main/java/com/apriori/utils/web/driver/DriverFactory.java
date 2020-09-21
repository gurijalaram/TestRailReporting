package com.apriori.utils.web.driver;

import com.apriori.utils.reader.BaseReader;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
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
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.logging.Level;

public class DriverFactory {

    private static final Logger logger_DriverFactory = LoggerFactory.getLogger(DriverFactory.class);

    private WebDriver driver;
    private String server = null;
    private boolean headless = false;

    public DriverFactory(TestMode testMode, TestType testType, String browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.driver = createDriver(testMode, testType, browser, proxy, downloadPath, remoteDownloadPath, locale);
    }

    private WebDriver createDriver(TestMode testMode, TestType testType, String browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        try {
            switch (testMode) {
                case QA:
                    logger_DriverFactory.debug("Getting host and port from properties file");
                    BaseReader propertiesReader = new BaseReader("seleniumconfig.properties");
                    String seleniumProtocol = propertiesReader.getProperties().getProperty("protocol");
                    String seleniumPort = propertiesReader.getProperties().getProperty("port");
                    String seleniumHost = propertiesReader.getProperties().getProperty("host");
                    String seleniumPrefix = propertiesReader.getProperties().getProperty("prefix");
                    if (StringUtils.isNotEmpty(System.getProperty("selenium.port"))) {
                        seleniumPort = System.getProperty("selenium.port");
                    }
                    if (StringUtils.isNotEmpty(System.getProperty("selenium.host"))) {
                        seleniumHost = System.getProperty("selenium.host");
                    }
                    logger_DriverFactory.debug("Starting driver on " + seleniumHost + ":" + seleniumPort);

                    StringBuilder serverBuilder = new StringBuilder(seleniumProtocol + "://" + seleniumHost);

                    if (seleniumPort != null && !seleniumPort.isEmpty()) {
                        serverBuilder.append(":").append(seleniumPort);
                    }
                    serverBuilder.append(seleniumPrefix);

                    server = serverBuilder.toString();

                    if (testType.equals(TestType.EXPORT)) {
                        driver = getQADriver(server, browser, proxy, downloadPath, remoteDownloadPath, locale);
                    } else {
                        driver = getQADriver(server.concat("/wd/hub"), browser, proxy, null, null, locale);
                    }
                    break;
                case GRID:
                    driver = getQADriver(("http://").concat("conqsgrafana01").concat(":4444").concat("/wd/hub"), browser, proxy, null, null, locale);
                    break;
                case EXPORT:
                    throw new InvalidParameterException("Use QA mode with EXPORT type instead: " + testMode);
                case LOCAL:
                    driver = getLocalDriver(browser, proxy, downloadPath, locale);
                    break;
                default:
                    throw new InvalidParameterException("Unexpected test mode: " + testMode);
            }
        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
        }

        return driver;
    }

    private WebDriver getLocalDriver(String browser, Proxy proxy, String downloadPath, String locale) {
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
                System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, WebDriverManager.firefoxdriver().getBinaryPath());
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
                logger_DriverFactory.info("Full list of Capabilities: " + ((FirefoxDriver) result).getCapabilities().toString());
                break;
            case "iexplorer11":
                dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                dc.setVersion("11");
                result = new InternetExplorerDriver(dc);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                result = new EdgeDriver(dc);
                logger_DriverFactory.info("Full list of Capabilities: " + ((EdgeDriver) result).getCapabilities().toString());
                break;
            default:
            case "chrome":
                WebDriverManager.chromedriver().setup();
                System.setProperty("webdriver.chrome.logfile", System.getProperty("java.io.tmpdir") + File.separator + "chromedriver.log");
                System.setProperty("webdriver.chrome.verboseLogging", "true");

                ChromeOptions options = getChromeOptions(downloadPath, locale);
                dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                dc.setCapability(ChromeOptions.CAPABILITY, options);
                result = new ChromeDriver(dc);
                logger_DriverFactory.info("Full list of Capabilities: " + ((ChromeDriver) result).getCapabilities().toString());
                break;
        }
        return result;
    }

    private void setDriverBrowserLogging(DesiredCapabilities dc) {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        logs.enable(LogType.PERFORMANCE, Level.ALL);
        dc.setCapability(CapabilityType.LOGGING_PREFS, logs);
    }

    /**
     * TODO: modify this if we decide to move architecture to run on AWS using our own AMIs. This can also be re-purposed to run on a Docker instance but if we are planning to
     * use Zalenium then we can just modify this to account for test runs there
     **/
    private RemoteWebDriver getQADriver(String server, String browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) throws MalformedURLException {
        logger_DriverFactory.info("server: " + server);

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
                logger_DriverFactory.info("Full list of Capabilities: " + result.getCapabilities().toString());
                break;
            case "iexplorer11":
                dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                dc.setVersion("11");
                result = new RemoteWebDriver(new URL(server), dc);
                break;
            case "chrome":
                logger_DriverFactory.info("Starting ChromeDriver........ ");
                ChromeOptions options = getChromeOptions(remoteDownloadPath, locale);
                options.addArguments("--no-sandbox");

                dc.setCapability(ChromeOptions.CAPABILITY, options);
                dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                result = new RemoteWebDriver(new URL(server), dc);
                logger_DriverFactory.info("Full list of Capabilities: " + (result).getCapabilities().toString());
                break;
            case "edge":
                dc.setBrowserName(DesiredCapabilities.edge().getBrowserName());
                result = new RemoteWebDriver(new URL(server), dc);
                logger_DriverFactory.info("Full list of Capabilities: " + (result).getCapabilities().toString());
                break;
            default:
                throw new InvalidParameterException("Unexpected browser type: " + browser);
        }

        result.setFileDetector(new LocalFileDetector());
        return result;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isHeadless() {
        return headless;
    }

    private ChromeOptions getChromeOptions(String downloadPath, String locale) {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        // Set Custom Download Dir for downloads in chrome
        if (downloadPath != null) {
            chromePrefs.put("download.default_directory", downloadPath);
        }
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allow-outdated-plugins");
        if (!StringUtils.isEmpty(System.getProperty("ignoreSslCheck")) && Boolean.parseBoolean(System.getProperty("ignoreSslCheck"))) {
            options.addArguments("--ignore-certificate-errors");
        }
        options.setExperimentalOption("prefs", chromePrefs);

        // TODO: 28/02/2020 quick fix for running on linux. this will be reworked with major changes in the near future
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-dev-shm-usage");
        }

        headless = !StringUtils.isEmpty(System.getProperty("headless")) && Boolean.parseBoolean(System.getProperty("headless"));

        if (headless) {
            // note: the window size in headless is not limited to the display size
            options.addArguments("headless", "disable-gpu", "window-size=1920,1080");
        }

        if (StringUtils.isNotEmpty(locale)) {
            options.addArguments("--lang=" + locale);
        }
        return options;
    }
}
