package main.java.pages.explore;

import main.java.header.ExploreHeader;
import main.java.pages.evaluate.EvaluatePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExplorePage extends ExploreHeader {

    private final Logger logger = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(css = "select[data-ap-field='filter'] option")
    private List<WebElement> workspaceDropdownList;

    @FindBy(css = "select.form-control.input-md.auto-width")
    private WebElement workspaceDropdown;

    @FindBy(css = "button[data-ap-nav-dialog='showScenarioSearchCriteria']")
    private WebElement filterButton;

    @FindBy(css = "span[data-ap-comp='resultCount']")
    private WebElement objectsFound;

    @FindBy(css = "button[data-ap-nav-dialog='showTableViewEditor']")
    private WebElement columnsButton;

    @FindBy(css = "button[data-ap-comp='togglePreviewButton']")
    private WebElement previewButton;

    @FindBy(css = "div[data-ap-comp='componentTable'] div.v-grid-scroller-vertical")
    private WebElement componentScroller;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ExplorePage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementsToAppear(workspaceDropdownList);
    }

    /**
     * Selects the workspace from the dropdown
     *
     * @param workspace - workspace dropdown
     * @return current page object
     */
    public ExplorePage selectWorkSpace(String workspace) {
        pageUtils.selectDropdownOption(workspaceDropdown, workspace);
        return this;
    }

    /**
     * Find specific element in the table
     * @param partName - name of the part
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findScenario(String scenarioName, String partName) {
        By scenario = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElement(scenario, componentScroller);
    }

    /**
     * Highlights the scenario in the table
     * @param scenarioName - scenario name
     * @param partName - name of the part
     */
    public void highlightScenario(String scenarioName, String partName) {
        By scenario = By.xpath("//div[@data-ap-comp='componentTable']//a[contains(@href,'#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::td");
        pageUtils.scrollToElement(scenario, componentScroller).click();
    }

    /**
     * Gets the number of elements present on the page
     * @param scenarioName - scenario name
     * @param partName - part name
     * @return size of the element as int
     */
    public int getListOfScenarios(String scenarioName, String partName) {
        By scenario = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElements(scenario, componentScroller).size();
    }

    /**
     * Find specific scenario in the table
     * @param comparisonName - name of the scenario
     * @return the scenario as webelement
     */
    public WebElement findComparison(String comparisonName) {
        By comparison = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,comparisonState," + comparisonName.toUpperCase() + "']");
        return pageUtils.scrollToElement(comparison, componentScroller);
    }

    /**
     * Gets the number of elements present on the page
     * @param comparisonName - scenario name
     * @return size of the element as int
     */
    public int getListOfComparisons(String comparisonName) {
        By comparison = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,comparisonState," + comparisonName.toUpperCase() + "']");
        return pageUtils.scrollToElements(comparison, componentScroller).size();
    }

    /**
     * Gets the number of elements present on the page
     * @param comparisonName - scenario name
     * @return size of the element as int
     */
    public int getListOfComparisons(String comparisonName) {
        By comparison = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,comparisonState," + comparisonName.toUpperCase() + "']");
        return pageUtils.scrollToElements(comparison, componentScroller).size();
    }

    /**
     * Opens the scenario
     * @param partName - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openScenario(String scenarioName, String partName) {
        findScenario(scenarioName, partName).click();
        return new EvaluatePage(driver);
    }

    /**
     * Selects filter criteria button
     * @return new page object
     */
    public FilterCriteriaPage filterCriteria() {
        pageUtils.waitForElementToAppear(filterButton).click();
        return new FilterCriteriaPage(driver);
    }

    /**
     * Selects the table column button
     * @return new page object
     */
    public TableColumnsPage openColumnsTable() {
        pageUtils.waitForElementToAppear(columnsButton).click();
        return new TableColumnsPage(driver);
    }
}