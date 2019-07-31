package main.java.pages.settings;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ToleranceValuesPage extends LoadableComponent<ToleranceValuesPage> {

    private final Logger logger = LoggerFactory.getLogger(ToleranceValuesPage.class);

    @FindBy(css = "[data-ap-comp='partOverrideTolerances'] .modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "input[data-ap-field='toleranceOverride']")
    private WebElement toleranceInput;

    @FindBy(css = "input[data-ap-field='roughnessOverride']")
    private WebElement roughnessRaInput;

    @FindBy(css = "input[data-ap-field='roughnessRzOverride']")
    private WebElement roughnessRzInput;

    @FindBy(css = "input[data-ap-field='diamToleranceOverride']")
    private WebElement diamToleranceInput;

    @FindBy(css = "input[data-ap-field='positionToleranceOverride']")
    private WebElement truePositionInput;

    @FindBy(css = "input[data-ap-field='bendAngleToleranceOverride']")
    private WebElement bendAngleInput;

    @FindBy(css = "input[data-ap-field='circularityOverride']")
    private WebElement circularityInput;

    @FindBy(css = "input[data-ap-field='concentricityOverride']")
    private WebElement concentricityInput;

    @FindBy(css = "input[data-ap-field='cylindricityOverride']")
    private WebElement cylindricityInput;

    @FindBy(css = "input[data-ap-field='flatnessOverride']")
    private WebElement flatnessInput;

    @FindBy(css = "input[data-ap-field='parallelismOverride']")
    private WebElement parallelismInput;

    @FindBy(css = "input[data-ap-field='perpendicularityOverride']")
    private WebElement perpendicularityInput;

    @FindBy(css = "input[data-ap-field='profileOfSurfaceOverride']")
    private WebElement profileSurfaceInput;

    @FindBy(css = "input[data-ap-field='runoutOverride']")
    private WebElement runoutInput;

    @FindBy(css = "input[data-ap-field='totalRunoutOverride']")
    private WebElement totlaRunoutInput;

    @FindBy(css = "input[data-ap-field='straightnessOverride']")
    private WebElement straightnessInput;

    @FindBy(css = "input[data-ap-field='symmetryOverride']")
    private WebElement symmetryInput;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ToleranceValuesPage(WebDriver driver) {
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
     * Set tolerance value
     * @param tolerance - the tolerance value
     * @return current page object
     */
    public ToleranceValuesPage setTolerance(String tolerance) {
        return clearInputAndSendKeys(toleranceInput, tolerance);
    }

    /**
     * Set roughness ra
     * @param roughnessRa - the roughness ra
     * @return current page object
     */
    public ToleranceValuesPage setRoughnessRa(String roughnessRa) {
        return clearInputAndSendKeys(roughnessRaInput, roughnessRa);
    }

    /**
     * Set roughness rz
     * @param roughnessRz - the roughness rz
     * @return current page object
     */
    public ToleranceValuesPage setRoughnessRz(String roughnessRz) {
        return clearInputAndSendKeys(roughnessRzInput, roughnessRz);
    }

    /**
     * Set diam tolerance
     * @param diamTolerance - the diam tolerance
     * @return current page object
     */
    public ToleranceValuesPage setDiamTolerance(String diamTolerance) {
        return clearInputAndSendKeys(diamToleranceInput, diamTolerance);
    }

    /**
     * Set true position
     * @param truePosition - the true position
     * @return current page object
     */
    public ToleranceValuesPage setTruePosition(String truePosition) {
        return clearInputAndSendKeys(truePositionInput, truePosition);
    }

    /**
     * Set bend angle
     * @param bendAngle - the bend angle
     * @return current page object
     */
    public ToleranceValuesPage setBendAngle(String bendAngle) {
        return clearInputAndSendKeys(bendAngleInput, bendAngle);
    }

    /**
     * Set circularity
     * @param circularity - the circularity
     * @return current page object
     */
    public ToleranceValuesPage setCircularity(String circularity) {
        return clearInputAndSendKeys(circularityInput, circularity);
    }

    /**
     * Set concentricity
     * @param concentricity - the concentricity
     * @return current page object
     */
    public ToleranceValuesPage setConcentricity(String concentricity) {
        return clearInputAndSendKeys(concentricityInput, concentricity);
    }

    /**
     * Set cylindricity
     * @param cylindricity - the cylindricity
     * @return current page object
     */
    public ToleranceValuesPage setCylindricity(String cylindricity) {
        return clearInputAndSendKeys(cylindricityInput, cylindricity);
    }

    /**
     * Set flatness
     * @param flatness - the flatness
     * @return current page object
     */
    public ToleranceValuesPage setFlatness(String flatness) {
        return clearInputAndSendKeys(flatnessInput, flatness);
    }

    /**
     * Set parallelism
     * @param parallelism - the parallelism
     * @return current page object
     */
    public ToleranceValuesPage setParallelism(String parallelism) {
        return clearInputAndSendKeys(parallelismInput, parallelism);
    }

    /**
     * Set perpendicularity
     * @param perpendicularity - the perpendicularity
     * @return current page object
     */
    public ToleranceValuesPage setPerpendicularity(String perpendicularity) {
        return clearInputAndSendKeys(perpendicularityInput, perpendicularity);
    }

    /**
     * Set profile surface
     * @param profileSurface - the profile surface
     * @return current page object
     */
    public ToleranceValuesPage setProfileSurface(String profileSurface) {
        return clearInputAndSendKeys(profileSurfaceInput, profileSurface);
    }

    /**
     * Set runout
     * @param runout - the runout
     * @return current page object
     */
    public ToleranceValuesPage setRunout(String runout) {
        return clearInputAndSendKeys(runoutInput, runout);
    }

    /**
     * Set total runout
     * @param totalRunout - the total runout
     * @return current page object
     */
    public ToleranceValuesPage setTotalRunout(String totalRunout) {
        return clearInputAndSendKeys(totlaRunoutInput, totalRunout);
    }

    /**
     * Set straightness
     * @param straightness - the straightness
     * @return current page object
     */
    public ToleranceValuesPage setStraightness(String straightness) {
        return clearInputAndSendKeys(straightnessInput, straightness);
    }

    /**
     * Set symmetry
     * @param symmetry - the symmetry
     * @return current page object
     */
    public ToleranceValuesPage setSymmetry(String symmetry) {
        return clearInputAndSendKeys(symmetryInput, symmetry);
    }

    /**
     * Private helper method used to clear input box and sendkeys
     *
     * @param element - the webelement
     * @param value   - the value
     * @return current page object
     */
    ToleranceValuesPage clearInputAndSendKeys(WebElement element, String value) {
        pageUtils.clearInput(element);
        element.sendKeys(value);
        return this;
    }
}