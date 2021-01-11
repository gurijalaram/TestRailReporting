package com.apriori.utils.web.driver;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class MyTestWatcher extends TestWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTestWatcher.class);

    @Override
    protected void failed(Throwable e, Description description) {
        MDC.put("methodName", description.getTestClass().getSimpleName() + "." + description.getMethodName());
        super.failed(e, description);
        LOGGER.info("Test {} failed.", description.getMethodName());
        MDC.remove("methodName");
    }

    @Override
    protected void succeeded(Description description) {
        MDC.put("methodName", description.getTestClass().getSimpleName() + "." + description.getMethodName());
        super.succeeded(description);
        LOGGER.info("Test {} succesfully run.", description.getMethodName());
        MDC.remove("methodName");
    }

    @Override
    protected void starting(Description description) {
        MDC.put("methodName", description.getTestClass().getSimpleName() + "." + description.getMethodName());
        super.starting(description);
        LOGGER.info("Test {} is running.", description.getMethodName());
        MDC.remove("methodName");

    }
}
