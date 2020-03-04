package com.apriori.pageobjects.pages.compare;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
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

public class ComparePage extends LoadableComponent<ComparePage> {

    private final Logger logger = LoggerFactory.getLogger(ComparePage.class);

    @FindBy(css = "table.comparison-table-header-widget-table")
    private WebElement scenarioTable;

    @FindBy(css = "div.gwt-Label.comparison-table-header-part-number")
    private WebElement comparisonName;

    @FindBy(css = "textarea.gwt-TextArea.full-width")
    private WebElement descriptionText;

    @FindBy(css = "button.comparison-table-add-scenarios-button")
    private WebElement addScenariosButton;

    @FindBy(css = "[class='locked-status-icon fa fa-lock']")
    private WebElement lockedIcon;

    @FindBy(css = "[class='locked-status-icon fa fa-unlock']")
    private WebElement unlockedIcon;

    @FindBy(css = "[data-ap-comp='loadingComparisonData']")
    private WebElement loadingComparisonData;

    @FindBy(css = "div[data-ap-comp='scenarioTiles'] div.v-grid-scroller-horizontal")
    private WebElement horizontalScroller;


    private WebDriver driver;
    private PageUtils pageUtils;

    public ComparePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(scenarioTable);
    }

    /**
     * Adds a new scenario
     *
     * @return new page object
     */
    public ComparisonTablePage addScenario() {
        pageUtils.waitForElementAndClick(addScenariosButton);
        return new ComparisonTablePage(driver);
    }

    /**
     * Gets the comparison description text
     *
     * @return the text as String
     */
    public String getDescriptionText() {
        return descriptionText.getText();
    }

    /**
     * Checks the comparison name
     *
     * @param text - the text
     * @return true/false
     */
    public boolean isComparisonName(String text) {
        return pageUtils.checkElementAttribute(comparisonName, "title", text);
    }

    /**
     * Checks if the comparison is locked
     *
     * @param text - the text
     * @return true/false
     */
    public boolean isComparisonLocked(String text) {
        return pageUtils.checkElementAttribute(lockedIcon, "title", text);
    }

    /**
     * Checks if the comparison is unlocked
     *
     * @param text - the text
     * @return true/false
     */
    public boolean isComparisonUnlocked(String text) {
        return pageUtils.checkElementAttribute(unlockedIcon, "title", text);
    }

    /**
     * Checks if the comparison is being updated
     *
     * @return current page object
     */
    public ComparePage checkComparisonUpdated() {
        pageUtils.checkElementAttribute(loadingComparisonData, "style", "display: none;");
        return this;
    }

    /**
     * Removes the scenario from the comparison view
     * @param partName - the part name
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ComparePage removeComparison(String partName, String scenarioName) {
        By removeComparisonButton = By.xpath(String.format("//button[contains(@id,'rm_comp_btn_part_" + "%s" + "_" + "%s')]",
            partName.replace(" ", "_"), scenarioName.replace("-", "_")).toLowerCase());
        pageUtils.scrollHorizontally(removeComparisonButton, horizontalScroller).click();
        return this;
    }

    /**
     * Selects the basis button
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ComparePage setBasis(String scenarioName) {
        By basisButton = By.xpath(String.format("//a[contains(text(),'%s')]/ancestor::th//button", scenarioName));
        pageUtils.scrollHorizontally(basisButton, horizontalScroller).click();
        return this;
    }

    /**
     * Gets list of comparisons
     * @param scenarioName
     * @param partName
     * @return
     */
    public int getScenariosInComparisonView(String scenarioName, String partName) {
        By scenario = By.cssSelector(String.format("a[href*='#openFromSearch::sk,partState," + "%s" + "," + "%s" + "']", partName.toUpperCase(), scenarioName));
        return driver.findElements(scenario).size();
    }
}
