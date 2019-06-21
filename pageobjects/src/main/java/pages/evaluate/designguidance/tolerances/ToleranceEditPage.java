package main.java.pages.evaluate.designguidance.tolerances;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
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
    private String value;

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
     * Enter input fields.  Tolerance field do not require units eg. only enter Roughness Ra
     *
     * @param toleranceField
     * @param value
     * @return
     */
    public ToleranceEditPage enterToleranceValues(String toleranceField, String value) {
        WebElement tolerance = driver.findElement(By.cssSelector("input[data-ap-field='" + toleranceField + ".current']"));
        pageUtils.clearInput(tolerance);
        tolerance.sendKeys(value);
        return this;
    }

    /**
     * Selects the apply button
     * @return new page object
     */
    protected TolerancePage apply() {
        applyButton.click();
        return new TolerancePage(driver);
    }

    /**
     * Selects the cancel button
     * @return new page object
     */
    protected TolerancePage cancel() {
        cancelButton.click();
        return new TolerancePage(driver);
    }
}