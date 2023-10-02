package com.apriori.rules;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

@Slf4j
public class TestRulesApi implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        log.info("STARTING TEST:- {}", extensionContext.getTestClass().map(Class::getName).orElseThrow(null) + "." + extensionContext.getTestMethod().map(Method::getName).orElseThrow(null));
    }
}
