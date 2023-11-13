package com.apriori.ach.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchCurrentUserTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {21956})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        SoftAssertions soft = new SoftAssertions();
        String customerIdentity = achTestUtil.getAprioriInternal().getIdentity();
        ResponseWrapper<User> user = achTestUtil.getCommonRequest(ACHAPIEnum.USER, User.class, HttpStatus.SC_OK);

        soft.assertThat(user.getResponseEntity().getIdentity()).isNotEmpty();
        soft.assertThat(user.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }
}