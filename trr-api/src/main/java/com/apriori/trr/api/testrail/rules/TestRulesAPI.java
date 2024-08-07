package com.apriori.trr.api.testrail.rules;

import static com.apriori.trr.api.testrail.TestRailStatus.DISABLED;
import static com.apriori.trr.api.testrail.TestRailStatus.FAILED;
import static com.apriori.trr.api.testrail.TestRailStatus.PASSED;
import static com.apriori.trr.api.testrail.TestRailStatus.RETEST;

import com.apriori.trr.api.testrail.TestRailRule;
import com.apriori.trr.api.testrail.TestRailStatus;
import com.apriori.trr.api.testrail.model.Result;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author cfrith
 */
@Slf4j
public class TestRulesAPI implements BeforeTestExecutionCallback, BeforeAllCallback, TestWatcher {

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
            TestRailRule.class)) {
            TestRailRule element = context.getElement().get().getAnnotation(TestRailRule.class);
            Arrays.stream(element.id()).forEach(id -> {
                Result result = new Result();
                result.setTestId(id);
                result.setStatusId(status.getId());
                result.setCaseId(id);
                // TestRailReport.addResult(result, Integer.parseInt(LoadProperties.loadProperties("ProjectRunId").getProperty("project_run_id")));
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
            //TestRailReport.reportResults();
        }
    }
}
