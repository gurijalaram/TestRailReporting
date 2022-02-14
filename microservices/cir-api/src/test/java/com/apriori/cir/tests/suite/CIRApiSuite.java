package com.apriori.cir.tests.suite;

import com.apriori.cir.tests.JasperReportTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("") // TODO should be added ID
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    JasperReportTest.class
})
public class CIRApiSuite {
}