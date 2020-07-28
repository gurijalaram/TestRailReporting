package cireporttests.inputcontrols;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;

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
                .login("qa-automation-01", "qa-automation-01")
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setExportDateUsingInput(true)
                .setExportDateUsingInput(false)
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
}
