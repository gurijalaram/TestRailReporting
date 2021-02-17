package com.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;

import com.apriori.apibase.services.cas.Customers;
import com.apriori.apibase.services.cas.Sites;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasSitesTests extends TestUtil {

    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

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
    @TestRail(testCaseId = {"5649"})
    @Description("Returns a list of sites for the customer")
    public void getCustomerSites() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers/");

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        String identity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        String siteEndpoint = apiUrl + identity + "/sites";

        ResponseWrapper<Sites> siteResponse = new CommonRequestUtil().getCommonRequest(siteEndpoint, true, Sites.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(siteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(siteResponse.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(siteResponse.getResponseEntity().getResponse().getItems().get(0).getSiteId(), is(not(emptyString())));
    }
}