package com.apriori.utils.web.driver;

import com.apriori.utils.reader.BaseReader;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;

public class DriverFactory {

    private static final Logger LOGGER_DRIVER_FACTORY = LoggerFactory.getLogger(DriverFactory.class);

    BaseReader propertiesReader = new BaseReader("seleniumconfig.properties");
    String seleniumProtocol = propertiesReader.getProperties().getProperty("protocol");
    String seleniumPrefix = propertiesReader.getProperties().getProperty("prefix");
    private WebDriver driver;
    private String server = null;
    private boolean headless = false;

    public DriverFactory(TestMode testMode, TestType testType, String browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.driver = createDriver(testMode, testType, browser, proxy, downloadPath, remoteDownloadPath, locale);
    }

    private WebDriver createDriver(TestMode testMode, TestType testType, String browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        switch (testMode) {
            case QA:
                LOGGER_DRIVER_FACTORY.debug("Getting host and port from properties file");

                String seleniumPort = StringUtils.isNotEmpty(System.getProperty("selenium.port")) ? System.getProperty("selenium.port") : propertiesReader.getProperties().getProperty("port");
                String seleniumHost = StringUtils.isNotEmpty(System.getProperty("selenium.host")) ? System.getProperty("selenium.host") : propertiesReader.getProperties().getProperty("host");

                LOGGER_DRIVER_FACTORY.debug("Starting driver on " + seleniumHost + ":" + seleniumPort);

                StringBuilder serverBuilder = new StringBuilder(seleniumProtocol + "://" + seleniumHost);

                if (seleniumPort != null && !seleniumPort.isEmpty()) {
                    serverBuilder.append(":").append(seleniumPort);
                }

                serverBuilder.append(seleniumPrefix);
                server = serverBuilder.toString();
                driver = testType.equals(TestType.EXPORT) ? setQaBrowser(browser, server, proxy, downloadPath, remoteDownloadPath, locale) : setQaBrowser(browser, server.concat("/wd/hub"), proxy, null, null, locale);
                break;
            case GRID:
                setQaBrowser(browser, ("http://").concat("conqsgrafana01").concat(":4444").concat("/wd/hub"), proxy, null, null, locale);
                break;
            case LOCAL:
                setLocalBrowser(browser, proxy, downloadPath, locale);
                break;
            case EXPORT:
                throw new InvalidParameterException(String.format("Use QA mode with EXPORT type instead of '%s' ", testMode));
            default:
                throw new InvalidParameterException(String.format("Received unexpected test mode '%s' ", testMode));
        }
        return driver;
    }

    private WebDriver setQaBrowser(String browser, String server, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        switch (browser) {
            case "chrome":
                driver = new ChromeServiceQa(server, proxy, downloadPath, remoteDownloadPath, locale).startService();
                break;
        }
        return driver;
    }

    private void setLocalBrowser(String browser, Proxy proxy, String downloadPath, String locale) {
        switch (browser) {
            case "chrome":
                driver = new ChromeServiceLocal(proxy, downloadPath, locale).startService();
                break;
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isHeadless() {
        return headless;
    }
}
