package com.apriori.internalapi.services.suite;

import com.apriori.internalapi.services.CdsApplications;
import com.apriori.internalapi.services.CdsCustomers;
import com.apriori.internalapi.services.CdsRoles;
import com.apriori.internalapi.services.CdsUsers;
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
