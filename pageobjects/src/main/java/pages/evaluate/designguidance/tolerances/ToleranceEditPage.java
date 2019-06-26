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
    private ToleranceEditPage setTotalRunout(String value) {
        pageUtils.clearInput(totalRunoutInput);
        totalRunoutInput.sendKeys(value);
        return new ToleranceEditPage(driver);
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

    public static class ToleranceBuilder {
        private String circularity;
        private String concentricity;
        private String cylindricity;
        private String diamtolerance;
        private String parallelism;
        private String perpendicularity;
        private String truePosition;
        private String profileSurface;
        private String roughnessRa;
        private String roughnessRz;
        private String runout;
        private String straightness;
        private String symmetry;
        private String toleranceCoor;
        private String totalRunout;
        private WebDriver driver;

        public ToleranceBuilder(WebDriver driver) {
            this.driver = driver;
        }

        /**
         * @param circularity
         * @return
         */
        public ToleranceBuilder setCircularity(String circularity) {
            this.circularity = circularity;
            return this;
        }

        /**
         * @param concentricity
         * @return
         */
        public ToleranceBuilder setConcentricity(String concentricity) {
            this.concentricity = concentricity;
            return this;
        }

        /**
         * @param cylindricity
         */
        public ToleranceBuilder setCylindricity(String cylindricity) {
            this.cylindricity = cylindricity;
            return this;
        }

        /**
         * @param diamtolerance
         */
        public ToleranceBuilder setDiamtolerance(String diamtolerance) {
            this.diamtolerance = diamtolerance;
            return this;
        }

        /**
         * @param parallelism
         */
        public ToleranceBuilder setParallelism(String parallelism) {
            this.parallelism = parallelism;
            return this;
        }

        /**
         * @param perpendicularity
         */
        public ToleranceBuilder setPerpendicularity(String perpendicularity) {
            this.perpendicularity = perpendicularity;
            return this;
        }

        /**
         * @param truePosition
         */
        public ToleranceBuilder setTruePosition(String truePosition) {
            this.truePosition = truePosition;
            return this;
        }

        /**
         * @param profileSurface
         */
        public ToleranceBuilder setProfileSurface(String profileSurface) {
            this.profileSurface = profileSurface;
            return this;
        }

        /**
         * @param roughnessRa
         */
        public ToleranceBuilder setRoughnessRa(String roughnessRa) {
            this.roughnessRa = roughnessRa;
            return this;
        }

        /**
         * @param roughnessRz
         */
        public ToleranceBuilder setRoughnessRz(String roughnessRz) {
            this.roughnessRz = roughnessRz;
            return this;
        }

        /**
         * @param runout
         */
        public ToleranceBuilder setRunout(String runout) {
            this.runout = runout;
            return this;
        }

        /**
         * @param straightness
         */
        public ToleranceBuilder setStraightness(String straightness) {
            this.straightness = straightness;
            return this;
        }

        /**
         * @param symmetry
         */
        public ToleranceBuilder setSymmetry(String symmetry) {
            this.symmetry = symmetry;
            return this;
        }

        /**
         * @param toleranceCoor
         */
        public ToleranceBuilder setToleranceCoor(String toleranceCoor) {
            this.toleranceCoor = toleranceCoor;
            return this;
        }

        /**
         * @param totalRunout
         */
        public ToleranceBuilder setTotalRunout(String totalRunout) {
            this.totalRunout = totalRunout;
            return this;
        }

        public ToleranceEditPage build() {
            ToleranceEditPage toleranceEditPage = new ToleranceEditPage(driver);
            toleranceEditPage.setCircularity(this.circularity);
            toleranceEditPage.setConcentricity(this.concentricity);
            toleranceEditPage.setCylindricity(this.cylindricity);
            toleranceEditPage.setDiamTolerance(this.diamtolerance);
            toleranceEditPage.setParallelism(this.parallelism);
            toleranceEditPage.setPerpendicularity(this.perpendicularity);
            toleranceEditPage.setTruePosition(this.truePosition);
            toleranceEditPage.setProfileSurface(this.profileSurface);
            toleranceEditPage.setRoughnessRa(this.roughnessRa);
            toleranceEditPage.setRoughnessRz(this.roughnessRz);
            toleranceEditPage.setRunout(this.runout);
            toleranceEditPage.setStraightness(this.straightness);
            toleranceEditPage.setSymmetry(this.symmetry);
            toleranceEditPage.setToleranceCoor(this.toleranceCoor);
            toleranceEditPage.setTotalRunout(this.totalRunout);
            return new ToleranceEditPage(driver);
        }
    }
}