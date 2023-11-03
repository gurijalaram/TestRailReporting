package com.apriori.cic.api.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.web.app.util.PageUtils;
import com.apriori.web.app.util.login.LoginService;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CicLoginUtil extends TestBaseUI {

    private LoginService aprioriLoginService;
    private WebDriver driver;
    private PageUtils pageUtils;

    private String sessionId;

    @FindBy(css = "span[title='Users']")
    private WebElement usersMenuBtn;

    public CicLoginUtil(WebDriver webdriver) {
        this.driver = webdriver;
        this.aprioriLoginService = new LoginService(driver, "ci-connect");
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Login to cid
     *
     * @return new page object
     */
    public CicLoginUtil login(UserCredentials currentUser) {
        aprioriLoginService.loginNoReturn(currentUser.getEmail(), currentUser.getPassword());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToBeClickable(usersMenuBtn);
        this.sessionId = driver.manage().getCookieNamed("JSESSIONID").getValue();
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getWebSession() {
        return this.sessionId;
    }

    public void refreshBrowser() {
        driver.navigate().refresh();
    }

    public void endSession() {
        driver.quit();
    }

    public CicLoginUtil navigateToUserMenu() {
        return this;
    }
}