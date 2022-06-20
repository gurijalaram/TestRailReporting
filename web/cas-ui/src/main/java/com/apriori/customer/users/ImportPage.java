package com.apriori.customer.users;

import com.apriori.common.UsersTableController;
import com.apriori.utils.FileImport;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.SourceListComponent;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ImportPage extends LoadableComponent<ImportPage> {

    private static final Logger logger = LoggerFactory.getLogger(ImportPage.class);

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

    @FindBy(xpath = "//button[@class='mr-2 btn btn-secondary'][.='Cancel']")
    private WebElement confirmRemoveCancelButton;

    @FindBy(xpath = "//button[@class='btn btn-primary'][.='OK']")
    private WebElement confirmRemoveOkButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private UsersTableController usersTableController;

    public ImportPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.usersTableController = new UsersTableController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(importListButton);
    }

    /**
     * Validates if batch file is displayed
     *
     * @param fileName - the file name
     * @return - true or false
     */
    public boolean isBatchFileDisplayed(String fileName) {
        return pageUtils.isElementDisplayed(By.xpath(String.format("//div[@class='line-item-body']//div[.='%s']", fileName)));
    }

    /**
     * Load users
     *
     * @return - current page object
     */
    public ImportPage loadUsers() {
        pageUtils.waitForElementAndClick(loadButton);
        return this;
    }

    /**
     * Refresh list
     *
     * @return - current page object
     */
    public ImportPage refreshUsersList() {
        pageUtils.waitForElementAndClick(refreshButton);
        return this;
    }

    /**
     * Refreshes batch file list
     *
     * @return - current page object
     */
    public ImportPage refreshBatchFilesList() {
        pageUtils.waitForElementAndClick(refreshBatchItemsButton);
        pageUtils.waitForElementToAppear(importTableHeader);
        return this;
    }

    /**
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver   - WebDriver
     * @param customer - Customer ID
     * @return ImportPage
     */
    public static ImportPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s/users/import";
        driver.navigate().to(String.format(url, customer));
        return new ImportPage(driver);
    }

    /**
     * Validates that table has a correct columns
     *
     * @param expectedName name of column
     * @param id id of column
     * @param soft soft assertions
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
        List<WebElement> fieldName = driver.findElements(By.xpath("//div[@class='batch-item-details']//label"));
        return fieldName.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Gets user identity
     *
     * @return string
     */
    public String getCardFieldValue(String fieldId) {
        WebElement field = driver.findElement(By.xpath(String.format("//label/following-sibling::div[@class='text-overflow read-field read-field-%s']", fieldId)));
        return field.getAttribute("textContent");
    }

    /**
     * Gets error message text
     *
     * @return string error message
     */
    public String getTextErrorMessage() {
        return pageUtils.waitForElementToAppear(modalError).getAttribute(("textContent"));
    }

    /**
     * Clicks on Remove button
     *
     * @return current page object
     */
    public ImportPage clickRemoveButton() {
        pageUtils.waitForElementAndClick(removeButton);
        return this;
    }

    /**
     * Clicks on Ok button of confirmation remove action
     *
     * @return this object
     */
    public ImportPage clickOkConfirmRemove(String fileName) {
        pageUtils.waitForElementAndClick(confirmRemoveOkButton);
        pageUtils.waitForElementsToNotAppear(By.xpath(String.format("//div[@class='line-item-body']//div[.='%s']", fileName)));
        return this;
    }

    /**
     * Clicks on Cancel button of confirmation remove action
     *
     * @return this object
     */
    public ImportPage clickCancelConfirmRemove() {
        pageUtils.waitForElementAndClick(confirmRemoveCancelButton);
        return this;
    }
}
