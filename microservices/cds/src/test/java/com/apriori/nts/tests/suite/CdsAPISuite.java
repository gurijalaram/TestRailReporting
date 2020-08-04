package com.apriori.nts.tests.suite;

import com.apriori.nts.tests.CdsApplications;
import com.apriori.nts.tests.CdsCustomers;
import com.apriori.nts.tests.CdsRoles;
import com.apriori.nts.tests.CdsUsers;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("361")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CdsApplications.class,
    CdsCustomers.class,
    CdsRoles.class,
    CdsUsers.class
})
public class CdsAPISuite {

}
