package com.cic.tests;

import com.apriori.pageobjects.LoginPage;
import com.apriori.pageobjects.UsersPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class UsersTests extends TestBase {
    private WorkflowPage workflowPage;
    private UsersPage usersPage;
    private LoginPage loginPage;


    public UsersTests() {
        super();
    }

    @Before
    public void setup() {
        workflowPage = new WorkflowPage(driver);
        usersPage = new UsersPage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    @TestRail(testCaseId = {"3814"})
    @Description("Test Users List Tab")
    public void testUserTab() {
        loginPage.login();
        workflowPage.clickUserTab();
        usersPage.validateUsersSortedAlphabetical();
    }
}
