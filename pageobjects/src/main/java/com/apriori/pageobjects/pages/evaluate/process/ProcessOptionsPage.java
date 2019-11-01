package com.apriori.pageobjects.pages.evaluate.process;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessOptionsPage extends LoadableComponent<ProcessOptionsPage> {

    private final Logger logger = LoggerFactory.getLogger(ProcessOptionsPage.class);

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.default']")
    private WebElement defaultValueRadioButton;

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.defaultNumCavMode']")
    private WebElement defaultCavitiesRadioButton;

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.annualVolume']")
    private WebElement annVolumeRadioButton;

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.optimize']")
    private WebElement optimizeRadioButton;

    @FindBy(css = "input[data-ap-comp='numberOfCavities.radioButtons.user']")
    private WebElement definedValueRadioButton;

    @FindBy(css = "select[data-ap-field='numberOfCavities.modeValues.user.storedListValue']")
    private WebElement definedValueDropdown;

    @FindBy(css = "select[data-ap-field='moldMaterial.modeValues.defaultMoldMaterial.storedListValue']")
    private WebElement moldMaterialDropdown;

    @FindBy(css = "input[data-ap-field='numberOfCavities.modeValues.user.value']")
    private WebElement numberCavitiesInput;

    @FindBy(css = "input[data-ap-comp='nominalWallThickness.radioButtons.deriveFromPart']")
    private WebElement derivedRadioButton;

    @FindBy(css = "input[data-ap-comp='nominalWallThickness.radioButtons.userOverride']")
    private WebElement overrideNominalRadioButton;

    @FindBy(css = "input[data-ap-field='nominalWallThickness.modeValues.userOverride.value']")
    private WebElement overrideInput;

    @FindBy(css = "input[data-ap-field='bundleSawingCount.modeValues.userOverride.value']")
    private WebElement overrideBundleInput;

    @FindBy(css = "input[data-ap-comp='colorantAdd.radioButtons.defaultColorant']")
    private WebElement noColorantRadioButton;

    @FindBy(css = "input[data-ap-comp='colorantAdd.radioButtons.colorantAdded']")
    private WebElement addColorantRadioButton;

    @FindBy(css = "input[data-ap-comp='colorantAdd.radioButtons.userDefined']")
    private WebElement definedColorRadioButton;

    @FindBy(css = "input[data-ap-field='colorantAdd.modeValues.userDefined.value']")
    private WebElement definedInput;

    @FindBy(css = "input[data-ap-comp='regrindAmount.radioButtons.defaultRegrind']")
    private WebElement materialRegrindRadioButton;

    @FindBy(css = "input[data-ap-comp='regrindAmount.radioButtons.userDefinedMode']")
    private WebElement materialDefinedRadioButton;

    @FindBy(css = "input[data-ap-field='regrindAmount.modeValues.userDefinedMode.value']")
    private WebElement materialRegrindInput;

    @FindBy(css = "input[data-ap-field='coolingTime.modeValues.user.value']")
    private WebElement coolingTimeInput;

    @FindBy(css = "input[data-ap-field='materialAllowance.modeValues.userOverride.value']")
    private WebElement materialAllowanceInput;

    @FindBy(css = "select[data-ap-field='partTolerance.modeValues.defaultTolerance.storedListValue']")
    private WebElement partToleranceDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessOptionsPage(WebDriver driver) {
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
     * Select use default value for number of cavities
     *
     * @return current page object
     */
    public ProcessOptionsPage selectDefaultValue() {
        pageUtils.waitForElementToAppear(defaultValueRadioButton).click();
        return this;
    }

    /**
     * Select default cavities
     *
     * @return current page object
     */
    public ProcessOptionsPage selectDefaultCavities() {
        pageUtils.waitForElementToAppear(defaultCavitiesRadioButton).click();
        return this;
    }

    /**
     * Selects base on annual volume for number of cavities
     *
     * @return current page object
     */
    public ProcessOptionsPage selectAnnualVolume() {
        pageUtils.waitForElementToAppear(annVolumeRadioButton).click();
        return this;
    }

    /**
     * Selects optimize for minimum cost button
     *
     * @return current page object
     */
    public ProcessOptionsPage selectOptimizeButton() {
        pageUtils.waitForElementToAppear(optimizeRadioButton).click();
        return this;
    }

    /**
     * Check Optimize for minimum cost is selected
     *
     * @return current page object
     */
    public String isOptimizeForMinimumCostSelected(String attribute) {
        return pageUtils.waitForElementToAppear(optimizeRadioButton).getAttribute(attribute);
    }

    /**
     * Selects user defined value dropdown for number of cavities
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessOptionsPage selectDefinedValueDropdown(String option) {
        pageUtils.selectDropdownOption(definedValueDropdown, option);
        return this;
    }

    /**
     * Gets the selected user defined value dropdown for number of cavities
     *
     * @return
     */
    public Boolean getDefinedValueDropdown(String text) {
        return pageUtils.checkElementFirstOption(definedValueDropdown, text);
    }

    /**
     * Selects material dropdown
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessOptionsPage selectMoldMaterialDropdown(String option) {
        pageUtils.selectDropdownOption(moldMaterialDropdown, option);
        return this;
    }

    /**
     * Gets the selected Mold Material
     *
     * @return
     */
    public Boolean getMoldMaterial(String text) {
        return pageUtils.checkElementFirstOption(moldMaterialDropdown, text);
    }

    /**
     * Sets number of cavities
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setNumberCavities(String value) {
        setInput(numberCavitiesInput, value);
        return this;
    }

    /**
     * Checks number of cavities
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isNumberOfCavities(String text) {
        return checkAttribute(numberCavitiesInput, text);
    }

    /**
     * Selects derived button
     *
     * @return current page object
     */
    public ProcessOptionsPage selectDerivedButton() {
        pageUtils.waitForElementToAppear(derivedRadioButton).click();
        return this;
    }

    /**
     * Selects override nominal wall thickness
     *
     * @return current page object
     */
    public ProcessOptionsPage selectOverrideNominalButton() {
        pageUtils.waitForElementToAppear(overrideNominalRadioButton).click();
        return this;
    }


    /**
     * Sets the override value for nominalWallThickness
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setOverride(String value) {
        setInput(overrideInput, value);
        return this;
    }

    /**
     * Checks the user override value for nominalWallThickness
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isNominalOverride(String text) {
        return checkAttribute(overrideInput, text);
    }

    /**
     * Sets the override value for bundle count
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setBundleCountOverride(String value) {
        setInput(overrideBundleInput, value);
        return this;
    }

    /**
     * Checks the user override value for bundle count
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isBundleCount(String text) {
        return checkAttribute(overrideBundleInput, text);
    }

    /**
     * Sets the override value for Material Allowance (Piece Part Cost Driver) (mm)
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setMaterialAllowanceOverride(String value) {
        setInput(materialAllowanceInput, value);
        return this;
    }

    /**
     * Checks the user override value for Material Allowance
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isMaterialAllowance(String text) {
        return checkAttribute(materialAllowanceInput, text);
    }

    /**
     * Selects no colorant
     *
     * @return current page object
     */
    public ProcessOptionsPage selectNoColorantButton() {
        pageUtils.waitForElementToAppear(noColorantRadioButton).click();
        return this;
    }

    /**
     * Selects add colorant at standard rate button
     *
     * @return current page object
     */
    public ProcessOptionsPage selectAddColorantButton() {
        pageUtils.waitForElementToAppear(addColorantRadioButton).click();
        return this;
    }

    /**
     * Check Add colorant at standard rate is selected
     *
     * @return current page object
     */
    public String isAddColorantSelected(String attribute) {
        return pageUtils.waitForElementToAppear(addColorantRadioButton).getAttribute(attribute);
    }

    /**
     * Selects defined colour charge button
     *
     * @return current page object
     */
    public ProcessOptionsPage selectDefinedColorButton() {
        pageUtils.waitForElementToAppear(definedColorRadioButton).click();
        return this;
    }

    /**
     * Sets the defined input for color charge
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setDefinedInput(String value) {
        setInput(definedInput, value);
        return this;
    }

    /**
     * Checks the defined value for color charge
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isDefinedInput(String text) {
        return checkAttribute(definedInput, text);
    }

    /**
     * Select material regrind allowance button
     *
     * @return current page object
     */
    public ProcessOptionsPage selectMaterialRegrindButton() {
        pageUtils.waitForElementToAppear(materialRegrindRadioButton).click();
        return this;
    }

    /**
     * Selects material defined value button
     *
     * @return current page object
     */
    public ProcessOptionsPage selectMaterialDefinedButton() {
        pageUtils.waitForElementToAppear(materialDefinedRadioButton).click();
        return this;
    }

    /**
     * Sets the material regrind user defined value
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setMaterialRegrindInput(String value) {
        setInput(materialRegrindInput, value);
        return this;
    }

    /**
     * Checks material regrind user defined value
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isMaterialRegrind(String text) {
        return checkAttribute(materialRegrindInput, text);
    }

    /**
     * Sets the cooling time user defined value
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setCoolingtimeInput(String value) {
        setInput(coolingTimeInput, value);
        return this;
    }

    /**
     * Checks cooling tme user defined value
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isCoolingTime(String text) {
        return checkAttribute(coolingTimeInput, text);
    }

    private void setInput(WebElement locator, String value) {
        pageUtils.waitForElementToAppear(locator).clear();
        locator.sendKeys(value);
    }

    private Boolean checkAttribute(WebElement locator, String text) {
        return pageUtils.checkElementAttribute(locator, "value", text);
    }

    /**
     * Selects part tolerance dropdown
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessOptionsPage selectPartToleranceDropdown(String option) {
        pageUtils.selectDropdownOption(partToleranceDropdown, option);
        return this;
    }

    /**
     * Gets the selected Part Tolerance
     *
     * @return
     */
    public Boolean getSelectedPartTolerance(String text) {
        return pageUtils.checkElementFirstOption(partToleranceDropdown, text);
    }
}