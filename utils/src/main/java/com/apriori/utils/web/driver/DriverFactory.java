package com.apriori.utils.web.driver;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

import java.security.InvalidParameterException;

public class DriverFactory {

    private WebDriver driver;

    public DriverFactory(TestMode testMode, TestType testType, BrowserTypes browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.driver = createDriver(testMode, testType, browser, proxy, downloadPath, remoteDownloadPath, locale);
    }

    private WebDriver createDriver(TestMode testMode, TestType testType, BrowserTypes browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        switch (testMode) {
            case GRID:
                driver = new RemoteWebDriverService(browser, ("http://").concat("conqsldocker01").concat(":4444"), proxy, downloadPath, remoteDownloadPath, locale).startService();
                break;
            case QA:
                driver = new RemoteWebDriverService(browser, ("http://").concat("localhost").concat(":4444"), proxy, downloadPath, remoteDownloadPath, locale).startService();
                break;
            case DOCKER:
                driver = new RemoteWebDriverService(browser, ("http://").concat("host.docker.internal").concat(":4444"), proxy, downloadPath, remoteDownloadPath, locale).startService();
                break;
            case LOCAL:
                driver = new WebDriverService(browser, proxy, downloadPath, locale).startService();
                break;
            default:
                throw new InvalidParameterException(String.format("Received unexpected test mode '%s' ", testMode));
        }
        return driver;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
