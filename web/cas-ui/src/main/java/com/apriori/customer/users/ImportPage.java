package com.apriori.customer.users;

import com.apriori.common.UsersTableController;
import com.apriori.components.SourceListComponent;
import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ImportPage extends UsersPage {

    @FindBy(css = ".batch-upload-button.btn.btn-light")
    private WebElement importListButton;

    @FindBy(css = ".left-tree-refresh-button")
    private WebElement refreshBatchItemsButton;

    @FindBy(xpath = "//button[.='Load']")
    private WebElement loadButton;

    @FindBy(css = ".batch-list-remove-button span")
    private WebElement removeButton;

    @FindBy(css = "button.apriori-source-list-refresh-button")
    private WebElement refreshButton;

    @FindBy(css = ".apriori-source-list-layout-table")
    private WebElement usersTable;

    @FindBy(css = ".apriori-source-list-sub-title.ml-2")
    private WebElement importTableHeader;

    @FindBy(css = ".modal-body")
    private WebElement modalError;

    @FindBy(css = ".MuiDialogActions-spacing [data-testid='secondary-button']")
    private WebElement confirmRemoveCancelButton;

    @FindBy(xpath = "//button[.='Ok']")
    private WebElement confirmRemoveOkButton;

    private UsersTableController usersTableController;

    public ImportPage(WebDriver driver) {
        super(driver);
        usersTableController = new UsersTableController(driver);
    }

    /**
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver   - WebDriver
     * @param customer - Customer ID
     * @return ImportPage
     */
    public static ImportPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("cas.ui_url") + "customers/%s/users/import";
        driver.navigate().to(String.format(url, customer));
        return new ImportPage(driver);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(importListButton);
    }

    /**
     * Validates if batch file is displayed
     *
     * @param fileName - the file name
     * @return - true or false
     */
    public boolean isBatchFileDisplayed(String fileName) {
        return getPageUtils().isElementDisplayed(By.xpath(String.format("//div[@class='line-item-body']//div[.='%s']", fileName)));
    }

    /**
     * Load users
     *
     * @return - current page object
     */
    public ImportPage loadUsers() {
        getPageUtils().waitForElementAndClick(loadButton);
        return this;
    }

    /**
     * Refresh list
     *
     * @return - current page object
     */
    public ImportPage refreshUsersList() {
        getPageUtils().waitForElementAndClick(refreshButton);
        return this;
    }

    /**
     * Refreshes batch file list
     *
     * @return - current page object
     */
    public ImportPage refreshBatchFilesList() {
        getPageUtils().waitForElementAndClick(refreshBatchItemsButton);
        getPageUtils().waitForElementToAppear(importTableHeader);
        return this;
    }

    /**
     * Validates that table has a correct columns
     *
     * @param expectedName name of column
     * @param id           id of column
     * @param soft         soft assertions
     * @return This object
     */
    public ImportPage validateImportUsersTableHasCorrectColumns(String expectedName, String id, SoftAssertions soft) {
        return usersTableController.validateUsersTableHasCorrectColumns(expectedName, id, soft, ImportPage.class);
    }

    /**
     * Gets the users list.
     *
     * @return users list
     */
    public SourceListComponent getUsersList() {
        return usersTableController.getUsersTable();
    }

    /**
     * Checks if button is enabled
     *
     * @param button - name of button
     * @return - true or false
     */
    private boolean isButtonEnabled(WebElement button) {
        return button != null && button.isEnabled();
    }

    /**
     * Can click the save button.
     *
     * @return Boolean representing can click save button
     */
    public boolean canLoad() {
        return isButtonEnabled(loadButton);
    }

    /**
     * Gets the list of fields names
     *
     * @return list of fields names
     */
    public List<String> getFieldName() {
        List<WebElement> fieldName = getDriver().findElements(By.xpath("//div[@class='batch-item-details']//label"));
        return fieldName.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Gets user identity
     *
     * @return string
     */
    public String getCardFieldValue(String fieldId) {
        WebElement field = getDriver().findElement(By.xpath(String.format("//label/following-sibling::div[@class='text-overflow read-field read-field-%s']", fieldId)));
        return field.getAttribute("textContent");
    }

    /**
     * Gets error message text
     *
     * @return string error message
     */
    public String getTextErrorMessage() {
        return getPageUtils().waitForElementToAppear(modalError).getAttribute(("textContent"));
    }

    /**
     * Clicks on Remove button
     *
     * @return current page object
     */
    public ImportPage clickRemoveButton() {
        getPageUtils().waitForElementAndClick(removeButton);
        return this;
    }

    /**
     * Clicks on Ok button of confirmation remove action
     *
     * @return this object
     */
    public ImportPage clickOkConfirmRemove(String fileName) {
        getPageUtils().waitForElementAndClick(confirmRemoveOkButton);
        getPageUtils().waitForElementsToNotAppear(By.xpath(String.format("//div[@class='line-item-body']//div[.='%s']", fileName)));
        return this;
    }

    /**
     * Clicks on Cancel button of confirmation remove action
     *
     * @return this object
     */
    public ImportPage clickCancelConfirmRemove() {
        getPageUtils().waitForElementAndClick(confirmRemoveCancelButton);
        return this;
    }
}
