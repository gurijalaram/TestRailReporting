package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.AchErrorResponse;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchCustomerUsersTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private final UserCredentials currentUser = UserUtil.getUser();
    private String customerIdentity;

    @BeforeEach
    public void getToken() {
        RequestEntityUtil_Old.useTokenForRequests(currentUser.getToken());
        customerIdentity = achTestUtil.getCustomer("aPriori Internal").getIdentity();
    }

    @Test
    @TestRail(id = {28477})
    @Description("Get a list of customer users")
    public void getCustomerUsers() {
        ResponseWrapper<Users> users = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMER_USERS, Users.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(users.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(users.getResponseEntity().getItems().get(0).getUserProfile()).isNotNull();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28478})
    @Description("Get user by name")
    public void getUserByName() {
        ResponseWrapper<Users> users = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMER_USERS, Users.class, HttpStatus.SC_OK, customerIdentity);
        String userName = users.getResponseEntity().getItems().get(0).getUsername();

        ResponseWrapper<Users> userByName = achTestUtil.getUsersWithParams("username[CN]", userName, customerIdentity);

        soft.assertThat(userByName.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(userByName.getResponseEntity().getItems().get(0).getUsername()).isEqualTo(userName);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28479})
    @Description("Get users with request params that no users match")
    public void getNoUsersMatch() {
        ResponseWrapper<Users> userNoMatch = achTestUtil.getUsersWithParams("identity[CN]", "000000000000", customerIdentity);

        soft.assertThat(userNoMatch.getResponseEntity().getTotalItemCount()).isEqualTo(0);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28480})
    @Description("Get users with request params that no users match")
    public void getUsersNonExistingCustomer() {
        String nonExistingIdentity = "000000000000";
        ResponseWrapper<AchErrorResponse> users = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMER_USERS, AchErrorResponse.class, HttpStatus.SC_NOT_FOUND, nonExistingIdentity);

        soft.assertThat(users.getResponseEntity().getMessage()).isEqualTo(String.format("Resource 'Customer' with identity '%s' was not found", nonExistingIdentity));
        soft.assertAll();
    }
}