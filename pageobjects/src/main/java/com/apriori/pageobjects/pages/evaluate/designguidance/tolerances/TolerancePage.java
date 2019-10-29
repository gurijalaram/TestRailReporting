package com.apriori.pageobjects.pages.evaluate.designguidance.tolerances;

import com.apriori.pageobjects.utils.ColumnUtils;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class TolerancePage extends LoadableComponent<TolerancePage> {

    private final Logger logger = LoggerFactory.getLogger(TolerancePage.class);

    @FindBy(css = "div[data-ap-comp='tolerancesTable']")
    private WebElement toleranceTable;

    @FindBy(css = "div[data-ap-comp='tolerancesTable'] .v-grid-scroller-vertical")
    private WebElement toleranceScroller;

    @FindBy(css = "button[data-ap-comp='editTolerancesBtn']")
    private WebElement editToleranceButton;

    @FindBy(css = "div[data-ap-comp='tolerancesDetailsTable'] .v-grid-scroller-vertical")
    private WebElement detailsScroller;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ColumnUtils columnUtils;

    public TolerancePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.columnUtils = new ColumnUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * Selects both tolerance and gcd
     *
     * @param toleranceType - the tolerance type
     * @param gcdType       - the gcd type
     * @return current page object
     */
    public TolerancePage selectToleranceTypeAndGCD(String toleranceType, String gcdType) {
        pageUtils.waitForElementAndClick(findToleranceType(toleranceType));
        pageUtils.waitForElementAndClick(findGCD(gcdType));
        return this;
    }

    /**
     * Finds the tolerance type.  Selection is based on contains so unit must be specified eg. Flatness
     *
     * @param toleranceType - the tolerance type
     * @return the tolerance as webelement
     */
    public WebElement findToleranceType(String toleranceType) {
        By tolerance = By.xpath("//div[@data-ap-comp='tolerancesTable']//td[contains(text(),'" + toleranceType + "')]/ancestor::tr");
        pageUtils.waitForElementToAppear(tolerance);
        return pageUtils.scrollToElement(tolerance, toleranceScroller);
    }

    /**
     * Selects the gcd.  Selection is based on exact match
     *
     * @param gcdType - the gcd type
     * @return the gcd as webelement
     */
    private WebElement findGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='tolerancesDetailsTable']//td[.='" + gcdType + "']/ancestor::tr");
        pageUtils.waitForElementToAppear(gcd);
        return pageUtils.scrollToElement(gcd, detailsScroller);
    }

    /**
     * Checks the tolerance count
     *
     * @param toleranceType - the tolerance type
     * @param text          - the text
     * @return true/false
     */
    public Boolean isToleranceCount(String toleranceType, String text) {
        findToleranceType(toleranceType).click();
        return pageUtils.checkElementAttribute(findToleranceType(toleranceType), "outerText", text);
    }

    /**
     * Selects the edit button
     *
     * @return current page object
     */
    public ToleranceEditPage selectEditButton() {
        pageUtils.waitForElementAndClick(editToleranceButton);
        return new ToleranceEditPage(driver);
    }

    /**
     * Gets the button as a webelement
     *
     * @return the button as webelement
     */
    public WebElement getEditButton() {
        return editToleranceButton;
    }

    /**
     * Gets the cell details
     * @param toleranceType - tolerance type
     * @param column - the column
     * @return string
     */
    public String getToleranceCell(String toleranceType, String column) {
        String rowLocator = "//div[@data-ap-comp='tolerancesTable']//td[contains(text(),'" + toleranceType + "')]/ancestor::tr[@class]";
        return columnUtils.columnDetails("tolerancesTable", column, rowLocator);
    }

    /**
     * Gets the cell details
     * @param gcd - gcd
     * @param column - the column
     * @return string
     */
    public String getGCDCell(String gcd, String column) {
        String rowLocator = "//div[@data-ap-comp='tolerancesDetailsTable']//td[.='" + gcd + "']/ancestor::tr[@class]";
        return columnUtils.columnDetails("tolerancesDetailsTable", column, rowLocator);
    }
}