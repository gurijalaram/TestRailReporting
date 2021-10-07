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

import java.util.Arrays;
import java.util.HashSet;

public class ProductionDefaultPage extends LoadableComponent<ProductionDefaultPage> {

    private static final Logger logger = LoggerFactory.getLogger(ToleranceSettingsPage.class);

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
        enterInput(scenarioNameInput, scenarioName);
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
        return enterInput(volumeInput, annualVolume);
    }

    /**
     * Clears annual volume
     *
     * @return current page object
     */
    public ProductionDefaultPage clearAnnualVolume() {
        return clearInput(volumeInput);
    }

    /**
     * Enters the production life
     *
     * @param years - the years
     * @return current page object
     */
    public ProductionDefaultPage enterProductionLife(String years) {
        return enterInput(lifeInput, years);
    }

    /**
     * Clears the production life
     *
     * @return current page object
     */
    public ProductionDefaultPage clearProductionLife() {
        return clearInput(lifeInput);
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
        return enterInput(batchInput, input);
    }

    /**
     * Clears batch input
     *
     * @return current page object
     */
    public ProductionDefaultPage clearBatchInput() {
        return clearInput(batchInput);
    }

    /**
     * Gets the selected process group
     *
     * @return
     */
    public boolean getSelectedProcessGroup(String text) {
        return pageUtils.checkElementFirstOption(processGroupDropdown, text);
    }

    /**
     * Gets the selected VPE
     *
     * @return
     */
    public boolean getSelectedVPE(String text) {
        return pageUtils.checkElementFirstOption(vpeDropdown, text);
    }

    /**
     * Gets the MaterialCatalogue
     *
     * @return
     */
    public boolean getSelectedCatalog(String text) {
        return pageUtils.checkElementFirstOption(catalogDropdown, text);
    }

    /**
     * Gets the Material
     *
     * @return true/false
     */
    public boolean getSelectedMaterial(String text) {
        return pageUtils.checkElementFirstOption(materialDropdown, text);
    }

    /**
     * Get the input value is correct
     *
     * @return string
     */
    public String getScenarioName() {
        By scenarioInput = By.cssSelector("input[data-ap-field='scenarioName']");
        pageUtils.waitForElementToAppear(scenarioInput);
        return driver.findElement(scenarioInput).getAttribute("value");
    }

    private ProductionDefaultPage clearInput(WebElement locator) {
        pageUtils.waitForElementToAppear(locator).clear();
        return this;
    }

    private ProductionDefaultPage enterInput(WebElement locator, String text) {
        clearInput(locator);
        locator.sendKeys(text);
        return this;
    }

    /**
     * Gets the list of materials from the dropdown
     *
     * @return hashset as duplicates need to be removed
     */
    public HashSet<String> getListOfMaterials() {
        return new HashSet<>(Arrays.asList(pageUtils.waitForElementToAppear(materialDropdown).getAttribute("innerText").split("\n")));
    }
}