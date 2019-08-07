package main.java.base;

import main.java.constants.Constants;
import main.java.rules.TestRule;
import main.java.runner.ConcurrentTestRunner;
import main.java.utils.ConsoleLogHandler;
import main.java.utils.MaximizeBrowserOnUnix;
import main.java.utils.MyTestWatcher;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;

/**
 * @author kpatel
 */
@RunWith(ConcurrentTestRunner.class)
public class TestBase {

    protected static final Logger logger = LoggerFactory.getLogger(TestBase.class);

    @Rule
    public TestName name = new TestName();
    @Rule
    public MyTestWatcher watchman = new MyTestWatcher();
    @Rule
    public TestRule testRule = new TestRule();

    protected String browser;
    protected TestMode mode;
    protected TestType type;
    protected String locale;
    private static Proxy seleniumProxy;

    protected String downloadPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
    protected String remoteDownloadFolder = null;

    protected WebDriver driver = null;

    public TestBase() {
        mode = getTestMode(System.getProperty("mode"));
        browser = getBrowserType(System.getProperty("browser"));
        type = getTestType(System.getProperty("type"));
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

        if (browser.equalsIgnoreCase("chrome")) {
            driver = new EventFiringWebDriver(df.getDriver());
            logger.info("CONSOLE LOG LEVEL: " + Constants.consoleLogLevel.getName());
            ConsoleLogHandler consoleLogHandler = new ConsoleLogHandler(Constants.consoleLogLevel);
            ((EventFiringWebDriver) driver).register(consoleLogHandler);
        }

        String os = System.getProperty("os.name");
        logger.info("Current Operating System:" + os);
        logger.info("Windows width before Maximize: " + driver.manage().window().getSize().getWidth());

        if (!df.isHeadless() && mode.equals(TestMode.LOCAL) && (os.toLowerCase().contains("linux") || os.toLowerCase().contains("mac"))) {
            MaximizeBrowserOnUnix.maximizeOnUnixSystems(driver);
        } else {
            driver.manage().window().maximize();
        }

        logger.info("Windows width after Maximize: " + driver.manage().window().getSize().getWidth());
        driver.manage().deleteAllCookies();
        logger.debug("Browser window size: " + driver.manage().window().getSize());


        MDC.put("methodName", this.getClass().getSimpleName() + "." + name.getMethodName());
        if (type.equals(TestType.EXPORT)) {
            logger.debug("Driver for " + this.getClass().getSimpleName() + "." + name.getMethodName() + " started: " + driver.hashCode() + " with download path=" + downloadPath);
        } else {
            logger.debug("Driver for " + this.getClass().getSimpleName() + "." + name.getMethodName() + " started: " + driver.hashCode());
        }

    }

    @After
    public void closeBrowser() {
        MDC.remove("methodName");
    }

    public WebDriver getDriver() {
        return driver;
    }

    public TestMode getMode() {
        return mode;
    }

    private TestMode getTestMode(String testMode) {
        TestMode result;

        if (testMode == null || testMode.isEmpty()) {
            logger.info("Test mode was null. Setting LOCAL mode.");
            return TestMode.LOCAL;
        }

        switch (testMode) {
            case "QA":
                result = TestMode.QA;
                break;
            case "LOCAL":
                result = TestMode.LOCAL;
                break;
            case "EXPORT":
                result = TestMode.EXPORT;
                break;
            default:
                throw new IllegalStateException("testMode could not be identified");
        }

        logger.info("Test mode set to: " + result.toString());
        return result;
    }

    private TestType getTestType(String testType) {
        TestType type = null;
        if (StringUtils.isEmpty(testType)) {
            logger.debug("Test Type was not defined, assuming it to be :" + TestType.UI);
            return TestType.UI;
        }
        switch (testType) {
            case "EXPORT":
                type = TestType.EXPORT;
                break;
            case "API":
                type = TestType.API;
                break;
            default:
                type = TestType.UI;
                break;
        }
        return type;
    }

    public TestType getType() {
        return type;
    }

    private String getBrowserType(String browserProperty) {
        if (browserProperty == null || browserProperty.isEmpty()) {
            return "chrome";
        }
        return StringUtils.lowerCase(browserProperty);
    }

    public String getBrowser() {
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