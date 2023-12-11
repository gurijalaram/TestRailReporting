package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.EnablementsSupport;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchEnablementsSupportTests extends AchTestUtil {

    @Test
    @TestRail(id = {28023})
    @Description("Returns a list of customer assigned roles")
    public void getCustomerAssignedRoles() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<EnablementsSupport> customerRoles = getCommonRequest(ACHAPIEnum.ENABLEMENTS_SUPPORT, EnablementsSupport.class, HttpStatus.SC_OK);

        soft.assertThat(customerRoles.getResponseEntity().getCustomerAssignedRoles().size()).isGreaterThanOrEqualTo(1);
        soft.assertThat(customerRoles.getResponseEntity().getCustomerAssignedRoles().get(0).getIdentity()).isNotEmpty();
        soft.assertAll();
    }
}