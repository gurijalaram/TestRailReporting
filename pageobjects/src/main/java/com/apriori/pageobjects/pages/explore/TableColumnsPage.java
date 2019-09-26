package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.utils.PageUtils;

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

    private final Logger logger = LoggerFactory.getLogger(TableColumnsPage.class);

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
    public ExplorePage selectSaveButton() {
        okButton.click();
        return new ExplorePage(driver);
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
}