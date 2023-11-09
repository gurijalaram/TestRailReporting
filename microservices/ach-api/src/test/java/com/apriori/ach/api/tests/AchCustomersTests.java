package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.CustomersAch;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchCustomersTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil_Old.useTokenForRequests(currentUser.getToken());
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