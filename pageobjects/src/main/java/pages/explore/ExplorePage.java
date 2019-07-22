package main.java.pages.explore;

import main.java.pages.compare.ComparePage;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.settings.SettingsPage;
import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExplorePage extends LoadableComponent<ExplorePage> {

    private final Logger logger = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(css = "a.dropdown-toggle.text-center span.glyphicon-file")
    private WebElement newFileDropdown;

    @FindBy(css = "button[data-ap-comp='editScenarioButton']")
    private WebElement editButton;

    @FindBy(css = "button[data-ap-comp='newComponentButton']")
    private WebElement componentButton;

    @FindBy(css = "button[data-ap-comp='saveAsButton']")
    private WebElement scenarioButton;

    @FindBy(css = "button[data-ap-comp='newComparisonButton']")
    private WebElement comparisonButton;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton']")
    private WebElement publishButton;

    @FindBy(css = "button[data-ap-comp='revertScenarioButton']")
    private WebElement revertButton;

    @FindBy(css = "button[data-ap-comp='deleteScenarioButton']")
    private WebElement deleteButton;

    @FindBy(css = "span.glyphicons-settings")
    private WebElement actionsDropdown;

    @FindBy(css = "button[data-ap-comp='toggleLockButton']")
    private WebElement lockButton;

    @FindBy(css = "button[data-ap-comp='reloadButton']")
    private WebElement cadModelButton;

    @FindBy(css = "button[data-ap-comp='assignScenarioButton']")
    private WebElement assignButton;

    @FindBy(css = "button[data-ap-comp='updateAdminInfoButton']")
    private WebElement scenarioNotesButton;

    @FindBy(css = "button[data-ap-comp='partCostReportButton']")
    private WebElement partCostButton;

    @FindBy(css = "button[data-ap-comp='costComparisonReportButton']")
    private WebElement comparisonReportButton;

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

    @FindBy(css = "a[data-ap-comp='jobQueue']")
    private WebElement jobQueueButton;

    @FindBy(css = "a span.glyphicon-cog")
    private WebElement settingsButton;

    @FindBy(css = "a.navbar-help")
    private WebElement helpButton;

    @FindBy(css = "span.glyphicon-user")
    private WebElement logoutButton;

    @FindBy(css = "div[data-ap-comp='componentTable'] div.v-grid-scroller-vertical")
    private WebElement componentScroller;

    @FindBy(css = "button[data-ap-comp='saveComparisonAsButton']")
    private WebElement saveAsButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ExplorePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(deleteButton);
        pageUtils.waitForElementsToAppear(workspaceDropdownList);
    }

    /**
     * Checks delete button is displayed
     *
     * @return visibility of button
     */
    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }

    /**
     * Collective method to upload a file
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param fileName     - name of the file
     * @return current page object
     */
    public EvaluatePage uploadFile(String scenarioName, String filePath, String fileName) {
        newFileDropdown.click();
        componentButton.click();
        return new FileUploadPage(driver).uploadFile(scenarioName, filePath, fileName);
    }

    /**
     * Selects new scenario button
     *
     * @return new page object
     */
    public ScenarioPage createNewScenario() {
        newFileDropdown.click();
        scenarioButton.click();
        return new ScenarioPage(driver);
    }

    /**
     * Selects new comparison button
     *
     * @return new page object
     */
    public ComparisonPage createNewComparison() {
        newFileDropdown.click();
        comparisonButton.click();
        return new ComparisonPage(driver);
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
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findScenario(String scenarioName, String partName) {
        By scenario = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElement(scenario, componentScroller);
    }

    /**
     * Highlights the scenario in the table
     *
     * @param scenarioName - scenario name
     * @param partName     - name of the part
     */
    public ExplorePage highlightScenario(String scenarioName, String partName) {
        By scenario = By.xpath("//div[@data-ap-comp='componentTable']//a[contains(@href,'#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::td");
        pageUtils.scrollToElement(scenario, componentScroller).click();
        return this;
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
     *
     * @param comparisonName - name of the scenario
     * @return the scenario as webelement
     */
    public WebElement findComparison(String comparisonName) {
        By comparison = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,comparisonState," + comparisonName.toUpperCase() + "']");
        return pageUtils.scrollToElement(comparison, componentScroller);
    }

    /**
     * Highlights the comparison in the table
     * @param comparisonName - the comparison name
     * @return the scenarion as webelement
     */
    public ExplorePage highlightComparison(String comparisonName) {
        By comparison = By.xpath("//div[@data-ap-comp='componentTable']//a[contains(@href,'#openFromSearch::sk,comparisonState," + comparisonName.toUpperCase() + "')]/ancestor::td");
        pageUtils.scrollToElement(comparison, componentScroller);
        return this;
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
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openScenario(String scenarioName, String partName) {
        findScenario(scenarioName, partName).click();
        return new EvaluatePage(driver);
    }

    /**
     * Opens the comparison
     * @param comparisonName - the comparison name
     * @return new page object
     */
    public ComparePage openComparison(String comparisonName) {
        findComparison(comparisonName).click();
        return new ComparePage(driver);
    }

    /**
     * Locks a scenario
     *
     * @return current page object
     */
    public ExplorePage lockScenario() {
        pageUtils.waitForElementToAppear(actionsDropdown).click();
        lockButton.click();
        return this;
    }

    /**
     * Selects assign scenario
     *
     * @return new page object
     */
    public AssignPage selectAssignScenario() {
        pageUtils.waitForElementToAppear(actionsDropdown).click();
        assignButton.click();
        return new AssignPage(driver);
    }

    /**
     * Selects scenario info and notes
     *
     * @return new page object
     */
    public ScenarioNotesPage selectScenarioInfoNotes() {
        pageUtils.waitForElementToAppear(actionsDropdown).click();
        scenarioNotesButton.click();
        return new ScenarioNotesPage(driver);
    }

    /**
     * Opens the settings page
     *
     * @return new page object
     */
    public SettingsPage openSettings() {
        pageUtils.waitForElementToAppear(settingsButton).click();
        return new SettingsPage(driver);
    }

    /**
     * Selects filter criteria button
     *
     * @return new page object
     */
    public FilterCriteriaPage filterCriteria() {
        pageUtils.waitForElementToAppear(filterButton).click();
        return new FilterCriteriaPage(driver);
    }

    /**
     * Selects the job queue button
     *
     * @return new page object
     */
    public void openJobQueue() {
        pageUtils.waitForElementToAppear(jobQueueButton).click();
    }

    /**
     * Selects the help button
     *
     * @retun new page object
     */
    public void openHelp() {
        pageUtils.waitForElementToAppear(helpButton).click();
    }

    /**
     * Selects the logout button
     *
     * @return new page object
     */
    public LogOutPage openLogOut() {
        pageUtils.waitForElementToAppear(logoutButton).click();
        return new LogOutPage(driver);
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
     * Edits the scenario
     *
     * @return new page object
     */
    public EvaluatePage editScenario() {
        pageUtils.waitForElementToAppear(editButton).click();
        return new EvaluatePage(driver);
    }

    /**
     * Deletes the scenario
     *
     * @return new page object
     */
    public DeletePage delete() {
        deleteButton.click();
        return new DeletePage(driver);
    }

    /**
     * Gets the state of save as button
     * @return visibility of button
     */
    public boolean getSaveAsButton() {
        return saveAsButton.isDisplayed();
    }
}