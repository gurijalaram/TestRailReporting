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

public class NewUserPage extends LoadableComponent<NewUserPage> {

    private final Logger logger = LoggerFactory.getLogger(NewUserPage.class);

    @FindBy(css = "input[name='username']")
    private WebElement usernameInput;

    @FindBy(css = "input[name='email']")
    private WebElement emailInput;

    @FindBy(css = "input[type='checkbox']")
    private WebElement activeCheckbox;

    @FindBy(css = "input[type='givenName']")
    private WebElement givenNameInput;

    @FindBy(css = "input[type='familyName']")
    private WebElement familyNameInput;

    @FindBy(css = "input[type='prefix']")
    private WebElement namePrefixInput;

    @FindBy(css = "input[type='suffix']")
    private WebElement nameSuffixInput;

    @FindBy(css = "input[type='jobTitle']")
    private WebElement jobTitleInput;

    @FindBy(css = "input[type='department']")
    private WebElement deptInput;

    @FindBy(css = "input[type='townCity']")
    private WebElement townCityInput;

    @FindBy(css = "select[name='stateProvince']")
    private WebElement stateProvDropdown;

    @FindBy(css = "input[type='county']")
    private WebElement countyInput;

    @FindBy(css = "select[name='countryCode']")
    private WebElement countyCodeDropdown;

    @FindBy(css = "select[name='timezone']")
    private WebElement timeZoneDropdown;

    @FindBy(css = "select[class='form-control form-control-sm']")
    private WebElement siteSelect;

    @FindBy(xpath = "//button[.='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[.='Edit']")
    private WebElement editButton;

