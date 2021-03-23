package com.pageobjects.pages.evaluate.designguidance.tolerances;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.ToleranceEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cfrith
 */

public class ToleranceEditPage extends LoadableComponent<ToleranceEditPage> {

    private static final Logger logger = LoggerFactory.getLogger(ToleranceEditPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "div[data-ap-comp='artifactTolerancesTable'] .v-grid-cell")
    private WebElement toleranceCell;

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

    @FindBy(css = "input[data-ap-field='flatness.current']")
    private WebElement flatnessInput;

    @FindBy(css = "input[data-ap-field='perpendicularity.current']")
    private WebElement perpendicularityInput;

    @FindBy(css = "input[data-ap-field='positionTolerance.current']")
    private WebElement truePositionInput;

    @FindBy(css = "input[data-ap-field='profileOfSurface.current']")
    private WebElement profileSurfaceInput;

    @FindBy(css = "input[data-ap-field='roughness.current']")
    private WebElement roughnessInput;

    @FindBy(css = "input[data-ap-field='roughnessRz.current']")
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

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button[data-ap-dialog-cancel]")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private Map<String, WebElement> map = new HashMap<>();

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
        pageUtils.waitForElementToAppear(toleranceCell);
    }

    /**
     * Gets the tolerance value
     *
     * @param toleranceName - the tolerance
     * @return string
     */
    public String getTolerance(String toleranceName) {
        pageUtils.waitForElementToAppear(getMap().get(toleranceName));
        return getMap().get(toleranceName).getAttribute("value");
    }

    /**
     * Removes the tolerance
     *
     * @param toleranceName - the string value
     * @return
     */
    public ToleranceEditPage removeTolerance(String toleranceName) {
        getMap().get(toleranceName).clear();
        return this;
    }

    /**
     * Sets the tolerance
     *
     * @param tolerance - the tolerance
     * @param text      - the string value
     * @return
     */
    public ToleranceEditPage setTolerance(String tolerance, String text) {
        getMap().get(tolerance).clear();
        getMap().get(tolerance).sendKeys(text);
        return this;
    }

    private Map<String, WebElement> buildMap() {
        map.put(ToleranceEnum.CIRCULARITY.getToleranceName(), circularityInput);
        map.put(ToleranceEnum.PARALLELISM.getToleranceName(), parallelismInput);
        map.put(ToleranceEnum.CONCENTRICITY.getToleranceName(), concentricityInput);
        map.put(ToleranceEnum.CYLINDRICITY.getToleranceName(), cylindricityInput);
        map.put(ToleranceEnum.DIAMTOLERANCE.getToleranceName(), diamToleranceInput);
        map.put(ToleranceEnum.FLATNESS.getToleranceName(), flatnessInput);
        map.put(ToleranceEnum.PERPENDICULARITY.getToleranceName(), perpendicularityInput);
        map.put(ToleranceEnum.TRUEPOSITION.getToleranceName(), truePositionInput);
        map.put(ToleranceEnum.PROFILESURFACE.getToleranceName(), profileSurfaceInput);
        map.put(ToleranceEnum.ROUGHNESSRA.getToleranceName(), roughnessInput);
        map.put(ToleranceEnum.ROUGHNESSRZ.getToleranceName(), roughnessRzInput);
        map.put(ToleranceEnum.RUNOUT.getToleranceName(), runoutInput);
        map.put(ToleranceEnum.STRAIGHTNESS.getToleranceName(), straightnessInput);
        map.put(ToleranceEnum.SYMMETRY.getToleranceName(), symmetryInput);
        map.put(ToleranceEnum.TOLERANCE.getToleranceName(), toleranceInput);
        map.put(ToleranceEnum.TOTALRUNOUT.getToleranceName(), totalRunoutInput);
        return map;
    }

    private Map<String, WebElement> getMap() {
        if (map == null || map.isEmpty()) {
            map = buildMap();
        }
        return map;
    }

    /**
     * Checks if the tolerance field is displayed and editable
     *
     * @param toleranceName - tolerance name
     * @return true/false
     */
    public boolean isToleranceInputDisplayed(String toleranceName) {
        return pageUtils.isElementDisplayed(getMap().get(toleranceName));
    }

    /**
     * Selects the apply button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return generic page object
     */
    public <T> T apply(Class<T> className) {
        pageUtils.waitForElementAndClick(applyButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public TolerancePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new TolerancePage(driver);
    }
}