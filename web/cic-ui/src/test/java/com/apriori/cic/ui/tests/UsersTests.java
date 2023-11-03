package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.users.UsersPage;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
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
    @Tag(SMOKE)
    @TestRail(id = {3814})
    @Description("Test Users List Tab")
    public void testUserTab() {
        UsersPage usersPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickUsersMenu();
        usersPage.validateUsersSortedAlphabetical();
    }
}
