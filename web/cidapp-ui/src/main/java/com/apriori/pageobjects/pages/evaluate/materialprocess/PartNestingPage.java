package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartNestingPage extends LoadableComponent<StockPage> {

    private static final Logger logger = LoggerFactory.getLogger(StockPage.class);

    @FindBy(id = "qa-sub-header-cost-button")
    private WebElement costButton;

    @FindBy(css = ".alert")
    private WebElement costLabel;

    @FindBy(css = "#qa-part-nesting-utilization-mode-select > div > div")
    private WebElement utilizationModeDropDown;

    @FindBy(css = "div[class*=width]")
    private WebElement stockWidth;

    @FindBy(css = "div[class*=length]")
    private WebElement stockLength;

    @FindBy (css = "#qa-part-nesting-utilization-mode-select > label")
    private WebElement utilizationMode;

    private PageUtils pageUtils;
    private WebDriver driver;

    public PartNestingPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(costLabel);
    }

    /**
     * Gets the Selected Sheet text value on the page
     *
     * @param label - the label
     * @return String
     */
    public String getSelectedSheetInfo(String label) {
        By selectedSheet = By.xpath(String.format("//span[.='Selected Sheet']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(selectedSheet).getAttribute("textContent");
    }

    /**
     * Gets the Blank Size value on the page
     *
     * @param label - the label
     * @return String
     */
    public String getBlankSizeInfo(String label) {
        By selectedSheet = By.xpath(String.format("//span[.='Blank Size']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(selectedSheet).getAttribute("textContent");
    }

    /**
     * Gets the Parts per Sheet value on the page
     *
     * @param label - the label
     * @return String
     */
    public String getPartsPerSheetInfo(String label) {
        By selectedSheet = By.xpath(String.format("//span[.='Parts Per Sheet']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(selectedSheet).getAttribute("textContent");
    }

    /**
     * Gets the Part Nesting value on the page
     *
     * @param label - the label
     * @return String
     */
    public String getPartNestingInfo(String label) {
        By selectedSheet = By.xpath(String.format("//span[.='Part Nesting']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(selectedSheet).getAttribute("textContent");
    }

    /**
     * Gets the Utilization Mode page source for assertion
     *
     * @param label - the label
     * @return Boolean
     */
    public boolean getUtilizationModeInfo(String label) {
        return utilizationMode.getText().equalsIgnoreCase(label);
    }

    /**
     * Gets the Stock Width value on the page
     *
     * @return String
     */
    public String getStockWidthInfo() {
        return stockWidth.getText();
    }

    /**
     * Gets the Stock Length value on the page
     *
     * @return String
     */
    public String getStockLengthInfo() {
        return stockLength.getText();
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

    /**
     * Gets cost for the selected part
     *
     * @return current page object
     */
    public PartNestingPage clickOnCost() {
        pageUtils.waitForElementToAppear(costLabel);
        pageUtils.waitForElementAndClick(costButton);
        return new PartNestingPage(driver);
    }
}


