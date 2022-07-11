package com.apriori.pageobjects.pages.partsandassemblies;

import com.apriori.pageobjects.common.PartsAndAssemblyTableController;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
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
public class PartsAndAssembliesPage extends EagerPageComponent<PartsAndAssembliesPage> {

    @FindBy(xpath = "//span[@role='progressbar']")
    private WebElement progressBar;

    @FindBy(xpath = "//div[starts-with(@Class,'MuiDataGrid-cellCheckbox')]")
    private List<WebElement> tableRow;

    @FindBy(xpath = "//div[@class='MuiDataGrid-row Mui-selected']")
    private List<WebElement> selectedCheckboxes;

    @FindBy(xpath = "//p[@data-testid ='toolbar-Show/Hide Fields']")
    private WebElement showHideFieldsOption;

    @FindBy(id = "show-button")
    private WebElement hideAllButton;

    @FindBy(id = "show-hide-field-input")
    private WebElement showHideSearchField;

    @FindBy(css = "div.MuiListItemText-root.css-1tsvksn")
    private WebElement fieldNameResult;

    @FindBy(xpath = "//span[@data-testid='switch']")
    private WebElement toggleButton;

    @FindBy(xpath = "//*[@data-field='scenarioState']")
    private WebElement scenarioStateField;

    @FindBy(xpath = "//*[@data-field='scenarioState']//button[@title='Menu']")
    private WebElement tripleDotIcon;

    @FindBy(xpath = "//*[@data-testid='menu-item-pin']")
    private WebElement btnPintoLeft;

    @FindBy(xpath = "//p[@data-testid ='toolbar-Search']")
    private WebElement btnSearch;

    @FindBy(xpath = "//div[@data-testid ='search-control-input']")
    private WebElement fieldSearch;

    @FindBy(xpath = "//input[@class='MuiOutlinedInput-input MuiInputBase-input MuiInputBase-inputAdornedEnd css-1uvydh2']")
    private WebElement searchInputField;

    @FindBy(xpath = "//div[starts-with(@Class,'MuiOutlinedInput-root MuiInputBase-root')]//button//*[@data-icon='times-circle']")
    private WebElement btnClear;

    @FindBy(xpath = "//*[@data-testid='menu-item-pin']")
    private WebElement btnUnpin;

    @FindBy(xpath = "//span[.='Unpin']")
    private WebElement btnPUnpin;

    @FindBy(id = "simple-popper")
    private WebElement showHideModal;

    @FindBy(id = "hide-button")
    private WebElement btnShowAll;

    @FindBy(xpath = "//*[@data-testid='title']")
    private WebElement headerText;

    @FindBy(xpath = "//div[@role='grid']")
    private WebElement partsAndAssembliesTable;

    @FindBy(css = "div.MuiDataGrid-columnHeaderTitleContainerContent .MuiCheckbox-root")
    private WebElement checkAllCheckBox;

    @FindBy(xpath = "//p[starts-with(@data-testid,'toolbar-Filter ')]")
    private WebElement btnFilter;

    @FindBy(id = "popover-filter-control")
    private WebElement filterModal;

    @FindBy(id = "add-condition-button-filter-control")
    private WebElement addCondition;

    @FindBy(xpath = "//div[contains(@data-testid,'select-control-filter-field-select')]")
    private WebElement filterField;

    @FindBy(xpath = "//div[contains(@data-testid,'select-control-filter-condition-type')]")
    private WebElement filterType;

    @FindBy(xpath = "//div[contains(@data-testid,'filter-value')]")
    private WebElement filterValue;

    @FindBy(xpath = "//*[@data-icon='times-circle']")
    private WebElement removeIcon;

    @FindBy(xpath = "//li[@data-value='componentName']")
    private WebElement componentNameFiled;

    @FindBy(xpath = "//div[contains(@data-testid,'select-control-filter-field-select')]//input")
    private WebElement selectedFiled;

    @FindBy(xpath = "//div[contains(@data-testid,'filter-value')]//input")
    private WebElement filteredValue;

    @FindBy(xpath = "//*[@data-field='costingInput.processGroupName']")
    private WebElement processGroupField;

    @FindBy(xpath = "//*[@data-field='costingInput.processGroupName']//button[@title='Menu']")
    private WebElement processGroupKebabMenu;

    @FindBy(xpath = "//div[@data-testid='list-subitem-text-left-menu.subTitle.dashboard']")
    private WebElement btnDashboard;

    @FindBy(xpath = "//*[@data-field='scenarioCreatedBy']")
    private WebElement createdByField;

    @FindBy(xpath = "//*[@data-field='scenarioState']//button//*[local-name()='svg']")
    private WebElement scenarioStateIcon;

    public PartsAndAssembliesPage(WebDriver driver) {

        this(driver, log);
    }

    private WebDriver driver;
    private PartsAndAssemblyTableController partsAndAssemblyTableController;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;

