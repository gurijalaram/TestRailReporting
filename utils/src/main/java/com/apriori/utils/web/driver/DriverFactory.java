package com.apriori.utils.web.driver;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

import java.security.InvalidParameterException;

public class DriverFactory {

    private WebDriver driver;
    private BrowserTypes browser;
    private Proxy proxy;
    private String downloadPath;
    private String remoteDownloadPath;
    private String locale;

    public DriverFactory(TestMode testMode, TestType testType, BrowserTypes browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.driver = createDriver(testMode, testType, browser, proxy, downloadPath, remoteDownloadPath, locale);
    }

    private WebDriver createDriver(TestMode testMode, TestType testType, BrowserTypes browser, Proxy proxy, String downloadPath, String remoteDownloadPath, String locale) {
        this.browser = browser;
        this.proxy = proxy;
        this.downloadPath = downloadPath;
        this.remoteDownloadPath = remoteDownloadPath;
        this.locale = locale;

        switch (testMode) {
            case HOSTED_GRID:
                remoteWebDriverService("conqsldocker01");
                break;
            case LOCAL_GRID:
                remoteWebDriverService("localhost");
                break;
            case DOCKER_GRID:
                remoteWebDriverService("host.docker.internal");
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

    private void remoteWebDriverService(String host) {
        driver = new RemoteWebDriverService(this.browser, ("http://").concat(host).concat(":4444"), this.proxy, this.downloadPath, this.remoteDownloadPath, this.locale).startService();
    }
}
