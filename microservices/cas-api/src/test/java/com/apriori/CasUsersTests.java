package com.apriori;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.User;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CasUsersTests extends TestUtil {
    private SoftAssertions soft = new SoftAssertions();
    private String userToken = UserUtil.getUser("admin").getToken();

    // TODO z: fix it threads
    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(userToken);
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {5666})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        RequestEntity request = new RequestEntity().endpoint(CASAPIEnum.CURRENT_USER)
            .returnType(User.class)
            .token(userToken)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<User> user = HTTPRequest.build(request).get();

        soft.assertThat(user.getResponseEntity().getIdentity())
            .isNotEmpty();
        soft.assertAll();
    }
}
