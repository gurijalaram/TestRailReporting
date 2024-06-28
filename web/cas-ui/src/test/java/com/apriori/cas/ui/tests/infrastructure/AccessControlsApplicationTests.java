package com.apriori.cas.ui.tests.infrastructure;

import static com.apriori.cds.api.enums.ApplicationEnum.AP_PRO;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cas.ui.pageobjects.newcustomer.InfrastructurePage;
import com.apriori.cas.ui.utils.UserCreation;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.AccessControls;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.ApplicationUtil;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.InstallationUtil;
import com.apriori.cds.api.utils.SiteUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.LicensedApplications;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Disabled("Feature was disabled")
public class AccessControlsApplicationTests extends TestBaseUI {

    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ApplicationUtil applicationUtil;
    private InstallationUtil installationUtil;
    private SiteUtil siteUtil;
    private InfrastructurePage infrastructurePage;
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private UserCreation userCreation;
    private String customerIdentity;
    private String customerName;
    private String siteIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String appIdentity;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        applicationUtil = new ApplicationUtil(requestEntityUtil);
        userCreation = new UserCreation(requestEntityUtil);
        installationUtil = new InstallationUtil(requestEntityUtil);
        siteUtil = new SiteUtil(requestEntityUtil);

        customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email, requestEntityUtil.getEmbeddedUser()).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();

        sourceUsers = userCreation.populateStaffTestUsers(2, customerIdentity, customerName);
        String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        String siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        String deploymentName = generateStringUtil.generateAlphabeticString("Deployment", 3);
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, deploymentName, siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateNumericString("RealmKey", 26);
        appIdentity = applicationUtil.getApplicationIdentity(AP_PRO);
        ResponseWrapper<LicensedApplications> newApplication = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = newApplication.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .siteIdentity(siteIdentity)
                .licenseIdentity(licensedApplicationIdentity)
                .build();

        ResponseWrapper<InstallationItems> installation = installationUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);

        installationIdentity = installation.getResponseEntity().getIdentity();
        installationIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .deploymentIdentity(deploymentIdentity)
                .installationIdentity(installationIdentity)
                .build();

        applicationUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        infrastructurePage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToInfrastructure()
                .selectApplication("aPriori Professional");
    }

    @AfterEach
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.APPLICATION_INSTALLATION_BY_ID, customerIdentity, deploymentIdentity, installationIdentity, appIdentity);
        sourceUsers.forEach(user -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
                licensedAppIdentityHolder.customerIdentity(),
                licensedAppIdentityHolder.siteIdentity(),
                licensedAppIdentityHolder.licenseIdentity());
        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
                installationIdentityHolder.customerIdentity(),
                installationIdentityHolder.deploymentIdentity(),
                installationIdentityHolder.installationIdentity());
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Description("Validate all users can be given access to applications from infrastructure view")
    @Tag(SMOKE)
    @TestRail(id = {13224, 13225, 13226, 13227, 13230, 14060})
    public void testUsersCanBeGivenAccessToApplication() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<Users> customerUsers = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_USERS, Users.class, HttpStatus.SC_OK, customerIdentity);
        String serviceAccountIdentity = customerUsers.getResponseEntity().getItems().stream().filter(givenName -> givenName.getUserProfile().getGivenName().equals("service-account"))
            .toList().get(0).getIdentity();
        String userIdentity = sourceUsers.get(0).getIdentity();
        String user2Identity = sourceUsers.get(1).getIdentity();

        ResponseWrapper<AccessControls> serviceAccountControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, serviceAccountIdentity);
        long accessControlsServiceAccount = serviceAccountControls.getResponseEntity().getTotalItemCount();

        ResponseWrapper<AccessControls> userControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(userControls.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected user doesn't have access control to customer application")
            .isEqualTo(0L);

        InfrastructurePage grantAll = infrastructurePage
            .clickGrantAllButton()
            .clickAllCancelButton()
            .clickGrantAllButton()
            .clickAllOkButton();

        soft.assertThat(grantAll.getTextSuccessMessage())
            .isEqualTo("Users have been granted access successfully");
        grantAll.closeMessage();

        ResponseWrapper<AccessControls> userControlsGranted = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(userControlsGranted.getResponseEntity().getItems().get(0).getDeploymentIdentity())
            .overridingErrorMessage("Expected all users were granted access control to customer application")
            .isEqualTo(deploymentIdentity);

        ResponseWrapper<AccessControls> user2ControlsGranted = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, user2Identity);
        soft.assertThat(user2ControlsGranted.getResponseEntity().getItems().get(0).getDeploymentIdentity())
            .overridingErrorMessage("Expected all users were granted access control to customer application")
            .isEqualTo(deploymentIdentity);

        ResponseWrapper<AccessControls> serviceAccountControlsGranted = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, serviceAccountIdentity);
        soft.assertThat(serviceAccountControlsGranted.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected service accounts access controls were not changed after grant all")
            .isEqualTo(accessControlsServiceAccount);

        InfrastructurePage denyAll = grantAll
            .clickDenyAllButton()
            .clickAllCancelButton()
            .clickDenyAllButton()
            .clickAllOkButton();

        soft.assertThat(denyAll.getTextSuccessMessage())
            .isEqualTo("Users have been denied access successfully");

        ResponseWrapper<AccessControls> serviceAccountsAfterDeny = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, serviceAccountIdentity);
        soft.assertThat(serviceAccountsAfterDeny.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected service accounts access controls were not changed after deny all")
            .isEqualTo(accessControlsServiceAccount);

        ResponseWrapper<AccessControls> userDeniedControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(userDeniedControls.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected all users were denied access control to customer application")
            .isEqualTo(0L);

        ResponseWrapper<AccessControls> user2DeniedControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, user2Identity);
        soft.assertThat(user2DeniedControls.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected all users were denied access control to customer application")
            .isEqualTo(0L);
        soft.assertAll();
    }
}
