package com.apriori.pageobjects.pages.view.reports;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.properties.PropertiesContext;

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
import utils.Constants;

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
    private final Map<String, WebElement> dtcComparisonDtcIssueMap = new HashMap<>();
    private final Map<String, WebElement> costDesignOutlierMap = new HashMap<>();
    private final Map<String, WebElement> dtcScoreBubbleMap = new HashMap<>();
    private final Map<String, WebElement> tooltipElementMap = new HashMap<>();
    private final Map<String, WebElement> currencyMap = new HashMap<>();
    private final Map<String, WebElement> partNameMap = new HashMap<>();
    private final Map<String, WebElement> bubbleMap = new HashMap<>();
    private String reportName = "";

    @FindBy(xpath = "//span[contains(text(), 'Select Parts')]")
    private WebElement selectPartsControlTitle;

    @FindBy(xpath = "//span[contains(text(), '* DTC Score')]")
    private WebElement dtcScoreControlTitle;

    @FindBy(css = "div[class='header'] div.title")
    protected WebElement upperTitle;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[55][local-name() = 'path']")
    private WebElement castingDtcBubble;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[18][local-name() = 'path']")
    private WebElement castingDtcBubbleTwo;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[25][local-name() = 'path']")
    private WebElement castingDtcBubbleThree;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[7][local-name() = 'path']")
    private WebElement dtcScoreMediumBubble;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[4][local-name() = 'path']")
    private WebElement dtcScoreSheetMetalMediumBubble;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[5][local-name() = 'path']")
    private WebElement dtcScoreHighBubble;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[54][local-name() = 'path']")
    private WebElement processGroupBubbleOne;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[56][local-name() = 'path']")
    private WebElement processGroupBubbleTwo;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[local-name() = 'path'][43]")
    private WebElement machiningDtcBubble;

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > " +
            "path:nth-of-type(38)")
    private WebElement machiningDtcBubbleTwo;

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > " +
            "path:nth-child(3)")
    private WebElement designOutlierChartSpotOne;

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > " +
            "path:nth-child(4)")
    private WebElement designOutlierChartSpotTwo;

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > path:nth-child(5)")
    private WebElement designOutlierChartSpotThree;

    @FindBy(xpath = "(//*[@class='highcharts-series-group']//*[local-name() = 'path'])[30]")
    private WebElement costOutlierChartSpotOne;

    @FindBy(xpath = "(//*[@class='highcharts-series-group']//*[local-name() = 'path'])[53]")
    private WebElement costOutlierChartSpotTwo;

    @FindBy(xpath = "(//*[@class='highcharts-series-group']//*[local-name() = 'path'])[47]")
    private WebElement costOutlierCentreSpot;

    @FindBy(xpath = "(//*[@class='highcharts-series-group']//*[local-name() = 'path'])[39]")
    private WebElement sheetMetalDtcBubble;

    @FindBy(xpath = "(//*[@class='highcharts-series-group']//*[local-name() = 'path'])[38]")
    private WebElement sheetMetalDtcBubbleTwo;

    @FindBy(xpath = "(//*[@class='highcharts-series-group']//*[local-name() = 'path'])[8]")
    private WebElement plasticDtcBubble;

    @FindBy(xpath = "(//*[text()='VERY LONG NAME'])[position()=1]/../..//*[local-name() = 'text' and position()=2]")
    private WebElement partNameCastingDtcComparisonReport;

    @FindBy(xpath = "//span[contains(text(), 'Comparison')]")
    private WebElement comparisonTitle;

    @FindBy(xpath = "//span[contains(text(), 'MLDES')]")
    private WebElement partNameCastingDtcDetailsReport;

    @FindBy(xpath = "//table[@class='jrPage superfocus']//span[text()='23']")
    private WebElement holeIssuesCastingDtcDetailsValue;

    @FindBy(xpath = "//span[contains(text(), 'Hole Issues')]")
    private WebElement holeIssuesCastingDtcDetailsTitle;

    @FindBy(xpath = "//span[contains(text(), 'Currency:')]/../../td[4]/span")
    private WebElement currentCurrency;

    @FindBy(xpath = "(//div[@id='reportContainer']//table/tbody/tr[7])[1]/td/span")
    private WebElement currentAssembly;

    @FindBy(css = "a[class='navbar-brand']")
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

    @FindBy(xpath = "//label[@title='Assembly Select']//a")
    private WebElement currentAssemblyElement;

    @FindBy(xpath = "//a[contains(text(), 'SUB-ASSEMBLY')]")
    private WebElement subAssOption;

    @FindBy(xpath = "//label[@title='Assembly Select']//input")
    private WebElement assemblyInput;

    @FindBy(xpath = "//label[@title='Assembly Select']//input")
    private WebElement inputBox;

    @FindBy(xpath = "//label[@title='Currency Code']/div/div/div/a")
    private WebElement currentCurrencyElement;

    @FindBy(css = "li[title='USD'] > div > a")
    private WebElement usdCurrencyOption;

    @FindBy(css = "li[title='GBP'] > div > a")
    private WebElement gbpCurrencyOption;

    @FindBy(id = "apply")
    private WebElement applyButton;

    @FindBy(xpath = "//button[@id='ok']/span/span")
    private WebElement okButton;

    @FindBy(id = "reset")
    private WebElement resetButton;

    @FindBy(xpath = "//div[@id='inputControls']//button[@id='cancel']")
    private WebElement cancelButton;

    @FindBy(id = "save")
    private WebElement saveButton;

    @FindBy(id = "loading")
    public WebElement loadingPopup;

    @FindBy(xpath = "//div[@title='Single export set selection.']//li[@title='Select All']/a")
    private WebElement exportSetSelectAll;

    @FindBy(css = "div[id='exportSetName'] > div > div > div > div > div:nth-of-type(1) > span")
    private WebElement availableExportSets;

    @FindBy(css = "div[id='exportSetName'] > div > div > div > div > div:nth-of-type(2) > span")
    private WebElement selectedExportSets;

    @FindBy(xpath = "//div[@title='Single export set selection.']//ul[@class='jr-mSelectlist jr']")
    private WebElement exportSetList;

    @FindBy(xpath = "//div[@title='Single export set selection.']//ul[@class='jr-mSelectlist jr']/..")
    private WebElement exportSetListDiv;

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

    @FindBy(xpath = "//div[@id='rollup']//a")
    private WebElement rollupDropdown;

    @FindBy(xpath = "//div[@id='rollup']//div[@class='jr-mSingleselect-search jr jr-isOpen']/input")
    private WebElement rollupSearchInput;

    @FindBy(xpath = "//div[@id='rollup']//a/span[2]")
    private WebElement rollupSelected;

    @FindBy(css = "input[id='savedValuesName']")
    private WebElement saveInput;

    @FindBy(xpath = "//div[@id='saveValues']//button[@id='saveAsBtnSave']")
    private WebElement saveAsButton;

    @FindBy(xpath = "//select[@id='reportOptionsSelect']")
    private WebElement savedOptionsDropDown;

    @FindBy(xpath = "//button[@id='remove' and @class='button action up']")
    private WebElement removeButton;

    @FindBy(xpath = "//div[@class='jr-mDialog jr confirmationDialog open']//div[@class='jr-mDialog-footer jr']" +
            "/button[1]")
    private WebElement confirmRemove;

    @FindBy(xpath = "//select[@id='reportOptionsSelect']//option[@value='']")
    private WebElement noneOption;

    @FindBy(xpath = "//div[@id='inputControls']//div[@class='sub header hidden']")
    private WebElement hiddenSavedOptions;

    @FindBy(id = "inputControls")
    private WebElement inputControlsDiv;

    @FindBy(xpath = "//span[contains(text(), 'Rollup:')]/../following-sibling::td[2]")
    private WebElement headerDisplayedRollup;

    @FindBy(xpath = "(//*[@style='font-weight:bold'])[1]")
    private WebElement partNameDtcReports;

    @FindBy(xpath = "(//*[local-name()='g']//*[local-name()='tspan'])[49]")
    private WebElement costOutlierPartName;

    @FindBy(id = "jr-ui-datepicker-div")
    private WebElement datePickerDiv;

    @FindBy(xpath = "//button[contains(text(), 'Close')]")
    private WebElement datePickerCloseButton;

    @FindBy(xpath = "//a[@title='Scenario']")
    private WebElement useLatestExportDropdown;

    @FindBy(xpath = "//div[@id='earliestExportDate']//div")
    private WebElement earliestExportSetDateError;

    @FindBy(xpath = "//div[@id='latestExportDate']//div")
    private WebElement latestExportSetDateError;

    @FindBy(xpath = "//div[@id='sortOrder']//a")
    private WebElement sortOrderDropdown;

    @FindBy(xpath = "//div[@id='sortOrder']//input")
    private WebElement sortOrderDropdownInput;

    @FindBy(xpath = "//div[@id='costMetric']//a")
    private WebElement costMetricDropdown;

    @FindBy(xpath = "(//*[@class='highcharts-root']//*[local-name() = 'tspan'])[2]")
    private WebElement costMetricElementOnChartAxis;

    @FindBy(xpath = "//span[contains(text(), 'Cost Metric:')]/../following-sibling::td[2]")
    private WebElement costMetricElementAboveChart;

    @FindBy(xpath = "(//*[local-name() = 'tspan'])[6]")
    private WebElement costMetricValueOnBubble;

    @FindBy(xpath = "//div[@id='massMetric']//a")
    private WebElement massMetricDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Mass Metric:')]/../following-sibling::td[2]")
    private WebElement massMetricElementAboveChart;

    @FindBy(xpath = "(//*[local-name() = 'tspan'])[4]")
    private WebElement massMetricValueOnBubble;

    @FindBy(css = "ul[id='resultsList']")
    private WebElement generalReportsList;

    @FindBy(xpath = "(//li[@title='Casting - Die'])[1]/div/a")
    private WebElement dieCastingOption;

    @FindBy(xpath = "(//li[@title='Casting - Sand'])[1]/div/a")
    private WebElement sandCastingOption;

    @FindBy(xpath = "//span[contains(text(), 'Process Group:')]/../following-sibling::td[2]/span")
    private WebElement processGroupCurrentValueCastingDtc;

    @FindBy(xpath = "//span[contains(text(), 'Process Group:')]/../following-sibling::td[1]/span")
    private WebElement processGroupCurrentValueDtcPartSummary;

    @FindBy(xpath = "//label[@title='Component Select']//a")
    private WebElement componentSelectDropdown;

    @FindBy(xpath = "//label[@title='Component Select']//input")
    private WebElement componentSelectSearchInput;

    @FindBy(xpath = "//*[@id='reportViewer']/div[10]/div/div/div/ul")
    private WebElement componentSelectUnorderedList;

    @FindBy(xpath = "//span[contains(text(), 'Process Group:')]/../following-sibling::td[1]/span")
    private WebElement dtcPartSummaryProcessGroupValue;

    @FindBy(xpath = "//span[contains(text(), 'No data available')]")
    private WebElement noDataAvailableElement;

    @FindBy(xpath = "//*[local-name() = 'g' and @data-z-index='8']")
    private WebElement dtcTooltipElement;

    @FindBy(xpath = "//span[contains(text(), 'Component Cost')]")
    private WebElement componentCostReportTitle;

    @FindBy(xpath = "//span[contains(text(), 'Part Number:')]/../following-sibling::td[1]/span")
    private WebElement componentCostReportPartNumber;

    @FindBy(xpath = "(//*[local-name() = 'tspan'])[9]")
    private WebElement dtcScoreValueOnBubble;

    @FindBy(xpath = "//span[contains(text(), 'DTC Score:')]/../following-sibling::td[2]")
    private WebElement dtcScoreValueAboveChart;

    @FindBy(xpath = "//div[@title='Created By']//input[@placeholder='Search list...']")
    private WebElement createdBySearchInput;

    @FindBy(xpath = "(//div[@title='Created By']//ul)[1]")
    private WebElement createdByListElement;

    @FindBy(css = "div[id='assemblyNumber'] input")
    private WebElement assemblyNumberSearchCriteria;

    @FindBy(css = "label[title='Assembly Select'] span[class='warning']")
    private WebElement assemblyNumberSearchCriteriaError;

    @FindBy(css = "label[title='Minimum aPriori Cost'] span[class='warning']")
    private WebElement minAprioriCostError;

    @FindBy(css = "label[title='Maximum aPriori Cost'] span[class='warning']")
    private WebElement maxAprioriCostError;

    @FindBy(xpath = "//span[@class='_jrHyperLink Reference']")
    private WebElement dtcPartSummaryPartName;

    @FindBy(xpath = "(//*[local-name()='tspan'])[62]")
    private WebElement costOutlierAprioriCost;

    @FindBy(xpath = "(//*[local-name()='tspan'])[68]")
    private WebElement costOutlierAprioriCostBottom;

    @FindBy(xpath = "(//*[local-name()='tspan'])[7]")
    private WebElement designOutlierAprioriCost;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[2]")
    private WebElement tooltipFinishMassName;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[3]")
    private WebElement tooltipFinishMassValue;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[4]")
    private WebElement tooltipFbcName;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[5]")
    private WebElement tooltipFbcValue;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[6]")
    private WebElement tooltipDtcScoreName;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[7]")
    private WebElement tooltipDtcScoreValue;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[8]")
    private WebElement tooltipAnnualSpendName;

    @FindBy(xpath = "(//*[local-name() = 'text' and @style='font-size:12px;color:#333333;fill:#333333;']/*)[9]")
    private WebElement tooltipAnnualSpendValue;

    @FindBy(xpath = "(((//div[@class='highcharts-container '])[2]//*[local-name()='g'])[7]/*[local-name()='rect'])" +
            "[13]")
    private WebElement machiningDtcComparisonBar;

    @FindBy(xpath = "(((//div[@class='highcharts-container '])[2]//*[local-name()='g'])[16]/*[local-name()='text'])" +
            "[13]")
    private WebElement machiningDtcComparisonPartName;

    @FindBy(xpath = "//table/tbody/tr[13]/td[2]")
    private WebElement machiningDtcDetailsPartNameLink;

    @FindBy(xpath = "//span[contains(text(), 'Minimum Annual Spend:')]/../following-sibling::td[2]/span")
    private WebElement minimumAnnualSpend;

    @FindBy(xpath = "//label[@title='Minimum Annual Spend']/input")
    private WebElement minimumAnnualSpendInput;

    @FindBy(xpath = "//tr[13]/td[20]/span")
    private WebElement annualSpendDetailsValue;

    @FindBy(xpath = "(//*[@class='highcharts-root'])[1]//*[contains(@class, 'highcharts-xaxis-labels')]")
    private WebElement chartCountElement;

    @FindBy(xpath = "//table/tbody/tr[13]/td[28]/span")
    private WebElement machiningDtcDetailsRowOneSharpCornerIssues;

    @FindBy(xpath = "//table/tbody/tr[13]/td[30]/span")
    private WebElement machiningDtcDetailsRowOneObstructedCornerIssues;

    @FindBy(xpath = "//table/tbody/tr[13]/td[32]/span")
    private WebElement machiningDtcDetailsRowOneLdRatioIssues;

    @FindBy(xpath = "//table/tbody/tr[15]/td[28]/span")
    private WebElement machiningDtcDetailsRowTwoSharpCornerIssues;

    @FindBy(xpath = "//table/tbody/tr[15]/td[30]/span")
    private WebElement machiningDtcDetailsRowTwoObstructedCornerIssues;

    @FindBy(xpath = "//table/tbody/tr[15]/td[32]/span")
    private WebElement machiningDtcDetailsRowTwoLdRatioIssues;

    @FindBy(xpath = "//table/tbody/tr[13]/td[42]/span")
    private WebElement machiningDtcDetailsRowOneTolerances;

    @FindBy(xpath = "//table/tbody/tr[15]/td[42]/span")
    private WebElement machiningDtcDetailsRowTwoTolerances;

    @FindBy(xpath = "//table/tbody/tr[13]/td[34]/span")
    private WebElement machiningDtcDetailsRowOneTime;

    @FindBy(xpath = "//table/tbody/tr[15]/td[34]/span")
    private WebElement machiningDtcDetailsRowTwoTime;

    @FindBy(xpath = "//table/tbody/tr[13]/td[20]/span")
    private WebElement machiningDtcDetailsRowOneAnnualSpend;

    @FindBy(xpath = "//table/tbody/tr[15]/td[20]/span")
    private WebElement machiningDtcDetailsRowTwoAnnualSpend;

    @FindBy(xpath = "//table/tbody/tr[13]/td[24]/span")
    private WebElement machiningDtcDetailsRowOneDtcRank;

    @FindBy(xpath = "//table/tbody/tr[17]/td[24]/span")
    private WebElement machiningDtcDetailsRowThreeDtcRank;

    @FindBy(xpath = "//table/tbody/tr[13]/td[38]/span")
    private WebElement machiningDtcDetailsRowOneFillet;

    @FindBy(xpath = "//table/tbody/tr[13]/td[40]/span")
    private WebElement machiningDtcDetailsRowOneHole;

    @FindBy(xpath = "//table/tbody/tr[17]/td[38]/span")
    private WebElement machiningDtcDetailsRowTwoFillet;

    @FindBy(xpath = "//table/tbody/tr[17]/td[40]/span")
    private WebElement machiningDtcDetailsRowTwoHole;

    @FindBy(xpath = "//span[contains(text(), 'details')]")
    private WebElement costOutlierDetailsLink;

    @FindBy(xpath = "(//*[local-name()='g'])[13]//*[local-name()='rect'][1]")
    private WebElement castingDtcComparisonFirstBarFirstChart;

    @FindBy(xpath = "(//*[@style='font-size: 10px'])[1]")
    private WebElement dtcComparisonPartNameTableOne;

    @FindBy(xpath = "(//*[@style='font-weight:bold'])[2]")
    private WebElement dtcComparisonDtcMaterialIssues;

    @FindBy(xpath = "(//*[@style='font-weight:bold'])[3]")
    private WebElement dtcComparisonDtcRadiusIssues;

    @FindBy(xpath = "(//*[@style='font-weight:bold'])[4]")
    private WebElement dtcComparisonDtcDraftIssues;

    @FindBy(xpath = "(//span[@class='_jrHyperLink ReportExecution']/span)[1]")
    private WebElement castingDtcDetailsComparisonPartNameRowOne;

    @FindBy(xpath = "(//div[@title='Scenario Name']//ul)[1]/li[1]")
    private WebElement firstScenarioName;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[16]/td[27]")
    private WebElement costOutlierApCostOne;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[18]/td[27]")
    private WebElement costOutlierApCostTwo;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[10]/td[24]")
    private WebElement designOutlierApCostOne;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[84]/td[24]")
    private WebElement designOutlierApCostTwo;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[10]/td[24]")
    private WebElement designOutlierMassOne;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[36]/td[21]")
    private WebElement designOutlierMassTwo;

    protected final String genericDeselectLocator = "//span[contains(text(), '%s')]/..//li[@title='Deselect All']";

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public GenericReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseDtcComparisonDtcIssueMap();
        initialiseCostDesignOutlierMap();
        initialiseTooltipElementMap();
        initialiseDtcScoreBubbleMap();
        initialiseCurrencyMap();
        initialisePartNameMap();
        initialiseBubbleMap();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(okButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
    }

    /**
     * Selects specified export set
     *
     * @param exportSet - String of export set name for locator
     * @return current page object
     */
    public <T> T selectExportSet(String exportSet, Class<T> className) {
        By locator = By.xpath(String.format("//li[@title='%s']/div/a", exportSet));
        driver.findElement(locator).click();
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Select export set for dtc tests
     *
     * @param exportSet String
     * @return instance of GenericReportPage
     */
    public ComponentCostReportPage selectExportSetDtcTests(String exportSet) {
        By locator = By.xpath(String.format("//li[@title='%s']/div/a", exportSet));
        pageUtils.waitForElementAndClick(locator);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return new ComponentCostReportPage(driver);
    }

    /**
     * Waits for specified export set to be selected
     *
     * @param exportSet String - export set to wait for selection of
     * @return instance of GenericReportPage
     */
    public GenericReportPage waitForExportSetSelection(String exportSet) {
        By locator = By.xpath(String.format("(//li[@title='%s' and contains(@class, 'jr-isSelected')])[1]", exportSet));
        pageUtils.waitForElementToAppear(locator);
        return this;
    }

    /**
     * Inputs Minimum Annual Spend
     *
     * @return current page object
     */
    public GenericReportPage inputMinimumAnnualSpend() {
        minimumAnnualSpendInput.clear();
        pageUtils.javaScriptClick(minimumAnnualSpendInput);
        pageUtils.waitForElementToAppear(By.xpath("//label[@title='Minimum Annual Spend']/input[@value='']"));
        minimumAnnualSpendInput.sendKeys("6631000");
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        clickCurrencyTwice();
        pageUtils.waitForElementToAppear(By.xpath("//label[@title='Minimum Annual Spend']/input[@value='6631000']"));
        return this;
    }

    /**
     * Wait for minimum annual spend to appear on chart
     */
    public void waitForMinimumAnnualSpendOnChart() {
        By locator = By.xpath("//span[contains(text(), 'Minimum Annual Spend')]/../following-sibling::td[2]/span[contains(text(), '6,631,000.00')]");
        pageUtils.waitForElementToAppear(locator);
    }

    /**
     * Inputs annualised or percent value
     *
     * @param annualisedOrPercent String
     * @param inputValue String
     * @return GenericReportPage instance
     */
    public GenericReportPage inputAnnualisedOrPercentValue(String annualisedOrPercent, String inputValue) {
        By locator = By.xpath(String.format("//label[contains(@title, '%s')]/input", annualisedOrPercent));
        pageUtils.waitForElementAndClick(locator);
        WebElement inputField = driver.findElement(locator);
        pageUtils.clearInput(inputField);

        pageUtils.waitForSteadinessOfElement(locator);
        inputField.sendKeys(inputValue);

        return this;
    }

    /**
     * Gets cost outlier report annualised or percent value from above chart
     *
     * @param isPercentSet boolean
     * @param annualisedOrPercent String
     * @return String - percent value
     */
    public String getCostOutlierAnnualisedOrPercentValueFromAboveChart(boolean isPercentSet,
                                                                       String annualisedOrPercent) {
        String valueIndex = isPercentSet && annualisedOrPercent.equals("Percent") ? "2" : "1";
        By locator = By.xpath(
            String.format(
                "//span[contains(text(), '%s')]/../following-sibling::td[%s]/span",
                annualisedOrPercent,
                valueIndex)
        );
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Get Export Set name
     *
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
     *
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
     * Method to set process group
     *
     * @param processGroupOption - String
     * @return instance of current page object
     */
    public GenericReportPage setProcessGroup(String processGroupOption) {
        pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "Process Group")));
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        waitForCorrectAvailableSelectedCount("", "Selected: ", "0");

        By locator = By.xpath(String.format("(//li[@title='%s'])[1]/div/a", processGroupOption));
        pageUtils.waitForElementToAppear(locator);
        pageUtils.waitForElementAndClick(driver.findElement(locator));
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);

        By locator2 = By.xpath(
                String.format("(//li[@title='%s' and contains(@class, 'jr-isSelected')])[1]", processGroupOption));
        pageUtils.waitForElementToAppear(locator2);
        waitForCorrectAvailableSelectedCount("", "Selected: ", "1");
        return this;
    }

    /**
     * Checks if Process Group warning message is displayed and enabled
     *
     * @param listName - String of list to use
     * @return boolean - is list displayed and enabled
     */
    public boolean isListWarningDisplayedAndEnabled(String listName) {
        By locator = By.xpath(
                String.format("//div[@id='%s']//span[contains(text(), 'This field is mandatory')]", listName));
        WebElement elementToClick = listName.equals("processGroup") ? dtcScoreControlTitle : selectPartsControlTitle;
        elementToClick.click();
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).isDisplayed() && driver.findElement(locator).isEnabled();
    }

    /**
     * Gets text of Process Group warning
     *
     * @param listName - String of list name
     * @return String - list warning text
     */
    public String getListWarningText(String listName) {
        By locator = By.xpath(
                String.format("//div[@id='%s']//span[contains(text(), 'This field is mandatory')]", listName));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Deselects any selected export sets
     *
     * @return GenericReportPage instance
     */
    public GenericReportPage deselectAllProcessGroups() {
        pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "Process Group")));
        return this;
    }

    /**
     * Gets current Process Group value
     *
     * @param reportName - String of report name to use
     * @return String - process group value
     */
    public String getProcessGroupValueDtc(String reportName) {
        String indexToUse = reportName.equals(ReportNamesEnum.DTC_PART_SUMMARY.getReportName()) ? "1" : "2";
        By genericLocator = By.xpath(String.format(
                "//span[contains(text(), 'Process Group:')]/../following-sibling::td[%s]/span", indexToUse));
        pageUtils.waitForElementToAppear(genericLocator);
        return driver.findElement(genericLocator).getText();
    }

    /**
     * Checks current currency selection, fixes if necessary
     *
     * @param currency - String
     * @param <T> - generic
     * @param className - class to return instance of
     * @return instance of class passed in
     */
    public <T> T checkCurrencySelected(String currency, Class<T> className) {
        if (!currentCurrencyElement.getAttribute("title").equals(currency)) {
            pageUtils.waitForElementAndClick(currentCurrencyElement);
            pageUtils.waitForElementAndClick(currencyMap.get(currency));
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects cost metric, if necessary
     *
     * @param costMetric - String
     * @return current page object
     */
    public GenericReportPage selectCostMetric(String costMetric) {
        if (!costMetricDropdown.getAttribute("title").equals(costMetric)) {
            pageUtils.waitForElementAndClick(costMetricDropdown);
            pageUtils.waitForElementAndClick(
                    driver.findElement(By.xpath(String.format("//li[@title='%s']/div/a", costMetric)))
            );
        }
        return this;
    }

    /**
     * Selects mass metric, if necessary
     *
     * @param massMetric - String
     * @return current page object
     */
    public GenericReportPage selectMassMetric(String massMetric) {
        if (massMetric.equals(MassMetricEnum.FINISH_MASS.getMassMetricName())) {
            massMetricDropdown.click();
            driver.findElement(By.xpath(
                    String.format("//li[@title='%s']/div/a", MassMetricEnum.ROUGH_MASS.getMassMetricName()))).click();
        }
        return this;
    }

    /**
     * Selects cost metric, if necessary
     *
     * @param sortOrder - String
     * @return current page object
     */
    public GenericReportPage selectSortOrder(String sortOrder) {
        pageUtils.scrollWithJavaScript(sortOrderDropdown, true);
        pageUtils.waitForElementToAppear(sortOrderDropdown);
        if (!sortOrderDropdown.getAttribute("title").equals(sortOrder)) {
            pageUtils.waitForSteadinessOfElement(By.xpath("//div[@id='sortOrder']//a"));
            sortOrderDropdown.click();
            By inputLocator = By.xpath("//div[@id='sortOrder']//input");
            driver.findElement(inputLocator).click();
            driver.findElement(inputLocator).sendKeys(sortOrder);
            By locator = By.xpath(String.format("//li[@title='%s']/div/a", sortOrder));
            pageUtils.waitForElementToAppear(By.xpath(String.format("//li[@title='%s']/div/a", sortOrder)));
            driver.findElement(locator).click();
            pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@id='sortOrder']//a[@title='%s']", sortOrder)));
            clickUseLatestExportDropdownTwice();
        }
        return this;
    }

    /**
     * Scroll down to specified element
     *
     * @param elementToScrollTo - WebElement
     */
    public void scrollDown(WebElement elementToScrollTo) {
        pageUtils.scrollWithJavaScript(elementToScrollTo, true);
    }

    /**
     * Gets cost metric value from above chart
     *
     * @return String - cost metric value
     */
    public String getCostMetricValueFromAboveChart() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return costMetricElementAboveChart.getText();
    }

    /**
     * Gets cost metric value from chart axis
     *
     * @return String - cost metric value
     */
    public String getCostMetricValueFromChartAxis() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return costMetricElementOnChartAxis.getAttribute("textContent");
    }

    /**
     * Gets cost metric value from bubble
     *
     * @return String - cost metric value
     */
    public String getCostMetricValueFromBubble() {
        pageUtils.waitForElementToAppear(costMetricValueOnBubble);
        return costMetricValueOnBubble.getAttribute("textContent");
    }

    /**
     * Gets mass metric value from above chart
     *
     * @return String - mass metric value
     */
    public String getMassMetricValueFromAboveChart() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return massMetricElementAboveChart.getAttribute("textContent");
    }

    /**
     * Gets mass metric value from bubble
     *
     * @return String - mass metric value
     */
    public String getMassMetricValueFromBubble(String reportName) {
        setReportName(reportName);
        hoverPartNameBubbleDtcReports();
        pageUtils.waitForElementToAppear(massMetricValueOnBubble);
        return massMetricValueOnBubble.getAttribute("textContent");
    }

    /**
     * Waits for new tab switch to occur when going to Casting DTC Comparison Report
     */
    public void waitForNewTabSwitchCastingDtcToComparison() {
        pageUtils.waitForElementToAppear(comparisonTitle);
    }

    /**
     * Opens new tab with CID open and switches to it
     *
     * @param index - int of index to go to
     * @return current page object
     */
    public GenericReportPage openNewCidTabAndFocus(int index) {
        pageUtils.jsNewTab();
        pageUtils.windowHandler(index);

        driver.get(PropertiesContext.get("{env}.cidapp.ui_url"));
        pageUtils.waitForElementToAppear(cidLogo);

        return new GenericReportPage(driver);
    }

    /**
     * Clicks ok
     *
     * @param <T> - generic
     * @param className - className
     * @return Instance of class passed in
     */
    public <T> T clickOk(Class<T> className) {
        Actions actions = new Actions(driver);
        actions.moveToElement(okButton).perform();
        actions.click().perform();
        if (!getInputControlsDivClassName().contains("hidden")) {
            actions.moveToElement(okButton).perform();
            actions.click().perform();
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Waits for loading popup to disappear
     *
     * @return instance of GenericReportPage
     */
    public GenericReportPage waitForLoadingPopupToDisappear() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return this;
    }

    /**
     * Gets specified value from report (Target and Quoted Cost Trend or Value Tracking reports)
     *
     * @param index String - index of value to get
     * @return String - specified value
     */
    public String getValueFromReport(String index) {
        By locator = By.xpath(String.format("//table[contains(@class, 'jrPage')]//tr[22]/td[%s]/span", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Hovers over bar in Casting DTC Comparison Report
     *
     * @param reportName - String of report name
     */
    public void hoverBarDtcComparison(String reportName) {
        String indexToUse = reportName.contains("Plastic") ? "9" : "13";
        By locator = By.xpath(String.format("(//*[local-name()='g'])[%s]//*[local-name()='rect'][1]", indexToUse));
        pageUtils.waitForElementToAppear(locator);
        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(locator)).build().perform();
    }

    /**
     * Gets part name from DTC Comparison report tooltip
     *
     * @return String - part name
     */
    public String getPartNameDtcComparisonTooltip() {
        pageUtils.waitForElementToAppear(dtcComparisonPartNameTableOne);
        return dtcComparisonPartNameTableOne.getAttribute("textContent");
    }

    /**
     * Get DTC Issue Value from Casting DTC Comparison Report
     *
     * @param valueToGet - String of value to retrieve
     * @return String - DTC issue value
     */
    public String getDtcIssueValueDtcComparison(String valueToGet) {
        WebElement elementToUse = dtcComparisonDtcIssueMap.get(valueToGet);
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.getAttribute("textContent");
    }

    /**
     * Click comparison link on Casting DTC Report
     *
     * @return instance of current page object
     */
    public GenericReportPage clickComparison() {
        pageUtils.waitForElementToAppear(castingDtcBubble);
        pageUtils.waitForElementAndClick(comparisonTitle);
        return this;
    }

    /**
     * Wait for export set list count to be zero
     *
     * @param listName - String of list name to use
     * @param expectedCount - String of expected count to wait on
     * @return current page object
     */
    public GenericReportPage waitForCorrectExportSetListCount(String listName, String expectedCount) {
        waitForCorrectAvailableSelectedCount(listName, "Available: ", expectedCount);
        waitForCorrectAvailableSelectedCount(listName, "Selected: ", expectedCount);
        return this;
    }

    /**
     * Sets export set date to now or two days from now using input field
     *
     * @param isEarliestAndToday - boolean to determine element to use and date to set
     * @param invalidValue - invalid value to use
     * @return instance of current page object
     */
    public GenericReportPage setExportDateUsingInput(boolean isEarliestAndToday, String invalidValue) {
        String dateToUse = isEarliestAndToday ? getCurrentDate() : getDateTwoDaysAfterCurrent();
        WebElement dateInputToUse = isEarliestAndToday ? earliestExportDateInput : latestExportDateInput;
        String valueToInput = invalidValue.isEmpty() ? dateToUse : invalidValue;

        dateInputToUse.clear();
        dateInputToUse.click();
        By locator = By.xpath("//input[contains(@class, 'date') and @value='']");
        pageUtils.waitForElementToAppear(locator);
        pageUtils.waitForSteadinessOfElement(locator);
        dateInputToUse.sendKeys(valueToInput);

        return this;
    }

    /**
     * Click Use Latest Scenario dropdown twice to remove focus from date
     *
     * @return Generic Report Page instance
     */
    public GenericReportPage clickUseLatestExportDropdownTwice() {
        useLatestExportDropdown.click();
        useLatestExportDropdown.click();
        return this;
    }

    /**
     * Gets isDisplayed and isEnabled for either export set filter error
     *
     * @param isEarliest - boolean of isEarliest to determine which WebElement to use
     * @return boolean of error displayed and enabled
     */
    public boolean isExportSetFilterErrorDisplayedAndEnabled(boolean isEarliest) {
        WebElement elementToWaitFor = isEarliest ? earliestExportSetDateError : latestExportSetDateError;
        pageUtils.waitForElementToAppear(elementToWaitFor);
        pageUtils.waitForElementToAppear(latestExportSetDateError);
        return isEarliest ? earliestExportSetDateError.isDisplayed() && earliestExportSetDateError.isEnabled() :
                latestExportSetDateError.isDisplayed() && latestExportSetDateError.isEnabled();
    }

    /**
     * Gets export set error text
     *
     * @param isEarliest - boolean of isEarliest to determine which WebElement to use
     * @return String of export set error text
     */
    public String getExportSetErrorText(boolean isEarliest) {
        return isEarliest ? earliestExportSetDateError.getText() : latestExportSetDateError.getText();
    }

    /**
     * Sets export date to now or two days from now using date picker
     *
     * @param isEarliestAndToday - boolean to determine element to use and date to set
     * @return instance of current page object
     */
    public GenericReportPage setExportDateUsingPicker(boolean isEarliestAndToday) {
        LocalDateTime newDt = isEarliestAndToday ? getCurrentDateLDT() : getCurrentDateLDT().plusDays(2);
        WebElement pickerTrigger = isEarliestAndToday ? earliestExportSetDatePickerTriggerBtn :
                latestExportSetDatePickerTriggerBtn;
        pageUtils.waitForElementAndClick(pickerTrigger);

        setDayValuePicker(newDt.getDayOfMonth());
        setMonthValuePicker((newDt.getMonthValue() - 1));
        setYearValuePicker(String.format("%d", newDt.getYear()));
        pickerTrigger.click();

        if (datePickerDiv.getAttribute("style").contains("display: block;")) {
            datePickerCloseButton.click();
            By locator = By.cssSelector("span[class='button picker calTriggerWrapper']");
            pageUtils.waitForElementToAppear(locator);
        }

        return this;
    }

    /**
     * Generic method to wait for correct currency and return specified page object
     *
     * @param currencyToCheck String
     * @param className generic
     * @param <T> return type - any page object that is specified
     * @return new instance of page object
     */
    public <T> T waitForCorrectCurrency(String currencyToCheck, Class<T> className) {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        By locator = By.xpath(String.format("//table//span[contains(text(), '%s')]", currencyToCheck));
        pageUtils.waitForElementToAppear(locator);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Wait for report to load
     */
    public void waitForReportToLoad() {
        pageUtils.waitForElementNotDisplayed(inputControlsDiv, 1);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
    }

    /**
     * Waits for SVG to render
     */
    public void waitForSvgToRender() {
        By locator = By.xpath("" +
            "*[local-name()='svg']");
        pageUtils.waitForElementToAppear(locator);
        pageUtils.isElementDisplayed(locator);
        pageUtils.isElementEnabled(driver.findElement(locator));
    }

    /**
     * Waits for sort order to appear on report
     */
    public void waitForSortOrderToAppearOnReport() {
        pageUtils.waitForElementToAppear(
                By.xpath("//span[contains(text(), 'Sort Metric')]/../following-sibling::td[2]/span")
        );
    }

    /**
     * Gets current currency setting
     *
     * @return String of current currency
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
        waitForCorrectAvailableSelectedCount(ListNameEnum.EXPORT_SET.getListName(), "Selected: ", exportSetCount);
        return this;
    }

    /**
     * Gets number of currently available export sets
     *
     * @return String - count of export sets
     */
    public String getCountOfExportSets() {
        return availableExportSets.getText().substring(11);
    }

    /**
     * Gets number of currently available list items
     *
     * @param listName - String of list name to use
     * @param option - String of option to get value of
     * @return String - count of list items
     */
    public String getCountOfListAvailableOrSelectedItems(String listName, String option) {
        int substringVal = option.equals("Available") ? 11 : 10;
        By locator = By.xpath(String.format("//div[@title='%s']//span[contains(@title, '%s')]", listName, option));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText().substring(substringVal);
    }

    /**
     * Gets specified component name
     *
     * @param index - String of index to use
     * @return String of component name
     */
    public String getComponentName(String index) {
        By locator = By.xpath(String.format("(//div[@title='Scenario Type']//ul)[1]/li[%s]", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Gets first scenario name
     *
     * @return String of first scenario name
     */
    public String getFirstScenarioName() {
        pageUtils.waitForElementToAppear(firstScenarioName);
        return firstScenarioName.getAttribute("title");
    }

    /**
     * Selects default scenario name (Initial)
     *
     * @param <T> - generic
     * @param className - class name to return instance of
     * @return instance of specified class
     */
    public <T> T selectDefaultScenarioName(Class<T> className) {
        pageUtils.scrollWithJavaScript(driver.findElement(By.xpath("//div[@title='Scenario Name']")), true);
        By inputLocator = By.xpath("//div[@title='Scenario Name']//input[contains(@class, 'jr-mInput-search jr')]");
        pageUtils.waitForElementAndClick(inputLocator);

        pageUtils.waitForSteadinessOfElement(inputLocator);
        driver.findElement(inputLocator).sendKeys("Initial");

        By locator = By.xpath("//li[@title='Initial']/div/a");
        pageUtils.waitForElementAndClick(locator);

        waitForCorrectAvailableSelectedCount(ListNameEnum.SCENARIO_NAME.getListName(), "Selected: ", "1");

        if (className.equals(ScenarioComparisonReportPage.class)) {
            By filteredLocator = By.xpath("((//div[@title='Scenarios to Compare']//ul)[1]/li[contains(@title, " +
                    "'(Initial)')])[1]");
            pageUtils.waitForElementToAppear(filteredLocator);
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects first two Scenarios to compare
     *
     * @return GenericReportPage instance
     */
    public GenericReportPage selectFirstTwoComparisonScenarios() {
        waitForCorrectAvailableSelectedCount(ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available: ",
            "14");

        for (int i = 1; i < 3; i++) {
            By locator = By.xpath(String.format("(//div[@title='Scenarios to Compare']//ul)[1]/li[%s]/div/a", i));
            pageUtils.waitForElementAndClick(locator);
            if (i == 1) {
                By postFilterLocator = By.xpath(String.format(
                        "(//div[@title='Scenarios to Compare']//ul)[1]/li[%s and @class='jr-mSelectlist-item " +
                                "jr-isHovered jr jr-isSelected']",
                        i
                ));
                pageUtils.waitForElementToAppear(postFilterLocator);
            }
        }
        return this;
    }

    /**
     * Waits for correct available or selected count in any input controls list
     *
     * @param listName - String
     * @param option - String
     * @param expectedCount - String
     */
    public void waitForCorrectAvailableSelectedCount(String listName, String option, String expectedCount) {
        By locator = By.xpath(String.format(
                "//div[contains(@title, '%s')]//span[@title='%s']",
                listName,
                option + expectedCount
        ));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
     * Get number of available export sets
     *
     * @return int of available export set count
     */
    public int getAvailableExportSetCount() {
        String count = pageUtils.waitForElementToAppear(availableExportSets).getAttribute("title");
        count = count.substring((count.lastIndexOf(" ") + 1));
        return Integer.parseInt(count);
    }

    /**
     * Get number of selected export sets
     *
     * @return int of selected export sets
     */
    public int getSelectedExportSetCount() {
        String count = pageUtils.waitForElementToAppear(selectedExportSets).getAttribute("title");
        count = count.substring((count.lastIndexOf(" ") + 1));
        return Integer.parseInt(count);
    }

    /**
     * Deselect export sets
     */
    public void deselectExportSet() {
        String expectedCount = String.valueOf(getSelectedExportSetCount() - 1);
        pageUtils.waitForElementAndClick(exportSetToSelect);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.EXPORT_SET.getListName(),
                "Selected: ",
                expectedCount
        );
    }

    /**
     * Invert export set selection
     */
    public void invertExportSetSelection() {
        String expectedCount = String.valueOf(getAvailableExportSetCount() - getSelectedExportSetCount());
        pageUtils.waitForElementAndClick(exportSetInvert);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.EXPORT_SET.getListName(),
                "Selected: ",
                expectedCount
        );
    }

    /**
     * Deselect all export sets
     */
    public void exportSetDeselectAll() {
        pageUtils.waitForElementAndClick(exportSetDeselect);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        clickUseLatestExportDropdownTwice();
        pageUtils.waitForElementToAppear(By.xpath("(//div[@title='Single export set selection.']//ul)[1]/li[@class = 'jr-mSelectlist-item   jr'][1]"));
        waitForCorrectAvailableSelectedCount(ListNameEnum.EXPORT_SET.getListName(), "Selected: ",
                "0");
    }

    /**
     * Expand rollup drop-down
     *
     * @param rollupName - String of rollup to select
     * @return current page object
     */
    public GenericReportPage selectRollup(String rollupName) {
        if (!rollupDropdown.getAttribute("title").equals(rollupName)) {
            pageUtils.waitForElementAndClick(rollupDropdown);
            pageUtils.waitForElementAndClick(rollupSearchInput);
            pageUtils.waitForSteadinessOfElement(By.xpath("//div[@id='rollup']//div[@class='jr-mSingleselect-search jr "
                    .concat("jr-isOpen']/input")));
            rollupSearchInput.sendKeys(rollupName);
            By rollupLocator = By.xpath(String.format("//li[@title='%s']", rollupName));
            pageUtils.waitForElementToAppear(rollupLocator);
            pageUtils.waitForElementAndClick(rollupLocator);
        }
        return this;
    }

    /**
     * Gets selected rollup from dropdown
     *
     * @param rollupName - String of rollupName
     * @return String
     */
    public String getSelectedRollup(String rollupName) {
        pageUtils.scrollWithJavaScript(driver.findElement(By.xpath("//span[.='* Rollup']")), true);
        By rollUp = By.cssSelector(String.format("a[title='%s']", rollupName));
        pageUtils.waitForElementToAppear(rollUp);
        return driver.findElement(rollUp).getAttribute("title");
    }

    /**
     * Gets invalid date
     *
     * @param datePartToInvalidate String
     * @return String of invalid date
     */
    public String getInvalidDate(String datePartToInvalidate) {
        String currentDate = getCurrentDate();
        Map<String, String> invalidDates = new HashMap<>();
        invalidDates.put("yyyy", String.valueOf(LocalDateTime.now().plusYears(1).getYear()));
        invalidDates.put("MM", "13");
        invalidDates.put("dd", "32");
        invalidDates.put("HH", "25");
        invalidDates.put("mm", "65");

        String newVal = "";
        switch (datePartToInvalidate) {
            case "yyyy":
                newVal = currentDate.substring(0, 4);
                break;
            case "MM":
                newVal = currentDate.substring(5, 7);
                break;
            case "dd":
                newVal = currentDate.substring(8, 10);
                break;
            case "HH":
                newVal = currentDate.substring(11, 13);
                break;
            case "mm":
                newVal = currentDate.substring(14, 16);
                break;
            default:
                newVal = "";
        }

        return currentDate.replace(newVal, invalidDates.get(datePartToInvalidate));
    }

    /**
     * Get name of a report
     *
     * @param reportName - String of report name to use
     * @return String - text of report name
     */
    public String getReportName(String reportName) {
        return driver.findElement(By.xpath(String.format("//a[text() = '%s']", reportName))).getText();
    }

    /**
     * Waits for report to appear
     *
     * @param reportName - String report name to wait on
     */
    public GenericReportPage waitForReportToAppear(String reportName) {
        pageUtils.waitForElementToAppear(By.xpath(String.format("//a[text() = '%s']", reportName)));
        return this;
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
     * @return instance of GenericReportPage
     */
    public GenericReportPage clickCancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementNotDisplayed(inputControlsDiv, 1);
        return this;
    }

    /**
     * Click reset
     *
     * @return current page object
     */
    public GenericReportPage clickReset() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementAndClick(resetButton);
        resetButton.click();
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
     * @param saveName - String name to save as
     * @return current page object
     */
    public GenericReportPage enterSaveName(String saveName) {
        pageUtils.waitForElementAndClick(saveInput);
        pageUtils.clearInput(saveInput);
        pageUtils.waitForSteadinessOfElement(By.xpath("input[id='savedValuesName']"));
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
     * @param optionsName - String of option to select
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
     * @param exportSetName - String of export set name
     * @return boolean - if export set selected
     */
    public boolean isExportSetSelected(String exportSetName) {
        pageUtils.waitForElementToAppear(exportSetList);
        List<WebElement> childElements = exportSetList.findElements(By.tagName("li"));

        return childElements.stream().anyMatch(webElement -> (webElement.getAttribute("title").contains(exportSetName)
            && webElement.getAttribute("class").contains("isHovered")));
    }

    /**
     * Click remove button
     */
    public void clickRemove() {
        pageUtils.waitForElementAndClick(removeButton);
        pageUtils.waitForElementAndClick(confirmRemove);
    }

    /**
     * Option in dropdown
     *
     * @param optionName - String of option to check for
     * @param expected - int of expected count
     * @return boolean - is option available in dropdown
     */
    public boolean isOptionInDropDown(String optionName, int expected) {
        pageUtils.waitForElementToAppear(
                By.xpath(String.format("//select[@id='reportOptionsSelect' and count(option) = %s]", expected)));
        if (driver.findElements(By.xpath("//div[@id='inputControls']//div[@class='sub header hidden']")).size() > 0) {
            return false;
        } else {
            pageUtils.waitForElementToAppear(savedOptionsDropDown);
            Select dropDown = new Select(savedOptionsDropDown);
            List<WebElement> options = dropDown.getOptions();

            return options.stream().anyMatch(webElement -> webElement.getText().equals(optionName));
        }
    }

    /**
     * Wait for expected export count
     *
     * @param expected - String of expected count
     * @return GenericReportPage instance
     */
    public GenericReportPage waitForExpectedExportCount(String expected) {
        waitForCorrectAvailableSelectedCount(ListNameEnum.EXPORT_SET.getListName(), "Selected: ", expected);
        return this;
    }

    /**
     * Waits for part list to have expected count
     *
     * @param expected - String of expected count
     * @return GenericReportPage instance
     */
    public GenericReportPage waitForCorrectPartListCount(String expected) {
        waitForCorrectAvailableSelectedCount(ListNameEnum.PARTS.getListName(), "Available: ", expected);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return this;
    }

    /**
     * Method to return value from Bubble in DTC Casting or Machining DTC Report
     *
     * @param valueIndexToGet - String of value index to get
     * @return BigDecimal of value retrieved
     */
    public BigDecimal getFBCValueFromBubbleTooltip(String valueIndexToGet) {
        WebElement elementToUse = tooltipElementMap.get(valueIndexToGet);
        pageUtils.waitForElementToAppear(elementToUse);

        return new BigDecimal(
                elementToUse.getText()
                        .replace(",", "")
        );
    }

    /**
     * Get Annual Spend value from Plastic Dtc Report Bubble
     *
     * @return BigDecimal of value retrieved
     */
    public BigDecimal getAnnualSpendFromBubbleTooltip() {
        WebElement elementToUse = tooltipElementMap.get("Annual Spend Value");
        pageUtils.waitForElementToAppear(elementToUse);
        return new BigDecimal(elementToUse.getText().replace(",", ""));
    }

    /**
     * Get Minimum Annual Spend value
     *
     * @return String of minimum annual spend
     */
    public String getMinimumAnnualSpendFromAboveChart() {
        By locator = By.xpath("//span[contains(text(), 'Minimum Annual Spend:')]/../following-sibling::td[2]/span");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Sets report name
     *
     * @param reportName - String of report name to set
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * Hovers over bubble in DTC Reports
     */
    public void hoverPartNameBubbleDtcReports() {
        WebElement elementToUse = bubbleMap.get(reportName);
        pageUtils.waitForElementToAppear(elementToUse);
        Actions builder = new Actions(driver).moveToElement(elementToUse);
        builder.build().perform();

        if (this.reportName.equals(ReportNamesEnum.PLASTIC_DTC.getReportName())) {
            elementToUse.click();
        }
    }

    /**
     * Hovers over the relevant bubble for any DTC Report test
     * @param bubbleIndex String
     */
    public void hoverSpecificPartNameBubble(String bubbleIndex) {
        By locator = By.xpath(
                String.format("//*[@class='highcharts-series-group']//*[%s][local-name() = 'path']", bubbleIndex));
        pageUtils.waitForElementToAppear(locator);
        Actions builder = new Actions(driver).moveToElement(driver.findElement(locator));
        builder.build().perform();
    }

    /**
     * Hover DTC Score bubble
     *
     * @param dtcScore String of dtc score bubble index to hover over
     */
    public void hoverBubbleDtcScoreDtcReports(String dtcScore) {
        WebElement elementToUse = dtcScoreBubbleMap.get(dtcScore);
        pageUtils.waitForElementToAppear(elementToUse);
        Actions builder = new Actions(driver).moveToElement(elementToUse);
        builder.perform();
    }

    /**
     * Hovers over Machining DTC Bubble twice
     */
    public void hoverMachiningBubbleTwice() {
        pageUtils.waitForElementToAppear(machiningDtcBubbleTwo);
        setReportName(ReportNamesEnum.MACHINING_DTC.getReportName().concat(" 2"));
        hoverPartNameBubbleDtcReports();
        waitForCorrectPartNameMachiningDtc(true);
        hoverPartNameBubbleDtcReports();
    }

    /**
     * Waits for correct Part Name
     *
     * @param initialCall - boolean to determine which part name to wait for
     */
    public void waitForCorrectPartNameMachiningDtc(boolean initialCall) {
        String partNameToExpect = initialCall ? Constants.PART_NAME_INITIAL_EXPECTED_MACHINING_DTC :
                Constants.PART_NAME_EXPECTED_MACHINING_DTC;
        By locator = By.xpath(String.format("//*[contains(text(), '%s')]", partNameToExpect));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
    * Waits for correct part name
     *
    * @param partName - String of part name to wait for
    */
    public void waitForCorrectPartName(String partName) {
        By locator = By.xpath(String.format("//*[contains(text(), '%s')]", partName));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
     * Hovers bubble one for process group test
     *
     * @param useBubbleOne - boolean to determine which bubble to use
     */
    public void hoverProcessGroupBubble(boolean useBubbleOne) {
        WebElement elementToUse = useBubbleOne ? processGroupBubbleOne : processGroupBubbleTwo;
        pageUtils.waitForElementToAppear(elementToUse);
        Actions builder = new Actions(driver).moveToElement(elementToUse);
        builder.perform();
    }

    /**
     * Select component from dropdown
     *
     * @param componentName String
     * @return GenericReportPage instance
     */
    public GenericReportPage selectComponent(String componentName) {
        pageUtils.scrollWithJavaScript(componentSelectDropdown, true);
        Actions actions = new Actions(driver);
        actions.moveToElement(componentSelectDropdown).perform();
        actions.click().perform();
        pageUtils.waitForElementToAppear(By.xpath("//label[@title='Component Select']//a[contains(@class, 'jr-isFocused')]"));
        pageUtils.waitForSteadinessOfElement(By.xpath("//label[@title='Component Select']//a"));
        pageUtils.waitForElementAndClick(componentSelectDropdown);
        pageUtils.waitForElementToAppear(By.xpath("//label[@title='Component Select']//a[contains(@class, 'jr-isOpen')]"));
        pageUtils.waitForElementAndClick(componentSelectSearchInput);
        componentSelectSearchInput.clear();
        pageUtils.waitForSteadinessOfElement(By.xpath("//label[@title='Component Select']//input"));
        componentSelectSearchInput.sendKeys(componentName);
        By componentToSelectLocator = By.xpath(String.format("//a[contains(text(), '%s')]", componentName));
        pageUtils.waitForElementAndClick(componentToSelectLocator);
        return this;
    }

    /**
     * Get part name from Casting DTC or Machining DTC Report
     *
     * @return String of part name
     */
    public String getPartNameDtcReports() {
        WebElement elementToUse = partNameMap.get(this.reportName);
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.getText();
    }

    /**
     * Gets DTC Score from Bubble in DTC Reports
     *
     * @return String of dtc score
     */
    public String getDtcScoreDtcReports() {
        pageUtils.waitForElementToAppear(dtcScoreValueOnBubble);
        return dtcScoreValueOnBubble.getAttribute("textContent");
    }

    /**
     * Deselects any selected export sets
     *
     * @return GenericReportPage instance
     */
    public GenericReportPage deselectAllDtcScores() {
        pageUtils.scrollWithJavaScript(driver.findElement(By.xpath(String.format(genericDeselectLocator, "DTC Score"))), true);
        pageUtils.waitForSteadinessOfElement(By.xpath(String.format(genericDeselectLocator, "DTC Score")));
        pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "DTC Score")));
        pageUtils.waitForElementToAppear(By.xpath("//div[@id='dtcScore']//span[contains(text(), 'Selected: 0')]"));
        return this;
    }

    /**
     * Gets Input Controls Div Class Name
     *
     * @return String of input controls div class names
     */
    public String getInputControlsDivClassName() {
        return inputControlsDiv.getAttribute("className");
    }

    /**
     * Gets input controls div isDisplayed value
     *
     * @return boolean of is input controls displayed
     */
    public boolean isInputControlsDisplayed() {
        return pageUtils.isElementDisplayed(inputControlsDiv);
    }

    /**
     * Gets input controls div isEnabled value
     *
     * @return boolean of is input controls enabled
     */
    public boolean isInputControlsEnabled() {
        return pageUtils.isElementEnabled(inputControlsDiv);
    }


    /**
     * Get roll-up displayed in header
     *
     * @return String name of displayed rollup
     */
    public String getDisplayedRollup() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementToAppear(headerDisplayedRollup);
        return headerDisplayedRollup.getText();
    }

    /**
     * Check if no data available element is displayed and enabled
     *
     * @param index - String of index for locator
     * @return boolean of is data available label displayed and enabled
     */
    public boolean isDataAvailableLabelDisplayedAndEnabled(String index) {
        WebElement elementToUse = driver.findElement(By.xpath(
                String.format(
                        "(//span[contains(text(), 'No data available')])[%s]",
                        index))
        );
        return elementToUse.isDisplayed() && elementToUse.isEnabled();
    }

    /**
     * Checks if tooltip is displayed
     *
     * @return boolean of is tooltip displayed
     */
    public boolean isTooltipDisplayed() {
        return dtcTooltipElement.getAttribute("opacity").equals("1");
    }

    /**
     * Checks if tooltip element is displayed and enabled
     *
     * @param elementKey - String of element key to use
     * @return boolean of is tooltip element visible
     */
    public boolean isTooltipElementVisible(String elementKey) {
        return tooltipElementMap.get(elementKey).isDisplayed() && tooltipElementMap.get(elementKey).isEnabled();
    }

    /**
     * Gets component cost report part number text
     *
     * @return String of part number
     */
    public String getComponentCostPartNumber() {
        return componentCostReportPartNumber.getText();
    }

    /**
     * Searches for export set
     *
     * @param exportSet - String of export set to search for
     */
    public void searchForExportSet(String exportSet) {
        pageUtils.waitForElementAndClick(exportSetSearchInput);
        exportSetSearchInput.sendKeys(exportSet);
        pageUtils.waitForSteadinessOfElement(By.xpath(
                "//div[@id='exportSetName']//input[contains(@class, 'jr-mInput-search')]"));
        By listLocator = By.xpath(
                "//div[@title='Single export set selection.']//ul[@class='jr-mSelectlist jr' and count(./li/*) = 2]");
        pageUtils.waitForElementToAppear(listLocator);

        By topLevelOptionLocator = By.xpath("//li[@title='---01-top-level-multi-vpe']/div/a");
        pageUtils.waitForElementToAppear(topLevelOptionLocator);
    }

    /**
     * Sets DTC Score Input Control
     *
     * @param dtcScoreOption String
     * @return Instance of current page object
     */
    public GenericReportPage setDtcScore(String dtcScoreOption) {
        String dtcScoreToUse = dtcScoreOption.equals("Medium Casting")
                ? DtcScoreEnum.MEDIUM.getDtcScoreName() : dtcScoreOption;
        if (!dtcScoreToUse.equals(DtcScoreEnum.ALL.getDtcScoreName())) {
            pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "DTC Score")));
            pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
            By locator = By.xpath(String.format("(//li[@title='%s'])[1]/div/a", dtcScoreToUse));
            pageUtils.waitForElementAndClick(locator);
        }
        pageUtils.waitForElementToAppear(By.xpath(String.format("(//li[@title='%s' and contains(@class, 'jr-isSelected')])[1]", dtcScoreOption)));
        String dtcScoreSelectedCount = dtcScoreOption.equals("High, Low, Medium") ? "3" : "1";
        pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@id='dtcScore']//span[@title='Selected: %s']", dtcScoreSelectedCount)));
        return this;
    }

    /**
     * Gets DTC Score value from above chart
     *
     * @return String of dtc score
     */
    public String getDtcScoreAboveChart() {
        pageUtils.waitForElementToAppear(dtcScoreValueAboveChart);
        return dtcScoreValueAboveChart.getText();
    }

    /**
     * Waits for no bubble report to load
     */
    public void waitForNoBubbleReportToLoad() {
        By loc = By.xpath("(//*[@class='highcharts-series-group']//*[local-name() = 'path'])[6]");
        pageUtils.waitForElementToAppear(loc);
    }

    /**
     * Gets all DTC Score Values on screen in details reports
     *
     * @param reportName - String of report name to determine column index to use
     * @return ArrayList of Strings of dtc score values
     */
    public ArrayList<String> getDtcScoreValuesDtcDetailsReports(String reportName) {
        String columnIndex = reportName.equals(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()) ? "23" : "26";
        ArrayList<WebElement> elementArrayList = new ArrayList<>(driver.findElements(
                By.cssSelector(String.format("table.jrPage tbody tr td:nth-of-type(%s) span", columnIndex))));
        ArrayList<String> valuesToReturn = new ArrayList<>();

        for (WebElement element : elementArrayList) {
            if (!element.getText().equals("DTC Score")) {
                valuesToReturn.add(element.getText());
            }
        }
        return valuesToReturn;
    }

    /**
     * Search for name in created by input control
     *
     * @param listName String - list name to use
     * @param inputString String - input string to input
     */
    public void searchListForName(String listName, String inputString) {
        By currentSearchInputLocator = By.xpath(
                String.format("//div[@title='%s']//input[@placeholder='Search list...']", listName));
        WebElement currentSearchInput = driver.findElement(currentSearchInputLocator);
        pageUtils.waitForElementAndClick(currentSearchInput);
        currentSearchInput.clear();
        pageUtils.waitForSteadinessOfElement(currentSearchInputLocator);
        currentSearchInput.sendKeys(inputString);
        if (inputString.equals("fakename")) {
            By locator = By.xpath(String.format(
                    "//div[@title='%s']/div[1]/div/div[2]/div[2]/div/div[@style='height: 1px']", listName));
            pageUtils.waitForElementToAppear(locator);
        }
    }

    /**
     * Checks if created by option is visible and enabled
     *
     * @param listName String - list name to check
     * @param inputString String - input string to input
     * @return boolean of is list option visible
     */
    public boolean isListOptionVisible(String listName, String inputString) {
        By locator = By.xpath(String.format("//div[@title='%s']//li[contains(@title, '%s')]/div/a", listName,
                inputString));
        pageUtils.waitForElementToAppear(locator);
        WebElement optionElement = driver.findElement(locator);
        return optionElement.isEnabled() && optionElement.isEnabled();
    }

    /**
     * Selects one of the names in created by list
     *
     * @param listName String - list name to check
     * @param nameToSelect String - name to select
     */
    public void selectListItem(String listName, String nameToSelect) {
        By locator = By.xpath(String.format("//div[@title='%s']//li[contains(@title ,'%s')]/div/a", listName,
                nameToSelect));
        pageUtils.waitForElementToAppear(locator);
        pageUtils.waitForElementAndClick(locator);
    }

    /**
     * Gets count of created by list items
     *
     * @param listName String - list name to get count from
     * @return String of count of list items in specified list
     */
    public String getCountOfListItems(String listName) {
        By locator = By.xpath(String.format("(//div[@title='%s']//ul)[1]", listName));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("childElementCount");
    }

    /**
     * Clicks Select All option for Created By List
     *
     * @param listName - String of list name to use
     * @param buttonName - String of button name to use
     */
    public void clickListPanelButton(String listName, String buttonName) {
        By buttonLocator = By.xpath(String.format("//div[@title='%s']//li[@title='%s']/a", listName, buttonName));
        pageUtils.waitForElementAndClick(buttonLocator);
    }

    /**
     * Inputs search query into Assembly Number Search Criteria
     *
     * @param inputString - String to input
     */
    public void inputAssemblyNumberSearchCriteria(String inputString) {
        inputString = inputString.isEmpty() ? "random" : inputString;
        pageUtils.waitForElementAndClick(assemblyNumberSearchCriteria);
        assemblyNumberSearchCriteria.clear();
        pageUtils.waitForSteadinessOfElement(By.cssSelector("div[id='assemblyNumber'] input"));
        assemblyNumberSearchCriteria.sendKeys(inputString);
        assemblyNumberSearchCriteria.sendKeys(Keys.ENTER);
        String dropdownText = inputString.equals("random") ? "" : inputString;
        By locator = By.xpath(String.format("//label[@title='Assembly Select']//a[contains(@title, '%s')]",
                dropdownText));
        pageUtils.scrollWithJavaScript(driver.findElement(By.xpath("//label[@title='Assembly Select']//a")),
                true);
        pageUtils.waitForSteadinessOfElement(locator);
        pageUtils.waitForElementToAppear(locator);
        if (inputString.equals("random")) {
            pageUtils.waitForElementToAppear(By.xpath("//span[@class='warning' and contains(text(), 'This field')]"));
        }
    }

    /**
     * Gets currently selected assembly
     *
     * @return String of currently selected assembly
     */
    public String getCurrentlySelectedAssembly() {
        By locator = By.xpath("//label[@title='Assembly Select']//a");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Gets Assembly Number Search Error visibility
     *
     * @return boolean of is error visible
     */
    public boolean isAssemblyNumberSearchErrorVisible() {
        return pageUtils.isElementDisplayed(assemblyNumberSearchCriteriaError) &&
                pageUtils.isElementEnabled(assemblyNumberSearchCriteriaError);
    }

    /**
     * Checks if cost error is enabled
     *
     * @param minOrMax - String of min or max to check
     * @param costOrMass - String of cost or mass to check
     * @return boolean of is error enabled
     */
    public boolean isCostOrMassMaxOrMinErrorEnabled(String minOrMax, String costOrMass) {
        costOrMass = costOrMass.equals("Mass") ? costOrMass.concat(" (kg)") : costOrMass;
        WebElement elementToUse = minOrMax.equals("Minimum") ? minAprioriCostError : maxAprioriCostError;
        By locator = By.xpath(String.format(
                "//label[@title='%s aPriori %s']/span[@class='warning' and contains(text(), "
                        .concat("'Specify')]"), minOrMax, costOrMass));
        pageUtils.waitForElementToAppear(locator);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return pageUtils.isElementEnabled(elementToUse);
    }

    /**
     * Checks if Annualised or Percent error is enabled
     *
     * @param annualisedOrPercent - String to determine which error to check
     * @return boolean of is error enabled
     */
    public boolean isAnnualisedOrPercentErrorEnabled(String annualisedOrPercent) {
        By locator = By.xpath(
                String.format(
                        "//label[contains(@title, '%s')]/span[@class='warning' and contains(text(), 'BigDecimal')]",
                        annualisedOrPercent)
        );
        pageUtils.waitForElementToAppear(locator);
        return pageUtils.isElementEnabled(driver.findElement(locator));
    }

    /**
     * Gets Assembly Number Search Error text
     *
     * @return String of assembly number search error text
     */
    public String getAssemblyNumberSearchErrorText() {
        return assemblyNumberSearchCriteriaError.getText();
    }

    /**
     * Gets Annual Spend value from Details reports
     *
     * @return BigDecimal of annaul spend value
     */
    public BigDecimal getAnnualSpendValueDetailsReports() {
        pageUtils.waitForElementToAppear(annualSpendDetailsValue);
        return new BigDecimal(annualSpendDetailsValue.getText().replace(",", ""));
    }

    /**
     * Gets count of chart elements on comparison reports
     *
     * @return String of count of chart elements
     */
    public Integer getCountOfChartElements() {
        waitForReportToLoad();
        pageUtils.waitForElementToAppear(costMetricElementAboveChart);
        pageUtils.waitForElementToAppear(chartCountElement);
        return Integer.parseInt(chartCountElement.getAttribute("childElementCount"));
    }

    /**
     * Gets DTC Comparison table element name
     *
     * @param tableIndex String of table index
     * @param rowIndex String of row index
     * @return String of element name
     */
    public String getTableElementNameDtcComparison(String tableIndex, String rowIndex) {
        By locator = By.xpath(String.format(
                "((//*[@class='highcharts-axis-labels highcharts-xaxis-labels '])[%s]//*[local-name()='text'])[%s]",
                tableIndex,
                rowIndex)
        );
        return driver.findElement(locator).getText();
    }

    /**
     * Gets part name from Casting DTC Details Report
     *
     * @param getRowOnePartName boolean - to determine what row index to use
     * @return String of part name
     */
    public String getPartNameCastingSheetMetalDtcDetails(boolean getRowOnePartName) {
        String rowIndex = getRowOnePartName ? "1" : "2";
        By locator = By.xpath(String.format("(//span[@class='_jrHyperLink ReportExecution']/span)[%s]", rowIndex));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("textContent");
    }

    /**
     * Gets Part Name row one in Casting DTC Details
     *
     * @return String of part name
     */
    public String getPartNameRowOneCastingDtcDetails() {
        pageUtils.waitForElementToAppear(castingDtcDetailsComparisonPartNameRowOne);
        return castingDtcDetailsComparisonPartNameRowOne.getAttribute("textContent");
    }

    /**
     * Gets DTC Issue Count for draft or radius on Casting DTC Details report
     *
     * @param reportName - String report name to use
     * @param valueToGet - String key of value to get
     * @return String of dtc issue value
     */
    public String getDtcIssueValueDtcDetails(String reportName, String valueToGet) {
        int index;
        if (reportName.equals(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName())) {
            index = valueToGet.equals("Draft") ? 32 : 34;
        } else {
            index = valueToGet.equals("Material") ? 26 : 31;
        }
        By locator = By.xpath(String.format("//table/tbody/tr[13]/td[%s]/span", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Switches tab, if second tab is open
     *
     * @param index of tab to swtich to
     * @return GenericReportPage instance
     */
    public GenericReportPage switchTab(int index) {
        if (driver.getWindowHandles().size() == (index + 1)) {
            pageUtils.windowHandler(index);
        }
        return this;
    }

    /**
     * Inputs value into max or min cost or mass fields
     *
     * @param valueToSet - cost or mass input field
     * @param maxMinValue - max or min input field
     * @param valueToInput - value to input into field
     */
    public void inputMaxOrMinCostOrMass(boolean costReport, String valueToSet, String maxMinValue,
                                        String valueToInput) {
        By costLocator = By.xpath(String.format("//div[@id='componentCost%s']//input", maxMinValue));
        By locator = By.xpath(String.format("//div[@id='aPriori%s%s']//input", valueToSet, maxMinValue));
        By locatorToUse = costReport ? costLocator : locator;
        WebElement input = driver.findElement(locatorToUse);
        pageUtils.waitForElementToAppear(input);
        input.clear();
        input.click();
        pageUtils.waitForSteadinessOfElement(locatorToUse);
        input.sendKeys(valueToInput);
    }

    /**
     * Gets Cost min or max above chart value
     *
     * @param reportName - report name
     * @param massOrCost - max or cost to get
     * @param maxOrMin - max or min value to get
     * @return mass or cost min or max above chart value as String
     */
    public String getMassOrCostMinOrMaxAboveChartValue(String reportName, String massOrCost, String maxOrMin) {
        By generalLocator = By.xpath(
                String.format(
                        "//span[contains(text(), 'aPriori %s %s:')]/ancestor::td/following-sibling::td[1]/span",
                        massOrCost,
                        maxOrMin)
        );
        By designDetailsLocator = By.xpath(
                String.format(
                        "//span[contains(text(), '%simum aPriori %s')]/ancestor::td/following-sibling::td[1]/span",
                        maxOrMin,
                        massOrCost)
        );
        By locator = reportName.equals(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName())
                ? designDetailsLocator : generalLocator;

        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Clicks details link on Cost Outlier Identification Report to go to Details report
     */
    public void clickDetailsLink() {
        By locator = By.xpath("//span[contains(text(), 'details')]");
        pageUtils.waitForSteadinessOfElement(locator);
        pageUtils.waitForElementAndClick(locator);
        pageUtils.windowHandler(1);
        waitForReportToLoad();
    }

    /**
     * Gets value from Design or Cost Outlier Details report table
     *
     * @param valueIndex - String of cost or mass max or min value
     * @return String - value
     */
    public String getCostOrMassMaxOrMinCostOrDesignOutlierDetailsReports(String valueIndex) {
        return costDesignOutlierMap.get(valueIndex).getText();
    }

    /**
     * Gets current date
     *
     * @return String of current date
     */
    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
    }

    /**
     * Gets date two days after current
     *
     * @return String of date two days after current
     */
    private String getDateTwoDaysAfterCurrent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(LocalDateTime.now(ZoneOffset.UTC).plusDays(2).withNano(0));
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
     * Sets day value in date picker
     *
     * @param dayValue - int of day value for locator
     */
    private void setDayValuePicker(int dayValue) {
        By dayLocator = By.xpath(String.format("//a[contains(text(), '%d') and contains(@class, 'ui-state-default')]",
                dayValue));
        driver.findElement(dayLocator).click();
    }

    /**
     * Sets month dropdown value in date picker
     *
     * @param indexToSelect int
     */
    private void setMonthValuePicker(int indexToSelect) {
        Select monthSelect = new Select(datePickerMonthSelect);
        monthSelect.selectByIndex(indexToSelect);
    }

    /**
     * Sets year dropdown value in date picker
     *
     * @param valueToSelect String
     */
    private void setYearValuePicker(String valueToSelect) {
        Select yearSelect = new Select(datePickerYearSelect);
        yearSelect.selectByValue(valueToSelect);
    }

    /**
     * Clicks current currency dropdown twice to change focus
     */
    private void clickCurrencyTwice() {
        pageUtils.waitForElementAndClick(currentCurrencyElement);
        currentCurrencyElement.click();
    }

    /**
     * Initialises export set hash map
     */
    private void initialiseCurrencyMap() {
        currencyMap.put(CurrencyEnum.GBP.getCurrency(), gbpCurrencyOption);
        currencyMap.put(CurrencyEnum.USD.getCurrency(), usdCurrencyOption);
    }

    /**
     * Initialises part name map
     */
    private void initialisePartNameMap() {
        partNameMap.put(ReportNamesEnum.CASTING_DTC.getReportName(), partNameDtcReports);
        partNameMap.put(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(), partNameCastingDtcComparisonReport);
        partNameMap.put(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), partNameCastingDtcDetailsReport);
        partNameMap.put(ReportNamesEnum.PLASTIC_DTC.getReportName(), partNameDtcReports);
        partNameMap.put(ReportNamesEnum.DTC_PART_SUMMARY.getReportName(), dtcPartSummaryPartName);
        partNameMap.put(ReportNamesEnum.MACHINING_DTC.getReportName(), partNameDtcReports);
        partNameMap.put(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(), machiningDtcComparisonPartName);
        partNameMap.put(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), machiningDtcDetailsPartNameLink);
        partNameMap.put(ReportNamesEnum.SHEET_METAL_DTC.getReportName(), partNameDtcReports);
        partNameMap.put(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(), partNameDtcReports);
        partNameMap.put(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), partNameDtcReports);
        partNameMap.put(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(), costOutlierPartName);
    }

    /**
     * Initialise bubble map
     */
    private void initialiseBubbleMap() {
        bubbleMap.put(ReportNamesEnum.MACHINING_DTC.getReportName(), machiningDtcBubble);
        bubbleMap.put(ReportNamesEnum.MACHINING_DTC.getReportName().concat(" 2"), machiningDtcBubbleTwo);
        bubbleMap.put(ReportNamesEnum.CASTING_DTC.getReportName(), castingDtcBubble);
        bubbleMap.put(ReportNamesEnum.PLASTIC_DTC.getReportName(), plasticDtcBubble);
        bubbleMap.put(ReportNamesEnum.DTC_PART_SUMMARY.getReportName(), castingDtcBubbleTwo);
        bubbleMap.put(ReportNamesEnum.SHEET_METAL_DTC.getReportName(), sheetMetalDtcBubble);
        bubbleMap.put(ReportNamesEnum.SHEET_METAL_DTC.getReportName().concat(" 2"), sheetMetalDtcBubbleTwo);
        bubbleMap.put(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(), designOutlierChartSpotOne);
        bubbleMap.put(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 2"),
                designOutlierChartSpotTwo);
        bubbleMap.put(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 3"),
                designOutlierChartSpotThree);
        bubbleMap.put(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(), costOutlierChartSpotOne);
        bubbleMap.put(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName().concat(" 2"),
                costOutlierChartSpotTwo);
        bubbleMap.put(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName().concat(" 3"), costOutlierCentreSpot);
    }

    /**
     * Initialise DTC Score bubble map
     */
    private void initialiseDtcScoreBubbleMap() {
        dtcScoreBubbleMap.put("Low", castingDtcBubbleTwo);
        dtcScoreBubbleMap.put("Medium", dtcScoreMediumBubble);
        dtcScoreBubbleMap.put("Medium Casting", castingDtcBubbleThree);
        dtcScoreBubbleMap.put("High", dtcScoreHighBubble);
        dtcScoreBubbleMap.put("Sheet", dtcScoreSheetMetalMediumBubble);
    }

    /**
     * Initialises tool tip element map
     */
    private void initialiseTooltipElementMap() {
        tooltipElementMap.put("Finish Mass Name", tooltipFinishMassName);
        tooltipElementMap.put("Finish Mass Value", tooltipFinishMassValue);
        tooltipElementMap.put("FBC Name", tooltipFbcName);
        tooltipElementMap.put("FBC Value", tooltipFbcValue);
        tooltipElementMap.put("DTC Score Name", tooltipDtcScoreName);
        tooltipElementMap.put("DTC Score Value", tooltipDtcScoreValue);
        tooltipElementMap.put("Annual Spend Name", tooltipAnnualSpendName);
        tooltipElementMap.put("Annual Spend Value", tooltipAnnualSpendValue);
        tooltipElementMap.put("aPriori Cost Value (Cost Outlier)", costOutlierAprioriCost);
        tooltipElementMap.put("aPriori Cost Value (Cost Outlier) Bottom", costOutlierAprioriCostBottom);
        tooltipElementMap.put("aPriori Cost Value (Design Outlier)", designOutlierAprioriCost);
    }

    /**
     * Initialises DTC Comparison Dtc Issue map
     */
    private void initialiseDtcComparisonDtcIssueMap() {
        dtcComparisonDtcIssueMap.put("Draft", dtcComparisonDtcDraftIssues);
        dtcComparisonDtcIssueMap.put("Material", dtcComparisonDtcMaterialIssues);
        dtcComparisonDtcIssueMap.put("Radius", dtcComparisonDtcRadiusIssues);
    }

    /**
     * Initialises Cost Design map
     */
    private void initialiseCostDesignOutlierMap() {
        costDesignOutlierMap.put("Cost Outlier Identification Details Cost", costOutlierApCostOne);
        costDesignOutlierMap.put("Cost Outlier Identification Details Cost 2", costOutlierApCostTwo);
        costDesignOutlierMap.put("Design Outlier Identification Details Cost", designOutlierApCostOne);
        costDesignOutlierMap.put("Design Outlier Identification Details Cost 2", designOutlierApCostTwo);
        costDesignOutlierMap.put("Design Outlier Identification Details Mass", designOutlierMassOne);
        costDesignOutlierMap.put("Design Outlier Identification Details Mass 2", designOutlierMassTwo);
    }
}
