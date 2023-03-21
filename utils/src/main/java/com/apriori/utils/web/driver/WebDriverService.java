package com.apriori.utils.web.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.security.InvalidParameterException;

/**
 * @author cfrith
 */

@Slf4j
public class WebDriverService extends BrowserManager {

    private WebDriver result;
    private MutableCapabilities capabilities = new MutableCapabilities();
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

        try {
            switch (browser) {
                case CHROME:
                    log.info("Starting ChromeDriver........ ");

                    WebDriverManager.chromedriver().setup();

                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("browser.download.dir", downloadPath);
                    chromeOptions.setCapability("intl.accept.languages", locale);
                    chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    chromeOptions.setAcceptInsecureCerts(true);
                    chromeOptions.addArguments("--remote-allow-origins=*");

                    result = new ChromeDriver(chromeOptions);
                    log.info("Full list of Capabilities: " + ((ChromeDriver) result).getCapabilities().toString());
                    break;

                case FIREFOX:
                    log.info("Starting GeckoDriver........ ");

                    WebDriverManager.firefoxdriver().browserVersion("78.15.0esr").setup();

                    result = new FirefoxDriver(new FirefoxDriverOptions(downloadPath, locale).getFirefoxOptions());
                    log.info("Full list of Capabilities: " + ((FirefoxDriver) result).getCapabilities().toString());
                    break;

                case EDGE:
                    log.info("Starting EdgeDriver........ ");

                    WebDriverManager.edgedriver().setup();

                    result = new EdgeDriver();
                    log.info("Full list of Capabilities: " + ((EdgeDriver) result).getCapabilities().toString());
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
