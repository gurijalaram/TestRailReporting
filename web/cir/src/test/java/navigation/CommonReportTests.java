package navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;
import pageobjects.header.ReportsPageHeader;
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

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("1", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("1", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("2", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("2", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("3", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("3", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("4", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("4", "2"),
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

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("1", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("1", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("2", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("2", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("3", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("3", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("4", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("4", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("5", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("5", "2"),
                is(equalTo(elementNames[1])));

        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("6", "1"),
                is(equalTo(elementNames[0])));
        assertThat(genericReportPage.getTableElementNameMachiningDtcComparison("6", "2"),
                is(equalTo(elementNames[1])));
    }
}
