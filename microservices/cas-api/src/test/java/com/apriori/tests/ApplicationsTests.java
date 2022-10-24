package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.Applications;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class ApplicationsTests {

    private String token;
    private SoftAssertions soft = new SoftAssertions();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @Test
    @TestRail(testCaseId = {"5659"})
    @Description("Returns a list of applications for the customer.")
    public void getCustomerApplications() {
        ResponseWrapper<Customers> customersResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
                .token(token)).get();

        Customer customer = customersResponse.getResponseEntity().getItems().get(0);

        ResponseWrapper<Applications> responseApplications = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_APPLICATIONS, Applications.class)
            .token(token)
            .inlineVariables(customer.getIdentity())).get();

        soft.assertThat(responseApplications.getStatusCode())
                .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseApplications.getResponseEntity().getTotalItemCount())
                .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}