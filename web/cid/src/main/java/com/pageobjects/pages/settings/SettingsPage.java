package com.pageobjects.pages.settings;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
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

    private final Logger logger = LoggerFactory.getLogger(SettingsPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "a[href='#tolerancePolicyTab']")
    private WebElement tolerancesButton;

    @FindBy(css = "select[data-ap-field='unitVariantSettings']")
    private WebElement unitsDropdown;

    @FindBy(css = "select[data-ap-field='lenghtUnit']")
    private WebElement lengthDropdown;

    @FindBy(css = "select[data-ap-field='massUnit']")
    private WebElement massDropdown;

    @FindBy(css = "select[data-ap-field='timeUnit']")
    private WebElement timeDropdown;

    @FindBy(css = "select[data-ap-field='currency']")
    private WebElement currencyDropdown;

    @FindBy(css = "select[data-ap-field='decimalPlaces']")
    private WebElement decimalPlacesDropdown;

    @FindBy(css = "a[href='#selectionTab']")
    private WebElement selectionButton;

    @FindBy(css = "a[href='#prodInfoDefaults']")
    private WebElement prodDefaultButton;

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
     * Opens selection tab
     *
     * @return new page object
     */
    public SelectionSettingsPage openSelectionTab() {
        pageUtils.waitForElementAndClick(selectionButton);
        return new SelectionSettingsPage(driver);
    }

    /**
     * Opens production default tab
     *
     * @return new page object
     */
    public ProductionDefaultPage openProdDefaultTab() {
        pageUtils.waitForElementAndClick(prodDefaultButton);
        return new ProductionDefaultPage(driver);
    }

    /**
     * Changes system units
     *
     * @param units - the units
     * @return current page object
     */
    public SettingsPage selectUnits(String units) {
        pageUtils.selectDropdownOption(unitsDropdown, units);
        return this;
    }

    /**
     * Changes system selection
     * @param system - the metric system
     * @return current page object
     */
    public SettingsPage selectSystem(String system) {
        By systemSelection = By.xpath(String.format("//input[@data-ap-comp='%s']", system));
        pageUtils.waitForElementAndClick(systemSelection);
        return this;
    }

    /**
     * Selects the length
     * @param length - the length
     * @return current page object
     */
    public SettingsPage selectLength(String length) {
        pageUtils.selectDropdownOption(lengthDropdown, length);
        return this;
    }

    /**
     * Selects the mass
     * @param mass - the mass
     * @return current page object
     */
    public SettingsPage selectMass(String mass) {
        pageUtils.selectDropdownOption(massDropdown, mass);
        return this;
    }

    /**
     * Selects the time
     * @param time - the time
     * @return current page object
     */
    public SettingsPage selectTime(String time) {
        pageUtils.selectDropdownOption(timeDropdown, time);
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
     * Changes displayed no. of decimal places
     *
     * @param decimal - the no. of displayed decimal places
     * @return current page object
     */
    public SettingsPage changeDecimalPlaces(String decimal) {
        pageUtils.selectDropdownOption(decimalPlacesDropdown, decimal);
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
        pageUtils.waitForElementAndClick(saveButton);
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
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Gets the selected Units
     *
     * @return true/false
     */
    public boolean isSelectedMetricSystem(String system) {
        By systemSelection = By.xpath(String.format("//input[@data-ap-comp='%s']", system));
        return pageUtils.waitForElementToAppear(systemSelection).isSelected();
    }
}