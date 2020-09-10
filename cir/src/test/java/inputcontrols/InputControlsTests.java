package inputcontrols;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.view.reports.GenericReportPage;

import java.math.BigDecimal;

public class InputControlsTests extends TestBase {

    private GenericReportPage genericReportPage;
    private WebDriver driver;

    public InputControlsTests(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Generic export set filter using input field test
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
                .ensureDatesAreCorrect()
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(equalTo(0)));
    }

    /**
     * Generic export set filter using date picker test
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
                .ensureDatesAreCorrect()
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(equalTo(0)));
    }

    /**
     * Generic test for invalid characters in export set filter fields
     * @param reportName - report to use
     */
    public void testExportSetFilterInvalidCharacters(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .setExportDateUsingInput(true, "?")
                .setExportDateUsingInput(false, "?")
                .clickScenarioDropdownTwice();

        assertThat(genericReportPage.isExportSetFilterErrorDisplayedAndEnabled(true), is(true));
        assertThat(genericReportPage.isExportSetFilterErrorDisplayedAndEnabled(false), is(true));
        assertThat(genericReportPage.getExportSetErrorText(true).isEmpty(), is(false));
        assertThat(genericReportPage.getExportSetErrorText(false).isEmpty(), is(false));
    }

    /**
     * Generic apply button test
     * @param reportName - report to use
     * @param rollupName - rollup to use
     */
    public void testApplyButton(String reportName, String rollupName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectRollup(rollupName)
                .clickApply()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getDisplayedRollup(),
                is(equalTo(rollupName)));
    }

    /**
     * Generic test for cancel button
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
     * @param reportName - report to use
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
     * @param reportName - report to use
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
     * @param reportName - report to use
     * @param exportSetName - export set to use
     */
    public void testExportSetSelection(String reportName, String exportSetName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .waitForInputControlsLoad()
                .exportSetSelectAll();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount())));

        genericReportPage.deselectExportSet(exportSetName);

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount() - 1)));

        genericReportPage.invertExportSetSelection();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(1)));

        genericReportPage.exportSetDeselectAll();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    /**
     * Generic test for currency code
     * @param reportName - report to use
     * @param exportSetName - export set to use
     */
    public void testCurrencyCode(String reportName, String exportSetName) {
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
                .ensureCorrectRollupIsSelected(rollupName)
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
    public void testCostMetricInputControlMachiningDtc(String costMetric) {
        genericReportPage = testCostMetricCore(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
                costMetric
        );
        assertThat(genericReportPage.getCostMetricValueFromChartAxis(), is(equalTo(String.format("%s (USD)", costMetric))));

        genericReportPage.setReportName(ReportNamesEnum.MACHINING_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        genericReportPage.getCostMetricValueFromBubble();

        assertThat(genericReportPage.getCostMetricValueFromBubble(), is(equalTo(String.format("%s : ", costMetric))));
    }

    /**
     * Generic test for Cost Metric Input Control on Machining DTC Details, Comparison and Casting DTC
     * @param reportName - String
     * @param costMetric - String
     */
    public void testCostMetricInputControlOtherMachiningDtcReports(String reportName, String exportSet, String costMetric) {
        testCostMetricCore(reportName, exportSet, costMetric);
    }

    /**
     * Core part of cost metric test
     * @param costMetric - String
     * @return current page object
     */
    private GenericReportPage testCostMetricCore(String reportName, String exportSet, String costMetric) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(exportSet)
                .selectCostMetric(costMetric)
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getCostMetricValueFromAboveChart(), is(equalTo(String.format("\n%s", costMetric))));
        return genericReportPage;
    }

    /**
     * Generic test for mass metric input control
     */
    public void testMassMetric(String reportName, String exportSet, String massMetric) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(exportSet)
                .selectMassMetric(massMetric)
                .clickOk();

        assertThat(genericReportPage.getMassMetricValueFromAboveChart(), containsString(massMetric));
        if (!reportName.contains("Comparison") && !reportName.contains("Details")) {
            assertThat(genericReportPage.getMassMetricValueFromBubble(reportName), containsString(massMetric));
        }
    }
}