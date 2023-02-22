package com.apriori.pageobjects.pages.evaluate.components;

import static org.junit.Assert.assertTrue;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioManifestSubcomponents;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.pageobjects.common.AssembliesComponentsController;
import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.navtoolbars.DeletePage;
import com.apriori.pageobjects.navtoolbars.ExploreToolbar;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import com.utils.ButtonTypeEnum;
import com.utils.ColumnsEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class ComponentsTablePage extends LoadableComponent<ComponentsTablePage> {

    @FindBy(css = "[id='qa-scenario-list-table-view-button'] button")
    private WebElement tableViewButton;

    @FindBy(css = "[id='qa-scenario-list-card-view-button'] button")
    private WebElement treeViewButton;

    @FindBy(css = "[id='qa-sub-component-detail-preview-button'] button")
    private WebElement previewButton;

    @FindBy(css = "[id='qa-sub-component-detail-filter-button'] button")
    private WebElement filterButton;

    @FindBy(css = "div[data-testid='loader']")
    private WebElement loadingSpinner;

    @FindBy(css = ".sub-component-tree .component-name")
    private List<WebElement> subcomponentNames;

    @FindBy(css = ".sub-components-detail-card .table-body")
    private WebElement componentTable;

    @FindBy(id = "qa-sub-components-detail-card-filter-selector")
    private WebElement filterDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private ComponentTableActions componentTableActions;
    private ScenarioTableController scenarioTableController;
    private AssembliesComponentsController assembliesComponentsController;

    public ComponentsTablePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.assembliesComponentsController = new AssembliesComponentsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Table View is not active", pageUtils.waitForElementToAppear(tableViewButton).getAttribute("class").contains("active"));
        pageUtils.waitForElementToAppear(previewButton);
        pageUtils.waitForElementNotVisible(loadingSpinner, 1);
        pageUtils.waitForElementToAppear(componentTable);
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public ComponentsTablePage setPagination() {
        componentTableActions.setPagination();
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ComponentsTablePage enterSearch(String componentName) {
        componentTableActions.enterKeySearch(componentName);
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ComponentsTablePage clickSearch(String componentName) {
        componentTableActions.clickSearch(componentName);
        return this;
    }

    /**
     * Open configure page
     *
     * @return new page object
     */
    public ConfigurePage configure() {
        return assembliesComponentsController.configure();
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
     * Opens tree view
     *
     * @return new page object
     */
    public ComponentsTreePage selectTreeView() {
        pageUtils.waitForElementAndClick(treeViewButton);
        return new ComponentsTreePage(driver);
    }

    /**
     * Opens cost inputs page
     *
     * @return new page object
     */
    public ComponentBasicPage setInputs() {
        return assembliesComponentsController.setInputs();
    }

    /**
     * Checks if button is enabled
     *
     * @return true/false
     */
    public boolean isSetInputsEnabled() {
        return assembliesComponentsController.isSetInputsEnabled();
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
    public ComponentsTablePage highlightScenario(String componentName, String scenarioName) {
        assembliesComponentsController.highlightScenario(componentName, scenarioName);
        return this;
    }

    /**
     * Multi-highlight scenarios
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ComponentsTablePage multiHighlightScenarios(String... componentScenarioName) {
        assembliesComponentsController.multiHighlightScenarios(componentScenarioName);
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ComponentsTablePage clickScenarioCheckbox(String componentName, String scenarioName) {
        assembliesComponentsController.clickScenarioCheckbox(componentName, scenarioName);
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @return current page object
     */
    public ComponentsTablePage clickScenarioCheckbox(String componentName) {
        assembliesComponentsController.clickScenarioCheckbox(componentName);
        return this;
    }

    /**
     * Multi-select subcomponents
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ComponentsTablePage multiSelectSubcomponents(String... componentScenarioName) {
        assembliesComponentsController.multiSelectSubcomponents(componentScenarioName);
        return this;
    }

    /**
     * Highlights the scenario in the table using the keyboard control key
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ComponentsTablePage controlHighlightScenario(String componentName, String scenarioName) {
        assembliesComponentsController.controlHighlightScenario(componentName, scenarioName);
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
        return assembliesComponentsController.getRowDetails(componentName, scenarioName);
    }

    /**
     * Gets the data-icon value for the State icon
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of state icon
     */
    public String getScenarioState(String componentName, String scenarioName) {
        return assembliesComponentsController.getScenarioState(componentName, scenarioName);
    }

    /**
     * Gets the scenario state of the component
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param currentUser   -  current user
     * @param stateEnum     -  scenario state enum
     * @return - string
     */
    public String getScenarioState(String componentName, String scenarioName, UserCredentials currentUser, ScenarioStateEnum stateEnum) {
        return assembliesComponentsController.getScenarioState(componentName, scenarioName, currentUser, stateEnum);
    }

    /**
     * Gets the cost value for the State icon
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of state icon
     */
    public Double getScenarioFullyBurdenedCost(String componentName, String scenarioName) {
        return assembliesComponentsController.getScenarioFullyBurdenedCost(componentName, scenarioName);
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return assembliesComponentsController.getListOfScenarios(componentName, scenarioName);
    }

    /**
     * Opens the assembly
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public EvaluatePage openAssembly(String componentName, String scenarioName) {
        assembliesComponentsController.openAssembly(componentName, scenarioName);
        return new EvaluatePage(driver);
    }

    /**
     * Check if the include or the exclude button is enabled
     *
     * @return - boolean
     */
    public boolean isAssemblyTableButtonEnabled(ButtonTypeEnum buttonTypeEnum) {
        return assembliesComponentsController.isAssemblyTableButtonEnabled(buttonTypeEnum);
    }

    /**
     * clicks the box to select all subcomponents
     *
     * @return - the current page object
     */
    public ComponentsTablePage selectCheckAllBox() {
        assembliesComponentsController.selectCheckAllBox();
        return this;
    }

    /**
     * clicks the edit button
     *
     * @return - the current page object
     */
    public <T> T editSubcomponent(Class<T> klass) {
        return assembliesComponentsController.editSubcomponent(klass);
    }

    /**
     * Checks is edit button disabled
     *
     * @return boolean
     */
    public boolean isEditButtonDisabled() {
        return !assembliesComponentsController.isEditButtonDisabled();
    }

    /**
     * clicks the publish button
     *
     * @return - the current page object
     */
    public PublishPage publishSubcomponent() {
        return assembliesComponentsController.publishSubcomponent();
    }

    /**
     * clicks the delete button
     *
     * @return - the current page object
     */
    public DeletePage deleteSubcomponent() {
        return assembliesComponentsController.deleteSubComponent();
    }

    /**
     * Checks the subcomponent is in a completed state
     *
     * @param componentInfo     - the component info
     * @param subcomponentNames - the subcomponent names
     * @return current page object
     */
    public ComponentsTablePage checkSubcomponentState(ComponentInfoBuilder componentInfo, String... subcomponentNames) {
        assembliesComponentsController.checkSubcomponentState(componentInfo, subcomponentNames);
        return this;
    }

    /**
     * Checks scenario manifest is in a complete state
     *
     * @param componentInfo     - the component info
     * @param subcomponentNames - the subcomponent names
     * @return current page object
     */
    public ComponentsTablePage checkManifestComplete(ComponentInfoBuilder componentInfo, String... subcomponentNames) {

        List<String> componentNames = Arrays.stream(subcomponentNames)
            .flatMap(x -> Arrays.stream(x.split(","))
                .map(String::trim))
            .collect(Collectors.toList());

        componentNames.forEach(componentName -> {
            while (!getScenarioManifestState(componentInfo, componentName).contains("COMPLETE")) {
                getScenarioManifestState(componentInfo, componentName);
            }
            new ExploreToolbar(driver).refresh();

            isLoaded();

            if (pageUtils.isElementDisplayed(By.cssSelector(".sub-component-tree [data-icon='gear']"))) {
                checkManifestComplete(componentInfo, componentName);
            }
        });
        return this;
    }

    /**
     * Gets state of scenario manifest
     *
     * @param componentInfo -the component info
     * @param componentName - the subcomponent names
     * @return string
     */
    private String getScenarioManifestState(ComponentInfoBuilder componentInfo, String componentName) {

        ComponentInfoBuilder componentDetails = componentInfo.getSubComponents().stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(componentName))
            .findFirst()
            .get();

        return new ScenariosUtil().getScenarioManifest(componentInfo).getResponseEntity()
            .getSubcomponents()
            .stream()
            .filter(o -> o.getComponentName().equalsIgnoreCase(componentName)
                && Objects.equals(o.getComponentIdentity(), componentDetails.getComponentIdentity()))
            .map(ScenarioManifestSubcomponents::getScenarioState)
            .collect(Collectors.toList())
            .stream().findFirst().get();
    }

    /**
     * Gets the background colour of the cell
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return current page object
     */
    public String getCellColour(String componentName, String scenarioName) {
        return assembliesComponentsController.getCellColour(componentName, scenarioName);
    }

    /**
     * Gets the struck out component name
     *
     * @param componentName - the component name
     * @return - string
     */
    public boolean isTextDecorationStruckOut(String componentName) {
        return assembliesComponentsController.isTextDecorationStruckOut(componentName);
    }

    /**
     * Updates a cad file
     *
     * @return new page object
     */
    public ComponentsTablePage updateCadFile(File filePath) {
        assembliesComponentsController.updateCadFile(filePath, ComponentsTablePage.class);
        return this;
    }


    /**
     * Checks if the cad button is enabled
     *
     * @return true/false
     */
    public boolean isCadButtonEnabled() {
        return assembliesComponentsController.isCadButtonEnabled();
    }

    /**
     * Checks icon is displayed
     *
     * @param icon          - the icon
     * @param componentName - the component name
     * @return - boolean
     */
    public boolean isIconDisplayed(StatusIconEnum icon, String componentName) {
        return assembliesComponentsController.isIconDisplayed(icon, componentName);
    }

    /**
     * Gets list of subcomponent names
     *
     * @return string
     */
    public List<String> getListOfSubcomponents() {
        return assembliesComponentsController.getListOfSubcomponents();
    }

    /**
     * Gets the number of elements with state present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return boolean
     */
    public boolean getListOfScenariosWithStatus(String componentName, String scenarioName, ScenarioStateEnum scenarioState) {
        return assembliesComponentsController.getListOfScenariosWithStatus(componentName, scenarioName, scenarioState);
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public ComponentsTablePage selectFilter(String filter) {
        pageUtils.typeAheadSelect(filterDropdown, "qa-sub-components-detail-card-filter-selector", filter);
        setPagination();
        return this;
    }

    /**
     * Gets no scenarios message
     *
     * @return string
     */
    public String getScenarioMessage() {
        return assembliesComponentsController.getScenarioMessage();
    }

    /**
     * Gets all scenario State from Explorer Table
     *
     * @return - list of all scenario state
     */
    public List<String> getAllScenarioState() {
        return assembliesComponentsController.getAllScenarioState();
    }

    /**
     * assert if element exists in the DOM
     *
     * @return boolean
     */
    public boolean isElementDisplayed(String searchedText, String className) {
        return assembliesComponentsController.isElementDisplayed(searchedText, className);
    }

    /**
     * Gets all scenario Component Names from Explorer Table
     *
     * @return - list of all scenario Component Names
     */
    public List<String> getAllScenarioComponentName(int size) {
        return assembliesComponentsController.getAllScenarioComponentName(size);
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return assembliesComponentsController.getTableHeaders();
    }

    /**
     * Gets the column data from a table
     *
     * @param column          - the column
     * @param scenarioId      - the scenario identity
     * @param userCredentials - the user credentials
     * @return string
     */
    public String getColumnData(ColumnsEnum column, String scenarioId, UserCredentials userCredentials) {
        return assembliesComponentsController.getColumnData(column, scenarioId, userCredentials);
    }

    /**
     * Check if subcomponents are in the tree view
     *
     * @param componentName - component name
     * @return - boolean
     */
    public boolean isComponentNameDisplayedInTreeView(String componentName) {
        return assembliesComponentsController.isComponentNameDisplayedInTreeView(componentName);
    }

    /**
     * Check if table column already displayed and add if not
     *
     * @param columnToAdd - Name of column to be added
     * @return - The current page object
     */
    public ComponentsTablePage addColumn(ColumnsEnum columnToAdd) {
        assembliesComponentsController.addColumnTableView(columnToAdd);
        return this;
    }
}