    public PartsAndAssembliesPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.partsAndAssemblyTableController = new PartsAndAssemblyTableController(driver);
        PageFactory.initElements(driver, this);
        this.waitForTableLoad();

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public  List<String> getTableHeaders() {
        return partsAndAssemblyTableController.getTableHeaders();
    }

    /**
     * Method to wait until loading complete
     */
    public void waitForTableLoad() {
        getPageUtils().waitForElementToAppear(progressBar);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//span[@role='progressbar']"),1);
    }

    /**
     * Get the checkboxes status
     *
     * @return String
     */
    public String getComponentCheckBoxStatus() {
        if (tableRow.size() > 0) {
            for (int i = 0;i <= tableRow.size();i++) {
                getPageUtils().waitForElementToAppear(driver.findElement(By.xpath("//div[@data-rowindex='" + i + "']//span[starts-with(@class,'MuiCheckbox-root')]"))).click();
            }
        }
        return getPageUtils().waitForElementsToAppear(selectedCheckboxes).get(tableRow.size()).getAttribute("aria-selected");
    }

    /**
     * Click on Show/hide fields option
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickOnShowHideOption() {
        getPageUtils().waitForElementAndClick(showHideFieldsOption);
        return this;
    }

    /**
     * Click on HideAll option
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickOnHideAllButton() {
        getPageUtils().waitForElementAndClick(hideAllButton);
        return this;
    }

    /**
     * Input a field name to find a field
     *
     * @return current page object
     */
    public PartsAndAssembliesPage enterFieldName(String fieldName) {
        getPageUtils().waitForElementToAppear(showHideSearchField).sendKeys(fieldName);
        return this;
    }

    /**
     * Get searched field name
     *
     * @return current page object
     */
    public String getFieldName() {
        return getPageUtils().waitForElementToAppear(fieldNameResult).getText();
    }

