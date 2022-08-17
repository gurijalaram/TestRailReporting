package com.apriori.pageobjects.pages.partsandassembliesdetails;

import com.apriori.pageobjects.common.PartsAndAssemblyDetailsController;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public class PartsAndAssembliesDetailsPage extends EagerPageComponent<PartsAndAssembliesDetailsPage> {

    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    @FindBy(xpath = "//*[@data-testid='title']")
    private WebElement headerText;

    @FindBy(xpath = "//div[@data-testid='apriori-cad-viewer']")
    private WebElement cadViewer;

    @FindBy(xpath = "//div[@data-testid='cad-viewer-toolbar']")
    private WebElement cadViewerToolBar;

    @FindBy(xpath = "//div[@data-testid='scenario-results-expanded']")
    private WebElement scenarioResultsCard;

    @FindBy(xpath = "//div[@data-testid='insights-expaned']")
    private WebElement insightsCard;

    @FindBy(xpath = "//div[@data-testid='comments-expanded']")
    private WebElement commentsCard;

    @FindBy(xpath = "//div[@data-testid='Total Cost']")
    private WebElement totalCostCard;

    @FindBy(xpath = "//div[@data-testid='Scenario Inputs']")
    private WebElement scenarioInputCard;

    @FindBy(xpath = "//div[@data-testid='Material']")
    private WebElement materialCard;

    @FindBy(xpath = "//div[@data-testid='Manufacturing']")
    private WebElement manufacturingCard;

    @FindBy(xpath = "//div[@data-testid='Additional Cost']")
    private WebElement additionalCostCard;

    @FindBy(xpath = "//div[@data-testid='Total Capital Expenses']")
    private WebElement totalCapitalExpensesCard;

    @FindBy(xpath = "//button[@aria-label='Process Routing']")
    private WebElement processRoutingIcon;

    @FindBy(xpath = "//button[@aria-label='Cost']")
    private WebElement materialCostIcon;

    @FindBy(xpath = "//button[@aria-label='Part Nesting']")
    private WebElement partNestingIcon;

    @FindBy(xpath = "//button[@aria-label='Material Stock']")
    private WebElement materialStockIcon;

    @FindBy(xpath = "//button[@aria-label='Material Properties']")
    private WebElement materialPropertiesIcon;

    @FindBy(xpath = "//button[@aria-label='DFM Risk']")
    private WebElement dfmRiskIcon;

    @FindBy(xpath = "//div[@data-testid='Part Nesting']")
    private WebElement partNestingCard;

    @FindBy(xpath = "//div[@data-testid='Part Nesting']//h2")
    private WebElement partNestingTitle;

    @FindBy(xpath = "//div[@data-testid='Part Nesting']//span//*[local-name()='svg']")
    private WebElement partNestingCaretIcon;

    @FindBy(xpath = "//div[@data-testid='Material Properties']")
    private WebElement materialPropertiesCard;

    @FindBy(xpath = "//div[@data-testid='Material Properties']//h2")
    private WebElement materialPropertiesTitle;

    @FindBy(xpath = "//div[@data-testid='Material Properties']//span//*[local-name()='svg']")
    private WebElement materialPropertiesCaretIcon;

    @FindBy(xpath = "//div[@data-testid='Material Stock']")
    private WebElement materialStockCard;

    @FindBy(xpath = "//div[@data-testid='Material Stock']//h2")
    private WebElement materialStockTitle;

    @FindBy(xpath = "//div[@data-testid='Material Stock']//span//*[local-name()='svg']")
    private WebElement materialStockCaretIcon;

    @FindBy(xpath = "//div[@data-testid ='Details']")
    private WebElement partNestingDetailsSection;

    @FindBy(xpath = "//header[@data-testid='app-bar']//a")
    private WebElement linkBackToPartNAssemblyPage;

    @FindBy(xpath = "//button[@aria-label='Create New Card']")
    private WebElement createScenarioCardIcon;

    @FindBy(xpath = "//div[@class='MuiBox-root css-pf0r2f']//p")
    private WebElement titleOfModal;

    @FindBy(xpath = "//button[@title='Open']//*[local-name()='svg']")
    private WebElement dropDownFieldOnModal;

    @FindBy(id = "properties")
    private WebElement dropDownOption;

    @FindBy(id = "chip-properties-costingInput.processGroupName")
    private WebElement selectedFieldName;

    @FindBy(xpath = "//div[@data-testid='chip-properties-costingInput.processGroupName-data-chip']//*[local-name()='svg']")
    private WebElement selectedFieldRemoveIcon;

    @FindBy(id = "save-btn")
    private WebElement btnSave;

    @FindBy(id = "title")
    private WebElement nameField;

    @FindBy(xpath = "//div[@role='dialog']//*[@data-icon='times-circle']")
    private WebElement modalCloseIcon;

    @FindBy(xpath = "//div[@role='dialog']")
    private WebElement createCardModal;

    @FindBy(xpath = "//li[@data-testid ='option-item-remove']")
    private WebElement removeCardOption;

    @FindBy(id = "primary-id")
    private WebElement btnDelete;

    @FindBy(xpath = "//div[@data-testid='icon-button-group']")
    private WebElement partNestingGraphController;

    @FindBy(xpath = "//li[@data-testid ='option-item-edit']")
    private WebElement editCardOption;

    @FindBy(id = "chip-properties-analysisOfScenario.annualCost")
    private WebElement newSelectedFieldName;

    @FindBy(xpath = "//div[@data-testid='model-paper-comp-id']")
    private WebElement deleteConfirmationModal;

    @FindBy(xpath = "//div[@data-testid='title-content-id']")
    private WebElement deleteConfirmationModalTitle;

    @FindBy(xpath = "//p[@data-testid='model-content-id']")
    private WebElement deleteConfirmationModalContent;

    @FindBy(xpath = "//div[@data-testid='Process Routing']")
    private WebElement processRoutingCard;

    @FindBy(id = "chart-type-option-select-graph-process-routing")
    private WebElement processDropdown;

    @FindBy(xpath = "//div[@data-testid='process-routing-Select a process to see related details']")
    private WebElement processDetailsCard;

    @FindBy(xpath = "//div[@data-testid='Process Routing']//span[contains(@class,'MuiCardHeader-title')]//h2")
    private WebElement processDetailsCardTitle;

    @FindBy(xpath = "//div[@id='cycle-time-chart']//*[local-name()='svg']")
    private WebElement cycleTimeGraph;

    @FindBy(xpath = "//div[contains(@class,'MuiPaper-root MuiPaper-elevation MuiPaper-rounded')]//h2")
    private WebElement totalSection;

    @FindBy(id = "cycle-time-chart")
    private WebElement cycleTimeChart;

    @FindBy(xpath = "//div[@id='fully-burden-chart']//*[local-name()='svg']")
    private WebElement fullyBurdenedCostGraph;

    @FindBy(id = "fully-burden-chart")
    private WebElement fullyBurdenedCostChart;

    @FindBy(xpath = "//div[@id='piece-part-chart']//*[local-name()='svg']")
    private WebElement piecePartCostGraph;

    @FindBy(id = "piece-part-chart")
    private WebElement piecePartCostChart;

    @FindBy(xpath = "//button[@aria-label='Assembly Tree']")
    private WebElement assemblyTreeIcon;

    @FindBy(xpath = "//div[@data-testid='Assembly Tree View']")
    private WebElement assemblyTreeView;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button']")
    private WebElement showHideIcon;

    @FindBy(id = "show-hide-field-input")
    private WebElement showHideSearchField;

    @FindBy(xpath = "//span[@data-testid='switch']")
    private WebElement toggleButton;

    @FindBy(xpath = "//div[starts-with(@Class,'MuiDataGrid-cell--withRenderer')]")
    private List<WebElement> tableRow;

    @FindBy(xpath = "//*[@data-testid='app-bar']//h3")
    private WebElement subComponentName;

    @FindBy(xpath = "//button[@aria-label='Design Guidance']")
    private WebElement designGuidanceIcon;

    @FindBy(xpath = "//div[@data-testid='Issues']//button//*[local-name()='svg']")
    private WebElement issueCollapsibleIcon;

    @FindBy(xpath = "//div[@data-testid='Design Guidance']")
    private WebElement designGuidanceCard;

    @FindBy(xpath = "//div[@data-testid='Issues']")
    private WebElement issuesPanel;

    @FindBy(xpath = "//div[contains(@id,'design-guidance-holeIssue')]")
    private WebElement holeIssueIcon;

    @FindBy(xpath = "//div[@role='grid']")
    private WebElement gcdTable;

    @FindBy(xpath = "//div[contains(@class,'MuiDataGrid-virtualScrollerRenderZone')]//span")
    private WebElement gcdTableCheckBox;

    @FindBy(xpath = "//div[@data-testid='Investigation']//button//*[local-name()='svg']")
    private WebElement investigationCollapsibleIcon;

    @FindBy(xpath = "//div[@data-testid='Investigation']")
    private WebElement investigationPanel;

    @FindBy(xpath = "//div[contains(@id,'design-guidance-DISTINCT_SIZES')]")
    private WebElement distinctSizeCountTopic;

    @FindBy(xpath = "//*[@data-icon='circle-dollar']")
    private WebElement costIcon;

    @FindBy(xpath = "//div[@data-testid='Cost']")
    private WebElement costSection;

    @FindBy(xpath = "//div[@data-testid='Cost']//h2")
    private WebElement titleCost;

    @FindBy(xpath = "//div[@data-testid='chart-type-option-select-graph-cost']")
    private WebElement costDropDown;

    @FindBy(id = "fullyBurdenedCost-cost-graph")
    private WebElement costGraphFullyBurdened;

    @FindBy(id = "piecePartCost-cost-graph")
    private WebElement costGraphPiecePart;

    @FindBy(id = "totalCapitalInvestment-cost-graph")
    private WebElement costGraphTotalCapitalInvestment;

    @FindBy(xpath = "//div[contains(@id,'design-guidance-row-proximityWarning')]")
    private WebElement proximityLbl;


    public PartsAndAssembliesDetailsPage(WebDriver driver) {

        this(driver, log);
    }

    private WebDriver driver;
    private PartsAndAssemblyDetailsController partsAndAssemblyDetailsController;

    public PartsAndAssembliesDetailsPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.waitForCADViewerLoad();
        this.partsAndAssemblyDetailsController = new PartsAndAssemblyDetailsController(driver);

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Method to wait until CAD loading complete
     */
    public void waitForCADViewerLoad() {
        getPageUtils().waitForElementToAppear(spinner);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),1);
    }

    /**
     * Gets the header text on header bar
     *
     * @return String
     */
    public String getHeaderText() {
        return getPageUtils().waitForElementToAppear(headerText).getText();
    }

    /**
     * Check if 3D-CAD viewer display on the page
     *
     * @return true/false
     */
    public boolean is3DCadViewerDisplayed() {
        return getPageUtils().isElementDisplayed(cadViewer);
    }

    /**
     * Check if 3D-CAD Viewer toolbar display on the page
     *
     * @return true/false
     */
    public boolean is3DCadViewerToolBarDisplayed() {
        return getPageUtils().isElementDisplayed(cadViewerToolBar);
    }

    /**
     *
     * Checks if Scenario Results Card displayed
     *
     * @return true/false
     */
    public boolean isScenarioResultsCardDisplayed() {
        return getPageUtils().isElementDisplayed(scenarioResultsCard);
    }

    /**
     * Checks if Insights Card displayed
     *
     * @return true/false
     */
    public boolean isInsightsCardDisplayed() {
        return getPageUtils().isElementDisplayed(insightsCard);
    }

    /**
     * Checks if Comments Card displayed
     *
     * @return true/false
     */
    public boolean isCommentsCardDisplayed() {
        return getPageUtils().isElementDisplayed(commentsCard);
    }

    /**
     * Checks if total cost card displayed
     *
     * @return true/false
     */
    public boolean isTotalCostCardDisplayed() {
        return getPageUtils().isElementDisplayed(totalCostCard);
    }

    /**
     * Checks if scenario inputs card displayed
     *
     * @return true/false
     */
    public boolean isScenarioInputsCardDisplayed() {
        return getPageUtils().isElementDisplayed(scenarioInputCard);
    }

    /**
     * Checks if material card displayed
     *
     * @return true/false
     */
    public boolean isMaterialCardDisplayed() {
        return getPageUtils().isElementDisplayed(materialCard);
    }

    /**
     * Checks if manufacturing card displayed
     *
     * @return true/false
     */
    public boolean isManufacturingCardDisplayed() {
        return getPageUtils().isElementDisplayed(materialCard);
    }

    /**
     * Checks if additional cost card displayed
     *
     * @return true/false
     */
    public boolean isAdditionalCostCardDisplayed() {
        return getPageUtils().isElementDisplayed(additionalCostCard);
    }

    /**
     * Checks if total capital expenses card displayed
     *
     * @return true/false
     */
    public boolean isTotalCapitalExpensesCardDisplayed() {
        return getPageUtils().isElementDisplayed(totalCapitalExpensesCard);
    }

    /**
     * Gets scenario results card field names
     *
     * @return list of string
     */
    public List<String> getScenarioResultCardFieldsName(String cardName) {
        return partsAndAssemblyDetailsController.getScenarioResultCardFields(cardName);
    }

    /**
     * Gets the header title on Insights section
     *
     * @return String
     */
    public String getHeaderTitleOnInsights() {
        return getPageUtils().waitForElementToAppear(insightsCard).getText();
    }

    /**
     * Checks if Process routine menu item displayed
     *
     * @return true/false
     */
    public boolean isProcessRoutingMenuIconDisplayed() {
        return getPageUtils().isElementDisplayed(processRoutingIcon);
    }

    /**
     * Checks if cost menu item displayed
     *
     * @return true/false
     */
    public boolean isCostMenuIconDisplayed() {
        return getPageUtils().isElementDisplayed(materialCostIcon);
    }

    /**
     * Checks if Part Nesting menu item displayed
     *
     * @return true/false
     */
    public boolean isPartNestingMenuIconDisplayed() {
        return getPageUtils().isElementDisplayed(partNestingIcon);
    }

    /**
     * Checks if Material Stock menu item displayed
     *
     * @return true/false
     */
    public boolean isMaterialStockMenuIconDisplayed() {
        return getPageUtils().isElementDisplayed(materialStockIcon);
    }

    /**
     * Checks if Material properties menu item displayed
     *
     * @return true/false
     */
    public boolean isMaterialPropertiesMenuIconDisplayed() {
        return getPageUtils().isElementDisplayed(materialPropertiesIcon);
    }

    /**
     * Checks if DFM Risk menu item displayed
     *
     * @return true/false
     */
    public boolean isDfmRiskMenuIconDisplayed() {
        return getPageUtils().isElementDisplayed(dfmRiskIcon);
    }


    /**
     * Clicks on part nesting menu icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickPartNestingIcon() {
        getPageUtils().waitForElementAndClick(partNestingIcon);
        return this;
    }

    /**
     * Checks if part nesting card displayed
     *
     * @return true/false
     */
    public boolean isPartNestingCardDisplayed() {
        return getPageUtils().isElementDisplayed(partNestingCard);
    }

    /**
     * gets parts nesting section title
     *
     * @return string
     */
    public String getPartNestingTitle() {
        return getPageUtils().waitForElementToAppear(partNestingTitle).getText();
    }

    /**
     * gets parts nesting state
     *
     * @return string
     */
    public String getPartNestingState() {
        return getPageUtils().waitForElementToAppear(partNestingCaretIcon).getAttribute("data-icon");
    }

    /**
     * click to collapse the part nesting card
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage collapsePartNestingSection() {
        getPageUtils().waitForElementAndClick(partNestingCaretIcon);
        return this;
    }

    /**
     * Clicks on material properties menu icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickMaterialPropertiesIcon() {
        getPageUtils().waitForElementAndClick(materialPropertiesIcon);
        return this;
    }

    /**
     * Checks if material properties card displayed
     *
     * @return true/false
     */
    public boolean isMaterialPropertiesCardDisplayed() {
        return getPageUtils().isElementDisplayed(materialPropertiesCard);
    }

    /**
     * gets material properties title
     *
     * @return string
     */
    public String getMaterialPropertiesTitle() {
        return getPageUtils().waitForElementToAppear(materialPropertiesTitle).getText();
    }

    /**
     * gets material properties state
     *
     * @return string
     */
    public String getMaterialPropertiesState() {
        return getPageUtils().waitForElementToAppear(materialPropertiesCaretIcon).getAttribute("data-icon");
    }

    /**
     * click to collapse the material properties
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage collapseMaterialPropertiesSection() {
        getPageUtils().waitForElementAndClick(materialPropertiesCaretIcon);
        return this;
    }

    /**
     * Clicks on material stock menu icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickMaterialStockIcon() {
        getPageUtils().waitForElementAndClick(materialStockIcon);
        return this;
    }

    /**
     * Checks if material stock card displayed
     *
     * @return true/false
     */
    public boolean isMaterialStockCardDisplayed() {
        return getPageUtils().isElementDisplayed(materialStockCard);
    }

    /**
     * gets material Stock title
     *
     * @return string
     */
    public String getMaterialStockTitle() {
        return getPageUtils().waitForElementToAppear(materialStockTitle).getText();
    }

    /**
     * gets material stock state
     *
     * @return string
     */
    public String getMaterialStockState() {
        return getPageUtils().waitForElementToAppear(materialStockCaretIcon).getAttribute("data-icon");
    }

    /**
     * click to collapse the material properties
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage collapseMaterialStockSection() {
        getPageUtils().waitForElementAndClick(materialStockCaretIcon);
        return this;
    }

    /**
     * Checks if part nesting details section displayed
     *
     * @return true/false
     */
    public boolean isPartNestingDetailsSectionDisplayed() {
        return getPageUtils().isElementDisplayed(partNestingDetailsSection);
    }

    /**
     * Get items on each detail section
     *
     * @return list of string
     */
    public List<String> getItemsOfSections(String section) {
        return partsAndAssemblyDetailsController.getItemsOfSections(section);
    }

    /**
     * Checks if parts and assemblies link displayed
     *
     * @return true/false
     */
    public boolean isPartsAndAssembliesLinkDisplayed() {
        return getPageUtils().isElementDisplayed(linkBackToPartNAssemblyPage);
    }

    /**
     * click to go back to parts and assemblies page
     *
     * @return new page object
     */
    public PartsAndAssembliesPage clicksPartsAndAssembliesLink() {
        getPageUtils().waitForElementAndClick(linkBackToPartNAssemblyPage);
        return new PartsAndAssembliesPage(driver);
    }

    /**
     * Checks if option to create new scenario results card displayed
     *
     * @return true/false
     */
    public boolean isCreateNewCardOptionDisplayed() {
        return getPageUtils().isElementDisplayed(createScenarioCardIcon);
    }

    /**
     * clicks to open the card setting modal
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickToOpenModal() {
        getPageUtils().waitForElementAndClick(createScenarioCardIcon);
        return this;
    }

    /**
     * gets the modal title
     *
     * @return a String
     */
    public String getModalTitle() {
        return getPageUtils().waitForElementToAppear(titleOfModal).getText();
    }

    /**
     * clicks to open the drop-down field
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickToOpenDropDown() {
        getPageUtils().waitForElementAndClick(dropDownFieldOnModal);
        return this;
    }

    /**
     * select a field from drop-down
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage selectAnOption(String fieldOption) {
        getPageUtils().waitForElementAndClick(By.xpath("//span[contains(.,'" + fieldOption + "')]"));
        return this;
    }

    /**
     * get selected field name
     *
     * @return a String
     */
    public String getSelectedFieldName() {
        return getPageUtils().waitForElementToAppear(selectedFieldName).getAttribute("innerText");
    }

    /**
     * clicks to remove the selected field
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickToRemoveSelectedField() {
        getPageUtils().waitForElementAndClick(selectedFieldRemoveIcon);
        return this;
    }

    /**
     * get save button status as disabled
     *
     * @return a boolean
     */
    public boolean getSaveButtonStatus() {
        return getPageUtils().waitForElementToAppear(btnSave).isEnabled();
    }

    /**
     * Enter a name for the new card
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage enterCardName(String cardName) {
        getPageUtils().waitForElementToAppear(nameField).sendKeys(cardName);
        return this;
    }

    /**
     * clicks save button
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickSaveButton() {
        getPageUtils().waitForElementAndClick(btnSave);
        return this;
    }

    /**
     * clicks close icon on modal
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage closeModal() {
        getPageUtils().waitForElementAndClick(modalCloseIcon);
        return this;
    }

    /**
     * Checks if create new card modal displayed
     *
     * @return true/false
     */
    public boolean isCreateCardModalDisplayed() {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@role='dialog']"),1);
        return getPageUtils().isElementDisplayed(createCardModal);
    }

    /**
     * Checks if created new card displayed
     *
     * @return true/false
     */
    public boolean isCreatedCardDisplayed(String cardName) {
        return getPageUtils().isElementDisplayed(By.xpath("//div[@data-testid='" + cardName + "']"));
    }

    /**
     * clicks on ellipsis icon on created card
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickMoreOptions(String cardName) {
        getPageUtils().waitForElementAndClick(By.xpath("//div[@data-testid ='" + cardName + "']//*[@data-icon='ellipsis-v']"));
        return this;
    }

    /**
     * clicks on remove card option
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickRemoveCardOption() {
        getPageUtils().waitForElementAndClick(removeCardOption);
        return this;
    }

    /**
     * clicks on remove card option
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickDeleteButton() {
        getPageUtils().waitForElementAndClick(btnDelete);
        return this;
    }

    /**
     * Delete Scenario Result Card
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage deleteScenarioResultsCard(String cardName) {
        clickMoreOptions(cardName);
        clickRemoveCardOption();
        clickDeleteButton();
        return this;
    }

    /**
     * Checks if part nesting graph controller displayed
     *
     * @return true/false
     */
    public boolean isPartNestingGraphControllerDisplayed() {
        return getPageUtils().isElementDisplayed(partNestingGraphController);
    }

    /**
     * Checks if edit card option displayed
     *
     * @return true/false
     */
    public boolean isEditCardOptionDisplayed() {
        return getPageUtils().waitForElementToAppear(editCardOption).isDisplayed();
    }

    /**
     * clicks on edit card option
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickEditCardOption() {
        getPageUtils().waitForElementAndClick(editCardOption);
        return this;
    }

    /**
     * Get the card name
     *
     * @return String
     */
    public String getCardName() {
        return getPageUtils().waitForElementToAppear(nameField).getAttribute("value");
    }

    /**
     * clear card name
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clearCardName() {
        getPageUtils().clearValueOfElement(nameField);
        return this;
    }

    /**
     * get selected field name
     *
     * @return a String
     */
    public String getNewSelectedFieldName() {
        return getPageUtils().waitForElementToAppear(newSelectedFieldName).getAttribute("innerText");
    }

    /**
     * Remove selected fields
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage removeSelectedField(String selectedFieldName) {
        getPageUtils().waitForElementAndClick(By.xpath("//div[starts-with(@id,'chip-properties')]//span[text()='" + selectedFieldName + "']//..//*[local-name()='svg']"));
        return this;
    }

    /**
     * Checks if remove option displayed for scenario results card
     *
     * @return true/false
     */
    public boolean isRemoveOptionDisplayed() {
        return getPageUtils().waitForElementAppear(removeCardOption).isDisplayed();
    }

    /**
     * Checks if delete confirmation modal displayed
     *
     * @return true/false
     */
    public boolean isDeleteConfirmationModalDisplayed() {
        return getPageUtils().waitForElementAppear(deleteConfirmationModal).isDisplayed();
    }

    /**
     * get delete confirmation modal title
     *
     * @return a String
     */
    public String getDeleteModalTitle() {
        return getPageUtils().waitForElementToAppear(deleteConfirmationModalTitle).getText();
    }

    /**
     * get delete confirmation modal content
     *
     * @return a String
     */
    public String getDeleteModalContent() {
        return getPageUtils().waitForElementToAppear(deleteConfirmationModalContent).getText();
    }

    /**
     * Check if insight cards have option to delete
     *
     * @return true/false
     */
    public Boolean isInsightsCardsDeleteOptionDisplayed(String cardName) {
        return getPageUtils().isElementDisplayed(By.xpath("//div[@data-testid ='" + cardName + "']//*[@data-icon='ellipsis-v']"));
    }

    /**
     * clicks on process routing card
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickProcessRouting() {
        getPageUtils().waitForElementAndClick(processRoutingIcon);
        return this;
    }

    /**
     * Checks if process routing displayed
     *
     * @return true/false
     */
    public boolean isProcessRoutingCardDisplayed() {
        return getPageUtils().waitForElementAppear(processRoutingCard).isDisplayed();
    }

    /**
     * select process from the dropdown
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage selectProcess(String processName) {
        getPageUtils().waitForElementAndClick(processDropdown);
        getPageUtils().waitForElementAndClick(By.xpath("//ul[@role='listbox']//span[text()='" + processName + "']"));
        return this;
    }

    /**
     * Checks if process details section displayed
     *
     * @return true/false
     */
    public boolean isProcessDetailsSectionDisplayed() {
        return getPageUtils().waitForElementAppear(processDetailsCard).isDisplayed();
    }

    /**
     * get process details routing card title
     *
     * @return a String
     */
    public String getProcessRoutingCardTitle() {
        return getPageUtils().waitForElementToAppear(processDetailsCardTitle).getText();
    }

    /**
     * Checks if cycle time graph displayed
     *
     * @return true/false
     */
    public boolean isCycleTimeGraphDisplayed() {
        return getPageUtils().waitForElementAppear(cycleTimeGraph).isDisplayed();
    }

    /**
     * Checks if total section displayed
     *
     * @return true/false
     */
    public boolean isTotalSectionDisplayed() {
        return getPageUtils().waitForElementAppear(totalSection).isDisplayed();
    }

    /**
     * clicks on cycle time chart
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickCycleTimeChart() {
        getPageUtils().waitForElementAndClick(cycleTimeChart);
        return this;
    }

    /**
     * Gets process routing details
     *
     * @return list of string
     */
    public  List<String> getProcessRoutingDetails() {
        return partsAndAssemblyDetailsController.getProcessRoutingDetails();
    }

    /**
     * Checks if fully burdened graph displayed
     *
     * @return true/false
     */
    public boolean isFullyBurdenedGraphDisplayed() {
        return getPageUtils().waitForElementAppear(fullyBurdenedCostGraph).isDisplayed();
    }

    /**
     * clicks on fully burdened cost chart
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickFullyBurdenedCostChart() {
        getPageUtils().waitForElementAndClick(fullyBurdenedCostChart);
        return this;
    }

    /**
     * Checks if piece part cost graph displayed
     *
     * @return true/false
     */
    public boolean isPiecePartCostGraphDisplayed() {
        return getPageUtils().waitForElementAppear(piecePartCostGraph).isDisplayed();
    }

    /**
     * clicks on piece part cost chart
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickPiecePartCostCostChart() {
        getPageUtils().waitForElementAndClick(piecePartCostChart);
        return this;
    }

    /**
     * Gets the process details
     *
     * @return string
     */
    public String getProcessDetails(String processDetails) {
        return driver.findElement(By.xpath("//div[@id='process-routing']//div[text()='" + processDetails + "']")).getAttribute("textContent");
    }

    /**
     * clicks on assembly tree icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickAssemblyTree() {
        getPageUtils().waitForElementAppear(processRoutingCard);
        getPageUtils().waitForElementAndClick(assemblyTreeIcon);
        return this;
    }

    /**
     * Checks if assembly tree icon is displayed
     *
     * @return true/false
     */
    public boolean isAssemblyTreeIconDisplayed() {
        return getPageUtils().waitForElementAppear(assemblyTreeIcon).isDisplayed();
    }

    /**
     * Checks if assembly tree view is displayed
     *
     * @return true/false
     */
    public boolean isAssemblyTreeViewDisplayed() {
        return getPageUtils().waitForElementAppear(assemblyTreeView).isDisplayed();
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public  List<String> getTableHeaders() {
        return partsAndAssemblyDetailsController.getTableHeaders();
    }

    /**
     * clicks on show/hide icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickShowHideOption() {
        getPageUtils().waitForElementAndClick(showHideIcon);
        return this;
    }

    /**
     * hide field from table
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage hideField(String fieldName) {
        getPageUtils().waitForElementToAppear(showHideSearchField).sendKeys(fieldName);
        getPageUtils().waitForElementAndClick(toggleButton);
        getPageUtils().waitForElementsToAppear(tableRow);
        return this;
    }

    /**
     * Opens the assembly part
     *
     * @return a new page object
     */
    public PartsAndAssembliesDetailsPage openAssembly(String componentName, String scenarioName) {
        getPageUtils().waitForElementsToAppear(tableRow);
        getPageUtils().waitForElementAndClick(driver.findElement(By.xpath(String.format("//div[@data-field='scenarioName']//p[text()='%s']/ancestor::div[@role='row']//div[@data-field='componentName']//p[text()='%s']", scenarioName.trim(), componentName.trim()))));
        getPageUtils().windowHandler(1);
        return new PartsAndAssembliesDetailsPage(getDriver());
    }

    /**
     * Get subcomponent name
     *
     * @return string
     */
    public String getSubComponentName() {
        return getPageUtils().waitForElementToAppear(subComponentName).getText();
    }

    /**
     * clicks on design guidance icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickDesignGuidance() {
        getPageUtils().waitForElementToAppear(proximityLbl);
        getPageUtils().waitForElementAndClick(designGuidanceIcon);
        return this;
    }

    /**
     * clicks on design guidance issue collapsible icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickIssueCollapsibleIcon() {
        getPageUtils().waitForElementAndClick(issueCollapsibleIcon);
        return this;
    }

    /**
     * get issue collapsible status
     *
     * @return String
     */
    public String getIssueCollapsibleIcons() {
        return getPageUtils().waitForElementToAppear(issueCollapsibleIcon).getAttribute("data-icon");
    }

    /**
     * Checks if design guidance card displayed
     *
     * @return true/false
     */
    public boolean isDesignGuidanceCardDisplayed() {
        return getPageUtils().waitForElementAppear(designGuidanceCard).isDisplayed();
    }

    /**
     * Checks if issues panel displayed
     *
     * @return true/false
     */
    public boolean isIssuesPanelDisplayed() {
        return getPageUtils().waitForElementAppear(issuesPanel).isDisplayed();
    }

    /**
     * Gets design guidance details
     *
     * @return list of string
     */
    public  List<String> getDesignGuidanceDetails() {
        return partsAndAssemblyDetailsController.getDesignGuidanceDetails();
    }

    /**
     * Gets the issue details
     *
     * @return string
     */
    public String getIssueDetails(String issueName) {
        return driver.findElement(By.xpath("//div[@data-testid='Issues']//div[text()='" + issueName + "']//..//div[contains(@data-testid,'value')]")).getAttribute("textContent");
    }

    /**
     * clicks on hole issue section
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickOnHoleIssue() {
        getPageUtils().waitForElementAndClick(holeIssueIcon);
        return this;
    }

    /**
     * Checks if GCD table displayed
     *
     * @return true/false
     */
    public boolean isGCDTableDisplayed() {
        return getPageUtils().waitForElementAppear(gcdTable).isDisplayed();
    }

    /**
     * get checkbox status
     *
     * @return String
     */
    public String geCheckBoxStatus() {
        return getPageUtils().waitForElementToAppear(gcdTableCheckBox).getAttribute("class");
    }

    /**
     * clicks on design guidance investigation collapsible icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickInvestigationCollapsibleIcon() {
        getPageUtils().waitForElementAndClick(investigationCollapsibleIcon);
        return this;
    }

    /**
     * get investigation section collapsible status
     *
     * @return String
     */
    public String getInvestigationCollapsibleState() {
        return getPageUtils().waitForElementToAppear(investigationCollapsibleIcon).getAttribute("data-icon");
    }

    /**
     * Checks if investigation panel displayed
     *
     * @return true/false
     */
    public boolean isInvestigationPanelDisplayed() {
        return getPageUtils().waitForElementAppear(investigationPanel).isDisplayed();
    }

    /**
     * Gets investigation topics details
     *
     * @return list of string
     */
    public  List<String> getInvestigationTopics() {
        return partsAndAssemblyDetailsController.getInvestigationDetails();
    }

    /**
     * clicks on Distinct Sizes Count topic
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickOnDistinctSizesCount() {
        getPageUtils().waitForElementAndClick(distinctSizeCountTopic);
        return this;
    }

    /**
     * Checks if costs icon is displayed
     *
     * @return true/false
     */
    public boolean isCostsOptionDisplayed() {
        getPageUtils().waitForElementToAppear(proximityLbl);
        return getPageUtils().waitForElementAppear(costIcon).isDisplayed();
    }

    /**
     * clicks on costs icon
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickCostsOption() {
        getPageUtils().waitForElementAndClick(costIcon);
        return this;
    }

    /**
     * Checks if costs section is displayed
     *
     * @return true/false
     */
    public boolean isCostSectionDisplayed() {
        return getPageUtils().waitForElementAppear(costSection).isDisplayed();
    }

    /**
     * Get Cost section title
     *
     * @return a String
     */
    public String getCostTitle() {
        return getPageUtils().waitForElementAppear(titleCost).getText();
    }

    /**
     * select option
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage selectCostDropDownOption(String option) {
        getPageUtils().waitForElementAndClick(costDropDown);
        getPageUtils().waitForElementAndClick(By.xpath("//li[@role='option']//*[contains(text(),'" + option + "')]"));
        return this;
    }

    /**
     * Checks if fully burdened cost- cost graph is displayed
     *
     * @return true/false
     */
    public boolean isFullyBurdenedCostCostGraphDisplayed() {
        return getPageUtils().waitForElementAppear(costGraphFullyBurdened).isDisplayed();
    }

    /**
     * Checks if piece Part cost- cost graph is displayed
     *
     * @return true/false
     */
    public boolean isPiecePartCostCostGraphDisplayed() {
        return getPageUtils().waitForElementAppear(costGraphPiecePart).isDisplayed();
    }

    /**
     * Checks if total capital investment- cost graph is displayed
     *
     * @return true/false
     */
    public boolean isTotalCapitalInvestmentCostGraphDisplayed() {
        return getPageUtils().waitForElementAppear(costGraphTotalCapitalInvestment).isDisplayed();
    }
}