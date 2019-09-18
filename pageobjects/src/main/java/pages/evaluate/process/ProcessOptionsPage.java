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
     * @return
     */
    public ProcessOptionsPage selectDefaultValue() {
        pageUtils.waitForElementToAppear(defaultValueRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectDefaultCavities() {
        pageUtils.waitForElementToAppear(defaultCavitiesRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectAnnualVolume() {
        pageUtils.waitForElementToAppear(annVolumeRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectOptimizeButton() {
        pageUtils.waitForElementToAppear(optimizeRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectDefinedValueButton() {
        pageUtils.waitForElementToAppear(definedValueRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectDefinedValueDropdown() {
        pageUtils.waitForElementToAppear(definedValueDropdown).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectMaterialDropdown() {
        pageUtils.waitForElementToAppear(moldMaterialDropdown).click();
        return this;
    }

    /**
     * @param cavities
     * @return
     */
    public ProcessOptionsPage setNumberCavities(String cavities) {
        numberCavitiesInput.clear();
        numberCavitiesInput.sendKeys(cavities);
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectDerivedButton() {
        pageUtils.waitForElementToAppear(derivedRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectOverrideNominalButton() {
        pageUtils.waitForElementToAppear(overrideNominalRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage setOverride() {
        pageUtils.waitForElementToAppear(overrideInput).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectNoColorantButton() {
        pageUtils.waitForElementToAppear(noColorantRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectAddColorantButton() {
        pageUtils.waitForElementToAppear(addColorantRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectDefinedColorButton() {
        pageUtils.waitForElementToAppear(definedColorRadioButton).click();
        return this;
    }

    /**
     * @param text
     * @return
     */
    public ProcessOptionsPage setDefinedInput(String text) {
        definedInput.clear();
        definedInput.sendKeys(text);
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectMaterialRegrindButton() {
        pageUtils.waitForElementToAppear(materialRegrindRadioButton).click();
        return this;
    }

    /**
     * @return
     */
    public ProcessOptionsPage selectMaterialDefinedButton() {
        pageUtils.waitForElementToAppear(materialDefinedRadioButton).click();
        return this;
    }

    /**
     * @param text
     * @return
     */
    public ProcessOptionsPage setMaterialRegrindInput(String text) {
        materialRegrindInput.clear();
        materialRegrindInput.sendKeys(text);
        return this;
    }
}