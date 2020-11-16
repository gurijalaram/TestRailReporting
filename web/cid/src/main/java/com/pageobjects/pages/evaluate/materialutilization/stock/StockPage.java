package com.pageobjects.pages.evaluate.materialutilization.stock;

import com.apriori.utils.PageUtils;

import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class StockPage extends EvaluatePanelToolbar {

    private final Logger logger = LoggerFactory.getLogger(StockPage.class);

    @FindBy(css = ".secondary-treatments-panel-header-btn")
    private WebElement editButton;

    @FindBy(css = ".table.material-stock-table")
    private WebElement stockTable;

    private WebDriver driver;
    private PageUtils pageUtils;

    public StockPage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(stockTable);
    }

    /**
     * Gets details of the stock table.
     *
     * @return stock details as string
     */
    public boolean checkTableDetails(String stock) {
        return pageUtils.textPresentInElement(stockTable, stock);
    }

    /**
     * Selects the edit button
     *
     * @return new page object
     */
    public SelectStockPage editStock() {
        editButton.click();
        return new SelectStockPage(driver);
    }

    /**
     * Gets the button as a webelement
     *
     * @return string
     */
    public boolean isEditButtonEnabled() {
        return editButton.isEnabled();
    }
}