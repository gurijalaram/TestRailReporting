package testsuites;

import com.apriori.qds.api.tests.BidPackageItemTest;
import com.apriori.qds.api.tests.BidPackageProjectItemTest;
import com.apriori.qds.api.tests.BidPackageProjectUserTest;
import com.apriori.qds.api.tests.BidPackageTest;
import com.apriori.qds.api.tests.LayoutTest;
import com.apriori.qds.api.tests.QdsBidPackageProjectsTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    BidPackageTest.class,
    BidPackageItemTest.class,
    QdsBidPackageProjectsTest.class,
    BidPackageProjectItemTest.class,
    BidPackageProjectUserTest.class,
    LayoutTest.class,
})
public class QdsApiSuite {
}
