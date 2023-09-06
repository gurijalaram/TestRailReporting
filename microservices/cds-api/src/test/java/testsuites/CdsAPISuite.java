package testsuites;

import com.apriori.ApVersionsTests;
import com.apriori.CdsAccessAuthorizationsTests;
import com.apriori.CdsAccessControlsTests;
import com.apriori.CdsApplicationsTests;
import com.apriori.CdsAssociationUserTests;
import com.apriori.CdsConfigurationsTests;
import com.apriori.CdsCustomAttributesTests;
import com.apriori.CdsCustomerAssociationTests;
import com.apriori.CdsCustomerUserRolesTests;
import com.apriori.CdsCustomerUsersTests;
import com.apriori.CdsCustomersTests;
import com.apriori.CdsDeploymentsTests;
import com.apriori.CdsFeatureTests;
import com.apriori.CdsGetCustomerTests;
import com.apriori.CdsIdentityProvidersTests;
import com.apriori.CdsInstallationApplicationTests;
import com.apriori.CdsInstallationsTests;
import com.apriori.CdsLicenseTests;
import com.apriori.CdsRolesTests;
import com.apriori.CdsSitesApplicationsTests;
import com.apriori.CdsSitesTests;
import com.apriori.CdsUserEnablementsTests;
import com.apriori.CdsUserPreferencesTests;
import com.apriori.CdsUsersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
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
    CdsFeatureTests.class,
    CdsUserEnablementsTests.class
})
public class CdsAPISuite {
}
