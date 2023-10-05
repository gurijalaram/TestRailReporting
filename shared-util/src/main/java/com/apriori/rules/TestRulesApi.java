package com.apriori.rules;

import static com.apriori.properties.LoadProperties.loadProperties;
import static com.apriori.testrail.TestRailStatus.DISABLED;
import static com.apriori.testrail.TestRailStatus.FAILED;
import static com.apriori.testrail.TestRailStatus.PASSED;
import static com.apriori.testrail.TestRailStatus.RETEST;

import com.apriori.testrail.TestRail;
import com.apriori.testrail.TestRailReport;
import com.apriori.testrail.TestRailStatus;

import com.codepine.api.testrail.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class TestRulesApi implements BeforeTestExecutionCallback, BeforeAllCallback, TestWatcher {

    private static final String TESTRAIL_REPORT = "TEST_RAIL";
    private static boolean started = false;

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.info("STARTING TEST:- {}", context.getTestClass().map(Class::getName).orElseThrow(null) + "." + context.getTestMethod().map(Method::getName).orElseThrow(null));
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.info("TEST DISABLED:- {}: REASON:- {}", context.getDisplayName(), reason.orElse("Reason not supplied"));

        addResult(DISABLED, context);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("TEST SUCCESSFUL:- {}: ", context.getTestClass().map(Class::getName).orElseThrow(null) + "." + context.getTestMethod().map(Method::getName).orElseThrow(null));

        addResult(PASSED, context);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.info("TEST ABORTED:- {}: CAUSE:- {}", context.getDisplayName(), cause.getMessage());

        addResult(RETEST, context);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.info("TEST FAILED:- {}: CAUSE:- {}", context.getTestClass().map(Class::getName).orElseThrow(null) + "." + context.getTestMethod().map(Method::getName).orElseThrow(null),
            cause.getMessage());

        addResult(FAILED, context);
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
                TestRailReport.addResult(result, Integer.parseInt(loadProperties("ProjectRunId").getProperty("project_run_id")));
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
            //After all tests run hook.
            //Any additional desired action goes here
            TestRailReport.reportResults();
        }
    }
}
