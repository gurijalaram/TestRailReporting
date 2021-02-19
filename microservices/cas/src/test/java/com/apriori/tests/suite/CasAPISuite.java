package com.apriori.tests.suite;

import com.apriori.tests.*;
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
        CasSitesTests.class
})
public class CasAPISuite {
}
