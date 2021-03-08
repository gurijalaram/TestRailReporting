package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class TableColumnsPage extends LoadableComponent<TableColumnsPage> {

    private static final Logger logger = LoggerFactory.getLogger(TableColumnsPage.class);

    @FindBy(css = "[data-ap-scope='tableViewSelection'] .modal-content")
    private WebElement tableDialog;

    @FindBy(css = "select[data-ap-comp='leftList']")
    private WebElement availableList;

    @FindBy(css = "select[data-ap-comp='rightList']")
    private WebElement includedList;

    @FindBy(css = "button[data-ap-comp='rightButton']")
    private WebElement addColumnButton;

    @FindBy(css = "button[data-ap-comp='leftButton']")
    private WebElement removeColumnButton;

    @FindBy(css = "[data-ap-scope='tableViewSelection'] .btn-primary")
    private WebElement okButton;

    @FindBy(css = "[data-ap-scope='tableViewSelection'] .gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "button[data-ap-comp='upButton']")
    private WebElement upButton;

    @FindBy(css = "button[data-ap-comp='downButton']")
    private WebElement downButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public TableColumnsPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(tableDialog);
    }

    /**
     * Adds a column
     *
     * @param column - the enum column name
     * @return current page object
     */
    public TableColumnsPage addColumn(String column) {
        pageUtils.waitForElementToAppear(availableList);
        new Select(availableList).selectByValue(column);
        selectRightArrow();
        return this;
    }

    /**
     * Removes a column
     *
     * @param column - the enum column name
     * @return current page object
     */
    public TableColumnsPage removeColumn(String column) {
        pageUtils.waitForElementToAppear(includedList);
        new Select(includedList).selectByValue(column);
        selectLeftArrow();
        return this;
    }

    /**
     * Selects the right arrow to add a column
     *
     * @return current page object
     */
    private TableColumnsPage selectRightArrow() {
        pageUtils.waitForElementAndClick(addColumnButton);
        return this;
    }

    /**
     * Selects the left arrow to remove a column
     *
     * @return current page object
     */
    private TableColumnsPage selectLeftArrow() {
        pageUtils.waitForElementAndClick(removeColumnButton);
        return this;
    }

    /**
     * Selects the ok button
     *
     * @return new page object
     */
    public <T> T selectSaveButton(Class<T> className) {
        pageUtils.waitForElementAndClick(okButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage selectCancelButton() {
        cancelButton.click();
        return new ExplorePage(driver);
    }

    /**
     * Moves the column to the top
     *
     * @return current page object
     */
    public TableColumnsPage moveColumnToTop(String column) {
        pageUtils.waitForElementToAppear(includedList);
        new Select(includedList).selectByValue(column);
        clickUpButton();
        return this;
    }

    /**
     * Clicks up until disabled
     *
     * @return current page object
     */
    private TableColumnsPage clickUpButton() {
        do {
            pageUtils.waitForElementAndClick(upButton);
        } while (!upButton.getAttribute("outerHTML").contains("disabled"));
        return this;
    }

    /**
     * Gets the included columns list
     *
     * @return current page object
     */
    public String getIncludedList() {
        return includedList.getText();
    }
}