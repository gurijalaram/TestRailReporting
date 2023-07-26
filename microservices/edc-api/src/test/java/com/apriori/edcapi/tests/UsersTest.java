package com.apriori.edcapi.tests;

import com.apriori.authorization.AuthorizationUtil;
import com.apriori.edcapi.utils.UsersUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class UsersTest extends UsersUtil {

    SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = 9424")
        @Description("GET the current representation of the user performing the request.")
        public void testGetCurrentRepresentationOfUser(){
        Users users=getCurrentUser();

        softAssertions.assertThat(users.getUserType()).isEqualTo("AP_STAFF_USER");
        softAssertions.assertThat(users.getUserProfile().getFamilyName()).isEqualTo("Automation Account 01");
        softAssertions.assertThat(users.getCustomAttributes().getWorkspaceId()).isEqualTo(355);

        softAssertions.assertAll();
        }
}
