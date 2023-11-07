package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.IdentityProviderPagination;
import com.apriori.cds.api.models.response.IdentityProviderResponse;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.LicensedApplications;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsIdentityProvidersTests {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private String customerIdentity;
    private String userIdentity;
    private String idpIdentity;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<User> user;
    private SoftAssertions soft = new SoftAssertions();
    private String siteIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;
    private String installationIdentity;

    @AfterEach
    public void cleanUp() {
        if (idpIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS, customerIdentity, idpIdentity);
        }
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApProIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApProIdentity);
        }
        if (licensedCiaIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCiaIdentity);
        }
        if (licensedCirIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCirIdentity);
        }
        if (licensedAcsIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedAcsIdentity);
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
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<IdentityProviderResponse> identityProviderResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, userName);
        String identityProviderIdentity = identityProviderResponse.getResponseEntity().getIdentity();

        cdsTestUtil.delete(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS,
            customerIdentity,
            identityProviderIdentity
        );
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(rcd.getCustomerName(), rcd.getCustomerType(), rcd.getCloudRef(), rcd.getSalesForceId(), rcd.getEmailPattern());
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApProIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ciaLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, ciaIdentity);
        licensedCiaIdentity = ciaLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> cirLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, cirIdentity);
        licensedCirIdentity = cirLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ascLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, acsIdentity);
        licensedAcsIdentity = ascLicensed.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, cirIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, acsIdentity, siteIdentity);

        String userName = generateStringUtil.generateUserName();
        user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
