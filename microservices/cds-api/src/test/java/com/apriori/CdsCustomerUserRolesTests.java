package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.ErrorResponse;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.models.response.UserRole;
import com.apriori.cds.models.response.UserRoles;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.models.response.User;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsCustomerUserRolesTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    private String customerName;
    private String userName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String userIdentity;
    private final String role = "AP_USER_ADMIN";
    private final String invalidRole = "ADMIN";
    private SoftAssertions soft = new SoftAssertions();
    private String siteIdentity;
    private String deploymentIdentity;
    private String licensedApplicationIdentity;
    private String installationIdentity;

    @BeforeEach
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApplicationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApplicationIdentity);
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {13414})
    @Description("Returns a list of roles for a user")
    public void getUserRoles() {
        ResponseWrapper<UserRoles> userRoles = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES, UserRoles.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(userRoles.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {13415, 13417, 13422, 17166})
    @Description("Create a role for a user, gets it by identity and delete")
    public void postUserRoles() {
        ResponseWrapper<UserRole> newRole = cdsTestUtil.createRoleForUser(customerIdentity, userIdentity, role);
        String roleId = newRole.getResponseEntity().getIdentity();

        ResponseWrapper<UserRole> userRole = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ROLES_BY_ID, UserRole.class, HttpStatus.SC_OK, customerIdentity, userIdentity, roleId);

        soft.assertThat(userRole.getResponseEntity().getIdentity()).isEqualTo(roleId);
        soft.assertAll();

        cdsTestUtil.delete(CDSAPIEnum.USER_ROLES_BY_ID, customerIdentity, userIdentity, roleId);
    }

    @Test
    @TestRail(id = {17165})
    @Description("Try to create an invalid role (without AP_* prefix) for a user and verify that it fails")
    public void postInvalidUserRoles() {
        String expectedMessage = "Resource 'Role' with identity 'ADMIN' was not found";
        String expectedError = "Not Found";
        ErrorResponse response = cdsTestUtil.createInvalidRoleForUser(customerIdentity, userIdentity,invalidRole);

        soft.assertThat(response.getMessage()).isEqualTo(expectedMessage);
        soft.assertThat(response.getError()).isEqualTo(expectedError);
        soft.assertAll();
    }
}
