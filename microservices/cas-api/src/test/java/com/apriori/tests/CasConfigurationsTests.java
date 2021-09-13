package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.Configurations;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasConfigurationsTests extends TestUtil {

    private String token;

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
        RequestEntityUtil.useTokenForRequests(token);
    }

    @Test
    @TestRail(testCaseId = {"5660"})
    @Description("Returns a list of all aP Versions.")
    public void getConfigurationsTest() {
        ResponseWrapper<Configurations> configurationsResponse = HTTP2Request.build(RequestEntityUtil.init(CASAPIEnum.GET_CONFIGURATIONS, Configurations.class)
            .token(token)).get();

        assertThat(configurationsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(configurationsResponse.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }
}