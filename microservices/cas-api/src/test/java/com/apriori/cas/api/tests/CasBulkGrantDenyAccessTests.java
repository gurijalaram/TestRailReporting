package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.AccessControls;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.Sites;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.LicensedApplications;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CasBulkGrantDenyAccessTests {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private final UserCredentials currentUser = UserUtil.getUser("admin");
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private List<User> sourceUsers;
    private String customerIdentity;
    private String aPrioriIdentity;
    private String siteIdentity;
    private String aPsiteIdentity;
    private String deploymentIdentity;
    private String aPdeploymentIdentity;
    private String installationIdentity;
    private String apInstallationIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;
    private String achIdentity;

    @BeforeEach
    public void setup() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
        aPrioriIdentity = casTestUtil.getAprioriInternal().getIdentity();
        aPsiteIdentity = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, aPrioriIdentity).getResponseEntity().getItems().stream().filter(site -> site.getName().contains("Internal")).collect(Collectors.toList()).get(0).getIdentity();
        aPdeploymentIdentity = PropertiesContext.get("cds.apriori_production_deployment_identity");
        apInstallationIdentity = PropertiesContext.get("cds.apriori_core_services_installation_identity");
        achIdentity = PropertiesContext.get("cds.apriori_cloud_home_identity");
    }

    @AfterEach
    public void teardown() {
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
        sourceUsers.forEach((user) -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Description("Bulk grants a source customer's users application access to the target customers applications.")
    @TestRail(id = {13213})
    public void bulkGrantAccessOutOfContext() {
        setCustomerData();
        String user1Identity = sourceUsers.get(0).getIdentity();
        String user2Identity = sourceUsers.get(1).getIdentity();

        casTestUtil.grantDenyAll(aPrioriIdentity, aPsiteIdentity, aPdeploymentIdentity, apInstallationIdentity, achIdentity, "grant-all", customerIdentity);

        ResponseWrapper<AccessControls> userControlsGranted = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK,
                customerIdentity,
                user1Identity);
        soft.assertThat(userControlsGranted.getResponseEntity().getItems().get(0).getDeploymentIdentity())
                .overridingErrorMessage("Expected all users were granted access control to customer application")
                .isEqualTo(aPdeploymentIdentity);

        ResponseWrapper<AccessControls> user2ControlsGranted = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK,
                customerIdentity,
                user2Identity);
        soft.assertThat(user2ControlsGranted.getResponseEntity().getItems().get(0).getDeploymentIdentity())
                .overridingErrorMessage("Expected all users were granted access control to customer application")
                .isEqualTo(aPdeploymentIdentity);
        soft.assertAll();
    }

    @Test
    @Description("Bulk denies all access controls to target customers applications.")
    @TestRail(id = {13214})
    public void denyAllAccessOutOfContext() {
        setCustomerData();
        String user1Identity = sourceUsers.get(0).getIdentity();
        String user2Identity = sourceUsers.get(1).getIdentity();

        casTestUtil.grantDenyAll(aPrioriIdentity, aPsiteIdentity, aPdeploymentIdentity, apInstallationIdentity, achIdentity, "grant-all", customerIdentity);

        casTestUtil.grantDenyAll(aPrioriIdentity, aPsiteIdentity, aPdeploymentIdentity, apInstallationIdentity, achIdentity, "deny-all", customerIdentity);

        ResponseWrapper<AccessControls> userDeniedControls = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK,
                customerIdentity,
                user1Identity);
        soft.assertThat(userDeniedControls.getResponseEntity().getTotalItemCount())
                .overridingErrorMessage("Expected all users were denied access control to customer application")
                .isEqualTo(2L);

        ResponseWrapper<AccessControls> user2DeniedControls = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK,
                customerIdentity,
                user2Identity);
        soft.assertThat(user2DeniedControls.getResponseEntity().getTotalItemCount())
                .overridingErrorMessage("Expected all users were denied access control to customer application")
                .isEqualTo(2L);
        soft.assertAll();
    }

    @Test
    @Description("Bulk grant/deny all access controls to customers applications")
    @TestRail(id = {13211, 13212})
    public void grantAllDenyAll() {
        setCustomerData();
        String userIdentity = sourceUsers.get(0).getIdentity();

        casTestUtil.grantDenyAll(customerIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, "grant-all", null);

        ResponseWrapper<AccessControls> userControlsGranted = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK,
                customerIdentity,
                userIdentity);
        soft.assertThat(userControlsGranted.getResponseEntity().getItems().get(3).getDeploymentIdentity())
                .overridingErrorMessage("Expected all users were granted access control to customer application")
                .isEqualTo(deploymentIdentity);

        casTestUtil.grantDenyAll(customerIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, "deny-all", null);

        ResponseWrapper<AccessControls> userDeniedControls = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class, HttpStatus.SC_OK,
                customerIdentity,
                userIdentity);
        soft.assertThat(userDeniedControls.getResponseEntity().getTotalItemCount())
                .overridingErrorMessage("Expected all users were denied access control to customer application")
                .isEqualTo(3L);
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        Customer sourceCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = sourceCustomer.getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApProIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ciaLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, ciaIdentity);
        licensedCiaIdentity = ciaLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> cirLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, cirIdentity);
        licensedCirIdentity = cirLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> acsLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, acsIdentity);
        licensedAcsIdentity = acsLicensed.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, cirIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, acsIdentity, siteIdentity);

        sourceUsers = new ArrayList<>();
        sourceUsers.add(casTestUtil.createUser(sourceCustomer).getResponseEntity());
        sourceUsers.add(casTestUtil.createUser(sourceCustomer).getResponseEntity());
    }
}