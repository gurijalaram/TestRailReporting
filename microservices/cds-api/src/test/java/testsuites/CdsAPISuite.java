package testsuites;

import com.apriori.cds.tests.ApVersionsTests;
import com.apriori.cds.tests.CdsAccessAuthorizationsTests;
import com.apriori.cds.tests.CdsAccessControlsTests;
import com.apriori.cds.tests.CdsApplicationsTests;
import com.apriori.cds.tests.CdsAssociationUserTests;
import com.apriori.cds.tests.CdsConfigurationsTests;
import com.apriori.cds.tests.CdsCustomAttributesTests;
import com.apriori.cds.tests.CdsCustomerAssociationTests;
import com.apriori.cds.tests.CdsCustomerUserRolesTests;
import com.apriori.cds.tests.CdsCustomerUsersTests;
import com.apriori.cds.tests.CdsCustomersTests;
import com.apriori.cds.tests.CdsDeploymentsTests;
import com.apriori.cds.tests.CdsFeatureTests;
import com.apriori.cds.tests.CdsGetCustomerTests;
import com.apriori.cds.tests.CdsIdentityProvidersTests;
import com.apriori.cds.tests.CdsInstallationApplicationTests;
import com.apriori.cds.tests.CdsInstallationsTests;
import com.apriori.cds.tests.CdsLicenseTests;
import com.apriori.cds.tests.CdsRolesTests;
import com.apriori.cds.tests.CdsSitesApplicationsTests;
import com.apriori.cds.tests.CdsSitesTests;
import com.apriori.cds.tests.CdsUserPreferencesTests;
import com.apriori.cds.tests.CdsUsersTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("641")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ApVersionsTests.class,
    CdsAccessAuthorizationsTests.class,
    CdsAccessControlsTests.class,
    CdsApplicationsTests.class,
    CdsAssociationUserTests.class,
    CdsConfigurationsTests.class,
    CdsCustomAttributesTests.class,
    CdsCustomerAssociationTests.class,
    CdsCustomersTests.class,
    CdsCustomerUserRolesTests.class,
    CdsCustomerUsersTests.class,
    CdsDeploymentsTests.class,
    CdsGetCustomerTests.class,
    CdsIdentityProvidersTests.class,
    CdsInstallationApplicationTests.class,
    CdsInstallationsTests.class,
    CdsLicenseTests.class,
    CdsRolesTests.class,
    CdsSitesApplicationsTests.class,
    CdsSitesTests.class,
    CdsUserPreferencesTests.class,
    CdsUsersTests.class,
    CdsFeatureTests.class
})
public class CdsAPISuite {

}
