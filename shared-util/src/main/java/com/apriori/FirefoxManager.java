package com.apriori;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxManager implements DriverManager<FirefoxOptions> {

    private FirefoxOptions firefoxOptions = new FirefoxOptions();

    public WebDriver createDriver() {
        WebDriverManager.firefoxdriver().browserVersion("78.15.0.esr").setup();

        int sessionRetries = 0;

        while (true) {

            try {
                return new FirefoxDriver(getOptions());
            } catch (SessionNotCreatedException exception) {

                if (++sessionRetries == 3) {
                    throw exception;
                }
            }
        }
    }

    public FirefoxOptions getOptions() {
        firefoxOptions.addArguments("--start-maximized");
        firefoxOptions.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);

        return firefoxOptions;
    }
}
