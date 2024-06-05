package com.apriori.shared.util.testconfig;

import static com.apriori.shared.util.webdriver.DriverFactory.os;
import static com.apriori.shared.util.webdriver.DriverFactory.testMode;

import com.apriori.shared.util.rules.TestRulesUI;
import com.apriori.shared.util.webdriver.DriverFactory;
import com.apriori.shared.util.webdriver.EventCapture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

import java.io.File;
import java.lang.reflect.Method;


@Slf4j
@ExtendWith(TestRulesUI.class)
public class TestBaseUI {
    protected DriverFactory driverFactory;
    protected WebDriver driver;
    public static String dirDownloadPath = System.getProperty("user.dir") + File.separator + "downloads" + File.separator;
    public static String homeDownloadPath = System.getProperty("user.home") + File.separator + "downloads" + File.separator;;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        driverFactory = new DriverFactory();
        driver = driverFactory.getDriver();
        WebDriverListener listener = new EventCapture();
        driver = new EventFiringDecorator<>(listener).decorate(driver);

        log.info("Operating System:- {}", os);
        log.info("Mode:- {}", testMode);
        log.info("Browser:- {}", driverFactory.browser);
        log.info("Test type:- {}", driverFactory.type);

        log.info("STARTING TEST:- {}", testInfo.getTestClass().map(Class::getName).orElseThrow(null) + "." + testInfo.getTestMethod().map(Method::getName).orElseThrow(null));

        driver.manage().deleteAllCookies();
    }
}
