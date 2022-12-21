package com.apriori.tests;

import com.apriori.cusapi.entity.response.User;
import com.apriori.cusapi.utils.PeopleUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class UserTests {
    private static UserCredentials currentUser;
    SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = "16822")
    @Description("Verify GET current user endpoint test")
    public void verifyCurrentUserTest() {
        User user = new PeopleUtil().getCurrentUser(currentUser);
        softAssertions.assertThat(user.getUserType()).isEqualTo("AP_STAFF_USER");
        softAssertions.assertAll();
    }
}
