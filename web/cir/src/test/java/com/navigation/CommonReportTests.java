package com.navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.library.LibraryPage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.pages.view.reports.AssemblyCostReportPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.AssemblySetEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;
import utils.Constants;

import java.util.ArrayList;

public class CommonReportTests extends TestBase {

    private AssemblyCostReportPage assemblyCostReportPage;
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
     *
     * @param reportName - String
     */
    public void testReportAvailabilityByNavigation(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToReportFolder(reportName);

        assertThat(reportName, is(equalTo(genericReportPage.getReportName(reportName))));
    }

    /**
     * Generic test for report availability by library
     *
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
     *
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
     *
     * @param reportName    - String
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
     * Machining and Sheet Metal DTC Comparison Sort Order Test
     *
     * @param reportName     String
     * @param sortOrder      String
     * @param partNames      String[]
     */
    public void machiningSheetMetalDtcComparisonSortOrderTest(String reportName, String sortOrder, String[] partNames) {
        String exportSet = reportName.equals(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName())
                ? ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
                : ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSet(exportSet)
            .selectSortOrder(sortOrder)
            .clickOk();

        genericReportPage.waitForReportToLoad();

        for (int i = 1; i < 5; i++) {
            assertThat(genericReportPage.getTableElementNameDtcComparison(String.valueOf(i), String.valueOf(1)),
                    is(equalTo(partNames[0])));
            assertThat(genericReportPage.getTableElementNameDtcComparison(String.valueOf(i), String.valueOf(2)),
                    is(equalTo(partNames[1])));
        }

        assertThat(genericReportPage.getTableElementNameDtcComparison("1", "1"),
            is(equalTo(partNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("1", "2"),
            is(equalTo(partNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("2", "1"),
            is(equalTo(partNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("2", "2"),
            is(equalTo(partNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("3", "1"),
            is(equalTo(partNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("3", "2"),
            is(equalTo(partNames[1])));

        assertThat(genericReportPage.getTableElementNameDtcComparison("4", "1"),
            is(equalTo(partNames[0])));
        assertThat(genericReportPage.getTableElementNameDtcComparison("4", "2"),
            is(equalTo(partNames[1])));
    }

    /**
     * Machining DTC Comparison Sort Order Test
     *
     * @param sortOrder      String
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
     *
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

        /*DesignGuidancePage designGuidancePage = new ExplorePage(driver)
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
        assertThat(reportsRadiusValue, is(equalTo(cidRadiusValue)));*/
    }

    /**
     * Generic test for Plastic DTC Details and Comparison DTC Issue Counts
     *
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

        /*DesignGuidancePage designGuidancePage = new ExplorePage(driver)
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
        assertThat(reportsRadiusValue, is(equalTo(cidRadiusValue)));*/
    }

    /**
     * Generic test for Export Set Dropdown in Assembly Cost Reports
     * @param reportName String
     * @param exportSetName String
     */
    public void testExportSetDropdownAssemblyCost(String reportName, String exportSetName) {
        assemblyCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, AssemblyCostReportPage.class)
                .selectExportSetDropdown(exportSetName)
                .waitForAssemblyPartNumberFilter(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());

        assertThat(assemblyCostReportPage.getAssemblyPartNumberFilterItemCount(), is(equalTo("3")));

        assertThat(assemblyCostReportPage.isAssemblyPartNumberItemEnabled(
                AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()), is(true));

        assertThat(assemblyCostReportPage.isAssemblyPartNumberItemEnabled(
                AssemblySetEnum.SUB_SUB_ASM_SHORT.getAssemblySetName()), is(true));

        assertThat(assemblyCostReportPage.isAssemblyPartNumberItemEnabled(
                AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName()), is(true));
    }

    /**
     * Generic test for Export Set Dropdown in Assembly Cost Reports
     * @param reportName String
     * @param assemblyName String
     */
    public void testAssemblySetDropdownAssemblyCost(String reportName, String assemblyName) {
        assemblyCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, AssemblyCostReportPage.class)
                .selectExportSetDropdown(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .waitForAssemblyPartNumberFilter(assemblyName)
                .selectAssemblySetDropdown(assemblyName);

        assertThat(assemblyCostReportPage.getScenarioNameCount(), is(equalTo("1")));

        assertThat(assemblyCostReportPage.isScenarioNameEnabled(
                Constants.DEFAULT_SCENARIO_NAME), is(true));
    }

    /**
     * Generic test for Scenario Name Dropdown
     * @param reportName String
     * @param scenarioName String
     */
    public void testScenarioNameDropdownAssemblyCost(String reportName, String scenarioName) {
        assemblyCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, AssemblyCostReportPage.class)
                .selectExportSetDropdown(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .selectScenarioNameDropdown(scenarioName);

        assertThat(assemblyCostReportPage.getScenarioNameCount(), is(equalTo("1")));

        assertThat(assemblyCostReportPage.isScenarioNameEnabled(
                Constants.DEFAULT_SCENARIO_NAME), is(true));
    }

    /**
     * Generic test to check that Sub Assembly selection works
     * @param reportName String
     */
    public void testSubAssemblySelectionAssemblyCost(String reportName) {
        assemblyCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, AssemblyCostReportPage.class)
                .selectExportSetDropdown(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .waitForAssemblyPartNumberFilter(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());

        ArrayList<String> assemblyNames = new ArrayList<String>() {
            {
                add(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());
                add(AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName());
                add(AssemblySetEnum.SUB_SUB_ASM_SHORT.getAssemblySetName());
            }};

        for (String assemblyName : assemblyNames) {
            assemblyCostReportPage.selectAssemblySetDropdown(assemblyName);

            assertThat(assemblyCostReportPage.getCurrentAssemblyPartNumber(), is(equalTo(assemblyName)));
        }
    }

    /**
     * Generic test for Assembly Cost Data Integrity
     * @param reportName String
     */
    public void testAssemblyCostDataIntegrity(String reportName) {
        assemblyCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, AssemblyCostReportPage.class)
                .selectExportSetDropdown(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .waitForAssemblyPartNumberFilter(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());

        assemblyCostReportPage.clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyCostReportPage.class);

        String reportsPartName =
                assemblyCostReportPage.getGeneralCostInfoValue("Assembly #", true);
        String reportsScenarioName =
                assemblyCostReportPage.getGeneralCostInfoValue("Scenario Name", true);
        String reportsPiecePartCost =
                assemblyCostReportPage.getGeneralCostInfoValue("Total", false);
        String reportsCiCost = assemblyCostReportPage.getGeneralCostInfoValue("Capital", false);

        assemblyCostReportPage.openNewCidTabAndFocus(1);

        /*EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setScenarioType(Constants.ASSEMBLY_SCENARIO_TYPE)
                .setRowOne("Part Name", "Contains", AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName())
                .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
                .apply(ExplorePage.class)
                .openFirstScenario();

        evaluatePage.waitForCostsToLoad();
        CostDetailsPage costDetailsPage = evaluatePage.openAssemblyCostDetails();

        assertThat(reportsPartName, is(equalTo(evaluatePage.getPartName())));
        assertThat(reportsScenarioName, is(equalTo(evaluatePage.getScenarioName())));

        assertThat(reportsPiecePartCost, is(equalTo(costDetailsPage.getPiecePartCostString())));
        assertThat(reportsCiCost, is(equalTo(costDetailsPage.getTotalCapitalInvestments())));*/
    }
}
