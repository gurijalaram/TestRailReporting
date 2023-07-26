package com.apriori.tests;

import com.apriori.TestUtil;
import com.apriori.authorization.AuthorizationUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.entity.response.User;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class CasUsersTests extends TestUtil {
    private SoftAssertions soft = new SoftAssertions();
    private final CasTestUtil casTestUtil = new CasTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = {5666})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        ResponseWrapper<User> user = casTestUtil.getCommonRequest(CASAPIEnum.CURRENT_USER, User.class, HttpStatus.SC_OK);

        soft.assertThat(user.getResponseEntity().getIdentity())
            .isNotEmpty();
        soft.assertAll();
    }
}
