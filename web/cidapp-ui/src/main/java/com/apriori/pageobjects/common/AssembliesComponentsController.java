package com.apriori.pageobjects.common;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.pageobjects.navtoolbars.DeletePage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.UpdateCadFilePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.utils.CssComponent;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import com.utils.ButtonTypeEnum;
import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AssembliesComponentsController {

    @FindBy(css = "[id='qa-scenario-list-table-view-button'] button")
    private WebElement tableViewButton;

    @FindBy(css = "[id='qa-scenario-list-card-view-button'] button")
    private WebElement treeViewButton;

    @FindBy(css = "[id='qa-sub-component-detail-preview-button'] button")
    private WebElement previewButton;

    @FindBy(xpath = "//button[.='Selection']")
    private WebElement selectionButton;

    @FindBy(id = "qa-sub-component-action-bar-upload-button")
    private WebElement uploadButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-set-inputs-button'] button")
    private WebElement setInputsButton;

    @FindBy(css = "[id='qa-sub-component-detail-configure-button'] button")
    private WebElement configureButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-exclude-button'] button")
    private WebElement excludeButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-include-button'] button")
    private WebElement includeButton;

    @FindBy(css = ".table-head [data-testid='checkbox']")
    private WebElement checkAllBox;

    @FindBy(css = "[id='qa-sub-component-action-bar-edit-button'] button")
    private WebElement editButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-update-cad-file-button'] button")
    private WebElement updateCadButton;

    @FindBy(css = ".component-display-name-container [data-icon='arrow-up-right-from-square']")
    private WebElement subcomponentCard;

    @FindBy(css = "[id='qa-sub-component-action-bar-publish-button'] button")
    private WebElement publishButton;

    @FindBy(css = ".sub-component-tree [id='qa-sub-header-delete-button'] button")
    private WebElement deleteButton;

    @FindBy(css = ".sub-component-tree .component-name")
    private List<WebElement> subcomponentNames;

    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;


    private WebDriver driver;
    private PageUtils pageUtils;
    private ComponentTableActions componentTableActions;
    private PanelController panelController;
    private ScenarioTableController scenarioTableController;

    public AssembliesComponentsController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        this.panelController = new PanelController(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
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
     * Opens cost inputs page
     *
     * @return new page object
     */
    public ComponentBasicPage setInputs() {
        pageUtils.waitForElementAndClick(setInputsButton);
        return new ComponentBasicPage(driver);
    }

    /**
     * Checks if button is enabled
     *
     * @return true/false
     */
    public boolean isSetInputsEnabled() {
        return pageUtils.waitForElementToAppear(setInputsButton).isEnabled();
    }

    /**
     * Checks if button is enabled
     *
     * @return true/false
     */
    public boolean isDeleteButtonEnabled() {
        return pageUtils.waitForElementToAppear(deleteButton).isEnabled();
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public AssembliesComponentsController highlightScenario(String componentName, String scenarioName) {
        scenarioTableController.highlightScenario(componentName, scenarioName);
        return this;
    }

    /**
     * Multi-highlight scenarios
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public AssembliesComponentsController multiHighlightScenarios(String... componentScenarioName) {
        scenarioTableController.multiHighlightScenario(componentScenarioName);
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public AssembliesComponentsController clickScenarioCheckbox(String componentName, String scenarioName) {
        scenarioTableController.clickScenarioCheckbox(componentName, scenarioName);
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @return current page object
     */
    public AssembliesComponentsController clickScenarioCheckbox(String componentName) {
        By scenario = getXpath(componentName);
        pageUtils.waitForElementToAppear(scenario);
        pageUtils.scrollWithJavaScript(driver.findElement(scenario), true);
        pageUtils.waitForElementAndClick(scenario);
        return this;
    }

    /**
     * Multi-select subcomponents
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public AssembliesComponentsController multiSelectSubcomponents(String... componentScenarioName) {
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
    public AssembliesComponentsController controlHighlightScenario(String componentName, String scenarioName) {
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

    /**
     * Gets the data-icon value for the State icon
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of state icon
     */
    public String getScenarioState(String componentName, String scenarioName) {
        return scenarioTableController.getScenarioState(componentName, scenarioName);
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
        List<ScenarioItem> itemResponse = new CssComponent().getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName,
            SCENARIO_STATE_EQ.getKey() + stateEnum.getState());

        return itemResponse.stream().filter(item ->
            item.getScenarioState().equalsIgnoreCase(stateEnum.getState())).findFirst().get().getScenarioState();
    }

    /**
     * Gets the cost value for the State icon
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of state icon
     */
    public Double getScenarioFullyBurdenedCost(String componentName, String scenarioName) {
        return scenarioTableController.getScenarioFullyBurdenedCost(componentName, scenarioName);
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
     * Check if the include or the exclude button is enabled
     *
     * @return - boolean
     */
    public boolean isAssemblyTableButtonEnabled(ButtonTypeEnum buttonTypeEnum) {
        switch (buttonTypeEnum) {
            case INCLUDE:
                return pageUtils.isElementEnabled(includeButton);
            case EXCLUDE:
                return pageUtils.isElementEnabled(excludeButton);
            case PUBLISH:
                return pageUtils.isElementEnabled(publishButton);
            case EDIT:
                return pageUtils.isElementEnabled(editButton);
            case DELETE:
                return pageUtils.isElementEnabled(deleteButton);
            case SET_INPUTS:
                return pageUtils.isElementEnabled(setInputsButton);
            default:
                return false;
        }
    }

    /**
     * clicks the box to select all subcomponents
     *
     * @return - the current page object
     */
    public AssembliesComponentsController selectCheckAllBox() {
        pageUtils.waitForElementAndClick(checkAllBox);
        return this;
    }

    /**
     * clicks the edit button
     *
     * @return - the current page object
     */
    public <T> T editSubcomponent(Class<T> klass) {
        pageUtils.waitForElementAndClick(editButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Checks is edit button disabled
     *
     * @return boolean
     */
    public boolean isEditButtonDisabled() {
        return !pageUtils.waitForElementToAppear(editButton).getAttribute("class").contains("disabled");
    }

    /**
     * clicks the publish button
     *
     * @return - the current page object
     */
    public PublishPage publishSubcomponent() {
        pageUtils.waitForElementAndClick(publishButton);
        return new PublishPage(driver);
    }

    /**
     * clicks the delete button
     *
     * @return - the current page object
     */
    public DeletePage deleteSubComponent() {
        pageUtils.waitForElementAndClick(deleteButton);
        return new DeletePage(driver);
    }

    /**
     * Gets the background colour of the cell
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return current page object
     */
    public String getCellColour(String componentName, String scenarioName) {
        return scenarioTableController.getCellColour(componentName, scenarioName);
    }

    /**
     * Gets the struck out component name
     *
     * @param componentName - the component name
     * @return - string
     */
    public boolean isTextDecorationStruckOut(String componentName) {
        By byComponentName = By.xpath(String.format("//ancestor::div[@role='row']//span[contains(text(),'%s')]/ancestor::div[@role='row']",
            componentName.toUpperCase().trim()));
        return pageUtils.waitForElementToAppear(byComponentName).getCssValue("text-decoration").contains("line-through");
    }

    private By getXpath(String componentName) {
        return By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//span[@data-testid='checkbox']", componentName.toUpperCase().trim()));
    }

    /**
     * Updates a cad file
     *
     * @return new page object
     */
    public <T> T updateCadFile(File filePath, Class<T> klass) {
        pageUtils.waitForElementAndClick(updateCadButton);
        return new UpdateCadFilePage(driver).enterFilePath(filePath).submit(klass);
    }

    /**
     * Checks if the cad button is enabled
     *
     * @return true/false
     */
    public boolean isCadButtonEnabled() {
        return pageUtils.isElementEnabled(updateCadButton);
    }

    /**
     * Checks icon is displayed
     *
     * @param icon          - the icon
     * @param componentName - the component name
     * @return - boolean
     */
    public boolean isIconDisplayed(StatusIconEnum icon, String componentName) {
        By iconLogo = By.xpath(String.format("//span[text()='%s']/following::div[@id='qa-scenario-select-field']//*[name()='svg'='data-icon=%s']", componentName, icon.getStatusIcon()));
        return pageUtils.waitForElementToAppear(iconLogo).isDisplayed();
    }

    /**
     * Checks the subcomponent is in a completed state
     *
     * @param componentInfo     - the component info
     * @param subcomponentNames - the subcomponent names
     * @return current page object
     */
    public AssembliesComponentsController checkSubcomponentState(ComponentInfoBuilder componentInfo, String... subcomponentNames) {
        List<String> componentNames = Arrays.stream(subcomponentNames)
            .flatMap(x -> Arrays.stream(x.split(","))
                .map(String::trim))
            .collect(Collectors.toList());

        componentNames.forEach(componentName -> {
            ComponentInfoBuilder componentDetails = componentInfo.getSubComponents().stream()
                .filter(o -> o.getComponentName().equalsIgnoreCase(componentName))
                .findFirst()
                .get();

            new ScenariosUtil().getScenarioCompleted(componentInfo.getSubComponents()
                .stream()
                .filter(x -> x.getComponentName().equalsIgnoreCase(componentName)
                    && x.getComponentIdentity().equalsIgnoreCase(componentDetails.getComponentIdentity())
                    && x.getScenarioIdentity().equalsIgnoreCase(componentDetails.getScenarioIdentity()))
                .collect(Collectors.toList())
                .get(0));
        });
        return this;
    }

    /**
     * Gets subcomponent scenario name
     *
     * @param componentName - the component name
     * @return string
     */
    public String getSubcomponentScenarioName(String componentName) {
        By byComponentName = By.xpath(String.format("//span[text()='%s']/ancestor::div[@role='row']//div[@class='scenario-selector']", componentName.toUpperCase().trim()));
        return pageUtils.waitForElementToAppear(byComponentName).getAttribute("textContent");
    }

    /**
     * Gets list of subcomponent names
     *
     * @return string
     */
    public List<String> getListOfSubcomponents() {
        return pageUtils.waitForElementsToAppear(subcomponentNames).stream()
            .map(x -> x.getAttribute("textContent"))
            .map(String::trim).collect(Collectors.toList());
    }

    /**
     * Gets the number of elements with state present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return boolean
     */
    public boolean getListOfScenariosWithStatus(String componentName, String scenarioName, ScenarioStateEnum scenarioState) {
        return scenarioTableController.getListOfScenariosWithStatus(componentName, scenarioName, scenarioState);
    }

    /**
     * Gets no scenarios message
     *
     * @return string
     */
    public String getScenarioMessage() {
        return pageUtils.waitForElementToAppear(noScenariosMessage).getText();
    }

    /**
     * Gets all scenario State from Explorer Table
     *
     * @return - list of all scenario state
     */
    public List<String> getAllScenarioState() {
        List<WebElement> rowsStateCol =
            pageUtils.waitForElementsToAppear(By.xpath("//*[local-name()='svg' and contains(@class,'scenario-state-icon fa-1-5x')]"));
        List<String> rowStateNames = rowsStateCol.stream().map(s -> s.getAttribute("data-icon")).collect(Collectors.toList());
        List<String> stateNames = rowStateNames.stream().map(s -> changeToProperNames(s)).collect(Collectors.toList());
        return stateNames;
    }

    private String changeToProperNames(String s) {
        if (s.equals("circle-minus")) {
            return "Uncosted";
        } else if (s.equals("check")) {
            return "Costed";
        }
        return null;
    }

    /**
     * assert if element exists in the DOM
     *
     * @return boolean
     */
    public boolean isElementDisplayed(String searchedText, String className) {
        String xpath = "//div[contains(.,'".concat(searchedText).concat("')][@class = '").concat(className).concat("']");
        WebElement element = pageUtils.waitForElementToAppear(By.xpath(xpath));
        return element.isDisplayed();
    }

    /**
     * Gets all scenario Component Names from Explorer Table
     *
     * @return - list of all scenario Component Names
     */
    public List<String> getAllScenarioComponentName(int size) {
        List<WebElement> rows =
            pageUtils.waitForSpecificElementsNumberToAppear(By.xpath("//div[contains(@class,'table-cell')][contains(@data-header-id,'componentDisplayName')]"), size);
        List<String> componentNames = rows.stream().map(s -> s.getText()).collect(Collectors.toList());
        componentNames.remove("Component Name");
        return componentNames;
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return scenarioTableController.getTableHeaders();
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
        return scenarioTableController.getColumnData(column, scenarioId, userCredentials);
    }

    /**
     * Check if table column already displayed and add if not
     *
     * @param columnToAdd - Name of column to be added
     * @return - The current page object
     */
    public AssembliesComponentsController addColumnTreeView(ColumnsEnum columnToAdd) {
        if (!getTableHeaders().contains(columnToAdd.toString())) {
            componentTableActions.configure(configureButton)
                .selectColumn(columnToAdd)
                .moveColumn(DirectionEnum.RIGHT)
                .selectColumn(columnToAdd)
                .moveColumn(DirectionEnum.UP)
                .moveColumn(DirectionEnum.UP)
                .submit(ComponentsTreePage.class);
        }
        return this;
    }

    /**
     * Check if table column already displayed and add if not
     *
     * @param columnToAdd - Name of column to be added
     * @return - The current page object
     */
    public AssembliesComponentsController addColumnTableView(ColumnsEnum columnToAdd) {
        if (!getTableHeaders().contains(columnToAdd.toString())) {
            componentTableActions.configure(configureButton)
                .selectColumn(columnToAdd)
                .moveColumn(DirectionEnum.RIGHT)
                .selectColumn(columnToAdd)
                .moveColumn(DirectionEnum.UP)
                .moveColumn(DirectionEnum.UP)
                .submit(ComponentsTablePage.class);
        }
        return this;
    }

    /**
     * Check if subcomponents are in the tree view
     *
     * @param componentName - component name
     * @return - boolean
     */
    public boolean isComponentNameDisplayedInTreeView(String componentName) {
        By componentText = By.xpath(String.format("//div[@data-header-id='componentDisplayName']//span[text()='%s']", componentName.toUpperCase()));
        return pageUtils.waitForElementToAppear(componentText).isDisplayed();
    }

    /**
     * Checks if the scenario checkbox is selected
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return true/false
     */
    public boolean isScenarioCheckboxSelected(String componentName, String scenarioName) {
        By element = By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//div[.='%s']/ancestor::div[@role='row']//*[local-name() = 'svg'][@data-testid ='CheckBoxIcon']",
            componentName.toUpperCase().trim(), scenarioName.trim()));
        return pageUtils.isElementDisplayed(element);
    }
}



