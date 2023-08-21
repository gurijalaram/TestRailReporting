package com.apriori.webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeManager implements DriverManager<EdgeOptions> {

    private EdgeOptions edgeOptions = new EdgeOptions();

    public WebDriver createDriver() {
        WebDriverManager.edgedriver().browserVersion("115.0").setup();

        int sessionRetries = 0;

        while (true) {

            try {
                return new EdgeDriver(getOptions());
            } catch (SessionNotCreatedException exception) {

                if (++sessionRetries == 3) {
                    throw exception;
                }
            }
        }
    }

    public EdgeOptions getOptions() {
        edgeOptions.addArguments("--start-maximized");
        edgeOptions.setCapability(EdgeOptions.CAPABILITY, edgeOptions);

        return edgeOptions;
    }
}
