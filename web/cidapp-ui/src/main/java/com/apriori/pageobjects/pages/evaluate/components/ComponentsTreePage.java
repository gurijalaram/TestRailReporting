package com.apriori.pageobjects.pages.evaluate.components;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.common.AssembliesComponentsController;
import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.pages.explore.PreviewPage;
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
import java.util.List;

@Slf4j
public class ComponentsTreePage extends LoadableComponent<ComponentsTreePage> {

    @FindBy(css = "div[data-testid='loader']")
    private WebElement loadingSpinner;

    @FindBy(css = "[id='qa-scenario-list-card-view-button'] button")
    private WebElement treeViewButton;

    @FindBy(css = "[id='qa-scenario-list-table-view-button'] button")
    private WebElement tableViewButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-exclude-button'] button")
    private WebElement excludeButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-include-button'] button")
    private WebElement includeButton;

    @FindBy(css = ".component-display-name-container [data-icon='arrow-up-right-from-square']")
    private WebElement subcomponentCard;

    @FindBy(css = "[id='qa-sub-component-detail-preview-button']")
    private WebElement previewButtonOnComponentsPage;

    @FindBy(css = ".scenario-preview")
    private WebElement previewDataPanel;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private ScenarioTableController scenarioTableController;
    private AssembliesComponentsController assembliesComponentsController;
    private ComponentTableActions componentTableActions;

    public ComponentsTreePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.assembliesComponentsController = new AssembliesComponentsController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Tree View is not active", pageUtils.waitForElementToAppear(treeViewButton).getAttribute("class").contains("active"));
        pageUtils.waitForElementNotVisible(loadingSpinner, 1);
    }

    /**
     * Open table view
     *
     * @return new page object
     */
    public ComponentsTablePage selectTableView() {
        pageUtils.waitForElementAndClick(tableViewButton);
        return new ComponentsTablePage(driver);
    }

    /**
     * Selects the include or the exclude button
     *
     * @return - the current page object
     */
    public ComponentsTreePage selectButtonType(ButtonTypeEnum buttonTypeEnum) {
        WebElement buttonToPress;

        switch (buttonTypeEnum) {
            case INCLUDE:
                buttonToPress = includeButton;
                break;
            case EXCLUDE:
                buttonToPress = excludeButton;
                break;
            default:
                return this;
        }
        pageUtils.waitForElementAndClick(buttonToPress);
        pageUtils.waitForElementNotDisplayed(subcomponentCard, 1);
        return this;
    }

    /**
     * gets the sub component in a sub assembly
     *
     * @param componentName - the component name
     * @return - current page object
     */
    public ComponentsTreePage selectSubAssemblySubComponent(String componentName, String subAssemblyName) {
        By scenario = with(getXpath(componentName))
            .below(By.xpath(String.format("//span[text()='%s']", subAssemblyName.toUpperCase().trim())));
        pageUtils.waitForElementAndClick(scenario);
        return this;
    }

    private By getXpath(String componentName) {
        return By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//span[@data-testid='checkbox']", componentName.toUpperCase().trim()));
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
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Expands the Assembly
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public ComponentsTreePage expandSubAssembly(String componentName, String scenarioName) {
        scenarioTableController.expandSubassembly(componentName, scenarioName);
        return this;
    }

    /**
     * Collapse the Assembly
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public ComponentsTreePage collapseSubassembly(String componentName, String scenarioName) {
        scenarioTableController.collapseSubassembly(componentName, scenarioName);
        return this;
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
     * Method to switch to a new scenario name
     *
     * @param componentName - the component name
     * @param scenarioName  -the scenario name
     * @return current page object
     */
    public ComponentsTreePage switchScenarioName(String componentName, String scenarioName) {
        WebElement scenarioSwitch = driver.findElement(By.xpath(String.format("//span[text()='%s']/ancestor::div[@role='row']//div[@id='qa-scenario-select-field']", componentName.toUpperCase().trim())));
        pageUtils.typeAheadSelect(scenarioSwitch, scenarioName);
        return this;
    }

    /**
     * Multi-select subcomponents
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ComponentsTreePage multiSelectSubcomponents(String... componentScenarioName) {
        assembliesComponentsController.multiSelectSubcomponents(componentScenarioName);
        return this;
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
    public ComponentsTreePage selectCheckAllBox() {
        assembliesComponentsController.selectCheckAllBox();
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     *
     * @return current page object
     */
    public ComponentsTreePage clickScenarioCheckbox(String componentName) {
        assembliesComponentsController.clickScenarioCheckbox(componentName);
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ComponentsTreePage clickScenarioCheckbox(String componentName, String scenarioName) {
        assembliesComponentsController.clickScenarioCheckbox(componentName, scenarioName);
        return this;
    }

    /**
     * Updates a cad file
     *
     * @return new page object
     */
    public ComponentsTreePage updateCadFile(File filePath) {
        assembliesComponentsController.updateCadFile(filePath, ComponentsTreePage.class);
        pageUtils.waitForElementNotVisible(loadingSpinner, 2);
        return this;
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
     * Open configure page
     *
     * @return new page object
     */
    public ConfigurePage configure() {
        return assembliesComponentsController.configure();
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
     * Gets list of subcomponent names
     *
     * @return string
     */
    public List<String> getListOfSubcomponents() {
        return assembliesComponentsController.getListOfSubcomponents();
    }

    /**
     * Gets subcomponent scenario name
     *
     * @param componentName - the component name
     * @return string
     */
    public String getSubcomponentScenarioName(String componentName) {
        return assembliesComponentsController.getSubcomponentScenarioName(componentName);
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
     * Checks the subcomponent is in a completed state
     *
     * @param componentInfo     - the component info
     * @param subcomponentNames - the subcomponent names
     * @return current page object
     */
    public ComponentsTreePage checkSubcomponentState(ComponentInfoBuilder componentInfo, String... subcomponentNames) {
        assembliesComponentsController.checkSubcomponentState(componentInfo, subcomponentNames);
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
    public boolean isEditButtonEnabled() {
        return !assembliesComponentsController.isEditButtonEnabled();
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
     * Opens preview panel page
     *
     * @return new page object
     */
    public PreviewPage openPreviewPanel() {
        return componentTableActions.openPreviewPanel(previewButtonOnComponentsPage);
    }

    /**
     * Checks if the preview panel is displayed
     *
     * @return true/false
     */
    public boolean isPreviewPanelDisplayed() {
        return pageUtils.isElementDisplayed(previewDataPanel);
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
     * Checks if the scenario checkbox is selected
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return true/false
     */
    public boolean isScenarioCheckboxSelected(String componentName, String scenarioName) {
        return assembliesComponentsController.isScenarioCheckboxSelected(componentName, scenarioName);
    }
}
