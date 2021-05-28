package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.entity.response.User;
import com.apriori.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasUsersTests extends TestUtil {
    private String token;

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getCasServiceHost(),
                HttpStatus.SC_CREATED,
                Constants.getCasTokenUsername(),
                Constants.getCasTokenEmail(),
                Constants.getCasTokenIssuer(),
                Constants.getCasTokenSubject());
    }

    @Test
    @TestRail(testCaseId = {"5665"})
    @Description("Get user by identity.")
    public void getUserById() {
        String url = String.format(Constants.getApiUrl(), "users/18EFF425JA5J");

        ResponseWrapper<User> user = new CommonRequestUtil().getCommonRequest(url, true, User.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(user.getResponseEntity().getResponse().getIdentity(), is(not(emptyString())));
    }

    @Test
    @TestRail(testCaseId = {"5666"})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        String url = String.format(Constants.getApiUrl(), "users/current");

        ResponseWrapper<User> user = new CommonRequestUtil().getCommonRequest(url, true, User.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(user.getResponseEntity().getResponse().getIdentity(), is(not(emptyString())));
    }
}
