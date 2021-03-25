package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

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
    private WebElement deleteButton;

    @FindBy(css = "button [data-icon='times-circle']")
    private WebElement cancelButton;

    @FindBy(css = "input[name='name']")
    private WebElement nameInput;

    @FindBy(css = "button [data-icon='plus']")
    private WebElement addButton;

    @FindBy(css = "button [data-icon='clear']")
    private WebElement clearButton;

    @FindBy(css = "qa-searchCriterion[0].subject")
    private WebElement propertyDropdown;

    @FindBy(css = "qa-searchCriterion[0].subject input")
    private WebElement propertyInput;

    @FindBy(css = "qa-searchCriterion[0].operation")
    private WebElement operationDropdown;

    @FindBy(css = "qa-searchCriterion[0].operation input")
    private WebElement operationInput;

    @FindBy(css = "qa-searchCriterion[0].target")
    private WebElement valueDropdown;

    @FindBy(css = "qa-searchCriterion[0].target input")
    private WebElement valueInput;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

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
    public FilterPage typeAheadFilter(String filter) {
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
        typeAheadProperty(property)
            .typeAheadOperation(operation)
            .typeAheadValue(value);
        return this;
    }

    /**
     * Uses type ahead to input the property
     *
     * @param property - the property
     * @return current page object
     */
    private FilterPage typeAheadProperty(String property) {
        pageUtils.typeAheadInput(propertyDropdown, propertyInput, property);
        return this;
    }

    /**
     * Uses type ahead to input the operation
     *
     * @param operation - the operation
     * @return current page object
     */
    private FilterPage typeAheadOperation(String operation) {
        pageUtils.typeAheadInput(operationDropdown, operationInput, operation);
        return this;
    }

    /**
     * Uses type ahead to input the value
     *
     * @param value - the value
     * @return current page object
     */
    private FilterPage typeAheadValue(String value) {
        pageUtils.typeAheadInput(valueDropdown, valueInput, value);
        return this;
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
