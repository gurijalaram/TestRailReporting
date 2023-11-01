package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.AccessControlResponse;
import com.apriori.cds.models.response.AccessControls;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.cds.utils.RandomCustomerData;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CdsUserManagementHighMemTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String siteIdentity;
    private String deploymentIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String installationIdentityReg;
    private String installationIdentityHighMem;
    private String appIdentity;
    private ResponseWrapper<User> user;
    private String userName;
    private String userIdentity;
    private final String customerAssignedRole = "APRIORI_DEVELOPER";

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (installationIdentityReg != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentityReg);
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
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {28394, 28395})
    @Description("Verify user get access to highMem installation")
    public void getHighMemAccess() {
        setCustomerData();

        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, true, false, false);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUser.getEnablements().getHighMemEnabled()).isTrue();
        soft.assertThat(getUser.getRoles().toString()).contains("AP_HIGH_MEM");

        ResponseWrapper<AccessControls> accessControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        List<AccessControlResponse> apProApplicationControl = accessControls.getResponseEntity().getItems().stream().filter(control -> control.getApplicationIdentity().equals(appIdentity)).collect(Collectors.toList());

        soft.assertThat(apProApplicationControl.size()).isEqualTo(1);
        soft.assertThat(apProApplicationControl.get(0).getInstallationIdentity()).isEqualTo(installationIdentityHighMem);
        soft.assertAll();

        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentityHighMem);
    }

    @Test
    @TestRail(id = {28393})
    @Description("Verify user get access to regular installation when highMem is false")
    public void verifyHighMemIsFalse() {
        setCustomerData();

        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, false);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUser.getEnablements().getHighMemEnabled()).isFalse();
        soft.assertThat(getUser.getRoles().toString()).doesNotContain("AP_HIGH_MEM");

        ResponseWrapper<AccessControls> accessControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        List<AccessControlResponse> apProApplicationControl = accessControls.getResponseEntity().getItems().stream().filter(control -> control.getApplicationIdentity().equals(appIdentity)).collect(Collectors.toList());

        soft.assertThat(apProApplicationControl.size()).isEqualTo(1);
        soft.assertThat(apProApplicationControl.get(0).getInstallationIdentity()).isEqualTo(installationIdentityReg);
        soft.assertAll();

        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentityHighMem);
    }

    @Test
    @TestRail(id = {28392})
    @Description("Verify user get access to regular installation when highMem is false")
    public void deleteHighMemInstallation() {
        setCustomerData();

        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, true, false, false);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUser.getEnablements().getHighMemEnabled()).isTrue();
        soft.assertThat(getUser.getRoles().toString()).contains("AP_HIGH_MEM");

        ResponseWrapper<AccessControls> accessControls = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        List<AccessControlResponse> apProApplicationControl = accessControls.getResponseEntity().getItems().stream().filter(control -> control.getApplicationIdentity().equals(appIdentity)).collect(Collectors.toList());

        soft.assertThat(apProApplicationControl.size()).isEqualTo(1);
        soft.assertThat(apProApplicationControl.get(0).getInstallationIdentity()).isEqualTo(installationIdentityHighMem);

        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentityHighMem);

        User getUser2 = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUser2.getEnablements().getHighMemEnabled()).isFalse();
        soft.assertThat(getUser2.getRoles().toString()).doesNotContain("AP_HIGH_MEM");

        ResponseWrapper<AccessControls> accessControls2 = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        List<AccessControlResponse> apProApplicationControl2 = accessControls2.getResponseEntity().getItems().stream().filter(control -> control.getApplicationIdentity().equals(appIdentity)).collect(Collectors.toList());

        soft.assertThat(apProApplicationControl2.size()).isEqualTo(1);
        soft.assertThat(apProApplicationControl2.get(0).getInstallationIdentity()).isEqualTo(installationIdentityReg);
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(rcd.getCustomerName(), rcd.getCustomerType(), rcd.getCloudRef(), rcd.getSalesForceId(), rcd.getEmailPattern());
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentityReg = installation.getResponseEntity().getIdentity();

        appIdentity = Constants.getApProApplicationIdentity();
        String ciaIdentity = Constants.getCiaApplicationIdentity();
        String cirIdentity = Constants.getCirAppIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApProIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ciaLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, ciaIdentity);
        licensedCiaIdentity = ciaLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> cirLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, cirIdentity);
        licensedCirIdentity = cirLicensed.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityReg, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityReg, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityReg, cirIdentity, siteIdentity);

        String realmKey2 = generateStringUtil.generateRealmKey();
        String cloudRefHighMem = generateStringUtil.generateCloudReference();

        ResponseWrapper<InstallationItems> installationHighMem = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "High Mem Test Installation", realmKey2, cloudRefHighMem, siteIdentity, true);
        installationIdentityHighMem = installationHighMem.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityHighMem, appIdentity, siteIdentity);

        userName = generateStringUtil.generateUserName();
        user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
