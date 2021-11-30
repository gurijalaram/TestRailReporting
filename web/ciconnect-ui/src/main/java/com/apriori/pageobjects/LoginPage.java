package com.apriori.pageobjects;

import static org.junit.Assert.assertTrue;

import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.users.UserCredentials;
import com.apriori.workflows.GenericWorkflow;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class LoginPage extends LoadableComponent<LoginPage> {

    private WebDriver driver;
    private PageUtils pageUtils;
    private AprioriLoginPage aprioriLoginPage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "ci-connect");
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CIC login page was not displayed", aprioriLoginPage.getPageTitle().contains("Cost Insight Connect"));
    }

    /**
     * Login to cid
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public GenericWorkflow login(final UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, GenericWorkflow.class);
    }
}