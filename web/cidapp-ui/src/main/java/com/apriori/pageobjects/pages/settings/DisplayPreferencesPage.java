package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.UnitsEnum;

import com.utils.CurrencyEnum;
import com.utils.DecimalPlaceEnum;
import com.utils.LengthEnum;
import com.utils.MassEnum;
import com.utils.TimeEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class DisplayPreferencesPage extends LoadableComponent<DisplayPreferencesPage> {

    @FindBy(css = ".user-preferences [type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[.='Display Preferences']")
    private WebElement displayTab;

    @FindBy(xpath = "//button[.='Assembly Defaults']")
    private WebElement assemblyDefaultsTab;

    @FindBy(css = "[id='qa-preferences-unit-group-select'] .apriori-select")
    private WebElement unitsDropdown;

    @FindBy(css = "[id='qa-preferences-length-select'] .apriori-select")
    private WebElement lengthDropdown;

    @FindBy(css = "[id='qa-preferences-mass-select'] .apriori-select")
    private WebElement massDropdown;

    @FindBy(css = "[id='qa-preferences-time-select'] .apriori-select")
    private WebElement timeDropdown;

    @FindBy(css = "[id='qa-preferences-decimal-select'] .apriori-select")
    private WebElement decimalDropdown;

    @FindBy(css = "[id='qa-preferences-language-select'] .apriori-select")
    private WebElement languageDropdown;

    @FindBy(css = "[id='qa-preferences-currency-select'] .apriori-select")
    private WebElement currencyDropdown;

    @FindBy(css = ".exchange-rate-table-select .apriori-select")
    private WebElement ertDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SettingsNavigation settingsNavigation;
    private String root = "modal-body";

    public DisplayPreferencesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.settingsNavigation = new SettingsNavigation(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Display tab was not selected", displayTab.getAttribute("class").contains("active"));
    }

    /**
     * Go to selection tab
     *
     * @return new page object
     */
    public SelectionPage goToSelectionTab() {
        return settingsNavigation.goToSelectionTab();
    }

    /**
     * Go to production default tab
     *
     * @return new page object
     */
    public ProductionDefaultsPage goToProductionTab() {
        return settingsNavigation.goToProductionTab();
    }

    /**
     * Go to Multi-Body default tab
     *
     * @return new page object
     */
    public AssemblyDefaultsPage goToMultiBodyTab() {
        return settingsNavigation.goToAssemblyDefaultsTab();
    }

    /**
     * Go to tolerances default tab
     *
     * @return new page object
     */
    public ToleranceDefaultsPage goToToleranceTab() {
        return settingsNavigation.goToToleranceTab();
    }

    /**
     * Selects units
     *
     * @param units - the units
     * @return current page object
     */
    public DisplayPreferencesPage selectUnits(UnitsEnum units) {
        pageUtils.typeAheadSelect(unitsDropdown, root, units.getUnits());
        return this;
    }

    /**
     * Selects the length
     *
     * @param length - the length
     * @return current page object
     */
    public DisplayPreferencesPage selectLength(LengthEnum length) {
        pageUtils.typeAheadSelect(lengthDropdown, root, length.getLength());
        return this;
    }

    /**
     * Selects the mass
     *
     * @param mass - the mass
     * @return current page object
     */
    public DisplayPreferencesPage selectMass(MassEnum mass) {
        pageUtils.typeAheadSelect(massDropdown, root, mass.getMass());
        return this;
    }

    /**
     * Selects the time
     *
     * @param time - the time
     * @return current page object
     */
    public DisplayPreferencesPage selectTime(TimeEnum time) {
        pageUtils.typeAheadSelect(timeDropdown, root, time.getTime());
        return this;
    }

    /**
     * Selects the decimal places
     *
     * @param decimalPlaces - the decimal places
     * @return current page object
     */
    public DisplayPreferencesPage selectDecimalPlaces(DecimalPlaceEnum decimalPlaces) {
        pageUtils.typeAheadSelect(decimalDropdown, root, decimalPlaces.getDecimalPlaces());
        return this;
    }

    /**
     * Selects the language
     *
     * @param language - the language
     * @return current page object
     */
    public DisplayPreferencesPage selectLanguage(String language) {
        pageUtils.typeAheadSelect(languageDropdown, root, language);
        return this;
    }

    /**
     * Selects the currency
     *
     * @param currency - the currency
     * @return current page object
     */
    public DisplayPreferencesPage selectCurrency(CurrencyEnum currency) {
        pageUtils.typeAheadSelect(currencyDropdown, root, currency.getCurrency());
        return this;
    }

    /**
     * Selects the ERT
     *
     * @param ert - the ert
     * @return current page object
     */
    public DisplayPreferencesPage selectERT(String ert) {
        pageUtils.typeAheadSelect(ertDropdown, root, ert);
        return this;
    }

    /**
     * Gets the selected process group
     *
     * @return process group as String
     */
    public String getUnits() {
        return pageUtils.waitForElementToAppear(unitsDropdown).getAttribute("textContent");
    }

    /**
     * Gets the selected length
     *
     * @return length as String
     */
    public String getLength() {
        return pageUtils.waitForElementToAppear(lengthDropdown).getAttribute("textContent");
    }

    /**
     * Gets the selected mass
     *
     * @return mass as String
     */
    public String getMass() {
        return pageUtils.waitForElementToAppear(massDropdown).getAttribute("textContent");
    }

    /**
     * Gets the selected time
     *
     * @return time as String
     */
    public String getTime() {
        return pageUtils.waitForElementToAppear(timeDropdown).getAttribute("textContent");
    }

    /**
     * Gets the selected decimal places
     *
     * @return decimal places as String
     */
    public String getDecimalPlaces() {
        return pageUtils.waitForElementToAppear(decimalDropdown).getAttribute("textContent");
    }

    /**
     * Gets the selected language
     *
     * @return language as String
     */
    public String getLanguage() {
        return pageUtils.waitForElementToAppear(languageDropdown).getAttribute("textContent");
    }

    /**
     * Gets the selected currency
     *
     * @return currency as String
     */
    public String getCurrency() {
        return pageUtils.waitForElementToAppear(currencyDropdown).getAttribute("textContent");
    }

    /**
     * Gets the selected exchange rate table
     *
     * @return ert as String
     */
    public String getERT() {
        return pageUtils.waitForElementToAppear(ertDropdown).getAttribute("textContent");
    }

    /**
     * Set system selection
     *
     * @param system - the metric system
     * @return current page object
     */
    public DisplayPreferencesPage setSystem(String system) {
        By bySystem = getSystem(system);
        pageUtils.waitForElementAndClick(bySystem);
        return this;
    }

    /**
     * Checks if system is checked
     *
     * @param system - the metric system
     * @return boolean
     */
    public boolean isSystemChecked(String system) {
        By bySystem = getSystem(system);
        return !pageUtils.waitForElementToAppear(bySystem).getAttribute("checked").contains("null");
    }

    /**
     * Get system By
     *
     * @param system - the system
     * @return by
     */
    private By getSystem(String system) {
        return By.cssSelector(String.format("input[value='%s']", system.toUpperCase()));
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public EvaluatePage cancel() {
        return modalDialogController.cancel(EvaluatePage.class);
    }
}
