package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.entity.response.Applications;
import com.apriori.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class ApplicationsTests extends TestUtil {

    private String token;

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5659"})
    @Description("Returns a list of applications for the customer.")
    public void getCustomerApplications() {
        String url = String.format(Constants.getApiUrl(), "customers/L2H992828LC1/applications");

        ResponseWrapper<Applications> responseApplications = new CommonRequestUtil().getCommonRequest(url, true, Applications.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseApplications.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseApplications.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }
}