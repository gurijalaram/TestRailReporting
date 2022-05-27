package com.apriori.pageobjects.pages.evaluate.components;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.UpdateCadFilePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentPrimaryPage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.CssComponent;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import com.utils.ButtonTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ComponentsListPage extends LoadableComponent<ComponentsListPage> {

    @FindBy(css = "[id='qa-scenario-list-table-view-button'] button")
    private WebElement tableButton;

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

    @FindBy(css = "[id='qa-sub-component-detail-configure-button'] button")
    private WebElement configureButton;

    @FindBy(css = "[id='qa-sub-component-detail-filter-button'] button")
    private WebElement filterButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-exclude-button'] button")
    private WebElement excludeButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-include-button'] button")
    private WebElement includeButton;

    @FindBy(css = ".table-head [data-icon='square']")
    private WebElement checkAllBox;

    @FindBy(css = "[id='qa-sub-component-action-bar-edit-button'] button")
    private WebElement editButton;

    @FindBy(css = "[id='qa-sub-component-action-bar-update-cad-file-button'] button")
    private WebElement updateCadButton;

    @FindBy(css = ".component-display-name-container [data-icon='arrow-up-right-from-square']")
    private WebElement subcomponentCard;

    @FindBy(css = "[id='qa-sub-component-action-bar-publish-button'] button")
    private WebElement publishButton;

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
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(tableButton);
        pageUtils.waitForElementToAppear(previewButton);
        assertTrue("Tree View is not the default view", treeButton.getAttribute("class").contains("active"));
    }

    /**
     * Changes the view to table view
     *
     * @return current page object
     */
    public ComponentsListPage tableView() {
        pageUtils.waitForElementAndClick(tableButton);
        return this;
    }

    /**
     * Changes the view to tree view
     *
     * @return current page object
     */
    public ComponentsListPage treeView() {
        pageUtils.waitForElementAndClick(treeButton);
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
     *
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
     * Multi-select subcomponents
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ComponentsListPage multiSelectSubcomponents(String... componentScenarioName) {
        scenarioTableController.multiSelectScenario(componentScenarioName);
        return this;
    }

    /**
     * gets the sub component in a sub assembly
     *
     * @param componentName - the component name
     * @return - current page object
     */
    public ComponentsListPage selectSubAssemblySubComponent(String componentName, String subAssemblyName) {
        By scenario = with(By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//div[@class='checkbox-icon']", componentName.trim())))
            .below(By.xpath(String.format("//span[text()='%s']", subAssemblyName.toUpperCase().trim())));
        pageUtils.waitForElementAndClick(scenario);
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
     * Expands the sub assembly
     *
     * @param subAssemblyName - the sub assembly name
     * @return - the current page object
     */
    public ComponentsListPage expandSubAssembly(String subAssemblyName) {
        By byExpand = By.xpath(String.format("//span[text()='%s']/ancestor::div[@class='cell-text']/preceding-sibling::div//*[@data-icon='circle-chevron-down']", subAssemblyName.toUpperCase().trim()));
        pageUtils.waitForElementAndClick(byExpand);
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
     * Selects the include or the exclude button
     *
     * @return - the current page object
     */
    public ComponentsListPage selectButtonType(ButtonTypeEnum buttonTypeEnum) {
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
            default:
                return false;
        }
    }

    /**
     * clicks the box to select all subcomponents
     *
     * @return - the current page object
     */
    public ComponentsListPage selectCheckAllBox() {
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
    public boolean isEditButtonEnabled() {
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
     * Checks the subcomponent is in a completed state
     *
     * @param componentInfo     - the component info
     * @param subcomponentNames - the subcomponent names
     * @return current page object
     */
    public ComponentsListPage checkSubcomponentState(ComponentInfoBuilder componentInfo, String... subcomponentNames) {
        List<String> componentNames = Arrays.stream(subcomponentNames)
            .flatMap(x -> Arrays.stream(x.split(","))
                .map(String::trim))
            .collect(Collectors.toList());

        componentNames.forEach(componentName -> new ScenariosUtil().getScenarioRepresentation(componentInfo.getSubComponents()
            .stream()
            .filter(x -> x.getComponentName().equalsIgnoreCase(componentName))
            .collect(Collectors.toList()).get(0)));
        return this;
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
        return driver.findElement(byComponentName).getCssValue("text-decoration").contains("line-through");
    }

    /**
     * Updates a cad file
     *
     * @return new page object
     */
    public UpdateCadFilePage updateCadFile() {
        pageUtils.waitForElementAndClick(updateCadButton);
        return new UpdateCadFilePage(driver);
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
     * method to switch to a new scenario name
     *
     * @param componentName - the component name
     * @param scenarioName  -the scenario name
     * @return - current page object
     */
    public ComponentsListPage switchScenarioName(String componentName, String scenarioName) {
        WebElement scenarioSwitch = driver.findElement(By.xpath(String.format("//span[text()='%s']/ancestor::div[@role='row']//div[@id='qa-scenario-select-field']", componentName.toUpperCase().trim())));
        pageUtils.typeAheadSelect(scenarioSwitch, scenarioName);
        return this;
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
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @return current page object
     */
    public ComponentsListPage selectScenario(String componentName) {
        By scenario = By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']/child::div//div[@class='checkbox-icon']",
            componentName.toUpperCase().trim()));
        pageUtils.waitForElementToAppear(scenario);
        pageUtils.scrollWithJavaScript(driver.findElement(scenario), true);
        pageUtils.waitForElementAndClick(scenario);
        return this;
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
        List<ScenarioItem> itemResponse = new CssComponent().getCssComponent(componentName, scenarioName, currentUser, stateEnum);

        return itemResponse.stream().filter(item ->
            item.getScenarioState().equalsIgnoreCase(stateEnum.getState())).findFirst().get().getScenarioState();
    }
}
