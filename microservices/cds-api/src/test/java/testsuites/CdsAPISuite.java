package testsuites;

import com.apriori.cds.api.tests.ApVersionsTests;
import com.apriori.cds.api.tests.CdsAccessAuthorizationsTests;
import com.apriori.cds.api.tests.CdsAccessControlsTests;
import com.apriori.cds.api.tests.CdsApplicationsTests;
import com.apriori.cds.api.tests.CdsAssociationUserTests;
import com.apriori.cds.api.tests.CdsConfigurationsTests;
import com.apriori.cds.api.tests.CdsCustomAttributesTests;
import com.apriori.cds.api.tests.CdsCustomerAssociationTests;
import com.apriori.cds.api.tests.CdsCustomerUserRolesTests;
import com.apriori.cds.api.tests.CdsCustomerUsersTests;
import com.apriori.cds.api.tests.CdsCustomersTests;
import com.apriori.cds.api.tests.CdsDeploymentsTests;
import com.apriori.cds.api.tests.CdsFeatureTests;
import com.apriori.cds.api.tests.CdsGetCustomerTests;
import com.apriori.cds.api.tests.CdsIdentityProvidersTests;
import com.apriori.cds.api.tests.CdsInstallationApplicationTests;
import com.apriori.cds.api.tests.CdsInstallationsTests;
import com.apriori.cds.api.tests.CdsLicenseTests;
import com.apriori.cds.api.tests.CdsRolesTests;
import com.apriori.cds.api.tests.CdsSitesApplicationsTests;
import com.apriori.cds.api.tests.CdsSitesTests;
import com.apriori.cds.api.tests.CdsUpdateUserTests;
import com.apriori.cds.api.tests.CdsUserEnablementsTests;
import com.apriori.cds.api.tests.CdsUserManagementHighMemTests;
import com.apriori.cds.api.tests.CdsUserMgmtSandboxPreviewTests;
import com.apriori.cds.api.tests.CdsUserPreferencesTests;
import com.apriori.cds.api.tests.CdsUsersTests;

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
    CdsUserEnablementsTests.class,
    CdsUserManagementHighMemTests.class,
    CdsUserMgmtSandboxPreviewTests.class,
    CdsUpdateUserTests.class
})
public class CdsAPISuite {
}
