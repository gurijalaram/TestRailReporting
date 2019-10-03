package com.apriori.pageobjects.pages.settings;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class SettingsPage extends LoadableComponent<SettingsPage> {

    private Logger logger = LoggerFactory.getLogger(SettingsPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "a[href='#tolerancePolicyTab']")
    private WebElement tolerancesButton;

    @FindBy(css = "select[data-ap-field='unitSystem']")
    private WebElement unitsDropdown;

    @FindBy(css = "select[data-ap-field='currency']")
    private WebElement currencyDropdown;

    @FindBy(css = ".btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = ".btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public SettingsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(dialogTitle);

    }

    /**
     * Opens tolerances tab
     *
     * @return new page object
     */
    public ToleranceSettingsPage openTolerancesTab() {
        pageUtils.waitForElementAndClick(tolerancesButton);
        return new ToleranceSettingsPage(driver);
    }

    /**
     * Changes system units
     *
     * @param units - the units
     * @return current page object
     */
    public SettingsPage changeDisplayUnits(String units) {
        pageUtils.selectDropdownOption(unitsDropdown, units);
        return this;
    }

    /**
     * Changes system currency
     *
     * @param currency - the currency
     * @return current page object
     */
    public SettingsPage changeCurrency(String currency) {
        pageUtils.selectDropdownOption(currencyDropdown, currency);
        return this;
    }

    /**
     * Selects the save button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return generic page object
     */
    public <T> T save(Class<T> className) {
        saveButton.click();
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return generic page object
     */
    public <T> T cancel(Class<T> className) {
        cancelButton.click();
        return PageFactory.initElements(driver, className);
    }
}