package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.AchErrorResponse;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.CdsErrorResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.enums.TokenEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.request.TokenRequest;
import com.apriori.shared.util.models.response.Claims;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Token;
import com.apriori.shared.util.models.response.TokenInformation;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(TestRulesAPI.class)
public class AchUsersTests {
    private static final String USER_ADMIN = "testUser1";
    private static final String NOT_ADMIN_USER = "testUser11";
    private AchTestUtil achTestUtil = new AchTestUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private String domain;
    private String userIdentity;

    @BeforeEach
    public void setup() {
        RequestEntityUtil_Old.useTokenForRequests(getWidgetsUserToken(USER_ADMIN));
        customerIdentity = achTestUtil.getCustomer("Widgets").getIdentity();
        Customer widgets = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class, HttpStatus.SC_OK, customerIdentity).getResponseEntity();
        String pattern = widgets.getEmailRegexPatterns().stream().findFirst().orElseThrow();
        domain = pattern.replace("\\S+@", "").replace(".com", "");
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
    }

    @Test
    @TestRail(id = {29177, 29178})
    @Description("User Admin can create a user, user can be created with unique email")
    public void createUserByAdmin() {
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<User> newUser = achTestUtil.createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        userIdentity = newUser.getResponseEntity().getIdentity();

        soft.assertThat(newUser.getResponseEntity().getUsername()).isEqualTo(userName);

        ResponseWrapper<AchErrorResponse> newUserSameEmail = achTestUtil.createNewUser(AchErrorResponse.class, customerIdentity, userName, domain, HttpStatus.SC_CONFLICT);

        soft.assertThat(newUserSameEmail.getResponseEntity().getMessage()).isEqualTo(String.format("Can't create a user with email '%s' as the email already exists.", userName + "@" + domain + ".com"));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29179})
    @Description("Bad request is returned when create user without required fields")
    public void createUserNoRequiredFields() {
        ResponseWrapper<AchErrorResponse> newUserSameEmail = achTestUtil.createNewUser(AchErrorResponse.class, customerIdentity, null, null, HttpStatus.SC_BAD_REQUEST);

        soft.assertThat(newUserSameEmail.getResponseEntity().getMessage()).contains("username' should not be null.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29181})
    @Description("User admin can Edit the user")
    public void editUserByAdmin() {
        String userName = new GenerateStringUtil().generateUserName();
        String updatedJobTitle = "QA";

        ResponseWrapper<User> newUser = achTestUtil.createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        User userResponse = newUser.getResponseEntity();
        userIdentity = userResponse.getIdentity();

        ResponseWrapper<User> updateUser = achTestUtil.patchUser(User.class, userResponse, updatedJobTitle, HttpStatus.SC_OK);

        soft.assertThat(updateUser.getResponseEntity().getUserProfile().getJobTitle()).isEqualTo(updatedJobTitle);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29183})
    @Description("User admin can delete a user")
    public void deleteUserByAdmin() {
        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<User> newUser = achTestUtil.createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        String userIdentity = newUser.getResponseEntity().getIdentity();

        achTestUtil.delete(ACHAPIEnum.USER_BY_ID, customerIdentity, userIdentity);

        ResponseWrapper<CdsErrorResponse> getDeletedUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, CdsErrorResponse.class, HttpStatus.SC_NOT_FOUND, customerIdentity, userIdentity);

        soft.assertThat(getDeletedUser.getResponseEntity().getMessage()).isEqualTo(String.format("Unable to get user with identity '%s' for customer with identity '%s'.", userIdentity, customerIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29186})
    @Description("aPrioriCIGenerateUser cannot be deleted by API")
    public void tryDeleteAprioriCIGenerateUser() {
        String apGenerateIdentity = getUser("aPrioriCIGenerateUser", customerIdentity).getIdentity();

        RequestEntity deleteRequest = new RequestEntity().endpoint(ACHAPIEnum.USER_BY_ID)
            .returnType(AchErrorResponse.class)
            .token(getWidgetsUserToken(USER_ADMIN))
            .expectedResponseCode(HttpStatus.SC_CONFLICT)
            .inlineVariables(customerIdentity, apGenerateIdentity);

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();

        soft.assertThat(errorResponse.getResponseEntity().getMessage()).isEqualTo("This user can not be deleted through self service");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29186})
    @Description("Service account cannot be deleted by API")
    public void tryDeleteServiceAccount() {
        String serviceAccountIdentity = getUser("widgets.service-account.1", customerIdentity).getIdentity();

        RequestEntity deleteRequest = new RequestEntity().endpoint(ACHAPIEnum.USER_BY_ID)
            .returnType(AchErrorResponse.class)
            .token(getWidgetsUserToken(USER_ADMIN))
            .expectedResponseCode(HttpStatus.SC_CONFLICT)
            .inlineVariables(customerIdentity, serviceAccountIdentity);

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();

        soft.assertThat(errorResponse.getResponseEntity().getMessage()).isEqualTo("This user can not be deleted through self service");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29180})
    @Description("Error when non admin user trying to create user")
    public void notAdminCreateUser() {
        RequestEntityUtil.useTokenForRequests(getWidgetsUserToken(NOT_ADMIN_USER));

        String userName = new GenerateStringUtil().generateUserName();

        ResponseWrapper<AchErrorResponse> newUser = achTestUtil.createNewUser(AchErrorResponse.class, customerIdentity, userName, domain, HttpStatus.SC_FORBIDDEN);

        soft.assertThat(newUser.getResponseEntity().getMessage()).isEqualTo("Operation not allowed.");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29182, 29184})
    @Description("Error when non admin user trying to edit and delete user")
    public void editDeleteNotAdmin() {
        String userName = new GenerateStringUtil().generateUserName();
        String updatedJobTitle = "QA";

        ResponseWrapper<User> newUser = achTestUtil.createNewUser(User.class, customerIdentity, userName, domain, HttpStatus.SC_CREATED);
        User userResponse = newUser.getResponseEntity();
        userIdentity = userResponse.getIdentity();

        RequestEntityUtil_Old.useTokenForRequests(getWidgetsUserToken(NOT_ADMIN_USER));

        ResponseWrapper<AchErrorResponse> updateUser = achTestUtil.patchUser(AchErrorResponse.class, userResponse, updatedJobTitle, HttpStatus.SC_FORBIDDEN);

        soft.assertThat(updateUser.getResponseEntity().getMessage()).isEqualTo("Operation not allowed.");

        RequestEntity deleteRequest = new RequestEntity().endpoint(ACHAPIEnum.USER_BY_ID)
            .returnType(AchErrorResponse.class)
            .token(getWidgetsUserToken(NOT_ADMIN_USER))
            .expectedResponseCode(HttpStatus.SC_FORBIDDEN)
            .inlineVariables(customerIdentity, userIdentity);

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();

        soft.assertThat(errorResponse.getResponseEntity().getMessage()).isEqualTo("Operation not allowed.");
        soft.assertAll();
    }

    private String getWidgetsUserToken(String name) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(PropertiesContext.get("ats.token_issuer"))
                    .subject("739e")
                    .claims(Claims.builder()
                        .name(name)
                        .email(name + "@widgets.aprioritest.com")
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

        User user = achTestUtil.findFirst(ACHAPIEnum.CUSTOMER_USERS, Users.class, filters, Collections.emptyMap(), customerIdentity);

        if (user == null) {
            throw new IllegalStateException(String.format("User, %s, is missing.", name));
        }

        return user;
    }
}