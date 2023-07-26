package com.apriori.utils.web.driver;

import com.apriori.utils.TestHelper;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.runner.ConcurrentTestRunner;
import com.apriori.utils.web.rules.TestRule;
import com.apriori.utils.web.util.ConsoleLogHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.MDC;

import java.io.File;

/**
 * @author kpatel
 */
@RunWith(ConcurrentTestRunner.class)
public class TestBase  {

    @Rule
    public TestRule testRule = new TestRule();

    protected BrowserTypes browser;
    protected String locale;
    private static Proxy seleniumProxy;

    protected String downloadPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
    protected String remoteDownloadFolder = null;

    protected WebDriver driver = null;

    public TestBase() {
        browser = getBrowserType(System.getProperty("browser"));
        locale = System.getProperty("locale");

        logger.debug("Mode from property: " + System.getProperty("mode"));
        logger.debug("Browser from property: " + System.getProperty("browser"));
        logger.debug("Type of test from property: " + System.getProperty("type"));
        logger.debug("Locale from property: " + System.getProperty("locale"));
    }

    @Before
    public void testBaseBefore() {

        DriverFactory df;

        // By default downloadPath=$user.home/Downloads and remoteDownloadFolder=null
        df = new DriverFactory(mode, type, browser, seleniumProxy, downloadPath, remoteDownloadFolder, locale);
        driver = df.getDriver();

        if (browser.value().equalsIgnoreCase("chrome")) {
            driver = new EventFiringWebDriver(df.getDriver());
            TestHelper.logger.info("CONSOLE LOG LEVEL: " + CommonConstants.consoleLogLevel.getName());
            ConsoleLogHandler consoleLogHandler = new ConsoleLogHandler(CommonConstants.consoleLogLevel);
            ((EventFiringWebDriver) driver).register(consoleLogHandler);
        }

        String os = System.getProperty("os.name");
        TestHelper.logger.info("Current Operating System:" + os);
        TestHelper.logger.info("Window size before Maximize: " + driver.manage().window().getSize());

        if (os.equalsIgnoreCase("Linux")) {
            driver.manage().window().setSize(new Dimension(1390, 998));
        } else {
            driver.manage().window().maximize();
        }

        driver.manage().deleteAllCookies();
        TestHelper.logger.debug("Browser window size: " + driver.manage().window().getSize());

        MDC.put("methodName", this.getClass().getSimpleName() + "." + name.getMethodName());
        if (type.equals(TestType.EXPORT)) {
            TestHelper.logger.debug("Driver for " + this.getClass().getSimpleName() + "." + name.getMethodName() + " started: " + driver.hashCode() + " with download path=" + downloadPath);
        } else {
            TestHelper.logger.debug("Driver for " + this.getClass().getSimpleName() + "." + name.getMethodName() + " started: " + driver.hashCode());
        }

    }

    @After
    public void closeBrowser() {
        MDC.remove("methodName");
    }

    public WebDriver getDriver() {
        return driver;
    }


    private BrowserTypes getBrowserType(String browserProperty) {
        if (browserProperty == null || browserProperty.isEmpty()) {
            return BrowserTypes.CHROME;
        }
        return BrowserTypes.valueOf(browserProperty.toUpperCase());
    }

    public BrowserTypes getBrowser() {
        return browser;
    }

    public static Proxy getSeleniumProxy() {
        return seleniumProxy;
    }

    public static void setSeleniumProxy(Proxy seleniumProxy) {
        TestBase.seleniumProxy = seleniumProxy;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public String getLocale() {
        return locale;
    }

}