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
import java.util.stream.IntStream;

public class AssemblyDetailsReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    private Map<String, String> genericColumnMap = new HashMap<>();

    private Map<String, String> topLevelRowMap = new HashMap<>();
    private Map<String, String> subSubAsmRowMap = new HashMap<>();
    private Map<String, String> subAssemblyRowMap = new HashMap<>();

    List<BigDecimal> refinedQuantities = new ArrayList<>();

    private String genericTrSelector = "tr:nth-child(%s)";
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

        initialiseSubAssemblyRowMap();
        initialiseGenericColumnMap();
        initialiseSubSubAsmRowMap();
        initialiseTopLevelRowMap();
    }

    /**
     * Generic method to get specific value from an Assembly Details Report table
     * @param assemblyType
     * @param rowIndex
     * @param columnName
     * @return BigDecimal
     */
    public BigDecimal getValueFromTable(String assemblyType, String rowIndex, String columnName) {
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, rowIndex, columnName);
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
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", columnName);
        List<Element> valueElements = assemblyDetailsReport.select(cssSelector);

        return IntStream.range(0, valueElements.size()).filter(i -> isValueValid(valueElements.get(i).text()) || columnName.equals("Cycle Time") && i <= (valueElements.size() - 2)).mapToObj(i -> new BigDecimal(valueElements.get(i).text().replaceAll(",", ""))).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets quantities that are blank and returns them as 0
     * @param assemblyType
     * @param columnName
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getEmptyQuantities(String assemblyType, String columnName) {
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", columnName);
        List<Element> elementList = assemblyDetailsReport.select(cssSelector);

        return IntStream.range(0, elementList.size()).mapToObj(i -> new BigDecimal("0")).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all level values
     * @param assemblyType
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getLevelValues(String assemblyType) {
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", "Level");
        return assemblyDetailsReport.select(cssSelector).stream().filter(element -> isValueValid(element.text())).map(element -> new BigDecimal(element.text().replace(".", ""))).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all part numbers
     * @param assemblyType
     * @return List of Strings
     */
    private List<String> checkPartNumber(String assemblyType) {
        // Gets main part numbers
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", "Part Number Main");
        List<String> mainPartNums = new ArrayList<>();
        ArrayList<String> secondaryPartNums;

        if (assemblyType.equals("Top Level")) {
            mainPartNums = assemblyDetailsReport.select(cssSelector).stream().filter(element -> !element.text().isEmpty() && !element.text().equals("Part Number")
                && !element.text().equals("GRAND TOTAL")).map(Element::text).collect(Collectors.toList());
        } else {
            for (Element element : assemblyDetailsReport.select(cssSelector)) {
                if (!element.text().isEmpty() && !element.text().equals("Part Number")
                        && !element.text().equals("GRAND TOTAL")
                        && !element.text().equals("Assembly Processes")) {
                    mainPartNums.add(element.text());
                }
            }
        }

        // Gets secondary part numbers and integrates them into the list
        setCssLocator(assemblyType, "", "Part Number Secondary");
        secondaryPartNums = assemblyDetailsReport.select(cssSelector).stream().filter(element -> element.text().replace(" ", "").chars().allMatch(Character::isLetter) &&
            element.text().equals("Assembly Process")).map(Element::text).collect(Collectors.toCollection(ArrayList::new));

        mainPartNums.add(0, secondaryPartNums.get(0));
        if (assemblyType.equals("Sub-Assembly")) {
            mainPartNums.add(7, secondaryPartNums.get(1));
        } else if (assemblyType.equals("Top Level")) {
            mainPartNums.add(8, secondaryPartNums.get(1));
            mainPartNums.add(15, secondaryPartNums.get(2));
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
        // Gets all values from both columns in the HTML and merges them
        // One column on table is two columns in the markup
        ArrayList<BigDecimal> values;
        ArrayList<BigDecimal> totalValuesList;
        values = getValuesByColumn(assemblyType, columnName);
        totalValuesList = getValuesByColumn(assemblyType, columnName + " Total");

        BigDecimal firstValue = totalValuesList.get(0);
        BigDecimal secondValue = totalValuesList.get(1);
        values.add(0, firstValue);

        if (assemblyType.equals("Sub-Assembly")) {
            values.add(7, secondValue);
        } else if (assemblyType.equals("Top Level")) {
            BigDecimal thirdValue = totalValuesList.get(2);
            values.add(8, secondValue);
            values.add(15, thirdValue);
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
        List<BigDecimal> trimmedValueList;

        if (assemblyType.equals("Sub-Assembly")) {
            trimmedValueList = checkCTSubAssemblyValues(assemblyType, allValues);
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
     * Gets expected Piece Part Cost grand total
     * @return BigDecimal
     */
    public BigDecimal getExpectedPPCGrandTotal(String assemblyType, String columnName) {
        List<BigDecimal> allValues = getColumnValuesForSum(assemblyType, columnName);
        List<BigDecimal> levels = getLevelValues(assemblyType);
        List<BigDecimal> quantityList = checkQuantityList(assemblyType);

        List<BigDecimal> trimmedValueList = checkPPCValues(assemblyType, levels, allValues, quantityList);
        List<BigDecimal> finalValues = applyQuantities(trimmedValueList);

        return finalValues
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets expected Fully Burdened Cost grand total
     * @return BigDecimal
     */
    public BigDecimal getExpectedFBCGrandTotal(String assemblyType, String columnName) {
        List<BigDecimal> allValues = getColumnValuesForSum(assemblyType, columnName);
        ArrayList<BigDecimal> levels = getLevelValues(assemblyType);
        List<BigDecimal> quantityList = checkQuantityList(assemblyType);

        List<BigDecimal> trimmedValueList = checkPPCValues(assemblyType, levels, allValues, quantityList);
        List<BigDecimal> finalValues = applyQuantities(trimmedValueList);

        return finalValues
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets expected Capital Investment grand total
     * @return BigDecimal
     */
    public BigDecimal getExpectedCIGrandTotal(String assemblyType, String columnName) {
        List<BigDecimal> allValues = getColumnValuesForSum(assemblyType, columnName);
        ArrayList<BigDecimal> levels = getLevelValues(assemblyType);
        List<BigDecimal> trimmedValueList;

        if (assemblyType.equals("Sub-Assembly")) {
            trimmedValueList = checkCISubAssemblyValues(assemblyType, levels, allValues);
        } else if (assemblyType.equals("Sub-Sub-ASM")) {
            trimmedValueList = checkCISubSubAsmValues(assemblyType, levels, allValues);
        } else {
            trimmedValueList = checkCITopLevelValues(assemblyType, levels, allValues);
        }

        return trimmedValueList
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets Cycle Time values for Sub Assembly grand total
     * @param assemblyType
     * @param values
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCTSubAssemblyValues(String assemblyType, List<BigDecimal> values) {
        List<BigDecimal> levelValues = getLevelValues(assemblyType);
        return IntStream.range(0, levelValues.size()).filter(i -> levelValues.get(i).compareTo(new BigDecimal("1")) == 0).filter(i -> values.get(i).compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Cycle Time values for Sub-Sub-Assembly grand total
     * @param assemblyType
     * @param levels
     * @param values
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCTSubSubAsmValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Cycle Time values for Top Level grand total
     * @param assemblyType
     * @param levels
     * @param values
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCTTopLevelValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).chars().allMatch(Character::isDigit) || partNums.get(i).equals("Assembly Process") ||
            partNums.get(i).equals("SUB-ASSEMBLY")).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Piece Part Cost values for Sub Assembly grand total
     * @param assemblyType
     * @param levels
     * @param values
     * @param quantities
     * @return
     */
    private List<BigDecimal> checkPPCValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values, List<BigDecimal> quantities) {
        List<String> partNums = checkPartNumber(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();

        if (!refinedQuantities.isEmpty()) {
            refinedQuantities.clear();
        }

        IntStream.range(0, partNums.size()).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0).forEach(i -> {
            trimmedValues.add(values.get(i));
            refinedQuantities.add(quantities.get(i));
        });
        return trimmedValues;
    }

    /**
     * Gets Capital Investment values for Sub Assembly grand total
     * @param assemblyType
     * @param levels
     * @param values
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCISubAssemblyValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).equals("Assembly Process") || partNums.get(i).equals("SUB-SUB-ASM")).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Capital Investment values for Sub-Sub-Assembly grand total
     * @param assemblyType
     * @param levels
     * @param values
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCISubSubAsmValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Capital Investment values for Top Level grand total
     * @param assemblyType
     * @param levels
     * @param values
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCITopLevelValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).equals("Assembly Process") || partNums.get(i).equals("SUB-ASSEMBLY")).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets quantity list, trims it to size and multiplies by value where necessary
     * @param assemblyType
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkQuantityList(String assemblyType) {
        List<BigDecimal> quantities = getValuesByColumn(assemblyType, "Quantity");
        List<BigDecimal> quantitiesEmpty = getEmptyQuantities(assemblyType,"Quantity Empty");

        quantities.add(0, quantitiesEmpty.get(0));

        if (assemblyType.equals("Sub-Assembly")) {
            quantities.add(7, quantitiesEmpty.get(1));
        } else if (assemblyType.equals("Top Level")) {
            quantities.add(8, quantitiesEmpty.get(1));
            quantities.add(15, quantitiesEmpty.get(2));
        }

        return quantities;
    }

    /**
     * Applies quantities to values (final stage before sum, usually)
     * @param values
     * @return List of BigDecimals
     */
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
     * Ensures two values are almost near (within 0.03)
     * @param valueOne
     * @param valueTwo
     * @return boolean
     */
    public boolean areValuesAlmostEqual(BigDecimal valueOne, BigDecimal valueTwo) {
        BigDecimal largerValue = valueOne.max(valueTwo);
        BigDecimal smallerValue = valueOne.min(valueTwo);
        BigDecimal difference = largerValue.subtract(smallerValue);
        boolean retVal = false;
        if (difference.compareTo(new BigDecimal("0.00")) >= 0 && difference.compareTo(new BigDecimal("0.03")) <= 0) {
            retVal = true;
        }
        return retVal;
    }

    /**
     * Returns parsed markup of page and sets the CSS Locator
     * @param assemblyType
     * @param rowIndex
     * @param columnName
     * @return Document (jsoup)
     */
    private Document parsePageSetCss(String assemblyType, String rowIndex, String columnName) {
        setCssLocator(assemblyType, rowIndex, columnName);
        return Jsoup.parse(driver.getPageSource());
    }

    /**
     * Sets css locator based on parameters
     * @param assemblyType
     * @param rowIndex
     * @param columnName
     */
    private void setCssLocator(String assemblyType, String rowIndex, String columnName) {
        String columnSelector;

        switch (assemblyType) {
            case "Sub-Assembly":
                rowSelector = subAssemblyRowMap.get(rowIndex);
                break;
            case "Sub-Sub-ASM":
                rowSelector = subSubAsmRowMap.get(rowIndex);
                break;
            case "Top Level":
                rowSelector = topLevelRowMap.get(rowIndex);
                break;
        }

        columnSelector = genericColumnMap.get(columnName);

        if (!rowIndex.isEmpty()) {
            String baseCssSelector = "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table %s %s span";
            cssSelector = String.format(baseCssSelector, rowSelector, columnSelector);
        } else if (columnName.equals("Part Number Main")) {
            String baseCssSelectorNoRowSpecified = "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table tr %s span span";
            cssSelector = String.format(baseCssSelectorNoRowSpecified, columnSelector);
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
