package com.infrastructure;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.AccessControls;
import com.apriori.utils.common.customer.response.Customer;
import com.apriori.utils.common.customer.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.utils.common.customer.response.LicensedApplication;
import com.apriori.utils.common.customer.response.Site;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.InfrastructurePage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.UserCreation;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;
import java.util.stream.Collectors;

public class AccessControlsApplicationTests extends TestBase {

    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private InfrastructurePage infrastructurePage;
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String customerName;
    private UserCreation userCreation;
    private String siteIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String appIdentity;

    @Before
    public void setup() {
        customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        userCreation = new UserCreation();
        sourceUsers = userCreation.populateStaffTestUsers(2, customerIdentity, customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        String deploymentName = generateStringUtil.generateDeploymentName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, deploymentName, siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> newApplication = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = newApplication.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .siteIdentity(siteIdentity)
                .licenseIdentity(licensedApplicationIdentity)
                .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);

        installationIdentity = installation.getResponseEntity().getIdentity();
        installationIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .deploymentIdentity(deploymentIdentity)
                .installationIdentity(installationIdentity)
                .build();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        infrastructurePage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToInfrastructure()
                .selectApplication("aPriori Professional");
    }

    @After
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
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"13224", "13225", "13226", "13227", "13230", "14060"})
    public void testUsersCanBeGivenAccessToApplication() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<Users> customerUsers = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_USERS, Users.class, HttpStatus.SC_OK, customerIdentity);
        String serviceAccountIdentity = customerUsers.getResponseEntity().getItems().stream().filter(givenName -> givenName.getUserProfile().getGivenName().equals("service-account")).collect(Collectors.toList()).get(0).getIdentity();
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