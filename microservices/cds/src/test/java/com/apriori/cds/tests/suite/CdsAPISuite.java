package com.apriori.cds.tests.suite;

import com.apriori.cds.tests.CdsApplicationsTests;
import com.apriori.cds.tests.CdsCustomersTests;
import com.apriori.cds.tests.CdsRolesTests;
import com.apriori.cds.tests.CdsUsersTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("361")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CdsApplicationsTests.class,
    CdsCustomersTests.class,
    CdsRolesTests.class,
    CdsUsersTests.class
})
public class CdsAPISuite {

}
