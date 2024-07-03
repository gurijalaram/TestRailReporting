package com.apriori.ach.api.tests;

import static com.apriori.shared.util.enums.CustomerEnum.AP_INT;
import static com.apriori.shared.util.enums.CustomerEnum.WIDGETS;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.AchErrorResponse;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.enums.TokenEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.request.TokenRequest;
import com.apriori.shared.util.models.response.Claims;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Enablements;
import com.apriori.shared.util.models.response.Token;
import com.apriori.shared.util.models.response.TokenInformation;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ExtendWith(TestRulesAPI.class)
public class AchUsersTests extends AchTestUtil {
    private static final String USER_ADMIN = "testUser1";
    private static final String NOT_ADMIN_USER = "testUser11";
    private static Customer serviceCustomer;
    private static String customerIdentity;
    private RequestEntityUtil requestEntityUtilNoAdmin;
    private CdsTestUtil cdsTestUtil;
    private SoftAssertions soft = new SoftAssertions();
    private String domain;
    private String userIdentity;

    @BeforeEach
    public void setup() {
        serviceCustomer = PropertiesContext.get("customer").equalsIgnoreCase(AP_INT.getCustomer())
            ? SharedCustomerUtil.getCustomerData(WIDGETS.getCustomer()) : SharedCustomerUtil.getCustomerData();
        customerIdentity = serviceCustomer.getIdentity();

        requestEntityUtil = RequestEntityUtilBuilder
            .useCustomUser(new UserCredentials(USER_ADMIN, null))
            .useCustomTokenInRequests(getWidgetsUserToken(USER_ADMIN));

        requestEntityUtilNoAdmin = RequestEntityUtilBuilder
            .useCustomUser(new UserCredentials(NOT_ADMIN_USER, null))
            .useCustomTokenInRequests(getWidgetsUserToken(NOT_ADMIN_USER));

        cdsTestUtil = new CdsTestUtil(requestEntityUtil);

        Customer customer = getCommonRequest(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class, HttpStatus.SC_OK, customerIdentity).getResponseEntity();
        String pattern = customer.getEmailRegexPatterns().stream().findFirst().orElseThrow();
        domain = pattern.replace("\\S+@", "").replace(".com", "");
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
    }

