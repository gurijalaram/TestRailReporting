package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        pageUtils.waitForElementAppear(filterDropDown);
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public FilterPage inputFilter(String filter) {
        pageUtils.typeAheadInput(filterDropDown, filterInput, filter);
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
     * @param property  - the property
     * @param operation - the operation
     * @param value     - the value
     * @return current page object
     */
    public FilterPage addCriteria(String property, String operation, String value) {
        index = getIndex();
        add()
            .inputProperty(property)
            .inputOperation(operation)
            .inputValue(value);
        return this;
    }

    /**
     * Uses type ahead to input the property
     *
     * @param property - the property
     * @return current page object
     */
    private FilterPage inputProperty(String property) {
        WebElement propertyDropdown = driver.findElement(By.cssSelector(String.format("[id='qa-searchCriterion[%s].subject']", index)));
        WebElement propertyInput = driver.findElement(By.cssSelector(String.format("[id='qa-searchCriterion[%s].subject'] input", index)));
        pageUtils.typeAheadInput(propertyDropdown, propertyInput, property);
        return this;
    }

    /**
     * Uses type ahead to input the operation
     *
     * @param operation - the operation
     * @return current page object
     */
    private FilterPage inputOperation(String operation) {
        WebElement operationDropdown = driver.findElement(By.cssSelector(String.format("[id='qa-searchCriterion[%s].operation']", index)));
        WebElement operationInput = driver.findElement(By.cssSelector(String.format("[id='qa-searchCriterion[%s].operation'] input", index)));
        pageUtils.typeAheadInput(operationDropdown, operationInput, operation);
        return this;
    }

    /**
     * Uses type ahead to input the value
     *
     * @param value - the value
     * @return current page object
     */
    private FilterPage inputValue(String value) {
        WebElement valueDropdown = driver.findElement(By.cssSelector(String.format("id='qa-searchCriterion[%s].target']", index)));
        WebElement valueInput = driver.findElement(By.cssSelector(String.format("id='qa-searchCriterion[%s].target'] input", index)));
        pageUtils.typeAheadInput(valueDropdown, valueInput, value);
        return this;
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
        return modalDialogController.submit(klass);
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
