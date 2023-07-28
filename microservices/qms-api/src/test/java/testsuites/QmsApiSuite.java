package testsuites;

import com.apriori.BidPackageProjectItemTest;
import com.apriori.BidPackageProjectUserTest;
import com.apriori.BidPackageProjectsTest;
import com.apriori.LayoutConfigurationTest;
import com.apriori.QmsBidPackageItemTest;
import com.apriori.QmsBidPackageTest;
import com.apriori.QmsComponentTest;
import com.apriori.QmsProjectItemTest;
import com.apriori.QmsProjectUserPermissionsTest;
import com.apriori.QmsProjectsFilteredTest;
import com.apriori.QmsProjectsTest;
import com.apriori.QmsScenarioDiscussionFilteredTest;
import com.apriori.QmsScenarioDiscussionTest;
import com.apriori.QmsScenarioSharingTest;
import com.apriori.QmsUserPreferenceTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    BidPackageProjectItemTest.class,
    BidPackageProjectsTest.class,
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
    QmsScenarioDiscussionFilteredTest.class,
    QmsProjectUserPermissionsTest.class
})
public class QmsApiSuite {
}
