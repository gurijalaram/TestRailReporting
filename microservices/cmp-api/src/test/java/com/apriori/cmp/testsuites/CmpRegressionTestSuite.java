package com.apriori.cmp.testsuites;

import com.apriori.cmp.tests.CmpComparisonTests;
import com.apriori.cmp.tests.CmpSaveComparisonTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("2766")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    CmpSaveComparisonTests.class,
    CmpComparisonTests.class
})

public class CmpRegressionTestSuite {
}
