package com.apriori;

import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.users.UsersPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UsersTests extends TestBaseUI {
    private UserCredentials currentUser = UserUtil.getUser();

    public UsersTests() {
        super();
    }

    @BeforeEach
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
