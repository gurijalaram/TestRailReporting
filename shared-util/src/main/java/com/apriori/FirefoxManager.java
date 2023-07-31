package com.apriori;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxManager implements DriverManager<FirefoxOptions> {

    private FirefoxOptions firefoxOptions = new FirefoxOptions();

    public WebDriver createDriver() {
        WebDriverManager.firefoxdriver().browserVersion("78.15.0.esr").setup();

        return new FirefoxDriver(getOptions());
    }

    public FirefoxOptions getOptions() {
        firefoxOptions.addArguments("--start-maximized");
        firefoxOptions.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);

        return firefoxOptions;
    }
}
