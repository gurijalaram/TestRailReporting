package main.java.pages.evaluate.designguidance.tolerances;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToleranceBuilder extends LoadableComponent<ToleranceBuilder> {

    private final Logger logger = LoggerFactory.getLogger(ToleranceBuilder.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

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
    private PageUtils pageUtils;

    public ToleranceBuilder(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(dialogTitle);

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