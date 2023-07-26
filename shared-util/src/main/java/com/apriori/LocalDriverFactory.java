package com.apriori;

import com.apriori.exceptions.BrowserNotSupportedException;

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
            // TODO: 15/06/2023 maybe retry after exception caught here
        } catch (SessionNotCreatedException | NullPointerException e) {
            e.getCause();
        }
        return webDriver;
    }
}
