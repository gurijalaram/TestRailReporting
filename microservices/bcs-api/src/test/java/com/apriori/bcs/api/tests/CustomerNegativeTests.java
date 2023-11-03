package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.enums.BCSAPIEnum;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CustomerNegativeTests {

    @Test
    @TestRail(id = {8147})
    @Description("Get costing preferences with Invalid Customer Identity")
    public void getUserPrefInvalidCustID() {
        HTTPRequest.build(RequestEntityUtil.init(
            BCSAPIEnum.CUSTOMER_USER_PREFERENCES, ErrorMessage.class)
                .inlineVariables("INVALIDCUSTOMER")
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
            ).get();
    }

    @Test
    @TestRail(id = {8178})
    @Description("Missing customer identity")
    public void getUserPrefMissingCustID() {
        HTTPRequest.build(RequestEntityUtil.init(
            BCSAPIEnum.CUSTOMER_USER_PREFERENCES_NO_CUSTOMER, ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_NOT_FOUND)
        ).get();
    }
}