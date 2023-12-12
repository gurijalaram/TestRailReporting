package com.apriori.ach.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchCurrentUserTests extends AchTestUtil {

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {21956})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        SoftAssertions soft = new SoftAssertions();
        String customerIdentity = getAprioriInternal().getIdentity();
        ResponseWrapper<User> user = getCommonRequest(ACHAPIEnum.USER, User.class, HttpStatus.SC_OK);

        soft.assertThat(user.getResponseEntity().getIdentity()).isNotEmpty();
        soft.assertThat(user.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }
}