package com.apriori.pageobjects.pages.partsandassemblies;

import com.apriori.pageobjects.common.PartsAndAssemblyTableController;
import com.apriori.utils.PageUtils;
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

    @FindBy(xpath = "//div[@Class='MuiDataGrid-row']")
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

    @FindBy(xpath = "//*[@data-field='scenarioName']")
    private WebElement scenarioNameField;

    @FindBy(xpath = "//*[@data-field='scenarioName']//button[@title='Menu']")
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



    public PartsAndAssembliesPage(WebDriver driver) {

        this(driver, log);
    }

    private PageUtils pageUtils;
    private WebDriver driver;
    private PartsAndAssemblyTableController partsAndAssemblyTableController;

    public PartsAndAssembliesPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
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
        pageUtils.waitForElementToAppear(progressBar);
        pageUtils.waitForElementsToNotAppear(By.xpath("//span[@role='progressbar']"),1);
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
        getPageUtils().mouseMove(scenarioNameField);
        getPageUtils().waitForElementToAppear(tripleDotIcon).click();
        return this;
    }

    /**
     * Checks if pin to left option displayed
     *
     * @return true/false
     */
    public boolean isPinToLeftOptionDisplayed() {
        return pageUtils.isElementDisplayed(btnPintoLeft);
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
        return pageUtils.isElementDisplayed(btnSearch);
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
        return pageUtils.isElementDisplayed(fieldSearch);
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
        return pageUtils.isElementDisplayed(btnPintoLeft);
    }

    /**
     * Checks if show/hide fields option displayed
     *
     * @return true/false
     */
    public boolean isShowHideOptionDisplayed() {
        return pageUtils.isElementDisplayed(showHideFieldsOption);
    }

    /**
     * Checks if show/hide fields modal displayed
     *
     * @return true/false
     */
    public boolean isShowHideModalDisplayed() {
        return pageUtils.isElementDisplayed(showHideModal);
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
}