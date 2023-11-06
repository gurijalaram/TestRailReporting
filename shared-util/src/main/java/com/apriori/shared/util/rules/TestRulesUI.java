package com.apriori.shared.util.rules;

import static com.apriori.shared.util.testconfig.TestMode.DOCKER_GRID;
import static com.apriori.shared.util.testconfig.TestMode.QA_LOCAL;
import static com.apriori.shared.util.testrail.TestRailStatus.DISABLED;
import static com.apriori.shared.util.testrail.TestRailStatus.FAILED;
import static com.apriori.shared.util.testrail.TestRailStatus.PASSED;
import static com.apriori.shared.util.testrail.TestRailStatus.RETEST;
import static com.apriori.shared.util.webdriver.DriverFactory.testMode;

import com.apriori.shared.util.properties.LoadProperties;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.shared.util.testrail.TestRailReport;
import com.apriori.shared.util.testrail.TestRailStatus;

import com.codepine.api.testrail.model.Result;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author cfrith
 */
@Slf4j
public class TestRulesUI implements TestWatcher, BeforeAllCallback {

    public static final String DRIVER = "driver";
    private static final String TESTRAIL_REPORT = "TEST_RAIL";
    private static boolean started = false;

    @Attachment(value = "{1}", type = "image/png")
    private static byte[] saveImageAttach(String screenshotUrl, String fileName) {
        try {
            return toByteArray(screenshotUrl);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return new byte[0];
    }

    private static byte[] toByteArray(String uri) throws IOException {
        return Files.readAllBytes(Paths.get(uri));
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.info("TEST DISABLED:- {}: REASON:- {}", context.getDisplayName(), reason.orElse("Reason not supplied"));

        addResult(DISABLED, context);

        getDeclaredDriver(context).quit();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("TEST SUCCESSFUL:- {}: ", context.getTestClass().map(Class::getName).orElseThrow(null) + "." + context.getTestMethod().map(Method::getName).orElseThrow(null));

        addResult(PASSED, context);

        getDeclaredDriver(context).quit();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.info("TEST ABORTED:- {}: CAUSE:- {}", context.getDisplayName(), cause.getMessage());

        addResult(RETEST, context);

        getDeclaredDriver(context).quit();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.info("TEST FAILED:- {}: CAUSE:- {}", context.getTestClass().map(Class::getName).orElseThrow(null) + "." + context.getTestMethod().map(Method::getName).orElseThrow(null),
            cause.getMessage());

        String retryingScreenshot = captureScreenshot(context.getTestClass().map(Class::getName).orElse("Class"), context.getDisplayName(), context);
        saveImageAttach(new File(retryingScreenshot).getPath(), context.getTestClass().map(Class::getName) + "." + context.getTestMethod().map(Method::getName));

        addResult(FAILED, context);

        getDeclaredDriver(context).quit();
    }

    @SneakyThrows
    private WebDriver getDeclaredDriver(ExtensionContext context) {
        Field field = TestBaseUI.class.getDeclaredField(DRIVER);
        field.setAccessible(true);
        return (WebDriver) field.get(context.getRequiredTestInstance());
    }

    // TODO: 06/06/2023 revise this method and make sure naming is correct
    private String captureScreenshot(String className, String testName, ExtensionContext context) {
        String filename;
        String filePath = null;
        try {
            File screenshot;
            log.debug("FAILURE TIME: " + LocalDateTime.now(ZoneId.of("UTC")));
            if (getDeclaredDriver(context) instanceof RemoteWebDriver) {
                WebDriver webdriver = new Augmenter().augment(getDeclaredDriver(context));
                screenshot = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
            } else {
                screenshot = ((TakesScreenshot) getDeclaredDriver(context)).getScreenshotAs(OutputType.FILE);
            }
            filename = File.separator + "target" + File.separator + "screenshots" + File.separator + "screenshot-" + className + "-" + testName + "-" + "chrome" + 1 + ".png";
            filePath = new File(filename).getCanonicalPath();
            FileUtils.copyFile(screenshot, new File(filename));
            Allure.addAttachment(filename, FileUtils.openInputStream(screenshot));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return filePath;
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!started) {
            getStore(context).put(TESTRAIL_REPORT, new CloseableOnlyOnceResource());
            started = true;
        }
    }

    private void addResult(TestRailStatus status, ExtensionContext context) {
        if (context.getElement().isPresent() && context.getElement().get().isAnnotationPresent(
            TestRail.class)) {
            TestRail element = context.getElement().get().getAnnotation(TestRail.class);

            Arrays.stream(element.id()).forEach(id -> {
                Result result = new Result()
                    .setTestId(id)
                    .setStatusId(status.getId())
                    .setCaseId(id);
                TestRailReport.addResult(result, Integer.parseInt(LoadProperties.loadProperties("ProjectRunId").getProperty("project_run_id")));
            });
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);
    }

    private static class CloseableOnlyOnceResource implements
        ExtensionContext.Store.CloseableResource {
        @Override
        public void close() {
            if (testMode.value().equalsIgnoreCase(QA_LOCAL.value()) || testMode.value().equalsIgnoreCase(DOCKER_GRID.value())) {
                return;
            }
            //After all tests run hook.
            //Any additional desired action goes here
            TestRailReport.reportResults();
        }
    }
}
