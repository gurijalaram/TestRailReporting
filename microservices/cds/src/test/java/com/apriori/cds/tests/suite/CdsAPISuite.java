package com.apriori.cds.tests.suite;

import com.apriori.cds.tests.CdsApplications;
import com.apriori.cds.tests.CdsCustomersTests;
import com.apriori.cds.tests.CdsRoles;
import com.apriori.cds.tests.CdsUsers;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("361")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CdsApplications.class,
    CdsCustomersTests.class,
    CdsRoles.class,
    CdsUsers.class
})
public class CdsAPISuite {

}
