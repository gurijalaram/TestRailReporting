package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.IdentityProviderPagination;
import com.apriori.cds.models.response.IdentityProviderResponse;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.models.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class CdsIdentityProvidersTests {
    private String customerIdentity;
    private String userIdentity;
    private String idpIdentity;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private ResponseWrapper<Customer> customer;
    private String userName;
    private ResponseWrapper<User> user;
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
        if (idpIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS, customerIdentity, idpIdentity);
        }
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
    @TestRail(id = {5824, 5961})
    @Description("Create an Identity provider for a customer")
    public void postCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> samlResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        soft.assertThat(samlResponse.getResponseEntity().getIdentity()).isNotNull();
        soft.assertAll();

        idpIdentity = samlResponse.getResponseEntity().getIdentity();
    }

    @Test
    @TestRail(id = {5963})
    @Description("Update Identity provider for a customer")
    public void patchCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        idpIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviderResponse> updatedDescription = cdsTestUtil.patchIdp(customerIdentity, idpIdentity, userIdentity);
        soft.assertThat(updatedDescription.getResponseEntity().getDescription()).isEqualTo("patch IDP using Automation");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5962})
    @Description("getIDP details by Identity")
    public void getIDPbyIdentity() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        idpIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviderResponse> idp = cdsTestUtil.getCommonRequest(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS,
            IdentityProviderResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            idpIdentity
        );

        soft.assertThat(idp.getResponseEntity().getIdentity()).isEqualTo(idpIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5960})
    @Description("getIDP list for customer")
    public void getListOfIDPsForCustomer() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        idpIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviderPagination> idpPagination = cdsTestUtil.getCommonRequest(CDSAPIEnum.SAML_BY_CUSTOMER_ID,
            IdentityProviderPagination.class,
            HttpStatus.SC_OK,
            customerIdentity
        );

        soft.assertThat(idpPagination.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5961})
    @Description("Deletes identity provider")
    public void deleteCustomerIdentityProvider() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<IdentityProviderResponse> identityProviderResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, userName);
        String identityProviderIdentity = identityProviderResponse.getResponseEntity().getIdentity();

        cdsTestUtil.delete(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS,
            customerIdentity,
            identityProviderIdentity
        );
    }
}
