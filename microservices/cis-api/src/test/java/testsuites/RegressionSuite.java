package testsuites;

import com.apriori.cis.api.tests.CisBidPackageItemTest;
import com.apriori.cis.api.tests.CisBidPackageProjectUsersTest;
import com.apriori.cis.api.tests.CisBidPackageProjectsTest;
import com.apriori.cis.api.tests.CisBidPackageTest;
import com.apriori.cis.api.tests.CisComponentTest;
import com.apriori.cis.api.tests.CisDiscussionTest;
import com.apriori.cis.api.tests.CisProjectsTest;
import com.apriori.cis.api.tests.CisUserPreferencesTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CisUserPreferencesTest.class,
    CisBidPackageTest.class,
    CisBidPackageItemTest.class,
    CisBidPackageProjectsTest.class,
    CisComponentTest.class,
    CisBidPackageProjectUsersTest.class,
    CisProjectsTest.class,
    CisDiscussionTest.class
})
public class RegressionSuite {
}
