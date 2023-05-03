package com.apriori.qms.tests.suite;

import com.apriori.qms.tests.BidPackageProjectItemTest;
import com.apriori.qms.tests.BidPackageProjectUserTest;
import com.apriori.qms.tests.BidPackageProjectsTest;
import com.apriori.qms.tests.LayoutConfigurationTest;
import com.apriori.qms.tests.QmsBidPackageItemTest;
import com.apriori.qms.tests.QmsBidPackageTest;
import com.apriori.qms.tests.QmsComponentTest;
import com.apriori.qms.tests.QmsProjectItemTest;
import com.apriori.qms.tests.QmsProjectsTest;
import com.apriori.qms.tests.QmsScenarioSharingTest;
import com.apriori.qms.tests.QmsUserPreferenceTest;
import com.apriori.qms.tests.ScenarioDiscussionTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1712")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    BidPackageProjectItemTest.class,
    BidPackageProjectsTest.class,
    BidPackageProjectUserTest.class,
    LayoutConfigurationTest.class,
    QmsBidPackageItemTest.class,
    QmsBidPackageTest.class,
    QmsComponentTest.class,
    QmsProjectItemTest.class,
    QmsProjectsTest.class,
    QmsScenarioSharingTest.class,
    QmsUserPreferenceTest.class,
    ScenarioDiscussionTest.class
})

public class QmsApiSuite {
}
