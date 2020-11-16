package com.pageobjects.pages.evaluate.designguidance.tolerances;

import com.apriori.utils.ColumnUtils;
import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class TolerancePage extends EvaluatePanelToolbar {

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
        super(driver);
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
        pageUtils.waitForElementAppear(toleranceTable);
    }

    /**
     * Selects the tolerance type
     *
     * @param toleranceType - the tolerance type
     * @return current page object
     */
    public TolerancePage selectToleranceType(String toleranceType) {
        pageUtils.waitForElementAndClick(findToleranceType(toleranceType));
        return this;
    }

    /**
     * Selects the gcd
     *
     * @param gcdType       - the gcd type
     * @return current page object
     */
    public TolerancePage selectGcd(String gcdType) {
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
        return pageUtils.scrollToElement(tolerance, toleranceScroller, Constants.ARROW_DOWN);
    }

    /**
     * Selects the gcd.  Selection is based on exact match
     *
     * @param gcdType - the gcd type
     * @return the gcd as webelement
     */
    private WebElement findGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='tolerancesDetailsTable']//td[.='" + gcdType + "']");
        return pageUtils.scrollToElement(gcd, detailsScroller, Constants.ARROW_DOWN);
    }

    /**
     * Checks the tolerance count
     *
     * @param toleranceType - the tolerance type
     * @param text          - the text
     * @return true/false
     */
    public boolean isToleranceCount(String toleranceType, String text) {
        pageUtils.waitForElementAndClick(findToleranceType(toleranceType));
        return pageUtils.checkElementAttribute(findToleranceType(toleranceType), "textContent", text);
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
     * @return string
     */
    public boolean isEditButtonEnabled() {
        return editToleranceButton.isEnabled();
    }

    /**
     * Gets the cell details
     *
     * @param toleranceType - tolerance type
     * @param column        - the column
     * @return string
     */
    public String getToleranceCell(String toleranceType, String column) {
        String rowLocator = "//div[@data-ap-comp='tolerancesTable']//td[contains(text(),'" + toleranceType + "')]/ancestor::tr[@class]";
        return columnUtils.columnDetails("tolerancesTable", column, rowLocator);
    }

    /**
     * Gets the cell details
     *
     * @param gcd    - gcd
     * @param column - the column
     * @return string
     */
    public String getGCDCell(String gcd, String column) {
        String rowLocator = "//div[@data-ap-comp='tolerancesDetailsTable']//td[.='" + gcd + "']/ancestor::tr[@class]";
        return columnUtils.columnDetails("tolerancesDetailsTable", column, rowLocator);
    }
}