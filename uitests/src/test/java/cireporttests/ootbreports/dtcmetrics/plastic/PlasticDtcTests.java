package cireporttests.ootbreports.dtcmetrics.plastic;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.reports.pages.view.reports.PlasticDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.MachiningReportsEnum;
import com.apriori.utils.enums.PlasticDtcReportsEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;

import java.math.BigDecimal;

public class PlasticDtcTests extends TestBase {

    private PlasticDtcReportPage plasticDtcReportPage;
    private GenericReportPage genericReportPage;
    private ViewRepositoryPage repository;
    private EvaluatePage evaluatePage;

    public PlasticDtcTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailability() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToPlasticFolder();

        String[] expectedReportNames = repository.getReportNamesValues();

        assertThat(expectedReportNames, arrayContainingInAnyOrder(repository.getActualReportNames()));
    }

    @Test
    @TestRail(testCaseId = "1344")
    @Description("Test Plastic DTC Reports Export Set Availability")
    public void testPlasticDtcReportExportSetAvailability() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class);

        String[] expectedExportSetValues = plasticDtcReportPage.getExportSetEnumValues();

        assertThat(expectedExportSetValues, arrayContainingInAnyOrder(plasticDtcReportPage.getActualExportSetValues()));
    }

    @Test
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Input Field")
    public void testPlasticDtcExportSetFilterInputField() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(plasticDtcReportPage.getCountOfExportSets());

        plasticDtcReportPage.setEarliestExportDateToTodayInput()
                .setLatestExportDateToTwoDaysFutureInput()
                .ensureDatesAreCorrect(true, false)
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(plasticDtcReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));

    }

    @Test
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Date Picker")
    public void testPlasticDtcExportSetFilterDatePicker() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(plasticDtcReportPage.getCountOfExportSets());

        plasticDtcReportPage.setEarliestExportDateToTodayPicker()
            .setLatestExportDateToTodayPlusTwoPicker()
            .ensureDatesAreCorrect(true, false)
            .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(plasticDtcReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1346")
    @Description("Test Plastic DTC Export Set Selection")
    public void testPlasticDtcExportSetSelection() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .exportSetSelectAll();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount())));

        genericReportPage.deselectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName());

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount() - 1)));

        genericReportPage.invertExportSetSelection();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(1)));

        genericReportPage.exportSetDeselectAll();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1376")
    @Description("Test Plastic DTC Data Integrity")
    public void testPlasticDtcDataIntegrity() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk();

        genericReportPage.setReportName(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        String partName = genericReportPage.getPartNameDtcReports();
        BigDecimal reportFbcValue = genericReportPage.getFBCValueFromBubbleTooltip();
        genericReportPage.openNewTabAndFocus(1);

        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .setScenarioType(Constants.PART_SCENARIO_TYPE)
            .setWorkspace(Constants.PUBLIC_WORKSPACE)
            .setRowOne("Part Name", "Contains", partName)
            .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
            .apply(ExplorePage.class)
            .openFirstScenario();

        BigDecimal cidFbcValue = evaluatePage.getBurdenedCostValue();

        assertThat(reportFbcValue, is(equalTo(cidFbcValue)));
    }

}
