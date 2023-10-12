package com.apriori;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.Customers;
import com.apriori.cas.models.response.User;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

@ExtendWith(TestRulesApi.class)
public class CasUsersTests extends TestUtil {
    private SoftAssertions soft = new SoftAssertions();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private String userToken;

    // TODO z: fix it threads
    @BeforeEach
    public void getToken() {
        userToken = UserUtil.getUser().getToken();
        RequestEntityUtil.useTokenForRequests(userToken);
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {5666})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        //        ResponseWrapper<User> user = casTestUtil.getCommonRequest(CASAPIEnum.CURRENT_USER, User.class, HttpStatus.SC_OK);

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
