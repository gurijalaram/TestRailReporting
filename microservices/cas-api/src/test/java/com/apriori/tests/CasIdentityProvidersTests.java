package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.IdentityProviders;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.SingleIdp;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CasIdentityProvidersTests extends TestUtil {

    private String token;

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @Test
    @Ignore ("Will be fixed after fixing a bug IDS-851")
    @Issue("IDS-851")
    @TestRail(testCaseId = {"5646", "5647"})
    @Description("Get IDPs for customer and get IDP by identity")
    public void getIdpCustomer() {

        ResponseWrapper<IdentityProviders> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, IdentityProviders.class)
            .token(token)
            .inlineVariables("L2H992828N8M/identity-providers")).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String identity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SingleIdp> responseIdentity = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_DEPLOYMENT, SingleIdp.class)
            .token(token)
            .inlineVariables(identity)).get();

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getResponse().getIdentity(), is(equalTo(identity)));
    }
}
