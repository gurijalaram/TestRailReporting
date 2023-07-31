package com.apriori;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeManager implements DriverManager<EdgeOptions> {

    private EdgeOptions edgeOptions = new EdgeOptions();

    public WebDriver createDriver() {
        WebDriverManager.edgedriver().setup();

        return new EdgeDriver(getOptions());
    }

    public EdgeOptions getOptions() {
        edgeOptions.addArguments("--start-maximized");
        edgeOptions.setCapability(EdgeOptions.CAPABILITY, edgeOptions);

        return edgeOptions;
    }
}
