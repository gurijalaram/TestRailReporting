package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.EnablementsSupport;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchEnablementsSupportTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
    }

    @Test
    @TestRail(id = {28023})
    @Description("Returns a list of customer assigned roles")
    public void getCustomerAssignedRoles() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<EnablementsSupport> customerRoles = achTestUtil.getCommonRequest(ACHAPIEnum.ENABLEMENTS_SUPPORT, EnablementsSupport.class, HttpStatus.SC_OK);

        soft.assertThat(customerRoles.getResponseEntity().getCustomerAssignedRoles().size()).isGreaterThanOrEqualTo(1);
        soft.assertThat(customerRoles.getResponseEntity().getCustomerAssignedRoles().get(0).getIdentity()).isNotEmpty();
        soft.assertAll();
    }
}