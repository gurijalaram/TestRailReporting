package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author cfrith
 */

public class FilterCriteriaPage extends LoadableComponent<FilterCriteriaPage> {

    private final Logger logger = LoggerFactory.getLogger(FilterCriteriaPage.class);

    @FindBy(css = "[data-ap-comp='scenarioSearchCriteria'] h3.modal-title")
    private WebElement modalDialog;

    @FindBy(css = "input[data-ap-field='privateWorkspace']")
    private WebElement privateCheckBox;

    @FindBy(css = "input[data-ap-field='publicWorkspace']")
    private WebElement publicCheckBox;

    @FindBy(css = "input[data-ap-field='partScenarioType']")
    private WebElement partCheckBox;

    @FindBy(css = "input[data-ap-field='assemblyScenarioType']")
    private WebElement assemblyCheckBox;

    @FindBy(css = "input[data-ap-field='comparisonScenarioType']")
    private WebElement comparisonCheckBox;

    @FindBy(css = "select[data-ap-field='criteria0.criteriaName']")
    private WebElement rowOneAttributeDropdown;

    @FindBy(css = "select[data-ap-field='criteria0.operation']")
    private WebElement rowOneConditionDropdown;

    @FindBy(css = "select[data-ap-field='criteria1.criteriaName']")
    private WebElement rowTwoAttributeDropdown;

    @FindBy(css = "select[data-ap-field='criteria1.operation']")
    private WebElement rowTwoConditionDropdown;

    @FindBy(css = "select[data-ap-field='criteria2.criteriaName']")
    private WebElement rowThreeAttributeDropdown;

    @FindBy(css = "select[data-ap-field='criteria2.operation']")
    private WebElement rowThreeConditionDropdown;

    @FindBy(xpath = "//input[@data-ap-field='criteria0.value']")
    private WebElement rowOneInput;

    @FindBy(xpath = "//input[@data-ap-field='criteria1.value']")
    private WebElement rowTwoInput;

    @FindBy(xpath = "//input[@data-ap-field='criteria2.value']")
    private WebElement rowThreeInput;

    @FindBy(xpath = "//select[@data-ap-field='criteria0.value']/parent::div//button")
    private WebElement rowOneValueDropdown;

    @FindBy(xpath = "//select[@data-ap-field='criteria1.value']/parent::div//button")
    private WebElement rowTwoValueDropdown;

    @FindBy(xpath = "//select[@data-ap-field='criteria2.value']/parent::div//button")
    private WebElement rowThreeValueDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Initial')]/..")
    private WebElement costMaturityInitialOption;

    @FindBy(xpath = "//span[contains(text(), 'Low')]/..")
    private WebElement costMaturityLowOption;

    @FindBy(xpath = "//span[contains(text(), 'Medium')]/..")
    private WebElement costMaturityMediumOption;

    @FindBy(xpath = "//span[contains(text(), 'High')]/..")
    private WebElement costMaturityHighOption;

    @FindBy(xpath = "//div[@data-ap-comp='scenarioSearchCriteria'] //button[contains(text(),'Apply')]")
    private WebElement applyButton;

    @FindBy(xpath = "//div[@data-ap-comp='scenarioSearchCriteria'] //button[contains(text(),'Cancel')]")
    private WebElement cancelButton;

    @FindBy(css = "div[data-ap-comp='additionalSearchCriteria'] input")
    private List<WebElement> listOfCheckboxes;

    @FindBy(xpath = "//div[@data-ap-comp='scenarioSearchCriteria'] //button[contains(text(),'Clear')]")
    private WebElement clearButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private String attribute;

