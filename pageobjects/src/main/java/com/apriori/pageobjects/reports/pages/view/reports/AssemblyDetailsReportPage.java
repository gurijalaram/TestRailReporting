package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class AssemblyDetailsReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "//tr[5]/td[10]/span")
    private WebElement reportTableCellElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[25]/span")
    private WebElement cycleTimeGrandTotalElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[28]/span")
    private WebElement piecePartCostGrandTotalElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[31]/span")
    private WebElement fullyBurdenedCostGrandTotalElement;

    @FindBy(xpath = "//span[contains(text(), 'GRAND TOTAL')]/../../td[34]/span")
    private WebElement capitalInvGrandTotalElement;

    private float capitalInvGrandTotalPreviousCurrency;

    private PageUtils pageUtils;
    private WebDriver driver;

    public AssemblyDetailsReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Generic method to get text from specified cell
     * @return String text of cell
     */
    public BigDecimal getTableCellText(String rowIndex, String columnIndex) {
        By cellLocator = By.xpath(String.format("//tr[%s]/td[%s]/span", rowIndex, columnIndex));
        String commaRemovedFigure = pageUtils.getElementText(driver.findElement(cellLocator)).replaceAll(",","");
        return new BigDecimal(commaRemovedFigure);
    }

    /**
     * Performs calculation and returns result
     * @return BigDecimal
     */
    public BigDecimal getExpectedCycleTimeGrandTotal() {
        BigDecimal valueOne = getTableCellText("5", "24");
        BigDecimal valueTwo = getTableCellText("7", "24");
        BigDecimal valueThree = getTableCellText("11", "24");
        BigDecimal valueFour = getTableCellText("15", "24");
        return valueOne.add(valueTwo).add(valueThree).add(valueFour);
    }

    /**
     * Performs calculation and returns result
     * @return BigDecimal
     */
    public BigDecimal getExpectedPiecePartCostGrandTotal() {
        BigDecimal valueOne = getTableCellText("5", "27");
        BigDecimal valueTwo = getTableCellText("7", "27");
        BigDecimal valueThree = getTableCellText("11", "27");
        BigDecimal valueFour = getTableCellText("15", "27");
        BigDecimal valueFive = getTableCellText("17", "27");
        return valueOne.add(valueTwo).add(valueThree).add(valueFour).add(valueFive);
    }

    /**
     * Performs calculation and returns result
     * @return BigDecimal
     */
    public BigDecimal getExpectedFullyBurdenedCostGrandTotal() {
        BigDecimal valueOne = getTableCellText("5", "30");
        BigDecimal valueTwo = getTableCellText("7", "30");
        BigDecimal valueThree = getTableCellText("11", "30");
        BigDecimal valueFour = getTableCellText("15", "30");
        BigDecimal valueFive = getTableCellText("17", "30");
        return valueOne.add(valueTwo).add(valueThree).add(valueFour).add(valueFive);
    }

    /**
     * Performs calculation and returns result
     * @return BigDecimal
     */
    public BigDecimal getExpectedCapitalInvestmentGrandTotal() {
        BigDecimal valueOne = getTableCellText("7", "33");
        BigDecimal valueTwo = getTableCellText("11", "33");
        return valueOne.add(valueTwo);
    }

    /**
     * Gets current currency setting
     * @return String
     */
    public String getCurrentCurrency() {
        return pageUtils.getElementText(currentCurrency);
    }

    public AssemblyDetailsReportPage waitForCorrectCurrency(String currencyToCheck) {
        pageUtils.checkElementAttribute(currentCurrency, "innerText", currencyToCheck);
        return this;
    }
}
