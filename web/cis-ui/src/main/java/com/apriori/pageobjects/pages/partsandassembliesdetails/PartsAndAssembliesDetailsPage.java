package com.apriori.pageobjects.pages.partsandassembliesdetails;

import com.apriori.pageobjects.common.PartsAndAssemblyDetailsController;
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

    @FindBy(xpath = "//div[.='Scenario Results']")
    private WebElement scenarioResultsCard;

    @FindBy(xpath = "//div[.='Insights']")
    private WebElement insightsCard;

    @FindBy(xpath = "//div[.='Comments']")
    private WebElement commentsCard;

    @FindBy(xpath = "//div[@data-testid='collapsible-card-1']")
    private WebElement totalCostCard;

    @FindBy(xpath = "//div[@data-testid='collapsible-card-2']")
    private WebElement scenarioInputCard;

    @FindBy(xpath = "//div[@data-testid='collapsible-card-3']")
    private WebElement materialCard;

    @FindBy(xpath = "//div[@data-testid='collapsible-card-4']")
    private WebElement manufacturingCard;

    @FindBy(xpath = "//div[@data-testid='collapsible-card-5']")
    private WebElement additionalCostCard;

    @FindBy(xpath = "//div[@data-testid='collapsible-card-6']")
    private WebElement totalCapitalExpensesCard;


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
}