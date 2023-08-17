package com.apriori.webdriver;

import com.apriori.exceptions.BrowserNotSupportedException;
import com.apriori.testconfig.Browser;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;

@Slf4j
public class LocalDriverFactory {

    public WebDriver createInstance(Browser browser) {
        WebDriver webDriver = null;

        try {
            switch (browser) {
                case CHROME:
                    webDriver = new ChromeManager().createDriver();
                    log.info("WebDriver details:- {} ", webDriver);
                    break;

                case FIREFOX:
                    webDriver = new FirefoxManager().createDriver();
                    log.info("WebDriver details:- {} ", webDriver);
                    break;

                case EDGE:
                    webDriver = new EdgeManager().createDriver();
                    log.info("WebDriver details:- {} ", webDriver);
                    break;

                default:
                    throw new BrowserNotSupportedException(browser);
            }
        } catch (SessionNotCreatedException | NullPointerException e) {
            e.getCause();
        }
        return webDriver;
    }
}
