package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import com.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FilterPage extends LoadableComponent<FilterPage> {

    private static final Logger logger = LoggerFactory.getLogger(FilterPage.class);

    @FindBy(css = "[class='name-field'] .apriori-select")
    private WebElement filterDropDown;
    @FindBy(css = "[class='name-field'] input")
    private WebElement filterInput;
    @FindBy(css = ".btn-secondary [data-icon='plus']")
    private WebElement newButton;
    @FindBy(css = "[data-icon='file-export']")
    private WebElement saveAsButton;
    @FindBy(css = "[data-icon='pencil']")
    private WebElement renameButton;
    @FindBy(css = "button [data-icon='times-circle']")
    private WebElement cancelButton;
    @FindBy(css = "input[name='name']")
    private WebElement nameInput;
    @FindBy(css = "button [data-icon='plus']")
    private WebElement addButton;
    @FindBy(css = "button [data-icon='clear']")
    private WebElement clearButton;
    @FindBy(css = "qa-searchCriterion[0].delete")
    private WebElement deleteButton;
    @FindBy(css = ".filter-manager [type='submit']")
    private WebElement submitButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;
    private int index;

    public FilterPage(WebDriver driver) {
        this.driver = driver;
        this.modalDialogController = new ModalDialogController(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(filterDropDown);
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public FilterPage selectFilter(String filter) {
        pageUtils.typeAheadSelect(filterDropDown, filter);
        return this;
    }

    /**
     * Create new filter
     *
     * @return current page object
     */
    public FilterPage newFilter() {
        pageUtils.waitForElementAndClick(newButton);
        return this;
    }

    /**
     * Save filter
     *
     * @return current page object
     */
    public FilterPage saveAs() {
        pageUtils.waitForElementAndClick(saveAsButton);
        return this;
    }

    /**
     * Rename filter
     *
     * @return current page object
     */
    public FilterPage rename() {
        pageUtils.waitForElementAndClick(renameButton);
        return this;
    }

    /**
     * Delete filter
     *
     * @return current page object
     */
    public FilterPage delete() {
        pageUtils.waitForElementAndClick(deleteButton);
        return this;
    }

    /**
     * Cancel filter input
     *
     * @return current page object
     */
    public FilterPage cancelInput() {
        pageUtils.waitForElementAndClick(cancelButton);
        return this;
    }

    /**
     * Input filter name
     *
     * @param name - the name
     * @return current page object
     */
    public FilterPage inputName(String name) {
        pageUtils.waitForElementAndClick(nameInput);
        nameInput.clear();
        nameInput.sendKeys(name);
        return this;
    }

    /**
     * Add filter
     *
     * @return current page object
     */
    public FilterPage add() {
        pageUtils.waitForElementAndClick(addButton);
        return this;
    }

    /**
     * Clear filter
     *
     * @return current page object
     */
    public FilterPage clear() {
        pageUtils.waitForElementAndClick(clearButton);
        return this;
    }

    /**
     * Adds a new criteria
     *
     * @param property - the property
     * @param value    - the value
     * @return current page object
     */
    public FilterPage addCriteriaWithOption(String property, String value) {
        index = getIndex();

        add()
            .inputProperty(property)
            .inputValue(property, value);

        return this;
    }

    /**
     * Adds a new criteria
     *
     * @param property  - the property
     * @param operation - the operation
     * @param value     - the value
     * @return current page object
     */
    public FilterPage addCriteriaWithOption(String property, String operation, String value) {
        index = getIndex();

        add()
            .inputProperty(property.trim())
            .inputOperation(operation.trim())
            .inputValue(property.trim(), value.trim());

        return this;
    }

    /**
     * Uses type ahead to input the property
     *
     * @param property - the property
     * @return current page object
     */
    private FilterPage inputProperty(String property) {
        By propertyDropdown = By.cssSelector(String.format("[id='qa-searchCriterion[%s].subject']", index));
        pageUtils.waitForElementAndClick(propertyDropdown);
        By byMaterialCatalog = By.xpath(String.format("//div[@id='qa-searchCriterion[%s].subject']//div[.='%s']//div[@id]", index, property));
        pageUtils.waitForElementAndClick(byMaterialCatalog);

        return this;
    }

    /**
     * Uses type ahead to input the operation
     *
     * @param operation - the operation
     * @return current page object
     */
    private FilterPage inputOperation(String operation) {
        By operationDropdown = By.cssSelector(String.format("[id='qa-searchCriterion[%s].operation']", index));
        pageUtils.waitForElementAndClick(operationDropdown);
        By byMaterialCatalog = By.xpath(String.format("//div[@id='qa-searchCriterion[%s].operation']//div[.='%s']//div[@id]", index, operation));
        pageUtils.waitForElementAndClick(byMaterialCatalog);
        return this;
    }

    /**
     * Uses type ahead to input the value
     *
     * @param value    - the value
     * @param property - the property
     * @return current page object
     */
    private FilterPage inputValue(String property, String value) {
        boolean toggleValue = Constants.TOGGLE_VALUES.stream().anyMatch(str -> str.trim().equalsIgnoreCase(property));

        if (toggleValue) {
            driver.findElement(By.cssSelector(String.format("[id='qa-searchCriterion[%s].target'] button", index))).click();
        } else {
            WebElement valueDropdown = driver.findElement(By.cssSelector(String.format("[id='qa-searchCriterion[%s].target']", index)));
            WebElement valueInput = driver.findElement(By.cssSelector(String.format("[id='qa-searchCriterion[%s].target'] input", index)));

            valuesEntry(property, value, valueInput, Constants.INPUT_VALUES);

            inputValuesEntry(property, value, valueDropdown, Constants.TYPE_INPUT_VALUES);

            valuesEntry(property, value, valueInput, Constants.DATE_VALUES);
        }
        return this;
    }

    /**
     * Input values
     *
     * @param property      - the property
     * @param value         - the value
     * @param valueDropdown - the value dropdown
     */
    private void inputValuesEntry(String property, String value, WebElement valueDropdown, List<String> valueList) {
        valueList.stream().filter(x -> x.trim().equalsIgnoreCase(property)).forEach(y -> pageUtils.typeAheadSelect(valueDropdown, value));
    }

    /**
     * Input values
     *
     * @param property   - the property
     * @param value      - the value
     * @param valueInput - the value input
     * @param valueList  - the value list
     */
    private void valuesEntry(String property, String value, WebElement valueInput, List<String> valueList) {
        valueList.stream().filter(x -> x.trim().equalsIgnoreCase(property)).forEach(y -> {
            pageUtils.waitForElementToAppear(valueInput).clear();
            valueInput.sendKeys(value);
        });
    }

    /**
     * Counts the number of criteria rows that exist and sets the index based on this number
     *
     * @return int
     */
    private int getIndex() {
        int rows = 0;
        if (pageUtils.isElementDisplayed(By.cssSelector(".inputs-row.row"))) {
            rows = driver.findElements(By.cssSelector(".inputs-row.row")).size();
        }
        return rows > 0 ? rows : 0;
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        pageUtils.waitForElementAndClick(submitButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Select the save button
     *
     * @return generic page object
     */
    public <T> T save(Class<T> klass) {
        return modalDialogController.save(klass);
    }
}
