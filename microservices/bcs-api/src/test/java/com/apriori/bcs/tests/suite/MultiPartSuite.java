package com.apriori.bcs.tests.suite;

import com.apriori.bcs.tests.MultiPartCostingScenarioTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("848")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        MultiPartCostingScenarioTest.class
})
public class MultiPartSuite {
}
