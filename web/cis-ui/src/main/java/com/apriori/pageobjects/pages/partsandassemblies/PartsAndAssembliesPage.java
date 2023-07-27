package com.apriori.pageobjects.pages.partsandassemblies;

import com.apriori.EagerPageComponent;
import com.apriori.pageobjects.common.PartsAndAssemblyFilterController;
import com.apriori.pageobjects.common.PartsAndAssemblyTableController;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;

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

    @FindBy(xpath = "//div[@role='grid']//span[@role='progressbar']")
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

    @FindBy(xpath = "//*[@data-field='componentType']//button[@title='Menu']//*[local-name()='svg']")
    private WebElement tripleDotIcon;

    @FindBy(xpath = "//*[@data-testid='menu-item-pin']")
    private WebElement btnPintoLeft;

    @FindBy(xpath = "//p[@data-testid ='toolbar-Search']")
    private WebElement btnSearch;

    @FindBy(xpath = "//div[@data-testid ='search-control-input']")
    private WebElement fieldSearch;

    @FindBy(xpath = "//div[@data-testid='search-control-input']//input")
    private WebElement searchInputField;

    @FindBy(xpath = "//div[@data-testid='search-control-popper']//button//*[local-name()='svg']")
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

    @FindBy(xpath = "//*[@data-field='costingInput.processGroupName']//button[@title='Menu']//*[local-name()='svg']")
    private WebElement processGroupKebabMenu;

    @FindBy(xpath = "//div[@data-testid='list-subitem-left-menu.subTitle.messages']")
    private WebElement btnMessages;

    @FindBy(xpath = "//*[@data-field='scenarioCreatedBy']")
    private WebElement createdByField;

    @FindBy(xpath = "//*[@data-field='scenarioState']//button//*[local-name()='svg']")
    private WebElement scenarioStateIcon;

    @FindBy(xpath = "//div[contains(@data-testid,'filter-condition-type')]")
    private WebElement filterCondition;

    @FindBy(xpath = "//*[@data-field='scenarioCreatedBy']//button//*[local-name()='svg']")
    private WebElement createdByIcon;

    @FindBy(xpath = "//div[@class='MuiDataGrid-row']")
    private WebElement filterRecords;

    @FindBy(xpath = "//div[@class='MuiDataGrid-row']")
    private WebElement tableRecords;

    @FindBy(xpath = "//*[@data-field='costingInput.vpeName']")
    private WebElement digitalFactoryField;

    @FindBy(xpath = "//*[@data-field='costingInput.vpeName']//button//*[local-name()='svg']")
    private WebElement digitalFactoryIcon;

    @FindBy(xpath = "//*[@data-field='costingInput.processGroupName']//div[contains(@class,'columnHeaderDraggableContainer')]")
    private WebElement processGroupFieldDrag;

    @FindBy(xpath = "//*[@data-field='componentType']")
    private WebElement componentTypeField;

    @FindBy(xpath = "//li[@data-value='scenarioName']")
    private WebElement scenarioNameFiled;

    @FindBy(xpath = "//*[@data-field='scenarioCreatedAt']")
    private WebElement createdAtField;

    @FindBy(xpath = "//*[@data-field='scenarioCreatedAt']//button//*[local-name()='svg']")
    private WebElement createdAtSortIcon;

    @FindBy(xpath = "//header[@data-testid='app-bar']//button[contains(@class,'MuiButton-text')]")
    private WebElement linkBackToPartNAssemblyPage;

    @FindBy(xpath = "//div[@data-testid='scenario-discussion-no-data-No comments available']")
    private WebElement emptyCommentMessage;

    @FindBy(xpath = "//span[contains(@class,'Switch-checked')]")
    private WebElement btnCheckedStatus;

    @FindBy(xpath = "//span[@data-testid='switch']//span")
    private WebElement statusField;

    public PartsAndAssembliesPage(WebDriver driver) {

        this(driver, log);
    }

    private WebDriver driver;
    private PartsAndAssemblyTableController partsAndAssemblyTableController;
    private PartsAndAssemblyFilterController partsAndAssemblyFilterController;

    public PartsAndAssembliesPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.partsAndAssemblyTableController = new PartsAndAssemblyTableController(driver);
        this.partsAndAssemblyFilterController = new PartsAndAssemblyFilterController(driver);
        PageFactory.initElements(driver, this);
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
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@role='grid']//span[@role='progressbar']"),1);
    }

    /**
     * Get the checkboxes status
     *
     * @return String
     */
    public String getComponentCheckBoxStatus() {
        if (getPageUtils().waitForElementsToAppear(tableRow).size() > 0) {
            for (int i = 0;i < tableRow.size();i++) {
                getPageUtils().waitForElementToAppear(driver.findElement(By.xpath("//div[@data-rowindex='" + i + "']//span[contains(@class,'MuiCheckbox-root')]"))).click();
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
        getPageUtils().waitForElementToAppear(tableRecords);
        getPageUtils().waitForElementToAppear(componentTypeField);
        getPageUtils().mouseMove(componentTypeField);
        getPageUtils().waitForElementAndClick(tripleDotIcon);
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
        return getPageUtils().waitForElementAppear(btnSearch).isDisplayed();
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
        getPageUtils().waitForElementToAppear(filterRecords);
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
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@class='MuiDataGrid-row MuiDataGrid-row--lastVisible']"));
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
        getPageUtils().waitForElementToAppear(btnPintoLeft).click();
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
        return getPageUtils().waitForElementAppear(showHideFieldsOption).isDisplayed();
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
        if (getPageUtils().waitForElementsToAppear(tableRow).size() > 0) {
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
    public PartsAndAssembliesPage addFilterValue(String componentName, String scenarioName) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(scenarioNameFiled);
        getPageUtils().waitForElementAndClick(filteredValue);
        filteredValue.sendKeys(scenarioName);
        getPageUtils().waitForElementToAppear(By.xpath("//div[@data-field='scenarioName']//p[text()='" + scenarioName + "']/ancestor::div[@role='row']//div[@data-field='componentName']//p[text()='" + componentName + "']"));
        getPageUtils().waitForElementToAppear(By.xpath("//div[contains(@class,'MuiDataGrid-row--lastVisible')]"));
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
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[contains(@class,'MuiDataGrid-row--lastVisible')]"));
        getPageUtils().waitForElementsToAppear(tableRow);
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
        getPageUtils().waitForElementsToAppear(tableRow);
        while (getPageUtils().waitForElementToAppear(processGroupFieldDrag).getAttribute("draggable").equals("true")) {
            getPageUtils().waitForElementToAppear(processGroupField);
            getPageUtils().mouseMove(processGroupField);
            getPageUtils().waitForElementToAppear(processGroupKebabMenu);
            getPageUtils().moveAndClick(processGroupKebabMenu);
            getPageUtils().waitForElementToAppear(btnPintoLeft);
            getPageUtils().waitForElementAndClick(btnPintoLeft);
        }
        return this;
    }

    /**
     * Navigate to message page
     *
     * @return new page object
     */
    public LeftHandNavigationBar clickMessages() {
        getPageUtils().waitForElementAndClick(btnMessages);
        return new LeftHandNavigationBar(getDriver());
    }

    /**
     * Click on selected scenarioName
     *
     * @return new page object
     */
    public PartsAndAssembliesDetailsPage clickOnComponentName(String componentName) {
        getPageUtils().waitForElementAndClick(driver.findElement(By.xpath("//div[@data-field='componentName']//p[text()='" + componentName + "']")));
        return new PartsAndAssembliesDetailsPage(getDriver());
    }

    /**
     * sort digital factory field
     *
     * @return new page object
     */
    public PartsAndAssembliesPage sortDownDigitalFactoryField() {
        getPageUtils().waitForElementToAppear(digitalFactoryField);
        getPageUtils().mouseMove(digitalFactoryField);
        while (getPageUtils().waitForElementToAppear(digitalFactoryIcon).getAttribute("data-icon").equals("sort-down")) {
            getPageUtils().waitForElementAndClick(digitalFactoryField);
            getPageUtils().waitForElementsToAppear(tableRow);
        }
        return this;
    }

    /**
     * Get the sorting rule
     *
     * @return a String
     */
    public String getDigitalFactorySortingRule() {
        getPageUtils().mouseMove(digitalFactoryIcon);
        getPageUtils().waitForElementToAppear(digitalFactoryIcon);
        return getPageUtils().waitForElementToAppear(digitalFactoryIcon).getAttribute("data-icon");
    }

    /**
     * Get sorting status
     *
     * @return a String
     */
    public String getSortingStatus() {
        return getPageUtils().waitForElementToAppear(scenarioStateIcon).getAttribute("data-icon");
    }

    /**
     * Click the filter option
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickFilterOption() {
        getPageUtils().waitForElementToAppear(btnFilter);
        getPageUtils().moveAndClick(btnFilter);
        return this;
    }

    /**
     * Select a filter field
     *
     * @return new page object
     */
    public PartsAndAssembliesPage selectFilterField(String fieldName) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(By.xpath("//li[contains(@class,'MuiMenuItem-root')]//span[text()='" + fieldName + "']"));
        getPageUtils().waitForElementAndClick(filterCondition);
        return this;
    }

    /**
     * Gets operation list
     *
     * @return list of string
     */
    public List<String> getOperationList() {
        return partsAndAssemblyFilterController.getOperationList();
    }

    /**
     * remove filter modal
     *
     * @return current page object
     */
    public PartsAndAssembliesPage removeFilterModal() {
        getPageUtils().moveAndClick(removeIcon);
        getPageUtils().moveAndClick(btnFilter);
        return this;
    }

    /**
     * Method to wait until loading complete after filter
     */
    public PartsAndAssembliesPage waitForTableResults() {
        getPageUtils().waitForElementToAppear(filterRecords);
        return this;
    }

    /**
     * sort digital factory field
     *
     * @return new page object
     */
    public PartsAndAssembliesPage sortUpDigitalFactoryField() {
        getPageUtils().waitForElementToAppear(digitalFactoryField);
        getPageUtils().mouseMove(digitalFactoryField);
        while (getPageUtils().waitForElementToAppear(digitalFactoryIcon).getAttribute("data-icon").equals("sort-up")) {
            getPageUtils().waitForElementAndClick(digitalFactoryField);
            getPageUtils().waitForElementsToAppear(tableRow);
        }
        return this;
    }

    /**
     * Click on pin to right on process group
     *
     * @return current page object
     */
    public PartsAndAssembliesPage pinToRightProcessGroupColumn() {
        while (processGroupFieldDrag.getAttribute("draggable").equals("false")) {
            getPageUtils().waitForElementToAppear(processGroupField);
            getPageUtils().mouseMove(processGroupField);
            getPageUtils().waitForElementToAppear(processGroupKebabMenu);
            getPageUtils().moveAndClick(processGroupKebabMenu);
            getPageUtils().waitForElementAndClick(btnPintoLeft);
        }
        return this;
    }

    /**
     * Click on selected componentName
     *
     * @return new page object
     */
    public PartsAndAssembliesDetailsPage clickOnComponent(String componentName, String scenarioName) {
        getPageUtils().waitForElementAndClick(driver.findElement(By.xpath(String.format("//div[@data-field='scenarioName']//p[text()='%s']/ancestor::div[@role='row']//div[@data-field='componentName']//p[text()='%s']", scenarioName.trim(), componentName.trim()))));
        return new PartsAndAssembliesDetailsPage(getDriver());
    }

    /**
     * sort Created At field
     *
     * @return current page object
     */
    public PartsAndAssembliesPage sortDownCreatedAtField() {
        getPageUtils().waitForElementToAppear(createdAtField);
        getPageUtils().mouseMove(createdAtField);
        while (getPageUtils().waitForElementToAppear(createdAtSortIcon).getAttribute("data-icon").equals("sort-up")) {
            getPageUtils().waitForElementAndClick(createdAtField);
            getPageUtils().waitForElementsToAppear(tableRow);
        }
        return this;
    }

    /**
     * Click on remove icon
     *
     * @return current page object
     */
    public PartsAndAssembliesPage clickToRemoveAddedFilter() {
        getPageUtils().waitForElementAndClick(removeIcon);
        getPageUtils().waitForElementsToAppear(tableRow);
        return this;
    }

    /**
     * hide filter modal
     *
     * @return current page object
     */
    public PartsAndAssembliesPage hideFilterModal() {
        getPageUtils().waitForElementAndClick(btnFilter);
        getPageUtils().waitForElementsToNotAppear(By.id("popover-filter-control"),1);
        return this;
    }

    /**
     * check components
     *
     * @return current page object
     */

    public PartsAndAssembliesPage checkFilteredComponents() {
        if (getPageUtils().waitForElementsToAppear(tableRow).size() > 0) {
            getPageUtils().waitForElementAndClick(checkAllCheckBox);
            getPageUtils().waitForElementToAppear(By.xpath("//input[@aria-label='Unselect row']"));
        }
        return this;
    }

    /**
     * open selected component
     *
     * @return current page object
     */
    public PartsAndAssembliesPage openSelectedComponent(String componentName) {
        getPageUtils().waitForElementAndClick(driver.findElement(By.xpath("//div[@data-field='componentName']//p[text()='" + componentName + "']")));
        getPageUtils().waitForElementToAppear(emptyCommentMessage);
        getPageUtils().waitForElementAndClick(linkBackToPartNAssemblyPage);
        return this;
    }

    /**
     * disable state field
     *
     * @return current page object
     */
    public PartsAndAssembliesPage disableStateField() {
        if (getPageUtils().waitForElementToAppear(statusField).getAttribute("class").contains("Switch-checked")) {
            getPageUtils().waitForElementToAppear(toggleButton);
            getPageUtils().moveAndClick(toggleButton);
            getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-field='scenarioState']"),3);
        } else {
            getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-field='scenarioState']"),3);
        }
        return this;
    }

    /**
     * enable state field
     *
     * @return current page object
     */
    public PartsAndAssembliesPage enableStateField() {
        getPageUtils().waitForElementToAppear(toggleButton);
        getPageUtils().moveAndClick(toggleButton);
        getPageUtils().waitForElementToAppear(btnCheckedStatus);
        return this;
    }
}