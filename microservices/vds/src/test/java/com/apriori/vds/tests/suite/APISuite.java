package com.apriori.vds.tests.suite;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import com.apriori.vds.tests.AccessControlsTest;

import com.apriori.vds.tests.ConfigurationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("704")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AccessControlsTest.class,
    ConfigurationTest.class
})
public class APISuite {
}
