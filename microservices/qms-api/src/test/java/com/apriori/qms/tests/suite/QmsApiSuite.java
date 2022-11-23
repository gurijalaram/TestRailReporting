package com.apriori.qms.tests.suite;

import com.apriori.qms.tests.BidPackageProjectUserTest;
import com.apriori.qms.tests.BidPackageProjectsTest;
import com.apriori.qms.tests.QmsBidPackageItemTest;
import com.apriori.qms.tests.QmsBidPackageTest;
import com.apriori.qms.tests.QmsComponentTest;
import com.apriori.qms.tests.ScenarioDiscussionTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1712")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    QmsComponentTest.class,
    QmsBidPackageTest.class,
    QmsBidPackageItemTest.class,
    BidPackageProjectsTest.class,
    BidPackageProjectUserTest.class,
    ScenarioDiscussionTest.class
})
public class QmsApiSuite {
}
