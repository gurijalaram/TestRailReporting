package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.CustomersAch;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AchCustomersTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
    }

    @Test
    @TestRail(id = {21955})
    @Description("Get a list of customers")
    public void getCustomers() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<CustomersAch> customers = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMERS, CustomersAch.class, HttpStatus.SC_OK);

        soft.assertThat(customers.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(customers.getResponseEntity().getItems().get(0).getDeployments()).isNotEmpty();
        soft.assertAll();
    }
}