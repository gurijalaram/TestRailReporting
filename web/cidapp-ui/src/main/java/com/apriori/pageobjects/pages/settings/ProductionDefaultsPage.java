package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ProductionDefaultsPage extends LoadableComponent<ProductionDefaultsPage> {

    @FindBy(css = ".user-preferences [type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[.='Production Defaults']")
    private WebElement productionsTab;

    @FindBy(css = ".production-defaults")
    private WebElement productionDefaults;

    @FindBy(css = "[name='production.defaultScenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "[id='qa-production-defaults-process-group-select'] .apriori-select")
    private WebElement processGroupDropdown;

    @FindBy(css = "[id='qa-production-defaults-digital-factory-select'] .apriori-select")
    private WebElement digitalFactoryDropdown;

    @FindBy(css = "[id='qa-production-defaults-material-catalog-select'] .apriori-select")
    private WebElement materialCatalogDropdown;

    @FindBy(css = "[id='qa-material-modal-select-field'] .modal-select-content")
    private WebElement materialInput;

    @FindBy(css = "[id='qa-material-modal-select-field'] [data-icon='pencil']")
    private WebElement materialPencil;

    @FindBy(css = "[name='production.defaultAnnualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = "[name='production.defaultProductionLife']")
    private WebElement yearsInput;

    @FindBy(css = "[name='production.defaultBatchSize']")
    private WebElement batchSizeInput;

    @FindBy(css = ".invalid-feedback-for-production-default-batch-size")
    private WebElement errorMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SettingsNavigation settingsNavigation;

    public ProductionDefaultsPage(WebDriver driver) {
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
        assertTrue("Production Defaults tab is not active", productionsTab.getAttribute("class").contains("active"));
        assertTrue("Production Defaults is not displayed", productionDefaults.isDisplayed());
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
     * Go to display default tab
     *
     * @return new page object
     */
    public DisplayPreferencesPage goToDisplayTab() {
        return settingsNavigation.goToDisplayTab();
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
     * Input scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ProductionDefaultsPage inputScenarioName(String scenarioName) {
        pageUtils.clearInput(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Select process group
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public ProductionDefaultsPage selectProcessGroup(ProcessGroupEnum processGroup) {
        pageUtils.modalTypeAheadSelect(processGroupDropdown, "Process Group", processGroup.getProcessGroup());
        return this;
    }

    /**
     * Select digital factory
     *
     * @param digitalFactory - the digital factory
     * @return current page object
     */
    public ProductionDefaultsPage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        pageUtils.modalTypeAheadSelect(digitalFactoryDropdown, "Digital Factory", digitalFactory.getDigitalFactory());
        return this;
    }

    /**
     * Select material catalog
     *
     * @param materialCatalog - the material catalog
     * @return current page object
     */
    public ProductionDefaultsPage selectMaterialCatalog(DigitalFactoryEnum materialCatalog) {
        pageUtils.modalTypeAheadSelect(materialCatalogDropdown, "Material Catalog", materialCatalog.getDigitalFactory());
        return this;
    }

    /**
     * Opens the material selector table
     *
     * @return new page object
     */
    public MaterialSelectorPage openMaterialSelectorTable() {
        pageUtils.waitForElementAndClick(materialPencil);
        return new MaterialSelectorPage(driver);
    }

    /**
     * Input annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public ProductionDefaultsPage inputAnnualVolume(String annualVolume) {
        pageUtils.clearValueOfElement(annualVolumeInput);
        annualVolumeInput.sendKeys(annualVolume);
        return this;
    }

    /**
     * Input years
     *
     * @param years - the years
     * @return current page object
     */
    public ProductionDefaultsPage inputYears(String years) {
        pageUtils.clearValueOfElement(yearsInput);
        yearsInput.sendKeys(years);
        return this;
    }

    /**
     * Input batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public ProductionDefaultsPage inputBatchSize(String batchSize) {
        pageUtils.clearValueOfElement(batchSizeInput);
        batchSizeInput.sendKeys(batchSize);
        return this;
    }

    /**
     * Get scenario name
     *
     * @return string
     */
    public String getScenarioName() {
        return pageUtils.waitForElementToAppear(scenarioNameInput).getAttribute("value");
    }

    /**
     * Gets the selected process group
     *
     * @return process group as String
     */
    public String getProcessGroup() {
        return pageUtils.waitForElementToAppear(processGroupDropdown).getAttribute("textContent");
    }

    /**
     * Get digital factory
     *
     * @return string
     */
    public String getDigitalFactory() {
        return pageUtils.waitForElementToAppear(digitalFactoryDropdown).getAttribute("textContent");
    }

    /**
     * Get material catalog
     *
     * @return string
     */
    public String getMaterialCatalog() {
        return pageUtils.waitForElementToAppear(materialCatalogDropdown).getAttribute("textContent");
    }

    /**
     * Get material
     *
     * @return string
     */
    public String getMaterial() {
        return pageUtils.waitForElementToAppear(materialInput).getAttribute("textContent");
    }

    /**
     * Get annual volume
     *
     * @return string
     */
    public String getAnnualVolume() {
        return pageUtils.waitForElementToAppear(annualVolumeInput).getAttribute("value");
    }

    /**
     * Get years
     *
     * @return string
     */
    public String getYears() {
        return pageUtils.waitForElementToAppear(yearsInput).getAttribute("value");
    }

    /**
     * Get batch size
     *
     * @return string
     */
    public String getBatchSize() {
        return pageUtils.waitForElementToAppear(batchSizeInput).getAttribute("value");
    }

    /**
     * Get error message
     *
     * @return string
     */
    public String getErrorMessage() {
        return pageUtils.waitForElementToAppear(errorMessage).getAttribute("textContent");
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
     * Selects the submit button
     *
     * @return current page object
     */
    public ProductionDefaultsPage submit() {
        modalDialogController.submit(submitButton);
        return this;
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
