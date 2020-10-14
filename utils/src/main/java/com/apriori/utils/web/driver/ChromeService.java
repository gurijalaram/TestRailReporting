package com.apriori.utils.web.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class ChromeService extends BrowserManager{

    private WebDriver result;
    private DesiredCapabilities dc;
    private String browser;
    private Proxy proxy;
    private String downloadPath;
    private String locale;

    @Override
    public void createService() {

    }

    @Override
    public WebDriver startService() {
        try {
            WebDriverManager.chromedriver().setup();
            System.setProperty("webdriver.chrome.logfile", System.getProperty("java.io.tmpdir") + File.separator + "chromedriver.log");
            System.setProperty("webdriver.chrome.verboseLogging", "true");

            ChromeOptions options = new ChromeDriverOptions(downloadPath, locale).getChromeOptions();
            dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
            dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            dc.setCapability(ChromeOptions.CAPABILITY, options);
            result = new ChromeDriver(dc);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void stopService() {

    }
}
