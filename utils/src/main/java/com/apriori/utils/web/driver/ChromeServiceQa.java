package com.apriori.utils.web.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ChromeServiceQa extends BrowserManager{

    private static final Logger CHROME_SERVICE_QA_LOGGER = LoggerFactory.getLogger(LocalDriver.class);

    private RemoteWebDriver result;
    private DesiredCapabilities dc = new DesiredCapabilities();
    private String server;
    private String browser;
    private Proxy proxy;
    private String downloadPath;
    private String locale;

    public ChromeServiceQa(String server, String browser, Proxy proxy, String downloadPath, String locale) {
        this.server = server;
        this.browser = browser;
        this.proxy = proxy;
        this.downloadPath = downloadPath;
        this.locale = locale;
    }

    @Override
    public WebDriver startService() {
        CHROME_SERVICE_QA_LOGGER.info("server: " + server);

        String uuid = StringUtils.isEmpty(System.getProperty("uuid")) ? "ParallelTestsRun" : System.getProperty("uuid");

        setDownloadFolder(downloadPath);
        setProxy(proxy);
        logInfo(dc);
        dc.setCapability("uuid", uuid.toLowerCase());

        try {
            CHROME_SERVICE_QA_LOGGER.info("Starting ChromeDriver........ ");

            ChromeOptions options = new ChromeDriverOptions(downloadPath, locale).getChromeOptions();
            dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            dc.setCapability(ChromeOptions.CAPABILITY, options);
            dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
            result = new RemoteWebDriver(new URL(server), dc);
            CHROME_SERVICE_QA_LOGGER.info("Full list of Capabilities: " + (result).getCapabilities().toString());
        } catch (NullPointerException | MalformedURLException e) {
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
