package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.AppAccessControlsEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.enums.CDSRolesEnum;
import com.apriori.cds.api.enums.DeploymentEnum;
import com.apriori.cds.api.models.Applications;
import com.apriori.cds.api.models.ApplicationsList;
import com.apriori.cds.api.utils.ApplicationUtil;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CdsUserUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.CustomerUtil;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Enablements;
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

import java.util.Arrays;


@ExtendWith(TestRulesAPI.class)
public class CdsUserEnablementsTests extends CdsTestUtil {
    private final String customerAssignedRole = "APRIORI_CONTRIBUTOR";
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure;
    private CdsTestUtil cdsTestUtil;
    private ApplicationUtil applicationUtil;
    private CustomerUtil customerUtil;
    private CdsUserUtil cdsUserUtil;
    private String customerIdentity;
    private String userIdentity;

    @BeforeEach
    public void setDetails() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        applicationUtil = new ApplicationUtil(requestEntityUtil);
        customerInfrastructure = new CustomerInfrastructure(requestEntityUtil);
        customerUtil = new CustomerUtil(requestEntityUtil);
        cdsUserUtil = new CdsUserUtil(requestEntityUtil);
    }

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
        User user = cdsUserUtil.getUserByEmail("user-role_1@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.CONTRIBUTOR.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.CONTRIBUTOR.getRole());

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29558})
    @Description("verify Analyst role")
    public void verifyAnalystRole() {
        User user = cdsUserUtil.getUserByEmail("user-role_2@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.ANALYST.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.ANALYST.getRole());

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29562})
    @Description("verify Designer role")
    public void verifyDesignerRole() {
        User user = cdsUserUtil.getUserByEmail("user-role_3@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.DESIGNER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.DESIGNER.getRole());

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29561})
    @Description("verify Expert role")
    public void verifyExpertRole() {
        User user = cdsUserUtil.getUserByEmail("user-role_4@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.EXPERT.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.EXPERT.getRole());

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29560})
    @Description("verify Developer role")
    public void verifyDeveloperRole() {
        User user = cdsUserUtil.getUserByEmail("user-role_5@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.DEVELOPER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.DEVELOPER.getRole());

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29559})
    @Description("verify Analyst with added enablement role")
    public void verifyAnalystWithEnablementRole() {
        User user = cdsUserUtil.getUserByEmail("user-role_6@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.ANALYST_CONNECT.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.ANALYST.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29564})
    @Description("verify Designer with added enablements role")
    public void verifyDesignerWithEnablementRole() {
        User user = cdsUserUtil.getUserByEmail("user-role_7@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.DESIGNER_CONNECT_USER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.DESIGNER.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29563})
    @Description("verify Expert with added enablements role")
    public void verifyExpertWithEnablementRole() {
        User user = cdsUserUtil.getUserByEmail("user-role_8@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                .applications(CDSRolesEnum.EXPERT_CONNECT_USER.getApps())
                .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.EXPERT.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION)))
            .build();
        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29693})
    @Description("verify Expert with added enablements role and sandbox deployment")
    public void verifyExpertWithEnablementRoleAndSandbox() {

        User user = cdsUserUtil.getUserByEmail("user-role_9@apriori.com")
            .getResponseEntity().getItems().get(0);

        ApplicationsList applicationsList = ApplicationsList.builder()
            .applicationsList(Arrays.asList(Applications.builder()
                    .applications(CDSRolesEnum.EXPERT_CONNECT_USER_ADMIN.getApps())
                    .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                    .build(),
                Applications.builder()
                    .applications(CDSRolesEnum.EXPERT_CONNECT_USER_SANDBOX.getApps())
                    .deployment(DeploymentEnum.PRODUCTION.getDeployment())
                    .build()))
            .build();

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();
        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo(CDSRolesEnum.EXPERT.getRole());
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        ApplicationsList applicationsResponse = ApplicationsList.builder()
            .applicationsList(Arrays.asList(
                applicationUtil.getUserApplications(user, DeploymentEnum.PRODUCTION),
                applicationUtil.getUserApplications(user, DeploymentEnum.SANDBOX)
            ))
            .build();

        soft.assertThatList(applicationsResponse.getApplicationsList().get(0).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(0).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(0).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(0).getDeployment()).isEqualTo(DeploymentEnum.PRODUCTION.getDeployment());

        soft.assertThatList(applicationsResponse.getApplicationsList().get(1).getApplications())
            .containsExactlyInAnyOrder((applicationsList.getApplicationsList().get(1).getApplications())
                .toArray(new AppAccessControlsEnum[applicationsList.getApplicationsList().get(1).getApplications().size()]));
        soft.assertThat(applicationsResponse.getApplicationsList().get(1).getDeployment()).isEqualTo(DeploymentEnum.SANDBOX.getDeployment());
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = customerUtil.addCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);

        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsUserUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}