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

    private PageUtils pageUtils;

    public UserPreferencePage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;

    private UserPreferenceController userPreferenceController;

    public UserPreferencePage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.waitForMessagePageLoad();
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
        return pageUtils.isElementDisplayed(preferencesModal);
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
}