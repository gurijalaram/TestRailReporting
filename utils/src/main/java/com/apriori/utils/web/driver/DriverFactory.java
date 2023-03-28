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
            case HOSTED_GRID:
                remoteWebDriverService(browser, "conqsldocker01", proxy, downloadPath, remoteDownloadPath, locale);
                break;
            case LOCAL_GRID:
                remoteWebDriverService(browser, "localhost", proxy, downloadPath, remoteDownloadPath, locale);
                break;
            case DOCKER_GRID:
                remoteWebDriverService(browser, "host.docker.internal", proxy, downloadPath, remoteDownloadPath, locale);
                break;
            case QA_LOCAL:
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

    private void remoteWebDriverService(BrowserTypes browser, String host, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        driver = new RemoteWebDriverService(browser, ("http://").concat(host).concat(":4444"), proxy, downloadPath, remoteDownloadPath, locale).startService();
    }
}
