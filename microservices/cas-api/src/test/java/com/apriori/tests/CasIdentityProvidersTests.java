package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.IdentityProviders;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.SingleIdp;
import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasIdentityProvidersTests extends TestUtil {

    private String token;

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5646", "5647"})
    @Description("Get IDPs for customer and get IDP by identity")
    public void getIdpCustomer() {

        ResponseWrapper<IdentityProviders> response = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.GET_CUSTOMER_ID, true, IdentityProviders.class,
            new APIAuthentication().initAuthorizationHeaderContent(token))
            .inlineVariables("L2H992828N8M/deployments"))
            .get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String identity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SingleIdp> responseIdentity = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.GET_CUSTOMER_DEPLOYMENT, true, SingleIdp.class,
            new APIAuthentication().initAuthorizationHeaderContent(token))
            .inlineVariables(identity))
            .get();

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getResponse().getIdentity(), is(equalTo(identity)));
    }
}
