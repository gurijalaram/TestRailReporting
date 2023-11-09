package com.apriori.edc.api.tests;

import com.apriori.edc.api.models.response.users.Users;
import com.apriori.edc.api.utils.UsersUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UsersTest extends UsersUtil {

    SoftAssertions softAssertions = new SoftAssertions();

    @BeforeEach
    public void setUp() {
        RequestEntityUtil_Old.useTokenForRequests(UserUtil.getUser().getToken());
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
