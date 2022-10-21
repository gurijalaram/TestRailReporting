package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CasCustomersTests {
    private SoftAssertions soft = new SoftAssertions();
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"5810"})
    @Description("Get a list of CAS customers sorted by name")
    public void getCustomersSortedByName() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
            .token(token)).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(response.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5645"})
    @Description("Get the Customer identified by its identity")
    public void getCustomersByIdentity() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
            .token(token)).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);

        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customer> responseIdentity = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, Customer.class)
            .token(token)
            .inlineVariables(customer.getIdentity())).get();

        soft.assertThat(responseIdentity.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseIdentity.getResponseEntity().getName())
            .isEqualTo(customer.getName());
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer identified by its name")
    public void getCustomerByName() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
            .token(token)).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);

        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customers> responseName = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, Customers.class)
            .token(token)
            .urlEncodingEnabled(true)
            .inlineVariables("?name[CN]=" + customer.getName())).get();

        soft.assertThat(responseName.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseName.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5644"})
    @Description("Get the Customer by not existing identity")
    public void getCustomerNotExistingIdentity() {
        ResponseWrapper<CasErrorMessage> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, CasErrorMessage.class)
            .token(token)
            .inlineVariables("76EA87KCHIKD")).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer by not existing name")
    public void getCustomerNotExistingName() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, Customers.class)
            .token(token)
            .urlEncodingEnabled(false)
            .inlineVariables("?name[CN]=" + generateStringUtil.generateCustomerName())).get();

        soft.assertThat(response.getResponseEntity().getTotalItemCount())
            .isEqualTo(0);
    }

    @Test
    @TestRail(testCaseId = {"5642", "5644"})
    @Description("Add a new customer, get it by name, update the customer and get it by identity")
    public void createUpdateCustomer() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String emailPattern = "\\S+@".concat(customerName);

        ResponseWrapper<Customer> response = CasTestUtil.addCustomer(customerName, cloudRef, description, emailPattern);

        soft.assertThat(response.getResponseEntity().getName())
            .isEqualTo(customerName);

        ResponseWrapper<Customers> responseName = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, Customers.class)
            .token(token)
            .urlEncodingEnabled(false)
            .inlineVariables("?name[CN]=" + customerName)).get();

        soft.assertThat(responseName.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseName.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        customerIdentity = responseName.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Customer> patchResponse = CasTestUtil.updateCustomer(customerIdentity, email);

        soft.assertThat(patchResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(patchResponse.getResponseEntity().getEmailDomains())
            .isEqualTo(Arrays.asList(email + ".com", email + ".co.uk"));

        ResponseWrapper<Customer> responseIdentity = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, Customer.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        soft.assertThat(responseIdentity.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseIdentity.getResponseEntity().getName())
            .isEqualTo(customerName);
        soft.assertThat(responseIdentity.getResponseEntity().getEmailDomains())
            .isEqualTo(Arrays.asList(email + ".com", email + ".co.uk"));
        soft.assertAll();
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

        soft.assertThat(response.getResponseEntity().getName())
            .isEqualTo(customerName);

        customerIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<String> resettingResponse = CasTestUtil.resetMfa(customerIdentity);

        soft.assertThat(resettingResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_ACCEPTED);
        soft.assertAll();
    }
}
