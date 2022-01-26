package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.PropertyEnum;

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
     * Adds a criteria
     *
     * @param propertyEnum - property from the enum
     * @param value        - the value
     * @return current page object
     */
    public FilterPage addCriteriaWithOption(final PropertyEnum propertyEnum, final boolean value) {
        index = getIndex();

        add().selectProperty(index, propertyEnum)
            .toggle(index, propertyEnum, value);
        return this;
    }

    /**
     * Adds a criteria
     *
     * @param propertyEnum  - property from the enum
     * @param operationEnum - operation from the enum
     * @param value         - the value
     * @return current page object
     */
    public FilterPage addCriteriaWithOption(final PropertyEnum propertyEnum, final OperationEnum operationEnum, final String value) {
        index = getIndex();

        add().selectProperty(index, propertyEnum)
            .selectOperation(index, operationEnum)
            .inputValue(index, propertyEnum, value);
        return this;
    }

    /**
     * Toggles yes/no
     *
     * @param propertyEnum - property from the enum
     * @param value        - the value
     * @return current page object
     */
    private FilterPage toggle(int index, final PropertyEnum propertyEnum, final boolean value) {
        if (!PropertyEnum.toggleGroup.contains(propertyEnum)) {
            throw new IllegalStateException(String.format("Not able to toggle 'Yes/No' because property '%s' was not found in this group: %s", propertyEnum, PropertyEnum.toggleGroup));
        }
        WebElement buttonStatus = pageUtils.waitForElementToAppear(driver.findElement(By.cssSelector(String.format("//div[@id='modal-body'][id='qa-searchCriterion[%s].target'] button", index))));

        if (value && buttonStatus.getAttribute("class").contains("not-checked")) {
            pageUtils.waitForElementAndClick(buttonStatus);
        }
        return this;
    }

    /**
     * Enters the value
     *
     * @param propertyEnum - property from the enum
     * @param value        - the value
     * @return current page object
     */
    private FilterPage inputValue(int index, final PropertyEnum propertyEnum, final String value) {
        if (PropertyEnum.inputGroup.contains(propertyEnum) || PropertyEnum.dateGroup.contains(propertyEnum)) {
            pageUtils.waitForElementToAppear(By.cssSelector(String.format("[id='modal-body'] input[name='searchCriterion[%s].target']", index))).sendKeys(value);
        }

        if (PropertyEnum.dropdownGroup.contains(propertyEnum)) {
            pageUtils.waitForElementAndClick(By.cssSelector(String.format("[id='modal-body'] div[id='qa-searchCriterion[%s].target']", index)));
            pageUtils.javaScriptClick(pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@id='modal-body']//div[.='%s']//div[@id]", value))));
            //click the dropdown again to remove it
            pageUtils.waitForElementAndClick(By.cssSelector(String.format("[id='modal-body'] div[id='qa-searchCriterion[%s].target']", index)));
        }
        return this;
    }

    /**
     * Selects the property from the dropdown
     *
     * @param propertyEnum - property from the enum
     * @return current page object
     */
    private FilterPage selectProperty(int index, PropertyEnum propertyEnum) {
        By propertyDropdown = By.cssSelector(String.format("[id='qa-searchCriterion[%s].subject']", index));
        pageUtils.waitForElementAndClick(propertyDropdown);
        By byProperty = By.xpath(String.format("//div[@id='qa-searchCriterion[%s].subject']//div[.='%s']//div[@id]", index, propertyEnum.getProperty()));
        pageUtils.waitForElementAndClick(byProperty);
        return this;
    }

    /**
     * Selects the operation from the dropdown
     *
     * @param operationEnum - operation from the enum
     * @return current page object
     */
    private FilterPage selectOperation(int index, OperationEnum operationEnum) {
        By operationDropdown = By.cssSelector(String.format("[id='qa-searchCriterion[%s].operation']", index));
        pageUtils.waitForElementAndClick(operationDropdown);
        By byOperation = By.xpath(String.format("//div[@id='qa-searchCriterion[%s].operation']//div[.='%s']//div[@id]", index, operationEnum.getOperation()));
        pageUtils.waitForElementAndClick(byOperation);
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
