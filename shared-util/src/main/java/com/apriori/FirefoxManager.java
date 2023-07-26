package com.apriori;

import static io.github.bonigarcia.wdm.config.DriverManagerType.FIREFOX;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxManager implements DriverManager<FirefoxOptions> {

    private FirefoxOptions firefoxOptions = new FirefoxOptions();

    public WebDriver createDriver() {
        WebDriverManager.getInstance(FIREFOX).setup();

        return new FirefoxDriver(getOptions());
    }

    public FirefoxOptions getOptions() {
        firefoxOptions.addArguments("--start-maximized");
        firefoxOptions.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);

        return firefoxOptions;
    }
}