    public FilterCriteriaPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        clear()
            .clearAllCheckBoxes();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(modalDialog);
        pageUtils.waitForElementToAppear(rowOneAttributeDropdown);
    }

    /**
     * Sets the workspace
     *
     * @param criteriaWorkspace - the workspace
     * @return current page object
     */
    public FilterCriteriaPage setWorkspace(String criteriaWorkspace) {
        String[] workspaces = criteriaWorkspace.split(",");

        Arrays.stream(workspaces).forEach(workspace -> {
            if (workspace.trim().equalsIgnoreCase("Private")) {
                pageUtils.waitForElementAndClick(privateCheckBox);
            }
            if (workspace.trim().equalsIgnoreCase("Public")) {
                pageUtils.waitForElementAndClick(publicCheckBox);
            }
        });
        return this;
    }

    /**
     * Sets the scenario type
     *
     * @param scenarioType - scenario type
     * @return current page object
     */
    public FilterCriteriaPage setScenarioType(String scenarioType) {
        String[] scenarios = scenarioType.split(",");

        Arrays.stream(scenarios).forEach(scenario -> {
            if (scenario.trim().equalsIgnoreCase("Part")) {
                pageUtils.waitForElementAndClick(partCheckBox);
            }
            if (scenario.trim().equalsIgnoreCase("Assembly")) {
                pageUtils.waitForElementAndClick(assemblyCheckBox);
            }
            if (scenario.trim().equalsIgnoreCase("Comparison")) {
                pageUtils.waitForElementAndClick(comparisonCheckBox);
            }
        });
        return this;
    }

    /**
     * Sets fields for the first row
     *
     * @param attribute - the attribute
     * @param condition - the condition
     * @param value     - the value
     * @return current page object
     */
    public FilterCriteriaPage setRowOne(String attribute, String condition, String value) {
        setRow(rowOneAttributeDropdown, rowOneConditionDropdown, 1, attribute, condition, value);
        return this;
    }

    /**
     * Sets fields for the second row
     *
     * @param attribute - the attribute
     * @param condition - the condition
     * @param value     - the value
     * @return current page object
     */
    public FilterCriteriaPage setRowTwo(String attribute, String condition, String value) {
        setRow(rowTwoAttributeDropdown, rowTwoConditionDropdown, 2, attribute, condition, value);
        return this;
    }

    /**
     * Sets fields for the third row
     *
     * @param attribute - the attribute
     * @param condition - the condition
     * @param value     - the value
     * @return current page object
     */
    public FilterCriteriaPage setRowThree(String attribute, String condition, String value) {
        setRow(rowThreeAttributeDropdown, rowThreeConditionDropdown, 3, attribute, condition, value);
        return this;
    }

    private void setRow(WebElement attributeLocator, WebElement conditionLocator, int row, String attribute, String condition, String value) {
        this.attribute = attribute;
        new Select(attributeLocator).selectByVisibleText(attribute);
        new Select(conditionLocator).selectByVisibleText(condition);
        setValueType(row, value);
    }

    /**
     * Choose how data is entered either via input or dropdown based on enum
     *
     * @param values - enum value
     * @return current page object
     */
    private FilterCriteriaPage setValueType(int row, String values) {
        if (Arrays.stream(Attribute.values()).map(Attribute::getAttributeValue).anyMatch(attributeValues -> attributeValues.equalsIgnoreCase(attribute))) {
            setDropdown(row, values);
        } else {
            inputValue(row, values);
        }
        return this;
    }

    /**
     * Selects the value as a dropdown
     *
     * @param values - the input values
     * @return current page object
     */
    private FilterCriteriaPage setDropdown(int row, String values) {
        WebElement value;
        WebElement rowLocator;
        String[] valuesToSelect = values.split(",");

        switch (row) {
            case 1:
                rowLocator = rowOneValueDropdown;
                break;
            case 2:
                rowLocator = rowTwoValueDropdown;
                break;
            case 3:
                rowLocator = rowThreeValueDropdown;
                break;
            default:
                throw new IllegalArgumentException(String.format("Row '%s' doesn't exist", row));
        }

        pageUtils.waitForElementAndClick(rowLocator);

        for (String valueToSelect : valuesToSelect) {
            value = driver.findElement(By.xpath(String.format("//div[contains(@class,'show-tick open')]//span[contains(text(),'%s')]", valueToSelect.trim())));
            pageUtils.waitForElementAndClick(value);
        }
        rowLocator.sendKeys(Keys.ESCAPE);
        return this;
    }

    /**
     * Sets the value as input
     *
     * @param input - the input value
     * @return current page object
     */
    private FilterCriteriaPage inputValue(int row, String input) {
        WebElement rowLocator;

        switch (row) {
            case 1:
                rowLocator = rowOneInput;
                break;
            case 2:
                rowLocator = rowTwoInput;
                break;
            case 3:
                rowLocator = rowThreeInput;
                break;
            default:
                throw new IllegalArgumentException(String.format("Row '%s' doesn't exist", row));
        }

        pageUtils.waitForElementAndClick(rowLocator);
        rowLocator.clear();
        rowLocator.sendKeys(input);
        return this;
    }

    /**
     * Selects the apply button
     *
     * @param className - the class the method should return
     * @param <T>       - the generic declaration type
     * @return generic page object
     */
    public <T> T apply(Class<T> className) {
        pageUtils.waitForElementAndClick(applyButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @param className - the class the method should return
     * @param <T>       - the generic declaration type
     * @return generic page object
     */
    public <T> T cancel(Class<T> className) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the clear button
     *
     * @return generic page object
     */
    public FilterCriteriaPage clear() {
        pageUtils.waitForElementAndClick(clearButton);
        return this;
    }

    /**
     * Clears all listed checkboxes
     *
     * @return current page object
     */
    public FilterCriteriaPage clearAllCheckBoxes() {
        listOfCheckboxes.stream().filter(checkbox -> checkbox.getAttribute("checked") != null).forEach(WebElement::click);
        return this;
    }

    /**
     * Enum to select specific attributes
     */
    protected enum Attribute {
        PROCESSGROUP("Process Group"),
        VPE("VPE"),
        STATUS("Status"),
        LASTSAVED("Last Saved"),
        COSTMATURITY("Cost Maturity"),
        ASSIGNEE("Assignee");

        private final String attributeValue;

        Attribute(String attributeValue) {

            this.attributeValue = attributeValue;
        }

        public String getAttributeValue() {

            return attributeValue;
        }
    }
}