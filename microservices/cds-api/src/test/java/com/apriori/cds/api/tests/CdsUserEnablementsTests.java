package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Enablements;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
        String email = "user-role_1@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service","Production");
        applicationsExpected.put("Cloud Search Service","Production");
        applicationsExpected.put("aPriori Cloud Home","Production");
        applicationsExpected.put("aP Workspace","Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_CONTRIBUTOR");

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29558})
    @Description("verify Analyst role")
    public void verifyAnalystRole() {
        String email = "user-role_2@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("Cloud Search Service", "Production");
        applicationsExpected.put("aPriori Cloud Home", "Production");
        applicationsExpected.put("aP Workspace", "Production");
        applicationsExpected.put("aP Analytics", "Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_ANALYST");

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29562})
    @Description("verify Designer role")
    public void verifyDesignerRole() {
        String email = "user-role_3@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("Cloud Search Service", "Production");
        applicationsExpected.put("aPriori Cloud Home", "Production");
        applicationsExpected.put("aP Workspace", "Production");
        applicationsExpected.put("aP Analytics", "Production");
        applicationsExpected.put("File Management Service", "Production");
        applicationsExpected.put("Anonymized Costing Service", "Production");
        applicationsExpected.put("aP Design", "Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_DESIGNER");

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29561})
    @Description("verify Expert role")
    public void verifyExpertRole() {
        String email = "user-role_4@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("Cloud Search Service", "Production");
        applicationsExpected.put("aPriori Cloud Home", "Production");
        applicationsExpected.put("aP Workspace", "Production");
        applicationsExpected.put("aP Analytics", "Production");
        applicationsExpected.put("File Management Service", "Production");
        applicationsExpected.put("Anonymized Costing Service", "Production");
        applicationsExpected.put("aP Design", "Production");
        applicationsExpected.put("aP Pro", "Production");
        applicationsExpected.put("Electronics Data Collection", "Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_EXPERT");

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29560})
    @Description("verify Developer role")
    public void verifyDeveloperRole() {
        String email = "user-role_5@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("Cloud Search Service", "Production");
        applicationsExpected.put("aPriori Cloud Home", "Production");
        applicationsExpected.put("aP Workspace", "Production");
        applicationsExpected.put("aP Analytics", "Production");
        applicationsExpected.put("File Management Service", "Production");
        applicationsExpected.put("Anonymized Costing Service", "Production");
        applicationsExpected.put("aP Design", "Production");
        applicationsExpected.put("aP Pro", "Production");
        applicationsExpected.put("Electronics Data Collection", "Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_DEVELOPER");

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29559})
    @Description("verify Analyst with added enablement role")
    public void verifyAnalystWithEnablementRole() {
        String email = "user-role_6@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("Cloud Search Service", "Production");
        applicationsExpected.put("aPriori Cloud Home", "Production");
        applicationsExpected.put("aP Workspace", "Production");
        applicationsExpected.put("aP Analytics", "Production");
        applicationsExpected.put("aP Connect", "Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_ANALYST");
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29564})
    @Description("verify Designer with added enablements role")
    public void verifyDesignerWithEnablementRole() {
        String email = "user-role_7@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("Cloud Search Service", "Production");
        applicationsExpected.put("aPriori Cloud Home", "Production");
        applicationsExpected.put("aP Workspace", "Production");
        applicationsExpected.put("aP Analytics", "Production");
        applicationsExpected.put("File Management Service", "Production");
        applicationsExpected.put("Anonymized Costing Service", "Production");
        applicationsExpected.put("aP Design", "Production");
        applicationsExpected.put("aP Connect", "Production");
        applicationsExpected.put("Customer Admin", "Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_DESIGNER");
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29563})
    @Description("verify Expert with added enablements role")
    public void verifyExpertWithEnablementRole() {
        String email = "user-role_8@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("Cloud Search Service", "Production");
        applicationsExpected.put("aPriori Cloud Home", "Production");
        applicationsExpected.put("aP Workspace", "Production");
        applicationsExpected.put("aP Analytics", "Production");
        applicationsExpected.put("File Management Service", "Production");
        applicationsExpected.put("Anonymized Costing Service", "Production");
        applicationsExpected.put("aP Design", "Production");
        applicationsExpected.put("aP Pro", "Production");
        applicationsExpected.put("Electronics Data Collection", "Production");
        applicationsExpected.put("Customer Admin", "Production");
        applicationsExpected.put("aP Connect", "Production");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_EXPERT");
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29563})
    @Description("verify Expert with added enablements role and sandbox deployment")
    public void verifyExpertWithEnablementRoleAndSandbox() {
        String email = "user-role_9@apriori.com";
        User user = cdsTestUtil.getUserByEmail(email)
            .getResponseEntity().getItems().get(0);

        MultiValuedMap<String,Object> applicationsExpected = new ArrayListValuedHashMap<>();
        applicationsExpected.put("aPriori Cloud Home","Production");
        applicationsExpected.put("aP Workspace","Production");
        applicationsExpected.put("aP Workspace","Sandbox");
        applicationsExpected.put("aP Analytics","Production");
        applicationsExpected.put("aP Analytics","Sandbox");
        applicationsExpected.put("aP Design","Production");
        applicationsExpected.put("aP Design","Sandbox");
        applicationsExpected.put("aP Pro","Production");
        applicationsExpected.put("aP Pro","Sandbox");
        applicationsExpected.put("aP Admin","Production");
        applicationsExpected.put("aP Admin","Sandbox");
        applicationsExpected.put("aP Connect","Production");
        applicationsExpected.put("aP Connect","Sandbox");
        applicationsExpected.put("Electronics Data Collection","Production");
        applicationsExpected.put("Customer Admin","Production");
        applicationsExpected.put("Current User Service", "Production");
        applicationsExpected.put("File Management Service","Production");
        applicationsExpected.put("File Management Service","Sandbox");
        applicationsExpected.put("Anonymized Costing Service","Production");
        applicationsExpected.put("Anonymized Costing Service","Sandbox");
        applicationsExpected.put("Cloud Search Service","Production");
        applicationsExpected.put("Cloud Search Service","Sandbox");

        Enablements enablements = cdsTestUtil.getEnablement(user).getResponseEntity();

        soft.assertThat(enablements.getCustomerAssignedRole()).isEqualTo("APRIORI_EXPERT");
        soft.assertThat(enablements.getConnectAdminEnabled()).isTrue();
        soft.assertThat(enablements.getUserAdminEnabled()).isTrue();

        MultiValuedMap<String,Object> applicationsResponse =  cdsTestUtil.getUserApplications(user);

        soft.assertThat(applicationsResponse).isEqualTo(applicationsExpected);
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