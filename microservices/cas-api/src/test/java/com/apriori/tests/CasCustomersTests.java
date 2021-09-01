package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.entity.response.SingleCustomer;
import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import com.google.common.net.UrlEscapers;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CasCustomersTests extends TestUtil {

    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CasTestUtil casTestUtil = new CasTestUtil();
    private String url = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers/");

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5810"})
    @Description("Get a list of CAS customers sorted by name")
    public void getCustomersSortedByName() {
        String apiUrl = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers?sortBy[ASC]=name");

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, Customers.class,
            new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5645"})
    @Description("Get the Customer identified by its identity")
    public void getCustomersByIdentity() {
        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(url, true, Customers.class,
            new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        Customer customer = response.getResponseEntity().getResponse().getItems().get(0);
        String identity = customer.getIdentity();
        String name = customer.getName();

        String identityEndpoint = url + identity;

        ResponseWrapper<Customer> responseIdentity = new CommonRequestUtil().getCommonRequest(identityEndpoint, true, Customer.class,
            new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getName(), is(equalTo(name)));
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer identified by its name")
    public void getCustomerByName() {
        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(url, true, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        Customer customer = response.getResponseEntity().getResponse().getItems().get(0);
        String name = customer.getName();

        String nameEndpoint = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers").concat("?name[CN]=") + UrlEscapers.urlFragmentEscaper().escape(name);

        ResponseWrapper<Customers> responseName = new CommonRequestUtil().getCommonRequest(nameEndpoint, false, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseName.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseName.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5644"})
    @Description("Get the Customer by not existing identity")
    public void getCustomerNotExistingIdentity() {
        String apiUrl = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers/76EA87KCHIKD");

        ResponseWrapper<ErrorMessage> response = new CommonRequestUtil().getCommonRequest(apiUrl, true, ErrorMessage.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer by not existing name")
    public void getCustomerNotExistingName() {
        String name = generateStringUtil.generateCustomerName();
        String apiUrl = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers").concat("?name[CN]=") + UrlEscapers.urlFragmentEscaper().escape(name);

        ResponseWrapper<Customers> response = new CommonRequestUtil().getCommonRequest(apiUrl, false, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(0));
    }

    @Test
    @Issue("MIC-1679")
    @TestRail(testCaseId = {"5642", "5644"})
    @Description("Add a new customer, get it by name, update the customer and get it by identity")
    public void createUpdateCustomer() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String emailPattern = "\\S+@".concat(customerName);

        ResponseWrapper<SingleCustomer> response = casTestUtil.addCustomer(customerName, cloudRef, description, emailPattern);

        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));

        String customerNameUrl = String.format(PropertiesContext.get("${env}.cas.api_url"), "customers").concat("?name[CN]=") + UrlEscapers.urlFragmentEscaper().escape(customerName);

        ResponseWrapper<Customers> responseName = new CommonRequestUtil().getCommonRequest(customerNameUrl, false, Customers.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseName.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseName.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String identity = responseName.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String identityUrl = url + identity;

        ResponseWrapper<Customer> patchResponse = casTestUtil.updateCustomer(identity, email);

        assertThat(patchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(patchResponse.getResponseEntity().getEmailDomains(), is(equalTo(Arrays.asList(email + "com", email + ".co.uk"))));

        ResponseWrapper<Customer> responseIdentity = new CommonRequestUtil().getCommonRequest(identityUrl, true, Customer.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getName(), is(equalTo(customerName)));
        assertThat(responseIdentity.getResponseEntity().getEmailDomains(), is(equalTo(Arrays.asList(email + "com", email + ".co.uk"))));
    }

    @Test
    @TestRail(testCaseId = {"5826"})
    @Description("Resetting the MFA enrollment status of every user for the customer")
    public void ResettingMFA() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<SingleCustomer> response = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));

        String identity = response.getResponseEntity().getIdentity();
        String mfaUrl = url + identity + "/reset-mfa";

        ResponseWrapper resettingResponse = casTestUtil.resetMfa(mfaUrl);

        assertThat(resettingResponse.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
    }
}
