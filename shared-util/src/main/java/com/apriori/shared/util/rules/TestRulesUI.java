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
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
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
@ExtendWith(ReportPortalExtension.class)
public class TestRulesUI implements TestWatcher, BeforeAllCallback, InvocationInterceptor {

    public static final String DRIVER = "driver";
    private static final String TESTRAIL_REPORT = "TEST_RAIL";
    private static boolean started = false;

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

        addResult(FAILED, context);

        getDeclaredDriver(context).quit();
    }

    @SneakyThrows
    private WebDriver getDeclaredDriver(ExtensionContext context) {
        Field field = TestBaseUI.class.getDeclaredField(DRIVER);
        field.setAccessible(true);
        return (WebDriver) field.get(context.getRequiredTestInstance());
    }

    /**
     * Takes a screenshot with Selenium and attach it to the current test item in case of test failure.
     *
     * @param invocation        JUnit 5th invocation object
     * @param invocationContext JUnit 5th invocation context
     * @param extensionContext  JUnit 5th extension context
     */
    @Override
    public void interceptTestMethod(InvocationInterceptor.Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {
        try {
            invocation.proceed();
        } catch (Throwable cause) {
            File screenshot;
            if (getDeclaredDriver(extensionContext) instanceof RemoteWebDriver) {
                WebDriver webdriver = new Augmenter().augment(getDeclaredDriver(extensionContext));
                screenshot = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
            } else {
                screenshot = ((TakesScreenshot) getDeclaredDriver(extensionContext)).getScreenshotAs(OutputType.FILE);
            }

            sendScreenshotToAllure(extensionContext, screenshot);
            sendScreenshotToReportPortal(extensionContext, screenshot);

            throw cause;
        }
    }

    private void sendScreenshotToAllure(ExtensionContext extensionContext, File screenshot) {

        try {
            Allure.addAttachment(extensionContext.getDisplayName(), FileUtils.openInputStream(screenshot));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendScreenshotToReportPortal(ExtensionContext extensionContext, File screenshot) {
        log.info("RP_MESSAGE#FILE#{}#{}", screenshot, extensionContext.getDisplayName());
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
