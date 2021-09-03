package com.apriori.utils.web.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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

    private static final Logger logger = LoggerFactory.getLogger(WebDriverService.class);

    private WebDriver result;
    private DesiredCapabilities dc = new DesiredCapabilities();
    private BrowserTypes browser;
    private Proxy proxy;
    private String downloadPath;
    private String locale;

    public WebDriverService(BrowserTypes browser, Proxy proxy, String downloadPath, String locale) {
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
                case CHROME:
                    WebDriverManager.chromedriver().setup();
                    System.setProperty("webdriver.chrome.logfile", System.getProperty("java.io.tmpdir") + File.separator + "chromedriver.log");
                    System.setProperty("webdriver.chrome.verboseLogging", "true");

                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("browser.download.dir", downloadPath);
                    chromeOptions.setCapability("intl.accept.languages", locale);
                    chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

                    result = new ChromeDriver(chromeOptions);
                    logger.info("Full list of Capabilities: " + ((ChromeDriver) result).getCapabilities().toString());
                    break;

                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.getBrowserName();
                    firefoxOptions.setCapability("browser.download.dir", downloadPath);
                    firefoxOptions.setCapability("intl.accept_languages", locale);

                    result = new FirefoxDriver(firefoxOptions);
                    logger.info("Full list of Capabilities: " + ((FirefoxDriver) result).getCapabilities().toString());
                    break;

                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    result = new EdgeDriver();
                    logger.info("Full list of Capabilities: " + ((EdgeDriver) result).getCapabilities().toString());
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
