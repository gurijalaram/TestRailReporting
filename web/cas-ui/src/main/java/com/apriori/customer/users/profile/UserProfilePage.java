package com.apriori.customer.users.profile;

import com.apriori.customer.users.UsersListPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserProfilePage extends LoadableComponent<UserProfilePage> {

    private static final Logger logger = LoggerFactory.getLogger(NewUserPage.class);

    @FindBy(xpath = "//button[.='Edit']")
    private WebElement editButton;

    @FindBy(xpath = "//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[.='Save']")
    private WebElement saveButton;

    @FindBy(linkText = "< Back to User List Page")
    private WebElement backToUsersListPage;

    @FindBy(css = "[type='checkbox']")
    private WebElement statusCheckbox;

    private WebDriver driver;
    private PageUtils pageUtils;

    public UserProfilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
        //Empty due to missed loading process
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(editButton);
    }

    /**
     * Edit customer info
     *
     * @return new page object
     */
    public UserProfilePage edit() {
        pageUtils.waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Cancels customer info
     *
     * @return new page object
     */
    public UserProfilePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return this;
    }

    /**
     * Saves user info
     *
     * @return new page object
     */
    public UserProfilePage save() {
        pageUtils.waitForElementAndClick(saveButton);
        return this;
    }

    /**
     * Opens customer staff page
     *
     * @return new page object
     */
    public UsersListPage backToUsersListPage() {
        pageUtils.waitForElementAndClick(backToUsersListPage);
        return new UsersListPage(driver);
    }

    /**
     * Changes status of user
     *
     * @return this object
     */
    public UserProfilePage changeStatus() {
        pageUtils.waitForElementAndClick(statusCheckbox);
        return this;
    }

    /**
     * Gets user identity
     *
     * @return string
     */
    public String getUserIdentity() {
        return driver.findElement(By.xpath("//span[.='Identity']/following-sibling::span[@class='display-field-value']")).getAttribute("textContent");
    }
}
