package testsuites;

import com.apriori.ApplicationsTests;
import com.apriori.CasAccessAuthorizationTests;
import com.apriori.CasBatchItemTests;
import com.apriori.CasBulkGrantDenyAccessTests;
import com.apriori.CasConfigurationsTests;
import com.apriori.CasCustomerAssociationTests;
import com.apriori.CasCustomerBatchTests;
import com.apriori.CasCustomerServiceAccountsTests;
import com.apriori.CasCustomerUserAccessControlsTests;
import com.apriori.CasCustomerUserAssociationTests;
import com.apriori.CasCustomersTests;
import com.apriori.CasCustomersUsersTests;
import com.apriori.CasDeploymentsTests;
import com.apriori.CasIdentityProvidersTests;
import com.apriori.CasSiteLicenseSublicenseTests;
import com.apriori.CasSiteLicenseTests;
import com.apriori.CasSitesTests;
import com.apriori.CasUserSubLicensesTests;
import com.apriori.CasUsersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    ApplicationsTests.class,
    CasConfigurationsTests.class,
    CasCustomersTests.class,
    CasDeploymentsTests.class,
    CasIdentityProvidersTests.class,
    CasUserSubLicensesTests.class,
    CasSitesTests.class,
    CasCustomersUsersTests.class,
    CasUsersTests.class,
    CasCustomerBatchTests.class,
    CasBatchItemTests.class,
    CasCustomerUserAssociationTests.class,
    CasCustomerAssociationTests.class,
    CasCustomerUserAccessControlsTests.class,
    CasBulkGrantDenyAccessTests.class,
    CasSiteLicenseTests.class,
    CasSiteLicenseSublicenseTests.class,
    CasAccessAuthorizationTests.class,
    CasCustomerServiceAccountsTests.class
})
public class CasAPISuite {
}
