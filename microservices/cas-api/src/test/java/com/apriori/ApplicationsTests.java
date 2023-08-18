package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.Applications;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.Customers;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApplicationsTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = {5659})
    @Description("Returns a list of applications for the customer.")
    public void getCustomerApplications() {
        ResponseWrapper<Customers> customersResponse = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);

        Customer customer = customersResponse.getResponseEntity().getItems().get(0);

        ResponseWrapper<Applications> responseApplications = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_APPLICATIONS,
            Applications.class,
            HttpStatus.SC_OK,
            customer.getIdentity());

        soft.assertThat(responseApplications.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}