package com.apriori.tests;

import com.apriori.entity.response.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.entity.response.ServiceAccounts;
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

public class CasCustomerServiceAccountsTests {
    private Customer customer;
    private Customer onPremCustomer;
    private String customerIdentity;
    private SoftAssertions soft = new SoftAssertions();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setup() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @Description("Returns a list of users for the customer that are considered service accounts.")
    @TestRail(testCaseId = {"16543"})
    public void getCustomersServiceAccounts() {
        customer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = customer.getIdentity();

        ResponseWrapper<ServiceAccounts> serviceAccountsResponse = casTestUtil.getCommonRequest(CASAPIEnum.SERVICE_ACCOUNTS, ServiceAccounts.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(serviceAccountsResponse.getResponseEntity().getTotalItemCount())
            .isEqualTo(4);
        soft.assertThat(serviceAccountsResponse.getResponseEntity().getItems().get(0).getUserProfile().getGivenName())
            .isEqualTo("service-account");
        soft.assertAll();
    }

    @Test
    @Description("Gets service accounts for On Prem customer")
    @TestRail(testCaseId = {"16544"})
    public void getServiceAccountsOnPremCustomer() {
        String customerName = new GenerateStringUtil().generateCustomerName();
        String email = customerName.toLowerCase();
        onPremCustomer = casTestUtil.findOnPremCustomer();

        onPremCustomer = onPremCustomer == null
            ? casTestUtil.createOnPremCustomer(customerName, email).getResponseEntity()
            : onPremCustomer;

        String customerIdentity = onPremCustomer.getIdentity();

        ResponseWrapper<CasErrorMessage> serviceAccounts = casTestUtil.getCommonRequest(CASAPIEnum.SERVICE_ACCOUNTS, CasErrorMessage.class, HttpStatus.SC_BAD_REQUEST, customerIdentity);
        soft.assertThat(serviceAccounts.getResponseEntity().getMessage())
            .isEqualTo(String.format("Customer with identity, %s, is not a cloud supported customer.", customerIdentity));
        soft.assertAll();
    }
}