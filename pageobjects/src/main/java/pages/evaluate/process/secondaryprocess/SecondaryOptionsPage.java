package main.java.pages.evaluate.process.secondaryprocess;

import main.java.utils.PageUtils;
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
     * @return current page object
     */
    public SecondaryOptionsPage selectCalculatedValue() {
        pageUtils.waitForElementToAppear(calculatedValueRadioButton).click();
        return this;
    }

    /**
     * Selects override button
     * @return current page object
     */
    public SecondaryOptionsPage selectOverrideButton() {
        pageUtils.waitForElementToAppear(overrideRadioButton).click();
        return this;
    }

    /**
     * Sets part thickness value
     * @param value - the value
     * @return current page object
     */
    public SecondaryOptionsPage setPartThickness(String value) {
        setInput(partThicknessInput, value);
        return this;
    }

    /**
     * Checks part thickness value
     * @param text - the value
     * @return true/false
     */
    public Boolean isPartThickness(String text) {
        return checkAttribute(partThicknessInput, text);
    }

    /**
     * Selects whole part/assembly
     * @return current page object
     */
    public SecondaryOptionsPage selectWholePartButton() {
        pageUtils.waitForElementToAppear(wholeRadioButton).click();
        return this;
    }

    /**
     * Selects fraction painted
     * @return current page object
     */
    public SecondaryOptionsPage selectFractionButton() {
        pageUtils.waitForElementToAppear(fractionRadioButton).click();
        return this;
    }

    private void setInput(WebElement locator, String value) {
        pageUtils.waitForElementToAppear(locator).clear();
        locator.sendKeys(value);
    }

    private Boolean checkAttribute(WebElement locator, String text) {
        return pageUtils.checkElementAttribute(locator, "value", text);
    }
}
