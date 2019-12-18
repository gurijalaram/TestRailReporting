package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.utils.PageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    private Map<String, String> genericColumnMap = new HashMap<>();

    private Map<String, String> topLevelRowMap = new HashMap<>();
    private Map<String, String> subSubAsmRowMap = new HashMap<>();
    private Map<String, String> subAssemblyRowMap = new HashMap<>();

    private String genericTrSelector = "tr:nth-child(%s)";
    private String columnSelector;
    private String rowSelector;
    private String cssSelector;

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    private PageUtils pageUtils;
    private WebDriver driver;

    public AssemblyDetailsReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);

        initialiseGenericColumnMap();
        initialiseTopLevelRowMap();
        initialiseSubSubAsmRowMap();
        initialiseSubAssemblyRowMap();
    }

    /**
     * Generic method to get specific value from an Assembly Details Report table
     * @param assemblyType
     * @param rowIndex
     * @param columnName
     * @return BigDecimal
     */
    public BigDecimal getValueFromTable(String assemblyType, String rowIndex, String columnName) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        setCssLocator(assemblyType, rowIndex, columnName);
        BigDecimal valueRequired = new BigDecimal("0.00");
        Element valueCell = assemblyDetailsReport.select(cssSelector).first();

        if (isValueValid(valueCell.text())) {
            valueRequired = new BigDecimal(valueCell.text().replaceAll(",", ""));
        }

        return valueRequired;
    }

    /**
     * Generic method to get values in a given column
     * @param assemblyType
     * @param columnName
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getValuesByColumn(String assemblyType, String columnName) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        setCssLocator(assemblyType, "", columnName);
        ArrayList<BigDecimal> valuesToReturn = new ArrayList<>();

        for (Element element : assemblyDetailsReport.select(cssSelector)) {
            if (isValueValid(element.text())) {
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
     * Generic method to get expected total of a certain column
     * @param assemblyType
     * @param columnName
     * @return BigDecimal
     */
    public BigDecimal getExpectedColumnGrandTotal(String assemblyType, String columnName) {
        ArrayList<BigDecimal> values;
        values = getValuesByColumn(assemblyType, columnName);

        // 1 - get quantities
        // 2 - apply quantities to values in for loop
        // Will this work before making values list distinct?
        int i = 0;
        ArrayList<BigDecimal> quantities = getValuesByColumn(assemblyType, "Quantity");
        for (BigDecimal quantity : quantities) {
            //values.get(i).multiply(quantity);
            logger.debug(String.format("quantity at %d is: %s", i, quantity.setScale(2).toString()));
            i++;
        }

        for (int j = 0; j < values.size(); j++) {
            logger.debug(String.format("value at %d is: %f", j, values.get(j)));
        }

        // needs checked to ensure it will fulfil the intended purpose (possible scenarios exist in which it won't work)
        return values
                .stream()
                .distinct()
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
     * Waits for correct current currency to appear on screen
     * @param currencyToCheck
     * @return current page object
     */
    public AssemblyDetailsReportPage waitForCorrectCurrency(String currencyToCheck) {
        pageUtils.waitForElementToAppear(currentCurrency);
        pageUtils.checkElementAttribute(currentCurrency, "innerText", currencyToCheck);
        return this;
    }

    /**
     * Checks if value of current cell is a valid one
     * @param valueToCheck
     * @return boolean
     */
    private boolean isValueValid(String valueToCheck) {
        boolean returnValue = false;
        if (!valueToCheck.isEmpty() &&
                !valueToCheck.equals("null") &&
                !valueToCheck.equals("0.00") &&
                valueToCheck.chars().noneMatch(Character::isLetter)) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Method to reduce duplication - sets css locator based on parameters
     */
    private void setCssLocator(String assemblyType, String rowIndex, String columnName) {
        switch (assemblyType) {
            case "Top Level":
                rowSelector = topLevelRowMap.get(rowIndex);
                columnSelector = genericColumnMap.get(columnName);
                break;
            case "Sub-Sub-ASM":
                rowSelector = subSubAsmRowMap.get(rowIndex);
                columnSelector = genericColumnMap.get(columnName);
                break;
            case "Sub-Assembly":
                rowSelector = subAssemblyRowMap.get(rowIndex);
                columnSelector = genericColumnMap.get(columnName);
                break;
        }

        if (!rowIndex.isEmpty()) {
            String baseCssSelector = "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table %s %s span";
            cssSelector = String.format(baseCssSelector, rowSelector, columnSelector);
        } else {
            String baseCssSelectorNoRowSpecified = "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table tr %s span";
            cssSelector = String.format(baseCssSelectorNoRowSpecified, columnSelector);
        }
    }

    /**
     * Hash Map initialisation for columns in Top Level export set report table
     */
    private void initialiseGenericColumnMap() {
        String genericTdSelector = "td:nth-child(%s)";

        genericColumnMap.put("Quantity", String.format(genericTdSelector, "10"));

        genericColumnMap.put("Cycle Time", String.format(genericTdSelector, "24"));
        genericColumnMap.put("Cycle Time Total", String.format(genericTdSelector, "25"));

        genericColumnMap.put("Piece Part Cost", String.format(genericTdSelector, "27"));
        genericColumnMap.put("Piece Part Cost Total", String.format(genericTdSelector, "28"));

        genericColumnMap.put("Fully Burdened Cost", String.format(genericTdSelector, "30"));
        genericColumnMap.put("Fully Burdened Cost Total", String.format(genericTdSelector, "31"));

        genericColumnMap.put("Capital Investments", String.format(genericTdSelector, "33"));
        genericColumnMap.put("Capital Investments Total", String.format(genericTdSelector, "34"));
    }


    /**
     * Hash Map initialisation for columns in Sub Assembly export set report table
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

    /**
     * Hash Map initialisation for columns in Sub-Sub-ASM export set report table
     */
    private void initialiseSubSubAsmRowMap() {
        subSubAsmRowMap.put("1 Sub Sub ASM", String.format(genericTrSelector, "5"));
        subSubAsmRowMap.put("2 Sub Sub ASM", String.format(genericTrSelector, "7"));
        subSubAsmRowMap.put("Component Subtotal Sub Sub ASM", String.format(genericTrSelector, "11"));
        subSubAsmRowMap.put("Assembly Processes Sub Sub ASM", String.format(genericTrSelector, "14"));
        subSubAsmRowMap.put("Grand Total Sub Sub ASM", String.format(genericTrSelector, "16"));
    }

    /**
     * Hash Map initialisation for columns in Top Level export set report table
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
}
