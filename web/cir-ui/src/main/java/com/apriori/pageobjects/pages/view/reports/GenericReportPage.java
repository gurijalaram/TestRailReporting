package com.apriori.pageobjects.pages.view.reports;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.AssemblyTypeEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;

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
    private Map<String, WebElement> costOutlierValueElementMap = new HashMap<>();
    private Map<String, WebElement> dtcComparisonDtcIssueMap = new HashMap<>();
    private Map<String, WebElement> costDesignOutlierMap = new HashMap<>();
    private Map<String, WebElement> dtcScoreBubbleMap = new HashMap<>();
    private Map<String, WebElement> tooltipElementMap = new HashMap<>();
    private Map<String, WebElement> currencyMap = new HashMap<>();
    private Map<String, WebElement> partNameMap = new HashMap<>();
    private Map<String, WebElement> bubbleMap = new HashMap<>();
    private String reportName = "";

    @FindBy(xpath = "//span[contains(text(), 'Select Parts')]")
    private WebElement selectPartsControlTitle;

    @FindBy(xpath = "//span[contains(text(), '* DTC Score')]")
    private WebElement dtcScoreControlTitle;

    @FindBy(xpath = "(//div[@id='reportViewFrame']//div[@class='title'])[1]")
    private WebElement upperTitle;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[3][local-name() = 'path']")
    private WebElement castingDtcBubble;

    @FindBy(xpath = "//*[@class='highcharts-series-group']//*[18][local-name() = 'path']")
    private WebElement castingDtcBubbleTwo;

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

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > path:nth-of-type(38)")
    private WebElement machiningDtcBubbleTwo;

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > path:nth-child(3)")
    private WebElement designOutlierChartSpotOne;

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > path:nth-child(4)")
    private WebElement designOutlierChartSpotTwo;

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

    @FindBy(xpath = "//*[local-name() = 'rect' and @y='180.5']")
    private WebElement partOfCastingChartComparisonReport;

    @FindBy(xpath = "//*[contains(text(), 'Hole Issues')]/following-sibling::*[1]")
    private WebElement holeIssuesChartOneComparisonReport;

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

    @FindBy(id = "ok")
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

    @FindBy(xpath = "//div[@class='jr-mDialog jr confirmationDialog open']//div[@class='jr-mDialog-footer jr']/button[1]")
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

    @FindBy(xpath = "//span[contains(text(), '3570824')]")
    private WebElement componentLinkAssemblyDetails;

    @FindBy(xpath = "//span[contains(text(), 'SUB-SUB-ASM')]")
    private WebElement assemblyLinkAssemblyDetails;

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

    @FindBy(xpath = "(((//div[@class='highcharts-container '])[2]//*[local-name()='g'])[7]/*[local-name()='rect'])[13]")
    private WebElement machiningDtcComparisonBar;

    @FindBy(xpath = "(((//div[@class='highcharts-container '])[2]//*[local-name()='g'])[16]/*[local-name()='text'])[13]")
    private WebElement machiningDtcComparisonPartName;

    @FindBy(xpath = "//table/tbody/tr[13]/td[2]")
    private WebElement machiningDtcDetailsPartNameLink;

    @FindBy(xpath = "//span[contains(text(), 'Minimum Annual Spend:')]/../following-sibling::td[2]/span")
    private WebElement minimumAnnualSpend;

    @FindBy(xpath = "//tr[13]/td[20]/span")
    private WebElement annualSpendDetailsValue;

    @FindBy(xpath = "(//*[@class='highcharts-root'])[1]//*[contains(@class, 'highcharts-xaxis-labels')]")
    private WebElement chartCountElement;

    @FindBy(xpath = "//label[@title='Outlier Distance']/input")
    private WebElement outlierDistanceElement;

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

    @FindBy(xpath = "//span[@class='_jrHyperLink ReportExecution']/span")
    private WebElement plasticDtcDetailsRowOnePartName;

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

    @FindBy(xpath = "//span[contains(text(), 'Steel')]")
    private WebElement cycleTimeValueTrackingDetailsMaterialComposition;

    @FindBy(xpath = "//div[@id='exportDate']//div[@class='jr-mSingleselect jr']")
    private WebElement exportDateList;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[16]/td[27]")
    private WebElement costOutlierApCostOne;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[117]/td[24]")
    private WebElement costOutlierApCostTwo;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[10]/td[24]")
    private WebElement designOutlierApCostOne;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[86]/td[24]")
    private WebElement designOutlierApCostTwo;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[10]/td[24]")
    private WebElement designOutlierMassOne;

    @FindBy(xpath = "//div[@id='reportContainer']//table//tr[36]/td[21]")
    private WebElement designOutlierMassTwo;

    @FindBy(xpath = "(//span[contains(text(), 'Annualized Potential Savings')])[2]")
    private WebElement costOutlierAnnualisedPotentialSavings;

    @FindBy(xpath = "(//span[contains(text(), 'Percent Difference')])[2]")
    private WebElement costOutlierPercentDifference;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[16]//td[34]/span")
    private WebElement costOutlierPercentDifferenceValueInChartPercentSet;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[19]//td[14]/span")
    private WebElement costOutlierTotalAnnualisedValuePercentSet;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[16]//td[33]/span")
    private WebElement costOutlierPercentValueInChartAnnualisedSet;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[21]//td[14]/span")
    private WebElement costOutlierTotalAnnualisedValueAnnualisedSet;

    private final String genericDeselectLocator = "//span[contains(text(), '%s')]/..//li[@title='Deselect All']";
    private final String genericAssemblySetLocator = "//a[contains(text(), '%s [assembly]')]";

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public GenericReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseCostOutlierValueElementMap();
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
     * @return current page object
     */
    public GenericReportPage selectExportSet(String exportSet) {
        exportSetSearchInput.sendKeys(exportSet);
        By locator = By.xpath(String.format("//li[@title='%s']/div/a", exportSet));
        pageUtils.waitForElementToAppear(locator);
        pageUtils.waitForSteadinessOfElement(locator);
        pageUtils.waitForElementAndClick(locator);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return this;
    }

    /**
     * Inputs Minimum Annual Spend
     * @return current page object
     */
    public GenericReportPage inputMinimumAnnualSpend() {
        By locator = By.xpath("//label[@title='Minimum Annual Spend']/input");
        pageUtils.waitForElementAndClick(locator);
        WebElement minimumAnnualSpend = driver.findElement(locator);
        pageUtils.clearInput(driver.findElement(locator));
        minimumAnnualSpend.sendKeys("6631000");
        return this;
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
        inputField.sendKeys(inputValue);
        return this;
    }

    /**
     * Gets cost outlier report annualised or percent value from above chart
     *
     * @param annualisedOrPercent String
     * @return String
     */
    public String getCostOutlierAnnualisedOrPercentValueFromAboveChart(boolean isPercentSet, String annualisedOrPercent) {
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
     * Clicks Distance Outlier input and scrolls down
     * @return current page object
     */
    public GenericReportPage clickDistanceOutlierInputAndScrollDown() {
        outlierDistanceElement.click();
        pageUtils.scrollWithJavaScript(driver.findElement(By.xpath("(//div[@title='Select Parts ']//ul)[1]")), true);
        waitForCorrectAvailableSelectedCount(ListNameEnum.PARTS.getListName(), "Available: ", "0");
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
     * Method to set process group
     * @param processGroupOption - String
     * @return instance of current page object
     */
    public GenericReportPage setProcessGroup(String processGroupOption) {
        pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "Process Group")));
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        By locator = By.xpath(String.format("(//li[@title='%s'])[1]/div/a", processGroupOption));
        pageUtils.waitForSteadinessOfElement(locator);
        pageUtils.waitForElementAndClick(driver.findElement(locator));
        return this;
    }

    /**
     * Checks if Process Group warning message is displayed and enabled
     * @return boolean
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
     * @return String
     */
    public String getListWarningText(String listName) {
        By locator = By.xpath(
                String.format("//div[@id='%s']//span[contains(text(), 'This field is mandatory')]", listName));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Deselects any selected export sets
     */
    public GenericReportPage deselectAllProcessGroups() {
        pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "Process Group")));
        return this;
    }

    /**
     * Gets current Process Group value
     * @return String
     */
    public String getProcessGroupValueDtc(String reportName) {
        WebElement elementToUse = reportName.equals(ReportNamesEnum.DTC_PART_SUMMARY.getReportName())
                ? processGroupCurrentValueDtcPartSummary
                : processGroupCurrentValueCastingDtc;
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.getText();
    }

    /**
     * Sets specified assembly
     *
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
     */
    public String getAssemblyNameFromSetAssemblyDropdown(String assemblyName) {
        return driver.findElement(By.xpath(String.format(genericAssemblySetLocator, assemblyName)))
                .getAttribute("textContent");
    }

    /**
     * Checks current currency selection, fixes if necessary
     *
     * @param currency - String
     * @return current page object
     */
    public GenericReportPage checkCurrencySelected(String currency) {
        if (!currentCurrencyElement.getAttribute("title").equals(currency)) {
            pageUtils.waitForElementAndClick(currentCurrencyElement);
            pageUtils.waitForElementAndClick(currencyMap.get(currency));
        }
        return this;
    }

    /**
     * Selects cost metric, if necessary
     * @param costMetric - String
     * @return current page object
     */
    public GenericReportPage selectCostMetric(String costMetric) {
        if (!costMetricDropdown.getAttribute("title").equals(costMetric)) {
            costMetricDropdown.click();
            driver.findElement(By.xpath(String.format("//li[@title='%s']/div/a", costMetric))).click();
        }
        return this;
    }

    /**
     * Selects mass metric, if necessary
     * @param massMetric - String
     * @return current page object
     */
    public GenericReportPage selectMassMetric(String massMetric) {
        if (!massMetricDropdown.getAttribute("title").equals(massMetric)) {
            massMetricDropdown.click();
            driver.findElement(By.xpath(String.format("//li[@title='%s']/div/a", massMetric))).click();
        }
        return this;
    }

    /**
     * Selects cost metric, if necessary
     * @param sortOrder - String
     * @return current page object
     */
    public GenericReportPage selectSortOrder(String sortOrder) {
        pageUtils.scrollWithJavaScript(sortOrderDropdown, true);
        if (!sortOrderDropdown.getAttribute("title").equals(sortOrder)) {
            sortOrderDropdown.click();
            driver.findElement(By.xpath(String.format("//li[@title='%s']/div/a", sortOrder))).click();
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
     * @return String
     */
    public String getCostMetricValueFromAboveChart() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return costMetricElementAboveChart.getText();
    }

    /**
     * Gets cost metric value from chart axis
     * @return String
     */
    public String getCostMetricValueFromChartAxis() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return costMetricElementOnChartAxis.getAttribute("textContent");
    }

    /**
     * Gets cost metric value from bubble
     * @return String
     */
    public String getCostMetricValueFromBubble() {
        pageUtils.waitForElementToAppear(costMetricValueOnBubble);
        return costMetricValueOnBubble.getAttribute("textContent");
    }

    /**
     * Gets mass metric value from above chart
     * @return String
     */
    public String getMassMetricValueFromAboveChart() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return massMetricElementAboveChart.getAttribute("textContent");
    }

    /**
     * Gets mass metric value from bubble
     * @return String
     */
    public String getMassMetricValueFromBubble(String reportName) {
        setReportName(reportName);
        hoverPartNameBubbleDtcReports();
        pageUtils.waitForElementToAppear(massMetricValueOnBubble);
        return massMetricValueOnBubble.getAttribute("textContent");
    }

    public void waitForNewTabSwitchCastingDtcToComparison() {
        pageUtils.waitForElementToAppear(comparisonTitle);
    }

    /**
     * Opens new tab with CID open and switches to it
     * @return current page object
     */
    public GenericReportPage openNewCidTabAndFocus(int index) {
        pageUtils.jsNewTab();
        pageUtils.windowHandler(index);

        driver.get(Constants.getCidUrl());
        pageUtils.waitForElementToAppear(cidLogo);

        return new GenericReportPage(driver);
    }

    /**
     * Clicks ok
     * @return Instance of Generic Report Page object
     */
    public GenericReportPage clickOk() {
        pageUtils.waitForElementAndClick(okButton);
        pageUtils.waitForElementToAppear(upperTitle);
        return this;
    }

    /**
     * Gets specified value from report (Target and Quoted Cost Trend or Value Tracking reports)
     * @param index String - index of value to get
     * @return String
     */
    public String getValueFromReport(String index) {
        By locator = By.xpath(String.format("//table[contains(@class, 'jrPage')]//tr[22]/td[%s]/span", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Hovers over bar in Casting DTC Comparison Report
     * @return current page object
     */
    public GenericReportPage hoverBarDtcComparison(String reportName) {
        String indexToUse = reportName.contains("Plastic") ? "9" : "13";
        By locator = By.xpath(String.format("(//*[local-name()='g'])[%s]//*[local-name()='rect'][1]", indexToUse));
        pageUtils.waitForElementToAppear(locator);
        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(locator)).build().perform();
        return this;
    }

    /**
     * Gets part name from DTC Comparison report tooltip
     * @return String
     */
    public String getPartNameDtcComparisonTooltip() {
        pageUtils.waitForElementToAppear(dtcComparisonPartNameTableOne);
        return dtcComparisonPartNameTableOne.getAttribute("textContent");
    }

    /**
     * Get DTC Issue Value from Casting DTC Comparison Report
     * @return String
     */
    public String getDtcIssueValueDtcComparison(String valueToGet) {
        WebElement elementToUse = dtcComparisonDtcIssueMap.get(valueToGet);
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.getAttribute("textContent");
    }

    /**
     * Click comparison link
     * @return instance of current page object
     */
    public GenericReportPage clickComparison() {
        pageUtils.waitForElementToAppear(castingDtcBubble);
        pageUtils.waitForElementAndClick(comparisonTitle);
        return this;
    }

    /**
     * Waits for correct assembly to appear on screen (not on Input Controls - on report itself)
     *
     * @param assemblyToCheck String - assembly name to wait on
     * @return Generic - instance of specified class
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
     * @return current page object
     */
    public GenericReportPage waitForCorrectAssemblyInDropdown(String assemblyName) {
        By locator = By.xpath(String.format("//a[contains(@title, '%s')]", assemblyName));
        pageUtils.waitForElementToAppear(locator);
        return this;
    }

    /**
     * Wait for export set list count to be zero
     *
     * @return current page object
     */
    public GenericReportPage waitForCorrectExportSetListCount(String listName, String expectedCount) {
        waitForCorrectAvailableSelectedCount(listName, "Available: ", expectedCount);
        waitForCorrectAvailableSelectedCount(listName, "Selected: ", expectedCount);
        return this;
    }

    /**
     * Sets export set date to now or two days from now using input field
     * @param isEarliestAndToday - boolean to determine element to use and date to set
     * @return instance of current page object
     */
    public GenericReportPage setExportDateUsingInput(boolean isEarliestAndToday, String invalidValue) {
        String dateToUse = isEarliestAndToday ? getCurrentDate() : getDateTwoDaysAfterCurrent();
        WebElement dateInputToUse = isEarliestAndToday ? earliestExportDateInput : latestExportDateInput;
        String valueToInput = invalidValue.isEmpty() ? dateToUse : invalidValue;

        dateInputToUse.clear();
        dateInputToUse.click();
        dateInputToUse.sendKeys(valueToInput);

        return this;
    }

    /**
     * Click Use Latest Scenario dropdown twice to remove focus from date
     * @return Generic Report Page instance
     */
    public GenericReportPage clickUseLatestExportDropdownTwice() {
        pageUtils.waitForElementAndClick(useLatestExportDropdown);
        useLatestExportDropdown.click();
        return this;
    }

    /**
     * Gets isDisplayed and isEnabled for either export set filter error
     * @return booolean
     */
    public boolean isExportSetFilterErrorDisplayedAndEnabled(boolean isEarliest) {
        pageUtils.waitForElementToAppear(earliestExportSetDateError);
        pageUtils.waitForElementToAppear(latestExportSetDateError);
        return isEarliest ? earliestExportSetDateError.isDisplayed() && earliestExportSetDateError.isEnabled() :
                latestExportSetDateError.isDisplayed() && latestExportSetDateError.isEnabled();
    }

    /**
     * Gets export set error text
     * @param isEarliest - boolean
     * @return String
     */
    public String getExportSetErrorText(boolean isEarliest) {
        return isEarliest ? earliestExportSetDateError.getText() : latestExportSetDateError.getText();
    }

    /**
     * Sets export date to now or two days from now using date picker
     * @param isEarliestAndToday - boolean to determine element to use and date to set
     * @return instance of current page object
     */
    public GenericReportPage setExportDateUsingPicker(boolean isEarliestAndToday) {
        LocalDateTime newDt = isEarliestAndToday ? getCurrentDateLDT() : getCurrentDateLDT().plusDays(2);
        WebElement pickerTrigger = isEarliestAndToday ? earliestExportSetDatePickerTriggerBtn : latestExportSetDatePickerTriggerBtn;
        pageUtils.waitForElementAndClick(pickerTrigger);

        setDayValuePicker(newDt.getDayOfMonth());
        setMonthValuePicker(getMonthDropdownIndex(newDt));
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
     * @param index String
     * @return String
     */
    public String getComponentName(String index) {
        By locator = By.xpath(String.format("(//div[@title='Scenario Type']//ul)[1]/li[%s]", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Gets first scenario name
     * @return String
     */
    public String getFirstScenarioName() {
        pageUtils.waitForElementToAppear(firstScenarioName);
        return firstScenarioName.getAttribute("title");
    }

    /**
     * Selects default scenario name (Initial)
     * @return instance of Scenario Comparison Report Page
     */
    public <T> T selectDefaultScenarioName(Class<T> className) {
        By locator = By.xpath("//li[@title='Initial']/div/a");
        pageUtils.waitForElementAndClick(locator);

        By selectedLocator = By.xpath("(//li[@title='Initial' and contains(@class, 'jr-isSelected')])[1]");
        pageUtils.waitForElementToAppear(selectedLocator);

        if (className.equals(ScenarioComparisonReportPage.class)) {
            By filteredLocator = By.xpath("((//div[@title='Scenarios to Compare']//ul)[1]/li[contains(@title, '(Initial)')])[1]");
            pageUtils.waitForElementToAppear(filteredLocator);
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects first two Scenarios to compare
     */
    public GenericReportPage selectFirstTwoComparisonScenarios() {
        waitForCorrectAvailableSelectedCount(ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available: ",
            "14");

        for (int i = 1; i < 3; i++) {
            By locator = By.xpath(String.format("(//div[@title='Scenarios to Compare']//ul)[1]/li[%s]/div/a", i));
            pageUtils.waitForElementAndClick(locator);
            if (i == 1) {
                By postFilterLocator = By.xpath(String.format(
                        "(//div[@title='Scenarios to Compare']//ul)[1]/li[%s and @class='jr-mSelectlist-item jr-isHovered jr jr-isSelected']",
                        i
                ));
                pageUtils.waitForElementToAppear(postFilterLocator);
            }
        }
        return this;
    }

    /**
     * Waits for correct available or selected count in any input controls list
     * @param listName - String
     * @param option - String
     * @param expectedCount - String
     */
    public void waitForCorrectAvailableSelectedCount(String listName, String option, String expectedCount) {
        By locator = By.xpath(String.format(
                "//div[@title='%s']//span[@title='%s']",
                listName,
                option + expectedCount
        ));
        pageUtils.waitForElementToAppear(locator);
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
    public GenericReportPage deselectExportSet() {
        String expectedCount = String.valueOf(getSelectedExportSetCount() - 1);
        pageUtils.waitForElementAndClick(exportSetToSelect);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.EXPORT_SET.getListName(),
                "Selected: ",
                expectedCount
        );
        return this;
    }

    /**
     * Invert export set selection
     *
     * @return current page object
     */
    public GenericReportPage invertExportSetSelection() {
        String expectedCount = String.valueOf(getAvailableExportSetCount() - getSelectedExportSetCount());
        pageUtils.waitForElementAndClick(exportSetInvert);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.EXPORT_SET.getListName(),
                "Selected: ",
                expectedCount
        );
        return this;
    }

    /**
     * Deselect all export sets
     *
     * @return current page object
     */
    public GenericReportPage exportSetDeselectAll() {
        pageUtils.waitForElementAndClick(exportSetDeselect);
        waitForCorrectAvailableSelectedCount(ListNameEnum.EXPORT_SET.getListName(), "Selected: ", "0");
        return this;
    }

    /**
     * Expand rollup drop-down
     *
     * @return current page object
     */
    public GenericReportPage selectRollup(String rollupName) {
        if (!rollupDropdown.getAttribute("title").equals(rollupName)) {
            pageUtils.waitForElementAndClick(rollupDropdown);
            pageUtils.waitForElementAndClick(rollupSearchInput);
            rollupSearchInput.sendKeys(rollupName);
            By rollupLocator = By.xpath(String.format("//li[@title='%s']", rollupName));
            pageUtils.waitForElementToAppear(rollupLocator);
            pageUtils.waitForElementAndClick(rollupLocator);
        }
        return this;
    }

    /**
     * Gets selected rollup from dropdown
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
     * @param datePartToInvalidate String
     * @return String
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

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
    }

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
     * Get name of a report
     *
     * @return String - text of report name
     */
    public String getReportName(String reportName) {
        return pageUtils.getReportElement(reportName).getText();
    }

    /**
     * Sets day value in date picker
     */
    private void setDayValuePicker(int dayValue) {
        By dayLocator = By.xpath(String.format("//a[contains(text(), '%d') and contains(@class, 'ui-state-default')]", dayValue));
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
        pageUtils.clearInput(saveInput);
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
        pageUtils.waitForElementToAppear(
                By.xpath(String.format("//select[@id='reportOptionsSelect' and count(option) = %s]", expected)));
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
        waitForCorrectAvailableSelectedCount(ListNameEnum.EXPORT_SET.getListName(), "Selected: ", expected);
        return this;
    }

    /**
     * Method to return value from Bubble in DTC Casting or Machining DTC Report
     * @return BigDecimal value
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
     * @return BigDecimal value
     */
    public BigDecimal getAnnualSpendFromBubbleTooltip() {
        WebElement elementToUse = tooltipElementMap.get("Annual Spend Value");
        pageUtils.waitForElementToAppear(elementToUse);
        return new BigDecimal(elementToUse.getText().replace(",", ""));
    }

    /**
     * Get Minimum Annual Spend value
     * @return String
     */
    public String getMinimumAnnualSpendFromAboveChart() {
        By locator = By.xpath("//span[contains(text(), 'Minimum Annual Spend:')]/../following-sibling::td[2]/span");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Sets report name
     * @param reportName String
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
     * Hover DTC Score bubble
     * @param dtcScore String
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
     */
    public void waitForCorrectPartNameMachiningDtc(boolean initialCall) {
        String partNameToExpect = initialCall ? Constants.PART_NAME_INITIAL_EXPECTED_MACHINING_DTC :
                Constants.PART_NAME_EXPECTED_MACHINING_DTC;
        By locator = By.xpath(String.format("//*[contains(text(), '%s')]", partNameToExpect));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
    * Waits for correct part name
    * @param partName String
    */
    public void waitForCorrectPartName(String partName) {
        By locator = By.xpath(String.format("//*[contains(text(), '%s')]", partName));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
     * Clicks bubble to get to DTC Part Summary and Switches tab
     */
    public void clickMachiningBubbleAndSwitchTab() {
        pageUtils.actionClick(machiningDtcBubbleTwo);

        switchTab(1);
        pageUtils.waitForElementToAppear(upperTitle);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementToAppear(dtcPartSummaryPartName);
    }

    /**
     * Clicks bar in Machining DTC Comparison Report and switches tab
     * @return String
     */
    public String clickMachiningDtcComparisonBar() {
        pageUtils.waitForElementToAppear(machiningDtcComparisonPartName);
        pageUtils.waitForElementToAppear(machiningDtcComparisonBar);
        setReportName(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
        String partName = getPartNameDtcReports();

        for (int i = 0; i < 2; i++) {
            Actions builder = new Actions(driver).moveToElement(machiningDtcComparisonBar).click();
            builder.build().perform();
        }

        switchTab(1);
        pageUtils.waitForElementToAppear(upperTitle);
        pageUtils.waitForElementToAppear(dtcPartSummaryPartName);
        return partName;
    }

    /**
     * Clicks part name link in Machining DTC Details report and switches tab
     * @return String
     */
    public String clickMachiningDtcDetailsPartName() {
        pageUtils.waitForElementToAppear(machiningDtcDetailsPartNameLink);

        setReportName(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
        String partName = getPartNameDtcReports();

        pageUtils.waitForElementAndClick(machiningDtcDetailsPartNameLink);

        switchTab(1);
        pageUtils.waitForElementToAppear(upperTitle);
        pageUtils.waitForElementToAppear(dtcPartSummaryPartName);

        return partName;
    }

    /**
     * Hovers bubble one for process group test
     */
    public void hoverProcessGroupBubble(boolean useBubbleOne) {
        WebElement elementToUse = useBubbleOne ? processGroupBubbleOne : processGroupBubbleTwo;
        pageUtils.waitForElementToAppear(elementToUse);
        Actions builder = new Actions(driver).moveToElement(elementToUse);
        builder.perform();
    }

    /**
     * Select component from dropdown
     * @param componentName String
     */
    public GenericReportPage selectComponent(String componentName) {
        pageUtils.waitForElementAndClick(componentSelectDropdown);
        pageUtils.waitForElementAndClick(componentSelectSearchInput);
        componentSelectSearchInput.sendKeys(componentName);
        By componentToSelectLocator = By.xpath(String.format("//a[contains(text(), '%s')]", componentName));
        pageUtils.waitForElementToAppear(componentToSelectLocator);
        pageUtils.waitForElementAndClick(componentToSelectLocator);
        return this;
    }

    /**
     * Get part name from Casting DTC or Machining DTC Report
     * @return String of part name
     */
    public String getPartNameDtcReports() {
        WebElement elementToUse = partNameMap.get(this.reportName);
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.getText();
    }

    /**
     * Gets DTC Score from Bubble in DTC Reports
     * @return String
     */
    public String getDtcScoreDtcReports() {
        pageUtils.waitForElementToAppear(dtcScoreValueOnBubble);
        return dtcScoreValueOnBubble.getAttribute("textContent");
    }

    /**
     * Deselects any selected export sets
     */
    public GenericReportPage deselectAllDtcScores() {
        pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "DTC Score")));
        return this;
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
     * @return boolean
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
     * @return boolean
     */
    public boolean isTooltipDisplayed() {
        return dtcTooltipElement.getAttribute("opacity").equals("1");
    }

    /**
     * Checks if tooltip element is displayed and enabled
     * @param elementKey - String
     * @return boolean
     */
    public boolean isTooltipElementVisible(String elementKey) {
        return tooltipElementMap.get(elementKey).isDisplayed() && tooltipElementMap.get(elementKey).isEnabled();
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
        pageUtils.windowHandler(1);
        pageUtils.waitForElementToAppear(componentCostReportTitle);
    }

    /**
     * Gets component link part number
     * @return String
     */
    public String getComponentLinkPartNumber() {
        return componentLinkAssemblyDetails.getText();
    }

    /**
     * Gets assembly link part number
     * @return String
     */
    public String getAssemblyLinkPartNumber() {
        return assemblyLinkAssemblyDetails.getText();
    }

    /**
     * Gets report title
     * @return String
     */
    public String getReportTitle() {
        return upperTitle.getText();
    }

    /**
     * Gets component cost report part number text
     * @return String
     */
    public String getComponentCostPartNumber() {
        return componentCostReportPartNumber.getText();
    }

    /**
     * Closes current tab and switches back to main tab
     */
    public void closeTab() {
        driver.close();
        pageUtils.windowHandler(0);
    }

    /**
     * Searches for export set
     * @param exportSet - String
     */
    public void searchForExportSet(String exportSet) {
        pageUtils.waitForElementAndClick(exportSetSearchInput);
        exportSetSearchInput.sendKeys(exportSet);

        By listLocator = By.xpath(
                "//div[@title='Single export set selection.']//ul[@class='jr-mSelectlist jr' and count(./li/*) = 2]");
        pageUtils.waitForElementToAppear(listLocator);

        By topLevelOptionLocator = By.xpath("//li[@title='---01-top-level-multi-vpe']/div/a");
        pageUtils.waitForElementToAppear(topLevelOptionLocator);
    }

    /**
     * Checks if export set option is visible
     * @param exportSet - String
     * @return boolean
     */
    public boolean isExportSetVisible(String exportSet) {
        WebElement exportSetElement = driver.findElement(By.xpath(String.format("//li[@title='%s']/div/a", exportSet)));
        return exportSetElement.isDisplayed() && exportSetElement.isEnabled();
    }

    /**
     * Gets count of export sets visible
     * @return String
     */
    public String getExportSetOptionCount() {
        return exportSetList.getAttribute("childElementCount");
    }

    /**
     * Sets DTC Score Input Control
     * @return Instance of current page object
     */
    public GenericReportPage setDtcScore(String dtcScoreOption) {
        if (!dtcScoreOption.equals(DtcScoreEnum.ALL.getDtcScoreName())) {
            pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "DTC Score")));
            pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
            By locator = By.xpath(String.format("(//li[@title='%s'])[1]/div/a", dtcScoreOption));
            pageUtils.waitForSteadinessOfElement(locator);
            pageUtils.waitForElementAndClick(driver.findElement(locator));
        }
        return this;
    }

    /**
     * Gets DTC Score value from above chart
     * @return String
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
     * @return ArrayList of String values
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
     * @param listName String
     * @param inputString String
     */
    public void searchListForName(String listName, String inputString) {
        WebElement currentSearchInput = driver.findElement(By.xpath(
                String.format("//div[@title='%s']//input[@placeholder='Search list...']", listName)));
        pageUtils.waitForElementAndClick(currentSearchInput);
        currentSearchInput.clear();
        currentSearchInput.sendKeys(inputString);
        if (inputString.equals("fakename")) {
            By locator = By.xpath(String.format(
                    "//div[@title='%s']/div[1]/div/div[2]/div[2]/div/div[@style='height: 1px']", listName));
            pageUtils.waitForElementToAppear(locator);
        }
    }

    /**
     * Checks if created by option is visible and enabled
     * @param listName String
     * @param inputString String
     * @return boolean
     */
    public boolean isListOptionVisible(String listName, String inputString) {
        By locator = By.xpath(String.format("//div[@title='%s']//li[contains(@title, '%s')]/div/a", listName, inputString));
        pageUtils.waitForElementToAppear(locator);
        WebElement optionElement = driver.findElement(locator);
        return optionElement.isEnabled() && optionElement.isEnabled();
    }

    /**
     * Selects one of the names in created by list
     * @param listName String
     * @param nameToSelect String
     */
    public void selectListItem(String listName, String nameToSelect) {
        By locator = By.xpath(String.format("//div[@title='%s']//li[contains(@title ,'%s')]/div/a", listName, nameToSelect));
        pageUtils.waitForElementToAppear(locator);
        pageUtils.waitForElementAndClick(locator);
    }

    /**
     * Gets count of created by list items
     * @param listName String
     * @return String
     */
    public String getCountOfListItems(String listName) {
        By locator = By.xpath(String.format("(//div[@title='%s']//ul)[1]", listName));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("childElementCount");
    }

    /**
     * Clicks Select All option for Created By List
     */
    public void clickListPanelButton(String listName, String buttonName) {
        By buttonLocator = By.xpath(String.format("//div[@title='%s']//li[@title='%s']/a", listName, buttonName));
        pageUtils.waitForElementAndClick(buttonLocator);
    }

    /**
     * Inputs search query into Assembly Number Search Criteria
     */
    public void inputAssemblyNumberSearchCriteria(String inputString) {
        inputString = inputString.isEmpty() ? "random" : inputString;
        pageUtils.waitForElementAndClick(assemblyNumberSearchCriteria);
        assemblyNumberSearchCriteria.clear();
        assemblyNumberSearchCriteria.sendKeys(inputString);
        assemblyNumberSearchCriteria.sendKeys(Keys.ENTER);
        String dropdownText = inputString.equals("random") ? "" : inputString;
        By locator = By.xpath(String.format("//label[@title='Assembly Select']//a[contains(@title, '%s')]", dropdownText));
        pageUtils.scrollWithJavaScript(driver.findElement(By.xpath("//label[@title='Assembly Select']//a")), true);
        pageUtils.waitForSteadinessOfElement(locator);
        pageUtils.waitForElementToAppear(locator);

        if (inputString.equals("random")) {
            pageUtils.waitForElementToAppear(By.xpath("//label[@title='Assembly Select']/span[@class='warning']"));
        }
    }

    /**
     * Gets currently selected assembly
     * @return String
     */
    public String getCurrentlySelectedAssembly() {
        By locator = By.xpath("//label[@title='Assembly Select']//a");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Gets Assembly Number Search Error visibility
     * @return boolean
     */
    public boolean isAssemblyNumberSearchErrorVisible() {
        return pageUtils.isElementDisplayed(assemblyNumberSearchCriteriaError) &&
                pageUtils.isElementEnabled(assemblyNumberSearchCriteriaError);
    }

    /**
     * Checks if cost error is enabled
     *
     * @param minOrMax - String
     * @param costOrMass - String
     * @return boolean
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
     *
     * @param annualisedOrPercent
     * @return
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
     * @return String
     */
    public String getAssemblyNumberSearchErrorText() {
        return assemblyNumberSearchCriteriaError.getText();
    }

    /**
     * Gets Part Name value from DTC Part Summary report
     * @return String
     */
    public String getDtcPartSummaryPartNameValue() {
        return dtcPartSummaryPartName.getText();
    }

    /**
     * Gets upper title text from any report
     * @return String
     */
    public String getUpperTitleText() {
        return upperTitle.getText();
    }

    /**
     * Gets Annual Spend value from Details reports
     * @return BigDecimal
     */
    public BigDecimal getAnnualSpendValueDetailsReports() {
        pageUtils.waitForElementToAppear(annualSpendDetailsValue);
        return new BigDecimal(annualSpendDetailsValue.getText().replace(",", ""));
    }

    /**
     * Gets count of chart elements on comparison reports
     * @return String
     */
    public Integer getCountOfChartElements() {
        waitForReportToLoad();
        pageUtils.waitForElementToAppear(costMetricElementAboveChart);
        pageUtils.waitForElementToAppear(chartCountElement);
        return Integer.parseInt(chartCountElement.getAttribute("childElementCount"));
    }

    /**
     * Gets DTC Comparison table element name
     * @param tableIndex String
     * @param rowIndex String
     * @return String
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
     * Gets part name from row one of Plastic Dtc Details
     * @return String
     */
    public String getPlasticDtcDetailsRowOnePartName() {
        pageUtils.waitForElementToAppear(plasticDtcDetailsRowOnePartName);
        return plasticDtcDetailsRowOnePartName.getAttribute("textContent");
    }

    /**
     * Gets Annualised or Percent value from chart on Cost Outlier Identification Details Report
     *
     * @param index String
     * @return String
     */
    public String getTotalAnnualisedOrPercentValue(String index) {
        return costOutlierValueElementMap.get(index).getText();
    }

    /**
     * Gets part name from Casting DTC Details Report
     * @param getRowOnePartName boolean
     * @return String
     */
    public String getPartNameCastingSheetMetalDtcDetails(boolean getRowOnePartName) {
        String rowIndex = getRowOnePartName ? "1" : "2";
        By locator = By.xpath(String.format("(//span[@class='_jrHyperLink ReportExecution']/span)[%s]", rowIndex));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("textContent");
    }

    /**
     * Gets first FBC on Cost Outlier Details report
     *
     * @return BigDecimal
     */
    public BigDecimal getFirstFbcCostOutlierDetailsReport() {
        By locator = By.xpath("//div[@id='reportContainer']//table//tr[16]/td[27]/span");
        pageUtils.waitForElementToAppear(locator);
        return new BigDecimal(driver.findElement(locator).getText());
    }

    /**
     * Gets Scenario Name from Casting Dtc Details report
     * @param getRowOneScenarioName boolean
     * @return String
     */
    public String getScenarioNameCastingDtcDetails(boolean getRowOneScenarioName) {
        String rowIndex = getRowOneScenarioName ? "1" : "2";
        By locator = By.xpath(String.format("(//span[@class='_jrHyperLink ReportExecution'])[%s]/../following-sibling::td[2]/span", rowIndex));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("textContent");
    }

    /**
     * Gets Part Name row one in Casting DTC Details
     * @return String
     */
    public String getPartNameRowOneCastingDtcDetails() {
        pageUtils.waitForElementToAppear(castingDtcDetailsComparisonPartNameRowOne);
        return castingDtcDetailsComparisonPartNameRowOne.getAttribute("textContent");
    }

    /**
     * Gets DTC Issue Count for draft or radius on Casting DTC Details report
     * @param valueToGet - String value to get
     * @return String
     */
    public String getDtcIssueValueDtcDetails(String reportName, String valueToGet) {
        int index = 0;
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
     * Gets value from Component Cost Report
     * @param valueToGet String
     * @return String
     */
    public BigDecimal getComponentCostReportValue(String valueToGet) {
        By locator = By.xpath(String.format("//span[contains(text(), '%s')]/../following-sibling::td[1]/span", valueToGet));
        pageUtils.waitForElementToAppear(locator);
        return new BigDecimal(driver.findElement(locator).getText().replace(",", ""));
    }

    /**
     * Waits for Component dropdown filter to take effect
     * @return ComponentCostReportPage instance
     */
    public ComponentCostReportPage waitForComponentFilter() {
        By locator = By.xpath("//a[@title='3538968 (Initial)  [part]']");
        pageUtils.waitForElementToAppear(locator);
        return new ComponentCostReportPage(driver);
    }

    /**
     * Selects Component Type
     * @return instance of Scenario Comparison Report Page
     */
    public ScenarioComparisonReportPage selectComponentType(String componentType) {
        pageUtils.waitForElementAndClick(By.xpath(String.format(genericDeselectLocator, "Component Type")));

        By locator = By.xpath(String.format("(//div[@title='Scenario Type']//ul)[1]/li[@title='%s']", componentType));
        pageUtils.waitForElementAndClick(locator);
        waitForCorrectAvailableSelectedCount(
                ListNameEnum.COMPONENT_TYPE.getListName(), "Selected: ", "1");

        for (int i = 1; i < 6; i++) {
            By locator2 =
                    By.xpath(String.format(
                            "((//div[@title='Scenarios to Compare']//ul)[1]/li[contains(@title, '[%s]')])[%s]",
                            componentType, i)
            );
            pageUtils.waitForElementToAppear(locator2);
        }
        return new ScenarioComparisonReportPage(driver);
    }

    /**
     * Waits for rollup change to take effect
     * @param rollup String
     * @return instance of Sheet Metal Dtc Report page
     */
    public SheetMetalDtcReportPage waitForCorrectRollupInDropdown(String rollup) {
        By locator = By.xpath(String.format("//label[@title='Rollup']//a[@title='%s']", rollup));
        pageUtils.waitForElementToAppear(locator);
        return new SheetMetalDtcReportPage(driver);
    }

    /**
     * Switches tab, if second tab is open
     * @return GenericReportPage instance
     */
    public GenericReportPage switchTab(int index) {
        if (driver.getWindowHandles().size() == (index + 1)) {
            pageUtils.windowHandler(index);
        }
        return this;
    }

    /**
     * Waits for tab switch to occur from Cycle Time Value Tracking to Details or Component Cost Reports
     */
    public void waitForNewTabSwitchCycleTimeToDetailsOrComponentCost() {
        pageUtils.waitForElementToAppear(cycleTimeValueTrackingDetailsMaterialComposition);
    }

    /**
     * Gets all VPE Values from Assembly Details Report
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
     * Gets export date count
     * @return String
     */
    public String getExportDateCount() {
        pageUtils.waitForElementToAppear(exportDateList);
        return exportDateList.getAttribute("childElementCount");
    }

    /**
     * Inputs value into max or min cost or mass fields
     *
     * @param valueToSet - cost or mass input field
     * @param maxMinValue - max or min input field
     * @param valueToInput - value to input into field
     */
    public void inputMaxOrMinCostOrMass(boolean costReport, String valueToSet, String maxMinValue, String valueToInput) {
        By costLocator = By.xpath(String.format("//div[@id='componentCost%s']//input", maxMinValue));
        By locator = By.xpath(String.format("//div[@id='aPriori%s%s']//input", valueToSet, maxMinValue));
        By locatorToUse = costReport ? costLocator : locator;
        WebElement input = driver.findElement(locatorToUse);
        pageUtils.waitForElementToAppear(input);
        input.clear();
        input.click();
        input.sendKeys(valueToInput);
    }

    /**
     * Gets Cost min or max above chart value
     *
     * @param reportName - report name
     * @param massOrCost - max or cost to get
     * @param maxOrMin - max or min value to get
     * @return value as string
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
     * Checks if Cost Outlier Identification report SVG is displayed and enabled
     *
     * @param index String
     * @return boolean
     */
    public boolean isCostOutlierSvgDisplayedAndEnabled(String index) {
        By locator = By.xpath(String.format("(//div[@id='reportContainer']//*[local-name()='svg'])[%s]", index));
        WebElement element = driver.findElement(locator);
        return element.isDisplayed() && element.isEnabled();
    }

    /**
     * Checks if Cost Outlier Identification Report Title is displayed
     *
     * @param useAnnualisedPotentialSavings boolean
     * @return boolean
     */
    public boolean isCostOutlierTableTitleDisplayed(boolean useAnnualisedPotentialSavings) {
        WebElement elementToUse =
                useAnnualisedPotentialSavings ? costOutlierAnnualisedPotentialSavings : costOutlierPercentDifference;
        return elementToUse.isDisplayed() && elementToUse.isEnabled();
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
     * Checks if Cost Outlier Identification Details table is displayed
     *
     * @param name String
     * @return boolean
     */
    public boolean isCostOutlierDetailsTableTitleDisplayed(String name) {
        By titleLocator = By.xpath("//span[contains(text(), 'Cost Outlier Identification Details')]");
        pageUtils.waitForElementToAppear(titleLocator);

        By tableTitleLocator = By.xpath((String.format("(//span[contains(text(), '%s ')])[2]", name)));
        WebElement element = driver.findElement(tableTitleLocator);
        return pageUtils.isElementDisplayed(tableTitleLocator) && element.isEnabled();
    }

    /**
     * Gets value from Design or Cost Outlier Details report table
     *
     * @param valueIndex - String
     * @return WebElement
     */
    public String getCostOrMassMaxOrMinCostOrDesignOutlierDetailsReports(String valueIndex) {
        return costDesignOutlierMap.get(valueIndex).getText();
    }

    /**
     * Gets count of bar charts on specified chart
     *
     * @param chartName - String
     * @return int
     */
    public int getCostOutlierBarChartBarCount(String chartName) {
        String chartIndex = chartName.equals("Annualized") ? "1" : "7";
        return driver.findElements(By.xpath(
                String.format(
                        "(//*[@class='highcharts-series-group']//*[local-name() = 'g'])[%s]//*[local-name()='rect']",
                        chartIndex
                )
        )).size();
    }

    /**
     * Checks if cost outlier report bar is enabled and displayed
     *
     * @param chartName String
     * @return boolean
     */
    public boolean isCostOutlierBarEnabledAndDisplayed(String chartName) {
        String chartIndex = chartName.equals("Annualized") ? "1" : "7";
        WebElement elementToUse = driver.findElement(
                By.xpath(String.format(
                        "(//*[@class='highcharts-series-group']//*[local-name() = 'g'])[%s]//*[local-name()='rect']",
                        chartIndex))
        );
        return pageUtils.isElementDisplayed(elementToUse) && pageUtils.isElementEnabled(elementToUse);
    }

    /**
     * Gets dropdown index from date
     * @param date - date to use
     * @return int - index value
     */
    private int getMonthDropdownIndex(LocalDateTime date) {
        return date.getMonthValue() - 1;
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

    /**
     * Initialise cost outlier value element map
     */
    private void initialiseCostOutlierValueElementMap() {
        costOutlierValueElementMap.put("Percent Value Percent Set", costOutlierPercentDifferenceValueInChartPercentSet);
        costOutlierValueElementMap.put("Annualised Value Percent Set", costOutlierTotalAnnualisedValuePercentSet);
        costOutlierValueElementMap.put("Percent Value Annualised Set", costOutlierPercentValueInChartAnnualisedSet);
        costOutlierValueElementMap.put("Annualised Value Annualised Set", costOutlierTotalAnnualisedValueAnnualisedSet);
    }
}