package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ToleranceDefaultsPage extends LoadableComponent<ToleranceDefaultsPage> {

    @FindBy(xpath = "//button[.='Tolerance Defaults']")
    private WebElement toleranceTab;

    @FindBy(css = "[value='SYSTEMDEFAULT']")
    private WebElement systemRadioButton;

    @FindBy(css = "[value='PARTOVERRIDE']")
    private WebElement specificRadioButton;

    @FindBy(css = ".tolerance-defaults [data-icon='pencil']")
    private WebElement specificPencilButton;

    @FindBy(css = "[value='CAD']")
    private WebElement cadRadioButton;

    @FindBy(css = "[id='qa-tolerance-defaults-use-cad-tolerance-threshold'] .checkbox-icon")
    private WebElement replaceValuesCheckbox;

    @FindBy(css = "[name='tolerance.minCadToleranceThreshold']")
    private WebElement minCadInput;

    @FindBy(css = "[name='tolerance.cadToleranceReplacement']")
    private WebElement cadToleranceInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SettingsNavigation settingsNavigation;

    public ToleranceDefaultsPage(WebDriver driver) {
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
        assertTrue("Tolerance tab was not selected", toleranceTab.getAttribute("class").contains("active"));
    }

    /**
     * Go to display default tab
     *
     * @return new page object
     */
    public DisplayPreferencesPage goToDisplayTab() {
        return settingsNavigation.goToDisplayTab();
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
     * Go to selection tab
     *
     * @return new page object
     */
    public SelectionPage goToSelectionTab() {
        return settingsNavigation.goToSelectionTab();
    }

    /**
     * Select system default
     *
     * @return current page object
     */
    public ToleranceDefaultsPage selectSystemDefault() {
        pageUtils.waitForElementAndClick(systemRadioButton);
        return this;
    }

    /**
     * Select specific value
     *
     * @return current page object
     */
    public ToleranceDefaultsPage selectSpecificValues() {
        pageUtils.waitForElementAndClick(specificRadioButton);
        return this;
    }

    /**
     * Edit specific values
     *
     * @return new page object
     */
    public ToleranceOverridesPage editSpecificValues() {
        pageUtils.waitForElementAndClick(specificPencilButton);
        return new ToleranceOverridesPage(driver);
    }

    /**
     * Select cad
     *
     * @return current page object
     */
    public ToleranceDefaultsPage selectCad() {
        pageUtils.waitForElementAndClick(cadRadioButton);
        return this;
    }

    /**
     * Replace current values
     *
     * @param cadValue       - the cad value
     * @param toleranceValue - the cad tolerance
     * @return current page object
     */
    public ToleranceDefaultsPage replaceValues(String cadValue, String toleranceValue) {
        pageUtils.waitForElementAndClick(replaceValuesCheckbox);
        return this;
    }

    /**
     * Input cad value
     *
     * @param cadValue - the cad value
     * @return current page object
     */
    public ToleranceDefaultsPage inputCadValue(String cadValue) {
        pageUtils.clearInput(minCadInput);
        minCadInput.sendKeys(cadValue);
        return this;
    }

    /**
     * Get cad value
     *
     * @return double
     */
    public double getCadValue() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(minCadInput).getAttribute("value"));
    }

    /**
     * Input cad tolerance
     *
     * @param toleranceValue - the cad tolerance
     * @return current page object
     */
    public ToleranceDefaultsPage inputCadTolerance(String toleranceValue) {
        pageUtils.clearInput(cadToleranceInput);
        cadToleranceInput.sendKeys(toleranceValue);
        return this;
    }

    /**
     * Get cad tolerance
     *
     * @return double
     */
    public double getCadTolerance() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(cadToleranceInput).getAttribute("value"));
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
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
