package com.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.objects.Customer;
import com.apriori.apibase.services.cas.objects.Customers;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasCustomersTest extends TestUtil {

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
    @TestRail(testCaseId = {"5810"})
    @Description("Get a list of CAS customers sorted by name")
    public void getCustomersByName() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers?sortBy[ASC]=name");

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5645"})
    @Description("Get the Customer identified by its identity")
    public void getCustomersByIdentity() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers/");

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        Customer customer =  response.getResponseEntity().getResponse().getItems().get(0);
        String identity = customer.getIdentity();
        String name = customer.getName();

        String identityEndpoint = apiUrl + identity;

        ResponseWrapper<Customer> responseIdentity = new CommonRequestUtil().getCommonRequest(identityEndpoint, true, Customer.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getCustomerResponse().getName(), is(equalTo(name)));
    }
}
