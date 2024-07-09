package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.AchErrorResponse;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ExtendWith(TestRulesAPI.class)
public class AchUsersTests {
    private String customerIdentity;
    private RequestEntityUtil requestEntityUtil;
    private AchTestUtil achTestUtil;
    private SoftAssertions soft = new SoftAssertions();
    private String domain;
    private String userIdentity;
    private Customer customer;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser().useTokenInRequests();
        achTestUtil = new AchTestUtil(requestEntityUtil);
        customer = SharedCustomerUtil.getCustomerData();
        customerIdentity = customer.getIdentity();
        domain = achTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class, HttpStatus.SC_OK, customerIdentity).getResponseEntity()
            .getEmailRegexPatterns().stream().findFirst().orElseThrow().replace("\\S+@", "").replace(".com", "");
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            achTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
    }

    @Test
    @TestRail(id = {29177, 29178})
    @Description("User Admin can create a user, user can be created with unique email")
    public void createUserByAdmin() {
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<User> newUser = achTestUtil.createNewUser("CreateUserData.json", customerIdentity, userName, domain, HttpStatus.SC_CREATED, User.class);
        userIdentity = newUser.getResponseEntity().getIdentity();

        soft.assertThat(newUser.getResponseEntity().getUsername()).isEqualTo(userName);

        ResponseWrapper<AchErrorResponse> newUserSameEmail = achTestUtil.createNewUser("CreateUserData.json", customerIdentity,
            userName, domain, HttpStatus.SC_CONFLICT, AchErrorResponse.class);

        soft.assertThat(newUserSameEmail.getResponseEntity().getMessage())
            .isEqualTo(String.format("Can't create a user with email '%s' as the email already exists.", userName + "@" + domain + ".com"));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29179})
    @Description("Bad request is returned when create user without required fields")
    public void createUserNoRequiredFields() {
        ResponseWrapper<AchErrorResponse> newUserSameEmail = achTestUtil.createNewUser("CreateUserData.json", customerIdentity,
            null, null, HttpStatus.SC_BAD_REQUEST, AchErrorResponse.class);

        soft.assertThat(newUserSameEmail.getResponseEntity().getMessage())
            .contains("username' should not be null.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29181})
    @Description("User admin can Edit the user")
    public void editUserByAdmin() {
        String userName = new GenerateStringUtil().generateUserName();
        String updatedJobTitle = "QA";

        ResponseWrapper<User> newUser = achTestUtil.createNewUser("CreateUserData.json", customerIdentity, userName, domain, HttpStatus.SC_CREATED, User.class);
        User userResponse = newUser.getResponseEntity();
        userIdentity = userResponse.getIdentity();

        ResponseWrapper<User> updateUser = achTestUtil.patchUser(User.class, userResponse, updatedJobTitle, HttpStatus.SC_OK);

        soft.assertThat(updateUser.getResponseEntity().getUserProfile().getJobTitle())
            .isEqualTo(updatedJobTitle);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29183})
    @Description("User admin can delete a user")
    public void deleteUserByAdmin() {
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<User> newUser = achTestUtil.createNewUser("CreateUserData.json", customerIdentity, userName, domain, HttpStatus.SC_CREATED, User.class);
        String userIdentity = newUser.getResponseEntity().getIdentity();

        achTestUtil.delete(ACHAPIEnum.USER_BY_ID, customerIdentity, userIdentity);
        ResponseWrapper<User> deletedUser = achTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_ID, User.class, HttpStatus.SC_OK, userIdentity);

        soft.assertThat(deletedUser.getResponseEntity().getActive())
            .isFalse();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29186})
    @Description("aPrioriCIGenerateUser cannot be deleted by API")
    public void tryDeleteAprioriCIGenerateUser() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("username[EQ]", "aPrioriCIGenerateUser");

        String apGenerateIdentity = achTestUtil.findFirst(ACHAPIEnum.CUSTOMER_USERS, Users.class, filters, Collections.emptyMap(), customerIdentity).getIdentity();

        AchErrorResponse errorResponse = achTestUtil.deleteUser(HttpStatus.SC_CONFLICT, customerIdentity, apGenerateIdentity);

        soft.assertThat(errorResponse.getMessage()).isEqualTo("This user can not be deleted through self service");
        soft.assertAll();
    }


    @Test
    @DisabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
    @TestRail(id = {29185})
    @Description("Service account cannot be deleted by API")
    public void tryDeleteServiceAccount() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("username[EQ]", "widgets.service-account.1");

        String apGenerateIdentity = achTestUtil.findFirst(ACHAPIEnum.CUSTOMER_USERS, Users.class, filters, Collections.emptyMap(), customerIdentity).getIdentity();

        AchErrorResponse errorResponse = achTestUtil.deleteUser(HttpStatus.SC_CONFLICT, customerIdentity, apGenerateIdentity);

        soft.assertThat(errorResponse.getMessage()).isEqualTo("This user can not be deleted through self service");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29180})
    @Description("Error when non admin user trying to create user")
    public void notAdminCreateUser() {
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<User> newNonAdminUser = achTestUtil.createNewUser("CreateUserData.json", customerIdentity, userName, domain, HttpStatus.SC_CREATED, User.class);
        User userResponse = newNonAdminUser.getResponseEntity();
        userIdentity = userResponse.getIdentity();

        requestEntityUtil = TestHelper.initCustomUser(new UserCredentials().setEmail(userResponse.getEmail())).useTokenInRequests();

        ResponseWrapper<AchErrorResponse> newUser = achTestUtil.createNewUser("CreateUserData.json", customerIdentity,
            userName, domain, HttpStatus.SC_FORBIDDEN, AchErrorResponse.class);

        soft.assertThat(newUser.getResponseEntity().getMessage()).isEqualTo("Operation not allowed.");
        soft.assertAll();
    }

    @Test
    @DisabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
    @TestRail(id = {30930})
    @Description("unable to create AP_STAFF_USER for widgets customer")
    public void unableToCreateApStaffUserForWidgets() {
        String userName = new GenerateStringUtil().generateUserName();
        ResponseWrapper<AchErrorResponse> newUser = achTestUtil.createNewUser("CreateUserDataApStaff.json", customerIdentity,
            userName, domain, HttpStatus.SC_CONFLICT, AchErrorResponse.class);

        soft.assertThat(newUser.getResponseEntity().getMessage())
            .isEqualTo("Can't create an AP_STAFF_USER user type for the given customer.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29182, 29184})
    @Description("Error when non admin user trying to edit and delete user")
    public void editDeleteNotAdmin() {
        String userName = new GenerateStringUtil().generateUserName();
        String updatedJobTitle = "QA";

        ResponseWrapper<User> newUser = achTestUtil.createNewUser("CreateUserData.json", customerIdentity, userName, domain, HttpStatus.SC_CREATED, User.class);
        User userResponse = newUser.getResponseEntity();
        userIdentity = userResponse.getIdentity();

        requestEntityUtil = TestHelper.initCustomUser(new UserCredentials().setEmail(userResponse.getEmail())).useTokenInRequests();
        ResponseWrapper<AchErrorResponse> updateUser = achTestUtil.patchUser(AchErrorResponse.class, userResponse, updatedJobTitle, HttpStatus.SC_FORBIDDEN, requestEntityUtil);

        soft.assertThat(updateUser.getResponseEntity().getMessage()).isEqualTo("Operation not allowed.");

        // TODO: 09/07/2024 cn - nataliia, should this be a status code 204 with User already deactivated?
        AchErrorResponse errorResponse = achTestUtil.deleteUser(HttpStatus.SC_FORBIDDEN, customerIdentity, userIdentity);

        soft.assertThat(errorResponse.getMessage()).isEqualTo("Operation not allowed.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29343})
    @Description("User admin cannot update own enablements")
    public void updateOwnEnablements() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("username[EQ]", requestEntityUtil.getEmbeddedUser().getUsername());

        String userIdentity = achTestUtil.findFirst(ACHAPIEnum.CUSTOMER_USERS, Users.class, filters, Collections.emptyMap(), customerIdentity).getIdentity();
        AchErrorResponse errorResponse = achTestUtil.patchEnablements(customerIdentity, userIdentity);

        soft.assertThat(errorResponse.getMessage()).isEqualTo("You are not allowed to update your own enablements");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29344})
    @Description("User admin cannot delete themself")
    public void tryDeleteThemself() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("username[EQ]", requestEntityUtil.getEmbeddedUser().getUsername());

        String userIdentity = achTestUtil.findFirst(ACHAPIEnum.CUSTOMER_USERS, Users.class, filters, Collections.emptyMap(), customerIdentity).getIdentity();
        AchErrorResponse errorResponse = achTestUtil.deleteUser(HttpStatus.SC_FORBIDDEN, customerIdentity, userIdentity);

        soft.assertThat(errorResponse.getMessage())
            .isEqualTo("You are not allowed to delete yourself. Please ask another user administrator to do this for you.");
        soft.assertAll();
    }
}