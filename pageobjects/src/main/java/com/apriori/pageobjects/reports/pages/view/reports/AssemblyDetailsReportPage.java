package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.pages.view.objects.TableRowNumbers;
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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssemblyDetailsReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    private Map<String, String> columnTotalMap = new HashMap<>();
    private Map<String, String> columnMap = new HashMap<>();
    private Map<String, String> valueMap = new HashMap<>();
    private Map<Integer, String> rowMap = new HashMap<>();

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
        initialiseColumnTotalMap();
        initialiseColumnMap();
        initialiseValueMap();
        initialiseRowMap();
    }

    private ArrayList<BigDecimal> getValuesFromTable(String cssSelector) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        ArrayList<BigDecimal> valuesRetrieved = new ArrayList<>();

        for (Element column : assemblyDetailsReport.select("table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table tr")) {
            if (!column.select(cssSelector).text().isEmpty() && !column.select(cssSelector).text().equals("null")
                && !column.select(cssSelector).text().chars().anyMatch(Character::isLetter) && !column.select(cssSelector).text().equals("0.00")) {
                valuesRetrieved.add(new BigDecimal(column.select(cssSelector).text().replaceAll(",", "")));
            }
        }
        return valuesRetrieved;
    }

    public BigDecimal getQuantityOrPriceValue(int rowIndex, String valueName) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        TableRowNumbers rowValues = new TableRowNumbers();

        Element row = assemblyDetailsReport.select(String.format("table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table %s %s", rowMap.get(rowIndex), valueMap.get(valueName))).first();
        BigDecimal retVal = new BigDecimal("0.00");

        if (valueName.equals("Cycle Time (s)")) {
            retVal = getCycleTime(rowValues, row);
        } else if (valueName.equals("Piece Part Cost")) {
            retVal = getPiecePartCost(rowValues, row);
        } else if (valueName.equals("Fully Burdened Cost")) {
            retVal = getFullyBurdenedCost(rowValues, row);
        } else {
            retVal = getCapitalInvestments(rowValues, row);
        }

        return retVal;
    }

    public Integer getQuantity(int rowIndex) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        TableRowNumbers rowValues = new TableRowNumbers();

        Element row = assemblyDetailsReport.select(String.format("table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table %s %s", rowMap.get(rowIndex), valueMap.get("Quantity"))).first();
        return Integer.parseInt(row.text());
    }

    public BigDecimal getCycleTime(TableRowNumbers nums, Element row) {
        nums.setCycleTime(new BigDecimal(row.text().replaceAll(",", "")));
        return nums.getCycleTime();
    }

    public BigDecimal getPiecePartCost(TableRowNumbers nums, Element row) {
        nums.setPiecePartCost(new BigDecimal(row.text().replaceAll(",", "")));
        return nums.getPiecePartCost();
    }

    public BigDecimal getFullyBurdenedCost(TableRowNumbers nums, Element row) {
        nums.setFullyBurdenedCost(new BigDecimal(row.text().replaceAll(",", "")));
        return nums.getFullyBurdenedCost();
    }

    public BigDecimal getCapitalInvestments(TableRowNumbers nums, Element row) {
        nums.setCapitalInvestments(new BigDecimal(row.text().replaceAll(",", "")));
        return nums.getCapitalInvestments();
    }

    public BigDecimal getActualColumnGrandTotal(String column) {
        ArrayList<BigDecimal> valuesRetrieved = getValuesFromTable(columnTotalMap.get(column));
        return valuesRetrieved.get(0);
    }

    public BigDecimal getExpectedColumnGrandTotal(String valueName) {
        ArrayList<Integer> indexes = new ArrayList<>();
        indexes.add(1);
        indexes.add(2);
        indexes.add(4);
        indexes.add(6);
        indexes.add(8);
        indexes.add(9);

        List<BigDecimal> values = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            values.add(getQuantityOrPriceValue(indexes.get(i), valueName));
        }

        // if qty is more than 1, multiply by that, then add
        //List<BigDecimal> finalValsToAdd = new ArrayList<>();

        //int i = 0;
        //for (BigDecimal val : values) {
        //    if (getQuantityOrPriceValue(indexes.get(i), "Quantity").compareTo(new BigDecimal("2")) == 0) {
        //        finalValsToAdd.add(val.multiply(new BigDecimal(getQuantity(indexes.get(i)))));
        //    } else {
        //        finalValsToAdd.add(val);
        //    }
        //    i++;
        //}

        List<BigDecimal> distinctTotalValues = values
                .stream()
                .distinct()
                .collect(Collectors.toList());

        return distinctTotalValues
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    private void initialiseValueMap() {
        valueMap.put("Quantity", "td:nth-child(10)");
        valueMap.put("Cycle Time (s)", "td:nth-child(24)");
        valueMap.put("Piece Part Cost", "td:nth-child(27)");
        valueMap.put("Fully Burdened Cost", "td:nth-child(30)");
        valueMap.put("Capital Investments", "td:nth-child(33)");
    }

    private void initialiseRowMap() {
        rowMap.put(1, "tr:nth-child(5)");
        rowMap.put(2, "tr:nth-child(7)");
        rowMap.put(4, "tr:nth-child(11)");
        rowMap.put(6, "tr:nth-child(15)");
        rowMap.put(8, "tr:nth-child(17)");
        rowMap.put(9, "tr:nth-child(19)");
    }
}
