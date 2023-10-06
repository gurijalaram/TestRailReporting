package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.AchErrorResponse;
import com.apriori.ach.models.response.UsersAch;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class AchCustomerUsersTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private final UserCredentials currentUser = UserUtil.getUser();
    private String customerIdentity;

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
        customerIdentity = achTestUtil.getAprioriInternal().getIdentity();
    }

    @Test
    @TestRail(id = {28477})
    @Description("Get a list of customer users")
    public void getCustomerUsers() {
        ResponseWrapper<UsersAch> users = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMER_USERS, UsersAch.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(users.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(users.getResponseEntity().getItems().get(0).getUserProfile()).isNotNull();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28478})
    @Description("Get user by name")
    public void getUserByName() {
        ResponseWrapper<UsersAch> users = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMER_USERS, UsersAch.class, HttpStatus.SC_OK, customerIdentity);
        String userName = users.getResponseEntity().getItems().get(0).getUsername();

        ResponseWrapper<UsersAch> userByName = achTestUtil.getUsersWithParams("username[CN]", userName, customerIdentity);

        soft.assertThat(userByName.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(userByName.getResponseEntity().getItems().get(0).getUsername()).isEqualTo(userName);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28479})
    @Description("Get users with request params that no users match")
    public void getNoUsersMatch() {
        ResponseWrapper<UsersAch> userNoMatch = achTestUtil.getUsersWithParams("identity[CN]", "000000000000", customerIdentity);

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