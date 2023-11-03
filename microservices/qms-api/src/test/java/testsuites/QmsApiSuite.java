package testsuites;

import com.apriori.qms.api.tests.BidPackageProjectItemTest;
import com.apriori.qms.api.tests.BidPackageProjectUserTest;
import com.apriori.qms.api.tests.LayoutConfigurationTest;
import com.apriori.qms.api.tests.QmsBidPackageItemTest;
import com.apriori.qms.api.tests.QmsBidPackageProjectsTest;
import com.apriori.qms.api.tests.QmsBidPackageTest;
import com.apriori.qms.api.tests.QmsComponentTest;
import com.apriori.qms.api.tests.QmsDiscussionFilteredTest;
import com.apriori.qms.api.tests.QmsProjectItemTest;
import com.apriori.qms.api.tests.QmsProjectUserPermissionsTest;
import com.apriori.qms.api.tests.QmsProjectsFilteredTest;
import com.apriori.qms.api.tests.QmsProjectsTest;
import com.apriori.qms.api.tests.QmsScenarioDiscussionTest;
import com.apriori.qms.api.tests.QmsScenarioSharingTest;
import com.apriori.qms.api.tests.QmsUserPreferenceTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    BidPackageProjectItemTest.class,
    QmsBidPackageProjectsTest.class,
    BidPackageProjectUserTest.class,
    LayoutConfigurationTest.class,
    QmsBidPackageItemTest.class,
    QmsBidPackageTest.class,
    QmsComponentTest.class,
    QmsProjectItemTest.class,
    QmsProjectsTest.class,
    QmsProjectsFilteredTest.class,
    QmsScenarioSharingTest.class,
    QmsUserPreferenceTest.class,
    QmsScenarioDiscussionTest.class,
    QmsDiscussionFilteredTest.class,
    QmsProjectUserPermissionsTest.class
})
public class QmsApiSuite {
}
