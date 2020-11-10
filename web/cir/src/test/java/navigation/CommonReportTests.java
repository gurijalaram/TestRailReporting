package navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;
import pageobjects.header.ReportsPageHeader;
import pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.library.LibraryPage;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.userguides.CirUserGuidePage;
import pageobjects.pages.view.ViewSearchResultsPage;
import pageobjects.pages.view.reports.GenericReportPage;

public class CommonReportTests extends TestBase {

    private ViewSearchResultsPage viewSearchResultsPage;
    private GenericReportPage genericReportPage;
    private ReportsPageHeader reportsPageHeader;
    private CirUserGuidePage cirUserGuide;
    private LibraryPage libraryPage;
    private WebDriver driver;

    public CommonReportTests(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Generic test for report availability by navigation
     * @param reportName - String
     */
    public void testReportAvailabilityByNavigation(String firstFolder, String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToViewRepositoryPage()
                .navigateToReportFolder(firstFolder, reportName);

        assertThat(reportName, is(equalTo(genericReportPage.getReportName(reportName))));
    }

    /**
     * Generic test for report availability by library
     * @param reportName - String
     */
    public void testReportAvailabilityByLibrary(String reportName) {
        libraryPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage();

        assertThat(reportName, is(equalTo(libraryPage.getReportName(reportName))));
    }

    /**
     * Generic test for report availability by search
     * @param reportName - String
     */
    public void testReportAvailabilityBySearch(String reportName) {
        reportsPageHeader = new ReportsLoginPage(driver)
                .login();

        viewSearchResultsPage = reportsPageHeader.searchForReport(reportName);

        assertThat(viewSearchResultsPage.getReportName(reportName),
                is(equalTo(reportName))
        );
    }

    /**
     * Generic test for user guide navigation
     * @param reportName - String
     * @param exportSetName - String
     * @throws Exception
     */
    public void testReportsUserGuideNavigation(String reportName, String exportSetName) throws Exception {
        cirUserGuide = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(exportSetName)
                .clickOk()
                .navigateToReportUserGuide()
                .switchTab()
                .switchToIFrameUserGuide("page_iframe");

        assertThat(cirUserGuide.getReportsUserGuidePageHeading(), is(equalTo("Cost Insight Report:User Guide")));
        assertThat(cirUserGuide.getCurrentUrl(), is(containsString("CIR_UG")));
        assertThat(cirUserGuide.getTabCount(), is(2));
    }

    /**
     * Machining DTC Comparison Sort Order Test
     * @param sortOrder String
     * @param elementNameOne String
     * @param elementNameTwo String
     */
    public void machiningDtcComparisonSortOrderTest(String sortOrder, String elementNameOne, String elementNameTwo) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
                .selectSortOrder(sortOrder)
                .clickOk();

        genericReportPage.waitForReportToLoad();
        String[] elementNames = {elementNameOne, elementNameTwo};

        assertThat(genericReportPage.getTableElementNameDtcComparison("1", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("1", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("2", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("2", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("3", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("3", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("4", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("4", "2"),
                is(equalTo(elementNames[1])));
    }

    /**
     * Machining DTC Comparison Sort Order Test
     * @param sortOrder String
     * @param elementNameOne String
     * @param elementNameTwo String
     */
    public void castingDtcComparisonSortOrderTest(String sortOrder, String elementNameOne, String elementNameTwo) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
                .selectSortOrder(sortOrder)
                .clickOk();

        genericReportPage.waitForReportToLoad();
        String[] elementNames = {elementNameOne, elementNameTwo};

        assertThat(genericReportPage.getTableElementNameDtcComparison("1", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("1", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("2", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("2", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("3", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("3", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("4", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("4", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("5", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("5", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("6", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("6", "2"),
                is(equalTo(elementNames[1])));
    }

    /**
     * Generic test for Casting DTC Details and Comparison DTC Issue Counts
     * @param reportName String
     */
    public void testCastingDtcIssueCounts(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        String partName = "";
        String reportsDraftValue = "";
        String reportsRadiusValue = "";
        String draftString = "Draft";
        String radiusString = "Radius";

        if (reportName.equals(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName())) {
            genericReportPage.hoverBarDtcComparison(reportName);
            partName = genericReportPage.getPartNameDtcComparisonTooltip().substring(0, 30);
            reportsDraftValue = genericReportPage.getDtcIssueValueDtcComparison(draftString);
            reportsRadiusValue = genericReportPage.getDtcIssueValueDtcComparison(radiusString);
        } else {
            partName = genericReportPage.getPartNameRowOneCastingDtcDetails();
            reportsDraftValue = genericReportPage.getDtcIssueValueDtcDetails(reportName, draftString);
            reportsRadiusValue = genericReportPage.getDtcIssueValueDtcDetails(reportName, radiusString);
        }
        genericReportPage.openNewCidTabAndFocus(1);

        DesignGuidancePage designGuidancePage = new ExplorePage(driver)
                .filter()
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setRowOne("Part Name", "Contains", partName)
                .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
                .apply(ExplorePage.class)
                .openFirstScenario()
                .openDesignGuidance();

        String cidDraftValue = designGuidancePage.getDtcIssueValue(draftString);
        String cidRadiusValue = designGuidancePage.getDtcIssueValue(radiusString);

        assertThat(reportsDraftValue, is(equalTo(cidDraftValue)));
        assertThat(reportsRadiusValue, is(equalTo(cidRadiusValue)));
    }

    /**
     * Generic test for Plastic DTC Details and Comparison DTC Issue Counts
     * @param reportName String
     */
    public void testPlasticDtcIssueCounts(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        String partName = "";
        String reportsMaterialValue = "";
        String reportsRadiusValue = "";
        String materialString = "Material";
        String radiusString = "Radius";

        if (reportName.equals(ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName())) {
            genericReportPage.hoverBarDtcComparison(reportName);
            partName = genericReportPage.getPartNameDtcComparisonTooltip();
            reportsMaterialValue = genericReportPage.getDtcIssueValueDtcComparison(materialString);
            reportsRadiusValue = genericReportPage.getDtcIssueValueDtcComparison(radiusString);
        } else {
            partName = genericReportPage.getPartNameRowOneCastingDtcDetails();
            reportsMaterialValue = genericReportPage.getDtcIssueValueDtcDetails(reportName, materialString);
            reportsRadiusValue = genericReportPage.getDtcIssueValueDtcDetails(reportName, radiusString);
        }
        genericReportPage.openNewCidTabAndFocus(1);

        DesignGuidancePage designGuidancePage = new ExplorePage(driver)
                .filter()
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setRowOne("Part Name", "Contains", partName)
                .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
                .apply(ExplorePage.class)
                .openFirstScenario()
                .openDesignGuidance();

        radiusString = "Radii";
        String cidMaterialValue = designGuidancePage.getDtcIssueValue(materialString);
        String cidRadiusValue = designGuidancePage.getDtcIssueValue(radiusString);

        assertThat(reportsMaterialValue, is(equalTo(cidMaterialValue)));
        assertThat(reportsRadiusValue, is(equalTo(cidRadiusValue)));
    }
}
