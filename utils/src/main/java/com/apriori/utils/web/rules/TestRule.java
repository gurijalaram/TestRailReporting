package com.apriori.utils.web.rules;

import com.apriori.utils.web.driver.TestBase;
import com.apriori.utils.web.driver.TestMode;
import com.apriori.utils.web.driver.TestType;
import com.apriori.utils.web.util.ConsoleLogger;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;

/* Repeats a test if a test is part of @RetryTestSuite i.e. annotated with @Category(Repeat.class)
 * if the test is also annotated with @Repeat then the value of number of times test should repeat can be passed as a value e.g. @Repeat(2) will run tests 3 times
 * By Default failed tests run 2 times
 */
public class TestRule implements MethodRule {

    private static final Logger logger = LoggerFactory.getLogger(TestRule.class);

    private int times = 0;

    private Throwable originalException = null;
    private String originalScreenshot = null;
    private boolean retry = false;

    public Statement apply(final Statement s, final FrameworkMethod fm, final Object o) {
        return new Statement() {
            TestBase testBaseNew = (TestBase) o;
            FrameworkMethod frameworkMethod = fm;
            Statement statement = s;

            @Override
            public void evaluate() throws Throwable {
                //String filename = null;
                try {
                    MDC.put("methodName", frameworkMethod.getMethod().getDeclaringClass().getSimpleName() + "." + frameworkMethod.getName());

                    if (testBaseNew.getMode().equals(TestMode.LOCAL_GRID) || (StringUtils.isNotEmpty(System.getProperty("repeatTests")) && System.getProperty("repeatTests").equalsIgnoreCase("true"))) {
                        logger.debug("Running test: ' " + frameworkMethod.getMethod().getDeclaringClass().getCanonicalName() + "." + frameworkMethod.getName() + "' through retry with retry count: " + times);
                        retry = true;
                        repeatTest(statement, times, frameworkMethod, testBaseNew);
                    } else {
                        statement.evaluate();
                    }

                    MDC.remove("methodName");
                } catch (Throwable t) {
                    MDC.put("methodName", frameworkMethod.getMethod().getDeclaringClass().getSimpleName() + "." + frameworkMethod.getName());
                    if (t instanceof MultipleFailureException) {
                        ((MultipleFailureException) t).getFailures().forEach(exception -> logger.error("Exception caught: ", exception));
                    } else {
                        logger.error("Exception caught: ", t);
                    }

                    String driverHash = testBaseNew.getDriver() != null ? testBaseNew.getDriver().hashCode() + "" : "null";
                    logger.debug("FAILURE IN " + frameworkMethod.getMethod().getDeclaringClass().getCanonicalName() + "." + frameworkMethod.getName() + " with driver: " + driverHash);
                    String retryingScreenshot = captureScreenshot(testBaseNew.getClass().getCanonicalName(), frameworkMethod.getName(), testBaseNew, times + 1);
                    saveImageAttach(new File(retryingScreenshot).getPath(), frameworkMethod.getMethod().getDeclaringClass().getName() + "." + frameworkMethod.getName());

                    MDC.remove("methodName");
                    if (retry) {
                        throw originalException; // rethrow to allow the failure to be reported to JUnit
                    } else {
                        throw t;
                    }
                } finally {
                    MDC.put("methodName", frameworkMethod.getMethod().getDeclaringClass().getSimpleName() + "." + frameworkMethod.getName());

                    if (testBaseNew.getDriver() != null && testBaseNew.getBrowser().value().equalsIgnoreCase("chrome")) {
                        Logs logs = testBaseNew.getDriver().manage().logs();
                        try {
                            ConsoleLogger.printConsoleEntries(logs, frameworkMethod);
                        } catch (NullPointerException ex) {
                            logger.error("No console logs were available.");
                        }
                    }
                    logger.debug("Closing driver" + " for "
                        + frameworkMethod.getMethod().getDeclaringClass().getCanonicalName() + "."
                        + frameworkMethod.getName() + ": " +
                        (testBaseNew.getDriver() != null ? testBaseNew.getDriver().hashCode() : "null"));

                    if (testBaseNew.getDriver() == null) {
                        logger.debug("Driver object was not created");
                    } else if (testBaseNew.getBrowser().value().equalsIgnoreCase("firefox")) {
                        testBaseNew.getDriver().close();
                    } else {
                        testBaseNew.getDriver().quit();
                    }

                    //The download folder will be deleted only after the retries
                    if (testBaseNew.getType().equals(TestType.EXPORT) && StringUtils.isNotEmpty(testBaseNew.getDownloadPath())) {
                        File downloadFolder = new File(testBaseNew.getDownloadPath());
                        FileUtils.forceDelete(downloadFolder);
                    }

                    MDC.remove("methodName");
                }
            }

        };
    }


    private void repeatTest(Statement statement, int repeat, FrameworkMethod fm, TestBase testBaseNew) throws Throwable {
        boolean failed = true;
        for (int i = 0; i <= repeat; i++) {
            try {
                statement.evaluate();
                failed = false;
                break;
            } catch (Throwable t) {
                // Adding some comments
                originalException = t;
                int retryNo = i + 1;
                if (retryNo < repeat + 1) {
                    logger.debug("Original issue with test " + fm.getMethod().getDeclaringClass().getCanonicalName() + "." + fm.getName() + ": ");
                    logger.debug("Exception: ", t);
                    originalScreenshot = captureScreenshot(testBaseNew.getClass().getCanonicalName(), fm.getName(), testBaseNew, i + 1);
                    logger.debug("RETRY NO. " + retryNo);

                    if (testBaseNew.getDriver() == null) {
                        logger.debug("Driver object was not created");
                    } else if (testBaseNew.getBrowser().value().equalsIgnoreCase("firefox")) {
                        testBaseNew.getDriver().close();
                    } else {
                        testBaseNew.getDriver().quit();
                    }
                }
            }
        }
        if (failed) {
            throw originalException;
        }
    }

    /**
     * Creates screenshot with the classname as filename
     *
     * @param className   - name of class for screenshot
     * @param testName    - name of test
     * @param testBase    - test base instance
     * @param errorNumber - failed 1st time or 2nd time or whatever time
     * @return something
     */
    public String captureScreenshot(String className, String testName, TestBase testBase, int errorNumber) {
        String filename;
        String filePath = null;
        try {
            File screenshot;
            logger.debug("FAILURE TIME: " + LocalDateTime.now(ZoneId.of("UTC")).toString());
            if (testBase.getDriver() instanceof RemoteWebDriver) {
                WebDriver webdriver = new Augmenter().augment(testBase.getDriver());
                screenshot = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
            } else {
                screenshot = ((TakesScreenshot) testBase.getDriver()).getScreenshotAs(OutputType.FILE);
            }
            filename = File.separator + "target" + File.separator + "screenshots" + File.separator + "screenshot-" + className + "-" + testName + "-" + testBase.getBrowser() + errorNumber + ".png";
            filePath = new File(filename).getCanonicalPath();
            FileUtils.copyFile(screenshot, new File(filename));
            Allure.addAttachment(filename, FileUtils.openInputStream(screenshot));
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return filePath;
    }

    @Attachment(value = "{1}", type = "image/png")
    public static byte[] saveImageAttach(String screenshotUrl, String fileName) {
        try {
            return toByteArray(screenshotUrl);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return new byte[0];
    }

    private static byte[] toByteArray(String uri) throws IOException {
        return Files.readAllBytes(Paths.get(uri));
    }
}
