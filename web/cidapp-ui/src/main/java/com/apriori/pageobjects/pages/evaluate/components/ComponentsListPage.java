package com.apriori.pageobjects.pages.evaluate.components;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentPrimaryPage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ComponentsListPage extends LoadableComponent<ComponentsListPage> {

    private final Logger logger = LoggerFactory.getLogger(ComponentsListPage.class);

    @FindBy(css = "[id='qa-scenario-list-table-view-button'] button")
    private WebElement listButton;

    @FindBy(css = "[id='qa-scenario-list-card-view-button'] button")
    private WebElement treeButton;

    @FindBy(css = "[id='qa-sub-component-detail-preview-button'] button")
    private WebElement previewButton;

    @FindBy(xpath = "//button[.='Selection']")
    private WebElement selectionButton;

    @FindBy(id = "qa-sub-component-action-bar-upload-button")
    private WebElement uploadButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-set-inputs-button'] button")
    private WebElement setInputsButton;

    @FindBy(id = "qa-sub-component-detail-configure-button")
    private WebElement configureButton;

    @FindBy(id = "qa-sub-component-detail-filter-button")
    private WebElement filterButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-exclude-button'] button")
    private WebElement excludeButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private ComponentTableActions componentTableActions;
    private ScenarioTableController scenarioTableController;

    public ComponentsListPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(listButton);
        pageUtils.waitForElementToAppear(previewButton);
        assertTrue("Tree View is not the default view", treeButton.getAttribute("class").contains("active"));
    }

    /**
     * Changes the view to table view
     *
     * @return current page object
     */
    public ComponentsListPage tableView() {
        pageUtils.waitForElementToAppear(listButton);
        return this;
    }

    /**
     * Changes the view to tree view
     *
     * @return current page object
     */
    public ComponentsListPage treeView() {
        pageUtils.waitForElementToAppear(treeButton);
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ComponentsListPage enterSearch(String componentName) {
        componentTableActions.enterKeySearch(componentName);
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ComponentsListPage clickSearch(String componentName) {
        componentTableActions.clickSearch(componentName);
        return this;
    }

    /**
     * Open configure page
     *
     * @return new page object
     */
    public ConfigurePage configure() {
        return componentTableActions.configure(configureButton);
    }

    /**
     * Open filters page
     *
     * @return new page object
     */
    public FilterPage filter() {
        return componentTableActions.filter(filterButton);
    }


    /**
     * Opens tree view tab
     *
     * @return new page object
     */
    public TreePage openTreeTab() {
        pageUtils.waitForElementAndClick(treeButton);
        return new TreePage(driver);
    }

    /**
     * Opens cost inputs page
     *
     * @return new page object
     */
    public ComponentPrimaryPage setInputs() {
        pageUtils.waitForElementAndClick(setInputsButton);
        return new ComponentPrimaryPage(driver);
    }

    /**
     * Checks if button is enabled
     * @return true/false
     */
    public boolean isSetInputsEnabled() {
        return pageUtils.waitForElementToAppear(setInputsButton).isEnabled();
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ComponentsListPage highlightScenario(String componentName, String scenarioName) {
        scenarioTableController.highlightScenario(componentName, scenarioName);
        return this;
    }

    /**
     * Multi-highlight scenarios
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ComponentsListPage multiHighlightScenarios(String... componentScenarioName) {
        scenarioTableController.multiHighlightScenario(componentScenarioName);
        return this;
    }

    /**
     * Multi-select scenario
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ComponentsListPage multiSelectScenarios(String... componentScenarioName) {
        scenarioTableController.multiSelectScenario(componentScenarioName);
        return this;
    }

    /**
     * Highlights the scenario in the table using the keyboard control key
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ComponentsListPage controlHighlightScenario(String componentName, String scenarioName) {
        scenarioTableController.controlHighlightScenario(componentName, scenarioName);
        return this;
    }

    /**
     * Expands the Assembly
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public ComponentsListPage expandAssembly(String componentName, String scenarioName) {
        scenarioTableController.expandAssembly(componentName, scenarioName);
        return this;
    }

    /**
     * Gets the icon in the row
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return list of string
     */
    public List<String> getRowDetails(String componentName, String scenarioName) {
        return scenarioTableController.getRowDetails(componentName, scenarioName);
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return scenarioTableController.getListOfScenarios(componentName, scenarioName);
    }

    /**
     * Opens the assembly
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public EvaluatePage openAssembly(String componentName, String scenarioName) {
        scenarioTableController.openScenario(componentName, scenarioName);
        pageUtils.windowHandler(1);
        return new EvaluatePage(driver);
    }

    /**
     * Selects the exclude button
     * @return current page object
     */
    public ComponentsListPage exclude() {
        pageUtils.waitForElementAndClick(excludeButton);
        return this;
    }

    /**
     * Gets the background colour of the cell
     * @param componentName - the component name
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public String getCellColour(String componentName, String scenarioName) {
        return scenarioTableController.getCellColour(componentName, scenarioName);
    }
}
