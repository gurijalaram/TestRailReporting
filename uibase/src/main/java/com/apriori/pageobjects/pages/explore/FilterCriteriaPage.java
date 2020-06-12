package com.apriori.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.WorkspaceEnum;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cfrith
 */

public class FilterCriteriaPage extends LoadableComponent<FilterCriteriaPage> {

    private final Logger logger = LoggerFactory.getLogger(FilterCriteriaPage.class);
    private Map<String, WebElement> costMaturityOptions = new HashMap<>();

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
        initialiseCostMaturityMap();
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
        clear()
            .setWorkspace(WorkspaceEnum.PRIVATE.getWorkspace())
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value);
        return this;
    }

    /**
     * Filter criteria for public selection
     *
     * @param type      - type of selection whether private or public
     * @param attribute - the attribute
     * @param condition - specified condition
     * @param value     - the value
     * @return current page object
     */
    public FilterCriteriaPage filterPublicCriteria(String type, String attribute, String condition, String value) {
        clear()
            .setWorkspace(WorkspaceEnum.PUBLIC.getWorkspace())
            .setScenarioType(type)
            .selectAttribute(attribute)
            .selectCondition(condition)
            .setTypeOfValue(value);
        return this;
    }

    /**
     * Multi filter criteria for public selection
     *
     * @return current page object
     */
    public FilterCriteriaPage multiFilterPublicCriteria(String scenarioType, String[] attributes, String[] values, Boolean[] dropdownFlags) {
        clear()
            .setWorkspace(WorkspaceEnum.PUBLIC.getWorkspace())
            .setScenarioType(scenarioType)
            .multiSelectAttributes(attributes)
            .multiSelectValue(values, dropdownFlags);
        return this;
    }

    /**
     * Sets the scenario type
     *
     * @param type - scenario type
     * @return current page object
     */
    protected FilterCriteriaPage setScenarioType(String type) {
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
        return this;
    }

    /**
     * Sets the workspace
     * @param workspace - the workspace
     * @return current page object
     */
    public FilterCriteriaPage setWorkspace(String workspace) {
        switch (workspace) {
            case "Private":
                pageUtils.waitForElementAndClick(privateCheckBox);
                break;
            case "Public":
                pageUtils.waitForElementAndClick(publicCheckBox);
                break;
            default:
                throw new IllegalArgumentException("The workspace: " + workspace + " is not found");
        }
        return this;
    }

    /**
     * Selects the attribute
     *
     * @param attribute - the attribute
     * @return current page object
     */
    private FilterCriteriaPage selectAttribute(String attribute) {
        new Select(rowOneAttributeDropdown).selectByVisibleText(attribute);
        this.attribute = attribute;
        return this;
    }

    /**
     * Insert multiple attributes
     *
     * @param attributes - attribute array
     * @return current page object
     */
    private FilterCriteriaPage multiSelectAttributes(String[] attributes) {
        setAttributes(attributes);
        return this;
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
        pageUtils.waitForElementAndClick(valueInputOne);
        valueInputOne.clear();
        valueInputOne.sendKeys(input);
        return this;
    }

    /**
     * Selects the value as a dropdown
     *
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
     * Set value in more than one row
     *
     * @param values - values to set
     * @return current page object
     */
    private FilterCriteriaPage setValues(String[] values, Boolean[] dropdownFlags) {
        for (int i = 0; i < values.length; i++) {
            if (!dropdownFlags[i]) {
                WebElement elementToUse = i == 0 ? valueInputOne : valueInputTwo;
                valueSelectionActionTextEntry(elementToUse, values[i]);
            } else {
                valueInputDropdown.click();
                costMaturityOptions.get(values[i]).click();
                valueInputDropdown.click();
            }
        }
        return this;
    }

    /**
     * Multi select for value
     *
     * @param values -  values to set
     */
    private void multiSelectValue(String[] values, Boolean[] dropdownFlags) {
        setValues(values, dropdownFlags);
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
     * Sets attribute for multi filter
     *
     * @param attributes - array of attributes to set
     * @return current page object
     */
    private FilterCriteriaPage setAttributes(String[] attributes) {
        for (int i = 0; i < attributes.length; i++) {
            WebElement elementToUse = i == 0 ? rowOneAttributeDropdown : rowTwoAttributeDropdown;
            pageUtils.selectDropdownOption(elementToUse, attributes[i]);
        }
        return this;
    }

    /**
     * Inputs value into input field
     *
     * @param inputToUse   - WebElement to enter text into
     * @param valueToEnter - the text to enter in the input
     */
    private void valueSelectionActionTextEntry(WebElement inputToUse, String valueToEnter) {
        pageUtils.waitForElementAndClick(inputToUse);
        inputToUse.clear();
        inputToUse.sendKeys(valueToEnter);
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

    private void initialiseCostMaturityMap() {
        costMaturityOptions.put("Initial", costMaturityInitialOption);
        costMaturityOptions.put("Low", costMaturityLowOption);
        costMaturityOptions.put("Medium", costMaturityMediumOption);
        costMaturityOptions.put("High", costMaturityHighOption);
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