package com.apriori.pageobjects.pages.evaluate.designguidance.tolerances;

import com.apriori.pageobjects.utils.PageUtils;
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

    private final Logger logger = LoggerFactory.getLogger(ToleranceEditPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "div[data-ap-comp='artifactTolerancesTable'] .v-grid-cell.tolerance-input-column")
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

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
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
     * Checks the value is correct
     *
     * @param tolerance - the tolerance
     * @param text      - the string value
     * @return true/false
     */
    public Boolean isTolerance(String tolerance, String text) {
        return pageUtils.checkElementAttribute(getLocatorFromMap(tolerance), "value", text);
    }

    /**
     * Removes the tolerance
     *
     * @param text - the string value
     * @return
     */
    public ToleranceEditPage removeTolerance(String text) {
        pageUtils.waitForElementToAppear(getLocatorFromMap(text)).clear();
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
        pageUtils.clearInput(getLocatorFromMap(tolerance));
        getLocatorFromMap(tolerance).sendKeys(text);
        return this;
    }

    private Map<String, WebElement> buildMap() {

        map.put(ToleranceEnum.CIRCULARITY.getTolerance(), circularityInput);
        map.put(ToleranceEnum.PARALLELISM.getTolerance(), parallelismInput);
        map.put(ToleranceEnum.CONCENTRICITY.getTolerance(), concentricityInput);
        map.put(ToleranceEnum.CYLINDRICITY.getTolerance(), cylindricityInput);
        map.put(ToleranceEnum.DIAMTOLERANCE.getTolerance(), diamToleranceInput);
        map.put(ToleranceEnum.FLATNESS.getTolerance(), flatnessInput);
        map.put(ToleranceEnum.PERPENDICULARITY.getTolerance(), perpendicularityInput);
        map.put(ToleranceEnum.TRUEPOSITION.getTolerance(), truePositionInput);
        map.put(ToleranceEnum.PROFILESURFACE.getTolerance(), profileSurfaceInput);
        map.put(ToleranceEnum.ROUGHNESSRA.getTolerance(), roughnessInput);
        map.put(ToleranceEnum.ROUGHNESSRZ.getTolerance(), roughnessRzInput);
        map.put(ToleranceEnum.RUNOUT.getTolerance(), runoutInput);
        map.put(ToleranceEnum.STRAIGHTNESS.getTolerance(), straightnessInput);
        map.put(ToleranceEnum.SYMMETRY.getTolerance(), symmetryInput);
        map.put(ToleranceEnum.TOLERANCE.getTolerance(), toleranceInput);
        map.put(ToleranceEnum.TOTALRUNOUT.getTolerance(), totalRunoutInput);

        return map;
    }

    private Map<String, WebElement> getMap() {
        if (map.isEmpty()) {
            map = buildMap();
        }
        return map;
    }

    private WebElement getLocatorFromMap(String toleranceName) {
        return getMap().get(toleranceName);
    }

    /**
     * Selects the apply button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return generic page object
     */
    public <T> T apply(Class<T> className) {
        pageUtils.javaScriptClick(applyButton);
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