    @Test
    @TestRail(id = {29177, 29178})
    @Description("User Admin can create a user, user can be created with unique email")
    public void createUserByAdmin() {

        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<User> newUser = createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        userIdentity = newUser.getResponseEntity().getIdentity();

        soft.assertThat(newUser.getResponseEntity().getUsername()).isEqualTo(userName);

        ResponseWrapper<AchErrorResponse> newUserSameEmail = createNewUser(AchErrorResponse.class, customerIdentity, userName, domain, HttpStatus.SC_CONFLICT);

        soft.assertThat(newUserSameEmail.getResponseEntity().getMessage())
            .isEqualTo(String.format("Can't create a user with email '%s' as the email already exists.", userName + "@" + domain + ".com"));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29179})
    @Description("Bad request is returned when create user without required fields")
    public void createUserNoRequiredFields() {
        ResponseWrapper<AchErrorResponse> newUserSameEmail = createNewUser(AchErrorResponse.class, customerIdentity, null, null, HttpStatus.SC_BAD_REQUEST);

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

        ResponseWrapper<User> newUser = createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        User userResponse = newUser.getResponseEntity();
        userIdentity = userResponse.getIdentity();

        ResponseWrapper<User> updateUser = patchUser(User.class, userResponse, updatedJobTitle, HttpStatus.SC_OK);

        soft.assertThat(updateUser.getResponseEntity().getUserProfile().getJobTitle())
            .isEqualTo(updatedJobTitle);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29183})
    @Description("User admin can delete a user")
    public void deleteUserByAdmin() {
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<User> newUser = createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        String userIdentity = newUser.getResponseEntity().getIdentity();

        RequestEntity deleteRequest = requestEntityUtil.init(ACHAPIEnum.USER_BY_ID, null)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT)
            .inlineVariables(customerIdentity, userIdentity);
        HTTPRequest.build(deleteRequest).delete();

        ResponseWrapper<User> getDeletedUser = getCommonRequest(CDSAPIEnum.USER_BY_ID, User.class, HttpStatus.SC_OK, userIdentity);

        soft.assertThat(getDeletedUser.getResponseEntity().getActive())
            .isFalse();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29186})
    @Description("aPrioriCIGenerateUser cannot be deleted by API")
    public void tryDeleteAprioriCIGenerateUser() {
        String apGenerateIdentity = getUser("aPrioriCIGenerateUser", customerIdentity).getIdentity();

        RequestEntity deleteRequest = requestEntityUtil.init(ACHAPIEnum.USER_BY_ID, AchErrorResponse.class)
            .expectedResponseCode(HttpStatus.SC_CONFLICT)
            .inlineVariables(customerIdentity, apGenerateIdentity);

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();

        soft.assertThat(errorResponse.getResponseEntity().getMessage())
            .isEqualTo("This user can not be deleted through self service");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29185})
    @Description("Service account cannot be deleted by API")
    public void tryDeleteServiceAccount() {
        String serviceAccountIdentity = getUser("widgets.service-account.1", customerIdentity).getIdentity();

        RequestEntity deleteRequest = requestEntityUtil.init(ACHAPIEnum.USER_BY_ID, AchErrorResponse.class)
            .expectedResponseCode(HttpStatus.SC_CONFLICT)
            .inlineVariables(customerIdentity, serviceAccountIdentity);

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();

        soft.assertThat(errorResponse.getResponseEntity().getMessage())
            .isEqualTo("This user can not be deleted through self service");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29180})
    @Description("Error when non admin user trying to create user")
    public void notAdminCreateUser() {
        String userName = new GenerateStringUtil().generateUserName();
        ResponseWrapper<AchErrorResponse> newUser = createNewUser(AchErrorResponse.class, customerIdentity, userName, domain, HttpStatus.SC_FORBIDDEN, requestEntityUtilNoAdmin);

        soft.assertThat(newUser.getResponseEntity().getMessage())
            .isEqualTo("Operation not allowed.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30930})
    @Description("unable to create AP_STAFF_USER for widgets customer")
    public void unableToCreateApStaffUserForWidgets() {
        String userName = new GenerateStringUtil().generateUserName();
        ResponseWrapper<AchErrorResponse> newUser = createNewUserApStaff(AchErrorResponse.class, customerIdentity, userName, domain, HttpStatus.SC_CONFLICT, requestEntityUtil);

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

        ResponseWrapper<User> newUser = createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        User userResponse = newUser.getResponseEntity();
        userIdentity = userResponse.getIdentity();

        ResponseWrapper<AchErrorResponse> updateUser = patchUser(AchErrorResponse.class, userResponse, updatedJobTitle, HttpStatus.SC_FORBIDDEN, requestEntityUtilNoAdmin);

        soft.assertThat(updateUser.getResponseEntity().getMessage()).isEqualTo("Operation not allowed.");

        RequestEntity deleteRequest = requestEntityUtilNoAdmin.init(ACHAPIEnum.USER_BY_ID, AchErrorResponse.class)
            .expectedResponseCode(HttpStatus.SC_FORBIDDEN)
            .inlineVariables(customerIdentity, userIdentity);

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();

        soft.assertThat(errorResponse.getResponseEntity().getMessage())
            .isEqualTo("Operation not allowed.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29343})
    @Description("User admin cannot update own enablements")
    public void updateOwnEnablements() {
        User current = getUser(USER_ADMIN, customerIdentity);
        RequestEntity updateEnablements = requestEntityUtil.init(ACHAPIEnum.USER_BY_ID, AchErrorResponse.class)
            .expectedResponseCode(HttpStatus.SC_FORBIDDEN)
            .inlineVariables(customerIdentity, current.getIdentity())
            .body("user",
                User.builder()
                    .enablements(Enablements.builder()
                        .customerAssignedRole("APRIORI_DEVELOPER")
                        .userAdminEnabled(false).build())
                    .build());

        ResponseWrapper<AchErrorResponse> error = HTTPRequest.build(updateEnablements).patch();
        soft.assertThat(error.getResponseEntity().getMessage())
            .isEqualTo("You are not allowed to update your own enablements");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29344})
    @Description("User admin cannot delete themself")
    public void tryDeleteThemself() {
        User current = getUser(USER_ADMIN, customerIdentity);
        RequestEntity deleteRequest = requestEntityUtil.init(ACHAPIEnum.USER_BY_ID, AchErrorResponse.class)
            .expectedResponseCode(HttpStatus.SC_FORBIDDEN)
            .inlineVariables(customerIdentity, current.getIdentity());

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();
        soft.assertThat(errorResponse.getResponseEntity().getMessage())
            .isEqualTo("You are not allowed to delete yourself. Please ask another user administrator to do this for you.");
        soft.assertAll();
    }

    private static String generateTokenSubject() {
        final String customerSiteId = SharedCustomerUtil.getCustomerSiteIdByCustomer(serviceCustomer);

        if (StringUtils.isBlank(customerSiteId)) {
            log.error("Customer site id is empty. Customer: {}", serviceCustomer.getCloudReference());
        }

        return customerSiteId.substring(customerSiteId.length() - 4);
    }

    private String getWidgetsUserToken(String name) {
        RequestEntity requestEntity = requestEntityUtil.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(PropertiesContext.get("ats.token_issuer"))
                    .subject(generateTokenSubject())
                    .claims(Claims.builder()
                        .name(name)
                        .email(String.format("%s@%s.aprioritest.com", name, serviceCustomer.getName()))
                        .build())
                    .build())
                .build())
            .expectedResponseCode(HttpStatus.SC_CREATED);
        ResponseWrapper<Token> tokenResponse = HTTPRequest.build(requestEntity).post();

        return tokenResponse.getResponseEntity().getToken();
    }

    private User getUser(String name, String customerIdentity) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("username[EQ]", name);

        User user = findFirst(ACHAPIEnum.CUSTOMER_USERS, Users.class, filters, Collections.emptyMap(), customerIdentity);

        if (user == null) {
            throw new IllegalStateException(String.format("User, %s, is missing.", name));
        }

        return user;
    }
}