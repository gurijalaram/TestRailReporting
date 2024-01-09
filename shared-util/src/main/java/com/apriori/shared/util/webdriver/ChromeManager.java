package com.apriori.shared.util.webdriver;

import static com.apriori.shared.util.webdriver.DriverFactory.mode;
import static com.apriori.shared.util.webdriver.DriverFactory.os;
import static io.github.bonigarcia.wdm.config.OperatingSystem.LINUX;

import com.apriori.shared.util.testconfig.TestMode;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

@Slf4j
public class ChromeManager implements DriverManager<ChromeOptions> {

    private ChromeOptions chromeOptions = new ChromeOptions();
    private HashMap<String, Object> chromePrefs = new HashMap<>();

    public WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        int sessionRetries = 0;

        while (true) {

            try {
                return new ChromeDriver(getOptions());
            } catch (SessionNotCreatedException exception) {

                if (++sessionRetries == 3) {
                    throw exception;
                }
            }
        }
    }

    public ChromeOptions getOptions() {
        chromeOptions.addArguments("--start-maximized");

        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--allow-outdated-plugins");
        chromeOptions.addArguments("--remote-allow-origins=*");

        if (mode != null && mode.toUpperCase().equals(TestMode.DOCKER_GRID.value())) {
            chromeOptions.addArguments("--unsafely-treat-insecure-origin-as-secure=http://host.docker.internal:3003");
            chromeOptions.addArguments("--unsafely-treat-insecure-origin-as-secure=http://host.docker.internal:3002");
        }

        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        if (!StringUtils.isEmpty(System.getProperty("ignoreSslCheck")) && Boolean.parseBoolean(System.getProperty("ignoreSslCheck"))) {
            chromeOptions.addArguments("--ignore-certificate-errors");
        }

        if (os.toLowerCase().contains(LINUX.getName())) {
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

        chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        return chromeOptions;
    }
}
