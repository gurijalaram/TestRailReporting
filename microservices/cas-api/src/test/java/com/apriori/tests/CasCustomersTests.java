package com.apriori.tests;

import com.apriori.entity.response.Customer;
import com.apriori.entity.response.Customers;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.Applications;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
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
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
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
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5645"})
    @Description("Get the Customer identified by its identity")
    public void getCustomersByIdentity() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customer> responseIdentity = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customer.class, HttpStatus.SC_OK, customer.getIdentity());

        soft.assertThat(responseIdentity.getResponseEntity().getName())
            .isEqualTo(customer.getName());
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer identified by its name")
    public void getCustomerByName() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customers> responseName = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customers.class, HttpStatus.SC_OK,"?name[CN]=" + customer.getName());

        soft.assertThat(responseName.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5644"})
    @Description("Get the Customer by not existing identity")
    public void getCustomerNotExistingIdentity() {
        casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, CasErrorMessage.class, HttpStatus.SC_NOT_FOUND, "76EA87KCHIKD");
    }

    @Test
    @TestRail(testCaseId = {"5643"})
    @Description("Get the Customer by not existing name")
    public void getCustomerNotExistingName() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customers.class, HttpStatus.SC_OK, "?name[CN]=" + generateStringUtil.generateCustomerName());

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

        ResponseWrapper<Customers> responseName = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customers.class, HttpStatus.SC_OK, "?name[CN]=" + customerName);

        soft.assertThat(responseName.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        customerIdentity = responseName.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Customer> patchResponse = CasTestUtil.updateCustomer(customerIdentity, email);

        soft.assertThat(patchResponse.getResponseEntity().getEmailDomains())
            .isEqualTo(Arrays.asList(email + ".com", email + ".co.uk"));

        ResponseWrapper<Customer> responseIdentity = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customer.class, HttpStatus.SC_OK, customerIdentity);

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
        soft.assertAll();

        customerIdentity = response.getResponseEntity().getIdentity();
        CasTestUtil.resetMfa(customerIdentity);
    }

    @Test
    @TestRail(testCaseId = {"16546"})
    @Description("Return a paged list of applications licensed for a specific customer.")
    public void getCustomerLicensedApplications() {
        ResponseWrapper<Customers> customersResponse = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);

        Customer customer = customersResponse.getResponseEntity().getItems().get(0);

        ResponseWrapper<Applications> responseApplications = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_LICENSED_APP,
            Applications.class,
            HttpStatus.SC_OK,
            customer.getIdentity());

        soft.assertThat(responseApplications.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}
