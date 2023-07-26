package com.apriori.customer.users;

import com.apriori.PageUtils;
import com.apriori.common.UsersTableController;
import com.apriori.components.SearchFieldComponent;
import com.apriori.components.SourceListComponent;
import com.apriori.customer.users.profile.NewUserPage;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.http.utils.Obligation;
import com.apriori.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the page under the users tab that contains the users list.
 */

@Slf4j
public class UsersListPage extends UsersPage {

    @FindBy(css = ".btn-light")
    private WebElement newUserButton;

    @FindBy(className = "apriori-source-list-layout-table-button")
    private WebElement tableViewButton;

    @FindBy(className = "apriori-source-list-layout-card-button")
    private WebElement cardViewButton;

    @FindBy(css = ".user-list-customer-staff")
    private WebElement userListCardViewRoot;
    private SourceListComponent userListCardView;

    @FindBy(css = ".drawer.apriori-source-list-right .card-body")
    private WebElement detailsPlaceholder;

    @FindBy(css = ".user-licenses-details")
    private WebElement licenseDetailsListRoot;
    private final SourceListComponent licenseDetailsList;

    @FindBy(css = ".delete-user-button")
    private WebElement deleteUserButton;

    @FindBy(css = ".MuiDialogActions-spacing [data-testid='secondary-button']")
    private WebElement confirmDeleteCancelButton;

    @FindBy(xpath = "//button[.='Ok']")
    private WebElement confirmDeleteOkButton;

    @FindBy(css = ".fa-file-circle-minus")
    private WebElement trashcanIcon;

    private UsersTableController usersTableController;

    /**
     * @inheritDoc
     */
    public UsersListPage(WebDriver driver) {
        super(driver);
        usersTableController = new UsersTableController(driver);
        userListCardView = new SourceListComponent(driver, userListCardViewRoot);
        licenseDetailsList = new SourceListComponent(driver, licenseDetailsListRoot);
    }

    @Override
    protected void load() {
        //Empty due to missed loading process
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(newUserButton);
        getPageUtils().waitForElementToAppear(tableViewButton);
        getPageUtils().waitForElementToAppear(cardViewButton);
    }

