package main.java.pages.evaluate.designguidance.tolerances;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToleranceEditPage extends LoadableComponent<ToleranceEditPage> {

    private final Logger logger = LoggerFactory.getLogger(ToleranceEditPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "input[data-ap-field='circularity.current']")
    private WebElement circularityInput;

    @FindBy(css = "input[data-ap-field='concentricity.current']")
    private WebElement concentricityInput;

    @FindBy(css = "input[data-ap-field='cylindricity.current']")
    private WebElement cylindricityInput;

    @FindBy(css = "input[data-ap-field='diamTolerance.current']")
    private WebElement diamToleranceInput;

    @FindBy(css = "input[data-ap-field='parallelism.current']")
    private WebElement parallelismInput;

    @FindBy(css = "input[data-ap-field='perpendicularity.current']")
    private WebElement perpendicularityInput;

    @FindBy(css = "input[data-ap-field='positionTolerance.current']")
    private WebElement truePositionInput;

    @FindBy(css = "input[data-ap-field='profileOfSurface.current']")
    private WebElement profileSurfaceInput;

    @FindBy(css = "input[data-ap-field='roughness.current']")
    private WebElement roughnessInput;

    @FindBy(css = "input[data-ap-field='rougnessRz.current']")
    private WebElement roughnessRzInput;

    @FindBy(css = "input[data-ap-field='runout.current']")
    private WebElement runoutInput;

    @FindBy(css = "input[data-ap-field='straightness.current']")
    private WebElement straightnessInput;

    @FindBy(css = "input[data-ap-field='symmetry.current']")
    private WebElement symmetryInput;

    @FindBy(css = "input[data-ap-field='tolerance.current']")
    private WebElement toleranceInput;

    @FindBy(css = "input[data-ap-field='totalRunout.current']")
    private WebElement totalRunoutInput;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ToleranceEditPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(dialogTitle);
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setCircularity(String value) {
        pageUtils.clearInput(circularityInput);
        circularityInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setConcentricity(String value) {
        pageUtils.clearInput(concentricityInput);
        concentricityInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setCylindricity(String value) {
        pageUtils.clearInput(cylindricityInput);
        cylindricityInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setDiamTolerance(String value) {
        pageUtils.clearInput(diamToleranceInput);
        diamToleranceInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setParallelism(String value) {
        pageUtils.clearInput(parallelismInput);
        parallelismInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setPerpendicularity(String value) {
        pageUtils.clearInput(perpendicularityInput);
        perpendicularityInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setTruePosition(String value) {
        pageUtils.clearInput(truePositionInput);
        truePositionInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setProfileSurface(String value) {
        pageUtils.clearInput(profileSurfaceInput);
        profileSurfaceInput.sendKeys(value);
        return this;
    }

    private ToleranceEditPage setRoughnessRa(String value) {
        pageUtils.clearInput(roughnessInput);
        roughnessInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setRoughnessRz(String value) {
        pageUtils.clearInput(roughnessRzInput);
        roughnessRzInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setRunout(String value) {
        pageUtils.clearInput(runoutInput);
        runoutInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setStraightness(String value) {
        pageUtils.clearInput(straightnessInput);
        straightnessInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setSymmetry(String value) {
        pageUtils.clearInput(symmetryInput);
        symmetryInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    private ToleranceEditPage setToleranceCoor(String value) {
        pageUtils.clearInput(toleranceInput);
        toleranceInput.sendKeys(value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setTotalRunout(String value) {
        return clearInputAndSendKeys(totalRunoutInput, value);
    }

    /**
     * Private helper method used to clear input box and sendkeys
     *
     * @param element - the webelement
     * @param value   - the value
     * @return current page object
     */
    ToleranceEditPage clearInputAndSendKeys(WebElement element, String value) {
        pageUtils.clearInput(element);
        element.sendKeys(value);
        return this;
    }

    /**
     * Selects the apply button
     * @return new page object
     */
    public TolerancePage apply() {
        applyButton.click();
        return new TolerancePage(driver);
    }

    /**
     * Selects the cancel button
     * @return new page object
     */
    public TolerancePage cancel() {
        cancelButton.click();
        return new TolerancePage(driver);
    }
}