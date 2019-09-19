package main.java.pages.evaluate.process.secondaryprocess;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondaryProcessOptions extends SecondaryProcessPage {

    private final Logger logger = LoggerFactory.getLogger(SecondaryProcessOptions.class);

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

    private WebDriver driver;
    private PageUtils pageUtils;

    public SecondaryProcessOptions(WebDriver driver) {
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
    public SecondaryProcessOptions selectCalculatedValue() {
        pageUtils.waitForElementToAppear(calculatedValueRadioButton).click();
        return this;
    }

    /**
     * Selects override button
     * @return current page object
     */
    public SecondaryProcessOptions selectOverrideButton() {
        pageUtils.waitForElementToAppear(overrideRadioButton).click();
        return this;
    }

    /**
     * Sets part thickness value
     * @param value - the value
     * @return current page object
     */
    public SecondaryProcessOptions setPartThickness(String value) {
        pageUtils.waitForElementToAppear(partThicknessInput).clear();
        partThicknessInput.sendKeys(value);
        return this;
    }

    /**
     * Checks part thickness value
     * @param value - the value
     * @return true/false
     */
    public Boolean isPartThickness(String value) {
        return pageUtils.checkElementAttribute(partThicknessInput, "value", value);
    }
}
