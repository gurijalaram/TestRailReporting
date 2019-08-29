package main.java.pages.evaluate;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferenceComparePage extends LoadableComponent<ReferenceComparePage> {

    private final Logger logger = LoggerFactory.getLogger(ReferenceComparePage.class);

    @FindBy(css = "[data-ap-comp='baselineScenarioSelection']")
    private WebElement filterDropdown;

    @FindBy(css = "td[data-ap-field='dtcMessagesCount.percentDifference']")
    private WebElement dtcPercentDifference;

    @FindBy(css = "td[data-ap-field='dtcMessagesCount.baseline']")
    private WebElement dtcBaseLine;

    @FindBy(css = "td[data-ap-field='failuresWarningsCount.baseline']")
    private WebElement failuresBaseline;

    @FindBy(css = "td[data-ap-field='gcdWithTolerancesCount.baseline']")
    private WebElement gcdBaseline;

    @FindBy(css = "td[data-ap-field='cycleTime.percentDifference']")
    private WebElement cycleTimePercentage;

    @FindBy(css = "td[data-ap-field='cycleTime.baseline']")
    private WebElement cycleBaseLine;

    @FindBy(css = "td[data-ap-field='materialCost.percentDifference']")
    private WebElement materialDifference;

    @FindBy(css = "td[data-ap-field='materialCost.baseline']")
    private WebElement materialBaseline;

    @FindBy(css = "td[data-ap-field='totalCost.percentDifference']")
    private WebElement totalPercentDifference;

    @FindBy(css = "td[data-ap-field='totalCost.baseline']")
    private WebElement totalBaseline;

    @FindBy(css = "td[data-ap-field='fullyBurdenedCost.percentDifference']")
    private WebElement burdenedDifference;

    @FindBy(css = "td[data-ap-field='fullyBurdenedCost.baseline']")
    private WebElement burdenedBaseline;

    @FindBy(css = "td[data-ap-field='capitalInvestment.percentDifference']")
    private WebElement capitalDifference;

    @FindBy(css = "td[data-ap-field='capitalInvestment.baseline']")
    private WebElement capitalBaseline;

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
     * Gets dtc percentage difference
     * @return string
     */
    public String getDTCPercentDifference() {
        return pageUtils.waitForElementToAppear(dtcPercentDifference).getText();
    }

    /**
     * Gets dtc baseline
     * @return string
     */
    public String getDTCBaseLine() {
        return pageUtils.waitForElementToAppear(dtcBaseLine).getText();
    }

    /**
     * Gets failures baseline
     * @return string
     */
    public String getFailuresBaseline() {
        return pageUtils.waitForElementToAppear(failuresBaseline).getText();
    }

    /**
     * Gets gcd baseline
     * @return string
     */
    public String getGCDBaseline() {
        return pageUtils.waitForElementToAppear(gcdBaseline).getText();
    }

    /**
     * Gets cycle percentage difference
     * @return string
     */
    public String getCycleTimePercentage() {
        return pageUtils.waitForElementToAppear(cycleTimePercentage).getText();
    }

    /**
     * Gets cycle baseline
     * @return string
     */
    public String getCycleBaseLine() {
        return pageUtils.waitForElementToAppear(cycleBaseLine).getText();
    }

    /**
     * Gets material difference
     * @return string
     */
    public String getMaterialDifference() {
        return pageUtils.waitForElementToAppear(materialDifference).getText();
    }

    /**
     * Gets material baseline
     * @return string
     */
    public String getMaterialBaseline() {
        return pageUtils.waitForElementToAppear(materialBaseline).getText();
    }

    /**
     * Gets total percentage difference
     * @return string
     */
    public String getTotalPercentDifference() {
        return pageUtils.waitForElementToAppear(totalPercentDifference).getText();
    }

    /**
     * Gets total baseline
     * @return string
     */
    public String getTotalBaseline() {
        return pageUtils.waitForElementToAppear(totalBaseline).getText();
    }

    /**
     * Gets burdened difference
     * @return string
     */
    public String getBurdenedDifference() {
        return pageUtils.waitForElementToAppear(burdenedDifference).getText();
    }

    /**
     * Gets burdened baseline
     * @return string
     */
    public String getBurdenedBaseline() {
        return pageUtils.waitForElementToAppear(burdenedBaseline).getText();
    }

    /**
     * Gets capital difference
     * @return string
     */
    public String getCapitalDifference() {
        return pageUtils.waitForElementToAppear(capitalDifference).getText();
    }

    /**
     * Gets capital baseline
     * @return string
     */
    public String getCapitalBaseline() {
        return pageUtils.waitForElementToAppear(capitalBaseline).getText();
    }
}