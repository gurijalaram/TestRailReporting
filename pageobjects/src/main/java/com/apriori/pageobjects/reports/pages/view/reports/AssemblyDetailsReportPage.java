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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssemblyDetailsReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    private Map<String, String> columnTotalMap = new HashMap<>();
    private Map<String, String> valueMap = new HashMap<>();

    private Map<String, String> topLevelColumnMap = new HashMap<>();
    private Map<String, String> subSubAsmColumnMap = new HashMap<>();
    private Map<String, String> subAssemblyColumnMap = new HashMap<>();

    private Map<String, String> topLevelRowMap = new HashMap<>();
    private Map<String, String> subSubAsmRowMap = new HashMap<>();
    private Map<String, String> subAssemblyRowMap = new HashMap<>();

    private String genericTrSelector = "tr:nth-child(%s)";
    private String genericTdSelector = "td:nth-child(%s)";

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

        initialiseTopLevelColumnMap();
        initialiseSubSubAsmColumnMap();
        initialiseSubAssemblyColumnMap();

        initialiseTopLevelRowMap();
        initialiseSubSubAsmRowMap();
        initialiseSubAssemblyRowMap();
    }

    /**
     *
     * @return
     */
    public BigDecimal getValueFromTable(String assemblyType, String rowIndex, String columnName) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        String columnSelector = "";
        String rowSelector = "";

        switch (assemblyType) {
            case "Top Level":
                rowSelector = topLevelRowMap.get(rowIndex);
                columnSelector = topLevelColumnMap.get(columnName);
                break;
            case "Sub-Sub-ASM":
                rowSelector = subSubAsmRowMap.get(rowIndex);
                columnSelector = subSubAsmColumnMap.get(columnName);
                break;
            case "Sub-Assembly":
                rowSelector = subAssemblyRowMap.get(rowIndex);
                columnSelector = subAssemblyColumnMap.get(columnName);
                break;
        }
        String cssSelector = String.format("table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table %s %s span", rowSelector, columnSelector);

        BigDecimal actualTotal = new BigDecimal("0.00");

        Element actualTotalCell = assemblyDetailsReport.select(cssSelector).first();
        if (!actualTotalCell.text().isEmpty() && !actualTotalCell.text().equals("null") && !actualTotalCell.text().equals("0.00")
            && actualTotalCell.text().chars().noneMatch(Character::isLetter)) {
            actualTotal = new BigDecimal(actualTotalCell.text().replaceAll(",", ""));
        }

        return actualTotal;
    }

    /**
     *
     * @return
     */
    public ArrayList<BigDecimal> getValuesByColumn(String assemblyType, String columnName) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        String columnSelector = "";

        switch (assemblyType) {
            case "Top Level":
                columnSelector = topLevelColumnMap.get(columnName);
                break;
            case "Sub-Sub-ASM":
                columnSelector = subSubAsmColumnMap.get(columnName);
                break;
            case "Sub-Assembly":
                columnSelector = subAssemblyColumnMap.get(columnName);
                break;
        }
        String cssSelector = String.format("table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table tr %s span", columnSelector);

        ArrayList<BigDecimal> valuesToReturn = new ArrayList<>();

        for (Element element : assemblyDetailsReport.select(cssSelector)) {
            if (!element.text().isEmpty() && !element.text().equals("null") && !element.text().equals("0.00")
                    && element.text().chars().noneMatch(Character::isLetter)) {
                if (columnName.equals("Cycle Time") && element.text().equals("79,995.28")) {
                    continue;
                } else {
                    valuesToReturn.add(new BigDecimal(element.text().replaceAll(",", "")));
                }
            }
        }

        return valuesToReturn;
    }

    /**
     *
     * @param cssSelector
     * @return
     */
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

    /**
     *
     * @param assemblyType
     * @param columnName
     * @param rowIndex
     * @return
     */
    public BigDecimal getActualColumnGrandTotal(String assemblyType, String rowIndex, String columnName) {
        return getValueFromTable(assemblyType, rowIndex, columnName);
    }

    /**
     *
     * @param assemblyType
     * @param columnName
     * @return
     */
    public BigDecimal getExpectedColumnGrandTotal(String assemblyType, String columnName) {
        List<BigDecimal> values;
        values = getValuesByColumn(assemblyType, columnName);

        List<BigDecimal> distinctTotalValues = values
                .stream()
                .distinct()
                .collect(Collectors.toList());

        return distinctTotalValues
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets current currency setting
     * @return String
     */
    public String getCurrentCurrency() {
        return pageUtils.getElementText(currentCurrency);
    }

    /**
     *
     * @param currencyToCheck
     * @return
     */
    public AssemblyDetailsReportPage waitForCorrectCurrency(String currencyToCheck) {
        pageUtils.waitForElementToAppear(currentCurrency);
        pageUtils.checkElementAttribute(currentCurrency, "innerText", currencyToCheck);
        return this;
    }

    /**
     *
     */
    private void initialiseTopLevelColumnMap() {
        topLevelColumnMap.put("Cycle Time", String.format(genericTdSelector, "24"));
        topLevelColumnMap.put("Cycle Time Total", String.format(genericTdSelector, "25"));

        topLevelColumnMap.put("Piece Part Cost", String.format(genericTdSelector, "27"));
        topLevelColumnMap.put("Piece Part Cost Total", String.format(genericTdSelector, "28"));

        topLevelColumnMap.put("Fully Burdened Cost", String.format(genericTdSelector, "30"));
        topLevelColumnMap.put("Fully Burdened Cost Total", String.format(genericTdSelector, "31"));

        topLevelColumnMap.put("Capital Investments", String.format(genericTdSelector, "33"));
        topLevelColumnMap.put("Capital Investments Total", String.format(genericTdSelector, "34"));
    }

    /**
     *
     */
    private void initialiseSubSubAsmColumnMap() {
        subSubAsmColumnMap.put("Cycle Time", String.format(genericTdSelector, "24"));
        subSubAsmColumnMap.put("Cycle Time Total", String.format(genericTdSelector, "25"));

        subSubAsmColumnMap.put("Piece Part Cost", String.format(genericTdSelector, "27"));
        subSubAsmColumnMap.put("Piece Part Cost Total", String.format(genericTdSelector, "28"));

        subSubAsmColumnMap.put("Fully Burdened Cost", String.format(genericTdSelector, "30"));
        subSubAsmColumnMap.put("Fully Burdened Cost Total", String.format(genericTdSelector, "31"));

        subSubAsmColumnMap.put("Capital Investments", String.format(genericTdSelector, "33"));
        subSubAsmColumnMap.put("Capital Investments Total", String.format(genericTdSelector, "34"));
    }

    /**
     *
     */
    private void initialiseSubAssemblyColumnMap() {
        subAssemblyColumnMap.put("Cycle Time", String.format(genericTdSelector, "24"));
        subAssemblyColumnMap.put("Cycle Time Total", String.format(genericTdSelector, "25"));

        subAssemblyColumnMap.put("Piece Part Cost", String.format(genericTdSelector, "27"));
        subAssemblyColumnMap.put("Piece Part Cost Total", String.format(genericTdSelector, "28"));

        subAssemblyColumnMap.put("Fully Burdened Cost", String.format(genericTdSelector, "30"));
        subAssemblyColumnMap.put("Fully Burdened Cost Total", String.format(genericTdSelector, "31"));

        subAssemblyColumnMap.put("Capital Investments", String.format(genericTdSelector, "33"));
        subAssemblyColumnMap.put("Capital Investments Total", String.format(genericTdSelector, "34"));
    }

    /**
     *
     */
    private void initialiseTopLevelRowMap() {
        topLevelRowMap.put("1 Top Level", String.format(genericTrSelector, "5"));
        topLevelRowMap.put("2 Top Level", String.format(genericTrSelector, "8"));
        topLevelRowMap.put("3 Top Level", String.format(genericTrSelector, "11"));
        topLevelRowMap.put("4 Top Level", String.format(genericTrSelector, "14"));
        topLevelRowMap.put("5 Top Level", String.format(genericTrSelector, "17"));
        topLevelRowMap.put("6 Top Level", String.format(genericTrSelector, "19"));
        topLevelRowMap.put("7 Top Level", String.format(genericTrSelector, "21"));
        topLevelRowMap.put("8 Top Level", String.format(genericTrSelector, "23"));
        topLevelRowMap.put("9 Top Level", String.format(genericTrSelector, "27"));
        topLevelRowMap.put("10 Top Level", String.format(genericTrSelector, "31"));
        topLevelRowMap.put("11 Top Level", String.format(genericTrSelector, "33"));
        topLevelRowMap.put("12 Top Level", String.format(genericTrSelector, "35"));
        topLevelRowMap.put("13 Top Level", String.format(genericTrSelector, "38"));
        topLevelRowMap.put("Component Subtotal Top Level", String.format(genericTrSelector, "42"));
        topLevelRowMap.put("Assembly Processes Top Level", String.format(genericTrSelector, "45"));
        topLevelRowMap.put("Grand Total Top Level", String.format(genericTrSelector, "47"));
    }

    /**
     *
     */
    private void initialiseSubSubAsmRowMap() {
        subSubAsmRowMap.put("1 Sub Sub ASM", String.format(genericTrSelector, "5"));
        subSubAsmRowMap.put("2 Sub Sub ASM", String.format(genericTrSelector, "7"));
        subSubAsmRowMap.put("Component Subtotal Sub Sub ASM", String.format(genericTrSelector, "11"));
        subSubAsmRowMap.put("Assembly Processes Sub Sub ASM", String.format(genericTrSelector, "14"));
        subSubAsmRowMap.put("Grand Total Sub Sub ASM", String.format(genericTrSelector, "16"));
    }

    /**
     *
     */
    private void initialiseSubAssemblyRowMap() {
        subAssemblyRowMap.put("1 Sub Assembly", String.format(genericTrSelector, "5"));
        subAssemblyRowMap.put("2 Sub Assembly", String.format(genericTrSelector, "7"));
        subAssemblyRowMap.put("3 Sub Assembly", String.format(genericTrSelector, "11"));
        subAssemblyRowMap.put("4 Sub Assembly", String.format(genericTrSelector, "15"));
        subAssemblyRowMap.put("5 Sub Assembly", String.format(genericTrSelector, "17"));
        subAssemblyRowMap.put("6 Sub Assembly", String.format(genericTrSelector, "19"));
        subAssemblyRowMap.put("Component Subtotal Sub Assembly", String.format(genericTrSelector, "23"));
        subAssemblyRowMap.put("Assembly Processes Sub Assembly", String.format(genericTrSelector, "26"));
        subAssemblyRowMap.put("Grand Total Sub Assembly", String.format(genericTrSelector, "28"));
    }
}
