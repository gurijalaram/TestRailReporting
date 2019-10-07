package com.apriori.pageobjects.pages.settings;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductionDefaultPage extends LoadableComponent<ProductionDefaultPage> {

    private final Logger logger = LoggerFactory.getLogger(ToleranceSettingsPage.class);

    @FindBy(css = "input[data-ap-field='scenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "select[data-ap-field='pg']")
    private WebElement processGroupDropdown;

    @FindBy(css = "select[data-ap-field='vpe']")
    private WebElement vpeDropdown;

    @FindBy(css = "select[data-ap-field='materialCatalogName']")
    private WebElement catalogDropdown;

    @FindBy(css = "select[data-ap-field='material']")
    private WebElement materialDropdown;

    @FindBy(css = "input[data-ap-field='annualVolume']")
    private WebElement volumeInput;

    @FindBy(css = "input[data-ap-field='productionLife']")
    private WebElement lifeInput;

    @FindBy(css = "input[data-ap-comp='AUTO']")
    private WebElement autoRadioButton;

    @FindBy(css = "input[data-ap-comp='MANUAL']")
    private WebElement manualRadioButton;

    @FindBy(css = ".col-xs-2 .bold-font")
    private WebElement batchInput;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProductionDefaultPage(WebDriver driver) {
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

    }

    /**
     * Enter scenario name
     *
     * @param scenarioName - the scenario name
     * @return curent page object
     */
    public ProductionDefaultPage enterScenarioName(String scenarioName) {
        pageUtils.waitForElementToAppear(scenarioNameInput).clear();
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public ProductionDefaultPage selectProcessGroup(String processGroup) {
        pageUtils.waitForElementToBeClickable(processGroupDropdown);
        pageUtils.selectDropdownOption(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * Selects the vpe dropdown
     *
     * @param vpe - the vpe
     * @return current page object
     */
    public ProductionDefaultPage selectVPE(String vpe) {
        pageUtils.selectDropdownOption(vpeDropdown, vpe);
        return this;
    }

    /**
     * Selects the material catalog dropdown
     *
     * @param materialCatalog - the vpe
     * @return current page object
     */
    public ProductionDefaultPage selectMaterialCatalog(String materialCatalog) {
        pageUtils.selectDropdownOption(catalogDropdown, materialCatalog);
        return this;
    }

    /**
     * Selects the material dropdown
     *
     * @param material - the material
     * @return current page object
     */
    public ProductionDefaultPage selectMaterial(String material) {
        pageUtils.selectDropdownOption(materialDropdown, material);
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public ProductionDefaultPage enterAnnualVolume(String annualVolume) {
        pageUtils.waitForElementToAppear(volumeInput).clear();
        volumeInput.sendKeys(annualVolume);
        return this;
    }

    /**
     * Enters the production life
     *
     * @param years - the years
     * @return current page object
     */
    public ProductionDefaultPage enterProductionLife(String years) {
        pageUtils.waitForElementToAppear(lifeInput).clear();
        lifeInput.sendKeys(years);
        return this;
    }

    /**
     * Selects the auto radio button
     *
     * @return current page object
     */
    public ProductionDefaultPage selectBatchAuto() {
        pageUtils.waitForElementAndClick(autoRadioButton);
        return this;
    }

    /**
     * Selects the manual radio button
     *
     * @return current page object
     */
    public ProductionDefaultPage selectBatchManual() {
        pageUtils.waitForElementAndClick(manualRadioButton);
        return this;
    }

    /**
     * Enter batch input
     *
     * @param input - the value
     * @return
     */
    public ProductionDefaultPage enterBatchInput(String input) {
        pageUtils.waitForElementToAppear(batchInput).clear();
        batchInput.sendKeys(input);
        return this;
    }

    /**
     * Gets the selected process group
     *
     * @return
     */

    public Boolean getSelectedProcessGroup(String text) {
        return pageUtils.checkElementFirstOption(processGroupDropdown, text);

    }

    /**
     * Gets the selected VPE
     *
     * @return
     */

    public Boolean getSelectedVPE(String text) {
        return pageUtils.checkElementFirstOption(vpeDropdown, text);
        /**
         * Gets the MaterialCatalogue
         *
         * @return
         */
    }

    public Boolean getSelectedCatalog(String text) {
        return pageUtils.checkElementFirstOption(catalogDropdown, text);
    }

    /**
     * Gets the Material
     *
     * @return
     */

    public Boolean getSelectedMaterial(String text) {
        return pageUtils.checkElementFirstOption(materialDropdown, text);
    }

    /**
     * Checks the input value is correct
     *
     * @return true/false
     */
    public Boolean getScenarioName(String text) {
        return pageUtils.checkElementAttribute(scenarioNameInput, "value", text);
    }
}