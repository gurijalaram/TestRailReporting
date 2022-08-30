package com.apriori.testsuites;

import com.apriori.tests.CnhPositiveTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("704")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CnhPositiveTests.class
})
public class APISuite {
}
