package com.apriori.pageobjects.pages.explore;

import com.apriori.apibase.http.builder.common.response.common.ToleranceValuesEntity;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.pageobjects.header.ExploreHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.utils.APIAuthentication;
import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

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

    @FindBy(css = "[data-ap-comp='closePreviewButton'] .glyphicon-remove")
    private WebElement closePreviewButton;

    @FindBy(css = "[data-ap-comp='previewPanel']")
    private WebElement previewPanelData;

    @FindBy(css = "div[data-ap-comp='componentTable'] div.v-grid-scroller-vertical")
    private WebElement componentScroller;

    @FindBy(css = ".v-grid-header")
    private WebElement columnHeaders;

    @FindBy(css = "div[data-ap-comp='noComponentsMessage']")
    private WebElement noComponentText;

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
     * Opens the scenario
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openScenario(String scenarioName, String partName) {
        pageUtils.waitForElementToAppear(findScenario(scenarioName, partName));
        findScenario(scenarioName, partName).click();
        return new EvaluatePage(driver);
    }

    /**
     * Opens the comparison
     *
     * @param comparisonName - the comparison name
     * @return new page object
     */
    public ComparePage openComparison(String comparisonName) {
        pageUtils.waitForElementToAppear(findComparison(comparisonName));
        findComparison(comparisonName).click();
        return new ComparePage(driver);
    }

    /**
     * Find specific element in the table
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findScenario(String scenarioName, String partName) {
        By scenario = By.xpath("//a[contains(@href,'#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]");
        return pageUtils.scrollToElement(scenario, componentScroller, Constants.PAGE_DOWN);
    }

    /**
     * Highlights the scenario in the table
     *
     * @param scenarioName - scenario name
     * @param partName     - name of the part
     */
    public void highlightScenario(String scenarioName, String partName) {
        By scenario = By.xpath("//a[contains(@href,'#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::td");
        pageUtils.scrollToElement(scenario, componentScroller, Constants.PAGE_DOWN);
        pageUtils.waitForElementToAppear(scenario);
        pageUtils.javaScriptClick(driver.findElement(scenario));
    }

    /**
     * Opens the part
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openAssembly(String scenarioName, String partName) {
        pageUtils.waitForElementToAppear(findAssembly(scenarioName, partName));
        findAssembly(scenarioName, partName).click();
        return new EvaluatePage(driver);
    }

    /**
     * Find specific element in the table
     *
     * @param partName     - name of the assembly
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findAssembly(String scenarioName, String partName) {
        By scenario = By.cssSelector("a[href*='#openFromSearch::sk,assemblyState," + partName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElement(scenario, componentScroller, Constants.PAGE_DOWN);
    }

    /**
     * Highlights the assembly in the table
     *
     * @param scenarioName - scenario name
     * @param partName     - name of the assembly
     */
    public void highlightAssembly(String scenarioName, String partName) {
        By scenario = By.xpath("//a[contains(@href,'#openFromSearch::sk,assemblyState," + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::td");
        pageUtils.scrollToElement(scenario, componentScroller, Constants.PAGE_DOWN);
        pageUtils.javaScriptClick(driver.findElement(scenario));
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param scenarioName - scenario name
     * @param partName     - part name
     * @return size of the element as int
     */
    public int getListOfScenarios(String scenarioName, String partName) {
        By scenario = By.cssSelector("a[href*='#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElements(scenario, componentScroller, Constants.PAGE_DOWN).size();
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param scenarioName - scenario name
     * @param partName     - part name
     * @return size of the element as int
     */
    public int getListOfAssemblies(String scenarioName, String partName) {
        By assembly = By.cssSelector("a[href*='#openFromSearch::sk,assemblyState," + partName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElements(assembly, componentScroller, Constants.PAGE_DOWN).size();
    }

    /**
     * Find specific scenario in the table
     *
     * @param comparisonName - name of the scenario
     * @return the scenario as webelement
     */
    public WebElement findComparison(String comparisonName) {
        By comparison = By.xpath("//a[contains(@href,'#openFromSearch::sk,comparisonState," + comparisonName.toUpperCase() + "')]");
        return pageUtils.scrollToElement(comparison, componentScroller, Constants.PAGE_DOWN);
    }

    /**
     * Highlights the comparison in the table
     *
     * @param comparisonName - the comparison name
     * @return the scenario as webelement
     */
    public ExplorePage highlightComparison(String comparisonName) {
        By comparison = By.xpath("//a[contains(@href,'#openFromSearch::sk,comparisonState," + comparisonName.toUpperCase() + "')]/ancestor::td");
        pageUtils.scrollToElement(comparison, componentScroller, Constants.PAGE_DOWN);
        pageUtils.javaScriptClick(driver.findElement(comparison));
        return this;
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param comparisonName - scenario name
     * @return size of the element as int
     */
    public int getListOfComparisons(String comparisonName) {
        By comparison = By.xpath("//div[@title='" + comparisonName.toUpperCase() + "']");
        return pageUtils.scrollToElements(comparison, componentScroller, Constants.PAGE_DOWN).size();
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
     * Selects the table column button
     *
     * @return new page object
     */
    public TableColumnsPage openColumnsTable() {
        pageUtils.waitForElementToAppear(columnsButton).click();
        return new TableColumnsPage(driver);
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
        return Arrays.stream(columnHeaders.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets the no component message
     *
     * @return string
     */
    public String getNoComponentText() {
        return pageUtils.waitForElementToAppear(noComponentText).getText();
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
    public ExplorePage sortColumnAscending(String columnName) {
        return setColumn(columnName, "sort-asc");
    }

    /**
     * Sorts column in descending order
     *
     * @param columnName - column name
     * @return current page object
     */
    public ExplorePage sortColumnDescending(String columnName) {
        sortColumnAscending(columnName);
        return setColumn(columnName, "sort-desc");
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

    /**
     * Sets the column order
     *
     * @param columnName - column name
     * @param order      - column order
     * @return current page object
     */
    private ExplorePage setColumn(String columnName, String order) {
        By column = By.xpath("//div[.='" + columnName + "']/ancestor::th");

        if (pageUtils.waitForElementToAppear(column).getAttribute("outerHTML").contains(order)) {
            return this;
        } else {
            pageUtils.waitForElementAndClick(column);
        }
        return this;
    }

    /**
     * Gets the api value of the cad threshold
     * @param username - logged in user username
     * @param field - the field
     * @return string
     */
    public String getToleranceThreshold(String username, String field) {

        List<String[]> apiProperties = Arrays.stream(new HTTPRequest()
            .unauthorized()
            .customizeRequest().setHeaders(new APIAuthentication().initAuthorizationHeader(username))
            .setEndpoint(Constants.getBaseUrl() + "ws/workspace/users/me/tolerance-policy-defaults")
            .setAutoLogin(false)
            .setReturnType(ToleranceValuesEntity.class)
            .commitChanges()
            .connect()
            .getJSON()
            .split("\n")).map(option -> option.split(":")).collect(Collectors.toList());

        Map<String, String> apiMap = new HashMap<>();

        for (int i = 0; i < apiProperties.size(); i++) {
            String[] apiValues = apiProperties.get(i);
            for (String apiValue : apiValues) {
                if (apiValue.replace("\"", "").trim().equals(field)) {
                    apiMap.put(apiValues[0].replace("\"", "").trim(), apiValues[1].replace(",", "").replace(" ", ""));

                    return apiMap.get(field);
                }
            }
        }
        return null;
    }
}