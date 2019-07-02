package main.java.pages.explore;

import main.java.pages.evaluate.EvaluatePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExplorePage extends LoadableComponent<ExplorePage> {

    private final Logger logger = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(css = "a.dropdown-toggle.text-center span.glyphicon-file")
    private WebElement newFileDropdown;

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

    @FindBy(css = "span.delete-button")
    private WebElement deleteButton;

    @FindBy(css = "span.glyphicons-settings")
    private WebElement actionsDropdown;

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
     * @return visibility of button
     */
    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }

    /**
     * Collective method to upload a file
     * @param scenarioName - the name of the scenario
     * @param filePath - location of the file
     * @param fileName - name of the file
     * @return current page object
     */
    public ExplorePage uploadFile(String scenarioName, String filePath, String fileName) {
        newFileDropdown.click();
        componentButton.click();
        new FileUploadPage(driver).uploadFile(scenarioName, filePath, fileName);
        return this;
    }

    /**
     * Filter criteria for private selection
     * @param type - type of selection whether private or public
     * @param attribute - the attribute
     * @param condition - specified condition
     * @param value - the value
     * @return current page object
     */
    public ExplorePage filterPrivateCriteria(String type, String attribute, String condition, String value) {
        filterButton.click();
        new FilterCriteriaPage(driver).clearAllCheckBoxes()
            .setPrivateWorkSpace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value)
            .apply();
        return new ExplorePage(driver);
    }

    /**
     * Filter criteria for public selection
     * @param type - type of selection whether private or public
     * @param attribute - the attribute
     * @param condition - specified condition
     * @param value - the value
     * @return current page object
     */
    public ExplorePage filterPublicCriteria(String type, String attribute, String condition, String value) {
        filterButton.click();
        new FilterCriteriaPage(driver).clearAllCheckBoxes()
            .setPublicWorkspace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value)
            .apply();
        return new ExplorePage(driver);
    }

    /**
     * Selects the workspace from the dropdown
     * @param workspace - workspace dropdown
     * @return current page object
     */
    public ExplorePage selectWorkSpace(String workspace) {
        new Select(workspaceDropdown).selectByVisibleText(workspace);
        return this;
    }

    /**
     * Find specific element in the table
     * @param partName - name of the part
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findScenario(String partName, String scenarioName) {
        By scenario = By.cssSelector("div[data-ap-comp='componentTable'] a[href*='#openFromSearch::sk,partState," + partName + "," + scenarioName + "']");
        return pageUtils.scrollToElement(scenario, componentScroller);
    }

    /**
     * Opens the scenario
     * @param partName - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openScenario(String partName, String scenarioName) {
        findScenario(partName, scenarioName).click();
        return new EvaluatePage(driver);
    }
}
