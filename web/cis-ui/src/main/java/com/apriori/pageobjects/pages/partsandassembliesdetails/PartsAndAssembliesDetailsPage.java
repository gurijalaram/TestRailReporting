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

    @FindBy(xpath = "//div[@data-testid='input-properties']")
    private WebElement dropDownFieldOnModal;

    @FindBy(id="properties")
    private WebElement dropDownOption;

    @FindBy(id="chip-properties-costingInput.processGroupName")
    private WebElement chipDropDownOption;

    @FindBy(xpath = "//*[@data-testid='chip-properties-costingInput.vpeName-data-chip-delete-icon']")
    private WebElement chipRemoveIcon;

    @FindBy(id="save-btn")
    private WebElement btnSave;

    @FindBy(id="title")
    private WebElement nameField;

   // @FindBy(xpath = "//div[@data-testid='Testautomation']")
   // private WebElement createdNewCard;

    @FindBy(xpath = "//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeSmall css-1nn0sz6']//*[@data-icon='times-circle']")
    private WebElement modalClearIcon;

    @FindBy(xpath = "//div[@class='MuiBox-root css-pf0r2f']")
    private WebElement createCardModal;

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
        getPageUtils().mouseMove(dropDownOption);
         getPageUtils().waitForElementAndClick(By.xpath("//input[@aria-activedescendant='" + fieldOption + "']"));
         return this;
    }

    /**
     * get selected option as chip
     *
     * @return a String
     */
    public String getSelectedFieldOption() {
        return getPageUtils().waitForElementToAppear(chipDropDownOption).getAttribute("id");
    }

    /**
     * clicks to remove the selected option on chip list
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickToRemoveChip() {
        getPageUtils().waitForElementAndClick(chipRemoveIcon);
        return this;
    }

    /**
     * get save button status as disabled
     *
     * @return a String
     */
    public String getSaveButtonStatus() {
        return getPageUtils().waitForElementToAppear(btnSave).getAttribute("");
    }

    /**
     * Enter a name for the new card
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage enterAName(String cardName) {
        getPageUtils().waitForElementToAppear(nameField).sendKeys(cardName);
        return this;
    }

    /**
     * clicks save button to create new card
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickSaveBtn() {
        getPageUtils().waitForElementAndClick(btnSave);
        return this;
    }

    /**
     * clicks clear button on modal
     *
     * @return current page object
     */
    public PartsAndAssembliesDetailsPage clickClearBtn() {
        getPageUtils().waitForElementAndClick(btnSave);
        return this;
    }

    /**
     * Checks if create new card modal displayed
     *
     * @return true/false
     */
    public boolean isCreateCardModalDisplayed() {
        return getPageUtils().isElementDisplayed(createCardModal);
    }

}