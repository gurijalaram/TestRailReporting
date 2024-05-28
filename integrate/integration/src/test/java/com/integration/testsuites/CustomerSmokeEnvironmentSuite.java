package com.integration.testsuites;

import com.apriori.ach.api.tests.AchMainPageAPITestAPI;
import com.apriori.ach.ui.tests.AchMainPageUITest;

import com.integration.tests.CustomerComponentLifeCycleTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AchMainPageUITest.class,
    AchMainPageAPITestAPI.class,
    CustomerComponentLifeCycleTests.class
})
public class CustomerSmokeEnvironmentSuite {
}
