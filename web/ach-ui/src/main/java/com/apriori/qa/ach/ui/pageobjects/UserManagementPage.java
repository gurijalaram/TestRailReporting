package com.apriori.qa.ach.ui.pageobjects;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManagementPage extends LoadableComponent<UserManagementPage> {

    @FindBy(css = "img[alt='Application Logo']")
    private WebElement logo;
    @FindBy(xpath = "//button[contains(@class,'add-user-button')]")
    private WebElement addUserButton;
    @FindBy(xpath = "//div[contains(@class,'searchable')]")
    private WebElement rolesDropDown;
    @FindBy(xpath = "//div[@class = 'enablements-additional-properties']")
    private WebElement additionalProperties;
    @FindBy(xpath = "//button[contains(.,'Next')]")
    private WebElement nextButton;
    @FindBy(xpath = "//input[@name ='username']")
    private WebElement username;
    @FindBy(xpath = "//input[@name ='email']")
    private WebElement email;
    @FindBy(xpath = "//input[@name ='userProfile.givenName']")
    private WebElement givenName;
    @FindBy(xpath = "//input[@name ='userProfile.familyName']")
    private WebElement familyName;
    @FindBy(xpath = "//div[@class = 'select-field select-field-user-profile-timezone form-group required']/div")
    private WebElement timezoneDropDown;
    @FindBy(xpath = "//div[@aria-label = 'Africa/Abidjan']")
    private WebElement chooseOptionFromTimezone;
    @FindBy(id = "finish-button")
    private WebElement finishButton;
    @FindBy(xpath = "//input[@name = 'search']")
    private WebElement searchInput;
    @FindBy(xpath = "//button[@data-testid = 'search-field-button']")
    private WebElement loopIconOnSearch;
    @FindBy(xpath = "//button[contains(@class,'menu-button')]")
    private WebElement threeDotsOnSearchedRow;
    @FindBy(xpath = "//div[contains(.,'Edit')][@class = 'line-item-body']")
    private WebElement editButtonOnTheRow;
    @FindBy(xpath = "//h2[contains(.,'Edit User Details')]")
    private WebElement editUserPageHeader;
    @FindBy(xpath = "//div[contains(.,'User Management')][@class = 'apriori-source-list-title mr-2']")
    private WebElement userManagementHeader;
    @FindBy(xpath = "//div[contains(@data-header-id,'username')][contains(@role,'cell')]")
    private WebElement searchedUsernameResult;
    @FindBy(xpath = "//div[contains(.,'Delete')][@class = 'line-item-body']")
    private WebElement deleteButton;
    @FindBy(xpath = "//button[contains(.,'Yes')]")
    private WebElement yesButtonOnConfirmationScreen;
    @FindBy(xpath = "//div[contains(.,'Inactive')][@aria-label = 'Inactive']")
    private WebElement userIsInactive;
    @FindBy(xpath = "//div[contains(.,'This user is not able to have their enablements edited')][@data-testid ='alert-messaging']")
    private WebElement cannotEditEnablaments;
    @FindBy(xpath = "//div[contains(@class,'apriori-select disabled')]")
    private WebElement disabledEnablments;


    private PageUtils pageUtils;
    private WebDriver driver;

    public UserManagementPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(logo);
    }

    /**
     * click on the add user button
     *
     * @return this page
     */

    public UserManagementPage clickAdduser() {
        pageUtils.waitForElementAndClick(addUserButton);
        return this;
    }

    /**
     * click on the dropdown for roles
     *
     * @return list of roles
     */
    public List<String> clickDropDownAndGetRoles() {
        pageUtils.waitForElementAndClick(rolesDropDown);
        List<WebElement> listOfRoles = driver.findElements(By.xpath("//div[contains(@class,'option-display-name')]"));
        return listOfRoles.stream().map(i -> i.getText()).collect(Collectors.toList());
    }

    /**
     * get all additional properties from check boxes
     *
     * @return list of string
     */
    public List<String> getAdditionalProperties() {
        List<String> list = new LinkedList<>(Arrays.asList(additionalProperties.getText().split("\n")));
        list.remove(0);
        return list;
    }

    /**
     * click on the dropdown for roles and choose a role
     *
     * @return this object
     */
    public UserManagementPage clickDropDownAndChooseRole(String role) {
        pageUtils.waitForElementAndClick(rolesDropDown);
        String xpath = "//div[contains(@class,'option-display-name')][contains(.,'" + role + "')]";
        pageUtils.waitForElementAndClick(By.xpath(xpath));
        return this;
    }

    /**
     * click on the Next button
     *
     * @return this object
     */
    public UserManagementPage clickNext() {
        pageUtils.waitForElementToBeClickable(nextButton);
        nextButton.click();
        return this;
    }

    /**
     * fill in all required information
     *
     * @return this object
     */
    public UserManagementPage fillInAllRequiredInfo(String username, String email) {
        pageUtils.waitForElementToBeClickable(this.username);
        this.username.sendKeys(username);
        this.email.sendKeys(email);
        this.givenName.sendKeys(username);
        this.familyName.sendKeys(username);
        pageUtils.waitForElementAndClick(timezoneDropDown);
        pageUtils.waitForElementAndClick(chooseOptionFromTimezone);
        return this;
    }


    /**
     * click finish button
     *
     * @return this object
     */
    public UserManagementPage clickFinishButton() {
        pageUtils.waitForElementAndClick(finishButton);
        return this;
    }
    /**
     * get username from result table (after performing search)
     * (it will only work for single result)
     *
     * @return string
     */

    public String getUsernameFromSearching() {
        pageUtils.waitForElementAppear(searchedUsernameResult);
        return searchedUsernameResult.getText();
    }

    /**
     * click on search form, paste a searching string, click on search and edit user
     *
     * @return this object
     */
    public UserManagementPage findUserAndClickEdit(String input) {
        pageUtils.waitForElementAndClick(searchInput);
        searchInput.sendKeys(input);
        pageUtils.waitForElementAndClick(loopIconOnSearch);
        pageUtils.waitForElementAndClick(threeDotsOnSearchedRow);
        pageUtils.waitForElementAndClick(editButtonOnTheRow);
        return this;
    }

    /**
     * click on three dots on user row
     *
     * @return this object
     */
    public UserManagementPage clickOnThreeDotsUserRowAndHitDelete() {
        pageUtils.waitForElementAndClick(threeDotsOnSearchedRow);
        pageUtils.waitForElementAndClick(deleteButton);
        pageUtils.waitForElementAndClick(yesButtonOnConfirmationScreen);
        return this;
    }

    /**
     * check if the user is inactive
     *
     * @return boolean
     */
    public boolean isUserInactive() {
        pageUtils.waitForElementAppear(userIsInactive);
        return pageUtils.isElementDisplayed(userIsInactive);
    }

    /**
     * click on search form, paste a searching string, click on search
     *
     * @return this object
     */
    public UserManagementPage findUser(String input) {
        pageUtils.waitForElementAndClick(searchInput);
        searchInput.sendKeys(input);
        pageUtils.waitForElementAndClick(loopIconOnSearch);
        return this;
    }

    /**
     * validate is Edit User Details header is displayed
     *
     * @return boolean
     */
    public boolean editUserHeaderIsDisplayed() {

        return pageUtils.isElementDisplayed(editUserPageHeader);
    }

    /**
     * On the second edit page change the data and click finish
     *
     * @return this
     */
    public UserManagementPage secondPageEditUserChangeDataClickFinish(List<String> nameOfProperty, List<String> valueForProperty) {

        for (int i = 0; i < nameOfProperty.size(); i++) {
            String property = nameOfProperty.get(i);
            String value = valueForProperty.get(i);
            String xpath = "//div[contains(.,'" + property + "')][@class = 'input-field vertical form-group']/div/input";
            WebElement header = pageUtils.waitForElementToBeClickable(By.xpath(xpath));
            pageUtils.waitForElementToBeClickable(header);
            header.click();
            header.clear();
            header.sendKeys(value);
        }

        return this;
    }

    /**
     * check logo
     *
     * @return list of roles
     */
    public boolean ifOnUserManagementPage() {
        return pageUtils.isElementDisplayed(userManagementHeader);
    }

    /**
     * check user see warning info
     * return boolean
     */
    public boolean isInfoCannotEditIsVisible() {
        pageUtils.waitForElementAppear(cannotEditEnablaments);
        return cannotEditEnablaments.isDisplayed();
    }

    /**
     * check user is disabling for editing enablements
     * return boolean
     */
    public boolean isDisabledToEditEnablements() {
        pageUtils.waitForElementAppear(disabledEnablments);
        return disabledEnablments.isDisplayed();
    }
}
