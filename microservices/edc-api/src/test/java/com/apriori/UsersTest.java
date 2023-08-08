package com.apriori;

import com.apriori.edc.models.response.users.Users;
import com.apriori.edc.utils.UsersUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UsersTest extends UsersUtil {

    SoftAssertions softAssertions = new SoftAssertions();

    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = 9424)
    @Description("GET the current representation of the user performing the request.")
    public void testGetCurrentRepresentationOfUser() {
        Users users = getCurrentUser();

        softAssertions.assertThat(users.getUserType()).isEqualTo("AP_STAFF_USER");
        softAssertions.assertThat(users.getUserProfile().getFamilyName()).isEqualTo("Automation Account 01");
        softAssertions.assertThat(users.getCustomAttributes().getWorkspaceId()).isEqualTo(355);

        softAssertions.assertAll();
    }
}
