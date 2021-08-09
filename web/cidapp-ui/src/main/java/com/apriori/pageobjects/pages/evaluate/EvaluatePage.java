package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.common.CustomAttributesInputsController;
import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.inputs.CustomAttributesPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;

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

/**
 * @author cfrith
 */

public class EvaluatePage extends EvaluateToolbar {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatePage.class);

    @FindBy(css = ".costing-inputs .spinner-border")
    private List<WebElement> panelLoaders;

    @FindBy(css = ".webviewer-canvas")
    private WebElement viewerCanvas;

    @FindBy(css = ".scenario-state-preview [data-icon='cog']")
    private List<WebElement> cogIcon;

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

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field'] .pill-box")
    private WebElement secondaryProcessBox;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field'] button")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = "div[id='qa-material-modal-select-field'] button")
    private WebElement materialsPencil;

    @FindBy(css = ".material-summary-card.card input")
    private WebElement materialName;

    @FindBy(xpath = "//span[contains(text(), 'Finish Mass')]/following-sibling::span")
    private WebElement finishMass;

    @FindBy(css = ".design-guidance-summary-card button")
    private WebElement designGuidanceDetailsButton;

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

    @FindBy(css = ".sub-components-summary-card button")
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

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Secondary']")
    private WebElement secondaryTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Custom Attributes']")
    private WebElement customAttributesTab;

    @FindBy(css = (".modal-title"))
    private WebElement sourceModel;

    private PageUtils pageUtils;
    private WebDriver driver;
    private InputsController inputsController;
    private CustomAttributesInputsController customAttributesInputsController;
    private StatusIcon statusIcon;
    private ModalDialogController modalDialogController;

    public EvaluatePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.inputsController = new InputsController(driver);
        this.customAttributesInputsController = new CustomAttributesInputsController(driver);
        this.statusIcon = new StatusIcon(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        pageUtils.invisibilityOfElements(panelLoaders);
        pageUtils.invisibilityOfElements(cogIcon);
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
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage selectProcessGroup(ProcessGroupEnum processGroup) {
        inputsController.selectProcessGroup(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * Selects the digital factory dropdown
     *
     * @param digitalFactory - the digital factory
     * @return current page object
     */
    public EvaluatePage selectDigitalFactory(DigitalFactoryEnum digitalFactory) {
        inputsController.selectDigitalFactory(digitalFactoryDropdown, digitalFactory);
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
     * Opens secondary input tab
     *
     * @return new page object
     */
    public SecondaryPage goToSecondaryTab() {
        pageUtils.waitForElementAndClick(secondaryTab);
        return new SecondaryPage(driver);
    }

    /**
     * Opens custom attributes tab
     *
     * @return new page object
     */
    public CustomAttributesPage goToCustomAttributesTab() {
        pageUtils.waitForElementAndClick(customAttributesTab);
        return new CustomAttributesPage(driver);
    }

    /**
     * Checks material info is displayed
     *
     * @return true/false
     */
    public boolean isMaterialInfoDisplayed(String material) {
        By materialsInfo = By.cssSelector(String.format("div[id='qa-material-modal-select-field'] input[value='%s']", material));
        return pageUtils.waitForElementToAppear(materialsInfo).isDisplayed();
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
    public GuidanceIssuesPage openDesignGuidance() {
        pageUtils.waitForElementAndClick(designGuidanceDetailsButton);
        return new GuidanceIssuesPage(driver);
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
    public ComponentsListPage openComponents() {
        if (!pageUtils.isElementDisplayed(componentsDetailsButton)) {
            return new ComponentsListPage(driver);
        }
        pageUtils.waitForElementAndClick(componentsDetailsButton);
        return new ComponentsListPage(driver);
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
        By costResult = By.xpath(String.format("//div[@class='cost-result-summary']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(costResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
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

        return Color.fromString(pageUtils.waitForElementAppear(elementColour).getCssValue("background-color")).asHex();
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
     * Gets component result - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public String getComponentResults(String label) {
        By componentResult = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return pageUtils.waitForElementToAppear(componentResult).getAttribute("textContent");
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
     * Gets the source part details
     *
     * @return string
     */
    public String getSourcePartDetails() {
        By sourcePart = By.cssSelector("[id='qa-source-model-modal-select-field'] .input-group");
        pageUtils.waitForElementToAppear(sourcePart);
        return driver.findElement(sourcePart).getAttribute("textContent");
    }

    /**
     * Opens the source component
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public EvaluatePage openSourceScenario(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//div[.='%s']/following-sibling::div//a[.='%s']", componentName.toUpperCase().trim(), scenarioName.trim()));
        pageUtils.waitForElementAndClick(scenario);
        pageUtils.waitForElementAndClick(scenario);
        pageUtils.windowHandler(1);
        return new EvaluatePage(driver);
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

    public String getSourceModelInvalid() {
         return pageUtils.waitForElementToAppear(sourceModel).getAttribute("textContent");
    }
}