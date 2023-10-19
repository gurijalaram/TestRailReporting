package com.apriori.qa.ach.ui.pageobjects;

import com.apriori.PageUtils;

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
    @FindBy(xpath = "//div[contains(.,'User Management')][@class = 'apriori-source-list-title mr-2']")
    private WebElement userManagementHeader;

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
     * @return this object
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
        String xpath = "//div[contains(@class,'option-display-name')][contains(.,'APRIORI ANALYST')]";
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
    public UserManagementPage fillInAllRequiredInfo(String username, String email, String givenName, String familyName) {
        pageUtils.waitForElementToBeClickable(this.username);
        this.username.sendKeys(username);
        this.email.sendKeys(email);
        this.givenName.sendKeys(givenName);
        this.familyName.sendKeys(familyName);
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
     * check logo
     *
     * @return list of roles
     */
    public boolean ifOnUserManagementPage() {
        return pageUtils.isElementDisplayed(userManagementHeader);
    }


}
