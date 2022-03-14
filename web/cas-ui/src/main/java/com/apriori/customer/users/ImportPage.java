package com.apriori.customer.users;

import com.apriori.common.UsersTableController;
import com.apriori.customeradmin.NavToolbar;
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

    @FindBy(id = "batch-upload")
    private WebElement uploadCard;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(xpath = "//button[.='Load']")
    private WebElement loadButton;

    @FindBy(css = "button.apriori-source-list-refresh-button")
    private WebElement refreshButton;

    @FindBy(css = "div[class='pl-2'] [aria-label='Search']")
    private WebElement userNameSearch;

    @FindBy(css = ".apriori-source-list-layout-table")
    private WebElement usersTable;

    @FindBy(css = ".apriori-source-list-sub-title.ml-2")
    private WebElement importTableHeader;

    @FindBy(css = ".modal-body")
    private WebElement modalError;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;
    private FileImport fileImport;
    private UsersTableController usersTableController;

    public ImportPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        this.fileImport = new FileImport(driver);
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
        pageUtils.waitForElementToAppear(uploadCard);
        pageUtils.waitForElementAppear(loadButton);
    }

    /**
     * Import File
     *
     * @param filePath - the file path
     * @return current page object
     */
    public ImportPage importFile(File filePath) {
        fileImport.importFile(filePath);
        return this;
    }

    /**
     * Select card
     *
     * @param fileName - file name
     * @return current page object
     */
    public ImportPage selectCard(String fileName) {
        fileImport.selectCard(fileName);
        return this;
    }

    /**
     * Validates if license card is displayed
     *
     * @param fileName - the license name
     * @return - true or false
     */
    public boolean isCardDisplayed(String fileName) {
        return fileImport.isCardDisplayed(fileName);
    }

    /**
     * Search for user
     *
     * @param userName - user details
     * @return current page object
     */
    public ImportPage searchForUser(String userName) {
        pageUtils.waitForElementToAppear(userNameSearch).sendKeys(userName);
        return this;
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
    public ImportPage refreshList() {
        pageUtils.waitForElementAndClick(refreshButton);
        return this;
    }

    /**
     * Deletes csv file from import page
     *
     * @param fileName - name of csv file
     * @return - current page object
     */
    public ImportPage deleteCsvFile(String fileName) {
        By deleteCsvButton = By.xpath(String.format("//div[.='%s']//button[@title='Delete the csv file']", fileName));
        pageUtils.waitForElementAndClick(deleteCsvButton);
        pageUtils.waitForElementsToNotAppear(By.xpath(String.format("//div[@class='card-header']//div[.='%s']", fileName)));
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
     * Validates that table is pageable and refreshable
     *
     * @param soft soft assertions
     * @return current page object
     */
    public ImportPage validateImportTableArePageableAndRefreshable(SoftAssertions soft) {
        return usersTableController.validateUsersTableArePageableAndRefreshable(soft, ImportPage.class);
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
        List<WebElement> fieldName = driver.findElements(By.xpath("//div[@class='card-body']//span[@class='display-field-label']"));
        return fieldName.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Gets user identity
     *
     * @return string
     */
    public String getCardFieldValue(String fieldName) {
        WebElement field = driver.findElement(By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='display-field-value']", fieldName)));
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
}
