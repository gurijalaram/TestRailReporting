package com.apriori.tests.suite;

import com.apriori.tests.ApplicationsTests;
import com.apriori.tests.CasConfigurationsTests;
import com.apriori.tests.CasCustomersTests;
import com.apriori.tests.CasCustomersUsersTests;
import com.apriori.tests.CasDeploymentsTests;
import com.apriori.tests.CasIdentityProvidersTests;
import com.apriori.tests.CasSitesTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*@ProjectRunID("361")*/
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        ApplicationsTests.class,
        CasConfigurationsTests.class,
        CasCustomersTests.class,
        CasDeploymentsTests.class,
        CasIdentityProvidersTests.class,
        CasSitesTests.class,
        CasCustomersUsersTests.class
})
public class CasAPISuite {
}
