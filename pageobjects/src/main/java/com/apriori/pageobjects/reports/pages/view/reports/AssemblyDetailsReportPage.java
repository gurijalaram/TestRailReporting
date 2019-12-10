package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.utils.PageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

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
     * Temporary test method to use in order to understand how Jsoup works
     */
    public void getValues() {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());

        int i = 0;
        for (Element row : assemblyDetailsReport.select("table.jrPage > tbody > tr:nth-child(16) > td:nth-child(2) > div > div:nth-child(2) > table tr")) {
            String cssQuery = "td:nth-child(24)";
            //String cssQueryTwo = "td:nth-child(25)";

            if (!row.select(cssQuery).text().isEmpty()) {
                String mainValues = row.select(cssQuery).text();
                logger.debug(String.format("main values (i: %d): %s", i, mainValues));
            }

            //if (!row.select(cssQueryTwo).text().isEmpty()) {
            //    String nullTotalValues = row.select(cssQueryTwo).text();
            //    logger.debug(String.format("null total values (i: %d): %s", i, nullTotalValues));
            //}
            i++;
        }
    }

    private ArrayList<BigDecimal> getValuesFromTable(String cssSelector, int indexRequired) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        ArrayList<BigDecimal> valuesRetrieved = new ArrayList<>();
        int i = 0;

        for (Element row : assemblyDetailsReport.select("table.jrPage > tbody > tr:nth-child(16) > td:nth-child(2) > div > div:nth-child(2) > table tr")) {
            if (!row.select(cssSelector).text().isEmpty() && indexRequired != 0 && i == 27) {
                valuesRetrieved.add(new BigDecimal(row.select(cssSelector).text().replaceAll(",", "")));
            } else if (row.select(cssSelector).text().isEmpty() && indexRequired == 0) {
                valuesRetrieved.add(new BigDecimal(row.select(cssSelector).text().replaceAll(",", "")));
            }
            i++;
        }
        return valuesRetrieved;
    }

    public BigDecimal getExpectedCycleTimeGrandTotal() {
        ArrayList<BigDecimal> valuesToAdd = getValuesFromTable("td:nth-child(24)", 0);
        BigDecimal total = new BigDecimal("0");

        for (BigDecimal value : valuesToAdd) {
            total = total.add(value);
        }

        return total;
    }

    public BigDecimal getColumnTotal() {
        ArrayList<BigDecimal> totalValue = getValuesFromTable("td:nth-child(25)", 27);
        return totalValue.get(totalValue.size() - 1);
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
