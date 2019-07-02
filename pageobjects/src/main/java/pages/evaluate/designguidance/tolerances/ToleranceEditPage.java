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

    @FindBy(css = "input[data-ap-field='roughessRz.current']")
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
    public ToleranceEditPage setCircularity(String value) {
        return clearInputAndSendKeys(circularityInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setConcentricity(String value) {
        return clearInputAndSendKeys(concentricityInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setCylindricity(String value) {
        return clearInputAndSendKeys(cylindricityInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setDiamTolerance(String value) {
        return clearInputAndSendKeys(diamToleranceInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setParallelism(String value) {
        return clearInputAndSendKeys(parallelismInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setPerpendicularity(String value) {
        return clearInputAndSendKeys(perpendicularityInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setTruePosition(String value) {
        return clearInputAndSendKeys(truePositionInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setProfileSurface(String value) {
        return clearInputAndSendKeys(profileSurfaceInput, value);
    }

    public ToleranceEditPage setRoughnessRa(String value) {
        return clearInputAndSendKeys(roughnessInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setRoughnessRz(String value) {
        return clearInputAndSendKeys(roughnessRzInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setRunout(String value) {
        return clearInputAndSendKeys(runoutInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setStraightness(String value) {
        return clearInputAndSendKeys(straightnessInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setSymmetry(String value) {
        return clearInputAndSendKeys(symmetryInput, value);
    }

    /**
     * @param value
     * @return current page object
     */
    public ToleranceEditPage setToleranceCoor(String value) {
        return clearInputAndSendKeys(toleranceInput, value);
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