package com.apriori.cid.ui.pageobjects.evaluate;

import com.apriori.cid.ui.pageobjects.common.InputsController;
import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.common.StatusIcon;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.CustomPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cfrith
 */

public class EvaluatePage extends EvaluateToolbar {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatePage.class);

    @FindBy(css = ".costing-inputs .spinner-border")
    private List<WebElement> panelLoaders;

    @FindBy(css = ".webviewer-canvas")
    private WebElement viewerCanvas;

    @FindBy(css = "svg[data-icon='home']")
    private WebElement homeButton;

    @FindBy(css = "svg[data-icon='expand-arrows-alt']")
    private WebElement zoomButton;

    @FindBy(css = "svg[data-icon='list-alt']")
    private WebElement analysisButton;

    @FindBy(css = "svg[data-icon='cube']")
    private WebElement cubeButton;

    @FindBy(css = "svg[data-icon='eye']")
    private WebElement viewButton;

    @FindBy(css = "svg[data-icon='border-none']")
    private WebElement sectioningButton;

    @FindBy(css = "input[name='annualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = "input[name='productionLife']")
    private WebElement productionLifeInput;

    @FindBy(id = "qa-scenario-select-field")
    private WebElement currentScenarioDropdown;

    @FindBy(css = "div[id='qa-process-group-select-field'] [data-icon='chevron-down']")
    private WebElement processGroupDropdown;

    @FindBy(css = "div[id='qa-process-group-select-field'] input")
    private WebElement processGroupInput;

    @FindBy(css = "div[id='qa-digital-factory-select-field'] [data-icon='chevron-down']")
    private WebElement digitalFactoryDropdown;

    @FindBy(css = "div[id='qa-digital-factory-select-field'] input")
    private WebElement digitalFactoryInput;

    @FindBy(xpath = "//div[@id='qa-digital-factory-select-field']//div[@class='text-overflow']")
    private WebElement selectedVPE;

    @FindBy(css = ".secondary-process-modal-select-field .modal-select-content")
    private WebElement secondaryProcessBox;

    @FindBy(css = ".secondary-process-modal-select-field button")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = "div[id='qa-material-modal-select-field'] button")
    private WebElement materialsPencil;

    @FindBy(css = ".material-summary-card.card input")
    private WebElement materialName;

    @FindBy(css = "div[id='qa-material-modal-select-field'] .modal-select-content")
    private WebElement currentMaterial;

    @FindBy(xpath = "//span[contains(text(), 'Finish Mass')]/following-sibling::span")
    private WebElement finishMass;

    @FindBy(css = ".design-guidance-summary-card button")
    private WebElement DetailsButton;

    @FindBy(css = ".process-summary-card button")
    private WebElement processesDetailsButton;

    @FindBy(css = ".cost-result-summary-card button")
    private WebElement costDetailsButton;

    @FindBy(css = ".production-info-summary-card.card .pill.action-button")
    private WebElement inputDetailsButton;

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(css = "[id='qa-digital-factory-select-field']")
    private WebElement digitalFactoryList;

    @FindBy(css = "[id='qa-digital-factory-select-field'] .apriori-select")
    private WebElement digitalFactory;

    @FindBy(css = "[id='qa-process-group-select-field']")
    private WebElement processGroupList;

    @FindBy(css = ".sub-components-summary button")
    private WebElement componentsDetailsButton;

    @FindBy(css = "div[id='qa-source-model-modal-select-field'] button")
    private WebElement sourceComponentPencil;

    @FindBy(id = "qa-scenario-select-field")
    @CacheLookup
    private WebElement scenarioName;

    @FindBy(xpath = "(//div[@class='card-header'])[1]/div[@class='left']/div")
    private WebElement partName;

    @FindBy(xpath = "//div[@id='qa-process-group-select-field']//div[@class='text-overflow']")
    private WebElement currentProcessGroup;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Advanced']")
    private WebElement advancedTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Custom']")
    private WebElement customTab;

    @FindBy(css = "[id='qa-sub-header-refresh-view-button'] button")
    private WebElement refreshButton;

    @FindBy(css = "[id='qa-scenario-list-card-view-button'] button")
    private WebElement treeButton;

    @FindBy(css = ".MuiFormControlLabel-root [data-testid='checkbox']")
    private WebElement machinePartCheckbox;

    @FindBy(css = ".PrivateSwitchBase-input")
    private WebElement checkBoxInput;

    @FindBy(css = "[data-icon = 'xmark']")
    private WebElement closeInvalidSourcePanelButton;

    @FindBy(css = ".sustainability-summary-card")
    private WebElement sustainabilityDetails;

    @FindBy(css = ".sustainability-summary-card .property-name")
    private List<WebElement> sustainabilityPropertyNames;

    private PageUtils pageUtils;
    private WebDriver driver;
    private InputsController inputsController;
    private StatusIcon statusIcon;
    private ModalDialogController modalDialogController;

    public EvaluatePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.inputsController = new InputsController(driver);
        this.statusIcon = new StatusIcon(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        pageUtils.waitForElementsToNotAppear(panelLoaders);
    }

    /**
     * Selects the current scenario
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public EvaluatePage selectCurrentScenario(String scenarioName) {
        pageUtils.waitForElementAndClick(currentScenarioDropdown);
        By currentScenario = By.xpath(String.format("//button[.='%s']", scenarioName));
        pageUtils.waitForElementAndClick(currentScenario);
        return this;
    }

    /**
     * Gets the scenario name
     *
     * @return current page object
     */
    public String getCurrentScenarioName() {
        return pageUtils.waitForElementToAppear(scenarioName).getAttribute("textContent");
    }

    /**
     * Checks scenario name appears
     *
     * @return true/false
     */
    public boolean isCurrentScenarioNameDisplayed(String scenarioName) {
        By byCurrentScenario = By.xpath(String.format("//div[@class='costing-inputs']//div[text()='%s']", scenarioName));
        return pageUtils.waitForElementToAppear(byCurrentScenario).isDisplayed();
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage selectProcessGroup(ProcessGroupEnum processGroup) {
        inputsController.selectInputsDropdown(processGroupDropdown, "qa-process-group-select-field", processGroup.getProcessGroup());
        return this;
    }

    /**
     * Selects the digital factory dropdown
     *
     * @param digitalFactory - the digital factory
     * @return current page object
     */
    public EvaluatePage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        inputsController.selectInputsDropdown(digitalFactoryDropdown, "qa-digital-factory-select-field", digitalFactory.getDigitalFactory());
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public EvaluatePage enterAnnualVolume(String annualVolume) {
        inputsController.enterAnnualVolume(annualVolumeInput, annualVolume);
        return this;
    }

    /**
     * Checks if annual volume input is enabled
     *
     * @return boolean
     */
    public boolean isAnnualVolumeInputEnabled() {
        return pageUtils.isElementEnabled(annualVolumeInput);
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLife - the years
     * @return current page object
     */
    public EvaluatePage enterAnnualYears(String productionLife) {
        inputsController.enterAnnualYears(productionLifeInput, productionLife);
        return this;
    }

    /**
     * Checks if years of annual volume input is enabled
     *
     * @return boolean
     */
    public boolean isAnnualYearsInputEnabled() {
        return pageUtils.isElementEnabled(productionLifeInput);
    }

    /**
     * Opens advanced input tab
     *
     * @return new page object
     */
    public AdvancedPage goToAdvancedTab() {
        pageUtils.javaScriptClick(pageUtils.waitForElementToAppear(advancedTab));
        return new AdvancedPage(driver);
    }

    /**
     * Opens custom tab
     *
     * @return new page object
     */
    public CustomPage goToCustomTab() {
        pageUtils.waitForElementAndClick(customTab);
        return new CustomPage(driver);
    }

    /**
     * Checks material info is displayed
     *
     * @return true/false
     */
    public boolean isMaterialInfoDisplayed(String material) {
        By materialsInfo = By.xpath(String.format("//div[@id='qa-material-modal-select-field']//div[.='%s']", material));
        return pageUtils.isElementDisplayed(materialsInfo);
    }

    /**
     * Opens the material selector table
     *
     * @return new page object
     */
    public MaterialSelectorPage openMaterialSelectorTable() {
        inputsController.openMaterialSelectorTable(materialsPencil);
        return new MaterialSelectorPage(driver);
    }

    /**
     * Get the currently selected Material
     *
     * @return String of currently used material
     */
    public String getCurrentlySelectedMaterial() {
        pageUtils.waitForElementToAppear(currentMaterial);
        return currentMaterial.getAttribute("textContent");
    }

    /**
     * Opens the input panel
     *
     * @return new page object
     */
    public MoreInputsPage openInputDetails() {
        pageUtils.waitForElementAndClick(inputDetailsButton);
        return new MoreInputsPage(driver);
    }

    /**
     * Opens the cost result panel
     *
     * @return new page object
     */
    public CostDetailsPage openCostDetails() {
        pageUtils.waitForElementAndClick(costDetailsButton);
        return new CostDetailsPage(driver);
    }

    /**
     * Opens the design guidance panel
     *
     * @return new page object
     */
    public GuidanceIssuesPage open() {
        if (isDesignGuidanceButtonDisplayed()) {
            designGuidanceDetailsButton.click();
        }
        return new GuidanceIssuesPage(driver);
    }

    /**
     * Checks if design guidance button is displayed which indicates if the window is open
     *
     * @return true/false
     */
    public boolean isDesignGuidanceButtonDisplayed() {
        return pageUtils.isElementDisplayed(designGuidanceDetailsButton);
    }

    /**
     * Opens the processes panel
     *
     * @return new page object
     */
    public MaterialProcessPage openMaterialProcess() {
        pageUtils.waitForElementAndClick(processesDetailsButton);
        return new MaterialProcessPage(driver);
    }

    /**
     * Opens the components panel
     *
     * @return new page object
     */
    public ComponentsTreePage openComponents() {
        if (!pageUtils.isElementDisplayed(componentsDetailsButton)) {
            return new ComponentsTreePage(driver);
        }
        pageUtils.waitForElementAndClick(componentsDetailsButton);
        return new ComponentsTreePage(driver);
    }

    /**
     * Opens the secondary processes page
     *
     * @return new page object
     */
    public SecondaryProcessesPage openSecondaryProcesses() {
        inputsController.openSecondaryProcesses(secondaryProcessesPencil);
        return new SecondaryProcessesPage(driver);
    }

    /**
     * Checks if secondary process pencil is enabled
     *
     * @return boolean
     */
    public boolean isSecondaryProcessButtonEnabled() {
        return pageUtils.isElementEnabled(secondaryProcessesPencil);
    }

    /**
     * Gets the process routing details
     *
     * @return string
     */
    public String getProcessRoutingDetails() {
        By processRouting = By.cssSelector("div[class='routing-name']");
        pageUtils.waitForElementToAppear(processRouting);
        return driver.findElement(processRouting).getAttribute("textContent");
    }

    /**
     * Gets material details - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getMaterialResult(String label) {
        By result = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        pageUtils.waitForElementToAppear(result);
        return Double.parseDouble(driver.findElement(result).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Gets material details - result is returned as a String with special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public String getMaterialResultText(String label) {
        By result = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        pageUtils.waitForElementToAppear(result);
        return driver.findElement(result).getAttribute("textContent").replaceAll("[^0-9?!\\.]", "");
    }

    /**
     * Checks the value of specified material
     *
     * @param label - the label
     * @return string
     */
    public String isMaterial(String label) {
        By result = By.xpath(String.format("//span[.='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(result).getAttribute("textContent");
    }

    /**
     * Gets guidance result
     *
     * @param label - the label
     * @return string
     */
    public String getGuidanceResult(String label) {
        By guidanceResult = By.xpath(String.format("//div[@class='design-guidance']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return pageUtils.waitForElementToAppear(guidanceResult).getAttribute("textContent");
    }

    /**
     * Checks the value of specified guidance
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isGuidanceResultDisplayed(String label, String value) {
        By guidanceResult = By.xpath(String.format("//div[@class='design-guidance']//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(guidanceResult);
        return driver.findElement(guidanceResult).isDisplayed();
    }

    /**
     * Gets processes result - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getProcessesResult(String label) {
        By processResult = By.xpath(String.format("//div[@class='process-summary']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(processResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Gets processes result as string - result is returned with special characters parsed
     *
     * @param label - the label
     * @return String
     */
    public String getProcessesResultText(String label) {
        By processResult = By.xpath(String.format("//div[@class='process-summary']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return pageUtils.waitForElementToAppear(processResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", "");
    }

    /**
     * Checks the value of specified process
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isProcessResultDisplayed(String label, String value) {
        By processResult = By.xpath(String.format("//div[@class='process-summary']//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(processResult);
        return driver.findElement(processResult).isDisplayed();
    }

    /**
     * Gets cost result - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getCostResults(String label) {
        return Double.parseDouble(getCostResultsString(label).replaceAll("-", "0").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Gets cost result - result is returned as a string with special characters parsed
     *
     * @param label - the label
     * @return String
     */
    public String getCostResultsText(String label) {
        return getCostResultsString(label).replaceAll("-", "0").replaceAll("[^0-9?!\\.]", "");
    }

    /**
     * Gets cost result - result is returned as a String including currency symbol
     *
     * @param label - the label
     * @return String
     */
    public String getCostResultsString(String label) {
        By costResult = By.xpath(String.format("//div[@class='cost-result-summary']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return pageUtils.waitForElementToAppear(costResult).getAttribute("textContent");
    }

    /**
     * Checks the value of specified cost
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isCostResultDisplayed(String label, String value) {
        By costResult = By.xpath(String.format("//div[@class='cost-result-summary']//span[.='%s']/following-sibling::span[.='%s']", label, value));
        return pageUtils.waitForElementToAppear(costResult).isDisplayed();
    }

    /**
     * Gets cost result - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getComponentResults(String label) {
        By costResult = By.xpath(String.format("//div[@class='sub-components-summary']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(costResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the value of specified cost
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isComponentResultDisplayed(String label, String value) {
        By costResult = By.xpath(String.format("//div[@class='sub-components-summary']//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(costResult);
        return driver.findElement(costResult).isDisplayed();
    }

    /**
     * Gets the dfm risk score
     *
     * @return string
     */
    public String getDfmRisk() {
        By risk = By.cssSelector(".design-guidance span[style]");
        return pageUtils.waitForElementToAppear(risk).getAttribute("textContent");
    }

    /**
     * Gets the dfm risk icon
     *
     * @return string
     */
    public String getDfmRiskIcon() {
        By riskIcon = By.cssSelector(".design-guidance span svg circle");
        return pageUtils.waitForElementToAppear(riskIcon).getAttribute("stroke");
    }

    /**
     * Gets list of process groups
     *
     * @return list as string
     */
    public List<String> getListOfProcessGroups() {
        pageUtils.waitForElementAndClick(processGroupList);
        return Arrays.stream(processGroupList.getText().split("\n")).filter(x -> !x.equalsIgnoreCase("Process Group")).collect(Collectors.toList());
    }

    /**
     * Gets list of digital factory
     *
     * @return list as string
     */
    public List<String> getListOfDigitalFactory() {
        pageUtils.waitForElementAndClick(digitalFactoryList);
        return inputsController.getListOfDigitalFactory(digitalFactoryList, "Digital Factory");
    }

    /**
     * Gets current digital factory
     *
     * @return string
     */
    public String getDigitalFactory() {
        return pageUtils.waitForElementToAppear(digitalFactory).getAttribute("textContent");
    }

    /**
     * Get background colour
     *
     * @param element - the element
     * @return hex code as string
     */
    public String getColour(String element) {
        WebElement elementColour = element.equalsIgnoreCase("Process Group") ? processGroupDropdown
            : element.equalsIgnoreCase("Digital Factory") ? digitalFactoryDropdown
            : element.equalsIgnoreCase("Secondary Processes") ? secondaryProcessBox
            : element.equalsIgnoreCase("Annual Volume") ? annualVolumeInput
            : element.equalsIgnoreCase("Years") ? productionLifeInput
            : element.equalsIgnoreCase("Material") ? materialName
            : null;

        return Color.fromString(pageUtils.waitForElementToAppear(elementColour).getCssValue("background-color")).asHex();
    }

    /**
     * Gets the part name
     *
     * @return part name as String
     */
    public String getPartName() {
        pageUtils.waitForElementToAppear(partName);
        return partName.getText();
    }

    /**
     * Gets the finish mass
     *
     * @return finish mass as String
     */
    public String getFinishMass() {
        pageUtils.waitForElementToAppear(finishMass);
        return finishMass.getText();
    }

    /**
     * Gets the selected process group
     *
     * @return process group as String
     */
    public String getSelectedProcessGroup() {
        pageUtils.waitForElementToAppear(currentProcessGroup);
        return currentProcessGroup.getText();
    }

    /**
     * Gets the Annual Volume
     *
     * @return annual volume as string
     */
    public String getAnnualVolume() {
        pageUtils.waitForElementToAppear(annualVolumeInput);
        return annualVolumeInput.getAttribute("value");
    }

    /**
     * Gets the Production Life in years
     *
     * @return production life as string
     */
    public String getProductionLife() {
        pageUtils.waitForElementToAppear(productionLifeInput);
        return productionLifeInput.getAttribute("value");
    }

    /**
     * Gets the selected VPE
     *
     * @return vpe as String
     */
    public String getSelectedVPE() {
        pageUtils.waitForElementToAppear(selectedVPE);
        return selectedVPE.getText();
    }

    /**
     * Checks icon is displayed
     *
     * @param icon - the icon
     * @return true/false
     */
    public boolean isIconDisplayed(StatusIconEnum icon) {
        return statusIcon.isIconDisplayed(icon);
    }

    /**
     * Opens the source selector table
     *
     * @return new page object
     */

    /**
     * Checks Sustainability tab is presented for costed part
     *
     * @return true/false
     */
    public boolean isSustainabilityDetailsPresentForCosted() {
        return pageUtils.isElementDisplayed(sustainabilityDetails);
    }

    /**
     * Get list of property names in sustainability card
     *
     * @return list of string
     */
    public List<String> getSustainabilityNames() {
        return pageUtils.waitForElementsToAppear(sustainabilityPropertyNames).stream().map(o -> o.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Opens the source selector table
     *
     * @return new page object
     */
    public SourceModelExplorePage selectSourcePart() {
        inputsController.openSourceModelSelectorTable(sourceComponentPencil);
        return new SourceModelExplorePage(driver);
    }

    /**
     * Gets the source model material
     *
     * @return string
     */
    public String getSourceModelMaterial() {
        By sourceModelMaterial = By.xpath("//label[.='Source Model Material']/following-sibling::p");
        pageUtils.waitForElementToAppear(sourceModelMaterial);
        return driver.findElement(sourceModelMaterial).getAttribute("textContent");
    }

    /**
     * Checks the source part details is displayed
     *
     * @return boolean
     */
    public boolean isSourcePartDetailsDisplayed(String scenarioName) {
        By byScenario = getByScenario(scenarioName);
        return driver.findElement(byScenario).isDisplayed();
    }

    /**
     * Opens the source component
     *
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openSourceScenario(String scenarioName) {
        By byScenario = getByScenario(scenarioName);
        pageUtils.waitForElementAndClick(byScenario);
        pageUtils.switchToWindow(1);
        return new EvaluatePage(driver);
    }

    /**
     * Get by scenario
     *
     * @param scenarioName - the scenario name
     * @return by
     */
    private By getByScenario(String scenarioName) {
        return By.xpath(String.format("//a[.='%s']", scenarioName.trim()));
    }

    /**
     * Gets error message about element was not found
     *
     * @return text of error message
     */
    public String getNotFoundMessage() {
        return modalDialogController.getNotFoundMessage();
    }

    /**
     * Clicks on Back button
     *
     * @return generic page object
     */
    public <T> T backFromError(Class<T> className) {
        return modalDialogController.backFromError(className);
    }

    /**
     * Gets secondary processes
     *
     * @return list of string
     */
    public List<String> getListOfSecondaryProcesses() {
        return Stream.of(secondaryProcessBox.getAttribute("textContent").split("\\[")).collect(Collectors.toList());
    }

    /**
     * Validates the new tab page title
     *
     * @return String
     */
    public String getTabTitle() {
        return driver.getTitle();
    }

    /**
     * Close The newly opened tab
     *
     * @return page object
     */
    public EvaluatePage closeNewlyOpenedTab() {
        List<String> listOfWindows = pageUtils.listOfWindows();
        driver.switchTo().window(listOfWindows.get(1)).close();
        driver.switchTo().window(listOfWindows.get(0));
        return new EvaluatePage(driver);
    }

    /**
     * Generates missing scenario by clicking Yes or No
     *
     * @param label - Yes or No
     * @return - new page object
     */
    public EvaluatePage generateMissingScenario(String label) {
        By byImages = By.xpath(String.format("//button[contains(text(), '%s')]", label));
        pageUtils.waitForElementAndClick(byImages);
        return this;
    }

    /**
     * Clicks on the Refresh button
     *
     * @return generic page object
     */
    public <T> T clickRefresh(Class<T> className) {
        pageUtils.waitForElementAndClick(refreshButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Changes the view to tree view
     *
     * @return current page object
     */
    public EvaluatePage treeView() {
        pageUtils.waitForElementAndClick(treeButton);
        return this;
    }

    /**
     * Ticks the Do not machine this part checkbox
     *
     * @return current page object
     */
    public EvaluatePage tickDoNotMachinePart() {
        inputsController.tickDoNotMachinePart(machinePartCheckbox);
        return this;
    }

    /**
     * Un-tick the Do not machine this part checkbox
     *
     * @return current page object
     */
    public EvaluatePage unTickDoNotMachinePart() {
        inputsController.unTickDoNotMachinePart(machinePartCheckbox);
        return this;
    }

    /**
     * Checks if the machine part checkbox is displayed
     *
     * @return boolean
     */
    public boolean isMachineOptionsCheckboxDisplayed() {
        return inputsController.isMachineOptionsCheckboxDisplayed(machinePartCheckbox);
    }

    /**
     * Checks if the machine part checkbox is selected
     *
     * @return boolean
     */
    public boolean isMachineOptionsCheckboxSelected() {
        return inputsController.isMachineOptionsCheckboxSelected(checkBoxInput);
    }

    /**
     * Get warning message
     *
     * @return string
     */
    public String getWarningMessageText() {
        By refreshWarningMessage = By.xpath("//div[@role='dialog']//div[contains(text(),'This assembly has uncosted changes.')]");
        return pageUtils.waitForElementToAppear(refreshWarningMessage).getAttribute("textContent");
    }

    /**
     * Checks if the select source model button is enabled
     *
     * @return boolean
     */
    public boolean isSelectSourceButtonEnabled() {
        return inputsController.isSelectSourceButtonEnabled(sourceComponentPencil);
    }

    /**
     * Closes the source model invalid message
     *
     * @return new page object
     */
    public EvaluatePage closeMessagePanel() {
        inputsController.closeMessagePanel(closeInvalidSourcePanelButton);
        return this;
    }

    /**
     * Gets count of open tabs
     *
     * @return int - number of open tabs
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }
}
