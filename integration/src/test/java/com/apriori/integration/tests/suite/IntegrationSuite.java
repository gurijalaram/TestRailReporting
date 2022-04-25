package com.apriori.integration.tests.suite;

import com.apriori.integration.tests.BcsSdsIntegrationTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("859")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        BcsSdsIntegrationTests.class
})
public class IntegrationSuite {
}
