package com.apriori.newcustomer.users;

import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationGrantsPage extends LoadableComponent<ApplicationGrantsPage> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationGrantsPage.class);

    @FindBy(xpath = "//span[.='Available Users']/parent::div//input[@class]")
    private WebElement userSearchInput;

    @FindBy(xpath = "//span[.='Users in Target Application']/parent::div//input[@class]")
    private WebElement applicationSearchInput;

    @FindBy(xpath = "//div[@class='banner d-flex justify-content-between mb-0']//button[.='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//div[@class='banner d-flex justify-content-between mb-0']//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(css = "svg[data-icon='chevron-left']")
    private WebElement leftChevron;

    @FindBy(css = "svg[data-icon='chevron-right']")
    private WebElement rightChevron;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public ApplicationGrantsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(userSearchInput);
        pageUtils.waitForElementAppear(applicationSearchInput);

    }

    /**
     * Search for user
     *
     * @param userName - user details
     * @return current page object
     */
    public ApplicationGrantsPage searchAvailableUser(String userName) {
        pageUtils.waitForElementToAppear(userSearchInput).sendKeys(userName);
        return this;
    }

    /**
     * Search for user
     *
     * @param userName - user details
     * @return current page object
     */
    public ApplicationGrantsPage searchApplicationUser(String userName) {
        pageUtils.waitForElementToAppear(applicationSearchInput).sendKeys(userName);
        return this;
    }

    /**
     * Move left
     *
     * @return current page object
     */
    public ApplicationGrantsPage moveLeft() {
        pageUtils.waitForElementAndClick(leftChevron);
        return this;
    }

    /**
     * Move right
     *
     * @return current page object
     */
    public ApplicationGrantsPage moveRight() {
        pageUtils.waitForElementAndClick(rightChevron);
        return this;
    }

    /**
     * Cancel customer info
     *
     * @return new page object
     */
    public <T> T cancel(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Save customer info
     *
     * @return new page object
     */
    public CustomerAdminPage save() {
        pageUtils.waitForElementAndClick(saveButton);
        return new CustomerAdminPage(driver);
    }
}
