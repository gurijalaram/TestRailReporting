package com.apriori.customer.users;

import com.apriori.common.UsersTableController;
import com.apriori.customer.users.profile.NewUserPage;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.SearchFieldComponent;
import com.apriori.utils.web.components.SourceListComponent;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the page under the users tab that contains the users list.
 */


public class UsersListPage extends LoadableComponent<UsersListPage> {

    private static final Logger logger = LoggerFactory.getLogger(UsersListPage.class);

    @FindBy(css = ".btn-light")
    private WebElement newUserButton;

    @FindBy(className = "apriori-source-list-layout-table-button")
    private WebElement tableViewButton;

    @FindBy(className = "apriori-source-list-layout-card-button")
    private WebElement cardViewButton;

    @FindBy(css = ".user-list-customer-staff")
    private WebElement userListCardViewRoot;
    private SourceListComponent userListCardView;

    private WebDriver driver;
    private PageUtils pageUtils;
    private UsersTableController usersTableController;

    /**
     * @inheritDoc
     */
    public UsersListPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        usersTableController = new UsersTableController(driver);
        userListCardView = new SourceListComponent(driver, userListCardViewRoot);
    }

    @Override
    protected void load() {
        //Empty due to missed loading process
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(newUserButton);
        pageUtils.waitForElementToAppear(tableViewButton);
        pageUtils.waitForElementToAppear(cardViewButton);
    }

    /**
     * Retrieves UsersListPage for customer via URL and returns Page object.
     *
     * @param driver - WebDriver
     * @param customer - Customer ID
     * @return UsersListPage
     */
    public static UsersListPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s/users/customer-staff";
        driver.navigate().to(String.format(url, customer));
        return new UsersListPage(driver);
    }

    /**
     * Gets the users list.
     *
     * @return The users list.
     */
    public SourceListComponent getUsersList() {
        return usersTableController.getUsersTable();
    }

    /**
     * Validates that table has a correct columns
     *
     * @param expectedName name of column
     * @param id id of column
     * @param soft soft assertions
     * @return This object
     */
    public UsersListPage validateUsersTableHasCorrectColumns(String expectedName, String id, SoftAssertions soft) {
        return usersTableController.validateUsersTableHasCorrectColumns(expectedName, id, soft, UsersListPage.class);
    }

    /**
     * Clicks the table view button
     *
     * @return This object
     */
    public UsersListPage clickTableViewButton() {
        pageUtils.waitForElementAndClick(tableViewButton);
        return this;
    }

    /**
     * Clicks the card view button
     *
     * @return This object
     */
    public UsersListPage clickCardViewButton() {
        pageUtils.waitForElementAndClick(cardViewButton);
        return this;
    }

    /**
     * Validates that table is pageable and refreshable
     *
     * @param soft soft assertions
     * @return This object
     */
    public UsersListPage validateCustomerStaffTableArePageableRefreshable(SoftAssertions soft) {
        return usersTableController.validateUsersTableArePageableAndRefreshable(soft, UsersListPage.class);
    }

    /**
     * Gets the user's locator
     *
     * @param customerIdentity the customer identity
     * @param userIdentity the user identity
     * @param userName the user name
     * @return web element
     */
    private WebElement findUser(String customerIdentity, String userIdentity, String userName) {
        By user = By.xpath(String.format("//a[@href='/customers/%s/user-profiles/%s']/div[.='%s']",
                customerIdentity.toUpperCase().trim(), userIdentity.toUpperCase().trim(), userName.trim()));
        pageUtils.waitForElementToAppear(user);
        return pageUtils.scrollWithJavaScript(driver.findElement(user), true);
    }

    /**
     * Clicks on user name
     *
     * @param customerIdentity the customer identity
     * @param userIdentity the user identity
     * @param userName the user name
     * @return new page object
     */
    public UserProfilePage selectUser(String customerIdentity, String userIdentity, String userName) {
        pageUtils.waitForElementAndClick(findUser(customerIdentity, userIdentity, userName));
        return new UserProfilePage(driver);
    }

    /**
     * Clicks on New button
     *
     * @return new page object
     */
    public NewUserPage clickNew() {
        pageUtils.waitForElementAndClick(newUserButton);
        return new NewUserPage(driver);
    }

    /**
     * Gets the user list in card view
     *
     * @return user list
     */
    public SourceListComponent getUserListCardView() {
        pageUtils.waitForCondition(userListCardView::isStable, PageUtils.DURATION_LOADING);
        return userListCardView;
    }

    /**
     * Gets the user's locator in card view
     *
     * @param customerIdentity the customer identity
     * @param userIdentity the user identity
     * @return web element
     */
    private WebElement findCardUser(String customerIdentity, String userIdentity) {
        By user = By.xpath(String.format("//a[@href='/customers/%s/user-profiles/%s']",
                customerIdentity.toUpperCase().trim(), userIdentity.toUpperCase().trim()));
        pageUtils.waitForElementToAppear(user);
        return pageUtils.scrollWithJavaScript(driver.findElement(user), true);
    }

    /**
     * Clicks on user card
     *
     * @param customerIdentity the customer identity
     * @param userIdentity the user isentity
     * @return new page object
     */
    public UserProfilePage selectCard(String customerIdentity, String userIdentity) {
        pageUtils.waitForElementAndClick(findCardUser(customerIdentity, userIdentity));
        return new UserProfilePage(driver);
    }

    /**
     * Checks is icon has a expected color
     *
     * @param customerIdentity the customer identity
     * @param userIdentity the user identity
     * @param color color of icon
     * @return true or false
     */
    public boolean isIconColour(String customerIdentity, String userIdentity, String color) {
        return pageUtils.scrollWithJavaScript(findStatusIcon(customerIdentity, userIdentity)
                .findElement(By.cssSelector("svg")), true).getAttribute("color").equals(color);
    }

    /**
     * Gets locator of status icon
     *
     * @param customerIdentity the customer identity
     * @param userIdentity the user identity
     * @return web element
     */
    private WebElement findStatusIcon(String customerIdentity, String userIdentity) {
        return driver.findElement(By.xpath((String.format("//a[@href='/customers/%s/user-profiles/%s']/ancestor::div[@class='card-header']//div[@class='right']",
                customerIdentity.toUpperCase().trim(), userIdentity.toUpperCase().trim()))));
    }

    /**
     * Gets the list of fields names
     *
     * @param customerIdentity the customer identity
     * @param userIdentity the user identity
     * @return list of fields names
     */
    public List<String> getFieldName(String customerIdentity, String userIdentity) {
        List<WebElement> fieldName = driver.findElements(By.xpath(String.format("//a[@href='/customers/%s/user-profiles/%s']/ancestor::div[@class='card-header']/following-sibling::div[@class='card-body']//label",
                customerIdentity.toUpperCase().trim(), userIdentity.toUpperCase().trim())));
        return fieldName.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Opens a customer profile from source table
     *
     * @param name - name of customer
     * @return The profile page for customer
     */
    public UserProfilePage openUser(String name) {
        SourceListComponent list = getUsersList();
        SearchFieldComponent search = Obligation.mandatory(list::getSearch, "The user search is missing.");
        search.search(name);
        pageUtils.waitForCondition(list::isStable, PageUtils.DURATION_LOADING);

        list.selectTableLayout();
        Obligation.mandatory(list::getTable, "The table layout is not active")
            .getRows()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("User %s is missing.", name)))
            .getCell("username")
            .click();

        return new UserProfilePage(driver);
    }
}
