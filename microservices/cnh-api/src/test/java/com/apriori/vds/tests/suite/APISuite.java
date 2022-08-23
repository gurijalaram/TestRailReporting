package com.apriori.vds.tests.suite;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import com.apriori.cnh.tests.CnhPositiveTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("704")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CnhPositiveTests.class
})
public class APISuite {
}
