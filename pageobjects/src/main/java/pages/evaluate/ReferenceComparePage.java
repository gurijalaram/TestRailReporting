package main.java.pages.evaluate;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReferenceComparePage extends LoadableComponent<ReferenceComparePage> {

    private final Logger logger = LoggerFactory.getLogger(ReferenceComparePage.class);

    @FindBy(css = "[data-ap-comp='baselineScenarioSelection']")
    private WebElement filterDropdown;

    @FindBy(css = "div[data-ap-region='inputsTile']")
    private WebElement inputsTile;

    @FindBy(css = "div[data-ap-region='materialTile']")
    private WebElement materialUtilizationTile;

    @FindBy(css = "div[data-ap-region='costDriversGuidanceTile']")
    private WebElement designGuidanceTile;

    @FindBy(css = "div[data-ap-region='cycleTimeTile']")
    private WebElement processTile;

    @FindBy(css = "div[data-ap-region='costResultsTile']")
    private WebElement costResultsTile;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ReferenceComparePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(filterDropdown);
    }

    /**
     * Gets input tile info
     * @return input as string
     */
    public List<String> getInputsTile() {
        return Arrays.stream(inputsTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets material utilization info
     * @return material utilization as string
     */
    public List<String> getMaterialsUtilizationTile() {
        return Arrays.stream(materialUtilizationTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets design guidance info
     * @return design guidance as string
     */
    public List<String> getDesignGuidanceTile() {
        return Arrays.stream(designGuidanceTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets process tile info
     * @return process tile as string
     */
    public List<String> getProcessesTile() {
        return Arrays.stream(processTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets cost result info
     * @return cost result as string
     */
    public List<String> getCostResultsTile() {
        return Arrays.stream(costResultsTile.getText().split("\n")).collect(Collectors.toList());
    }
}