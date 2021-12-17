package com.myuser;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.myuser.MyProfilePage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class MyProfileTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private MyProfilePage myProfilePage;

    public MyProfileTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"1551"})
    @Description("Navigate to My Profile Test")
    public void testMyProfile() {

        loginPage = new EdcAppLoginPage(driver);
        myProfilePage = loginPage.login(UserUtil.getUser())
            .clickUserDropdown()
            .clickMyProfile();

        assertThat(myProfilePage.getUserProfileUrl(), containsString("user-profile"));
        assertThat(myProfilePage.isTextFieldEnabled(), not(true));
    }
}
