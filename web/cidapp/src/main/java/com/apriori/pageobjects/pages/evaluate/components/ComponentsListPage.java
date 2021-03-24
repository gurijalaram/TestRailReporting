package com.apriori.pageobjects.pages.evaluate.components;

import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.PrimaryInputsPage;
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

    private final Logger LOGGER = LoggerFactory.getLogger(ComponentsListPage.class);

    @FindBy(css = ".evaluate-view-drawer [data-icon='list']")
    private WebElement listButton;

    @FindBy(css = ".evaluate-view-drawer [data-icon='folder-tree']")
    private WebElement treeButton;

    @FindBy(xpath = "//button[.='Preview']")
    private WebElement previewButton;

    @FindBy(xpath = "//button[.='Selection']")
    private WebElement selectionButton;

    @FindBy(id = "qa-sub-component-action-bar-upload-button")
    private WebElement uploadButton;

    @FindBy(id = "qa-sub-component-action-bar-set-inputs-button")
    private WebElement costInputsButton;

    @FindBy(id = "qa-sub-component-action-bar-override-button")
    private WebElement pencilButton;

    @FindBy(id = "qa-sub-component-action-bar-exclude-button")
    private WebElement eyeSlashButton;

    @FindBy(id = "qa-sub-component-action-bar-include-button")
    private WebElement eyeButton;

    @FindBy(id = "qa-sub-component-detail-column-config-button")
    private WebElement configureButton;

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
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAndClick(listButton);
        pageUtils.waitForElementAndClick(previewButton);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController enterSearch(String componentName) {
        return componentTableActions.enterSearch(componentName);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController clickSearch(String componentName) {
        return componentTableActions.clickSearch(componentName);
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
        return componentTableActions.filter();
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
    public PrimaryInputsPage setCostInputs() {
        pageUtils.waitForElementAndClick(costInputsButton);
        return new PrimaryInputsPage(driver);
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
     * Gets the icon in the row
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return list of string
     */
    public List<String> getRowDetails(String componentName, String scenarioName) {
        return scenarioTableController.getRowDetails(componentName, scenarioName);
    }
}
