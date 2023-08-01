package com.apriori.myuser;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.navtoolbars.MyProfilePage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

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
