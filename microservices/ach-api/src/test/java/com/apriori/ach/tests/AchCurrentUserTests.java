package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.CurrentUser;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.authorization.AuthorizationUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AchCurrentUserTests {
    private AchTestUtil achTestUtil = new AchTestUtil();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = {21956})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        SoftAssertions soft = new SoftAssertions();
        String customerIdentity = achTestUtil.getAprioriInternal().getIdentity();
        ResponseWrapper<CurrentUser> user = achTestUtil.getCommonRequest(ACHAPIEnum.USER, CurrentUser.class, HttpStatus.SC_OK);

        soft.assertThat(user.getResponseEntity().getIdentity()).isNotEmpty();
        soft.assertThat(user.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }
}