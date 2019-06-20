package main.java.pages.evaluate.materialutilization.stock;

import main.java.pages.evaluate.materialutilization.MaterialUtilizationPage;
import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectStockPage extends LoadableComponent<SelectStockPage> {

    private final Logger logger = LoggerFactory.getLogger(SelectStockPage.class);

    @FindBy(css = "div[data-ap-comp='stockSelectionTable']")
    private WebElement stockTable;

    @FindBy(css = "select[data-ap-field='stockModes']")
    private WebElement selectionMode;

    @FindBy(css = "div[data-ap-comp='stockSelectionTable'] div.v-grid-scroller-vertical")
    private WebElement stockScroller;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public SelectStockPage(WebDriver driver) {
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
     * Selects the mode
     * @param option - the option to select
     * @return current page object
     */
    public SelectStockPage setSelectionMode(String option) {
        new Select(selectionMode).selectByVisibleText(option);
        return this;
    }

    /**
     * Selects the stock
     * @param stockName - the stock
     * @return current page object
     */
    public SelectStockPage selectStock(String stockName) {
        By stock = By.xpath("//div[@data-ap-comp='stockSelectionTable']//td[contains(text(),'" + stockName + "')]");
        pageUtils.scrollToElement(stock, stockScroller).click();
        return this;
    }

    /**
     * Selects the apply button
     * @return new page object
     */
    protected MaterialUtilizationPage apply() {
        applyButton.click();
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Selects the cancel button
     * @return new page object
     */
    protected MaterialUtilizationPage cancel() {
        cancelButton.click();
        return new MaterialUtilizationPage(driver);
    }

}