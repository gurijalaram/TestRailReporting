package com.inputcontrols;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.SheetMetalDtcReportPage;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.DateElementsEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InputControlsTests extends TestBase {

    private SheetMetalDtcReportPage sheetMetalDtcReportPage;
    private GenericReportPage genericReportPage;
    private WebDriver driver;

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
            .waitForCorrectExportSetListCount("Single export set selection.", "0");

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
            .waitForCorrectExportSetListCount("Single export set selection.", "0");

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
            .selectExportSet(exportSetName);

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
            .clickCancel(GenericReportPage.class);

        assertThat(genericReportPage.getInputControlsDivClassName(), containsString("hidden"));
        assertThat(genericReportPage.inputControlsIsDisplayed(), is(equalTo(false)));
        assertThat(genericReportPage.inputControlsIsEnabled(), is(equalTo(true)));
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
            .selectExportSet(exportSetName)
            .clickReset()
            .waitForExpectedExportCount("0");

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    /**
     * Generic save and remove buttons test
     *
     * @param reportName    - report to use
     * @param exportSetName - export set to use
     */
    public void testSaveAndRemoveButtons(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSetName)
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
            .clickOk()
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
            .selectExportSet(exportSetName)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        genericReportPage.setReportName(reportName);
        genericReportPage.hoverPartNameBubbleDtcReports();
        usdGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip();

        genericReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class);

        genericReportPage.setReportName(reportName);
        genericReportPage.hoverPartNameBubbleDtcReports();
        gbpGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip();

        assertThat(genericReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    /**
     * Generic test for export set selection
     */
    public void testExportSetAvailabilityAndSelection(String reportName, String exportSet, String rollupName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSet)
            .clickOk();

        assertThat(genericReportPage.getDisplayedRollup(),
            is(equalTo(rollupName)));
    }

    /**
     * Generic test to ensure expected export set tests are present
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
     * Generic test for cost metric input control
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
     * Generic test for Cost Metric Input Control on Machining, Casting and Sheet Metal DTC Reports, Details and Comparison
     *
     * @param reportName - String
     * @param costMetric - String
     */
    public void testCostMetricInputControlComparisonDetailsDtcReports(String reportName, String exportSet, String costMetric) {
        testCostMetricCore(reportName, exportSet, costMetric);
    }

    /**
     * Generic test for mass metric input control
     *
     * @param reportName - String
     * @param exportSet  - String
     * @param massMetric - String
     */
    public void testMassMetricDtcReports(String reportName, String exportSet, String massMetric) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSet(exportSet)
            .selectMassMetric(massMetric)
            .clickOk()
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
            .selectExportSet(exportSet)
            .setProcessGroup(processGroupName)
            .clickOk();

        if (processGroupName.equals(ProcessGroupEnum.CASTING_DIE.getProcessGroup())) {
            genericReportPage.clickOk();
        }

        assertThat(
            genericReportPage.getProcessGroupValueDtc(reportName),
            is(equalTo(processGroupName))
        );

        if (reportName.equals(ReportNamesEnum.MACHINING_DTC.getReportName()) &&
            processGroupName.equals(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())) {
            assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled(), is(equalTo(true)));
        } else if (reportName.equals(ReportNamesEnum.CASTING_DTC.getReportName()) ||
                reportName.equals(ReportNamesEnum.SHEET_METAL_DTC.getReportName())) {
            genericReportPage.hoverSpecificPartNameBubble("20");
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
     */
    public void testProcessGroupInputControlNoSelection(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(exportSetName)
                .deselectAllProcessGroups()
                .clickOk();

        String listName = "processGroup";
        assertThat(genericReportPage.isListWarningDisplayedAndEnabled(listName), is(equalTo(true)));
        assertThat(genericReportPage.getListWarningText(listName), is(equalTo(Constants.WARNING_TEXT)));
    }

    /**
     * Generic test to ensure warning on Process Group input controls works correctly
     */
    public void testDtcScoreInputControlNoSelection(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(exportSetName)
                .deselectAllDtcScores()
                .clickOk();

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

        String inputString = reportName.equals(ReportNamesEnum.SCENARIO_COMPARISON.getReportName()) ? "bhegan" : "Ben Hegan";

        genericReportPage.searchListForName(listName, inputString);
        assertThat(genericReportPage.isListOptionVisible(listName, inputString), is(true));

        genericReportPage.searchListForName(listName, "fakename");
        assertThat(genericReportPage.getCountOfListItems(listName), is(equalTo("0")));
    }

    /**
     * Generic test for created by filter buttons
     *
     * @param reportName String
     */
    public void testListFilterButtons(String reportName, String exportSet, String listName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class);

        if (listName.contains("Parts") && !exportSet.isEmpty()) {
            String expectedPartCount = reportName.contains("Sheet") ? "41" : "43";
            genericReportPage.selectExportSet(exportSet);
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
     * @param reportName          String
     * @param assemblySearchInput String
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
            .selectExportSet(exportSet)
            .clickOk();

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
     * @param reportName        - String
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
            .setExportDateUsingInput(false, invalidDate)
            .clickUseLatestExportDropdownTwice();

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
            assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled(), is(true));
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
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        if (reportName.equals(ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName())) {
            genericReportPage.waitForReportToLoad();
            assertThat(genericReportPage.isDataAvailableLabelDisplayedAndEnabled(), is(true));
        } else {
            assertThat(genericReportPage.getCountOfChartElements().compareTo(initialChartCount), is(equalTo(-1)));
        }
    }

    /**
     * Generic test for export set search (Sheet Metal DTC)
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

    private void testMinimumAnnualSpendCore(String reportName, String exportSet, boolean setMinimumAnnualSpend) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSet(exportSet);

        if (setMinimumAnnualSpend) {
            genericReportPage.inputMinimumAnnualSpend();
        }

        genericReportPage.clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);
    }

    private void testCostMetricCore(String reportName, String exportSet, String costMetric) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSet(exportSet)
            .selectCostMetric(costMetric)
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getCostMetricValueFromAboveChart(), is(equalTo(String.format("\n%s", costMetric))));
    }

    private void partOneOfCheckBothProcessGroupTest(String reportName, String exportSetName, String processGroupName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSetName)
            .clickOk();

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
            .clickOk();

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
            .selectExportSet(exportSet)
            .setDtcScore(dtcScore)
            .clickOk()
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
