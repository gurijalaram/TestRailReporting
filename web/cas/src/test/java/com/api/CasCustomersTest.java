package com.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.objects.Customer;
import com.apriori.apibase.services.cas.objects.Customers;
import com.apriori.apibase.services.cas.objects.ErrorMessage;
import com.apriori.apibase.services.cas.objects.SingleCustomerResponse;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.api.utils.CasTestUtil;

import com.google.common.net.UrlEscapers;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
    public void getCustomersSortedByName() {
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

        Customer customer = response.getResponseEntity().getResponse().getItems().get(0);
        String identity = customer.getIdentity();
        String name = customer.getName();

        String identityEndpoint = apiUrl + identity;

        ResponseWrapper<SingleCustomerResponse> responseIdentity = new CommonRequestUtil().getCommonRequest(identityEndpoint, true, SingleCustomerResponse.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getResponse().getName(), is(equalTo(name)));
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer identified by its name")
    public void getCustomerByName() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers");

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        Customer customer = response.getResponseEntity().getResponse().getItems().get(0);
        String name = customer.getName();

        String nameEndpoint = String.format(Constants.getApiUrl(), "customers").concat("?name[CN]=") + UrlEscapers.urlFragmentEscaper().escape(name);

        ResponseWrapper<Customers> responseName = new CommonRequestUtil().getCommonRequest(nameEndpoint, false, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseName.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseName.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5644"})
    @Description("Get the Customer by not existing identity")
    public void getCustomerNotExistingIdentity() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers/76EA87KCHIKD");

        ResponseWrapper<ErrorMessage> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, ErrorMessage.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer by not existing name")
    public void getCustomerNotExistingName() {
        String name = new GenerateStringUtil().generateCustomerName();
        String apiUrl = String.format(Constants.getApiUrl(), "customers").concat("?name[CN]=") + UrlEscapers.urlFragmentEscaper().escape(name);

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, false, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(0));
    }

    @Test
    @Issue("MIC-1679")
    @TestRail(testCaseId = {"5642", "5644"})
    @Description("Add a new customer, get it by name, update the customer and get it by identity")
    public void createUpdateCustomer() {
        String url = String.format(Constants.getApiUrl(), "customers/");
        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<SingleCustomerResponse> response = new CasTestUtil().addCustomer(url, SingleCustomerResponse.class, token, customerName, cloudRef, description, email);

        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        String customerNameUrl = String.format(Constants.getApiUrl(), "customers").concat("?name[CN]=") + UrlEscapers.urlFragmentEscaper().escape(customerName);

        ResponseWrapper<Customers> responseName = new CommonRequestUtil().getCommonRequest(customerNameUrl, false, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseName.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseName.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String identity = responseName.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String identityUrl = url + identity;

        ResponseWrapper<SingleCustomerResponse> patchResponse = new CasTestUtil().updateCustomer(identityUrl, SingleCustomerResponse.class, token, email);

        assertThat(patchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(patchResponse.getResponseEntity().getResponse().getEmailDomains(), is(equalTo(Arrays.asList(email + "com", email + ".co.uk"))));

        ResponseWrapper<SingleCustomerResponse> responseIdentity = new CommonRequestUtil().getCommonRequest(identityUrl, true, SingleCustomerResponse.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getResponse().getName(), is(equalTo("AutoCustomerPaJTKE")));
        assertThat(responseIdentity.getResponseEntity().getResponse().getEmailDomains(), is(equalTo(Arrays.asList(email + "com", email + ".co.uk"))));
    }
}
