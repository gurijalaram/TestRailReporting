package com.apriori.siteLicenses;

import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.FileImport;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class SitesLicensesPage extends LoadableComponent<SitesLicensesPage> {

    private static final Logger logger = LoggerFactory.getLogger(SitesLicensesPage.class);

    @FindBy(xpath = "//h5[.='Loaded Licenses']")
    private WebElement licenseHeader;

    @FindBy(css = "ul[class='list-group list-group-flush'] li")
    private List<WebElement> submodules;

    @FindBy(className = "Toastify__toast-container")
    private WebElement errorMessageContainer;

    @FindBy(css = "[class='Toastify__toast-body']")
    private WebElement toastify;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;
    private FileImport fileImport;

    public SitesLicensesPage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(licenseHeader);
    }

    /**
     * Import File
     *
     * @param filePath - the file path
     * @return current page object
     */
    public SitesLicensesPage importFile(File filePath) {
        fileImport.importFile(filePath);
        return this;
    }

    /**
     * Select card
     *
     * @param fileName - file name
     * @return current page object
     */
    public SitesLicensesPage selectCard(String fileName) {
        fileImport.selectCard(fileName);
        return this;
    }

    /**
     * Select the sub-license name
     *
     * @param licenseName - license name
     * @return current page object
     */
    public SitesLicensesPage selectSubLicense(String licenseName) {
        By license = By.xpath(String.format("//td[.='%s']", licenseName));
        pageUtils.waitForElementAndClick(license);
        return this;
    }

    /**
     * Get list of sub modules
     *
     * @return list
     */
    public List<String> getListOfSubModules() {
        return submodules.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver   - WebDriver
     * @param customer - Customer ID
     * @return SitesLicensesPage
     */
    public static SitesLicensesPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s/sites-and-licenses";
        driver.navigate().to(String.format(url, customer));
        return new SitesLicensesPage(driver);
    }

    /**
     * Uploads license file
     *
     * @param licenseFile license file
     * @param customerName name of customer
     * @param siteId site ID
     * @param subLicenseId SubLicense ID
     * @param klass class
     * @return new page object
     */
    public <T> T uploadLicense(String licenseFile, String customerName, String siteId, String subLicenseId, Class<T> klass) {
        InputStream license = new ByteArrayInputStream(String.format(licenseFile, customerName, siteId, subLicenseId, subLicenseId).getBytes(StandardCharsets.UTF_8));
        fileImport.importFile(FileResourceUtil.copyIntoTempFile(license, "license", "licenseTest.xml"));
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Gets error message
     *
     * @return string error message
     */
    public String getErrorMessage() {
        String message = pageUtils.waitForElementToAppear(toastify).getAttribute("textContent");
        pageUtils.waitForElementsToNotAppear(By.cssSelector("[class='Toastify__toast-body']"));
        pageUtils.clearInput(driver.findElement(By.cssSelector("input[type='file']")));
        return message;
    }

    /**
     * Validates if license card is displayed
     *
     * @param licenseName - the license name
     * @return - true or false
     */
    public boolean isLicenseCardDisplayed(String licenseName) {
        return fileImport.isCardDisplayed(licenseName);
    }
}
