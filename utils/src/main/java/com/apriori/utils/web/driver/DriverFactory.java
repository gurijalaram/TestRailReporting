package com.apriori.utils.web.driver;

import com.apriori.utils.reader.BaseReader;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;

public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);

    BaseReader propertiesReader = new BaseReader("seleniumconfig.properties");
    String seleniumProtocol = propertiesReader.getProperties().getProperty("protocol");
    String seleniumPrefix = propertiesReader.getProperties().getProperty("prefix");
    private WebDriver driver;
    private String server = null;
    private boolean headless = false;

    public DriverFactory(TestMode testMode, TestType testType, BrowserTypes browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.driver = createDriver(testMode, testType, browser, proxy, downloadPath, remoteDownloadPath, locale);
    }

    private WebDriver createDriver(TestMode testMode, TestType testType, BrowserTypes browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        switch (testMode) {
            case GRID:
                logger.debug("Getting host and port from properties file");

                String seleniumPort = StringUtils.isNotEmpty(System.getProperty("selenium.port")) ? System.getProperty("selenium.port") : propertiesReader.getProperties().getProperty("port");
                String seleniumHost = StringUtils.isNotEmpty(System.getProperty("selenium.host")) ? System.getProperty("selenium.host") : propertiesReader.getProperties().getProperty("host");

                logger.debug("Starting driver on " + seleniumHost + ":" + seleniumPort);

                StringBuilder serverBuilder = new StringBuilder(seleniumProtocol + "://" + seleniumHost);

                if (seleniumPort != null && !seleniumPort.isEmpty()) {
                    serverBuilder.append(":").append(seleniumPort);
                }

                serverBuilder.append(seleniumPrefix);
                server = serverBuilder.toString();
                driver = testType.equals(TestType.EXPORT) ? new WebDriverService(browser, proxy, downloadPath, locale).startService()
                    : new RemoteWebDriverService(browser, server, proxy, downloadPath, remoteDownloadPath, locale).startService();
                break;
            case QA:
                driver = new RemoteWebDriverService(browser, ("http://").concat("localhost").concat(":4444"), proxy, downloadPath, remoteDownloadPath, locale).startService();
                break;
            case LOCAL:
                driver = new WebDriverService(browser, proxy, downloadPath, locale).startService();
                break;
            case DOCKER:
                driver = new RemoteWebDriverService(browser, ("http://").concat("host.docker.internal").concat(":4444"), proxy, downloadPath, remoteDownloadPath, locale).startService();
                break;
            default:
                throw new InvalidParameterException(String.format("Received unexpected test mode '%s' ", testMode));
        }
        return driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public boolean isHeadless() {
        return headless;
    }
}
