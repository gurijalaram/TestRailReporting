package com.apriori.edc.ui.tests.myuser;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.edc.ui.pageobjects.login.EdcAppLoginPage;
import com.apriori.edc.ui.pageobjects.navtoolbars.MyProfilePage;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

public class MyProfileTests extends TestBaseUI {

    private EdcAppLoginPage loginPage;
    private MyProfilePage myProfilePage;
    private UserCredentials currentUser;

    public MyProfileTests() {
        super();
    }

    @Test
    @TestRail(id = {1551})
    @Description("Navigate to My Profile Test")
    public void testMyProfile() {
        currentUser = UserUtil.getUser();

        loginPage = new EdcAppLoginPage(driver);
        myProfilePage = loginPage.login(currentUser)
            .clickMyProfile();

        assertThat(myProfilePage.getUserProfileUrl(), containsString("user-profile"));
        assertThat(myProfilePage.isTextFieldEnabled(), not(true));
    }
}
