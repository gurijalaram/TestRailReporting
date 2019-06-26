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
    ToleranceEditPage setCircularity(String value) {
        clearInputAndSendKeys(circularityInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setConcentricity(String value) {
        clearInputAndSendKeys(concentricityInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setCylindricity(String value) {
        clearInputAndSendKeys(cylindricityInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setDiamTolerance(String value) {
        clearInputAndSendKeys(diamToleranceInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setParallelism(String value) {
        clearInputAndSendKeys(parallelismInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setPerpendicularity(String value) {
        clearInputAndSendKeys(perpendicularityInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setTruePosition(String value) {
        clearInputAndSendKeys(truePositionInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setProfileSurface(String value) {
        clearInputAndSendKeys(profileSurfaceInput, value);
        return this;
    }

    ToleranceEditPage setRoughnessRa(String value) {
        clearInputAndSendKeys(roughnessInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setRoughnessRz(String value) {
        clearInputAndSendKeys(roughnessRzInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setRunout(String value) {
        clearInputAndSendKeys(runoutInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setStraightness(String value) {
        clearInputAndSendKeys(straightnessInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setSymmetry(String value) {
        clearInputAndSendKeys(symmetryInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setToleranceCoor(String value) {
        clearInputAndSendKeys(toleranceInput, value);
        return this;
    }

    /**
     * @param value
     * @return current page object
     */
    ToleranceEditPage setTotalRunout(String value) {
        clearInputAndSendKeys(totalRunoutInput, value);
        return this;
    }

    /**
     * Private helper method used to clear and input box and sendkeys
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
     *
     * @return new page object
     */
    public TolerancePage apply() {
        applyButton.click();
        return new TolerancePage(driver);
    }

    /**
     * Selects the cancel button
     *
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