package testsuites;

import com.apriori.cas.api.tests.ApplicationsTests;
import com.apriori.cas.api.tests.CasAccessAuthorizationTests;
import com.apriori.cas.api.tests.CasBatchItemTests;
import com.apriori.cas.api.tests.CasBulkGrantDenyAccessTests;
import com.apriori.cas.api.tests.CasConfigurationsTests;
import com.apriori.cas.api.tests.CasCustomerAssociationTests;
import com.apriori.cas.api.tests.CasCustomerBatchTests;
import com.apriori.cas.api.tests.CasCustomerServiceAccountsTests;
import com.apriori.cas.api.tests.CasCustomerUserAccessControlsTests;
import com.apriori.cas.api.tests.CasCustomerUserAssociationTests;
import com.apriori.cas.api.tests.CasCustomersTests;
import com.apriori.cas.api.tests.CasCustomersUsersTests;
import com.apriori.cas.api.tests.CasDeploymentsTests;
import com.apriori.cas.api.tests.CasIdentityProvidersTests;
import com.apriori.cas.api.tests.CasSiteLicenseSublicenseTests;
import com.apriori.cas.api.tests.CasSiteLicenseTests;
import com.apriori.cas.api.tests.CasSitesTests;
import com.apriori.cas.api.tests.CasUserSubLicensesTests;
import com.apriori.cas.api.tests.CasUsersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
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
