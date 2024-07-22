package com.apriori.cas.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
public class CasUsersTests {
    private SoftAssertions soft = new SoftAssertions();
    private RequestEntityUtil requestEntityUtil;

    @BeforeEach
    public void setUp() {
        requestEntityUtil = TestHelper.initUser()
            .useTokenInRequests();
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {5666})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        RequestEntity request = requestEntityUtil.init(CASAPIEnum.CURRENT_USER, User.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<User> user = HTTPRequest.build(request).get();

        soft.assertThat(user.getResponseEntity().getIdentity())
            .isNotEmpty();
        soft.assertAll();
    }
}
