package com.apriori.pageobjects.pages.settings;

import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.enums.ToleranceEnum;

import com.sun.xml.bind.v2.TODO;
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

public class ToleranceValueSettingsPage extends LoadableComponent<ToleranceValueSettingsPage> {

    private final Logger logger = LoggerFactory.getLogger(ToleranceValueSettingsPage.class);

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
    private WebElement totalRunoutInput;

    @FindBy(css = "input[data-ap-field='straightnessOverride']")
    private WebElement straightnessInput;

    @FindBy(css = "input[data-ap-field='symmetryOverride']")
    private WebElement symmetryInput;

    @FindBy(css = ".modal-footer button.btn.btn-primary") "button[class='btn btn-primary']"
    TODO fix this
    private WebElement saveButton;

    @FindBy(css = ".modal-footer button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private Map<String, WebElement> map = new HashMap<>();

    public ToleranceValueSettingsPage(WebDriver driver) {
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
     * Checks the value is correct
     *
     * @param toleranceName - the tolerance
     * @param text          - the string value
     * @return true/false
     */
    public Boolean isTolerance(String toleranceName, String text) {
        return pageUtils.checkElementAttribute(getMap().get(toleranceName), "value", text);
    }

    /**
     * Removes the tolerance
     *
     * @param toleranceName - the string value
     * @return
     */
    public ToleranceValueSettingsPage removeTolerance(String toleranceName) {
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
    public ToleranceValueSettingsPage setTolerance(String tolerance, String text) {
        pageUtils.clearInput(getMap().get(tolerance));
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
        map.put(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), bendAngleInput);
        map.put(ToleranceEnum.PROFILESURFACE.getToleranceName(), profileSurfaceInput);
        map.put(ToleranceEnum.ROUGHNESSRA.getToleranceName(), roughnessRaInput);
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
     * Selects the save button
     *
     * @return new page object
     */
    public ToleranceSettingsPage save() {
        saveButton.click();
        return new ToleranceSettingsPage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ToleranceSettingsPage cancel() {
        cancelButton.click();
        return new ToleranceSettingsPage(driver);
    }
}