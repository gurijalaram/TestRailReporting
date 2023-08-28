package testsuites;

import com.apriori.CisBidPackageItemTest;
import com.apriori.CisBidPackageProjectUsersTest;
import com.apriori.CisBidPackageProjectsTest;
import com.apriori.CisBidPackageTest;
import com.apriori.CisComponentTest;
import com.apriori.CisUserPreferencesTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CisUserPreferencesTest.class,
    CisBidPackageTest.class,
    CisBidPackageItemTest.class,
    CisBidPackageProjectsTest.class,
    CisComponentTest.class,
    CisBidPackageProjectUsersTest.class
})
public class RegressionSuite {
}
