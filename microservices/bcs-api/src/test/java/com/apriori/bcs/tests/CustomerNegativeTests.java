package com.apriori.bcs.tests;

import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CustomerNegativeTests {

    @Test
    @TestRail(testCaseId = {"8147"})
    @Description("Get costing preferences with Invalid Customer Identity")
    public void getUserPrefInvalidCustID() {
        HTTPRequest.build(RequestEntityUtil.init(
            BCSAPIEnum.CUSTOMER_USER_PREFERENCES, ErrorMessage.class)
                .inlineVariables("INVALIDCUSTOMER")
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
            ).get();
    }

    @Test
    @TestRail(testCaseId = {"8178"})
    @Description("Missing customer identity")
    public void getUserPrefMissingCustID() {
        HTTPRequest.build(RequestEntityUtil.init(
            BCSAPIEnum.CUSTOMER_USER_PREFERENCES_NO_CUSTOMER, ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_NOT_FOUND)
        ).get();
    }
}