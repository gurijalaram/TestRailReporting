package com.apriori.pageobjects.pages.compare;

import com.apriori.pageobjects.pages.explore.FilterCriteriaPage;
import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.constants.Constants;

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

public class ComparisonTablePage extends LoadableComponent<ComparisonTablePage> {

    private final Logger logger = LoggerFactory.getLogger(ComparisonTablePage.class);

    @FindBy(css = "div[data-ap-comp='componentTable'] .v-grid-row-has-data")
    private WebElement workspaceRow;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "div[data-ap-comp='componentTable'] div.v-grid-scroller-vertical")
    private WebElement comparisonScroller;

    @FindBy(css = "button[data-ap-nav-dialog='showScenarioSearchCriteria']")
    private WebElement filterButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComparisonTablePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(workspaceRow);
    }

    /**
     * Selects the comparison in the table
     *
     * @param partName     - the name of the part
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ComparisonTablePage selectScenario(String scenarioName, String partName) {
        findScenario(scenarioName, partName);
        WebElement scenario = driver.findElement(By.xpath("//a[contains(@href,'" + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::tr//input[@class]"));
        pageUtils.javaScriptClick(scenario);
        return this;
    }

    /**
     * Find and select the comparison in the table
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return comparison as webelement
     */
    public WebElement findScenario(String scenarioName, String partName) {
        By comparison = By.xpath("//a[contains(@href,'" + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::tr//input[@class]");
        return pageUtils.scrollToElement(comparison, comparisonScroller, Constants.PAGE_DOWN);
    }

    /**
     * Selects filter criteria button
     *
     * @return new page object
     */
    public FilterCriteriaPage filterCriteria() {
        pageUtils.waitForElementAndClick(filterButton);
        return new FilterCriteriaPage(driver);
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public ComparePage apply() {
        pageUtils.waitForElementAndClick(applyButton);
        pageUtils.waitFor(45000);
        return new ComparePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ComparePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return new ComparePage(driver);
    }
}