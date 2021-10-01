package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.Applications;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class ApplicationsTests {

    private String token;

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5659"})
    @Description("Returns a list of applications for the customer.")
    public void getCustomerApplications() {
        ResponseWrapper<Applications> responseApplications = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Applications.class)
            .token(token)).get();

        assertThat(responseApplications.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseApplications.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }
}