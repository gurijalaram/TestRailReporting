package com.apriori.pageobjects.reports.pages.create;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAdHocViewDesignerPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(CreateAdHocViewDesignerPage.class);

    @FindBy(xpath = "//div[contains(text(), 'New Ad Hoc View')]")
    private WebElement designerTitle;

    @FindBy(id = "node2")
    private WebElement firstFieldElement;

    @FindBy(id = "node337")
    private WebElement firstMeasureElement;

    @FindBy(id = "node270")
    private WebElement scenarioTopLevelElement;

    @FindBy(id = "node281")
    private WebElement scenarioPartNumber;

    @FindBy(xpath = "//div[@id='availableFields']/div[contains(@class, 'secondary')]//input")
    private WebElement measuresSearchInput;

    @FindBy(xpath = "//div[@id='availableFields']/div[contains(@class, 'secondary')]//a[@class='button search up']")
    private WebElement measuresSearchButton;

    @FindBy(xpath = "//ul[@id='measuresTree']/li/ul/li[1]")
    private WebElement scenarioFullyBurdenedCostElement;

    @FindBy(xpath = "//ul[@id='dimensionsTree']/li/ul/li[2]")
    private WebElement scenarioPartNumberElement;

    @FindBy(xpath = "//button[@class='button minimize superfocus subfocus']")
    private WebElement filtersButton;

    @FindBy(xpath = "//div[@id='availableFields']/div[contains(@class, 'primary')]//input")
    private WebElement fieldsSearchInput;

    @FindBy(xpath = "//div[@id='availableFields']/div[contains(@class, 'primary')]//a[@class='button search up']")
    private WebElement fieldsSearchButton;

    @FindBy(xpath = "//p[contains(text(), 'Columns')]/..")
    private WebElement addToColumnsRightClickOption;

    @FindBy(xpath = "//p[contains(text(), 'Rows')]/..")
    private WebElement addToRowsRightClickOption;

    @FindBy(xpath = "//ul[@class='levels']//span[contains(text(), 'Scenario Part Number')]/..")
    private WebElement scenarioPartNumberInRows;

    @FindBy(xpath = "//ul[@class='members']//span[contains(text(), 'Scenario Fully Burdened Cost')]/..")
    private WebElement scenarioFbcInColumns;

    @FindBy(xpath = "//div[@id='filters']/button")
    private WebElement openFiltersButton;

    @FindBy(xpath = "//span[@id='filterPanelMutton']/preceding-sibling::div")
    private WebElement filtersTabTitle;

    @FindBy(xpath = "//p[contains(text(), 'Create Filter')]/..")
    private WebElement createFilterRightClickOption;

    @FindBy(xpath = "//div[@id='filter-container']//a")
    private WebElement filterCriteriaDropdown;

    @FindBy(xpath = "//div[@id='filter-container']//input")
    private WebElement filterCriteriaSearchInput;

    @FindBy(xpath = "//fieldset[@id='applyFilter']/button")
    private WebElement applyFilterButton;

    @FindBy(xpath = "//li[@title='0200613']/div/a")
    private WebElement dropdownFilterOptionOne;

    @FindBy(xpath = "//tr[@id='detailRow_0']/td[1]")
    private WebElement firstRowCellOne;

    @FindBy(xpath = "//tr[@id='detailRow_0']/td[2]")
    private WebElement firstRowCellTwo;

    private PageUtils pageUtils;
    private WebDriver driver;

    public CreateAdHocViewDesignerPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(designerTitle);
        pageUtils.waitForElementToAppear(firstFieldElement);
        pageUtils.waitForElementToAppear(firstMeasureElement);
    }

    /**
     * Search fields by given parameter
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage searchFields(String searchQuery) {
        pageUtils.waitForElementToAppear(fieldsSearchInput);
        fieldsSearchInput.clear();
        pageUtils.waitForElementAndClick(fieldsSearchInput);
        fieldsSearchInput.sendKeys(searchQuery);
        pageUtils.waitForElementAndClick(fieldsSearchButton);
        pageUtils.waitForElementToAppear(scenarioPartNumberElement);
        return this;
    }

    /**
     * Select Part Number field option for columns
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage selectPartNumberFieldOptionForRows() {
        Actions actions = new Actions(driver);
        actions.contextClick(scenarioPartNumberElement)
                .perform();
        pageUtils.waitForElementAndClick(addToRowsRightClickOption);
        pageUtils.waitForElementToAppear(scenarioPartNumberInRows);
        return this;
    }

    /**
     * Search measures by given parameter
     * @param searchQuery
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage searchMeasuresFields(String searchQuery) {
        pageUtils.waitForElementToAppear(measuresSearchInput);
        measuresSearchInput.clear();
        pageUtils.waitForElementAndClick(measuresSearchInput);
        measuresSearchInput.sendKeys(searchQuery);
        pageUtils.waitForElementAndClick(measuresSearchButton);
        pageUtils.waitForElementToAppear(scenarioFullyBurdenedCostElement);
        return this;
    }

    /**
     * Select Scenario FBC measures option for columns
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage selectScenarioFbcFieldOptionForColumns() {
        Actions actions = new Actions(driver);
        actions.contextClick(scenarioFullyBurdenedCostElement)
                .perform();
        pageUtils.waitForElementAndClick(addToColumnsRightClickOption);
        pageUtils.waitForElementToAppear(scenarioFbcInColumns);
        return this;
    }

    /**
     * Opens filter pane
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage openFilterPane() {
        pageUtils.waitForElementAndClick(openFiltersButton);
        pageUtils.waitForElementAndClick(filtersTabTitle);
        return this;
    }

    /**
     * Add Part Number filter
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage addFilterPartNumber() {
        Actions actions = new Actions(driver);
        actions.contextClick(scenarioPartNumberElement)
                .perform();
        pageUtils.waitForElementAndClick(createFilterRightClickOption);
        pageUtils.waitForElementToAppear(filtersTabTitle);
        return this;
    }

    /**
     * Select part number in filter dropdown
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage selectPartNumber() {
        pageUtils.waitForElementAndClick(filterCriteriaDropdown);
        pageUtils.waitForElementAndClick(filterCriteriaSearchInput);;
        pageUtils.waitForElementAndClick(dropdownFilterOptionOne);
        return this;
    }

    /**
     * Clicks Apply button
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public CreateAdHocViewDesignerPage clickApply() {
        pageUtils.waitForElementAndClick(applyFilterButton);
        return this;
    }

    /**
     * Gets value from table
     * @return Instance of CreateAdHocViewDesignerPage
     */
    public String getTableText(boolean getCellOne) {
        WebElement elementToUse = getCellOne ? firstRowCellOne : firstRowCellTwo;
        return elementToUse.getText();
    }
}
