package testsuites;

import com.apriori.BidPackageProjectItemTest;
import com.apriori.BidPackageProjectUserTest;
import com.apriori.LayoutConfigurationTest;
import com.apriori.QmsBidPackageItemTest;
import com.apriori.QmsBidPackageProjectsTest;
import com.apriori.QmsBidPackageTest;
import com.apriori.QmsComponentTest;
import com.apriori.QmsProjectItemTest;
import com.apriori.QmsProjectUserPermissionsTest;
import com.apriori.QmsProjectsFilteredTest;
import com.apriori.QmsProjectsTest;
import com.apriori.QmsDiscussionFilteredTest;
import com.apriori.QmsScenarioDiscussionTest;
import com.apriori.QmsScenarioSharingTest;
import com.apriori.QmsUserPreferenceTest;

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
