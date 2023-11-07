package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.models.response.UserPreference;
import com.apriori.cds.api.models.response.UserPreferences;
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
public class CdsUserPreferencesTests {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private SoftAssertions soft = new SoftAssertions();
    private IdentityHolder userPreferenceIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String userIdentity;
    private String siteIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;
    private String installationIdentity;

    @AfterEach
    public void deletePreferences() {
        if (userPreferenceIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.PREFERENCE_BY_ID,
                userPreferenceIdentityHolder.customerIdentity(),
                userPreferenceIdentityHolder.userIdentity(),
                userPreferenceIdentityHolder.userPreferenceIdentity()
            );
        }
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApProIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApProIdentity);
        }
        if (licensedCirIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCirIdentity);
        }
        if (licensedCiaIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCiaIdentity);
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
    @TestRail(id = {12397})
    @Description("Returns a paged set of UserPreferences for a specific user.")
    public void getUserPreferences() {
        setCustomerData();
        ResponseWrapper<UserPreferences> userPreferences = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_PREFERENCES, UserPreferences.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(userPreferences.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {12398, 12399})
    @Description("Creates a user preference for a user and gets it by identity")
    public void addUserPreference() {
        setCustomerData();
        ResponseWrapper<UserPreference> newPreference = cdsTestUtil.addUserPreference(customerIdentity, userIdentity);
        String preferenceIdentity = newPreference.getResponseEntity().getIdentity();

        ResponseWrapper<UserPreference> preferenceResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.PREFERENCE_BY_ID, UserPreference.class, HttpStatus.SC_OK, customerIdentity, userIdentity, preferenceIdentity);

        soft.assertThat(preferenceResponse.getResponseEntity().getIdentity()).isEqualTo(preferenceIdentity);
        soft.assertAll();

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .userPreferenceIdentity(preferenceIdentity)
            .build();
    }

    @Test
    @TestRail(id = {12400})
    @Description("Updates an existing user preference by identity")
    public void updateUserPreference() {
        setCustomerData();
        String updatedPreference = generateStringUtil.getRandomString();

        ResponseWrapper<UserPreference> newPreference = cdsTestUtil.addUserPreference(customerIdentity, userIdentity);
        String preferenceIdentity = newPreference.getResponseEntity().getIdentity();

        ResponseWrapper<UserPreference> updatedPreferenceResponse = cdsTestUtil.updatePreference(customerIdentity, userIdentity, preferenceIdentity, updatedPreference);

        soft.assertThat(updatedPreferenceResponse.getResponseEntity().getValue()).isEqualTo(updatedPreference);
        soft.assertAll();

        userPreferenceIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .userPreferenceIdentity(preferenceIdentity)
            .build();
    }

    @Test
    @TestRail(id = {12401, 12402})
    @Description("Adds or Replaces a UserPreference for a user")
    public void putUserPreference() {
        setCustomerData();
        String preferenceName = generateStringUtil.getRandomString();

        ResponseWrapper<UserPreference> preferenceResponse = cdsTestUtil.putUserPreference(customerIdentity, userIdentity, preferenceName);

        soft.assertThat(preferenceResponse.getResponseEntity().getName()).isEqualTo(preferenceName);
        soft.assertAll();

        String preferenceIdentity = preferenceResponse.getResponseEntity().getIdentity();

        cdsTestUtil.delete(CDSAPIEnum.PREFERENCE_BY_ID, customerIdentity, userIdentity, preferenceIdentity);
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