package com.apriori.utils.web.driver;

import com.apriori.utils.reader.BaseReader;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
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
import org.openqa.selenium.safari.SafariDriver;
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

    private WebDriver driver = null;
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
                    BaseReader propertiesReader = new BaseReader("main/resources/seleniumconfig.properties");
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
                        serverBuilder.append(":" + seleniumPort);
                    }
                    serverBuilder.append(seleniumPrefix);

                    server = serverBuilder.toString();

                    if (testType.equals(TestType.EXPORT)) {
                        driver = getQADriver(server, browser, proxy, downloadPath, remoteDownloadPath, locale);
                    } else {
                        driver = getQADriver(server.concat("/wd/hub"), browser, proxy, null, null, locale);
                    }
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
        WebDriver result = null;
        DesiredCapabilities dc = new DesiredCapabilities();

        File downloaDir = new File(downloadPath);
        if (!downloaDir.exists()) {
            downloaDir.mkdir();
        }

        if (proxy != null) {
            dc.setCapability(CapabilityType.PROXY, proxy);
        }

        setDriverBrowserLogging(dc);

        String driverLocation = getDriverLocation(browser);

        switch (browser) {
            case "firefox":
                FirefoxProfile fp = new FirefoxProfile();
                System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, driverLocation);
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
                break;
            case "iexplorer11":
                dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                dc.setVersion("11");
                result = new InternetExplorerDriver(dc);
                break;
            case "safari":
                dc.setBrowserName(DesiredCapabilities.safari().getBrowserName());
                result = new SafariDriver(dc);
                break;
            default:
            case "chrome":
                System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverLocation);
                System.setProperty("webdriver.chrome.logfile", System.getProperty("java.io.tmpdir") + File.separator + "chromedriver.log");
                System.setProperty("webdriver.chrome.verboseLogging", "true");

                ChromeOptions options = getChromeOptions(downloadPath, locale);
                dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                dc.setCapability(ChromeOptions.CAPABILITY, options);
                result = new ChromeDriver(dc);
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

        RemoteWebDriver result = null;
        DesiredCapabilities dc = new DesiredCapabilities();

        if (downloadPath != null) {
            File downloaDir = new File(downloadPath);
            if (!downloaDir.exists()) {
                downloaDir.mkdir();
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
                break;
            case "iexplorer10":
                dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                dc.setVersion("10");
                result = new RemoteWebDriver(new URL(server), dc);
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
                options.addArguments("--allow-insecure-localhost");

                dc.setCapability(ChromeOptions.CAPABILITY, options);
                dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                result = new RemoteWebDriver(new URL(server), dc);
                break;
            case "safari":
                dc.setBrowserName(DesiredCapabilities.safari().getBrowserName());
                result = new RemoteWebDriver(new URL(server), dc);
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

    private String getDriverLocation(String browser) {
        String driverLocation = null;
        logger_DriverFactory.info("OS: " + System.getProperty("os.name"));
        String osLowerCase = StringUtils.deleteWhitespace(System.getProperty("os.name").toLowerCase());

        if (browser.equalsIgnoreCase("firefox")) {
            if (osLowerCase.contains(Platform.WINDOWS.name().toLowerCase())) {
                driverLocation = new File("../utils/src/main/resources/geckodriver.exe").getAbsolutePath();
            }
        } else if (browser.equalsIgnoreCase("chrome")) {
            if (osLowerCase.contains(Platform.WINDOWS.name().toLowerCase())) {
                driverLocation = new File("../utils/src/main/resources/chromedriver.exe").getAbsolutePath();
            }
        }
        return driverLocation;
    }

    private ChromeOptions getChromeOptions(String downloadPath, String locale) {
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        // Set Custom Download Dir for downloads in chrome
        if (downloadPath != null) {
            chromePrefs.put("download.default_directory", downloadPath);
        }
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allow-outdated-plugins");
        options.setExperimentalOption("prefs", chromePrefs);

        headless = StringUtils.isEmpty(System.getProperty("headless")) ? false : Boolean.parseBoolean(System.getProperty("headless"));

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
