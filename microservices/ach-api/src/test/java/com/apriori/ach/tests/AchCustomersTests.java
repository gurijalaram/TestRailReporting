package com.apriori.ach.tests;

import com.apriori.ach.entity.response.CustomersAch;
import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class AchCustomersTests {
    private AchTestUtil achTestUtil = new AchTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = {"21955"})
    @Description("Get a list of customers")
    public void getCustomers() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<CustomersAch> customers = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMERS, CustomersAch.class, HttpStatus.SC_OK);

        soft.assertThat(customers.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(customers.getResponseEntity().getItems().get(0).getDeployments()).isNotEmpty();
        soft.assertAll();
    }
}