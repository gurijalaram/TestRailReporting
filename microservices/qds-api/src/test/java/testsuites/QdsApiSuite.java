package testsuites;

import com.apriori.BidPackageItemTest;
import com.apriori.BidPackageProjectItemTest;
import com.apriori.BidPackageProjectUserTest;
import com.apriori.BidPackageProjectsTest;
import com.apriori.BidPackageTest;
import com.apriori.LayoutTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    BidPackageTest.class,
    BidPackageItemTest.class,
    BidPackageProjectsTest.class,
    BidPackageProjectItemTest.class,
    BidPackageProjectUserTest.class,
    LayoutTest.class,
})
public class QdsApiSuite {
}
