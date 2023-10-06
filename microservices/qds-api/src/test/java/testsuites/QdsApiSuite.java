package testsuites;

import com.apriori.BidPackageItemTest;
import com.apriori.BidPackageProjectItemTest;
import com.apriori.BidPackageProjectUserTest;
import com.apriori.BidPackageTest;
import com.apriori.LayoutTest;
import com.apriori.QdsBidPackageProjectsTest;

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
