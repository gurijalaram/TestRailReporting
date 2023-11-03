package com.apriori.shared.util.webdriver;

import com.apriori.shared.util.exceptions.BrowserNotSupportedException;
import com.apriori.shared.util.testconfig.Browser;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class RemoteDriverFactory {
    private RemoteWebDriver result;
    private MutableCapabilities capabilities = new MutableCapabilities();

    public WebDriver createInstance(Browser browser, String server) {
        try {
            switch (browser) {
                case CHROME:
                    capabilities = new ChromeManager().getOptions();
                    break;
                case FIREFOX:
                    capabilities = new FirefoxManager().getOptions();
                    break;
                case EDGE:
                    capabilities = new EdgeManager().getOptions();
                    break;
                default:
                    throw new BrowserNotSupportedException(browser);
            }
            result = new RemoteWebDriver(new URL(server), capabilities);
            result.setFileDetector(new LocalFileDetector());

            log.info("Full list of Capabilities:-{} ", capabilities);

        } catch (SessionNotCreatedException | MalformedURLException e) {
            e.getCause();
        }
        return result;
    }
}
