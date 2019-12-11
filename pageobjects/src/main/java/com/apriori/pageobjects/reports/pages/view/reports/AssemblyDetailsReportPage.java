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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssemblyDetailsReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);
    private Map<String, String> columnMap = new HashMap<>();
    private Map<String, String> columnTotalMap = new HashMap<>();

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

    private PageUtils pageUtils;
    private WebDriver driver;

    public AssemblyDetailsReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseColumnMap();
        initialiseColumnTotalMap();
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

            if (!row.select(cssQuery).text().isEmpty() && !row.select(cssQuery).text().equals("null")
                && !row.select(cssQuery).text().equals("0.00")) {
                String mainValues = row.select(cssQuery).text();
                logger.debug(String.format("main values (i: %d): %s", i, mainValues));
            }

            //if (!row.select(cssQueryTwo).text().isEmpty() && !row.select(cssQueryTwo).text().equals("null")
            //    && !row.select(cssQueryTwo).text().contains("Cycle")) {
            //    String nullTotalValues = row.select(cssQueryTwo).text();
            //    logger.debug(String.format("null total values (i: %d): %s", i, nullTotalValues));
            //}
            i++;
        }
    }

    private ArrayList<BigDecimal> getValuesFromTable(String cssSelector) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        ArrayList<BigDecimal> valuesRetrieved = new ArrayList<>();

        for (Element column : assemblyDetailsReport.select("table.jrPage > tbody > tr:nth-child(16) > td:nth-child(2) > div > div:nth-child(2) > table tr")) {
            if (!column.select(cssSelector).text().isEmpty() && !column.select(cssSelector).text().equals("null")
                && !column.select(cssSelector).text().chars().anyMatch(Character::isLetter) && !column.select(cssSelector).text().equals("0.00")) {
                valuesRetrieved.add(new BigDecimal(column.select(cssSelector).text().replaceAll(",", "")));
            }
        }
        return valuesRetrieved;
    }

    public String[][] getQuantityAndPriceValues(String cssSelector, String column) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        ArrayList<BigDecimal> values = getValuesFromTable(columnMap.get(column));
        String[][] vals = new String[values.size()][];

        int i = 0;
        for (Element row : assemblyDetailsReport.select(cssSelector)) {
            if (!row.select(cssSelector).text().isEmpty() && !row.select(cssSelector).text().equals("null")
                && !row.select(cssSelector).text().chars().anyMatch(Character::isLetter)) {
                vals[i][i] = row.select(cssSelector).text();
                vals[i][i + 1] = getValuesFromTable(columnMap.get(column)).get(i).toString();
            }
            i++;
        }
        return vals;
    }

    public BigDecimal getActualColumnGrandTotal(String column) {
        ArrayList<BigDecimal> valuesRetrieved = getValuesFromTable(columnTotalMap.get(column));
        return valuesRetrieved.get(0);
    }

    public BigDecimal getExpectedColumnGrandTotal(String column) {
        List<BigDecimal> totalValues = getValuesFromTable(columnMap.get(column))
                .stream()
                .distinct()
                .collect(Collectors.toList());

        getQuantityAndPriceValues("tr:nth-child(5) span", columnMap.get(column));

        // quantity isn't always 1 - factor this in
        // 1 - get quantities of components
        //ArrayList<BigDecimal> quantities = getValuesFromTable("td:nth-child(10)");
        //for (int i = 0; i < totalValues.size(); i++) {
        //    quantities.remove(i);
        //}

        // 2 - loop over them, if 2 (or greater than 1), then append to totalValues list
        //int totalValuesStaticSize = totalValues.size();
        //for (int i = 0; i < totalValuesStaticSize; i++) {
        //    int result = quantities.get(i).compareTo(new BigDecimal("2"));
        //    if (result == 0) {
        //        totalValues.add(totalValues.get(i));
        //    }
        //}

        //BigDecimal sum = totalValues.stream()
        //        .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BigDecimal("2");
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

    private void initialiseColumnMap() {
        columnMap.put("Cycle Time", "td:nth-child(24)");
        columnMap.put("Piece Part Cost", "td:nth-child(27)");
        columnMap.put("Fully Burdened Cost", "td:nth-child(30)");
        columnMap.put("Capital Investments", "td:nth-child(33)");
    }

    private void initialiseColumnTotalMap() {
        columnTotalMap.put("Cycle Time", "td:nth-child(25)");
        columnTotalMap.put("Piece Part Cost", "td:nth-child(28)");
        columnTotalMap.put("Fully Burdened Cost", "td:nth-child(31)");
        columnTotalMap.put("Capital Investments", "td:nth-child(34)");
    }
}
