package com.apriori.customer.users;

import com.apriori.common.UsersTableController;
import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.FileImport;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;

import com.apriori.utils.web.components.SourceListComponent;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ImportPage extends LoadableComponent<ImportPage> {

    private static final Logger logger = LoggerFactory.getLogger(ImportPage.class);

    @FindBy(id = "batch-upload")
    private WebElement uploadCard;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    @FindBy(xpath = "//button[.='Load']")
    private WebElement loadButton;

    @FindBy(xpath = "//button[.='Refresh']")
    private WebElement refreshButton;

    @FindBy(css = "div[class='pl-2'] [aria-label='Search']")
    private WebElement userNameSearch;

    @FindBy(xpath = "//div[.='testusers.csv']//button[@title='Delete the csv file']")
    private WebElement deleteCsvFile;

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
     * Load license
     *
     * @return - current page object
     */
    public ImportPage loadLicense() {
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
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver - WebDriver
     * @param customer - Customer ID
     * @return ImportPage
     */
    public static ImportPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s/users/import";
        driver.navigate().to(String.format(url, customer));
        return new ImportPage(driver);
    }

    public ImportPage validateImportTableArePageableAndRefreshable(SoftAssertions soft) {
        return usersTableController.validateUsersTableArePageableAndRefreshable(soft, ImportPage.class);
    }

    public ImportPage validateImportUsersTableHasCorrectColumns(String expectedName, String id, SoftAssertions soft) {
        return usersTableController.validateUsersTableHasCorrectColumns(expectedName, id, soft, ImportPage.class);
    }

    public SourceListComponent getUsersList() {
        return usersTableController.getUsersTable();
    }
}
