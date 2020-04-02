package com.apriori.pageobjects.pages.explore;

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

    @FindBy(css = "h3.modal-title")
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

    @FindBy(css = "select[data-ap-field='criteria1.criteriaName']")
    private WebElement rowTwoAttributeDropdown;

    @FindBy(css = "select[data-ap-field='criteria0.operation']")
    private WebElement conditionDropdown;

    @FindBy(css = "input[data-ap-field='criteria0.value']")
    private WebElement valueInputOne;

    @FindBy(css = "input[data-ap-field='criteria1.value']")
    private WebElement valueInputTwo;

    @FindBy(css = "button.btn.dropdown-toggle.selectpicker.btn-default")
    private WebElement valueInputDropdown;

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
    private int numRows;

    public FilterCriteriaPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
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
     * Filter criteria for private selection
     *
     * @param type      - type of selection whether private or public
     * @param attribute - the attribute
     * @param condition - specified condition
     * @param value     - the value
     * @return current page object
     */
    public FilterCriteriaPage filterPrivateCriteria(String type, String attribute, String condition, String value) {
        clear(FilterCriteriaPage.class)
            .setPrivateWorkSpace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value);
        return this;
    }

    /**
     * Filter criteria for public selection
     * @param type      - type of selection whether private or public
     * @param attribute - the attribute
     * @param condition - specified condition
     * @param value     - the value
     * @return current page object
     */
    public FilterCriteriaPage filterPublicCriteria(String type, String attribute, String condition, String value) {
        clear(FilterCriteriaPage.class)
            .setPublicWorkspace()
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value);
        return this;
    }

    /**
     * Multi filter criteria for public selection
     * @return current page object
     */
    public FilterCriteriaPage multiFilterPublicCriteria(String[] type, String[] attribute, String[] condition, String[] value) {
        //setPublicWorkspace();

        //setScenarioType(type[0]);
        multiSelectAttribute(attribute[0]);
        selectCondition(condition[0]);
        multiSelectValue(value[0]);
        //setTypeOfValue(value[0]);

        return this;
    }

    /**
     * Clears all listed checkboxes
     *
     * @return current page object
     */
    private FilterCriteriaPage clearAllCheckBoxes() {
        listOfCheckboxes.stream().filter(checkbox -> checkbox.getAttribute("checked") != null).forEach(WebElement::click);
        return this;
    }

    /**
     * Sets the scenario type
     * @param type - scenario type
     * @return current page object
     */
    protected FilterCriteriaPage setScenarioType(String type) {
        selectScenarioType(type);
        return this;
    }

    /**
     * Sets the scenario type
     * @param type - scenario type
     * @return current page object
     */
    protected FilterCriteriaPage multiSelectScenarioType(String type, int row) {
        if (row == 1) {
            selectScenarioType(type);
        } else if (row == 2) {
            selectScenarioType(type);
        } else {
            return this;
        }
        return this;
    }

    /**
     * Selects the checkbox
     *
     * @return current page object
     */
    private FilterCriteriaPage setPrivateWorkSpace() {
        privateCheckBox.click();
        return this;
    }

    /**
     * Selects the checkbox
     *
     * @return current page object
     */
    private FilterCriteriaPage setPublicWorkspace() {
        publicCheckBox.click();
        return this;
    }

    /**
     * Selects the attribute
     * @param attribute - the attribute
     * @return current page object
     */
    private FilterCriteriaPage selectAttribute(String attribute) {
        return setAttribute(attribute, false);
    }

    private FilterCriteriaPage multiSelectAttribute(String attribute) {
        return setAttribute(attribute, true);
    }

    /**
     * Selects the condition
     *
     * @param condition - the condition
     * @return current page object
     */
    private FilterCriteriaPage selectCondition(String condition) {
        new Select(conditionDropdown).selectByVisibleText(condition);
        return this;
    }

    /**
     * Sets the value as input
     *
     * @param input - the input value
     * @return current page object
     */
    private FilterCriteriaPage inputValue(String input) {
        valueInputOne.click();
        pageUtils.clearInput(valueInputOne);
        valueInputOne.sendKeys(input);
        return this;
    }

    /**
     * Selects the value as a dropdown
     * @param input - the input value
     * @return current page object
     */
    private FilterCriteriaPage selectValue(String input) {
        valueInputDropdown.click();
        WebElement value = driver.findElement(By.xpath(String.format("//div[contains(@class,'show-tick open')]//span[contains(text(),'%s')]", input)));
        value.click();
        valueInputDropdown.sendKeys(Keys.ESCAPE);
        return this;
    }

    /**
     * Multi selects value of matcher
     * @param value - value to select in dropdown
     * @return current page object
     */
    private FilterCriteriaPage multiSelectValue(String value) {
        valueInputTwo.click();
        valueInputTwo.sendKeys(value);
        valueInputTwo.sendKeys(Keys.ESCAPE);
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
     * @param className - the class the method should return
     * @param <T>       - the generic declaration type
     * @return generic page object
     */
    public <T> T clear(Class<T> className) {
        pageUtils.waitForElementAndClick(clearButton);
        return PageFactory.initElements(driver, className);
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

        private String attributeValue;

        Attribute(String attributeValue) {

            this.attributeValue = attributeValue;
        }

        public String getAttributeValue() {

            return attributeValue;
        }
    }

    /**
     * Sets attribute for multi filter
     * @param attribute - attribute to set
     * @return current page object
     */
    private FilterCriteriaPage setAttribute(String attribute, boolean isMultiSelect) {
        if (!isMultiSelect) {
            new Select(rowOneAttributeDropdown).selectByVisibleText(attribute);
        } else {
            multiSelectAttribute(attribute, 2);
        }
        return this;
    }

    /**
     * Multi select for attribute
     * @param attribute - attribute to select
     * @param numRows - number of rows to use
     */
    private void multiSelectAttribute(String attribute, int numRows) {
        this.numRows = numRows;
        switch (numRows) {
            case 2:
                attributeSelectionAction(rowOneAttributeDropdown, attribute);
                attributeSelectionAction(rowTwoAttributeDropdown, attribute);
                break;
            default:
                break;
        }
    }

    /**
     * Actually selects the attribute
     * @param dropdownToUse - WebElement to use
     * @param attribute - attribute to select
     */
    private void attributeSelectionAction(WebElement dropdownToUse, String attribute) {
        new Select(dropdownToUse).selectByVisibleText(attribute);
    }

    /**
     * Selects scenario type for filter popup
     * @param type - scenario name
     */
    private void selectScenarioType(String type) {
        switch (type) {
            case "Part":
                pageUtils.waitForElementAndClick(partCheckBox);
                break;
            case "Assembly":
                pageUtils.waitForElementAndClick(assemblyCheckBox);
                break;
            case "Comparison":
                pageUtils.waitForElementAndClick(comparisonCheckBox);
                break;
            default:
                throw new IllegalArgumentException("The type: " + type + " is not found");
        }
    }

    /**
     * Choose how data is entered either via input or dropdown based on enum
     *
     * @param value - enum value
     * @return current page object
     */
    private FilterCriteriaPage setTypeOfValue(String value) {
        if (Arrays.stream(Attribute.values()).map(Attribute::getAttributeValue).anyMatch(values -> values.equalsIgnoreCase(attribute))) {
            selectValue(value);
        } else {
            inputValue(value);
        }
        return this;
    }

    /**
     * Multi set type of value
     * @param value - value to set
     * @return current page object
     */
    private FilterCriteriaPage multiSetTypeOfValue(String value) {
        switch (numRows) {
            case 2:
                attributeSelectionAction(rowOneAttributeDropdown, attribute);
                attributeSelectionAction(rowTwoAttributeDropdown, attribute);
                break;
            default:
                break;
        }
        return this;
    }
}