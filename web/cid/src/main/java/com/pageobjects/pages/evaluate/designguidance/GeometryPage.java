package com.pageobjects.pages.evaluate.designguidance;

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

public class GeometryPage extends EvaluatePanelToolbar {

    private final Logger logger = LoggerFactory.getLogger(GeometryPage.class);

    @FindBy(css = "div[data-ap-comp='gcdTreeTable']")
    private WebElement gcdTable;

    @FindBy(css = "div[data-ap-comp='gcdTreeTable'] thead[class='v-grid-header']")
    private WebElement gcdTableHeader;

    @FindBy(css = "div[data-ap-comp='gcdTreeTable'] div.v-grid-scroller-vertical")
    private WebElement gcdTableScroller;

    @FindBy(css = "div[data-ap-comp='artifactProperties'] div.v-grid-scroller-vertical")
    private WebElement propertiesScroller;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ColumnUtils columnUtils;

    public GeometryPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(gcdTable);
        pageUtils.waitForElementToAppear(gcdTableHeader);
    }

    /**
     * Selects both gcd and gcd property
     *
     * @param gcdParent   - the gcd dropdown
     * @param gcdChild    - the gcd type
     * @param gcdProperty - the gcd property
     * @return current page object
     */
    public GeometryPage selectGCDAndGCDProperty(String gcdParent, String gcdChild, String gcdProperty) {
        pageUtils.waitForElementAndClick(findGCDType(gcdParent));
        pageUtils.waitForElementAndClick(findGCDChild(gcdChild));
        selectGCDProperty(gcdProperty);
        return this;
    }

    /**
     * Finds the gcd type
     *
     * @param gcdParent - the gcd parent type
     * @return gcd type as webelement
     */
    private WebElement findGCDType(String gcdParent) {
        By gcdParentElement = By.xpath("//div[@data-ap-comp='gcdTreeTable']//div[.='" + gcdParent + "']//span[@class='fa fa-caret-right']");
        pageUtils.waitForElementToAppear(gcdParentElement);
        return pageUtils.scrollToElement(gcdParentElement, gcdTableScroller, Constants.ARROW_DOWN);
    }

    /**
     * Finds the gcd child
     *
     * @param gcdType - the gcd type
     * @return
     */
    private WebElement findGCDChild(String gcdType) {
        By gcdChildElement = By.xpath("//div[@data-ap-comp='gcdTreeTable']//div[.='" + gcdType + "']");
        pageUtils.waitForElementToAppear(gcdChildElement);
        return pageUtils.scrollToElement(gcdChildElement, gcdTableScroller, Constants.ARROW_DOWN);
    }

    /**
     * Selects the gcd property
     *
     * @param gcdProperty - the gcd property
     * @return gcd property as webelement
     */
    private GeometryPage selectGCDProperty(String gcdProperty) {
        By gcd = By.xpath("//div[@data-ap-comp='artifactProperties']//td[contains(text(),'" + gcdProperty + "')]");
        pageUtils.waitForElementToAppear(gcd);
        pageUtils.scrollToElement(gcd, propertiesScroller, Constants.ARROW_DOWN).click();
        return this;
    }

    /**
     * Finds the gcd
     *
     * @param gcdParent - the gcd dropdown
     * @param gcdChild  - the gcd type
     * @return current page object
     */
    public GeometryPage findGCD(String gcdParent, String gcdChild) {
        findGCDType(gcdParent).click();
        findGCDChild(gcdChild).click();
        return this;
    }

    /**
     * Gets the cell details
     *
     * @param column - the column
     * @return string
     */
    public String getGeometryCell(String gcdType, String column) {
        String cellLocator = "//div[@data-ap-comp='gcdTreeTable']//div[contains(text(),'" + gcdType + "')]/ancestor::tr[@class]";
        return columnUtils.columnDetails("gcdTreeTable", column, cellLocator);
    }
}