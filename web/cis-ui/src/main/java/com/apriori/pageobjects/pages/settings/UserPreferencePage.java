package com.apriori.pageobjects.pages.settings;

import com.apriori.pageobjects.common.UserPreferenceController;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public class UserPreferencePage extends EagerPageComponent<UserPreferencePage> {

    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    @FindBy(xpath = "//div[@role='presentation']//p[text()='Preferences']")
    private WebElement preferencesModal;

    @FindBy(id = "preferences-cancel-btn")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[@data-testid='select-control-display.lengthUnits']")
    private WebElement lengthField;

    @FindBy(xpath = "//div[@data-testid='select-control-display.massUnits']")
    private WebElement massField;

    @FindBy(xpath = "//div[@data-testid='select-control-display.timeUnits']")
    private WebElement timeField;

    @FindBy(xpath = "//div[@data-testid='select-control-display.decimalPlaces']")
    private WebElement decimalPlaceField;

    @FindBy(id = "preferences-submit-btn")
    private WebElement btnSubmit;

    private PageUtils pageUtils;

    public UserPreferencePage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;

    private UserPreferenceController userPreferenceController;

    public UserPreferencePage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.userPreferenceController = new UserPreferenceController(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Method to wait message page loads
     */
    public void waitForMessagePageLoad() {
        getPageUtils().waitForElementToAppear(spinner);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
    }

    /**
     * Checks if preferences modal displayed
     *
     * @return true/false
     */
    public boolean isPreferencesModalDisplayed() {
        return getPageUtils().isElementDisplayed(preferencesModal);
    }

    /**
     * Get user preference items
     *
     * @return list of string
     */
    public List<String> getUserPreferenceItems() {
        return userPreferenceController.getUserPreferenceItems();
    }

    /**
     * Click close modal icon
     *
     * @return current object
     */
    public UserPreferencePage clickCancelButton() {
        getPageUtils().waitForElementAndClick(cancelButton);
        return this;
    }

    /**
     * Get display preference fields
     *
     * @return list of string
     */
    public List<String> getDisplayPreferenceFields() {
        return userPreferenceController.getUserPreferenceItems();
    }

    /**
     * select length
     *
     * @return list of string
     */
    public UserPreferencePage selectLength(String length) {
        getPageUtils().waitForElementAndClick(lengthField);
        getPageUtils().waitForElementAndClick(By.xpath(String.format("//li[@role='option']//span[text()='%s']", length)));
        return this;
    }

    /**
     * select mass
     *
     * @return list of string
     */
    public UserPreferencePage selectMass(String mass) {
        getPageUtils().waitForElementAndClick(massField);
        getPageUtils().waitForElementAndClick(By.xpath(String.format("//li[@role='option']//span[text()='%s']", mass)));
        return this;
    }

    /**
     * select time
     *
     * @return list of string
     */
    public UserPreferencePage selectTime(String time) {
        getPageUtils().waitForElementAndClick(timeField);
        getPageUtils().waitForElementAndClick(By.xpath(String.format("//li[@role='option']//span[text()='%s']", time)));
        return this;
    }

    /**
     * select decimal place
     *
     * @return list of string
     */
    public UserPreferencePage selectDecimalPlace(String decimalPlace) {
        getPageUtils().waitForElementAndClick(decimalPlaceField);
        getPageUtils().waitForElementAndClick(By.xpath(String.format("//li[@role='option']//span[text()='%s']", decimalPlace)));
        return this;
    }

    /**
     * Checks if submit button displayed
     *
     * @return true/false
     */
    public boolean isSubmitButtonDisplayed() {
        return getPageUtils().isElementDisplayed(btnSubmit);
    }

    /**
     * get submit button status
     *
     * @return true/false
     */
    public String getSubmitButtonState() {
        return getPageUtils().waitForElementToAppear(btnSubmit).getAttribute("class");
    }

    /**
     * Click submit button
     *
     * @return current object
     */
    public UserPreferencePage clickSubmitButton() {
        getPageUtils().waitForElementAndClick(btnSubmit);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@role='presentation']//p[text()='Preferences']"));
        return this;
    }
}