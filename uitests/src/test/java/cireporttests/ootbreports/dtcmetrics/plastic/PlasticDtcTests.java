package cireporttests.ootbreports.dtcmetrics.plastic;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.reports.pages.view.reports.PlasticDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.AssemblyTypeEnum;
import com.apriori.utils.enums.reports.CastingReportsEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.PlasticDtcReportsEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import cireporttests.inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import org.junit.Test;

import java.math.BigDecimal;

public class PlasticDtcTests extends TestBase {

    private PlasticDtcReportPage plasticDtcReportPage;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private ViewRepositoryPage repository;
    private String assemblyType = "";

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
    @TestRail(testCaseId = "1365")
    @Description("Verify rollup dropdown input control functions correctly")
    public void testRollupDropdownInputControlsFunctionsProperly() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class)
                .waitForInputControlsLoad()
                .selectRollup(RollupEnum.ROLL_UP_A.getRollupName())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        assertThat(plasticDtcReportPage.getDisplayedRollup(),
                is(equalTo(RollupEnum.ROLL_UP_A.getRollupName())));
    }

    @Test
    @TestRail(testCaseId = "1370")
    @Description("Verify currency code functionality works correctly")
    public void testCurrencyCodeFunctionality() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpAnnualSpend;
        BigDecimal usdAnnualSpend;

        plasticDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class)
                .waitForInputControlsLoad()
                .selectRollup(RollupEnum.ROLL_UP_A.getRollupName())
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        plasticDtcReportPage.setReportName(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName());
        plasticDtcReportPage.hoverPartNameBubbleDtcReports();
        usdAnnualSpend = plasticDtcReportPage.getAnnualSpendFromBubbleTooltip();

        plasticDtcReportPage.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), PlasticDtcReportPage.class);

        plasticDtcReportPage.hoverPartNameBubbleDtcReports();
        gbpAnnualSpend = plasticDtcReportPage.getAnnualSpendFromBubbleTooltip();

        assertThat(plasticDtcReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpAnnualSpend, is(not(usdAnnualSpend)));
    }

    @Test
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Input Field")
    public void testPlasticDtcExportSetFilterInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Date Picker")
    public void testPlasticDtcExportSetFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName());
    }

    @Test
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

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC input control panel functions correctly")
    public void testApplyButton() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName(), PlasticDtcReportPage.class)
            .waitForInputControlsLoad()
            .selectRollup(RollupEnum.ROLL_UP_A.getRollupName())
            .clickApply()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        assertThat(plasticDtcReportPage.getDisplayedRollup(),
                is(equalTo(RollupEnum.ROLL_UP_A.getRollupName())));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC input control panel works")
    public void testCancelButton() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .clickCancel(PlasticDtcReportPage.class);

        assertThat(plasticDtcReportPage.getInputControlsDivClassName(), containsString("hidden"));
        assertThat(plasticDtcReportPage.inputControlsIsDisplayed(), is(equalTo(false)));
        assertThat(plasticDtcReportPage.inputControlsIsEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC input control panel works")
    public void testResetButton() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .clickReset()
            .waitForExpectedExportCount("0");

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Casting DTC input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .clickSave()
            .enterSaveName("Saved Config")
            .clickSaveAsButton()
            .clickReset()
            .selectSavedOptionByName("Saved Config");

        assertThat(genericReportPage.isExportSetSelected(ExportSetEnum.ROLL_UP_A.getExportSetName()), is(true));

        genericReportPage.clickRemove();

        assertThat(genericReportPage.isOptionInDropDown("Saved Config", 1), is(false));
    }
}
