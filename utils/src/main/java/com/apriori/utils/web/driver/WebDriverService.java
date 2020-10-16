package com.apriori.utils.web.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }
}
