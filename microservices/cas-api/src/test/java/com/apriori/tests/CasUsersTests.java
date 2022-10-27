package com.apriori.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.User;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class CasUsersTests extends TestUtil {
    private SoftAssertions soft = new SoftAssertions();
    private String token;

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @Test
    @TestRail(testCaseId = {"5666"})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        ResponseWrapper<User> user = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CURRENT_USER, User.class)
            .token(token)).get();

        soft.assertThat(user.getStatusCode())
                .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(user.getResponseEntity().getIdentity())
                .isNotEmpty();
        soft.assertAll();
    }
}
