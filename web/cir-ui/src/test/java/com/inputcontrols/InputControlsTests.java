package com.inputcontrols;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.SheetMetalDtcReportPage;
import com.apriori.pageobjects.pages.view.reports.TargetAndQuotedCostValueTrackingPage;
import com.apriori.pageobjects.pages.view.reports.TargetQuotedCostTrendReportPage;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.enums.reports.DateElementsEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InputControlsTests extends TestBaseUI {

    private TargetQuotedCostTrendReportPage targetQuotedCostTrendReportPage;
    private SheetMetalDtcReportPage sheetMetalDtcReportPage;
    private GenericReportPage genericReportPage;
    private final WebDriver driver;

    public InputControlsTests(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Generic export set filter using input field test
     *
     * @param reportName - report to use
     */
    public void testExportSetFilterUsingInputField(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setExportDateUsingInput(true, "")
            .setExportDateUsingInput(false, "")
            .waitForCorrectExportSetListCount(ListNameEnum.EXPORT_SET.getListName(), "0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(equalTo(0)));
    }

    /**
     * Generic export set filter using date picker test
     *
     * @param reportName - report to use
     */
    public void testExportSetFilterUsingDatePicker(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setExportDateUsingPicker(true)
            .setExportDateUsingPicker(false)
            .waitForCorrectExportSetListCount(ListNameEnum.EXPORT_SET.getListName(), "0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(equalTo(0)));
    }

    /**
     * Generic test for invalid characters in export set filter fields
     *
     * @param reportName - report to use
     */
    public void testExportSetFilterInvalidCharacters(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .setExportDateUsingInput(true, "?")
            .setExportDateUsingInput(false, "?")
            .clickUseLatestExportDropdownTwice();

        assertThat(genericReportPage.isExportSetFilterErrorDisplayedAndEnabled(true), is(true));
        assertThat(genericReportPage.isExportSetFilterErrorDisplayedAndEnabled(false), is(true));
        assertThat(genericReportPage.getExportSetErrorText(true).isEmpty(), is(false));
        assertThat(genericReportPage.getExportSetErrorText(false).isEmpty(), is(false));
    }

    /**
     * Generic apply button test
     *
     * @param reportName - report to use
     * @param rollupName - rollup to use
     */
    public void testApplyButton(String reportName, String exportSetName, String rollupName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSetName, GenericReportPage.class);

        assertThat(genericReportPage.getSelectedRollup(rollupName), is(rollupName));

        genericReportPage.clickApply()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getDisplayedRollup(),
            is(equalTo(rollupName)));
    }

    /**
     * Generic test for cancel button
     *
     * @param reportName - report to use
     */
    public void testCancelButton(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .clickCancel();

        assertThat(genericReportPage.getInputControlsDivClassName(), containsString("hidden"));
        assertThat(genericReportPage.isInputControlsDisplayed(), is(equalTo(false)));
        assertThat(genericReportPage.isInputControlsEnabled(), is(equalTo(true)));
    }

    /**
     * Generic test reset button test
     *
     * @param reportName    - report to use
     * @param exportSetName - export set to use
     */
    public void testResetButton(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSetName, GenericReportPage.class)
            .clickReset()
            .clickReset()
            .waitForExpectedExportCount("0")
            .waitForCorrectPartListCount("0");

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    /**
     * Generic save button test
     *
     * @param reportName    - report to use
     * @param exportSetName - export set to use
     */
    public void testSaveButton(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSetName, GenericReportPage.class)
            .clickSave()
            .enterSaveName(Constants.SAVED_CONFIG_NAME)
            .clickSaveAsButton()
            .clickReset()
            .selectSavedOptionByName(Constants.SAVED_CONFIG_NAME);

        assertThat(genericReportPage.isExportSetSelected(exportSetName), is(true));

        genericReportPage.clickRemove();

        assertThat(genericReportPage.isOptionInDropDown("Saved Config", 1), is(false));
    }

    /**
     * Generic rollup dropdown test
     *
     * @param reportName - report to use
     * @param rollupName - rollup to use
     */
    public void testRollupDropdown(String reportName, String rollupName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectRollup(rollupName)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getDisplayedRollup(),
            is(equalTo(rollupName)));
    }

    /**
     * Generic test for export set selection
     *
     * @param reportName - report to use
     */
    public void testExportSetSelection(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .exportSetSelectAll();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount())));

        genericReportPage.deselectExportSet();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount() - 1)));

        genericReportPage.invertExportSetSelection();

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.EXPORT_SET.getListName(), "Selected: ", "1");
        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(1)));

        genericReportPage.exportSetDeselectAll();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    /**
     * Generic test for currency code
     *
     * @param reportName    - report to use
     * @param exportSetName - export set to use
     */
    public void testCurrencyCodeDtcReports(String reportName, String exportSetName) {
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSetName, GenericReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        genericReportPage.setReportName(reportName);
        genericReportPage.hoverPartNameBubbleDtcReports();
        usdGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip("FBC Value");

        genericReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class);

        genericReportPage.setReportName(reportName);
        genericReportPage.hoverPartNameBubbleDtcReports();
        gbpGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip("FBC Value");

        assertThat(genericReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    /**
     * Generic test for export set selection
     *
     * @param reportName - String
     * @param exportSet - String
     * @param rollupName - String
     */
    public void testExportSetAvailabilityAndSelection(String reportName, String exportSet, String rollupName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSet, GenericReportPage.class)
            .clickOk(GenericReportPage.class);

        assertThat(genericReportPage.getDisplayedRollup(),
            is(equalTo(rollupName)));
    }

    /**
     * Generic test to ensure expected export set tests are present
     *
     * @param reportName String
     */
    public void testExportSetAvailability(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        String[] expectedExportSetValues = genericReportPage.getExportSetEnumValues();

        assertThat(expectedExportSetValues, arrayContainingInAnyOrder(genericReportPage.getActualExportSetValues()));
    }

    /**
     * Generic test for Cost Metric Input Control on Machining, Casting and Sheet Metal DTC Reports, as well as
     * Cost Outlier Identification, and Details and Comparison versions of reports
     *
     * @param reportName - String
     * @param exportSet - String
     * @param costMetric - String
     */
    public void testCostMetricInputControlGeneric(String reportName, String exportSet,
                                                  String costMetric) {
        testCostMetricCore(reportName, exportSet, costMetric);
    }

    /**
     * Generic test for cost metric input control
     *
     * @param reportName - name of report to navigate to
     * @param exportSetName - export set to select
     * @param costMetric - cost metric to select
     */
    public void testCostMetricInputControlMachiningSheetMetalDtc(String reportName, String exportSetName,
                                                                 String costMetric) {
        testCostMetricCore(reportName, exportSetName, costMetric);
        assertThat(genericReportPage.getCostMetricValueFromChartAxis(), is(equalTo(String.format("%s (USD)", costMetric))));

        genericReportPage.setReportName(reportName);
        genericReportPage.hoverPartNameBubbleDtcReports();
        genericReportPage.getCostMetricValueFromBubble();

        assertThat(genericReportPage.getCostMetricValueFromBubble(), is(equalTo(String.format("%s : ", costMetric))));
    }

    /**
     * Generic test for Cost Metric Input Control on Target and Quoted Cost Trend reports
     *
     * @param reportName - String
     * @param rollupName - String
     * @param costMetric - String
     */
    public void testCostMetricInputControlTargetQuotedCostTrendReports(String reportName, String rollupName,
                                                                       String costMetric) {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, TargetQuotedCostTrendReportPage.class);

        targetQuotedCostTrendReportPage.selectProjectRollup(rollupName)
                .selectCostMetric(costMetric)
                .clickOk(TargetAndQuotedCostValueTrackingPage.class)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class);

        assertThat(targetQuotedCostTrendReportPage.getCostMetricValueFromAboveChart(),
                is(equalTo(costMetric)));
        String costAvoidedFinalValue = costMetric.contains("Fully") ? "(0.56)" : "(0.31)";
        assertThat(targetQuotedCostTrendReportPage.getCostAvoidedFinal(), is(equalTo(costAvoidedFinalValue)));
    }

    /**
     * Generic test for cost metric input control on Value Tracking report
     *
     * @param reportName String
     * @param costMetric String
     */
    public void testCostMetricTargetQuotedCostValueTrackingReport(String reportName, String costMetric) {
        testCostMetricCoreTargetQuotedCostReports(reportName, costMetric);

        String expectedFirstProjectName = costMetric.contains("Fully") ? "4" : "1";
        String expectedQuotedCostDifference = costMetric.contains("Fully") ? "318.50" : "264.13";

        assertThat(targetQuotedCostTrendReportPage.getFirstProject(),
                is(equalTo(String.format("PROJECT %s", expectedFirstProjectName))));
        assertThat(targetQuotedCostTrendReportPage.getQuotedCostDifferenceFromApCost(),
                is(equalTo(expectedQuotedCostDifference)));
    }

    /**
     * Generic test for cost metric input control on Value Tracking Details report
     *
     * @param reportName String
     * @param costMetric String
     */
    public void testCostMetricTargetQuotedCostValueTrackingDetailsReport(String reportName, String costMetric) {
        testCostMetricCoreTargetQuotedCostReports(reportName, costMetric);

        String expectedCurrentAprioriCost = costMetric.contains("Fully") ? "264.92" : "264.61";
        String expectedAnnualizedAprioriCost = costMetric.contains("Fully") ? "1,453,962.46" : "1,453,623.64";

        assertThat(targetQuotedCostTrendReportPage.getCurrentAprioriCost(), is(equalTo(expectedCurrentAprioriCost)));
        assertThat(targetQuotedCostTrendReportPage.getAnnualizedCost(), is(equalTo(expectedAnnualizedAprioriCost)));
    }

    /**
     * Generic test for mass metric input control
     *
     * @param reportName - String
     * @param exportSet  - String
     * @param massMetric - String
     */
    public void testMassMetricReportsWithChart(String reportName, String exportSet, String massMetric) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSetDtcTests(exportSet)
            .waitForExportSetSelection(exportSet)
            .selectMassMetric(massMetric)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getMassMetricValueFromAboveChart(), containsString(massMetric));
        if (!reportName.contains("Comparison") && !reportName.contains("Details")) {
            assertThat(genericReportPage.getMassMetricValueFromBubble(reportName), containsString(massMetric));
        }
    }

    /**
     * Generic test for process group input control
     *
     * @param reportName       - String
     * @param exportSet        - String
     * @param processGroupName - String
     */
    public void testSingleProcessGroup(String reportName, String exportSet, String processGroupName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSet, GenericReportPage.class)
            .setProcessGroup(processGroupName)
            .clickOk(GenericReportPage.class);

        assertThat(
            genericReportPage.getProcessGroupValueDtc(reportName),
            is(equalTo(processGroupName))
        );

        if (reportName.equals(ReportNamesEnum.MACHINING_DTC.getReportName()) &&
            processGroupName.equals(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())) {
            assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled("1"), is(equalTo(true)));
        } else if (reportName.equals(ReportNamesEnum.CASTING_DTC.getReportName()) ||
                reportName.equals(ReportNamesEnum.SHEET_METAL_DTC.getReportName())) {
            genericReportPage.hoverSpecificPartNameBubble("20");
            genericReportPage.setReportName(reportName);
            String partName = genericReportPage.getPartNameDtcReports();

            navigateToDtcPartSummaryAndAssert(partName, processGroupName);
        }
    }

    /**
     * Generic test for process group input control with two process groups (Casting DTC)
     */
    public void testTwoProcessGroupsCasting() {
        partOneOfCheckBothProcessGroupTest(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            Constants.CASTING_DIE_SAND_NAME
        );

        genericReportPage.setReportName(ReportNamesEnum.CASTING_DTC.getReportName());
        genericReportPage.hoverProcessGroupBubble(true);
        String partName = genericReportPage.getPartNameDtcReports();
        genericReportPage.hoverProcessGroupBubble(false);
        String partNameTwo = genericReportPage.getPartNameDtcReports();

        navigateToDtcPartSummaryAndAssert(partName, ProcessGroupEnum.CASTING_DIE.getProcessGroup());
        navigateToDtcPartSummaryAndAssert(partNameTwo, ProcessGroupEnum.CASTING_SAND.getProcessGroup());
    }

    /**
     * Generic test for process group input control with two process groups (Machining DTC)
     */
    public void testTwoProcessGroupsMachining() {
        partOneOfCheckBothProcessGroupTest(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            Constants.STOCK_MACHINING_TWO_MODEL_NAME
        );

        genericReportPage.setReportName(ReportNamesEnum.CASTING_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        genericReportPage.setReportName(ReportNamesEnum.MACHINING_DTC.getReportName());
        String partName = genericReportPage.getPartNameDtcReports();

        navigateToDtcPartSummaryAndAssert(partName, ProcessGroupEnum.STOCK_MACHINING.getProcessGroup());
    }

    /**
     * Generic test to ensure warning on Process Group input control works correctly
     *
     * @param reportName String
     * @param exportSetName String
     */
    public void testProcessGroupInputControlNoSelection(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(exportSetName, GenericReportPage.class)
                .deselectAllProcessGroups()
                .clickOk(GenericReportPage.class);

        String listName = "processGroup";
        assertThat(genericReportPage.isListWarningDisplayedAndEnabled(listName), is(equalTo(true)));
        assertThat(genericReportPage.getListWarningText(listName), is(equalTo(Constants.WARNING_TEXT)));
    }

    /**
     * Generic test to ensure warning on Process Group input controls works correctly
     *
     * @param reportName String
     * @param exportSetName String
     */
    public void testDtcScoreInputControlNoSelection(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(exportSetName, GenericReportPage.class)
                .deselectAllDtcScores()
                .clickOk(GenericReportPage.class);

        String listName = "dtcScore";
        assertThat(genericReportPage.isListWarningDisplayedAndEnabled(listName), is(equalTo(true)));
        assertThat(genericReportPage.getListWarningText(listName), is(equalTo(Constants.WARNING_TEXT)));
    }

    /**
     * Generic test for DTC Score Input Control - main reports
     *
     * @param reportName - String
     * @param exportSet  - String
     * @param dtcScore   - String
     */
    public void testDtcScoreMainReports(String reportName, String exportSet, String dtcScore) {
        dtcScoreTestCore(reportName, exportSet, dtcScore);

        if (reportName.equals(ReportNamesEnum.PLASTIC_DTC.getReportName())) {
            dtcScorePlasticAssertions(reportName, dtcScore);
        } else if (reportName.equals(ReportNamesEnum.SHEET_METAL_DTC.getReportName()) &&
                dtcScore.equals(DtcScoreEnum.MEDIUM.getDtcScoreName())) {
            dtcScoreCastingMachiningAssertions("Sheet");
        } else {
            dtcScoreCastingMachiningAssertions(dtcScore);
        }
    }

    /**
     * Generic test for DTC Score Input Control - comparison reports
     *
     * @param reportName - String
     * @param exportSet  - String
     * @param dtcScore   - String
     */
    public void testDtcScoreComparisonReports(String reportName, String exportSet, String dtcScore) {
        dtcScoreTestCore(reportName, exportSet, dtcScore);

        assertThat(genericReportPage.getDtcScoreAboveChart(), is(equalTo(dtcScore)));
    }

    /**
     * Generic test for DTC Score Input Control - details reports
     *
     * @param reportName - String
     * @param exportSet  - String
     * @param dtcScore   - String
     */
    public void testDtcScoreDetailsReports(String reportName, String exportSet, String dtcScore) {
        dtcScoreTestCore(reportName, exportSet, dtcScore);

        assertThat(genericReportPage.getDtcScoreAboveChart(), is(equalTo(dtcScore)));

        ArrayList<String> valuesToCheck = genericReportPage.getDtcScoreValuesDtcDetailsReports(reportName);
        for (String value : valuesToCheck) {
            assertThat(value, is(equalTo(dtcScore)));
        }
    }

    /**
     * Generic test for created by filter search
     *
     * @param reportName String
     * @param listName   String
     */
    public void testListFilterSearch(String reportName, String listName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        genericReportPage.searchListForName(listName, Constants.NAME_TO_SELECT);
        assertThat(genericReportPage.isListOptionVisible(listName, Constants.NAME_TO_SELECT), is(true));

        genericReportPage.searchListForName(listName, "fakename");
        assertThat(genericReportPage.getCountOfListItems(listName), is(equalTo("0")));
    }

    /**
     * Generic test for created by filter buttons
     *
     * @param reportName String
     * @param exportSet String
     * @param listName String
     */
    public void testListFilterButtons(String reportName, String exportSet, String listName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        if (listName.contains("Parts") && !exportSet.isEmpty()) {
            String expectedPartCount = reportName.contains("Sheet") ? "41" : "43";
            genericReportPage.selectExportSet(exportSet, GenericReportPage.class);
            genericReportPage.waitForCorrectAvailableSelectedCount(listName, "Available: ", expectedPartCount);
        }

        ArrayList<String> buttonNames = new ArrayList<String>() {
            {
                add("Select All");
                add("Deselect All");
                add("Invert");
            }
        };

        String availableValue = genericReportPage.getCountOfListAvailableOrSelectedItems(listName, "Available");
        String selectedInitialValue = genericReportPage.getCountOfListAvailableOrSelectedItems(listName, "Selected");

        for (String buttonName : buttonNames) {
            genericReportPage.clickListPanelButton(listName, buttonName);

            if (buttonName.equals("Select All") || buttonName.equals("Invert")) {
                genericReportPage.waitForCorrectAvailableSelectedCount(listName, "Selected: ", availableValue);
                assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(listName, "Selected"), is(equalTo(availableValue)));
            } else {
                genericReportPage.waitForCorrectAvailableSelectedCount(listName, "Selected: ", selectedInitialValue);
                assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(listName, "Selected"),
                    is(equalTo(selectedInitialValue)));
            }
        }
    }

    /**
     * Generic test for assembly number search criteria
     *
     * @param reportName - String
     * @param assemblySearchInput - String
     */
    public void testAssemblyNumberSearchCriteria(String reportName, String assemblySearchInput) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        genericReportPage.inputAssemblyNumberSearchCriteria(assemblySearchInput);
        assertThat(genericReportPage.getCurrentlySelectedAssembly(), startsWith(assemblySearchInput));

        genericReportPage.inputAssemblyNumberSearchCriteria("");
        assertThat(genericReportPage.getCurrentlySelectedAssembly(), is(equalTo("")));
        assertThat(genericReportPage.isAssemblyNumberSearchErrorVisible(), is(equalTo(true)));
        assertThat(genericReportPage.getAssemblyNumberSearchErrorText(), is(equalTo(Constants.WARNING_TEXT)));
    }

    /**
     * Generic test for chart tooltips on DTC Reports
     *
     * @param reportName - String
     * @param exportSet  - String
     */
    public void testDtcChartTooltips(String reportName, String exportSet) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSet(exportSet, GenericReportPage.class)
            .clickOk(GenericReportPage.class);

        genericReportPage.setReportName(reportName);
        genericReportPage.hoverPartNameBubbleDtcReports();

        assertThat(genericReportPage.isTooltipDisplayed(), is(true));
        assertIsTooltipElementVisible("Finish Mass Name");
        assertIsTooltipElementVisible("Finish Mass Value");
        assertIsTooltipElementVisible("FBC Name");
        assertIsTooltipElementVisible("FBC Value");
        assertIsTooltipElementVisible("DTC Score Name");
        assertIsTooltipElementVisible("DTC Score Value");
        assertIsTooltipElementVisible("Annual Spend Name");
        assertIsTooltipElementVisible("Annual Spend Value");
    }

    /**
     * Generic test for invalid export set filter inputs
     *
     * @param reportName - String
     * @param valueToInvalidate - String
     */
    public void testInvalidExportSetFilterDateInputs(String reportName, String valueToInvalidate) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        String invalidDate = genericReportPage.getInvalidDate(valueToInvalidate);
        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setExportDateUsingInput(true, invalidDate)
            .setExportDateUsingInput(false, invalidDate);

        if (valueToInvalidate.equals(DateElementsEnum.MONTH.getDateElement()) ||
            valueToInvalidate.equals(DateElementsEnum.DAY.getDateElement())) {
            assertThat(genericReportPage.isExportSetFilterErrorDisplayedAndEnabled(true), is(true));
            assertThat(genericReportPage.isExportSetFilterErrorDisplayedAndEnabled(false), is(true));
            assertThat(genericReportPage.getExportSetErrorText(true).isEmpty(), is(false));
            assertThat(genericReportPage.getExportSetErrorText(false).isEmpty(), is(false));
        } else {
            genericReportPage.waitForCorrectExportSetListCount("Single export set selection.", "0");
            assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
            assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(equalTo(0)));
        }
    }

    /**
     * Generic test for Minimum Annual Spend
     *
     * @param reportName String
     * @param exportSet String
     */
    public void testMinimumAnnualSpend(String reportName, String exportSet) {
        testMinimumAnnualSpendCore(reportName, exportSet, true);

        assertThat(genericReportPage.getMinimumAnnualSpendFromAboveChart(),
            is(startsWith("6,631,000")));

        if (!reportName.equals(ReportNamesEnum.PLASTIC_DTC.getReportName())) {
            genericReportPage.hoverSpecificPartNameBubble("4");
            BigDecimal annualSpendValue = genericReportPage.getAnnualSpendFromBubbleTooltip();

            assertThat(
                annualSpendValue.compareTo(new BigDecimal("6631000")),
                is(equalTo(1))
            );
        }
    }

    /**
     * Generic test for Minimum Annual Spend Details Reports
     *
     * @param reportName String
     * @param exportSet  String
     */
    public void testMinimumAnnualSpendDetailsReports(String reportName, String exportSet) {
        testMinimumAnnualSpendCore(reportName, exportSet, true);

        if (reportName.equals(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName())) {
            genericReportPage.waitForReportToLoad();
            assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled("1"), is(true));
        } else {
            BigDecimal valueForAssert = new BigDecimal("6631000.00");
            assertThat(
                genericReportPage.getMinimumAnnualSpendFromAboveChart().replace(",", ""),
                is(equalTo(valueForAssert.toString()))
            );
            assertThat(
                genericReportPage.getAnnualSpendValueDetailsReports().compareTo(valueForAssert),
                is(equalTo(1))
            );
        }
    }

    /**
     * Generic test for Minimum Annual Spend Comparison Reports
     *
     * @param reportName String
     * @param exportSet  String
     */
    public void testMinimumAnnualSpendComparisonReports(String reportName, String exportSet) {
        testMinimumAnnualSpendCore(reportName, exportSet, false);

        Integer initialChartCount = genericReportPage.getCountOfChartElements();
        genericReportPage.clickInputControlsButton()
            .inputMinimumAnnualSpend()
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        if (reportName.equals(ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName())) {
            genericReportPage.waitForReportToLoad();
            assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled("1"), is(true));
        } else {
            assertThat(genericReportPage.getCountOfChartElements().compareTo(initialChartCount), is(equalTo(-1)));
        }
    }

    /**
     * Generic test for export set search (Sheet Metal DTC)
     *
     * @param reportName String
     * @param exportSetName String
     */
    public void testExportSetSearch(String reportName, String exportSetName) {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, SheetMetalDtcReportPage.class);

        sheetMetalDtcReportPage.waitForInputControlsLoad();
        sheetMetalDtcReportPage.searchForExportSet(exportSetName);

        assertThat(sheetMetalDtcReportPage.getFirstExportSetName(),
                is(equalTo(exportSetName)));
    }

    /**
     * Generic test for Target Quoted Cost Trend Report Links
     *
     * @param milestoneName String
     */
    public void testTargetQuotedCostTrendReportHyperlinks(String milestoneName) {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                        TargetQuotedCostTrendReportPage.class)
                .selectProjectRollup(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
                .clickOk(TargetQuotedCostTrendReportPage.class)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class)
                .clickMilestoneLink(milestoneName)
                .switchTab(1)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class);

        assertThat(targetQuotedCostTrendReportPage.getMilestoneName(),
                is(equalTo(String.format("Milestone: %s", milestoneName))));
    }

    /**
     * Generic test for Target and Quoted Cost Trend data integrity
     *
     * @param milestoneName String
     */
    public void testTargetQuotedCostTrendDataIntegrity(String milestoneName) {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                        TargetQuotedCostTrendReportPage.class)
                .selectProjectRollup(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
                .clickOk(TargetAndQuotedCostValueTrackingPage.class)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class)
                .clickMilestoneLink(milestoneName)
                .switchTab(1)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class);;

        String partName = targetQuotedCostTrendReportPage.getPartName("1");

        String reportsScenarioName = targetQuotedCostTrendReportPage.getValueFromReport("5");
        String reportsVpe = targetQuotedCostTrendReportPage.getValueFromReport("11");
        String reportsProcessGroup = targetQuotedCostTrendReportPage.getValueFromReport("14");
        String reportsMaterialComposition = targetQuotedCostTrendReportPage.getValueFromReport("17")
            .replace("", " ");
        String reportsAnnualVolume = targetQuotedCostTrendReportPage.getValueFromReport("22");
        String reportsCurrentCost = targetQuotedCostTrendReportPage.getValueFromReport("24");

        //targetQuotedCostTrendReportPage.openNewCidTabAndFocus(2);
        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .saveAs()
                .inputName(new GenerateStringUtil().generateFilterName())
                .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, partName)
                .addCriteria(PropertyEnum.DIGITAL_FACTORY, OperationEnum.IN, DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                .submit(ExplorePage.class)
                .openFirstScenario();

        String cidScenarioName = evaluatePage.getCurrentScenarioName();
        String cidVPE = evaluatePage.getSelectedVPE();
        String cidProcessGroup = evaluatePage.getSelectedProcessGroup();
        String cidMaterialComposition = evaluatePage.openMaterialProcess().openMaterialUtilizationTab().getMaterialName();
        String cidAnnualVolume = evaluatePage.getAnnualVolume();
        String cidFbc = String.valueOf(evaluatePage.getCostResults("Fully Burdened Cost"));

        assertThat(reportsScenarioName, is(equalTo(cidScenarioName)));
        assertThat(reportsVpe, is(equalTo(cidVPE)));
        assertThat(reportsProcessGroup, is(equalTo(cidProcessGroup)));
        assertThat(reportsMaterialComposition, is(equalTo(cidMaterialComposition)));
        assertThat(reportsAnnualVolume, is(equalTo(cidAnnualVolume)));
        assertThat(reportsCurrentCost, is(equalTo(cidFbc)));
    }

    /**
     * Generic test for min and max mass filter - for Design and Cost Outlier Identification Reports
     *
     * @param reportName - String
     * @param costOrMass - String
     */
    public void testMinAndMaxMassOrCostFilterDesignCostOutlierMainReports(String reportName, String costOrMass) {
        String minValue = "1.00";
        String maxValue = "1,173.00";
        String maxValToUse = reportName.contains("Cost") ? "945.00" : maxValue;
        genericReportPage = testMinAndMaxMassOrCostFilterCore(reportName, costOrMass, minValue, maxValToUse);

        genericReportPage.setReportName(reportName);
        genericReportPage.scrollDown(driver.findElement(
                By.xpath("//span[contains(text(), 'aPriori Technologies')]")));
        for (int i = 0; i < 3; i++) {
            genericReportPage.hoverPartNameBubbleDtcReports();
        }

        String valueToGetIndex = "aPriori Cost Value ";
        if (costOrMass.equals("Cost")) {
            valueToGetIndex = reportName.contains("Design") ? valueToGetIndex.concat("(Design Outlier)") :
                    valueToGetIndex.concat("(Cost Outlier)");
        } else {
            valueToGetIndex = "Finish Mass Value";
        }

        BigDecimal massValueOne = genericReportPage.getFBCValueFromBubbleTooltip(valueToGetIndex);

        genericReportPage.setReportName(reportName.concat(" 2"));
        genericReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal massValueTwo = genericReportPage.getFBCValueFromBubbleTooltip(valueToGetIndex);

        assertThat(massValueOne.compareTo(new BigDecimal(minValue)), is(equalTo(1)));
        assertThat(massValueOne.compareTo(
                new BigDecimal(maxValue.replace(",", ""))),
                is(equalTo(-1))
        );

        assertThat(massValueTwo.compareTo(new BigDecimal(minValue)), is(equalTo(1)));
        assertThat(massValueTwo.compareTo(
                new BigDecimal(maxValue.replace(",", ""))),
                is(equalTo(-1))
        );
    }

    /**
     * Generic test for min and max mass filter - for Design and Cost Outlier Identification Details Reports
     *
     * @param reportName - String
     * @param costOrMass - String
     */
    public void testMinAndMaxMassOrCostFilterDesignCostOutlierDetailsReports(String reportName,
                                                                             String costOrMass) {
        String minValueToUse = costOrMass.equals("Mass") ? "1.000" : "1.00";
        String maxValueToUse = costOrMass.equals("Mass") ? "1,173.00" : "945.00";

        genericReportPage = testMinAndMaxMassOrCostFilterCore(
                reportName,
                costOrMass,
                minValueToUse,
                maxValueToUse
        );

        BigDecimal valueOne = getValueFromCostOrDesignDetailsReport(
                genericReportPage,
                reportName.concat(String.format(" %s", costOrMass))
        );
        BigDecimal valueTwo = getValueFromCostOrDesignDetailsReport(
                genericReportPage,
                reportName.concat(String.format(" %s 2", costOrMass))
        );

        assertMinAndMaxValues(valueOne, minValueToUse, maxValueToUse);
        assertMinAndMaxValues(valueTwo, minValueToUse, maxValueToUse);
    }

    /**
     * Generic test for decimal places on cost filter on Design and Cost Outlier Identification and Details reports
     *
     * @param reportName String
     * @param costOrMass String
     */
    public void testMinAndMaxMassOrCostFilterJunkValues(String reportName, String costOrMass) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class);

        boolean costReport = reportName.contains("Cost");
        genericReportPage.inputMaxOrMinCostOrMass(
                costReport,
                costOrMass,
                "Min",
                "hello world"
        );
        genericReportPage.inputMaxOrMinCostOrMass(
                costReport,
                costOrMass,
                "Max",
                "goodbye world"
        );

        genericReportPage.clickOk(GenericReportPage.class);

        assertThat(
                genericReportPage.isCostOrMassMaxOrMinErrorEnabled("Minimum", costOrMass),
                is(equalTo(true))
        );
        assertThat(
                genericReportPage.isCostOrMassMaxOrMinErrorEnabled("Maximum", costOrMass),
                is(equalTo(true))
        );
    }

    /**
     * Generic test for junk value on annualised or percent error (Cost Outlier tests)
     *
     * @param reportName String
     * @param fieldToUse String
     */
    public void testAnnualisedOrPercentError(String reportName, String fieldToUse) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
                .inputAnnualisedOrPercentValue(fieldToUse, "abcd")
                .clickOk(GenericReportPage.class);

        assertThat(genericReportPage.isAnnualisedOrPercentErrorEnabled(fieldToUse), is(equalTo(true)));
    }

    /**
     * Generic test for decimal places on any report - annualised potential savings or percent difference
     *
     * @param reportName String
     * @param fieldToUse String
     */
    public void testAnnualisedOrPercentDecimalPlaces(String reportName, String fieldToUse) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
                .inputAnnualisedOrPercentValue(fieldToUse, "10.000")
                .clickOk(GenericReportPage.class);

        boolean isPercentSet = fieldToUse.equals("Percent");
        assertThat(
                genericReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(isPercentSet, fieldToUse).equals("10.0%"),
                is(equalTo(true))
        );

        if (!reportName.contains("Details")) {
            genericReportPage.clickDetailsLink();
            assertThat(
                    genericReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(isPercentSet, fieldToUse).equals("10.0%"),
                    is(equalTo(true))
            );
        }
    }

    /**
     * Generic test - Annualised or Percent filter (no data available) on Cost Outlier Identification Report
     * (+ Details)
     *
     * @param reportName String
     * @param valueToTest String
     */
    public void testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(String reportName,
                                                                              String valueToTest) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
                .inputAnnualisedOrPercentValue(valueToTest, "210000.00")
                .clickOk(GenericReportPage.class);

        genericReportPage.waitForReportToLoad();
        assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled("1"), is(equalTo(true)));
        if (!reportName.contains("Details")) {
            assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled("2"),
                    is(equalTo(true)));
        }
    }

    private BigDecimal getValueFromCostOrDesignDetailsReport(GenericReportPage genericReportPage, String valueIndex) {
        return new BigDecimal(genericReportPage.getCostOrMassMaxOrMinCostOrDesignOutlierDetailsReports(valueIndex));
    }

    private void assertMinAndMaxValues(BigDecimal valueToAssert, String firstValue, String secondValue) {
        assertThat(valueToAssert.compareTo(
                new BigDecimal(firstValue.replace(",", ""))),
                is(equalTo(1))
        );
        assertThat(valueToAssert.compareTo(
                new BigDecimal(secondValue.replace(",", ""))),
                is(equalTo(-1))
        );
    }

    private GenericReportPage testMinAndMaxMassOrCostFilterCore(String reportName, String costOrMass, String minValue,
                                                                String maxValue) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSetDtcTests(ExportSetEnum.SHEET_METAL_DTC.getExportSetName());

        boolean costReport = reportName.contains("Cost");
        genericReportPage.inputMaxOrMinCostOrMass(
                costReport,
                costOrMass,
                "Min",
                minValue
        );
        genericReportPage.inputMaxOrMinCostOrMass(
                costReport,
                costOrMass,
                "Max",
                maxValue.replace(",", "")
        );
        genericReportPage.clickOk(GenericReportPage.class);

        String initialMinValue = genericReportPage.getMassOrCostMinOrMaxAboveChartValue(
                reportName,
                costOrMass,
                "Min");

        String minAssertValue = minValue;
        String minInputValue = minValue;
        if (costOrMass.equals("Mass")) {
            minAssertValue = initialMinValue.substring(0, initialMinValue.length() - 1);
            minInputValue = reportName.equals(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName())
                    ? minValue.substring(0, minValue.length() - 1) : minInputValue;
        }

        assertThat(minAssertValue,
                is(equalTo(minInputValue))
        );

        String initialMaxValue = genericReportPage.getMassOrCostMinOrMaxAboveChartValue(
                reportName,
                costOrMass,
                "Max");
        String maxAssertVal = costOrMass.equals("Mass") ? initialMaxValue.substring(0, initialMaxValue.length() - 1) :
                initialMaxValue;
        assertThat(maxAssertVal,
                is(equalTo(maxValue))
        );

        return genericReportPage;
    }

    private void testCostMetricCoreTargetQuotedCostReports(String reportName, String costMetric) {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, TargetQuotedCostTrendReportPage.class)
                .selectProjectRollup(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
                .selectCostMetric(costMetric)
                .clickOk(TargetAndQuotedCostValueTrackingPage.class)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class);

        String expectedCostMetric = costMetric;
        if (reportName.contains("Details") && costMetric.contains("Fully")) {
            expectedCostMetric = "Fully Burdened Cost";
        }
        assertThat(targetQuotedCostTrendReportPage.getCostMetricValueFromAboveChart(),
                is(equalTo(expectedCostMetric)));
    }

    private void testMinimumAnnualSpendCore(String reportName, String exportSet, boolean setMinimumAnnualSpend) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSetDtcTests(exportSet);

        if (setMinimumAnnualSpend) {
            genericReportPage.inputMinimumAnnualSpend();
            genericReportPage.waitForMinimumAnnualSpendOnChart();
        }

        genericReportPage.clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .waitForMinimumAnnualSpendOnChart();

        genericReportPage.clickInputControlsButton()
                .waitForInputControlsLoad()
                .selectExportSet(exportSet, GenericReportPage.class);

        genericReportPage.waitForCorrectAvailableSelectedCount("export set selection.", "Selected: ", "1");
        genericReportPage.clickOk(GenericReportPage.class);
    }

    private void testCostMetricCore(String reportName, String exportSet, String costMetric) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSetDtcTests(exportSet)
            .selectCostMetric(costMetric)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getCostMetricValueFromAboveChart(), is(equalTo(String.format("%s", costMetric))));
    }

    private void partOneOfCheckBothProcessGroupTest(String reportName, String exportSetName, String processGroupName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSetName, GenericReportPage.class)
            .clickOk(GenericReportPage.class);

        assertThat(
            genericReportPage.getProcessGroupValueDtc(reportName),
            is(equalTo(processGroupName))
        );
    }

    private void navigateToDtcPartSummaryAndAssert(String partName, String processGroupName) {
        partName = partName.equals("DTCCASTINGISSUES") ? partName + " (sand casting)" : partName;
        genericReportPage.navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.DTC_PART_SUMMARY.getReportName(), GenericReportPage.class)
            .selectComponent(partName)
            .clickOk(GenericReportPage.class);

        assertThat(
            genericReportPage.getProcessGroupValueDtc(ReportNamesEnum.DTC_PART_SUMMARY.getReportName()),
            is(equalTo(processGroupName))
        );
    }

    private void dtcScoreTestCore(String reportName, String exportSet, String dtcScore) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(exportSet)
            .setDtcScore(dtcScore)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);
    }

    private void dtcScoreCastingMachiningAssertions(String dtcScore) {
        genericReportPage.hoverBubbleDtcScoreDtcReports(dtcScore);

        String dtcScoreValue = genericReportPage.getDtcScoreDtcReports().replace(" ", "");

        if (dtcScore.equals("Sheet") || dtcScore.equals("Medium Casting")) {
            dtcScore = DtcScoreEnum.MEDIUM.getDtcScoreName();
        }
        assertThat(dtcScore, is(equalTo(genericReportPage.getDtcScoreAboveChart())));
        assertThat(dtcScore, is(equalTo(dtcScoreValue)));
    }

    private void dtcScorePlasticAssertions(String reportName, String dtcScore) {
        if (dtcScore.equals("Low")) {
            genericReportPage.setReportName(reportName);
            genericReportPage.hoverPartNameBubbleDtcReports();
        } else {
            genericReportPage.waitForNoBubbleReportToLoad();
            By locator = By.xpath("//*[@class='highcharts-series-group']//*[local-name() = 'path']");
            List<WebElement> elements = driver.findElements(locator);
            int bubbleListSize = elements.size();
            assertThat(bubbleListSize, is(equalTo(6)));
        }

        assertThat(dtcScore, is(equalTo(genericReportPage.getDtcScoreAboveChart())));
    }

    private void assertIsTooltipElementVisible(String tooltipKey) {
        assertThat(genericReportPage.isTooltipElementVisible(tooltipKey), is(true));
    }
}
