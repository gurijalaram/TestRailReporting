package com.pageobjects.pages.evaluate.process;

import com.apriori.utils.PageUtils;

import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessSetupOptionsPage extends EvaluatePanelToolbar {

    private final Logger LOGGER = LoggerFactory.getLogger(ProcessSetupOptionsPage.class);

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

    @FindBy(css = "input[data-ap-comp='bundleSawingCount.radioButtons.userOverride']")
    private WebElement bundleSawingRadioButton;

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

    @FindBy(css = "input[data-ap-comp='materialAllowance.radioButtons.userOverride")
    private WebElement materialAllowanceOverrideButton;

    @FindBy(css = "select[data-ap-field='partTolerance.modeValues.defaultTolerance.storedListValue']")
    private WebElement partToleranceDropdown;

    @FindBy(css = "input[data-ap-comp='plateThickness.radioButtons.standard")
    private WebElement standardRadioButton;

    @FindBy(css = "input[data-ap-comp='plateThickness.radioButtons.user']")
    private WebElement desiredRadioButton;

    @FindBy(css = "input[data-ap-field='plateThickness.modeValues.user.value']")
    private WebElement thicknessInput;

    @FindBy(css = "select[data-ap-field='platingMethod.modeValues.platingMethod.storedListValue']")
    private WebElement platingMethodSelect;

    @FindBy(css = "input[data-ap-comp='partThickness.radioButtons.default']")
    private WebElement calculatedValueRadioButton;

    @FindBy(css = "input[data-ap-comp='partThickness.radioButtons.user']")
    private WebElement overridePartThicknessRadioButton;

    @FindBy(css = "input[data-ap-field='partThickness.modeValues.user.value']")
    private WebElement partThicknessInput;

    @FindBy(css = "input[data-ap-comp='fractionPainted.radioButtons.wholePart']")
    private WebElement wholeRadioButton;

    @FindBy(css = "input[data-ap-comp='fractionPainted.radioButtons.user']")
    private WebElement fractionRadioButton;

    @FindBy(css = "input[data-ap-field='fractionPainted.modeValues.user.value']")
    private WebElement fractionPaintedInput;

    @FindBy(css = "input[data-ap-comp='numMaskedFeatures.radioButtons.threadedHoles']")
    private WebElement threadRadioButton;

    @FindBy(css = "input[data-ap-comp='numMaskedFeatures.radioButtons.none']")
    private WebElement noMaskingRadioButton;

    @FindBy(css = "input[data-ap-comp='numMaskedFeatures.radioButtons.user']")
    private WebElement maskFeaturesRadioButton;

    @FindBy(css = "input[data-ap-field='numMaskedFeatures.modeValues.user.value']")
    private WebElement maskFeaturesInput;

    @FindBy(css = "input[data-ap-field='paintedBatchSize.radioButtons.productionBatchSize']")
    private WebElement proBatchRadioButton;

    @FindBy(css = "label[data-ap-field='paintedBatchSize.modeValues.productionBatchSize.value']")
    private WebElement proBatchValue;

    @FindBy(css = "input[data-ap-comp='paintedBatchSize.radioButtons.user']")
    private WebElement specifyRadioButton;

    @FindBy(css = "input[data-ap-field='paintedBatchSize.modeValues.user.value']")
    private WebElement specifyPaintedInput;

    @FindBy(css = "input[data-ap-comp='partsPerCart.radioButtons.auto']")
    private WebElement computedRadioButton;

    @FindBy(css = "input[data-ap-comp='partsPerCart.radioButtons.user']")
    private WebElement partsRadioButton;

    @FindBy(css = "input[data-ap-field='partsPerCart.modeValues.user.value']")
    private WebElement userSpecifiedInput;

    @FindBy(css = "input[data-ap-comp='caseDepth.radioButtons.default']")
    private WebElement caseDefaultRadioButton;

    @FindBy(css = "input[data-ap-comp='caseDepth.radioButtons.userOverride']")
    private WebElement caseOverrideRadioButton;

    @FindBy(css = "input[data-ap-field='caseDepth.modeValues.userOverride.value']")
    private WebElement caseOverrideInput;

    @FindBy(css = "input[data-ap-field='numMasks.modeValues.userOverride.value']")
    private WebElement maskModeInput;

    @FindBy(css = "input[data-ap-comp='numMasks.radioButtons.defaultNoMasking']")
    private WebElement noDefaultMaskingRadioButton;

    @FindBy(css = "input[data-ap-comp='numMasks.radioButtons.userOverride']")
    private WebElement maskFeatureRadioButton;

    @FindBy(css = "input[data-ap-field='partsPerLoadWindow.modeValues.user.value']")
    private WebElement componentsPerLoadInput;

    @FindBy(css = "div.form-section")
    private WebElement psoFormSection;

    @FindBy(css = "input[data-ap-comp='cadModelMisalignmentSensitivity.radioButtons.user']")
    private WebElement overrideSensitivityRadioButton;

    @FindBy(css = "input[data-ap-field='cadModelMisalignmentSensitivity.modeValues.user.value']")
    private WebElement overrideSensitivityInput;

    @FindBy(css = "select[data-ap-field='ComputedUtilizationPartOrientation.modeValues.PartOrientation.storedListValue']")
    private WebElement partOrientationDropdown;

    @FindBy(css = "select[data-ap-field='ComputedUtilizationSheetGrainDirection.modeValues.GrainDirection.storedListValue']")
    private WebElement grainDirectionDropdown;

    @FindBy(css = "input[data-ap-comp='minHoleDiameter.radioButtons.userOverride']")
    private WebElement holeDiameterOVerrideRadioButton;

    @FindBy(css = "input[data-ap-field='minHoleDiameter.modeValues.userOverride.value']")
    private WebElement minHoleDiameterInput;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessSetupOptionsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(psoFormSection);
    }

    /**
     * Select use default value for number of cavities
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectDefaultValue() {
        pageUtils.waitForElementToAppear(defaultValueRadioButton).click();
        return this;
    }

    /**
     * Select default cavities
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectDefaultCavities() {
        pageUtils.waitForElementToAppear(defaultCavitiesRadioButton).click();
        return this;
    }

    /**
     * Selects base on annual volume for number of cavities
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectAnnualVolume() {
        pageUtils.waitForElementToAppear(annVolumeRadioButton).click();
        return this;
    }

    /**
     * Selects optimize for minimum cost button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectOptimizeForMinimumCostButton() {
        pageUtils.waitForElementToAppear(optimizeRadioButton).click();
        return this;
    }

    /**
     * Gets Optimize for minimum cost is selected
     *
     * @return current page object
     */
    public boolean getOptimizeForMinimumCostSelected() {
        By radioButton = By.cssSelector("input[data-ap-comp='numberOfCavities.radioButtons.optimize']");
        pageUtils.waitForElementToAppear(radioButton);
        return driver.findElement(radioButton).isSelected();
    }

    /**
     * Selects user defined value dropdown for number of cavities
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessSetupOptionsPage selectDefinedValueDropdown(String option) {
        pageUtils.selectDropdownOption(definedValueDropdown, option);
        return this;
    }

    /**
     * Gets the selected user defined value dropdown for number of cavities
     *
     * @return
     */
    public boolean getDefinedValueDropdown(String text) {
        return pageUtils.checkElementFirstOption(definedValueDropdown, text);
    }

    /**
     * Selects material dropdown
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessSetupOptionsPage selectMoldMaterialDropdown(String option) {
        pageUtils.selectDropdownOption(moldMaterialDropdown, option);
        return this;
    }

    /**
     * Gets the selected Mold Material
     *
     * @return
     */
    public boolean getMoldMaterial(String text) {
        return pageUtils.checkElementFirstOption(moldMaterialDropdown, text);
    }

    /**
     * Sets number of cavities
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setNumberCavities(String value) {
        setInput(numberCavitiesInput, value);
        return this;
    }

    /**
     * Gets number of cavities
     *
     * @return string
     */
    public String getNumberOfCavities() {
        return getAttribute(numberCavitiesInput);
    }

    /**
     * Selects derived button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectDerivedButton() {
        pageUtils.waitForElementToAppear(derivedRadioButton).click();
        return this;
    }

    /**
     * Selects override nominal wall thickness
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectOverrideNominalButton() {
        pageUtils.waitForElementToAppear(overrideNominalRadioButton).click();
        return this;
    }


    /**
     * Sets the override value for nominalWallThickness
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setOverride(String value) {
        setInput(overrideInput, value);
        return this;
    }

    /**
     * Gets the user override value for nominalWallThickness
     *
     * @return true/false
     */
    public String getNominalWallThicknessOverride() {
        return getAttribute(overrideInput);
    }

    /**
     * Selects override bundle count
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectOverrideBundleCount() {
        pageUtils.waitForElementToAppear(bundleSawingRadioButton).click();
        return this;
    }

    /**
     * Sets the override value for bundle count
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setBundleCountOverride(String value) {
        setInput(overrideBundleInput, value);
        return this;
    }

    /**
     * Gets the user override value for bundle count
     *
     * @return string
     */
    public String getBundleCount() {
        return getAttribute(overrideBundleInput);
    }

    /**
     * Sets the override value for Material Allowance (Piece Part Cost Driver) (mm)
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setMaterialAllowanceOverride(String value) {
        setInput(materialAllowanceInput, value);
        return this;
    }

    /**
     * Gets the user override value for Material Allowance
     *
     * @return string
     */
    public String getMaterialAllowance() {
        return getAttribute(materialAllowanceInput);
    }

    /**
     * Selects material allowance override button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectMaterialAllowanceOverrideValue() {
        pageUtils.waitForElementToAppear(materialAllowanceOverrideButton).click();
        return this;
    }

    /**
     * Selects no colorant
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectNoColorantButton() {
        pageUtils.waitForElementToAppear(noColorantRadioButton).click();
        return this;
    }

    /**
     * Selects add colorant at standard rate button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectAddColorantButton() {
        pageUtils.waitForElementToAppear(addColorantRadioButton).click();
        return this;
    }

    /**
     * Check Add colorant at standard rate is selected
     *
     * @return current page object
     */
    public boolean isAddColorantSelected() {
        return pageUtils.waitForElementToAppear(addColorantRadioButton).isSelected();
    }

    /**
     * Selects defined colour charge button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectUserDefinedColorChargeButton() {
        pageUtils.waitForElementToAppear(definedColorRadioButton).click();
        return this;
    }

    /**
     * Sets the defined input for color charge
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setDefinedColorChargeInput(String value) {
        setInput(definedInput, value);
        return this;
    }

    /**
     * Gets the defined value for color charge
     *
     * @return string
     */
    public String getColorChargeOverride() {
        return getAttribute(definedInput);
    }

    /**
     * Select material regrind allowance button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectMaterialRegrindButton() {
        pageUtils.waitForElementToAppear(materialRegrindRadioButton).click();
        return this;
    }

    /**
     * Selects material defined value button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectMaterialDefinedButton() {
        pageUtils.waitForElementToAppear(materialDefinedRadioButton).click();
        return this;
    }

    /**
     * Sets the material regrind user defined value
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setMaterialRegrindInput(String value) {
        setInput(materialRegrindInput, value);
        return this;
    }

    /**
     * Gets material regrind user defined value
     *
     * @return string
     */
    public String getMaterialRegrind() {
        return getAttribute(materialRegrindInput);
    }

    /**
     * Sets the cooling time user defined value
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setCoolingtimeInput(String value) {
        setInput(coolingTimeInput, value);
        return this;
    }

    /**
     * Gets cooling tme user defined value
     *
     * @return string
     */
    public String getCoolingTime() {
        return getAttribute(coolingTimeInput);
    }

    private void setInput(WebElement locator, String value) {
        pageUtils.waitForElementToAppear(locator).clear();
        locator.sendKeys(value);
    }

    private String getAttribute(WebElement locator) {
        pageUtils.waitForElementToAppear(locator);
        return locator.getAttribute("value");
    }

    /**
     * Selects part tolerance dropdown
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessSetupOptionsPage selectPartToleranceDropdown(String option) {
        pageUtils.selectDropdownOption(partToleranceDropdown, option);
        return this;
    }

    /**
     * Gets the selected Part Tolerance
     *
     * @return
     */
    public boolean getSelectedPartTolerance(String text) {
        return pageUtils.checkElementFirstOption(partToleranceDropdown, text);
    }

    /**
     * Selects calculated value
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectCalculatedValue() {
        pageUtils.waitForElementToAppear(calculatedValueRadioButton).click();
        return this;
    }

    /**
     * Selects override button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectOverrideButton() {
        pageUtils.waitForElementToAppear(overridePartThicknessRadioButton).click();
        return this;
    }

    /**
     * Sets part thickness value
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setPartThickness(String value) {
        setInput(partThicknessInput, value);
        return this;
    }

    /**
     * Get part thickness value
     *
     * @return string
     */
    public String getPartThickness() {
        return getAttribute(partThicknessInput);
    }

    /**
     * Selects whole part/assembly
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectWholePartButton() {
        pageUtils.waitForElementToAppear(wholeRadioButton).click();
        return this;
    }

    /**
     * Selects fraction painted
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectFractionButton() {
        pageUtils.waitForElementToAppear(fractionRadioButton).click();
        return this;
    }

    /**
     * Enters the fraction painted value
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setFractionPainted(String text) {
        setInput(fractionPaintedInput, text);
        return this;
    }

    /**
     * Gets the fraction painted
     *
     * @return true/false
     */
    public String getFractionPainted() {
        return getAttribute(fractionPaintedInput);
    }

    /**
     * Select thread button
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectThreadButton() {
        pageUtils.waitForElementToAppear(threadRadioButton).click();
        return this;
    }

    /**
     * Select no masking
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectNoMaskingButton() {
        pageUtils.waitForElementToAppear(noMaskingRadioButton).click();
        return this;
    }

    /**
     * Select masked feature
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectEnterNumberOfMaskedFeaturesButton() {
        pageUtils.waitForElementToAppear(maskFeaturesRadioButton).click();
        return this;
    }

    /**
     * Sets mask features
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setMaskFeatures(String text) {
        setInput(maskFeaturesInput, text);
        return this;
    }

    /**
     * Check no masking button is selected
     *
     * @return current page object
     */
    public String isNoMaskingSelected(String attribute) {
        return pageUtils.waitForElementToAppear(noMaskingRadioButton).getAttribute(attribute);
    }


    /**
     * Gets mask feature input
     *
     * @return current page object
     */
    public String getTheNumberOfMaskedFeatures() {
        return getAttribute(maskFeaturesInput);
    }

    /**
     * Selects production batch
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectProductionBatch() {
        pageUtils.waitForElementToAppear(proBatchRadioButton).click();
        return this;
    }

    /**
     * Gets production batch size
     *
     * @return string
     */
    public String getProductionSize() {
        return pageUtils.waitForElementToAppear(proBatchValue).getText();
    }

    /**
     * Selects specify painted batch size
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectSpecify() {
        pageUtils.waitForElementToAppear(specifyRadioButton).click();
        return this;
    }

    /**
     * Sets specify painted batch size
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setSpecifyPainted(String text) {
        setInput(specifyPaintedInput, text);
        return this;
    }

    /**
     * Gets specify painted batch size
     *
     * @return string
     */
    public String getSpecifyPainted() {
        return getAttribute(specifyPaintedInput);
    }

    /**
     * Select auto computed
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectComputedButton() {
        pageUtils.waitForElementToAppear(computedRadioButton).click();
        return this;
    }

    /**
     * Select user specified
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectUserSpecifiedRadioButton() {
        pageUtils.waitForElementToAppear(partsRadioButton).click();
        return this;
    }

    /**
     * Set specified input
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setSpecifiedInput(String text) {
        setInput(userSpecifiedInput, text);
        return this;
    }

    /**
     * Gets specified input
     *
     * @return string
     */
    public String getSpecified() {
        return getAttribute(userSpecifiedInput);
    }

    /**
     * Selects default value
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectCaseDefaultButton() {
        pageUtils.waitForElementToAppear(caseDefaultRadioButton).click();
        return this;
    }

    /**
     * Selects case override
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectCaseOverrideButton() {
        pageUtils.waitForElementToAppear(caseOverrideRadioButton).click();
        return this;
    }

    /**
     * Set case override
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setCaseOverrideInput(String text) {
        setInput(caseOverrideInput, text);
        return this;
    }

    /**
     * Checks case override
     * @return title
     */
    public String getCaseOverride() {
        pageUtils.waitForElementToAppear(caseOverrideInput);
        return caseOverrideInput.getAttribute("title");
    }

    /**
     * Selects no masking default
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectNoDefaultMaskingButton() {
        pageUtils.waitForElementToAppear(noDefaultMaskingRadioButton).click();
        return this;
    }

    /**
     * Selects number of mask
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectNumMaskedFeaturesButton() {
        pageUtils.waitForElementToAppear(maskFeatureRadioButton).click();
        return this;
    }

    /**
     * Set masked features
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setMaskedFeaturesInput(String text) {
        setInput(maskModeInput, text);
        return this;
    }

    /**
     * Gets masked features
     *
     * @return string
     */
    public String getMaskedFeatures() {
        return getAttribute(maskModeInput);
    }

    /**
     * Set parts per load
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setComponentsPerLoad(String text) {
        setInput(componentsPerLoadInput, text);
        return this;
    }

    /**
     * Gets components per load
     *
     * @return string
     */
    public String getComponentsPerLoad() {
        return getAttribute(componentsPerLoadInput);
    }

    /**
     * Selects override sensitivity
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectOverrideSensitivityButton() {
        pageUtils.waitForElementToAppear(overrideSensitivityRadioButton).click();
        return this;
    }

    /**
     * Gets CAD Model Sensitivity Override
     *
     * @return String
     */
    public String getCadModelSensitivity() {
        pageUtils.waitForElementToAppear(overrideSensitivityInput);
        return overrideSensitivityInput.getAttribute("title");
    }

    /**
     * Set cad model sensitivity
     *
     * @param text - the text
     * @return current page object
     */
    public ProcessSetupOptionsPage setCadModelSensitivity(String text) {
        setInput(overrideSensitivityInput, text);
        return this;
    }

    /**
     * Selects part orientation dropdown
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessSetupOptionsPage selectPartOrientationDropdown(String option) {
        pageUtils.selectDropdownOption(partOrientationDropdown, option);
        return this;
    }

    /**
     * Gets the selected Part Orientation
     *
     * @return
     */
    public boolean getPartOrientation(String text) {
        return pageUtils.checkElementFirstOption(partOrientationDropdown, text);
    }

    /**
     * Selects grain direction dropdown
     *
     * @param option - the option
     * @return current page object
     */
    public ProcessSetupOptionsPage selectGrainDirectionDropdown(String option) {
        pageUtils.selectDropdownOption(grainDirectionDropdown, option);
        return this;
    }

    /**
     * Gets the selected grain direction
     *
     * @return
     */
    public boolean getGrainDirection(String text) {
        return pageUtils.checkElementFirstOption(grainDirectionDropdown, text);
    }

    /**
     * Selects user override for min hole diameter
     *
     * @return current page object
     */
    public ProcessSetupOptionsPage selectHoleDiameterOverride() {
        pageUtils.waitForElementToAppear(holeDiameterOVerrideRadioButton).click();
        return this;
    }

    /**
     * Sets the min hole diamater
     *
     * @param value - the value
     * @return current page object
     */
    public ProcessSetupOptionsPage setMinHoleDiameter(String value) {
        setInput(minHoleDiameterInput, value);
        return this;
    }

    /**
     * Gets the min hole diamater
     *
     * @return string
     */
    public String getMinHoleDiameter() {
        return getAttribute(minHoleDiameterInput);
    }
}