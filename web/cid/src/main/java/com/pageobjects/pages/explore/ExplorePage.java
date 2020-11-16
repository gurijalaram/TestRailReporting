package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import com.pageobjects.common.ScenarioTablePage;
import com.pageobjects.pages.compare.ComparePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.toolbars.ExploreHeader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author cfrith
 */

public class ExplorePage extends ExploreHeader {

    private final Logger logger = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(css = "select[data-ap-field='filter'] option")
    private List<WebElement> workspaceDropdownList;

    @FindBy(css = "select.form-control.input-md.auto-width")
    private WebElement workspaceDropdown;

    @FindBy(css = "button[data-ap-comp='togglePreviewButton']")
    private WebElement previewButton;

    @FindBy(css = "[data-ap-comp='closePreviewButton'] .glyphicon-remove")
    private WebElement closePreviewButton;

    @FindBy(css = "[data-ap-comp='previewPanel']")
    private WebElement previewPanelData;

    private ScenarioTablePage scenarioTablePage;
    private PageUtils pageUtils;
    private WebDriver driver;

    public ExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTablePage = new ScenarioTablePage(driver);
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
        scenarioTablePage.selectWorkSpace(workspace);
        return this;
    }

    /**
     * Opens the scenario
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openScenario(String scenarioName, String partName) {
        return scenarioTablePage.openScenario(scenarioName, partName.toUpperCase());
    }

    /**
     * Opens first scenario in list
     * @return new page object
     */
    public EvaluatePage openFirstScenario() {
        pageUtils.waitForElementAndClick(driver.findElements(By.cssSelector("a[class='gwt-Anchor']")).get(0));
        return new EvaluatePage(driver);
    }

    /**
     * Opens the comparison
     *
     * @param comparisonName - the comparison name
     * @return new page object
     */
    public ComparePage openComparison(String comparisonName) {
        return scenarioTablePage.openComparison(comparisonName);
    }

    /**
     * Find specific element in the table
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findScenario(String scenarioName, String partName) {
        return scenarioTablePage.findScenario(scenarioName, partName);
    }

    /**
     * Highlights the scenario in the table
     *
     * @param scenarioName - scenario name
     * @param partName     - name of the part
     */
    public void highlightScenario(String scenarioName, String partName) {
        scenarioTablePage.highlightScenario(scenarioName, partName);
    }

    /**
     * Opens the part
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openAssembly(String scenarioName, String partName) {
        return scenarioTablePage.openAssembly(scenarioName, partName.toUpperCase());
    }

    /**
     * Find specific element in the table
     *
     * @param partName     - name of the assembly
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findAssembly(String scenarioName, String partName) {
        return scenarioTablePage.findAssembly(scenarioName, partName);
    }

    /**
     * Highlights the assembly in the table
     *
     * @param scenarioName - scenario name
     * @param partName     - name of the assembly
     */
    public void highlightAssembly(String scenarioName, String partName) {
        scenarioTablePage.highlightAssembly(scenarioName, partName);
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param scenarioName - scenario name
     * @param partName     - part name
     * @return size of the element as int
     */
    public int getListOfScenarios(String scenarioName, String partName) {
        return scenarioTablePage.getListOfScenarios(scenarioName, partName);
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param scenarioName - scenario name
     * @param partName     - part name
     * @return size of the element as int
     */
    public int getListOfAssemblies(String scenarioName, String partName) {
        return scenarioTablePage.getListOfAssemblies(scenarioName, partName);
    }

    /**
     * Find specific scenario in the table
     *
     * @param comparisonName - name of the scenario
     * @return the scenario as webelement
     */
    public WebElement findComparison(String comparisonName) {
        return scenarioTablePage.findComparison(comparisonName);
    }

    /**
     * Highlights the comparison in the table
     *
     * @param comparisonName - the comparison name
     * @return the scenario as webelement
     */
    public ExplorePage highlightComparison(String comparisonName) {
        return scenarioTablePage.highlightComparison(comparisonName);
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param comparisonName - scenario name
     * @return size of the element as int
     */
    public int getListOfComparisons(String comparisonName) {
        return scenarioTablePage.getListOfComparisons(comparisonName);
    }

    /**
     * Selects filter criteria button
     *
     * @return new page object
     */
    public FilterCriteriaPage filter() {
        return scenarioTablePage.filter();
    }

    /**
     * Selects the table column button
     *
     * @return new page object
     */
    public TableColumnsPage openColumnsTable() {
        return scenarioTablePage.openColumnsTable();
    }

    /**
     * Opens the preview panel
     *
     * @return new page object
     */
    public PreviewPanelPage openPreviewPanel() {
        if (pageUtils.isElementDisplayed(closePreviewButton)) {
            closePreviewButton.click();
        }
        pageUtils.waitForElementToAppear(previewButton).click();
        return new PreviewPanelPage(driver);
    }

    /**
     * Gets the data in the preview panel
     *
     * @return current page object
     */
    public boolean viewPreviewPanelData() {
        return previewPanelData.isDisplayed();
    }

    /**
     * Gets all column headers in the table
     *
     * @return column headers as string
     */
    public List<String> getColumnHeaderNames() {
        return scenarioTablePage.getColumnHeaderNames();
    }

    /**
     * Gets the no component message
     *
     * @return string
     */
    public String getNoComponentText() {
        return scenarioTablePage.getNoComponentText();
    }

    /**
     * Refreshes the page
     * todo - intentionally on this page and not in pageutils because only this page will need to be reloaded
     * todo - for uploading files multiple times in the same test
     *
     * @return current page object
     */
    public ExplorePage refreshCurrentPage() {
        driver.navigate().refresh();
        return this;
    }

    /**
     * Sorts column in ascending order
     *
     * @param columnName - column name
     * @return current page object
     */
    public ScenarioTablePage sortColumnAscending(String columnName) {
        return scenarioTablePage.sortColumnAscending(columnName);
    }

    /**
     * Sorts column in descending order
     *
     * @param columnName - column name
     * @return current page object
     */
    public ExplorePage sortColumnDescending(String columnName) {
        scenarioTablePage.sortColumnDescending(columnName);
        return this;
    }

    /**
     * Gets the current order of the column
     *
     * @param columnName - column name
     * @return current page object
     */
    public String getColumnOrder(String columnName) {
        By column = By.xpath("//div[.='" + columnName + "']/ancestor::th");
        return driver.findElement(column).getAttribute("outerHTML");
    }
}