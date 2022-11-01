package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.entity.response.AccessControls;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.Sites;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CasBulkGrantDenyAccessTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private Customer sourceCustomer;
    private List<CustomerUser> sourceUsers;
    private String customerIdentity;
    private String aPrioriIdentity;
    private String siteIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String appIdentity;
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;

    @Before
    public void setup() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        aPrioriIdentity = casTestUtil.getAprioriInternal().getIdentity();
        sourceCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = sourceCustomer.getIdentity();
        sourceUsers = new ArrayList<>();
        sourceUsers.add(casTestUtil.createUser(sourceCustomer).getResponseEntity());
        sourceUsers.add(casTestUtil.createUser(sourceCustomer).getResponseEntity());
        siteIdentity = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, aPrioriIdentity).getResponseEntity().getItems().get(0).getIdentity();
        deploymentIdentity = PropertiesContext.get("${env}.cds.apriori_production_deployment_identity");
        installationIdentity = PropertiesContext.get("${env}.cds.apriori_core_services_installation_identity");
        appIdentity = PropertiesContext.get("${env}.cds.apriori_cloud_home_identity");
    }

    @After
    public void teardown() {
        if (licensedAppIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
                licensedAppIdentityHolder.customerIdentity(),
                licensedAppIdentityHolder.siteIdentity(),
                licensedAppIdentityHolder.licenseIdentity());
        }
        if (installationIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
                installationIdentityHolder.customerIdentity(),
                installationIdentityHolder.deploymentIdentity(),
                installationIdentityHolder.installationIdentity());
        }
        sourceUsers.forEach((user) -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Description ("Bulk grants a source customer's users application access to the target customers applications.")
    @TestRail(testCaseId = {"13213"})
    public void bulkGrantAccessOutOfContext() {
        String user1Identity = sourceUsers.get(0).getIdentity();
        String user2Identity = sourceUsers.get(1).getIdentity();

        ResponseWrapper<String> grantAll = casTestUtil.grantAll(aPrioriIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, customerIdentity);
        soft.assertThat(grantAll.getStatusCode())
            .isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<AccessControls> userControlsGranted = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class,
            customerIdentity,
            user1Identity);
        soft.assertThat(userControlsGranted.getResponseEntity().getItems().get(0).getDeploymentIdentity())
            .overridingErrorMessage("Expected all users were granted access control to customer application")
            .isEqualTo(deploymentIdentity);

        ResponseWrapper<AccessControls> user2ControlsGranted = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class,
            customerIdentity,
            user2Identity);
        soft.assertThat(user2ControlsGranted.getResponseEntity().getItems().get(0).getDeploymentIdentity())
            .overridingErrorMessage("Expected all users were granted access control to customer application")
            .isEqualTo(deploymentIdentity);
        soft.assertAll();
    }

    @Test
    @Description("Bulk denies all access controls to target customers applications.")
    @TestRail(testCaseId = {"13214"})
    public void denyAllAccessOutOfContext() {
        String user1Identity = sourceUsers.get(0).getIdentity();
        String user2Identity = sourceUsers.get(1).getIdentity();

        ResponseWrapper<String> grantAll = casTestUtil.grantAll(aPrioriIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, customerIdentity);
        soft.assertThat(grantAll.getStatusCode())
            .isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<String> denyAll = casTestUtil.denyAll(aPrioriIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, customerIdentity);
        soft.assertThat(denyAll.getStatusCode())
            .isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<AccessControls> userDeniedControls = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class,
            customerIdentity,
            user1Identity);
        soft.assertThat(userDeniedControls.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected all users were denied access control to customer application")
            .isEqualTo(0L);

        ResponseWrapper<AccessControls> user2DeniedControls = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class,
            customerIdentity,
            user2Identity);
        soft.assertThat(user2DeniedControls.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected all users were denied access control to customer application")
            .isEqualTo(0L);
        soft.assertAll();
    }

    @Test
    @Description("Bulk grant/deny all access controls to customers applications")
    @TestRail(testCaseId = {"13211", "13212"})
    public void grantAllDenyAll() {
        String userIdentity = sourceUsers.get(0).getIdentity();
        String cloudRef = generateStringUtil.generateCloudReference();
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();
        String deploymentName = generateStringUtil.generateDeploymentName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, deploymentName, siteIdentity, "PRODUCTION");
        String deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> newApplication = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = newApplication.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        String installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        ResponseWrapper<String> grantAll = casTestUtil.grantAll(customerIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, null);
        soft.assertThat(grantAll.getStatusCode())
            .isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<AccessControls> userControlsGranted = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class,
            customerIdentity,
            userIdentity);
        soft.assertThat(userControlsGranted.getResponseEntity().getItems().get(0).getDeploymentIdentity())
            .overridingErrorMessage("Expected all users were granted access control to customer application")
            .isEqualTo(deploymentIdentity);

        ResponseWrapper<String> denyAll = casTestUtil.denyAll(customerIdentity, siteIdentity, deploymentIdentity, installationIdentity, appIdentity, null);
        soft.assertThat(denyAll.getStatusCode())
            .isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<AccessControls> userDeniedControls = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_CONTROLS, AccessControls.class,
            customerIdentity,
            userIdentity);
        soft.assertThat(userDeniedControls.getResponseEntity().getTotalItemCount())
            .overridingErrorMessage("Expected all users were denied access control to customer application")
            .isEqualTo(0L);
    }
}