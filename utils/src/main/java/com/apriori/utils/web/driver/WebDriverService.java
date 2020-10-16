package com.apriori.utils.web.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.InvalidParameterException;

/**
 * @author cfrith
 */

public class WebDriverService extends BrowserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDriver.class);

    private WebDriver result;
    private DesiredCapabilities dc = new DesiredCapabilities();
    private String browser;
    private Proxy proxy;
    private String downloadPath;
    private String locale;

    public WebDriverService(String browser, Proxy proxy, String downloadPath, String locale) {
        this.browser = browser;
        this.proxy = proxy;
        this.downloadPath = downloadPath;
        this.locale = locale;
    }

    @Override
    public WebDriver startService() {
        setDownloadFolder(downloadPath);
        setProxy(proxy);
        logInfo(dc);

        try {
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    System.setProperty("webdriver.chrome.logfile", System.getProperty("java.io.tmpdir") + File.separator + "chromedriver.log");
                    System.setProperty("webdriver.chrome.verboseLogging", "true");

                    ChromeOptions options = new ChromeDriverOptions(downloadPath, locale).getChromeOptions();
                    dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                    dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    dc.setCapability(ChromeOptions.CAPABILITY, options);
                    result = new ChromeDriver(dc);
                    LOGGER.info("Full list of Capabilities: " + ((ChromeDriver) result).getCapabilities().toString());
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();

                    DesiredCapabilities firefoxOptions = new FirefoxOptions(downloadPath, locale).getFirefoxOptions();
                    result = new FirefoxDriver(firefoxOptions);
                    LOGGER.info("Full list of Capabilities: " + ((FirefoxDriver) result).getCapabilities().toString());
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    result = new EdgeDriver();
                    LOGGER.info("Full list of Capabilities: " + ((EdgeDriver) result).getCapabilities().toString());
                    break;

                case "iexplorer":
                    WebDriverManager.iedriver().setup();
                    result = new InternetExplorerDriver();
                    LOGGER.info("Full list of Capabilities: " + ((InternetExplorerDriver) result).getCapabilities().toString());
                    break;

                default:
                    throw new InvalidParameterException(String.format("Unexpected browser type: '%s' ", browser));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }
}
