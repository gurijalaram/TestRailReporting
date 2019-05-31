package main.java;

import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.LoginPage;
import main.java.pages.PrivateWorkspacePage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginTests extends TestBase {

    private LoginPage loginPage;
    private PrivateWorkspacePage privateWorkspacePage;
    private static String loginPageErrorMessage = "Wrong email or password.";

    public LoginTests() {
        super();
    }

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * Test successuful login
     */
    @Test
    public void testLogin() {
        loginPage = new LoginPage(driver);
        privateWorkspacePage = loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword());
        Assert.assertTrue(privateWorkspacePage.isDeleteButtonPresent());
    }

    /**
     * Test unsuccessful login with correct email, incorrect password
     */
    @Test
    public void testIncorrectPwd() {
        loginPage = new LoginPage(driver);
        loginPage = loginPage.failedLoginAs(UsersEnum.CIE_TE_USER.getUsername(), "asdfdasfas");
        Assert.assertEquals(loginPageErrorMessage.toUpperCase(), loginPage.getLoginErrorMessage());
    }
}  