    /**
     * Retrieves UsersListPage for customer via URL and returns Page object.
     *
     * @param driver   - WebDriver
     * @param customer - Customer ID
     * @return UsersListPage
     */
    public static UsersListPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("cas.ui_url") + "customers/%s/users/customer-staff";
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
     * @param id           id of column
     * @param soft         soft assertions
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
        getPageUtils().waitForElementAndClick(tableViewButton);
        return this;
    }

    /**
     * Clicks the card view button
     *
     * @return This object
     */
    public UsersListPage clickCardViewButton() {
        getPageUtils().waitForElementAndClick(cardViewButton);
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
     * @param userIdentity     the user identity
     * @param userName         the user name
     * @return web element
     */
    private WebElement findUser(String customerIdentity, String userIdentity, String userName) {
        By user = By.xpath(String.format("//a[@href='/customers/%s/user-profiles/%s']/div[.='%s']",
            customerIdentity.toUpperCase().trim(), userIdentity.toUpperCase().trim(), userName.trim()));
        getPageUtils().waitForElementToAppear(user);
        return getPageUtils().scrollWithJavaScript(getDriver().findElement(user), true);
    }

    /**
     * Clicks on user name
     *
     * @param customerIdentity the customer identity
     * @param userIdentity     the user identity
     * @param userName         the user name
     * @return new page object
     */
    public UserProfilePage selectUser(String customerIdentity, String userIdentity, String userName) {
        getPageUtils().waitForElementAndClick(findUser(customerIdentity, userIdentity, userName));
        return new UserProfilePage(getDriver());
    }

    /**
     * Clicks on New button
     *
     * @return new page object
     */
    public NewUserPage clickNew() {
        getPageUtils().waitForElementAndClick(newUserButton);
        return new NewUserPage(getDriver());
    }

    /**
     * Gets the user list in card view
     *
     * @return user list
     */
    public SourceListComponent getUserListCardView() {
        getPageUtils().waitForCondition(userListCardView::isStable, PageUtils.DURATION_LOADING);
        return userListCardView;
    }

    /**
     * Gets the user's locator in card view
     *
     * @param customerIdentity the customer identity
     * @param userIdentity     the user identity
     * @return web element
     */
    private WebElement findCardUser(String customerIdentity, String userIdentity) {
        By user = By.xpath(String.format("//a[@href='/customers/%s/user-profiles/%s']",
            customerIdentity.toUpperCase().trim(), userIdentity.toUpperCase().trim()));
        getPageUtils().waitForElementToAppear(user);
        return getPageUtils().scrollWithJavaScript(getDriver().findElement(user), true);
    }

    /**
     * Clicks on user card
     *
     * @param customerIdentity the customer identity
     * @param userIdentity     the user isentity
     * @return new page object
     */
    public UserProfilePage selectCard(String customerIdentity, String userIdentity) {
        getPageUtils().waitForElementAndClick(findCardUser(customerIdentity, userIdentity));
        return new UserProfilePage(getDriver());
    }

    /**
     * Checks is icon has a expected color
     *
     * @param customerIdentity the customer identity
     * @param userIdentity     the user identity
     * @param color            color of icon
     * @return true or false
     */
    public boolean isIconColour(String customerIdentity, String userIdentity, String color) {
        return getPageUtils().scrollWithJavaScript(findStatusIcon(customerIdentity, userIdentity)
            .findElement(By.cssSelector("svg")), true).getAttribute("color").equals(color);
    }

    /**
     * Gets locator of status icon
     *
     * @param customerIdentity the customer identity
     * @param userIdentity     the user identity
     * @return web element
     */
    private WebElement findStatusIcon(String customerIdentity, String userIdentity) {
        return getDriver().findElement(By.xpath((String.format("//a[@href='/customers/%s/user-profiles/%s']/ancestor::div[@class='card-header']//div[@class='right']",
            customerIdentity.toUpperCase().trim(), userIdentity.toUpperCase().trim()))));
    }

    /**
     * Gets the list of fields names
     *
     * @param customerIdentity the customer identity
     * @param userIdentity     the user identity
     * @return list of fields names
     */
    public List<String> getFieldName(String customerIdentity, String userIdentity) {
        List<WebElement> fieldName = getDriver().findElements(By.xpath(String.format("//a[@href='/customers/%s/user-profiles/%s']/ancestor::div[@class='card-header']/following-sibling::div[@class='card-body']//label",
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
        getPageUtils().waitForCondition(list::isStable, PageUtils.DURATION_LOADING);

        list.selectTableLayout();
        Obligation.mandatory(list::getTable, "The table layout is not active")
            .getRows()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("User %s is missing.", name)))
            .getCell("username")
            .click();

        return new UserProfilePage(getDriver());
    }

    /**
     * Clicks on Expand/Hide license details button
     *
     * @param arrow - direction of button arrow
     * @return this page object
     */
    public UsersListPage clickLicenceDetailsButton(String arrow) {
        getPageUtils().waitForElementAndClick(By.cssSelector(String.format(".apriori-source-list-details-button [data-icon = 'angles-%s']", arrow)));
        return this;
    }

    /**
     * Checks if details panel is opened
     *
     * @param arrow - direction of button arrow
     * @return true or false
     */
    public boolean isDetailsPanelOpened(String arrow) {
        return getPageUtils().isElementPresent(By.cssSelector(String.format(".apriori-source-list-details-button [data-icon = 'angles-%s']", arrow)));
    }

    /**
     * Gets text of placeholder
     *
     * @return string
     */
    public String getDetailsText() {
        return getPageUtils().waitForElementToAppear(detailsPlaceholder).getAttribute("textContent");
    }

    /**
     * Gets the license details list.
     *
     * @return The license details list.
     */
    public SourceListComponent getLicenseDetailsList() {
        getPageUtils().waitForCondition(licenseDetailsList::isStable, getPageUtils().DURATION_LOADING);
        return licenseDetailsList;
    }

    /**
     * Checks is button is enabled
     *
     * @return true or false
     */
    public boolean isDeleteButtonEnable() {
        return getPageUtils().isElementEnabled(deleteUserButton);
    }

    /**
     * Clicks on delete button
     *
     * @return this page object
     */
    public UsersListPage clickDeleteButton() {
        getPageUtils().waitForElementAndClick(deleteUserButton);
        return this;
    }

    /**
     * Clicks on OK button of remove confirm dialog
     *
     * @return this object
     */
    public UsersListPage clickConfirmDeleteOkButton() {
        getPageUtils().waitForElementAndClick(confirmDeleteOkButton);
        return this;
    }

    /**
     * Clicks on Cancel button of remove confirm dialog
     *
     * @return this page object
     */
    public UsersListPage clickConfirmDeleteCancelButton() {
        getPageUtils().waitForElementAndClick(confirmDeleteCancelButton);
        return this;
    }

    /**
     * Clicks on trashcan icon
     *
     * @return this page object
     */
    public UsersListPage clickOnTrashcanIcon() {
        getPageUtils().waitForElementAndClick(trashcanIcon);
        return this;
    }
}
