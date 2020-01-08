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

public class AssemblyDetailsReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    private Map<String, String> genericColumnMap = new HashMap<>();

    private Map<String, String> topLevelRowMap = new HashMap<>();
    private Map<String, String> subSubAsmRowMap = new HashMap<>();
    private Map<String, String> subAssemblyRowMap = new HashMap<>();

    List<BigDecimal> refinedQuantities = new ArrayList<>();

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
                valuesToReturn.add(new BigDecimal(element.text().replaceAll(",", "")));
            }
        }

        if (columnName.equals("Cycle Time")) {
            valuesToReturn.remove(valuesToReturn.size() - 1);
        }

        return valuesToReturn;
    }

    public ArrayList<BigDecimal> getEmptyQuantities(String assemblyType, String columnName) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        setCssLocator(assemblyType, "", columnName);
        ArrayList<BigDecimal> valuesToReturn = new ArrayList<>();

        for (Element element : assemblyDetailsReport.select(cssSelector)) {
            valuesToReturn.add(new BigDecimal("0"));
        }

        return valuesToReturn;
    }

    /**
     * Gets all level values
     * @param assemblyType
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getLevelValues(String assemblyType) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        setCssLocator(assemblyType, "", "Level");
        ArrayList<BigDecimal> valuesToReturn = new ArrayList<>();

        for (Element element : assemblyDetailsReport.select(cssSelector)) {
            if (isValueValid(element.text())) {
                valuesToReturn.add(new BigDecimal(element.text().replace(".", "")));
            }
        }

        return valuesToReturn;
    }

    /**
     * Gets all part numbers
     * @param assemblyType
     * @return List of Strings
     */
    private List<String> checkPartNumber(String assemblyType) {
        Document assemblyDetailsReport = Jsoup.parse(driver.getPageSource());
        setCssLocator(assemblyType, "", "Part Number Main");
        List<String> mainPartNums = new ArrayList<>();
        ArrayList<String> secondaryPartNums = new ArrayList<>();

        for (Element element : assemblyDetailsReport.select(cssSelector)) {
            if (!element.text().isEmpty() && !element.text().equals("Part Number") && !element.text().equals("GRAND TOTAL") && !element.text().equals("Assembly Processes")) {
                mainPartNums.add(element.text());
            }
        }
        //mainPartNums = mainPartNums.stream().distinct().collect(Collectors.toList());

        for (int i = 0; i < mainPartNums.size(); i++) {
            mainPartNums.remove(i);
        }

        setCssLocator(assemblyType, "", "Part Number Secondary");
        for (Element element : assemblyDetailsReport.select(cssSelector)) {
            if (element.text().chars().noneMatch(Character::isDigit) &&
                element.text().equals("Assembly Process")) {
                secondaryPartNums.add(element.text());
            }
        }

        mainPartNums.add(0, secondaryPartNums.get(0));
        if (assemblyType.equals("Sub-Assembly")) {
            mainPartNums.add(7, secondaryPartNums.get(1));
        } else if (assemblyType.equals("Top-Level")) {
            BigDecimal ab = new BigDecimal("0");
        }

        return mainPartNums;
    }

    /**
     * Generic method to get expected total of a certain column
     * @param assemblyType
     * @param columnName
     * @return BigDecimal
     */
    private List<BigDecimal> getColumnValuesForSum(String assemblyType, String columnName) {
        ArrayList<BigDecimal> values;
        values = getValuesByColumn(assemblyType, columnName);

        ArrayList<BigDecimal> totalValuesList;
        totalValuesList = getValuesByColumn(assemblyType, columnName + " Total");

        BigDecimal firstValue = totalValuesList.get(0);
        values.add(0, firstValue);

        if (assemblyType.equals("Sub-Assembly")) {
            BigDecimal secondValue = totalValuesList.get(1);
            values.add(7, secondValue);
        } else if (assemblyType.equals("Top Level")) {
            BigDecimal abcd = new BigDecimal("0");
        }

        return values;
    }

    /**
     * Gets expected Cycle Time grand total
     * @param assemblyType
     * @param columnName
     * @return BigDecimal
     */
    public BigDecimal getExpectedCTGrandTotal(String assemblyType, String columnName) {
        List<BigDecimal> allValues = getColumnValuesForSum(assemblyType, columnName);
        ArrayList<BigDecimal> levels = getLevelValues(assemblyType);
        List<BigDecimal> trimmedValueList = new ArrayList<>();
        if (assemblyType.equals("Sub-Assembly")) {
            trimmedValueList = checkCTSubAssemblyValues(assemblyType, levels, allValues);
        } else if (assemblyType.equals("Sub-Sub-ASM")) {
            trimmedValueList = checkCTSubSubAsmValues(assemblyType, levels, allValues);
        } else {
            trimmedValueList = checkCTTopLevelValues(assemblyType, levels, allValues);
        }
        return trimmedValueList
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     *
     * @return
     */
    public BigDecimal getExpectedPPCGrandTotal(String assemblyType, String columnName) {
        List<BigDecimal> allValues = getColumnValuesForSum(assemblyType, columnName);
        ArrayList<BigDecimal> levels = getLevelValues(assemblyType);
        List<BigDecimal> quantityList = checkQuantityList(assemblyType, levels, allValues);
        List<BigDecimal> trimmedValueList = checkPPCValues(assemblyType, levels, allValues, quantityList);
        List<BigDecimal> finalValues = applyQuantities(trimmedValueList);
        return finalValues
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     *
     * @return
     */
    public BigDecimal getExpectedFBCGrandTotal(String assemblyType, String columnName) {
        // sum = level 1 assembly process + sub-sub-asm + l2 assembly process
        List<BigDecimal> allValues = getColumnValuesForSum(assemblyType, columnName);
        ArrayList<BigDecimal> levels = getLevelValues(assemblyType);
        List<BigDecimal> quantityList = checkQuantityList(assemblyType, levels, allValues);
        List<BigDecimal> trimmedValueList = checkPPCValues(assemblyType, levels, allValues, quantityList);
        List<BigDecimal> finalValues = applyQuantities(trimmedValueList);
        return finalValues
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     *
     * @return
     */
    public BigDecimal getExpectedCIGrandTotal(String assemblyType, String columnName) {
        List<BigDecimal> allValues = getColumnValuesForSum(assemblyType, columnName);
        ArrayList<BigDecimal> levels = getLevelValues(assemblyType);
        List<BigDecimal> trimmedValueList = new ArrayList<>();

        if (assemblyType.equals("Sub-Assembly")) {
            trimmedValueList = checkCISubAssemblyValues(assemblyType, levels, allValues);
        } else if (assemblyType.equals("Sub-Sub-ASM")) {
            // do stuff
            trimmedValueList = checkCISubSubAsmValues(assemblyType, levels, allValues);
        }
        return trimmedValueList
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<BigDecimal> checkCTSubAssemblyValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = getPartNums(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();
        List<String> partNums2 = new ArrayList<>();

        for (int i = 0; i < partNums.size(); i++) {
            if (partNums.get(i).equals("Assembly Process") || partNums.get(i).equals("SUB-SUB-ASM")) {
                if (values.get(i).compareTo(new BigDecimal("0.00")) != 0) {
                    partNums2.add(partNums.get(i));
                    trimmedValues.add(values.get(i));
                }
            }
        }
        return trimmedValues;
    }

    private List<BigDecimal> checkCTSubSubAsmValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = getPartNums(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();
        List<String> partNums2 = new ArrayList<>();

        for (int i = 0; i < partNums.size(); i++) {
            if (levels.get(i).compareTo(new BigDecimal("1")) == 0) {
                partNums2.add(partNums.get(i));
                trimmedValues.add(values.get(i));
            }
        }
        return trimmedValues;
    }

    private List<BigDecimal> checkCTTopLevelValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = getPartNums(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();
        List<String> partNums2 = new ArrayList<>();

        for (int i = 0; i < partNums.size(); i++) {
            if (!partNums.get(i).equals("Assembly Process")) {
                if (levels.get(i).compareTo(new BigDecimal("1")) == 0) {
                    partNums2.add(partNums.get(i));
                    trimmedValues.add(values.get(i));
                }
            }
        }
        return trimmedValues;
    }

    private List<BigDecimal> checkPPCValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values, List<BigDecimal> quantities) {
        List<String> partNums = getPartNums(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();
        List<String> partNums2 = new ArrayList<>();

        if (!refinedQuantities.isEmpty()) {
            refinedQuantities.clear();
        }

        for (int i = 0; i < partNums.size(); i++) {
            if (levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0) {
                partNums2.add(partNums.get(i));
                trimmedValues.add(values.get(i));
                refinedQuantities.add(quantities.get(i));
            }
        }
        return trimmedValues;
    }

    private List<BigDecimal> checkCISubAssemblyValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = getPartNums(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();
        //List<String> partNums2 = new ArrayList<>();

        for (int i = 0; i < partNums.size(); i++) {
            if (partNums.get(i).equals("Assembly Process") || partNums.get(i).equals("SUB-SUB-ASM")) {
                if (levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0) {
                    //partNums2.add(partNums.get(i));
                    trimmedValues.add(values.get(i));
                }
            }
        }
        return trimmedValues;
    }

    private List<BigDecimal> checkCISubSubAsmValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = getPartNums(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();
        //List<String> partNums2 = new ArrayList<>();

        for (int i = 0; i < partNums.size(); i++) {
            if (levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0) {
                //partNums2.add(partNums.get(i));
                trimmedValues.add(values.get(i));
            }
        }
        return trimmedValues;
    }

    private List<String> getPartNums(String assemblyType) {
        return checkPartNumber(assemblyType);
    }

    /**
     * Gets quantity list, trims it to size and multiplies by value where necessary
     * @param assemblyType
     * @param values
     * @param levels
     * @return
     */
    private List<BigDecimal> checkQuantityList(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<BigDecimal> quantities = getValuesByColumn(assemblyType, "Quantity");
        List<BigDecimal> quantitiesEmpty = getEmptyQuantities(assemblyType,"Quantity Empty");

        quantities.add(0, quantitiesEmpty.get(0));

        if (assemblyType.equals("Sub-Assembly")) {
            quantities.add(7, quantitiesEmpty.get(1));
        } else if (assemblyType.equals("Top-Level")) {
            BigDecimal ab = new BigDecimal("0");
        }

        //List<BigDecimal> refinedQuantities = new ArrayList<>();
        //List<String> partNums = getPartNums(assemblyType);

        //for (int n = 0; n < values.size(); n++) {
        //    if (partNums.get(n).equals("Assembly Process") || partNums.get(n).equals("SUB-SUB-ASM")) {
        //        if (levels.get(n).compareTo(new BigDecimal("1")) == 0 && values.get(n).compareTo(new BigDecimal("0.00")) != 0) {
        //            refinedQuantities.add(quantities.get(n));
        //        }
        //    }
        //}
        return quantities;
    }

    private List<BigDecimal> applyQuantities(List<BigDecimal> values) {
        List<BigDecimal> finalValues = new ArrayList<>();

        for (int i = 0; i < refinedQuantities.size(); i++) {
            if (refinedQuantities.get(i).compareTo(new BigDecimal("0.00")) != 0) {
                BigDecimal newVal = values.get(i).multiply(refinedQuantities.get(i));
                finalValues.add(newVal);
            } else {
                BigDecimal newVal = values.get(i);
                finalValues.add(newVal);
            }
        }
        return finalValues;
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
        if (!valueToCheck.isEmpty() && valueToCheck.chars().noneMatch(Character::isLetter)) {
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

        genericColumnMap.put("Level", String.format(genericTdSelector, "2"));
        genericColumnMap.put("Part Number Main", String.format(genericTdSelector, "5"));
        genericColumnMap.put("Part Number Secondary", String.format(genericTdSelector, "6"));
        genericColumnMap.put("Quantity", String.format(genericTdSelector, "10"));
        genericColumnMap.put("Quantity Empty", String.format(genericTdSelector, "11"));

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
        subAssemblyRowMap.put("Component Subtotal Sub Assembly", String.format(genericTrSelector, "22"));
        subAssemblyRowMap.put("Assembly Processes Sub Assembly", String.format(genericTrSelector, "25"));
        subAssemblyRowMap.put("Grand Total Sub Assembly", String.format(genericTrSelector, "27"));
    }

    /**
     * Hash Map initialisation for columns in Sub-Sub-ASM export set report table
     */
    private void initialiseSubSubAsmRowMap() {
        subSubAsmRowMap.put("1 Sub Sub ASM", String.format(genericTrSelector, "4"));
        subSubAsmRowMap.put("2 Sub Sub ASM", String.format(genericTrSelector, "6"));
        subSubAsmRowMap.put("Component Subtotal Sub Sub ASM", String.format(genericTrSelector, "10"));
        subSubAsmRowMap.put("Assembly Processes Sub Sub ASM", String.format(genericTrSelector, "13"));
        subSubAsmRowMap.put("Grand Total Sub Sub ASM", String.format(genericTrSelector, "15"));
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
        topLevelRowMap.put("Grand Total Top Level", String.format(genericTrSelector, "43"));
    }
}
