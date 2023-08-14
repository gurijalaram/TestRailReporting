package com.apriori.webdriver;

import com.apriori.testconfig.TestMode;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public enum BrowserFactory {
    CHROME {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.getInstance(DriverManagerType.CHROME).setup();

            return new ChromeDriver(getOptions());
        }

        @Override
        public ChromeOptions getOptions() {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setAcceptInsecureCerts(true);
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--allow-outdated-plugins");
            chromeOptions.addArguments("--remote-allow-origins=*");

            final String mode = System.getProperty("mode");
            if (mode != null && mode.toUpperCase().equals(TestMode.DOCKER_GRID.value())) {
                chromeOptions.addArguments("--unsafely-treat-insecure-origin-as-secure=http://host.docker.internal:3003");
            }

            if (!StringUtils.isEmpty(System.getProperty("ignoreSslCheck")) && Boolean.parseBoolean(System.getProperty("ignoreSslCheck"))) {
                chromeOptions.addArguments("--ignore-certificate-errors");
            }

            // TODO: 28/02/2020 quick fix for running on linux. this will be reworked with major changes in the near future
            if (System.getProperty("os.name").toLowerCase().contains(OperatingSystem.LINUX.getName())) {
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-dev-shm-usage");
            }

            boolean headless = !StringUtils.isEmpty(System.getProperty("headless")) && Boolean.parseBoolean(System.getProperty("headless"));
            if (headless) {
                // note: the window size in headless is not limited to the display size
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--headless");
            }
            return chromeOptions;
        }
    };

    public abstract WebDriver createDriver();

    public abstract AbstractDriverOptions<?> getOptions();
}