    /**
     * Click on toggle button
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickOnToggleButton() {
        getPageUtils().waitForElementAndClick(toggleButton);
        return this;
    }

    /**
     * Click on dot icon on table header
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickKebabMenuOnTableHeader() {
        getPageUtils().mouseMove(scenarioStateField);
        getPageUtils().waitForElementToAppear(tripleDotIcon).click();
        return this;
    }

    /**
     * Checks if pin to left option displayed
     *
     * @return true/false
     */
    public boolean isPinToLeftOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnPintoLeft);
    }

    /**
     * Click on pin to left option
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickPinToLeft() {
        getPageUtils().waitForElementToAppear(btnPintoLeft).click();
        return this;
    }

    /**
     * Gets pinned table headers
     *
     * @return list of string
     */
    public  List<String> getPinnedTableHeaders() {
        return partsAndAssemblyTableController.getPinnedTableHeaders();
    }

    /**
     * Checks if search option displayed
     *
     * @return true/false
     */
    public boolean isSearchOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnSearch);
    }

    /**
     * Click on search
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickSearchOption() {
        getPageUtils().waitForElementAndClick(btnSearch);
        return this;
    }

    /**
     * Checks if search field displayed
     *
     * @return true/false
     */
    public boolean isSearchFieldDisplayed() {
        return getPageUtils().isElementDisplayed(fieldSearch);
    }

    /**
     * Click on search box
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickOnSearchField() {
        getPageUtils().waitForElementAndClick(fieldSearch);
        return this;
    }

    /**
     * Enter component name on search field
     *
     * @return current page object
     */
    public PartsAndAssembliesPage enterAComponentName(String componentName) {
        getPageUtils().waitForElementToAppear(searchInputField).sendKeys(componentName);
        return this;
    }

    /**
     * Get the added component Name
     *
     * @return a String
     */
    public String getAddedComponentName() {
        return getPageUtils().waitForElementToAppear(searchInputField).getAttribute("value");
    }


    /**
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return partsAndAssemblyTableController.getListOfScenarios(componentName, scenarioName);
    }

    /**
     * Click on clear icon on search field
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickClearOption() {
        getPageUtils().waitForElementAndClick(btnClear);
        return this;
    }

    /**
     * Gets the number of elements present on the page after clear search
     *
     * @return size of the element as int
     */
    public int getListOfComponents() {
        return tableRow.size();
    }

    /**
     * Click on unpin option
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickOnUnpinOption() {
        getPageUtils().waitForElementAndClick(btnPintoLeft);
        return this;
    }

    /**
     * Checks if unpin option displayed
     *
     * @return true/false
     */
    public boolean isUnPinOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnPintoLeft);
    }

    /**
     * Checks if show/hide fields option displayed
     *
     * @return true/false
     */
    public boolean isShowHideOptionDisplayed() {
        return getPageUtils().isElementDisplayed(showHideFieldsOption);
    }

    /**
     * Checks if show/hide fields modal displayed
     *
     * @return true/false
     */
    public boolean isShowHideModalDisplayed() {
        return getPageUtils().isElementDisplayed(showHideModal);
    }

    /**
     * Click on show All option
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickOnShowAllOption() {
        getPageUtils().waitForElementAndClick(btnShowAll);
        return this;
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
     * Checks if parts and assemblies table displayed
     *
     * @return true/false
     */
    public boolean isPartAndAssembliesTableDisplayed() {
        return getPageUtils().isElementDisplayed(partsAndAssembliesTable);
    }

    /**
     * Get the status of check all
     *
     * @return a String
     */
    public String getCheckAllStatus() {
        return getPageUtils().waitForElementToAppear(checkAllCheckBox).getAttribute("class");
    }

    /**
     * Click on check All
     *
     * @return current page object
     */

    public PartsAndAssembliesPage clickCheckAll() {
        if (tableRow.size() > 0) {
            getPageUtils().waitForElementAndClick(checkAllCheckBox);
        }
        return this;
    }

    /**
     * Click the filter option
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickFilter() {
        getPageUtils().waitForElementAndClick(btnFilter);
        return this;
    }

    /**
     * Checks if filter modal displayed
     *
     * @return true/false
     */
    public boolean isFilterModalDisplayed() {
        return getPageUtils().isElementDisplayed(filterModal);
    }

    /**
     * Click the add condition
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickAddCondition() {
        getPageUtils().waitForElementAndClick(addCondition);
        return this;
    }

    /**
     * Checks if filter field displayed
     *
     * @return true/false
     */
    public boolean isFilterFieldDisplayed() {
        return getPageUtils().isElementDisplayed(filterField);

    }

    /**
     * Checks if filter type displayed
     *
     * @return true/false
     */
    public boolean isFilterTypeDisplayed() {
        return getPageUtils().isElementDisplayed(filterType);

    }

    /**
     * Checks if filter value displayed
     *
     * @return true/false
     */
    public boolean isFilterValueDisplayed() {
        return getPageUtils().isElementDisplayed(filterValue);

    }

    /**
     * Checks if filter remove icon displayed
     *
     * @return true/false
     */
    public boolean isFilterClearIconDisplayed() {
        return getPageUtils().isElementDisplayed(removeIcon);

    }

    /**
     * Select a component name
     *
     * @return a string
     */
    public String selectComponentNameField() {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(componentNameFiled);
        return selectedFiled.getAttribute("value");
    }

    /**
     * Type a filter value
     *
     * @return current page object
     */
    public PartsAndAssembliesPage addFilterValue(String value) {
        getPageUtils().waitForElementAndClick(filteredValue);
        filteredValue.sendKeys(value);
        return new PartsAndAssembliesPage(getDriver());
    }

    /**
     * Get the added component Name
     *
     * @return a String
     */
    public String getFilteredComponentName() {
        return getPageUtils().waitForElementToAppear(filteredValue).getAttribute("value");
    }

    /**
     * Click on remove icon
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickRemoveCondition() {
        getPageUtils().waitForElementAndClick(removeIcon);
        return new PartsAndAssembliesPage(getDriver());
    }

    /**
     * Get the added sorting rule
     *
     * @return a String
     */
    public String getSortingRule() {
        getPageUtils().waitForElementAndClick(scenarioStateField);
        return getPageUtils().waitForElementToAppear(scenarioStateField).getAttribute("aria-sort");
    }

    /**
     * Click on pin to left on process group
     *
     * @return current page object
     */
    public PartsAndAssembliesPage pinToLeftProcessGroupColumn() {
        getPageUtils().mouseMove(processGroupField);
        getPageUtils().waitForElementAndClick(processGroupKebabMenu);
        getPageUtils().waitForElementAndClick(btnPintoLeft);
        return this;
    }

    /**
     * Navigate to dashboard page
     *
     * @return new page object
     */
    public LeftHandNavigationBar clickDashBoard() {
        getPageUtils().waitForElementAndClick(btnDashboard);
        return new LeftHandNavigationBar(getDriver());
    }

    /**
     * Click on selected scenarioName
     *
     * @return new page object
     */
    public PartsAndAssembliesDetailsPage clickOnScenarioName(String scenarioName) {
        getPageUtils().waitForElementAndClick(driver.findElement(By.xpath("//div[@data-field='scenarioName']//p[text()='" + scenarioName + "']")));
        return new PartsAndAssembliesDetailsPage(getDriver());
    }

    /**
     * sort digital factory field
     *
     * @return new page object
     */
    public PartsAndAssembliesPage sortCreatedByField() {
        getPageUtils().waitForElementAndClick(createdByField);
        return this;
    }

    /**
     * Get the sorting rule
     *
     * @return a String
     */
    public String getCreatedBySortingRule() {
        return getPageUtils().waitForElementToAppear(createdByField).getAttribute("aria-sort");
    }

    /**
     * Get sorting status
     *
     * @return a String
     */
    public String getSortingStatus() {
        return getPageUtils().waitForElementToAppear(scenarioStateIcon).getAttribute("data-icon");
    }
}