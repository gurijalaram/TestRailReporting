package com.apriori.edc.ui.pageobjects.navtoolbars;

import com.apriori.edc.ui.pageobjects.login.AccountsPage;
import com.apriori.edc.ui.pageobjects.login.EdcAppLoginPage;
import com.apriori.edc.ui.pageobjects.login.FileUploadPage;
import com.apriori.web.app.util.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

@Slf4j
public class NavigationBar extends EagerPageComponent<NavigationBar> {

    @FindBy(css = ".help-dropdown")
    private WebElement helpDropdown;

    @FindBy(css = ".user-dropdown.dropdown")
    private WebElement userDropdown;

    @FindBy(css = "[data-icon='chevron-left']")
    private WebElement goBackButton;

    @FindBy(css = ".dropdown-item [data-icon='info-circle']")
    private WebElement aboutButton;

    @FindBy(css = ".dropdown-item [data-icon='question-circle']")
    private WebElement helpButton;

    @FindBy(css = "[data-icon='sign-out-alt']")
    private WebElement logoutButton;

    @FindBy(css = ".dropdown-item [data-icon='user']")
    private WebElement myProfile;

    @FindBy(css = ".dropdown-item [data-icon='gavel']")
    private WebElement termsOfUse;

    @FindBy(css = ".dropdown-item [data-icon='folder-plus']")
    private WebElement uploadBom;

    @FindBy(css = ".dropdown-item [data-icon='tasks']")
    private WebElement manageAccounts;

    public NavigationBar(WebDriver driver, Logger logger) {
        super(driver, logger);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(helpDropdown);
    }

    /**
     * Select Help from Help drop down
     *
     * @return new page object
     */
    public ZendeskSignInPage clickHelp() {
        getPageUtils().waitForElementAndClick(helpDropdown);
        getPageUtils().waitForElementAndClick(helpButton);
        getPageUtils().switchToWindow(1);
        return new ZendeskSignInPage(getDriver());
    }

    /**
     * Select About from Help drop down
     *
     * @return new page object
     */
    public AboutUsPage clickAbout() {
        getPageUtils().waitForElementAndClick(helpDropdown);
        getPageUtils().waitForElementAndClick(aboutButton);
        getPageUtils().switchToWindow(1);
        return new AboutUsPage(getDriver());
    }

    /**
     * Click back button <
     *
     * @return new page object
     */
    public FileUploadPage clickBackButton() {
        getPageUtils().waitForElementAndClick(goBackButton);
        return new FileUploadPage(getDriver());
    }

    /**
     * Click on Upload BOM from the User dropdown
     *
     * @return new page object
     */
    public FileUploadPage clickUploadBom() {
        getPageUtils().waitForElementAndClick(userDropdown);
        getPageUtils().waitForElementAndClick(uploadBom);
        return new FileUploadPage(getDriver());
    }

    /**
     * Click on My Profile
     *
     * @return new page object
     */
    public MyProfilePage clickMyProfile() {
        getPageUtils().waitForElementAndClick(userDropdown);
        getPageUtils().waitForElementAndClick(myProfile);
        return new MyProfilePage(getDriver());
    }

    /**
     * Click on the Manage Accounts from User dropdown
     *
     * @return new page object
     */
    public AccountsPage clickManageAccounts() {
        getPageUtils().waitForElementAndClick(userDropdown);
        getPageUtils().waitForElementAndClick(manageAccounts);
        return new AccountsPage(getDriver());

    }

    /**
     * Click Terms of Use from User dropdown
     *
     * @return new page object
     */
    public TermsOfUsePage clickTermsOfUse() {
        getPageUtils().waitForElementAndClick(userDropdown);
        getPageUtils().waitForElementAndClick(termsOfUse);
        return new TermsOfUsePage(getDriver());
    }

    /**
     * Clicks the User dropdown and select Logout
     *
     * @retun new page object
     */
    public EdcAppLoginPage logout() {
        getPageUtils().waitForElementAndClick(userDropdown);
        getPageUtils().waitForElementAndClick(logoutButton);
        return new EdcAppLoginPage(getDriver());
    }
}

