package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblySetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.AssemblyTypeEnum;
import com.apriori.utils.enums.CurrencyEnum;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
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

public class GenericReportPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(GenericReportPage.class);
    private Map<String, WebElement> assemblyMap = new HashMap<>();
    private Map<String, WebElement> currencyMap = new HashMap<>();
    private Map<String, WebElement> partNameMap = new HashMap<>();

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[3][local-name()='path']")
    private WebElement castingDtcBubble;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[local-name()='path'][43]")
    private WebElement machiningDtcBubble;

    @FindBy(xpath = "//*[text()=\"Fully Burdened Cost : \"]/following-sibling::*[1]")
    private WebElement tooltipFbcElement;

    @FindBy(xpath = "//*[text()='Finish Mass : ']/preceding-sibling::*[1]")
    private WebElement partNameCastingDtcReport;

    @FindBy(xpath = "(//*[text()='VERY LONG NAME'])[position()=1]/../..//*[local-name()='text' and position()=2]")
    private WebElement partNameCastingDtcComparisonReport;

    @FindBy(xpath = "//*[local-name()='rect' and @y='180.5']")
    private WebElement partOfCastingChartComparisonReport;

    @FindBy(xpath = "//*[contains(text(), 'Hole Issues')]/following-sibling::*[1]")
    private WebElement holeIssuesChartOneComparisonReport;

    @FindBy(xpath = "//span[contains(text(), 'Comparison')]")
    private WebElement comparisonButton;

    @FindBy(xpath = "//span[contains(text(), 'MLDES')]")
    private WebElement partNameCastingDtcDetailsReport;

    @FindBy(xpath = "//table[@class='jrPage superfocus']//span[text()='23']")
    private WebElement holeIssuesCastingDtcDetailsValue;

    @FindBy(xpath = "//span[contains(text(), 'Hole Issues')]")
    private WebElement holeIssuesCastingDtcDetailsTitle;

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "//div[@id='reportContainer']/table/tbody/tr[7]/td/span")
    private WebElement currentAssembly;

    @FindBy(css = "a[id='logo']")
    private WebElement cidLogo;

    @FindBy(xpath = "//span[contains(text(), 'Casting DTC Comparison')]")
    private WebElement dtcCastingReportTitle;

    @FindBy(xpath = "//span[contains(text(), 'Rollup:')]/../following-sibling::td[2]/span")
    private WebElement dtcCastingSelectedRollup;

    @FindBy(xpath = "//label[contains(@title, 'Earliest Export Date')]/input")
    protected WebElement earliestExportDateInput;

    @FindBy(xpath = "//label[contains(@title, 'Latest Export Date')]/input")
    protected WebElement latestExportDateInput;

    @FindBy(xpath = "//div[@id='exportSetName']//input[contains(@class, 'jr-mInput-search')]")
    protected WebElement exportSetSearchInput;

    @FindBy(xpath = "//div[@id='exportSetName']//ul[@class='jr-mSelectlist jr']//a")
    protected WebElement exportSetToSelect;

    @FindBy(xpath = "//label[@title='Assembly Select']/div/div/div/a")
    private WebElement currentAssemblyElement;

    @FindBy(xpath = "//div[@id='partNumber']/label/div/div/div/a")
    private WebElement currentAssElement;

    @FindBy(xpath = "//a[contains(text(), 'SUB-ASSEMBLY')]")
    private WebElement subAssOption;

    @FindBy(xpath = "//label[@title='Assembly Select']//input")
    private WebElement assemblyInput;

    @FindBy(xpath = "//label[@title='Assembly Select']//input")
    private WebElement inputBox;

    @FindBy(css = "li[title='SUB-ASSEMBLY (Initial) [assembly]'] > div > a")
    private WebElement subAssemblyOption;

    @FindBy(css = "li[title='SUB-SUB-ASM (Initial) [assembly]'] > div > a")
    private WebElement subSubAsmOption;

    @FindBy(css = "li[title='TOP-LEVEL (Initial) [assembly]'] > div > a")
    private WebElement topLevelOption;

    @FindBy(xpath = "//label[@title='Currency Code']/div/div/div/a")
    private WebElement currentCurrencyElement;

    @FindBy(css = "li[title='USD'] > div > a")
    private WebElement usdCurrencyOption;

    @FindBy(css = "li[title='GBP'] > div > a")
    private WebElement gbpCurrencyOption;

    @FindBy(id = "apply")
    private WebElement applyButton;

    @FindBy(id = "ok")
    private WebElement okButton;

    @FindBy(id = "reset")
    private WebElement resetButton;

    @FindBy(xpath = "//div[@id='inputControls']//button[@id='cancel']")
    private WebElement cancelButton;

    @FindBy(id = "save")
    private WebElement saveButton;

    @FindBy(id = "loading")
    private WebElement loadingPopup;

    @FindBy(xpath = "//div[@title='Single export set selection.']//li[@title='Select All']/a")
    private WebElement exportSetSelectAll;

    @FindBy(css = "div[id='exportSetName'] > div > div > div > div > div:nth-of-type(1) > span")
    private WebElement availableExportSets;

    @FindBy(css = "div[id='exportSetName'] > div > div > div > div > div:nth-of-type(2) > span")
    private WebElement selectedExportSets;

    @FindBy(xpath = "//div[@title='Single export set selection.']//ul[@class='jr-mSelectlist jr']")
    private WebElement exportSetList;

    @FindBy(xpath = "//div[@title='Single export set selection.']//li[@title='Invert']/a")
    private WebElement exportSetInvert;

    @FindBy(xpath = "//div[@title='Single export set selection.']//li[@title='Deselect All']/a")
    private WebElement exportSetDeselect;

    @FindBy(xpath = "//label[contains(@title, 'Earliest Export Date')]//button")
    private WebElement earliestExportSetDatePickerTriggerBtn;

    @FindBy(xpath = "//label[contains(@title, 'Latest Export Date')]//button")
    private WebElement latestExportSetDatePickerTriggerBtn;

    @FindBy(xpath = "//button[contains(text(), 'Now')]")
    private WebElement nowPickerButton;

    @FindBy(xpath = "//button[contains(text(), 'Close')]")
    private WebElement closeButton;

    @FindBy(css = "select[class='ui-datepicker-month']")
    private WebElement datePickerMonthSelect;

    @FindBy(css = "select[class='ui-datepicker-year']")
    private WebElement datePickerYearSelect;

    @FindBy(xpath = "//label[@title='Rollup']/div/div/div/a")
    private WebElement rollupDropdown;

    @FindBy(xpath = "//div[@id='rollup']//div[@class='jr-mSingleselect-search jr jr-isOpen']/input")
    private WebElement rollupSearch;

    @FindBy(css = "input[id='savedValuesName']")
    private WebElement saveInput;

    @FindBy(xpath = "//div[@id='saveValues']//button[@id='saveAsBtnSave']")
    private WebElement saveAsButton;

    @FindBy(xpath = "//select[@id='reportOptionsSelect']")
    private WebElement savedOptionsDropDown;

    @FindBy(xpath = "//button[@id='remove' and @class='button action up']")
    private WebElement removeButton;

    @FindBy(xpath = "//div[@class='jr-mDialog jr confirmationDialog open']//div[@class='jr-mDialog-footer jr']/button[1]")
    private WebElement confirmRemove;

    @FindBy(xpath = "//select[@id='reportOptionsSelect']//option[@value='']")
    private WebElement noneOption;

    @FindBy(xpath = "//div[@id='inputControls']//div[@class='sub header hidden']")
    private WebElement hiddenSavedOptions;

    @FindBy(id = "inputControls")
    private WebElement inputControlsDiv;

    private WebDriver driver;
    private PageUtils pageUtils;

    public GenericReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseAssemblyHashMap();
        initialiseCurrencyMap();
        initialisePartNameMap();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(okButton);
    }

    /**
     * Selects specified export set
     *
     * @return current page object
     */
    public GenericReportPage selectExportSet(String exportSet) {
        By exportSetToSelect = By.xpath(String.format("//li[@title='%s']/div/a", exportSet));
        WebElement exportSetToPick = driver.findElement(exportSetToSelect);
        exportSetToPick.click();
        return this;
    }

    /**
     * Get Export Set name
     * @return current page object
     */
    public String[] getActualExportSetValues() {
        String[] actualExportSetValues = new String[getExportSetEnumValues().length];

        for (int i = 0; i < getExportSetEnumValues().length; i++) {
            actualExportSetValues[i] =
                    driver.findElement(
                            By.xpath(String.format("//li[@title='%s']/div/a", getExportSetEnumValues()[i])))
                            .getText();
        }
        return actualExportSetValues;
    }

    /**
     * Gets Export Set Enum values
     * @return String array of values
     */
    public String[] getExportSetEnumValues() {
        return new String[] {
                ExportSetEnum.TOP_LEVEL.getExportSetName(),
                ExportSetEnum.PISTON_ASSEMBLY.getExportSetName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
                ExportSetEnum.CASTING_DTC.getExportSetName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        };
    }

    /**
     * Generic scroll method
     *
     * @return current page object
     */
    public GenericReportPage scrollDownInputControls() {
        pageUtils.waitForElementToAppear(currentCurrencyElement);
        pageUtils.scrollWithJavaScript(currentCurrencyElement, true);
        return this;
    }

    /**
     * Sets specified assembly
     *
     * @return current page object
     */
    public GenericReportPage setAssembly(String assemblyName) {
        pageUtils.waitFor(710);
        currentAssemblyElement.click();
        if (!currentAssemblyElement.getAttribute("title").equals(assemblyName)) {
            assemblyMap.get(assemblyName).click();
        }
        return this;
    }

    /**
     * Checks current currency selection, fixes if necessary
     *
     * @param currency
     * @return current page object
     */
    public GenericReportPage checkCurrencySelected(String currency) {
        currentCurrencyElement.click();
        if (!currentCurrencyElement.getAttribute("title").equals(currency)) {
            currencyMap.get(currency).click();
        }
        return this;
    }

    /**
     * Moves to new tab (Casting DTC to Casting DTC Comparison)
     * @return current page object
     */
    public GenericReportPage newTabTransfer() {
        if (pageUtils.getCountOfOpenTabs() == 2) {
            pageUtils.windowHandler(1);
        }
        pageUtils.waitForElementToAppear(comparisonButton);
        return this;
    }

    /**
     * Opens new tab with CID open and switches to it
     * @return current page object
     */
    public GenericReportPage openNewTabAndFocus(int index) {
        pageUtils.jsNewTab();
        pageUtils.windowHandler(index);

        driver.get(Constants.cidURL);
        pageUtils.waitForElementToAppear(cidLogo);

        return new GenericReportPage(driver);
    }

    /**
     * Clicks ok
     * @return Instance of Generic Report Page object
     */
    public GenericReportPage clickOk() {
        pageUtils.waitForElementAndClick(okButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return this;
    }

    /**
     * Click comparison link
     * @return instance of current page object
     */
    public GenericReportPage clickComparison() {
        pageUtils.waitForElementToAppear(castingDtcBubble);
        pageUtils.waitForElementAndClick(comparisonButton);
        return this;
    }

    /**
     * Waits for correct assembly to appear on screen (not on Input Controls - on report itself)
     *
     * @param assemblyToCheck
     * @return Generic - instance of specified class
     */
    public GenericReportPage waitForCorrectAssembly(String assemblyToCheck) {
        pageUtils.waitForElementToAppear(currentAssembly);
        // if not top level, add -
        if (assemblyToCheck.equals(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType()) || assemblyToCheck.equals(AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType())) {
            String newVal = assemblyToCheck.toUpperCase().replace(" ", "-");
            pageUtils.checkElementAttribute(currentAssembly, "innerText", newVal);
        }
        return this;
    }


    /**
     * Wait for export set list count to be zero
     *
     * @return current page object
     */
    public GenericReportPage waitForCorrectExportSetListCount(String expectedCount) {
        pageUtils.checkElementAttribute(exportSetList, "childElementCount", expectedCount);
        pageUtils.checkElementAttribute(selectedExportSets, "title", getCountOfExportSets());
        return this;
    }

    /**
     * Ensures latest date is set to today
     *
     * @return current page object
     */
    public GenericReportPage setEarliestExportDateToTodayInput() {
        String dtToday = getDate(true);
        pageUtils.waitForElementToAppear(earliestExportDateInput);

        if (!earliestExportDateInput.getAttribute("value").isEmpty()) {
            earliestExportDateInput.click();
            earliestExportDateInput.clear();
            earliestExportDateInput.sendKeys(dtToday);
        }
        return this;
    }

    /**
     * Sets earliest export set date to today using picker
     *
     * @return current page object
     */
    public GenericReportPage setEarliestExportDateToTodayPicker() {
        LocalDateTime dtToday = getCurrentDateLDT();
        pageUtils.waitForElementAndClick(earliestExportSetDatePickerTriggerBtn);

        setMonthValuePicker(dtToday.getMonthValue() - 1);
        setYearValuePicker(String.format("%d", dtToday.getYear()));
        earliestExportSetDatePickerTriggerBtn.click();

        String currentVal = earliestExportDateInput.getAttribute("value");
        earliestExportDateInput.click();
        earliestExportDateInput.clear();
        earliestExportDateInput.sendKeys(currentVal.replace("23", String.format("%d", dtToday.getDayOfMonth())));
        return this;
    }

    /**
     * Sets export set time and date to current time minus two months using input field
     */
    public GenericReportPage setLatestExportDateToTwoDaysFutureInput() {
        String dtTwoMonthsAgo = getDate(false);

        pageUtils.waitForElementToAppear(latestExportDateInput);
        if (!latestExportDateInput.getAttribute("value").isEmpty()) {
            latestExportDateInput.click();
            latestExportDateInput.clear();
            latestExportDateInput.sendKeys(dtTwoMonthsAgo);
        }
        return this;
    }

    /**
     * Sets export set time and date to current time minus two months using input field
     */
    public GenericReportPage setLatestExportDateToTodayInput() {
        String dtToday = getDate(true);
        pageUtils.waitForElementToAppear(latestExportDateInput);

        if (!latestExportDateInput.getAttribute("value").isEmpty()) {
            latestExportDateInput.click();
            latestExportDateInput.clear();
            latestExportDateInput.sendKeys(dtToday);
        }
        return this;
    }

    /**
     * Sets latest export date to two days from current (dynamic)
     *
     * @return current page object
     */
    public GenericReportPage setLatestExportDateToTodayPlusTwoPicker() {
        LocalDateTime newDt = getCurrentDateLDT().plusDays(2);

        int monthValue = newDt.getMonthValue();
        int dayValue = newDt.getDayOfMonth();
        int yearValue = newDt.getYear();

        pageUtils.waitForElementAndClick(latestExportSetDatePickerTriggerBtn);
        setMonthValuePicker(monthValue - 1);
        setYearValuePicker(String.format("%d", yearValue));

        String twoDaysAheadBtnLocator = String.format("//a[contains(text(), '%d')]", dayValue);
        pageUtils.waitForElementAndClick(By.xpath(twoDaysAheadBtnLocator));
        pageUtils.waitForElementAndClick(closeButton);

        return this;
    }

    /**
     * Ensures latest date is set to today
     *
     * @return current page object
     */
    public GenericReportPage ensureDatesAreCorrect(boolean areBothInputsPresent, boolean getCurrentDateInitially) {
        WebElement dateElementToUse;
        String date = removeTimeFromDate(getDate(getCurrentDateInitially));

        if (areBothInputsPresent) {
            for (int i = 0; i < 2; i++) {
                String dateForBoth = getCurrentDateInitially ? date : removeTimeFromDate(getDate(false));
                dateElementToUse = getCurrentDateInitially ? earliestExportDateInput : latestExportDateInput;
                pageUtils.checkElementAttribute(dateElementToUse, "value", dateForBoth);
                getCurrentDateInitially = false;
            }
        } else {
            dateElementToUse = latestExportDateInput;
            pageUtils.checkElementAttribute(dateElementToUse, "value", date);
        }
        return this;
    }

    /**
     * Ensures filtering worked correctly
     *
     * @return int size of element list
     */
    public int getAmountOfTopLevelExportSets() {
        List<WebElement> list = driver.findElements(By.xpath("//div[contains(@title, 'Single export')]//ul[@class='jr-mSelectlist jr']/li[@title='top-level']/div/a"));
        return list.size();
    }

    /**
     * Generic method to wait for correct currency and return specified page object
     *
     * @param currencyToCheck
     * @param className
     * @param <T> return type - any page object that is specified
     * @return new instance of page object
     */
    public <T> T waitForCorrectCurrency(String currencyToCheck, Class<T> className) {
        pageUtils.waitForElementToAppear(currentCurrency);
        pageUtils.checkElementAttribute(currentCurrency, "innerText", currencyToCheck);
        return PageFactory.initElements(driver, className);
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
     * Click export set select all button
     *
     * @return current page object
     */
    public GenericReportPage exportSetSelectAll() {
        pageUtils.waitForElementAndClick(exportSetSelectAll);
        String exportSetCount = getCountOfExportSets();
        pageUtils.checkElementAttribute(selectedExportSets, "title", exportSetCount);
        return this;
    }

    /**
     * Gets number of currently available export sets
     *
     * @return String - count of export sets
     */
    public String getCountOfExportSets() {
        return exportSetList.getAttribute("childElementCount");
    }

    /**
     * Get number of available export sets
     *
     * @return int
     */
    public int getAvailableExportSetCount() {
        String count = pageUtils.waitForElementToAppear(availableExportSets).getAttribute("title");
        count = count.substring((count.lastIndexOf(" ") + 1));
        return Integer.parseInt(count);
    }

    /**
     * Get number of selected export sets
     *
     * @return int
     */
    public int getSelectedExportSetCount() {
        String count = pageUtils.waitForElementToAppear(selectedExportSets).getAttribute("title");
        count = count.substring((count.lastIndexOf(" ") + 1));
        return Integer.parseInt(count);
    }

    /**
     * Deselect export set
     *
     * @return current page object
     */
    public GenericReportPage deselectExportSet(String exportSet) {
        int expected = getSelectedExportSetCount() - 1;
        exportSetSearchInput.sendKeys(exportSet);
        pageUtils.waitForElementAndClick(exportSetToSelect);
        pageUtils.checkElementAttribute(selectedExportSets, "title", "Selected: " + expected);
        return this;
    }

    /**
     * Invert export set selection
     *
     * @return current page object
     */
    public GenericReportPage invertExportSetSelection() {
        int expected = getAvailableExportSetCount() - getSelectedExportSetCount();
        pageUtils.waitForElementAndClick(exportSetInvert);
        pageUtils.checkElementAttribute(selectedExportSets, "title", "Selected: " + expected);
        return this;
    }

    /**
     * Deselect all export sets
     *
     * @return current page object
     */
    public GenericReportPage exportSetDeselectAll() {
        pageUtils.waitForElementAndClick(exportSetDeselect);
        pageUtils.checkElementAttribute(selectedExportSets, "title", "Selected: " + "0");
        return this;
    }

    /**
     * Expand rollup drop-down
     *
     * @return current page object
     */
    public GenericReportPage selectRollup(String rollupName) {
        rollupDropdown.click();
        if (!rollupDropdown.getAttribute("title").equals(rollupName)) {
            driver.findElement(
                    By.xpath(String.format("//li[@title='%s']", rollupName)))
                    .click();
        }
        return this;
    }

    /**
     * Substrings date to remove time
     *
     * @return String
     */
    private String removeTimeFromDate(String dateToSubstring) {
        return dateToSubstring.substring(0, 10);
    }

    /**
     * Gets current date in correct format
     *
     * @return String
     */
    private String getDate(boolean getCurrent) {
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return getCurrent ? formatter
            .format(LocalDateTime.now(ZoneOffset.UTC).withNano(0))
            : formatter
            .format(LocalDateTime.now(ZoneOffset.UTC).plusDays(2).withNano(0));
    }

    /**
     * Get current date as Local Date Time, rather than String
     *
     * @return LocalDateTime
     */
    private LocalDateTime getCurrentDateLDT() {
        return LocalDateTime.now(ZoneOffset.UTC).withNano(0);
    }


    /**
     * Select Assembly option dropdown using send keys
     *
     * @param topIndex
     */
    private void selectAssemblyOption(int topIndex) {
        for (int i = 0; i < topIndex; i++) {
            inputBox.sendKeys(Keys.ARROW_DOWN);
        }
    }

    /**
     * Sets month dropdown value in date picker
     *
     * @param indexToSelect
     */
    private void setMonthValuePicker(int indexToSelect) {
        Select monthSelect = new Select(datePickerMonthSelect);
        monthSelect.selectByIndex(indexToSelect);
    }

    /**
     * Sets year dropdown value in date picker
     *
     * @param valueToSelect
     */
    private void setYearValuePicker(String valueToSelect) {
        Select yearSelect = new Select(datePickerYearSelect);
        yearSelect.selectByValue(valueToSelect);
    }

    /**
     * Click apply
     *
     * @return current page object
     */
    public GenericReportPage clickApply() {
        pageUtils.waitForElementAndClick(applyButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return this;
    }

    /**
     * Click cancel
     *
     * @return new library page object
     */
    public <T> T clickCancel(Class<T> className) {
        pageUtils.waitForElementAndClick(cancelButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementNotDisplayed(inputControlsDiv, 1);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Click reset
     *
     * @return current page object
     */
    public GenericReportPage clickReset() {
        pageUtils.waitForElementAndClick(resetButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return this;
    }

    /**
     * Click save
     *
     * @return current page object
     */
    public GenericReportPage clickSave() {
        pageUtils.waitForElementAndClick(saveButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return this;
    }

    /**
     * Enter saved input control configuration name
     *
     * @return current page object
     */
    public GenericReportPage enterSaveName(String saveName) {
        pageUtils.waitForElementAndClick(saveInput);
        saveInput.sendKeys(Keys.CONTROL + "a");
        saveInput.sendKeys(Keys.DELETE);
        saveInput.sendKeys(saveName);
        return this;
    }

    /**
     * Click save as button to save input control configuration
     *
     * @return current page object
     */
    public GenericReportPage clickSaveAsButton() {
        pageUtils.waitForElementAndClick(saveAsButton);
        return this;
    }

    /**
     * Select saved input control config by name
     *
     * @return current page object
     */
    public GenericReportPage selectSavedOptionByName(String optionsName) {
        pageUtils.waitForElementToAppear(savedOptionsDropDown);
        Select dropDown = new Select(savedOptionsDropDown);
        dropDown.selectByVisibleText(optionsName);
        return this;
    }

    /**
     * Get export set selection status
     *
     * @return boolean
     */
    public boolean isExportSetSelected(String exportSetName) {
        pageUtils.waitForElementToAppear(exportSetList);
        List<WebElement> childElements = exportSetList.findElements(By.tagName("li"));

        return childElements.stream().anyMatch(we -> (we.getAttribute("title").contains(exportSetName)
            && we.getAttribute("class").contains("isHovered")));
    }

    /**
     * Click remove button
     *
     * @return current page object
     */
    public GenericReportPage clickRemove() {
        pageUtils.waitForElementAndClick(removeButton);
        pageUtils.waitForElementAndClick(confirmRemove);
        return this;
    }

    /**
     * Option in dropdown
     *
     * @return boolean
     */
    public boolean isOptionInDropDown(String optionName, int expected) {
        pageUtils.checkElementAttribute(savedOptionsDropDown, "childElementCount", Integer.toString(expected));
        if (driver.findElements(By.xpath("//div[@id='inputControls']//div[@class='sub header hidden']")).size() > 0) {
            return false;
        } else {
            pageUtils.waitForElementToAppear(savedOptionsDropDown);
            Select dropDown = new Select(savedOptionsDropDown);
            List<WebElement> options = dropDown.getOptions();

            return options.stream().anyMatch(we -> we.getText().equals(optionName));
        }
    }

    /**
     * Wait for expected export count
     */
    public GenericReportPage waitForExpectedExportCount(String expected) {
        pageUtils.checkElementAttribute(selectedExportSets, "title", "Selected: " + expected);
        return this;
    }

    /**
     * Method to return value from Bubble in DTC Casting or Machining DTC Report
     * @return BigDecimal value
     */
    public BigDecimal getFBCValueFromBubbleTooltip(boolean isCastingDtcReport) {
        WebElement elementToUse = isCastingDtcReport ? castingDtcBubble : machiningDtcBubble;
        pageUtils.waitForElementToAppear(elementToUse);
        Actions builder = new Actions(driver).moveToElement(elementToUse);
        builder.perform();

        pageUtils.waitForElementToAppear(tooltipFbcElement);

        return new BigDecimal(
                tooltipFbcElement.getText()
                        .replace(",", "")
        );
    }

    /**
     * Get part name from Casting DTC or Machining DTC Report
     * @return String of part name
     */
    public String getPartNameDtcCastingReports(String reportName) {
        WebElement elementToUse = partNameMap.get(reportName);
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.getText();
    }

    /**
     * Gets Hole Issue number from DTC Casting Details Report
     * @return String - value
     */
    public String getHoleIssuesFromDetailsReport() {
        pageUtils.waitForElementAndClick(holeIssuesCastingDtcDetailsTitle);
        return holeIssuesCastingDtcDetailsValue.getText();
    }

    /**
     * Gets Hole Issue number from DTC Casting Comparison Report
     * @return String - value
     */
    public String getHoleIssuesFromComparisonReport() {
        pageUtils.waitForElementToAppear(partOfCastingChartComparisonReport);
        Actions builder = new Actions(driver).moveToElement(partOfCastingChartComparisonReport);
        builder.perform();

        pageUtils.waitForElementToAppear(holeIssuesChartOneComparisonReport);

        return holeIssuesChartOneComparisonReport.getText();
    }

    /**
     * Generic method to get numeric values in a given row
     */
    public ArrayList<BigDecimal> getValuesByRow(String row) {
        ArrayList<BigDecimal> valsToReturn = new ArrayList<>();
        Document reportsPartPage = Jsoup.parse(driver.getPageSource());

        String baseCssSelector = "table.jrPage tbody tr:nth-child(16) td:nth-child(2) div div:nth-child(2) table tr:nth-child(%s) td span";
        baseCssSelector = String.format(baseCssSelector, row);
        // 5, 8, 10, 13 row indexes

        List<Element> valueElements = reportsPartPage.select(baseCssSelector);

        for (Element valueCell : valueElements) {
            if (!valueCell.text().isEmpty() && valueCell.text().matches("[0-9]*[.][0-9]{2}")) {
                valsToReturn.add(new BigDecimal(valueCell.text()));
            }
        }
        return valsToReturn;
    }

    /**
     * Gets Input Controls Div Class Name
     * @return String
     */
    public String getInputControlsDivClassName() {
        return inputControlsDiv.getAttribute("className");
    }

    /**
     * Gets input controls div isEnabled value
     * @return boolean
     */
    public boolean inputControlsIsEnabled() {
        return pageUtils.isElementEnabled(inputControlsDiv);
    }

    /**
     *Gets input controls div isDisplayed value
     * @return boolean
     */
    public boolean inputControlsIsDisplayed() {
        return pageUtils.isElementDisplayed(inputControlsDiv);
    }

    /**
     * Initialises export set hash map
     */
    private void initialiseCurrencyMap() {
        currencyMap.put(CurrencyEnum.GBP.getCurrency(), gbpCurrencyOption);
        currencyMap.put(CurrencyEnum.USD.getCurrency(), usdCurrencyOption);
    }

    /**
     * Initialises assembly hash map
     */
    private void initialiseAssemblyHashMap() {
        assemblyMap.put(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName(), subAssemblyOption);
        assemblyMap.put(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName(), subSubAsmOption);
        assemblyMap.put(AssemblySetEnum.TOP_LEVEL.getAssemblySetName(), topLevelOption);
    }

    /**
     * Initialises part name map
     */
    private void initialisePartNameMap() {
        partNameMap.put(Constants.CASTING_DTC_REPORT_NAME, partNameCastingDtcReport);
        partNameMap.put(Constants.CASTING_DTC_COMPARISON_REPORT_NAME, partNameCastingDtcComparisonReport);
        partNameMap.put(Constants.CASTING_DTC_DETAILS_REPORT_NAME, partNameCastingDtcDetailsReport);
    }
}