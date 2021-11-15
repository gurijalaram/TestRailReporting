package com.apriori.pageobjects.pages.myuser;

import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MyUserPage extends EagerPageComponent<MyUserPage> {

    @FindBy(xpath = "//button[.='My Profile']")
    private WebElement myProfileButton;

    @FindBy(xpath = "//button[.='Terms of Use']")
    private WebElement termsOfUseButton;

    @FindBy(xpath = "//button[.='Logout']")
    private WebElement logoutButton;

    public MyUserPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(myProfileButton);
    }

    /**
     * Selects the User dropdown and go to Logout
     *
     * @retun new page object
     */
    public CisLoginPage logout() {
        getPageUtils().waitForElementAndClick(logoutButton);
        return new CisLoginPage(getDriver());
    }

    /**
     * Click on My Profile
     *
     * @return new page object
     */
    public MyProfilePage selectMyProfile() {
        getPageUtils().waitForElementAndClick(myProfileButton);
        return new MyProfilePage(getDriver());
    }

    /**
     * Click Terms of Use
     *
     * @return new page object
     */
    public TermsOfUsePage selectTermsOfUse() {
        getPageUtils().waitForElementAndClick(termsOfUseButton);
        return new TermsOfUsePage(getDriver());
    }
}
