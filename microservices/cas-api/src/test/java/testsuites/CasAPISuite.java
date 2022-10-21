package testsuites;

import com.apriori.tests.ApplicationsTests;
import com.apriori.tests.CasBatchItemTests;
import com.apriori.tests.CasConfigurationsTests;
import com.apriori.tests.CasCustomerAssociationTests;
import com.apriori.tests.CasCustomerBatchTests;
import com.apriori.tests.CasCustomerUserAssociationTests;
import com.apriori.tests.CasCustomersTests;
import com.apriori.tests.CasCustomersUsersTests;
import com.apriori.tests.CasDeploymentsTests;
import com.apriori.tests.CasIdentityProvidersTests;
import com.apriori.tests.CasLicenseTests;
import com.apriori.tests.CasSitesTests;
import com.apriori.tests.CasUsersTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("644")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ApplicationsTests.class,
    CasConfigurationsTests.class,
    CasCustomersTests.class,
    CasDeploymentsTests.class,
    CasIdentityProvidersTests.class,
    CasLicenseTests.class,
    CasSitesTests.class,
    CasCustomersUsersTests.class,
    CasUsersTests.class,
    CasCustomerBatchTests.class,
    CasBatchItemTests.class,
    CasCustomerUserAssociationTests.class,
    CasCustomerAssociationTests.class
})
public class CasAPISuite {
}
