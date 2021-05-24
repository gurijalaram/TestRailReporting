package com.pageobjects.pages.evaluate.designguidance.investigation;

import com.apriori.utils.ColumnUtils;
import com.apriori.utils.PageUtils;

import com.pageobjects.pages.evaluate.designguidance.tolerances.ThreadingPage;
import com.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class InvestigationPage extends LoadableComponent<InvestigationPage> {

    private static final Logger logger = LoggerFactory.getLogger(InvestigationPage.class);

    @FindBy(css = "div[data-ap-comp='dtcTopicTable']")
    private WebElement topicTable;

    @FindBy(css = "select[class='dtc-table-control']")
    private WebElement threadableDropdown;

    @FindBy(css = "div[data-ap-comp='dtcInvestigationTableExt'] .edit-tolerances-btn")
    private WebElement editButton;

    @FindBy(css = "div[data-ap-comp='dtcTableExtArea'] div.v-grid-scroller-vertical")
    private WebElement threadScroller;

    @FindBy(css = "div[data-ap-comp='dtcInvestigationTableExt'] .v-grid-header")
    private WebElement threadHeader;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ColumnUtils columnUtils;

    public InvestigationPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(topicTable);
    }

    public ThreadingPage editThread(String gcdType, String gcd) {
        selectGcdTypeAndGcd(gcdType, gcd);
        selectEditButton();
        return new ThreadingPage(driver);
    }

    /**
     * Selects investigation topic
     *
     * @param topic - the investigation topic
     * @return current page object
     */
    public InvestigationPage selectInvestigationTopic(String topic) {
        By investigationTopic = By.xpath("//div[@data-ap-comp='dtcTopicTable']//div[.='" + topic + "']");
        pageUtils.waitForElementToAppear(investigationTopic).click();
        return this;
    }

    /**
     * Selects the threadable dropdown
     *
     * @param option - the dropdown in the option
     * @return current page object
     */
    public InvestigationPage selectFilterDropdown(String option) {
        pageUtils.selectDropdownOption(threadableDropdown, option);
        return this;
    }

    /**
     * Selects the selectEditButton button
     *
     * @return new page object
     */
    public ThreadingPage selectEditButton() {
        pageUtils.waitForElementAndClick(editButton);
        return new ThreadingPage(driver);
    }

    /**
     * Gets the button as a webelement
     *
     * @return the button as webelement
     */
    public WebElement getEditButton() {
        return editButton;
    }

    /**
     * Gets the thread header information
     *
     * @return string
     */
    public String getThreadHeader() {
        By threadHeader = By.cssSelector("div[data-ap-comp='dtcInvestigationTableExt'] .v-grid-header");
        pageUtils.waitForElementToAppear(threadHeader);
        return driver.findElement(threadHeader).getAttribute("textContent");
    }

    /**
     * Gets the cell details
     *
     * @param issueType - tolerance type
     * @param column    - the column
     * @return string
     */
    public String getInvestigationCell(String issueType, String column) {
        String rowLocator = "//div[@data-ap-comp='dtcInvestigationTable']//div[contains(text(),'" + issueType + "')]/ancestor::tr[@class]";
        return columnUtils.columnDetails("dtcInvestigationTable", column, rowLocator);
    }

    /**
     * Gets the information in the gcd row
     *
     * @param gcd - the gcd type
     * @return the cell info as list
     */
    public List<String> getGcdRow(String gcd) {
        By gcdRow = By.xpath(String.format("//div[@data-ap-comp='dtcTableExtArea']//div[contains(text(),'%s')]/ancestor::tr[@class]", gcd));
        return Arrays.stream(driver.findElement(gcdRow).getText().split("[\n ]")).collect(Collectors.toList());
    }

    /**
     * Selects the gcd type and gcd
     *
     * @param gcdType - the gcd type
     * @param gcd     - the gcd
     * @return current page object
     */
    public InvestigationPage selectGcdTypeAndGcd(String gcdType, String gcd) {
        findGCDType(gcdType).click();
        findGCD(gcd).click();
        return this;
    }

    /**
     * Selects the gcd type
     *
     * @param gcdType - the gcd type
     * @return the gcd type as a webelement
     */
    private WebElement findGCDType(String gcdType) {
        By type = By.xpath("//div[@data-ap-comp='dtcTableExtArea']//div[contains(text(),'" + gcdType + "')]/ancestor::tr//label[@class]");
        return pageUtils.scrollToElement(type, threadScroller, Constants.ARROW_DOWN);
    }

    /**
     * Selects the gcd
     *
     * @param gcd - the gcd
     * @return the gcd as webelement
     */
    private WebElement findGCD(String gcd) {
        By gcdElement = By.xpath("//div[@data-ap-comp='dtcTableExtArea']//div[contains(text(),'" + gcd + "')]/ancestor::td[@class]");
        return pageUtils.scrollToElement(gcdElement, threadScroller, Constants.ARROW_DOWN);
    }
}