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
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasUsersTests extends TestUtil {
    private String token;

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5666"})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        String url = String.format(PropertiesContext.get("${env}.cas.api_url"), "users/current");

        ResponseWrapper<User> user = new CommonRequestUtil().getCommonRequest(url, true, User.class,
            new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(user.getResponseEntity().getIdentity(), is(not(emptyString())));
    }
}
