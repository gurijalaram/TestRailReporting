package com.apriori.apitests.cds.suite;

import com.apriori.apitests.cds.CdsApplications;
import com.apriori.apitests.cds.CdsCustomers;
import com.apriori.apitests.cds.CdsRoles;
import com.apriori.apitests.cds.CdsUsers;
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
