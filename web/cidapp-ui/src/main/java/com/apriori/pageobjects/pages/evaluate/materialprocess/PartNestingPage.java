package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class PartNestingPage extends LoadableComponent<StockPage> {

    @FindBy(css = "[id='qa-part-nesting-utilization-mode-select'] .apriori-select")
    private WebElement utilizationModeDropDown;

    @FindBy(css = ".stock-width")
    private WebElement stockWidth;

    @FindBy(css = ".stock-length")
    private WebElement stockLength;

    @FindBy(css = "[id='qa-part-nesting-utilization-mode-select'] > label")
    private WebElement utilizationMode;

    private PageUtils pageUtils;
    private WebDriver driver;

    public PartNestingPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets the Selected Sheet text value on the page
     *
     * @param label - the value
     * @return String
     */
    public String getNestingInfo(String label) {
        By selectedSheet = By.xpath(String.format("//span[.='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(selectedSheet).getAttribute("textContent");
    }

    /**
     * Gets the Utilization Mode page source for assertion
     *
     * @param text - the text
     * @return Boolean
     */
    public boolean isUtUtilizationModeInfo(String text) {
        return pageUtils.waitForElementToAppear(utilizationMode).getAttribute("textContent").equalsIgnoreCase(text);
    }

    /**
     * Gets the Stock Width value on the page
     *
     * @return String
     */
    public String getStockWidthInfo() {
        return pageUtils.waitForElementToAppear(stockWidth).getAttribute("textContent");
    }

    /**
     * Gets the Stock Length value on the page
     *
     * @return String
     */
    public String getStockLengthInfo() {
        return pageUtils.waitForElementToAppear(stockLength).getAttribute("textContent");
    }

    /**
     * Uses type ahead to input the status
     *
     * @param status - the status
     * @return current page object
     */
    public PartNestingPage SelectUtilizationModeDropDown(String status) {
        pageUtils.typeAheadSelect(utilizationModeDropDown, status);
        return this;
    }
}


