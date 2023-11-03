package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.ServiceAccounts;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CasCustomerServiceAccountsTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private Customer customer;
    private Customer onPremCustomer;
    private String customerIdentity;
    private SoftAssertions soft = new SoftAssertions();
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setup() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
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