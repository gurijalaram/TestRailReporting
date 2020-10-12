package com.apriori.utils.web.driver;

import com.apriori.utils.reader.BaseReader;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.security.InvalidParameterException;

public class DriverFactory {

    private static final Logger LOGGER_DRIVER_FACTORY = LoggerFactory.getLogger(DriverFactory.class);

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
                    LOGGER_DRIVER_FACTORY.debug("Getting host and port from properties file");
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
                    LOGGER_DRIVER_FACTORY.debug("Starting driver on " + seleniumHost + ":" + seleniumPort);

                    StringBuilder serverBuilder = new StringBuilder(seleniumProtocol + "://" + seleniumHost);

                    if (seleniumPort != null && !seleniumPort.isEmpty()) {
                        serverBuilder.append(":").append(seleniumPort);
                    }
                    serverBuilder.append(seleniumPrefix);

                    server = serverBuilder.toString();

                    if (testType.equals(TestType.EXPORT)) {
                        driver = new QaDriver(server, browser, proxy, downloadPath, remoteDownloadPath, locale).getQADriver();
                    } else {
                        driver = new QaDriver(server.concat("/wd/hub"), browser, proxy, null, null, locale).getQADriver();
                    }
                    break;
                case GRID:
                    driver = new QaDriver(("http://").concat("conqsgrafana01").concat(":4444").concat("/wd/hub"), browser, proxy, null, null, locale).getQADriver();
                    break;
                case EXPORT:
                    throw new InvalidParameterException("Use QA mode with EXPORT type instead: " + testMode);
                case LOCAL:
                    driver = new LocalDriver(browser, proxy, downloadPath, locale).getLocalDriver();
                    break;
                default:
                    throw new InvalidParameterException("Unexpected test mode: " + testMode);
            }
        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
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
