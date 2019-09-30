package com.apriori.pageobjects.pages.evaluate.process.secondaryprocess;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondaryOptionsPage extends SecondaryProcessPage {

    private final Logger logger = LoggerFactory.getLogger(SecondaryOptionsPage.class);

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
    private WebElement overrideRadioButton;

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

    @FindBy(css = "'input[data-ap-comp='numMaskedFeatures.radioButtons.user']")
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

    private WebDriver driver;
    private PageUtils pageUtils;

    public SecondaryOptionsPage(WebDriver driver) {
        super(driver);
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
     * Selects calculated value
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectCalculatedValue() {
        pageUtils.waitForElementToAppear(calculatedValueRadioButton).click();
        return this;
    }

    /**
     * Selects override button
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectOverrideButton() {
        pageUtils.waitForElementToAppear(overrideRadioButton).click();
        return this;
    }

    /**
     * Sets part thickness value
     *
     * @param value - the value
     * @return current page object
     */
    public SecondaryOptionsPage setPartThickness(String value) {
        setInput(partThicknessInput, value);
        return this;
    }

    /**
     * Checks part thickness value
     *
     * @param text - the value
     * @return true/false
     */
    public Boolean isPartThickness(String text) {
        return checkAttribute(partThicknessInput, text);
    }

    /**
     * Selects whole part/assembly
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectWholePartButton() {
        pageUtils.waitForElementToAppear(wholeRadioButton).click();
        return this;
    }

    /**
     * Selects fraction painted
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectFractionButton() {
        pageUtils.waitForElementToAppear(fractionRadioButton).click();
        return this;
    }

    /**
     * Enters the fraction painted value
     *
     * @param text - the text
     * @return current page object
     */
    public SecondaryOptionsPage setFractionPainted(String text) {
        setInput(fractionPaintedInput, text);
        return this;
    }

    /**
     * Checks the fraction painted
     *
     * @param text - the text
     * @return true/false
     */
    public Boolean isFractionPainted(String text) {
        return checkAttribute(fractionPaintedInput, text);
    }

    /**
     * Select thread button
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectThreadButton() {
        pageUtils.waitForElementToAppear(threadRadioButton).click();
        return this;
    }

    /**
     * Select no masking
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectNoMaskingButton() {
        pageUtils.waitForElementToAppear(noMaskingRadioButton).click();
        return this;
    }

    /**
     * Select masked feature
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectMaskedFeaturesButton() {
        pageUtils.waitForElementToAppear(maskFeaturesRadioButton).click();
        return this;
    }

    /**
     * Sets mask features
     *
     * @param text - the text
     * @return current page object
     */
    public SecondaryOptionsPage setMaskFeatures(String text) {
        setInput(maskFeaturesInput, text);
        return this;
    }

    /**
     * Checks mask feature input
     *
     * @param text - the text
     * @return current page object
     */
    public Boolean isMaskFeatures(String text) {
        return checkAttribute(maskFeaturesInput, text);
    }

    /**
     * Selects production batch
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectProductionBatch() {
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
    public SecondaryOptionsPage selectSpecify() {
        pageUtils.waitForElementToAppear(specifyRadioButton).click();
        return this;
    }

    /**
     * Sets specify painted batch size
     *
     * @param text - the text
     * @return current page object
     */
    public SecondaryOptionsPage setSpecifyPainted(String text) {
        setInput(specifyPaintedInput, text);
        return this;
    }

    /**
     * Checks specify painted batch size
     *
     * @param text - the text
     * @return true/false
     */
    public Boolean isSpecifyPainted(String text) {
        return checkAttribute(specifyPaintedInput, text);
    }

    /**
     * Select auto computed
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectComputedButton() {
        pageUtils.waitForElementToAppear(computedRadioButton).click();
        return this;
    }

    /**
     * Select user specified
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectUserSpecifiedRadioButton() {
        pageUtils.waitForElementToAppear(partsRadioButton).click();
        return this;
    }

    /**
     * Set specified input
     *
     * @param text - the text
     * @return current page object
     */
    public SecondaryOptionsPage setSpecifiedInput(String text) {
        setInput(userSpecifiedInput, text);
        return this;
    }

    /**
     * Checks specified input
     *
     * @param text - the text
     * @return true false
     */
    public Boolean isSpecified(String text) {
        return checkAttribute(userSpecifiedInput, text);
    }

    /**
     * Selects default value
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectCaseDefaultButton() {
        pageUtils.waitForElementToAppear(caseDefaultRadioButton).click();
        return this;
    }

    /**
     * Selects case override
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectCaseOverrideButton() {
        pageUtils.waitForElementToAppear(caseOverrideRadioButton).click();
        return this;
    }

    /**
     * Set case override
     *
     * @param text - the text
     * @return current page object
     */
    public SecondaryOptionsPage setCaseOverrideInput(String text) {
        setInput(caseOverrideInput, text);
        return this;
    }

    /**
     * Checks case override
     *
     * @param text - the text
     * @return true false
     */
    public Boolean isCaseOverride(String text) {
        return checkAttribute(caseOverrideInput, text);
    }

    /**
     * Selects no masking default
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectNoDefaultMaskingButton() {
        pageUtils.waitForElementToAppear(noDefaultMaskingRadioButton).click();
        return this;
    }

    /**
     * Selects number of mask
     *
     * @return current page object
     */
    public SecondaryOptionsPage selectNumMaskedFeaturesButton() {
        pageUtils.waitForElementToAppear(maskFeatureRadioButton).click();
        return this;
    }

    /**
     * Set masked features
     *
     * @param text - the text
     * @return current page object
     */
    public SecondaryOptionsPage setMaksedFeaturesInput(String text) {
        setInput(maskModeInput, text);
        return this;
    }

    /**
     * Checks masked features
     *
     * @param text - the text
     * @return true false
     */
    public Boolean isMaskedFeatures(String text) {
        return checkAttribute(maskModeInput, text);
    }

    private void setInput(WebElement locator, String value) {
        pageUtils.waitForElementToAppear(locator).clear();
        locator.sendKeys(value);
    }

    private Boolean checkAttribute(WebElement locator, String text) {
        return pageUtils.checkElementAttribute(locator, "value", text);
    }
}