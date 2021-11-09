package com.apriori.pageobjects.pages.myuser;

import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class MyUserPage extends LoadableComponent<MyUserPage> {

    @FindBy(xpath = "//button[.='My Profile']")
    private WebElement myProfileButton;

    @FindBy(xpath = "//button[.='Terms of Use']")
    private WebElement termsOfUseButton;

    @FindBy(xpath = "//button[.='Logout']")
    private WebElement logoutButton;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public MyUserPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(myProfileButton);
    }

    /**
     * Selects the User dropdown and go to Logout
     *
     * @retun new page object
     */
    public CisLoginPage logout() {
        pageUtils.waitForElementAndClick(logoutButton);
        return new CisLoginPage(driver);
    }

    /**
     * Click on My Profile
     *
     * @return new page object
     */
    public MyProfilePage selectMyProfile() {
        pageUtils.waitForElementAndClick(myProfileButton);
        return new MyProfilePage(driver);
    }

    /**
     * Click Terms of Use
     *
     * @return new page object
     */
    public TermsOfUsePage selectTermsOfUse() {
        pageUtils.waitForElementAndClick(termsOfUseButton);
        return new TermsOfUsePage(driver);
    }

}
