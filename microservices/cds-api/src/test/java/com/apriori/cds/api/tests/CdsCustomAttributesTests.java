package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.CustomAttribute;
import com.apriori.cds.api.models.response.CustomAttributesResponse;
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
public class CdsCustomAttributesTests {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private IdentityHolder customAttributesIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String userIdentity;
    private SoftAssertions soft = new SoftAssertions();
    private String siteIdentity;
    private String installationIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;

    @AfterEach
    public void cleanUp() {
        if (customAttributesIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOM_ATTRIBUTE_BY_ID,
                customAttributesIdentityHolder.customerIdentity(),
                customAttributesIdentityHolder.userIdentity(),
                customAttributesIdentityHolder.customAttributeIdentity()
            );
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
    @TestRail(id = {12365, 12366, 12367})
    @Description("Adding a CustomAttribute for a user and getting it")
    public void addCustomAttribute() {
        setCustomerData();
        String updatedDepartment = generateStringUtil.getRandomString();
        ResponseWrapper<CustomAttribute> customAttributeAdded = cdsTestUtil.addCustomAttribute(customerIdentity, userIdentity);
        soft.assertThat(customAttributeAdded.getResponseEntity().getIdentity()).isNotNull();

        ResponseWrapper<CustomAttributesResponse> customAttributes = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttributesResponse.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(customAttributes.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);

        ResponseWrapper<CustomAttribute> putCustomAttribute = cdsTestUtil.putCustomAttribute(customerIdentity, userIdentity, updatedDepartment);
        String customAttributeIdentity = putCustomAttribute.getResponseEntity().getIdentity();

        customAttributesIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .customAttributeIdentity(customAttributeIdentity)
            .build();

        soft.assertThat(putCustomAttribute.getResponseEntity().getValue()).isEqualTo(updatedDepartment);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {12369, 12370})
    @Description("Get a CustomAttribute by its identity and update")
    public void getCustomAttributeByIdAndUpdate() {
        setCustomerData();
        String updatedDepartment = generateStringUtil.getRandomString();
        ResponseWrapper<CustomAttribute> customAttributeAdded = cdsTestUtil.addCustomAttribute(customerIdentity, userIdentity);

        String attributeIdentity = customAttributeAdded.getResponseEntity().getIdentity();
        ResponseWrapper<CustomAttribute> customAttributes = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOM_ATTRIBUTE_BY_ID, CustomAttribute.class, HttpStatus.SC_OK, customerIdentity, userIdentity, attributeIdentity);

        soft.assertThat(customAttributes.getResponseEntity().getIdentity()).isEqualTo(attributeIdentity);

        ResponseWrapper<CustomAttribute> updateCustomAttribute = cdsTestUtil.updateAttribute(customerIdentity, userIdentity, attributeIdentity, updatedDepartment);

        soft.assertThat(updateCustomAttribute.getResponseEntity().getValue()).isEqualTo(updatedDepartment);
        soft.assertAll();

        customAttributesIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .customAttributeIdentity(attributeIdentity)
            .build();
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
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
