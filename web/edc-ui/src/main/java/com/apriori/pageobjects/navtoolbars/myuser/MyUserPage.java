package com.apriori.pageobjects.navtoolbars.myuser;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class MyUserPage extends LoadableComponent<MyUserPage> {

    @FindBy(css = "[data-icon='sign-out-alt']")
    private WebElement logoutButton;

    @FindBy(css = "[data-icon='user']")
    private WebElement myProfile;

    @FindBy(css = ".active-account")
    private WebElement activeAccount;

    @FindBy(css = "[data-icon='gavel']")
    private WebElement termsOfUse;

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
        pageUtils.waitForElementToAppear(activeAccount);
    }

    /**
     * Selects the User dropdown and go to Logout
     *
     * @retun new page object
     */
    public EdcAppLoginPage logout() {
        pageUtils.waitForElementAndClick(logoutButton);
        return new EdcAppLoginPage(driver);
    }

    public MyProfilePage clickMyProfile() {
        pageUtils.waitForElementAndClick(myProfile);
        return new MyProfilePage(driver);
    }

    public TermsOfUsePage selectTermsOfUse() {
        pageUtils.waitForElementAndClick(termsOfUse);
        return new TermsOfUsePage(driver);
    }
}
