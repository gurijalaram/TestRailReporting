package com.apriori.newcustomer.users;

import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsersListPage extends LoadableComponent<UsersListPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(UsersListPage.class);

    @FindBy(css = "a[name='userImport']")
    private WebElement importTab;

    @FindBy(css = "a[name='userAppsConfiguration']")
    private WebElement appGrantTab;

    @FindBy(xpath = "//button[.='Update']")
    private WebElement updateButton;

    @FindBy(xpath = "//a[.='Add']")
    private WebElement addButton;

    @FindBy(xpath = "//a[.='Deactivate']")
    private WebElement deactivateButton;

    @FindBy(xpath = "//a[.='Export']")
    private WebElement exportButton;

    @FindBy(css = "div[class='m-0'] [aria-label='Search']")
    private WebElement userNameSearch;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public UsersListPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(updateButton);
        pageUtils.waitForElementAppear(addButton);
    }

    /**
     * Go to import tab
     * @return new page object
     */
    public ImportPage goToImport() {
        pageUtils.waitForElementAndClick(importTab);
        return new ImportPage(driver);
    }

    /**
     * Go to application grants tab
     * @return new page object
     */
    public ApplicationGrantsPage goToApplicationGrants() {
        pageUtils.waitForElementAndClick(appGrantTab);
        return new ApplicationGrantsPage(driver);
    }

    /**
     * Add a new user
     * @return new page object
     */
    public NewUserPage addNewUser() {
        pageUtils.waitForElementAndClick(addButton);
        return new NewUserPage(driver);
    }

    /**
     * Deactivate user
     * @return current page object
     */
    public UsersListPage deactivateUser() {
        pageUtils.waitForElementAndClick(deactivateButton);
        return this;
    }

    /**
     * Export user
     * @return current page object
     */
    public UsersListPage exportUser() {
        pageUtils.waitForElementAndClick(exportButton);
        return this;
    }

    /**
     * Select a user
     * @param userName - user name
     * @return current page object
     */
    public UsersListPage selectUser(String userName) {
        By user = By.xpath(String.format("//a[.='%s']/ancestor::tr//input", userName));
        pageUtils.waitForElementAndClick(user);
        return this;
    }

    /**
     * Search for user
     *
     * @param userName - user details
     * @return current page object
     */
    public UsersListPage searchForUser(String userName) {
        pageUtils.waitForElementToAppear(userNameSearch).sendKeys(userName);
        return this;
    }
}
