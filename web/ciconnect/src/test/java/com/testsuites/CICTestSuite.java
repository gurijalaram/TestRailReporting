package com.testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.login.LoginTests;
import com.navbar.NavBarTests;
import com.workflow.WorkflowTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class,
    NavBarTests.class,
    WorkflowTests.class
})

public class CICTestSuite {
}
