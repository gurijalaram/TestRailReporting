package com.apriori;

import com.apriori.ats.models.response.AtsErrorMessage;
import com.apriori.ats.utils.AtsTestUtil;
import com.apriori.ats.utils.enums.ATSAPIEnum;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.models.response.User;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AtsUsersTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private AtsTestUtil atsTestUtil = new AtsTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    private String customerIdentity;
    private String userIdentity;
    private UserCredentials currentUser = UserUtil.getUser();
    private ResponseWrapper<Site> site;
    private String siteIdentity;
    private ResponseWrapper<Deployment> deployment;
    private String deploymentIdentity;
    private String licensedApplicationIdentity;
    private String installationIdentity;

    @AfterEach
    public void cleanUp() {
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApplicationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApplicationIdentity);
        }
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                customerIdentity,
                userIdentity
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @TestRail(id = {3578})
    @Description("Get the current representation of a user identified by their email.")
    public void getUserByEmailTest() {
        String userEmail = "qa-automation-01@apriori.com";
        ResponseWrapper<User> user = atsTestUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, User.class, HttpStatus.SC_OK, userEmail);

        soft.assertThat(user.getResponseEntity().getEmail()).isEqualTo(userEmail);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3668})
    @Description("Get user by invalid email")
    public void getUserInvalidEmail() {
        String invalidEmail = "qa-automation-01-apriori.com";
        ResponseWrapper<AtsErrorMessage> errorResponse = atsTestUtil.getCommonRequest(ATSAPIEnum.USER_BY_EMAIL, AtsErrorMessage.class, HttpStatus.SC_BAD_REQUEST, invalidEmail);

        soft.assertThat(errorResponse.getResponseEntity().getMessage()).isEqualTo("'email' is not a valid email address.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {22086, 22087})
    @Description("Reset the MFA configuration for a user.")
    public void resetUserMFA() {
        String customerName = generateStringUtil.generateCustomerName();
        String userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        user = cdsTestUtil.addUser(customerIdentity, userName, emailPattern);
        userIdentity = user.getResponseEntity().getIdentity();

        atsTestUtil.resetUserMFA(ATSAPIEnum.CUSTOMER_USERS_MFA, customerIdentity, HttpStatus.SC_ACCEPTED);
        atsTestUtil.resetUserMFA(ATSAPIEnum.USER_MFA, userIdentity, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {3579})
    @Description("Update/change the password of a user identified by their email")
    public void changeUserPassword() {
        String customerName = generateStringUtil.generateCustomerName();
        String userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();

        customer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email, currentUser);
        customerIdentity = customer.getResponseEntity().getIdentity();
        site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        user = cdsTestUtil.addUser(customerIdentity, userName, email);
        userIdentity = user.getResponseEntity().getIdentity();
        String userEmail = user.getResponseEntity().getEmail();

        atsTestUtil.changePassword(userEmail);
    }
}