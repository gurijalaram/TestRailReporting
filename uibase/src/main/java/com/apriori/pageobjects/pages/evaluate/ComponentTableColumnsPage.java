package com.apriori.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cfrith, bhegan
 */

public class ComponentTableColumnsPage extends LoadableComponent<ComponentTableColumnsPage> {

    private final Logger logger = LoggerFactory.getLogger(ComponentTableColumnsPage.class);

    private Map<String, String> columnSelectorMap = new HashMap<>();

    @FindBy(css = "[data-ap-scope='assemblyComponentsTableViewSelection'] .modal-content")
    private WebElement tableDialog;

    @FindBy(css = "select[data-ap-comp='leftList']")
    private WebElement availableList;

    @FindBy(css = "select[data-ap-comp='rightList']")
    private WebElement includedList;

    @FindBy(css = "button[data-ap-comp='rightButton']")
    private WebElement addColumnButton;

    @FindBy(css = "button[data-ap-comp='leftButton']")
    private WebElement removeColumnButton;

    @FindBy(css = "[data-ap-scope='assemblyComponentsTableViewSelection'] .btn-primary")
    private WebElement okButton;

    @FindBy(css = "[data-ap-scope='assemblyComponentsTableViewSelection'] .gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "button[data-ap-comp='upButton']")
    private WebElement upButton;

    @FindBy(css = "button[data-ap-comp='downButton']")
    private WebElement downButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComponentTableColumnsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseColumnSelectorMap();
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
    public ComponentTableColumnsPage addColumn(String column) {
        pageUtils.waitForElementToAppear(availableList);
        new Select(availableList).selectByValue(column);
        selectRightArrow();
        return this;
    }

    /**
     * Gets table values for PPC, CI, FBC and CT
     *
     * @return List of BigDecimals
     */
    public ArrayList<BigDecimal> getTableValues(String column) {
        Document evaluateComponentView = Jsoup.parse(driver.getPageSource());
        String baseCssSelector = "div[class='v-grid-tablewrapper'] > table > tbody > tr:nth-child(%s) td";
        ArrayList<BigDecimal> figureValues = new ArrayList<>();

        for (Element valueElement : evaluateComponentView.select(String.format(baseCssSelector, columnSelectorMap.get(column)))) {
            if (valueElement.text().chars().allMatch(Character::isDigit)) {
                figureValues.add(new BigDecimal(valueElement.text()));
            }
        }
        return figureValues;
    }

    /**
     * Checks if columns are already set properly
     *
     * @param columnsToAdd
     * @param columnsToRemove
     * @return current page object
     */
    public ComponentTableColumnsPage checkColumnSettings(List<String> columnsToAdd, List<String> columnsToRemove) {
        int availableCount = Integer.parseInt(availableList.getAttribute("childElementCount"));
        List<String> leftListContents = new ArrayList<>();
        for (int i = 0; i < availableCount; i++) {
            leftListContents.add(driver.findElements(By.xpath("//select[@data-ap-comp='leftList']/option")).get(i).getText());
        }

        int includedCount = Integer.parseInt(includedList.getAttribute("childElementCount"));
        List<String> rightListContents = new ArrayList<>();
        for (int j = 0; j < includedCount; j++) {
            rightListContents.add(driver.findElements(By.xpath("//select[@data-ap-comp='rightList']/option")).get(j).getText());
        }

        for (String columnName : columnsToAdd) {
            if (leftListContents.contains(columnName) && !rightListContents.contains(columnName)) {
                addColumn(columnName);
            }
        }

        for (String columnName : columnsToRemove) {
            if (!leftListContents.contains(columnName) && rightListContents.contains(columnName)) {
                removeColumn(columnName);
            }
        }

        return this;
    }

    /**
     * Adds multiple columns
     *
     * @param columnsToAdd - list of columns to add
     * @return current page object
     */
    public ComponentTableColumnsPage addMultipleColumns(List<String> columnsToAdd) {
        pageUtils.waitForElementToAppear(availableList);
        Select rightListDropdown = new Select(availableList);

        for (String column : columnsToAdd) {
            rightListDropdown.selectByValue(column);
            selectRightArrow();
        }
        return this;
    }

    /**
     * Removes a column
     *
     * @param column - the enum column name
     * @return current page object
     */
    public ComponentTableColumnsPage removeColumn(String column) {
        pageUtils.waitForElementToAppear(includedList);
        new Select(includedList).selectByValue(column);
        selectLeftArrow();
        return this;
    }

    /**
     * Removes multiple columns
     *
     * @param columnsToRemove - list of columns to remove
     * @return current page object
     */
    public ComponentTableColumnsPage removeMultipleColumns(List<String> columnsToRemove) {
        pageUtils.waitForElementToAppear(includedList);
        Select rightListDropdown = new Select(includedList);
        rightListDropdown.deselectAll();
        for (String column : columnsToRemove) {
            rightListDropdown.selectByValue(column);
            selectLeftArrow();
        }
        Select leftListDropdown = new Select(availableList);
        leftListDropdown.deselectAll();
        return this;
    }

    /**
     * Selects the right arrow to add a column
     *
     * @return current page object
     */
    private ComponentTableColumnsPage selectRightArrow() {
        pageUtils.waitForElementAndClick(addColumnButton);
        return this;
    }

    /**
     * Selects the left arrow to remove a column
     *
     * @return current page object
     */
    private ComponentTableColumnsPage selectLeftArrow() {
        pageUtils.waitForElementAndClick(removeColumnButton);
        return this;
    }

    /**
     * Selects the ok button
     *
     * @return new page object
     */
    public EvaluatePage selectSaveButton() {
        pageUtils.waitForElementAndClick(okButton);
        return new EvaluatePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage selectCancelButton() {
        cancelButton.click();
        return new EvaluatePage(driver);
    }

    /**
     * Moves the column to the top
     *
     * @return current page object
     */
    public ComponentTableColumnsPage moveColumnToTop(String column) {
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
    private ComponentTableColumnsPage clickUpButton() {
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

    private void initialiseColumnSelectorMap() {
        columnSelectorMap.put("Per Part Cost (USD)", "1");
        columnSelectorMap.put("Capital Investment (USD)", "2");
        columnSelectorMap.put("Fully Burdened Cost (USD)", "3");
        columnSelectorMap.put("Cycle Time (s)", "4");
    }
}