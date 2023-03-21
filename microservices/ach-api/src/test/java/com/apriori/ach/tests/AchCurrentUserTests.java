package com.apriori.ach.tests;

import com.apriori.ach.entity.response.CurrentUser;
import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class AchCurrentUserTests {
    private AchTestUtil achTestUtil = new AchTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = {"21956"})
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