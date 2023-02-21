package com.apriori.utils;

import com.apriori.utils.web.driver.MyTestWatcher;
import com.apriori.utils.web.driver.TestMode;
import com.apriori.utils.web.driver.TestType;
import com.apriori.utils.web.rules.TestRailRule;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHelper {

    public static final Logger logger = LoggerFactory.getLogger(TestHelper.class);

    @Rule
    public TestName name = new TestName();
    @Rule
    public MyTestWatcher watchman = new MyTestWatcher();
    @Rule
    public TestRailRule testRailRule = new TestRailRule();

    protected TestMode mode;
    protected TestType type;

    public TestHelper() {

        mode = getTestMode(System.getProperty("mode"));
        type = getTestType(System.getProperty("type"));
    }

    public TestMode getMode() {
        return mode;
    }

    private TestMode getTestMode(String testMode) {
        TestMode result;

        if (testMode == null || testMode.isEmpty()) {
            TestHelper.logger.info("Test mode was null. Setting LOCAL mode.");
            return TestMode.LOCAL;
        }

        switch (testMode.toUpperCase()) {
            case "QA":
                result = TestMode.QA;
                break;
            case "LOCAL":
                result = TestMode.LOCAL;
                break;
            case "EXPORT":
                result = TestMode.EXPORT;
                break;
            case "GRID":
                result = TestMode.GRID;
                break;
            case "DOCKER":
                result = TestMode.DOCKER;
                break;
            default:
                throw new IllegalStateException("testMode could not be identified");
        }

        TestHelper.logger.info("Test mode set to: " + result.toString());
        return result;
    }

    private TestType getTestType(String testType) {
        TestType type;
        if (StringUtils.isEmpty(testType)) {
            TestHelper.logger.debug("Test Type was not defined, assuming it to be :" + TestType.UI);
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

}
