package com.apriori.cic.ui.pageobjects.login;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.home.CIConnectHome;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.web.app.util.login.LoginService;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        assertTrue(aprioriLoginService.getLoginTitle().contains("Cost Insight Connect"), "CIC login page was not displayed");
    }

    /**
     * Login to cid
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public CIConnectHome login(final UserCredentials userCredentials) {
        CIConnectHome ciConnectHome = aprioriLoginService.login(userCredentials, CIConnectHome.class);
        pageUtils.waitForElementToBeClickable(usersMenuBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        if (Objects.isNull(ciConnectHome)) {
            throw new RuntimeException("Login failed!!");
        }
        return ciConnectHome;
    }

    /**
     * get email Input
     *
     * @return WebElement
     */
    public WebElement getEmailInputCloud() {
        return emailInputCloud;
    }
}