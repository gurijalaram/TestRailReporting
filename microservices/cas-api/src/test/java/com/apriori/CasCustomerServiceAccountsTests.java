package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.CasErrorMessage;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.ServiceAccounts;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CasCustomerServiceAccountsTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private Customer customer;
    private Customer onPremCustomer;
    private String customerIdentity;
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void setup() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @Description("Returns a list of users for the customer that are considered service accounts.")
    @TestRail(id = {16543})
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
    @TestRail(id = {16544})
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