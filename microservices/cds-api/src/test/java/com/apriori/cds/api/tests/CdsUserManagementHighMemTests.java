package com.apriori.cds.api.tests;

import static com.apriori.cds.api.enums.ApplicationEnum.ACS;
import static com.apriori.cds.api.enums.ApplicationEnum.AP_PRO;
import static com.apriori.cds.api.enums.ApplicationEnum.CIA;
import static com.apriori.cds.api.enums.ApplicationEnum.CIR;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.cds.api.models.response.AccessControls;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.ApplicationUtil;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.cds.api.utils.SiteUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CdsUserManagementHighMemTests {
    private final String customerAssignedRole = "APRIORI_DEVELOPER";
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private ApplicationUtil applicationUtil;
    private SiteUtil siteUtil;
    private String appIdentity;
    private String customerIdentity;
    private String siteIdentity;
    private String deploymentIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;
    private String installationIdentityReg;
    private String installationIdentityHighMem;
    private String userIdentity;

    @BeforeEach
    public void setDetails() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        applicationUtil = new ApplicationUtil(requestEntityUtil);
        siteUtil = new SiteUtil(requestEntityUtil);
        appIdentity = applicationUtil.getApplicationIdentity(AP_PRO);
    }

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
        if (licensedCiaIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCiaIdentity);
        }
        if (licensedCirIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCirIdentity);
        }
        if (licensedAcsIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedAcsIdentity);
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
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();
        String ciaIdentity = applicationUtil.getApplicationIdentity(CIA);
        String cirIdentity = applicationUtil.getApplicationIdentity(CIR);
        String acsIdentity = applicationUtil.getApplicationIdentity(ACS);

        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentityReg = installation.getResponseEntity().getIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApProIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ciaLicensed = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, ciaIdentity);
        licensedCiaIdentity = ciaLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> cirLicensed = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, cirIdentity);
        licensedCirIdentity = cirLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ascLicensed = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, acsIdentity);
        licensedAcsIdentity = ascLicensed.getResponseEntity().getIdentity();

        applicationUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityReg, appIdentity, siteIdentity);
        applicationUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityReg, ciaIdentity, siteIdentity);
        applicationUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityReg, cirIdentity, siteIdentity);
        applicationUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityReg, acsIdentity, siteIdentity);

        String realmKey2 = generateStringUtil.generateRealmKey();
        String cloudRefHighMem = generateStringUtil.generateCloudReference();

        ResponseWrapper<InstallationItems> installationHighMem = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "High Mem Test Installation", realmKey2, cloudRefHighMem, siteIdentity, true);
        installationIdentityHighMem = installationHighMem.getResponseEntity().getIdentity();
        applicationUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentityHighMem, appIdentity, siteIdentity);

        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
