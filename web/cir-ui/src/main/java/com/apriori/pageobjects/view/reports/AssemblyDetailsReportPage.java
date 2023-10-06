package com.apriori.pageobjects.view.reports;

import com.apriori.PageUtils;

import enums.AssemblyTypeEnum;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AssemblyDetailsReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    @FindBy(css = "a[id='logo']")
    private WebElement cidLogo;

    @FindBy(xpath = "//label[@title='Currency Code']/div/div/div/a")
    private WebElement currentCurrencyElement;

    @FindBy(xpath = "//label[@title='Assembly Select']//a")
    private WebElement currentAssemblyElement;

    @FindBy(xpath = "//table[@class='jrPage']/tbody/tr[7]/td/span")
    private WebElement currentAssembly;

    @FindBy(css = "button[class='ui-datepicker-trigger']")
    private WebElement datePickerTriggerBtn;

    @FindBy(css = "select[class='ui-datepicker-month']")
    private WebElement datePickerMonthSelect;

    @FindBy(css = "select[class='ui-datepicker-year']")
    private WebElement datePickerYearSelect;

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "//span[contains(text(), '3570824')]")
    private WebElement componentLinkAssemblyDetails;

    @FindBy(xpath = "//span[contains(text(), 'Component Cost')]")
    private WebElement componentCostReportTitle;

    @FindBy(xpath = "//span[contains(text(), 'SUB-SUB-ASM')]")
    private WebElement assemblyLinkAssemblyDetails;

    @FindBy(xpath = "//div[@title='Single export set selection.']//ul[@class='jr-mSelectlist jr']")
    private WebElement exportSetList;

    @FindBy(xpath = "//span[contains(text(), '0200613')]")
    private WebElement partNameRowFive;

    List<BigDecimal> refinedQuantities = new ArrayList<>();
    private final Map<String, String> genericColumnMap = new HashMap<>();
    private final Map<String, String> topLevelRowMap = new HashMap<>();
    private final Map<String, String> subSubAsmRowMap = new HashMap<>();
    private final Map<String, String> subAssemblyRowMap = new HashMap<>();

    private final String genericAssemblySetLocator = "//a[contains(text(), '%s [assembly]')]";
    private final String genericTrSelector = "tr:nth-child(%s)";
    private String cssSelector;

    private final PageUtils pageUtils;
    private final WebDriver driver;

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
     * Gets capital investments grand total from table
     *
     * @return BigDecimal
     */
    public BigDecimal getCapitalInvestmentsGrandTotalFromTable() {
        By locator = By.cssSelector("table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table ".concat(
                "tr:nth-child(20) td:nth-child(34) span"));
        return new BigDecimal(driver.findElement(locator).getText());
    }

    /**
     * Generic method to get specific value from an Assembly Details Report table
     *
     * @param assemblyType String
     * @param rowName String
     * @param columnName String
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
     *
     * @param assemblyType String
     * @param columnName String
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getValuesByColumn(String assemblyType, String columnName) {
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", columnName);
        List<Element> valueElements = assemblyDetailsReport.select(cssSelector);

        return IntStream.range(0, valueElements.size()).filter(i -> isValueValid(valueElements.get(i).text()) ||
                columnName.equals("Cycle Time") && i <= (valueElements.size() - 2))
            .mapToObj(i -> new BigDecimal(valueElements.get(i).text().replaceAll(",", "")))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets quantities that are blank and returns them as 0
     *
     * @param assemblyType String
     * @param columnName String
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getEmptyQuantities(String assemblyType, String columnName) {
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", columnName);
        List<Element> elementList = assemblyDetailsReport.select(cssSelector);

        return IntStream.range(0, elementList.size()).mapToObj(i -> new BigDecimal("0"))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all level values
     *
     * @param assemblyType String
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getLevelValues(String assemblyType) {
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", "Level");
        return assemblyDetailsReport.select(cssSelector).stream().filter(element -> isValueValid(element.text()))
                .map(element -> new BigDecimal(element.text().replace(".", "")))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * Gets expected Cycle Time grand total
     *
     * @param assemblyType String
     * @param columnName String
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
     * Gets expected Fully Burdened Cost/Piece Part Cost grand total
     *
     * @param assemblyType String
     * @param columnName String
     * @return BigDecimal
     */
    public BigDecimal getExpectedFbcPpcGrandTotal(String assemblyType, String columnName) {
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
     *
     * @param assemblyType String
     * @param columnName String
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
     * Gets current currency setting
     *
     * @return String
     */
    public String getCurrentCurrency() {
        return currentCurrency.getText();
    }


    /**
     * Ensures two values are almost near (within 0.03)
     *
     * @param valueOne BigDecimal
     * @param valueTwo BigDecimal
     * @return boolean
     */
    public boolean areValuesAlmostEqual(BigDecimal valueOne, BigDecimal valueTwo) {
        BigDecimal largerValue = valueOne.max(valueTwo);
        BigDecimal smallerValue = valueOne.min(valueTwo);
        BigDecimal difference = largerValue.subtract(smallerValue);
        return difference.compareTo(new BigDecimal("0.00")) >= 0 &&
                difference.compareTo(new BigDecimal("0.03")) <= 0;
    }

    /**
     * Gets sub total of values to add
     *
     * @param assemblyType String
     * @param column String
     * @return Array List of BigDecimals
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
     * Ensures filtering worked correctly
     *
     * @return int size of element list
     */
    public int getAmountOfTopLevelExportSets() {
        List<WebElement> list =
                driver.findElements(
                By.xpath(
                "//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='top-level']/div/a"
                ));
        return list.size();
    }

    /**
     * Gets part name from row five
     *
     * @return String
     */
    public String getRowFivePartName() {
        pageUtils.waitForElementToAppear(partNameRowFive);
        return partNameRowFive.getText();
    }

    /**
     * Gets one particular value from table row five
     *
     * @param valueName String
     * @return String
     */
    public String getFiguresFromTable(String valueName) {
        By locator = By.xpath(
                String.format(
                        "(//table)[7]/tbody/tr[11]/td[%s]",
                        genericColumnMap.get(valueName).substring(13, 15))
        );
        pageUtils.waitForElementToAppear(locator);
        pageUtils.waitForSteadinessOfElement(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Sets specified assembly
     *
     * @param assemblyName - String of assembly name to use
     * @return current page object
     */
    public GenericReportPage setAssembly(String assemblyName) {
        pageUtils.scrollWithJavaScript(currentCurrencyElement, true);
        if (!currentAssemblyElement.getAttribute("title").equals(assemblyName)) {
            currentAssemblyElement.click();
            By locator = By.xpath(String.format(genericAssemblySetLocator, assemblyName));
            pageUtils.waitForElementAndClick(locator);
        }
        return this;
    }

    /**
     * Gets assembly name from set assembly dropdown
     *
     * @param assemblyName - String of assembly name to use
     * @return String - assembly from dropdown
     */
    public String getAssemblyNameFromSetAssemblyDropdown(String assemblyName) {
        return driver.findElement(By.xpath(String.format(genericAssemblySetLocator, assemblyName)))
                .getAttribute("textContent");
    }

    /**
     * Waits for correct assembly to appear on screen (not on Input Controls - on report itself)
     *
     * @param assemblyToCheck String - assembly name to wait on
     * @return Generic - instance of GenericReportPage
     */
    public GenericReportPage waitForCorrectAssembly(String assemblyToCheck) {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementToAppear(currentAssembly);
        // if not top level, add -
        if (assemblyToCheck.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType()) ||
                assemblyToCheck.equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType())) {
            String newVal = assemblyToCheck.toUpperCase().replace(" ", "-");
            By locator = By.xpath(String.format("//span[contains(text(), '%s')]", newVal));
            pageUtils.waitForElementToAppear(locator);
        }
        return this;
    }

    /**
     * Wait for correct assembly selected in dropdown
     *
     * @param assemblyName - String of assembly name to use in locator
     */
    public void waitForCorrectAssemblyInDropdown(String assemblyName) {
        By locator = By.xpath(String.format("//a[contains(@title, '%s')]", assemblyName));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
     * Generic method to get numeric values in a given row
     *
     * @param row - String of row to get values from
     * @return ArrayList of BigDecimal values
     */
    public ArrayList<BigDecimal> getValuesByRow(String row) {
        ArrayList<BigDecimal> valsToReturn = new ArrayList<>();
        Document reportsPartPage = Jsoup.parse(driver.getPageSource());

        String baseCssSelector = "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table " +
                "tr:nth-child(%s) td span";
        baseCssSelector = String.format(baseCssSelector, row);

        List<Element> valueElements = reportsPartPage.select(baseCssSelector);

        for (Element valueCell : valueElements) {
            if (!valueCell.text().isEmpty() && valueCell.text().matches("[0-9]*[.][0-9]{2}")) {
                valsToReturn.add(new BigDecimal(valueCell.text()));
            }
        }
        return valsToReturn;
    }

    /**
     * Clicks Component Link in Assembly Details Report
     */
    public void clickComponentLinkAssemblyDetails() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementAndClick(componentLinkAssemblyDetails);
        switchTab(1);
        pageUtils.waitForElementToAppear(componentCostReportTitle);
    }

    /**
     * Clicks Assembly Link in Assembly Details Report
     */
    public void clickAssemblyLinkAssemblyDetails() {
        pageUtils.waitForElementAndClick(assemblyLinkAssemblyDetails);
        pageUtils.switchToWindow(1);
        pageUtils.waitForElementToAppear(componentCostReportTitle);
    }

    /**
     * Gets component link part number
     *
     * @return String of part number
     */
    public String getComponentLinkPartNumber() {
        return componentLinkAssemblyDetails.getText();
    }

    /**
     * Gets assembly link part number
     *
     * @return String of part number
     */
    public String getAssemblyLinkPartNumber() {
        return assemblyLinkAssemblyDetails.getText();
    }

    /**
     * Gets report title
     *
     * @return String of report title
     */
    public String getReportTitle() {
        return upperTitle.getText();
    }

    /**
     * Closes current tab and switches back to main tab
     */
    public void closeTab() {
        driver.close();
        pageUtils.switchToWindow(0);
    }

    /**
     * Checks if export set option is visible
     *
     * @param exportSet - String of export set to check visibility of
     * @return boolean of is export set visible
     */
    public boolean isExportSetVisible(String exportSet) {
        WebElement exportSetElement = driver.findElement(By.xpath(String.format("//li[@title='%s']/div/a", exportSet)));
        return exportSetElement.isDisplayed() && exportSetElement.isEnabled();
    }

    /**
     * Gets count of export sets visible
     *
     * @return String of export set option count
     */
    public String getExportSetOptionCount() {
        return exportSetList.getAttribute("childElementCount");
    }

    /**
     * Gets value from Component Cost Report
     *
     * @param valueToGet String of value to retrieve
     * @return String of component cost value
     */
    public BigDecimal getComponentCostReportValue(String valueToGet) {
        By locator = By.xpath(String.format(
                "//span[contains(text(), '%s')]/../following-sibling::td[1]/span", valueToGet));
        pageUtils.waitForElementToAppear(locator);
        return new BigDecimal(driver.findElement(locator).getText().replace(",", ""));
    }

    /**
     * Gets all VPE Values from Assembly Details Report
     *
     * @return ArrayList of type String
     */
    public ArrayList<String> getAllVpeValuesAssemblyDetailsReport() {
        ArrayList<String> vpeValues = new ArrayList<>();

        String[] partNames = { "3570823", "3570824", "3574255", "SUB-SUB-ASM", "3571050", "3575132", "3575133",
            "3575134", "0200613", "0362752", "3538968", "SUB-ASSEMBLY", "3575135" };

        String locator = "//span[contains(text(), '%s')]/ancestor::tr[@style='height:15px']/td[16]/span";

        for (String partName : partNames) {
            vpeValues.add(driver.findElement(By.xpath(String.format(locator, partName))).getText());
        }

        return vpeValues;
    }

    /**
     * Gets all part numbers
     *
     * @param assemblyType String
     * @return List of Strings
     */
    private List<String> checkPartNumber(String assemblyType) {
        // Gets main part numbers
        Document assemblyDetailsReport = parsePageSetCss(assemblyType, "", "Part Number Main");
        List<String> mainPartNums = new ArrayList<>();
        ArrayList<String> secondaryPartNums;

        if (assemblyType.equals(AssemblyTypeEnum.TOP_LEVEL.getAssemblyType())) {
            mainPartNums = assemblyDetailsReport.select(cssSelector).stream()
                    .filter(element -> !element.text().isEmpty() && !element.text().equals("Part Number")
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
        secondaryPartNums = assemblyDetailsReport.select(cssSelector).stream().filter(element -> element.text()
                .replace(" ", "").chars().allMatch(Character::isLetter) &&
            element.text().equals("Assembly Process")).map(Element::text)
                .collect(Collectors.toCollection(ArrayList::new));

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
     *
     * @param assemblyType String
     * @param columnName String
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
     * Gets Cycle Time values for Sub Assembly grand total
     *
     * @param assemblyType String
     * @param values String
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCTSubAssemblyValues(String assemblyType, List<BigDecimal> values) {
        List<BigDecimal> levelValues = getLevelValues(assemblyType);
        return IntStream.range(0, levelValues.size()).filter(i -> levelValues.get(i)
                .compareTo(new BigDecimal("1")) == 0).filter(i -> values.get(i)
            .compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Cycle Time values for Sub-Sub-Assembly grand total
     *
     * @param assemblyType String
     * @param levels String
     * @param values String
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCTSubSubAsmValues(String assemblyType, List<BigDecimal> levels,
                                                    List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0)
                .mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Cycle Time values for Top Level grand total
     *
     * @param assemblyType String
     * @param levels String
     * @param values String
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCTTopLevelValues(String assemblyType, List<BigDecimal> levels,
                                                   List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).chars().allMatch(Character::isDigit) ||
                partNums.get(i).equals("Assembly Process") ||
            partNums.get(i).equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType().toUpperCase()
                    .replace(" ", "-"))).filter(i -> levels.get(i)
            .compareTo(new BigDecimal("1")) == 0).mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Piece Part Cost values for Sub Assembly grand total
     *
     * @param assemblyType String
     * @param levels List of BigDecimals
     * @param values List of BigDecimals
     * @param quantities List of BigDecimals
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkPPCValues(String assemblyType, List<BigDecimal> levels, List<BigDecimal> values,
                                            List<BigDecimal> quantities) {
        List<String> partNums = checkPartNumber(assemblyType);
        List<BigDecimal> trimmedValues = new ArrayList<>();

        if (!refinedQuantities.isEmpty()) {
            refinedQuantities.clear();
        }

        IntStream.range(0, partNums.size()).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 &&
                values.get(i).compareTo(new BigDecimal("0.00")) != 0).forEach(i -> {
                    trimmedValues.add(values.get(i));
                    refinedQuantities.add(quantities.get(i));
                });
        return trimmedValues;
    }

    /**
     * Gets Capital Investment values for Sub Assembly grand total
     *
     * @param assemblyType String
     * @param levels List of BigDecimals
     * @param values List of BigDecimals
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCISubAssemblyValues(String assemblyType, List<BigDecimal> levels,
                                                      List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).equals("Assembly Process") ||
                partNums.get(i).equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType().toUpperCase()
                    .replace(" ", "-"))).filter(i -> levels.get(i)
                    .compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(new BigDecimal("0.00")) != 0)
                    .mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets Capital Investment values for Sub-Sub-Assembly grand total
     *
     * @param assemblyType String
     * @param levels List of BigDecimals
     * @param values List of BigDecimals
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCISubSubAsmValues(String assemblyType, List<BigDecimal> levels,
                                                    List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 &&
                values.get(i).compareTo(new BigDecimal("0.00")) != 0).mapToObj(values::get)
                .collect(Collectors.toList());
    }

    /**
     * Gets Capital Investment values for Top Level grand total
     *
     * @param assemblyType String
     * @param levels List of BigDecimals
     * @param values List of BigDecimals
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkCITopLevelValues(String assemblyType, List<BigDecimal> levels,
                                                   List<BigDecimal> values) {
        List<String> partNums = checkPartNumber(assemblyType);
        return IntStream.range(0, partNums.size()).filter(i -> partNums.get(i).equals("Assembly Process") ||
                partNums.get(i)
            .equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType().toUpperCase().replace(" ", "-")))
            .filter(i -> levels.get(i).compareTo(new BigDecimal("1")) == 0 && values.get(i).compareTo(
                    new BigDecimal("0.00")) != 0)
            .mapToObj(values::get).collect(Collectors.toList());
    }

    /**
     * Gets quantity list, trims it to size and multiplies by value where necessary
     *
     * @param assemblyType String
     * @return List of BigDecimals
     */
    private List<BigDecimal> checkQuantityList(String assemblyType) {
        List<BigDecimal> quantities = getValuesByColumn(assemblyType, "Quantity");
        List<BigDecimal> quantitiesEmpty = getEmptyQuantities(assemblyType, "Quantity Empty");

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
     *
     * @param values List of BigDecimals
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
     * Checks if value of current cell is a valid one
     *
     * @param valueToCheck String
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
     * Returns parsed markup of page and sets the CSS Locator
     *
     * @param assemblyType String
     * @param rowIndex String
     * @param columnName String
     * @return Document (jsoup)
     */
    private Document parsePageSetCss(String assemblyType, String rowIndex, String columnName) {
        setCssLocator(assemblyType, rowIndex, columnName);
        return Jsoup.parse(driver.getPageSource());
    }

    /**
     * Sets css locator based on parameters
     *
     * @param assemblyType String
     * @param rowIndex String
     * @param columnName String
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
            String baseCssSelector =
                    "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table %s %s span";
            cssSelector = String.format(baseCssSelector, rowSelector, columnSelector);
        } else if (columnName.equals("Part Number Main")) {
            String baseCssSelectorNoRowSpecified =
                    "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table tr %s span span";
            cssSelector = String.format(baseCssSelectorNoRowSpecified, columnSelector);
        } else {
            String baseCssSelectorNoRowSpecified =
                    "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table tr %s span";
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
        int j = 5;
        for (int i = 1; i < 7; i++) {
            putItemIntoSubAssemblyRowMap(String.format("%s Sub Assembly", i), String.valueOf(j));
            j++;
            j = j == 10 ? 12 : j;
        }
        putItemIntoSubAssemblyRowMap("Component Subtotal Sub Assembly", "15");
        putItemIntoSubAssemblyRowMap("Assembly Processes Sub Assembly", "18");
        putItemIntoSubAssemblyRowMap("Grand Total Sub Assembly", "20");
    }

    /**
     * Hash Map initialisation for columns in Sub-Sub-ASM export set report table
     */
    private void initialiseSubSubAsmRowMap() {
        putItemIntoSubSubAsmRowMap("1 Sub Sub ASM", "5");
        putItemIntoSubSubAsmRowMap("2 Sub Sub ASM", "6");
        putItemIntoSubSubAsmRowMap("Component Subtotal Sub Sub ASM", "8");
        putItemIntoSubSubAsmRowMap("Assembly Processes Sub Sub ASM", "11");
        putItemIntoSubSubAsmRowMap("Grand Total Sub Sub ASM", "13");
    }

    /**
     * Hash Map initialisation for columns in Top Level export set report table
     */
    private void initialiseTopLevelRowMap() {
        List<String> indexList = Arrays.asList("5", "6", "7", "8", "9", "10", "13", "14", "15", "16", "17", "20", "21");
        int j = 0;
        for (int i = 1; i < 14; i++) {
            putItemIntoTopLevelRowMap(String.format("%s Top Level", i), indexList.get(j));
            j++;
        }
        putItemIntoTopLevelRowMap("Component Subtotal Top Level", "24");
        putItemIntoTopLevelRowMap("Assembly Processes Top Level", "27");
        putItemIntoTopLevelRowMap("Grand Total Top Level", "29");
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
