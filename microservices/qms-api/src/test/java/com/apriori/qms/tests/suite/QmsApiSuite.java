package com.apriori.qms.tests.suite;

import com.apriori.qms.tests.QmsComponentTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1359")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    QmsComponentTest.class
})
public class QmsApiSuite {
}
