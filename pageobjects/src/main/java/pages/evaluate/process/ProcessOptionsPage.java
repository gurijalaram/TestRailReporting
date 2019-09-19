package main.java.pages.evaluate.process;

import main.java.utils.PageUtils;
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
     * Select default value
     * @return current page object
     */
    public ProcessOptionsPage selectDefaultValue() {
        pageUtils.waitForElementToAppear(defaultValueRadioButton).click();
        return this;
    }

    /**
     * Select default cavities
     * @return current page object
     */
    public ProcessOptionsPage selectDefaultCavities() {
        pageUtils.waitForElementToAppear(defaultCavitiesRadioButton).click();
        return this;
    }

    /**
     * Selects annual volume
     * @return current page object
     */
    public ProcessOptionsPage selectAnnualVolume() {
        pageUtils.waitForElementToAppear(annVolumeRadioButton).click();
        return this;
    }

    /**
     * Selects optimize button
     * @return current page object
     */
    public ProcessOptionsPage selectOptimizeButton() {
        pageUtils.waitForElementToAppear(optimizeRadioButton).click();
        return this;
    }

    /**
     * Selects defined value dropdown
     * @param option - the option
     * @return current page object
     */
    public ProcessOptionsPage selectDefinedValueDropdown(String option) {
        pageUtils.selectDropdownOption(definedValueDropdown, option);
        return this;
    }

    /**
     * Selects material dropdown
     * @param option - the option
     * @return current page object
     */
    public ProcessOptionsPage selectMaterialDropdown(String option) {
        pageUtils.selectDropdownOption(moldMaterialDropdown, option);
        return this;
    }

    /**
     * Sets number of cavities
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setNumberCavities(String value) {
        numberCavitiesInput.clear();
        numberCavitiesInput.sendKeys(value);
        return this;
    }

    /**
     * Checks number of cavities
     * @param value - the value
     * @return true/false
     */
    public Boolean isNumberOfCavities(String value) {
        return pageUtils.checkElementAttribute(numberCavitiesInput, "value", value);
    }

    /**
     * Selects derived button
     * @return current page object
     */
    public ProcessOptionsPage selectDerivedButton() {
        pageUtils.waitForElementToAppear(derivedRadioButton).click();
        return this;
    }

    /**
     * Selects override nominal
     * @return current page object
     */
    public ProcessOptionsPage selectOverrideNominalButton() {
        pageUtils.waitForElementToAppear(overrideNominalRadioButton).click();
        return this;
    }


    /**
     * Sets the override value
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setOverride(String value) {
        overrideInput.clear();
        overrideInput.sendKeys(value);
        return this;
    }

    /**
     * Checks the override value
     * @param value - the value
     * @return true/false
     */
    public Boolean isNominalOverride(String value) {
        return pageUtils.checkElementAttribute(overrideInput, "value", value);
    }

    /**
     * Selects no colorant
     * @return current page object
     */
    public ProcessOptionsPage selectNoColorantButton() {
        pageUtils.waitForElementToAppear(noColorantRadioButton).click();
        return this;
    }

    /**
     * Select add colorant
     * @return current page object
     */
    public ProcessOptionsPage selectAddColorantButton() {
        pageUtils.waitForElementToAppear(addColorantRadioButton).click();
        return this;
    }

    /**
     * Selects defined colour
     * @return current page object
     */
    public ProcessOptionsPage selectDefinedColorButton() {
        pageUtils.waitForElementToAppear(definedColorRadioButton).click();
        return this;
    }

    /**
     * Sets the defined input
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setDefinedInput(String value) {
        definedInput.clear();
        definedInput.sendKeys(value);
        return this;
    }

    /**
     * Checks the defined value
     * @param value - the value
     * @return true/false
     */
    public Boolean isDefinedInput(String value) {
        return pageUtils.checkElementAttribute(definedInput, "value", value);
    }

    /**
     * Select material regrind
     * @return current page object
     */
    public ProcessOptionsPage selectMaterialRegrindButton() {
        pageUtils.waitForElementToAppear(materialRegrindRadioButton).click();
        return this;
    }

    /** Selects material defined
     * @return current page object
     */
    public ProcessOptionsPage selectMaterialDefinedButton() {
        pageUtils.waitForElementToAppear(materialDefinedRadioButton).click();
        return this;
    }

    /**
     * Sets the material regrind
     * @param value - the value
     * @return current page object
     */
    public ProcessOptionsPage setMaterialRegrindInput(String value) {
        materialRegrindInput.clear();
        materialRegrindInput.sendKeys(value);
        return this;
    }

    /**
     * Checks material regrind value
     * @param value - the value
     * @return true/false
     */
    public Boolean isMaterialRegrind(String value) {
        return pageUtils.checkElementAttribute(materialRegrindInput, "value", value);
    }
}