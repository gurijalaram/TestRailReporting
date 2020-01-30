package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.enums.AssemblyTypeEnum;

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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
    private String cssSelector;

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "//div[@id='reportContainer']/table/tbody/tr[7]/td/span")
    private WebElement currentAssembly;

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
     * @param rowName
     * @param columnName
     * @return BigDecimal
     */
    public BigDecimal getValueFromTable(String assemblyType, String rowName, String columnName) {
        String rowIndex = rowName + " " + assemblyType;
        columnName = columnName + " Total";

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

        if (assemblyType.equals(AssemblyTypeEnum.TOP_LEVEL.getAssemblyType())) {
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
        if (assemblyType.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType())) {
            mainPartNums.add(7, secondaryPartNums.get(1));
        } else if (assemblyType.equals(AssemblyTypeEnum.TOP_LEVEL.getAssemblyType())) {
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

        if (assemblyType.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType())) {
            values.add(7, secondValue);
        } else if (assemblyType.equals(AssemblyTypeEnum.TOP_LEVEL.getAssemblyType())) {
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

        if (assemblyType.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType())) {
            trimmedValueList = checkCTSubAssemblyValues(assemblyType, allValues);
        } else if (assemblyType.equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType())) {
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

        if (assemblyType.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType())) {
            trimmedValueList = checkCISubAssemblyValues(assemblyType, levels, allValues);
        } else if (assemblyType.equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType())) {
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
            partNums.get(i).equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType().toUpperCase().replace(" ", "-"))).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0).mapToObj(values::get).collect(Collectors.toList());
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
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).equals("Assembly Process") || partNums.get(i).equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType().toUpperCase().replace(" ", "-"))).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get).collect(Collectors.toList());
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
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).equals("Assembly Process") || partNums.get(i).equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType().toUpperCase().replace(" ", "-"))).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get).collect(Collectors.toList());
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

        if (assemblyType.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType())) {
            quantities.add(7, quantitiesEmpty.get(1));
        } else if (assemblyType.equals(AssemblyTypeEnum.TOP_LEVEL.getAssemblyType())) {
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
     * Waits for correct assembly to appear on screen (not on Input Controls - on report itself)
     * @param assemblyToCheck
     * @return
     */
    public AssemblyDetailsReportPage waitForCorrectAssembly(String assemblyToCheck) {
        pageUtils.waitForElementToAppear(currentAssembly);
        // if not top level, add -
        if (assemblyToCheck.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType()) || assemblyToCheck.equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType())) {
            String newVal = assemblyToCheck.toUpperCase().replace(" ", "-");
            pageUtils.checkElementAttribute(currentAssembly, "innerText", newVal);
        }
        return this;
    }

    /**
     * Waits for correct current currency to appear on screen (not on Input Controls - on report itself)
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
     *
     * @param assemblyType
     * @param column
     * @return
     */
    public ArrayList<BigDecimal> getSubTotalAdditionValue(String assemblyType, String column) {
        ArrayList<BigDecimal> returnValues = new ArrayList<>();

        BigDecimal subTotal = getValueFromTable(assemblyType, "Component Subtotal", column + " Sub");
        BigDecimal assemblyProcesses = getValueFromTable(assemblyType, "Assembly Processes", column);
        BigDecimal expectedTotal = subTotal.add(assemblyProcesses);
        BigDecimal actualTotal = getValueFromTable(assemblyType, "Grand Total", column);

        returnValues.add(expectedTotal);
        returnValues.add(actualTotal);
        return returnValues;
    }

    /**
     * Sets export set time and date to current time minus two months
     */
    public AssemblyDetailsReportPage setExportDateToTwoMonthsAgo() {
        String dtTwoMonthsAgo = getDateTwoMonthsAgo();

        latestExportDateInput.clear();
        latestExportDateInput.sendKeys(dtTwoMonthsAgo);

        if (!latestExportDateInput.getAttribute("value").isEmpty()) {
            latestExportDateInput.clear();
            latestExportDateInput.sendKeys(dtTwoMonthsAgo);
        }
        return this;
    }

    /**
     * Ensures date has changed, before proceeding with test
     * @return current page object
     */
    public AssemblyDetailsReportPage ensureExportSetHasChanged() {
        pageUtils.checkElementAttribute(latestExportDateInput, "value", getDateTwoMonthsAgo().substring(0, 10));
        return this;
    }

    /**
     * Ensures filtering worked correctly
     * @return int size of element list
     */
    public int getAmountOfTopLevelExportSets() {
        List<WebElement> list = driver.findElements(By.xpath("//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='top-level']/div/a"));
        return list.size();
    }

    /**
     * Gets date from two months ago
     * @return String
     */
    private String getDateTwoMonthsAgo() {
        LocalDateTime pastDate = LocalDateTime.now(ZoneOffset.UTC).minusMonths(2).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(pastDate);
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
        String rowSelector;

        if (assemblyType.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType())) {
            rowSelector = subAssemblyRowMap.get(rowIndex);
        } else if (assemblyType.equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType())) {
            rowSelector = subSubAsmRowMap.get(rowIndex);
        } else {
            rowSelector = topLevelRowMap.get(rowIndex);
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
        putItemIntoColumnMap("Level", "2");
        putItemIntoColumnMap("Part Number Main", "5");
        putItemIntoColumnMap("Part Number Secondary", "6");
        putItemIntoColumnMap("Quantity", "10");
        putItemIntoColumnMap("Quantity Empty", "11");

        putItemIntoColumnMap("Cycle Time", "24");
        putItemIntoColumnMap("Cycle Time Sub Total", "15");
        putItemIntoColumnMap("Cycle Time Total", "25");

        putItemIntoColumnMap("Piece Part Cost", "27");
        putItemIntoColumnMap("Piece Part Cost Sub Total", "18");
        putItemIntoColumnMap("Piece Part Cost Total", "28");

        putItemIntoColumnMap("Fully Burdened Cost", "30");
        putItemIntoColumnMap("Fully Burdened Cost Sub Total", "21");
        putItemIntoColumnMap("Fully Burdened Cost Total", "31");

        putItemIntoColumnMap("Capital Investments", "33");
        putItemIntoColumnMap("Capital Investments Sub Total", "24");
        putItemIntoColumnMap("Capital Investments Total", "34");
    }


    /**
     * Hash Map initialisation for columns in Sub Assembly export set report table
     */
    private void initialiseSubAssemblyRowMap() {
        putItemIntoSubAssemblyRowMap("1 Sub Assembly", "5");
        putItemIntoSubAssemblyRowMap("2 Sub Assembly", "7");
        putItemIntoSubAssemblyRowMap("3 Sub Assembly", "11");
        putItemIntoSubAssemblyRowMap("4 Sub Assembly", "15");
        putItemIntoSubAssemblyRowMap("5 Sub Assembly", "17");
        putItemIntoSubAssemblyRowMap("6 Sub Assembly", "19");
        putItemIntoSubAssemblyRowMap("Component Subtotal Sub Assembly", "22");
        putItemIntoSubAssemblyRowMap("Assembly Processes Sub Assembly", "25");
        putItemIntoSubAssemblyRowMap("Grand Total Sub Assembly", "27");
    }

    /**
     * Hash Map initialisation for columns in Sub-Sub-ASM export set report table
     */
    private void initialiseSubSubAsmRowMap() {
        putItemIntoSubSubAsmRowMap("1 Sub Sub ASM", "4");
        putItemIntoSubSubAsmRowMap("2 Sub Sub ASM", "6");
        putItemIntoSubSubAsmRowMap("Component Subtotal Sub Sub ASM", "10");
        putItemIntoSubSubAsmRowMap("Assembly Processes Sub Sub ASM", "13");
        putItemIntoSubSubAsmRowMap("Grand Total Sub Sub ASM", "15");
    }

    /**
     * Hash Map initialisation for columns in Top Level export set report table
     */
    private void initialiseTopLevelRowMap() {
        putItemIntoTopLevelRowMap("1 Top Level", "5");
        putItemIntoTopLevelRowMap("2 Top Level", "8");
        putItemIntoTopLevelRowMap("3 Top Level", "11");
        putItemIntoTopLevelRowMap("4 Top Level", "14");
        putItemIntoTopLevelRowMap("5 Top Level", "17");
        putItemIntoTopLevelRowMap("6 Top Level", "19");
        putItemIntoTopLevelRowMap("7 Top Level", "21");
        putItemIntoTopLevelRowMap("8 Top Level", "23");
        putItemIntoTopLevelRowMap("9 Top Level", "27");
        putItemIntoTopLevelRowMap("10 Top Level", "31");
        putItemIntoTopLevelRowMap("11 Top Level", "33");
        putItemIntoTopLevelRowMap("12 Top Level", "35");
        putItemIntoTopLevelRowMap("13 Top Level", "38");
        putItemIntoTopLevelRowMap("Component Subtotal Top Level", "38");
        putItemIntoTopLevelRowMap("Assembly Processes Top Level", "41");
        putItemIntoTopLevelRowMap("Grand Total Top Level", "43");
    }

    private void putItemIntoColumnMap(String key, String value) {
        String genericTdSelector = "td:nth-child(%s)";
        genericColumnMap.put(key, String.format(genericTdSelector, value));
    }

    private void putItemIntoSubAssemblyRowMap(String key, String value) {
        subAssemblyRowMap.put(key, String.format(genericTrSelector, value));
    }

    private void putItemIntoSubSubAsmRowMap(String key, String value) {
        subSubAsmRowMap.put(key, String.format(genericTrSelector, value));
    }

    private void putItemIntoTopLevelRowMap(String key, String value) {
        topLevelRowMap.put(key, String.format(genericTrSelector, value));
    }
}
