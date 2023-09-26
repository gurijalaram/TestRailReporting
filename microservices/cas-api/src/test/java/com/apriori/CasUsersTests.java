package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.User;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CasUsersTests extends TestUtil {
    private SoftAssertions soft = new SoftAssertions();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
    }

    @Test
    @TestRail(id = {5666})
    @Description("Get the current representation of the user performing the request.")
    public void getCurrentUser() {
        ResponseWrapper<User> user = casTestUtil.getCommonRequest(CASAPIEnum.CURRENT_USER, User.class, HttpStatus.SC_OK);

        soft.assertThat(user.getResponseEntity().getIdentity())
            .isNotEmpty();
        soft.assertAll();
    }
}
