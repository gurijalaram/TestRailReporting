package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CasCustomersTests {

    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5810"})
    @Description("Get a list of CAS customers sorted by name")
    public void getCustomersSortedByName() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Customers.class)
            .token(token)).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5645"})
    @Description("Get the Customer identified by its identity")
    public void getCustomersByIdentity() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Customers.class)
            .token(token)).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customer> responseIdentity = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_ID, Customer.class)
            .token(token)
            .inlineVariables(customer.getIdentity())).get();

        assertThat(responseIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseIdentity.getResponseEntity().getName(), is(equalTo(customer.getName())));
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer identified by its name")
    public void getCustomerByName() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Customers.class)
            .token(token)).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customers> responseName = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_ID, Customer.class)
            .token(token)
            .urlEncodingEnabled(false)
            .inlineVariables("?name[CN]=" + customer.getName())).get();

        assertThat(responseName.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseName.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5644"})
    @Description("Get the Customer by not existing identity")
    public void getCustomerNotExistingIdentity() {
        ResponseWrapper<ErrorMessage> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_ID, Customer.class)
            .token(token)
            .inlineVariables("76EA87KCHIKD")).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer by not existing name")
    public void getCustomerNotExistingName() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_ID, Customer.class)
            .token(token)
            .inlineVariables("?name[CN]=" + generateStringUtil.generateCustomerName())).get();

        assertThat(response.getResponseEntity().getTotalItemCount(), is(0));
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

        ResponseWrapper<Customer> response = CasTestUtil.addCustomer(customerName, cloudRef, description, emailPattern);

        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));

        ResponseWrapper<Customers> responseName = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_ID, Customer.class)
            .token(token)
            .urlEncodingEnabled(false)
            .inlineVariables("?name[CN]=" + customerName)).get();

        assertThat(responseName.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseName.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String identity = responseName.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Customer> patchResponse = CasTestUtil.updateCustomer(identity, email);

        assertThat(patchResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(patchResponse.getResponseEntity().getEmailDomains(), is(equalTo(Arrays.asList(email + "com", email + ".co.uk"))));

        ResponseWrapper<Customer> responseIdentity = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_ID, Customer.class)
            .token(token)
            .inlineVariables(identity)).get();

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

        ResponseWrapper<Customer> response = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));

        String identity = response.getResponseEntity().getIdentity();

        ResponseWrapper<String> resettingResponse = CasTestUtil.resetMfa(identity);

        assertThat(resettingResponse.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
    }
}
