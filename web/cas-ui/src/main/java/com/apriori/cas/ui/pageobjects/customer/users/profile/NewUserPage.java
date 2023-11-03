package com.apriori.cas.ui.pageobjects.customer.users.profile;

import com.apriori.cas.ui.pageobjects.customer.users.UsersListPage;
import com.apriori.cas.ui.pageobjects.customeradmin.NavToolbar;
import com.apriori.web.app.util.PageUtils;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NewUserPage extends LoadableComponent<NewUserPage> {

    private static final Logger logger = LoggerFactory.getLogger(NewUserPage.class);

    @FindBy(css = "input[name='username']")
    private WebElement usernameInput;

    @FindBy(css = "input[name='email']")
    private WebElement emailInput;

    @FindBy(css = "input[type='checkbox']")
    private WebElement activeCheckbox;

    @FindBy(css = "input[name='userProfile.givenName']")
    private WebElement givenNameInput;

    @FindBy(css = "input[name='userProfile.familyName']")
    private WebElement familyNameInput;

    @FindBy(css = "input[name='userProfile.prefix']")
    private WebElement namePrefixInput;

    @FindBy(css = "input[name='userProfile.suffix']")
    private WebElement nameSuffixInput;

    @FindBy(css = "input[name='userProfile.jobTitle']")
    private WebElement jobTitleInput;

    @FindBy(css = "input[name='userProfile.department']")
    private WebElement deptInput;

    @FindBy(css = "input[name='userProfile.townCity']")
    private WebElement townCityInput;

    @FindBy(css = "select[name='stateProvince']")
    private WebElement stateProvDropdown;

    @FindBy(css = "input[name='userProfile.county']")
    private WebElement countyInput;

    @FindBy(css = "select[name='userProfile.countryCode']")
    private WebElement countyCodeDropdown;

    @FindBy(css = "select[name='userProfile.timezone']")
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

    @FindBy(linkText = "< Back to User List Page")
    private WebElement backToUsersListPage;

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
        //Empty due to missed loading process
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
     * @return current page object
     */
    public NewUserPage formFillNewUserDetails(String userName, String email, String givenName, String familyName) {
        inputUserName(userName)
            .inputEmail(email)
            .inputGivenName(givenName)
            .inputFamilyName(familyName);
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
    public NewUserPage inputUserName(String userName) {
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
    public NewUserPage inputEmail(String email) {
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
    public NewUserPage inputGivenName(String givenName) {
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
    public NewUserPage inputFamilyName(String familyName) {
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
    public NewUserPage inputNamePrefix(String namePrefix) {
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
    public <T> T save(Class<T> klass) {
        pageUtils.waitForElementAndClick(saveButton);
        return PageFactory.initElements(driver, klass);
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
     * Validates that appropriate field is displayed
     *
     * @param labelsToCheck - a list of fields names to check
     * @param soft - soft assertion
     * @return this object
     */
    public NewUserPage testNewUserLabelAvailable(List<String> labelsToCheck, SoftAssertions soft) {

        labelsToCheck.forEach(label -> {
            List<WebElement> elements = driver.findElements(By.xpath(String.format("//label[.='%s']", label)));
            soft.assertThat(elements.size())
                    .overridingErrorMessage(String.format("Could not find the label, %s", label))
                    .isGreaterThan(0);
        });

        return this;
    }

    /**
     * Gets error message for appropriate field
     *
     * @param label field name
     * @return
     */
    public String getFieldFeedback(String label) {
        return driver.findElement(By.className(String.format("invalid-feedback-for-user-profile-%s", label))).getAttribute("textContent");
    }

    /**
     * @param button button locator
     * @return true or false
     */
    private boolean isButtonEnabled(WebElement button) {
        return button != null && button.isEnabled();
    }

    /**
     * Can click the save button.
     *
     * @return Boolean representing can click save button
     */
    public boolean canSave() {
        return isButtonEnabled(saveButton);
    }
}
