package com.apriori;

import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.users.UsersPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class UsersTests extends TestBaseUI {
    private UserCredentials currentUser = UserUtil.getUser();

    public UsersTests() {
        super();
    }

    @Before
    public void setup() {
    }

    @Test
    @TestRail(id = {3814})
    @Description("Test Users List Tab")
    public void testUserTab() {
        UsersPage usersPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickUsersMenu();
        usersPage.validateUsersSortedAlphabetical();
    }
}