    @FindBy(xpath = "//button[.='Reset MFA']")
    private WebElement resetMfaButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public NewUserPage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(usernameInput);
    }

    /**
     * Form fill mandatory fields
     * @param userName - user name
     * @param email - email
     * @param givenName - given name
     * @param familyName - family name
     * @param jobTitle - job title
     * @param countryCode - county code
     * @param timezone - time zone
     * @return current page object
     */
    public NewUserPage formFillNewUserDetails(String userName, String email, String givenName, String familyName, String jobTitle, String countryCode, String timezone) {
        inputUserName(userName)
            .inputEmail(email)
            .inputGivenName(givenName)
            .inputFamilyName(familyName)
            .inputJobTitle(jobTitle)
            .selectCountryCode(countryCode)
            .selectTimezone(timezone);
        return this;
    }

    /**
     * Form fill for all user details
     * @param userName - user name
     * @param email - email
     * @param givenName - given name
     * @param familyName - family name
     * @param namePrefix - name prefix
     * @param nameSuffix - name suffix
     * @param jobTitle - job title
     * @param department - department
     * @param townCity - town city
     * @param stateProv - state province
     * @param county - county
     * @param countryCode - country code
     * @param timezone - time zone
     * @return current page object
     */
    public NewUserPage formFillNewUserDetails(String userName, String email, String givenName, String familyName, String namePrefix, String nameSuffix, String jobTitle, String department,
                                              String townCity, String stateProv, String county, String countryCode, String timezone) {
        inputUserName(userName)
            .inputEmail(email)
            .inputGivenName(givenName)
            .inputFamilyName(familyName)
            .inputNamePrefix(namePrefix)
            .inputNameSuffix(nameSuffix)
            .inputJobTitle(jobTitle)
            .inputDepartment(department)
            .inputTownCity(townCity)
            .selectStateProv(stateProv)
            .inputCounty(county)
            .selectCountryCode(countryCode)
            .selectTimezone(timezone);
        return this;
    }

    /**
     * Enter user name
     *
     * @param userName - user name
     * @return current page object
     */
    private NewUserPage inputUserName(String userName) {
        pageUtils.waitForElementToAppear(usernameInput).clear();
        usernameInput.sendKeys(userName);
        return this;
    }

    /**
     * Enter email
     *
     * @param email - email
     * @return current page object
     */
    private NewUserPage inputEmail(String email) {
        pageUtils.waitForElementToAppear(emailInput).clear();
        emailInput.sendKeys(email);
        return this;
    }

    /**
     * Enter given name
     *
     * @param givenName - given name
     * @return current page object
     */
    private NewUserPage inputGivenName(String givenName) {
        pageUtils.waitForElementToAppear(givenNameInput).clear();
        givenNameInput.sendKeys(givenName);
        return this;
    }

    /**
     * Enter family name
     *
     * @param familyName - family name
     * @return current page object
     */
    private NewUserPage inputFamilyName(String familyName) {
        pageUtils.waitForElementToAppear(familyNameInput).clear();
        familyNameInput.sendKeys(familyName);
        return this;
    }

    /**
     * Enter name prefix
     *
     * @param namePrefix - name prefix
     * @return current page object
     */
    private NewUserPage inputNamePrefix(String namePrefix) {
        pageUtils.waitForElementToAppear(namePrefixInput).clear();
        namePrefixInput.sendKeys(namePrefix);
        return this;
    }

    /**
     * Enter name suffix
     *
     * @param nameSuffix - name suffix
     * @return current page object
     */
    private NewUserPage inputNameSuffix(String nameSuffix) {
        pageUtils.waitForElementToAppear(nameSuffixInput).clear();
        namePrefixInput.sendKeys(nameSuffix);
        return this;
    }

    /**
     * Enter job title
     *
     * @param jobTitle - job title
     * @return current page object
     */
    private NewUserPage inputJobTitle(String jobTitle) {
        pageUtils.waitForElementToAppear(jobTitleInput).clear();
        jobTitleInput.sendKeys(jobTitle);
        return this;
    }

    /**
     * Enter department
     *
     * @param department - department
     * @return current page object
     */
    private NewUserPage inputDepartment(String department) {
        pageUtils.waitForElementToAppear(deptInput).clear();
        deptInput.sendKeys(department);
        return this;
    }

    /**
     * Enter town city
     *
     * @param townCity - town city
     * @return current page object
     */
    private NewUserPage inputTownCity(String townCity) {
        pageUtils.waitForElementToAppear(townCityInput).clear();
        townCityInput.sendKeys(townCity);
        return this;
    }

    /**
     * Select state province
     *
     * @param stateProv - state province
     * @return current page object
     */
    private NewUserPage selectStateProv(String stateProv) {
        pageUtils.selectDropdownOption(stateProvDropdown, stateProv);
        return this;
    }

    /**
     * Enter county
     *
     * @param county - county
     * @return current page object
     */
    private NewUserPage inputCounty(String county) {
        pageUtils.waitForElementToAppear(countyInput).clear();
        countyInput.sendKeys(county);
        return this;
    }

    /**
     * Select country code
     *
     * @param countryCode - country code
     * @return current page object
     */
    private NewUserPage selectCountryCode(String countryCode) {
        pageUtils.selectDropdownOption(countyCodeDropdown, countryCode);
        return this;
    }

    /**
     * Select timezone
     *
     * @param timezone - timezone
     * @return current page object
     */
    private NewUserPage selectTimezone(String timezone) {
        pageUtils.selectDropdownOption(timeZoneDropdown, timezone);
        return this;
    }

    /**
     * Select the site
     *
     * @param site - the site
     * @return current page object
     */
    public NewUserPage selectSite(String site) {
        pageUtils.selectDropdownOption(siteSelect, site);
        return this;
    }

    /**
     * Cancels customer info
     *
     * @return new page object
     */
    public <T> T cancel(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Saves customer info
     *
     * @return new page object
     */
    public CustomerAdminPage save() {
        pageUtils.waitForElementAndClick(saveButton);
        return new CustomerAdminPage(driver);
    }

    /**
     * Edit customer info
     *
     * @return new page object
     */
    public NewUserPage edit() {
        pageUtils.waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Reset MFA
     *
     * @return new page object
     */
    public NewUserPage resetMfa() {
        pageUtils.waitForElementAndClick(resetMfaButton);
        return this;
    }
}
