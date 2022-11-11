package com.apriori.qds.tests.suite;

import com.apriori.qds.tests.BidPackageItemTest;
import com.apriori.qds.tests.BidPackageProjectItemTest;
import com.apriori.qds.tests.BidPackageProjectUserTest;
import com.apriori.qds.tests.BidPackageProjectsTest;
import com.apriori.qds.tests.BidPackageTest;
import com.apriori.qds.tests.LayoutTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1359")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    BidPackageTest.class,
    BidPackageItemTest.class,
    BidPackageProjectsTest.class,
    BidPackageProjectItemTest.class,
    BidPackageProjectUserTest.class,
    LayoutTest.class,

})
public class QdsApiSuite {
}
