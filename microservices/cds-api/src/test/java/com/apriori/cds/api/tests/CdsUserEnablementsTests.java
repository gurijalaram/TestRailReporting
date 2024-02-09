package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.AppAccessControlsEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.enums.CDSRolesEnum;
import com.apriori.cds.api.enums.DeploymentEnum;
import com.apriori.cds.api.models.Apps;
import com.apriori.cds.api.models.AppsItems;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Enablements;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@ExtendWith(TestRulesAPI.class)
public class CdsUserEnablementsTests extends CdsTestUtil {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String userIdentity;
    private final String customerAssignedRole = "APRIORI_CONTRIBUTOR";

    @AfterEach
    public void deletePreferences() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {25977, 28007})
    @Description("Create and update user enablements")
    public void createUpdateUserEnablements() {
        setCustomerData();
        ResponseWrapper<Enablements> createEnablements = cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, false);

        String enablementsIdentity = createEnablements.getResponseEntity().getIdentity();
        soft.assertThat(createEnablements.getResponseEntity().getHighMemEnabled()).isFalse();

        ResponseWrapper<Enablements> updateEnablements = cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, true, false, false);

        soft.assertThat(updateEnablements.getResponseEntity().getIdentity()).isEqualTo(enablementsIdentity);
        soft.assertThat(updateEnablements.getResponseEntity().getHighMemEnabled()).isTrue();

        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUser.getEnablements().getCustomerAssignedRole()).isEqualTo(customerAssignedRole);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {25978})
    @Description("Returns user enablements")
    public void getUserEnablements() {
        setCustomerData();
        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, false);

        ResponseWrapper<Enablements> getEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);

        soft.assertThat(getEnablements.getResponseEntity().getIdentity()).isNotEmpty();
        soft.assertThat(getEnablements.getResponseEntity().getHighMemEnabled()).isFalse();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29550})
    @Description("verify Contributor role")
    public void verifyContributorRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_1@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.CONTRIBUTOR.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.CONTRIBUTOR.getRole());

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29558})
    @Description("verify Analyst role")
    public void verifyAnalystRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_2@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.ANALYST.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.ANALYST.getRole());

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29562})
    @Description("verify Designer role")
    public void verifyDesignerRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_3@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.DESIGNER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.DESIGNER.getRole());

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29561})
    @Description("verify Expert role")
    public void verifyExpertRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_4@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.EXPERT.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.EXPERT.getRole());

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29560})
    @Description("verify Developer role")
    public void verifyDeveloperRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_5@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.DEVELOPER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.DEVELOPER.getRole());

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29559})
    @Description("verify Analyst with added enablement role")
    public void verifyAnalystWithEnablementRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_6@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.ANALYST_CONNECT.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.ANALYST.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29564})
    @Description("verify Designer with added enablements role")
    public void verifyDesignerWithEnablementRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_7@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.DESIGNER_CONNECT_USER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.DESIGNER.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29563})
    @Description("verify Expert with added enablements role")
    public void verifyExpertWithEnablementRole() {
        User user = cdsTestUtil.getUserByEmail("user-role_8@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                .applications(CDSRolesEnum.EXPERT_CONNECT_USER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.EXPERT.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29693})
    @Description("verify Expert with added enablements role and sandbox deployment")
    public void verifyExpertWithEnablementRoleAndSandbox() {

        User user = cdsTestUtil.getUserByEmail("user-role_9@apriori.com")
            .getResponseEntity().getItems().get(0);

        AppsItems appsItems = AppsItems.builder()
            .appsList(Arrays.asList(Apps.builder()
                    .applications(CDSRolesEnum.EXPERT_CONNECT_USER_ADMIN.getApps())
                    .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                    .build(),
                Apps.builder()
                    .applications(CDSRolesEnum.EXPERT_CONNECT_USER_SANDBOX.getApps())
                    .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                    .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.EXPERT.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        AppsItems applicationsResponse = AppsItems.builder()
            .appsList(Arrays.asList(
                cdsTestUtil.getUserApplications(user, DeploymentEnum.PRODUCTION),
                cdsTestUtil.getUserApplications(user, DeploymentEnum.SANDBOX)
            ))
            .build();

        soft.assertThatList(applicationsResponse.getAppsList().get(0).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());

        soft.assertThatList(applicationsResponse.getAppsList().get(1).getApplications())
            .containsExactlyInAnyOrder((appsItems.getAppsList().get(1).getApplications())
                .toArray(new AppAccessControlsEnum[appsItems.getAppsList().get(1).getApplications().size()]));
        soft.assertThat(applicationsResponse.getAppsList().get(1).getDeployment()).isEqualTo(DeploymentEnum.SANDBOX.getDeployment());
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);

        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}