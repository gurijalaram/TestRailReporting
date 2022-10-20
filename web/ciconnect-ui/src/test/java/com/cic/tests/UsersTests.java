package com.cic.tests;

import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.users.UsersPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class UsersTests extends TestBase {
    private UserCredentials currentUser = UserUtil.getUser();


    public UsersTests() {
        super();
    }

    @Before
    public void setup() {
    }

    @Test
    @TestRail(testCaseId = {"3814"})
    @Description("Test Users List Tab")
    public void testUserTab() {
        UsersPage usersPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickUsersMenu();
        usersPage.validateUsersSortedAlphabetical();
    }
}
