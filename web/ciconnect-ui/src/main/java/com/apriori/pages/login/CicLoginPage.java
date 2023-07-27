package com.apriori.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.login.LoginService;
import com.apriori.pages.CICBasePage;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class CicLoginPage extends CICBasePage {

    private final LoginService aprioriLoginService;

    public CicLoginPage(WebDriver driver) {
        super(driver);
        this.aprioriLoginService = new LoginService(driver, "ci-connect");
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CIC login page was not displayed", aprioriLoginService.getLoginTitle().contains("Cost Insight Connect"));
    }

    /**
     * Login to cid
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public CIConnectHome login(final UserCredentials userCredentials) {
        CIConnectHome ciConnectHome = aprioriLoginService.login(userCredentials, CIConnectHome.class);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToBeClickable(usersMenuBtn);
        if (Objects.isNull(ciConnectHome)) {
            throw new RuntimeException("Login failed!!");
        }
        return ciConnectHome;
    }
}