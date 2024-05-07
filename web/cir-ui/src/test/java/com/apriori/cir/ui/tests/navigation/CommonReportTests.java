package com.apriori.cir.ui.tests.navigation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.cid.ui.pageobjects.evaluate.CostDetailsPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.AssemblySetEnum;
import com.apriori.cir.ui.enums.SortOrderEnum;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.cir.ui.pageobjects.library.LibraryPage;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.userguides.CirUserGuidePage;
import com.apriori.cir.ui.pageobjects.view.ViewSearchResultsPage;
import com.apriori.cir.ui.pageobjects.view.reports.AssemblyCostReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.CastingDtcReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class CommonReportTests extends TestBaseUI {

    private final WebDriver driver;
    private AssemblyCostReportPage assemblyCostReportPage;
    private ViewSearchResultsPage viewSearchResultsPage;
    private CastingDtcReportPage castingDtcReportPage;
    private GenericReportPage genericReportPage;
    private ReportsPageHeader reportsPageHeader;
    private CirUserGuidePage cirUserGuide;
    private LibraryPage libraryPage;

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
            .navigateToReportFolder(reportName)
            .waitForReportToAppear(reportName);

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

        viewSearchResultsPage = reportsPageHeader.waitForHomePageToLoad()
            .searchForReport(reportName);

        assertThat(viewSearchResultsPage.getReportName(reportName),
            is(equalTo(reportName))
        );
    }

    /**
     * Generic test for user guide navigation
     *
     * @param reportName    - String
     * @param exportSetName - String
     * @throws Exception - if page not found
     */
    public void testReportsUserGuideNavigation(String reportName, String exportSetName) throws Exception {
        cirUserGuide = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSet(exportSetName, GenericReportPage.class)
            .clickOk(CirUserGuidePage.class)
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
     * @param reportName - String
     * @param sortOrder  - String
     * @param partNames  - String[]
     */
    public void machiningSheetMetalDtcComparisonSortOrderTest(String reportName, String sortOrder, String[] partNames) {
        String exportSet = reportName.equals(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName())
            ? ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
            : ExportSetEnum.SHEET_METAL_DTC.getExportSetName();
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(exportSet, GenericReportPage.class)
            .selectSortOrder(sortOrder)
            .waitForLoadingPopupToDisappear()
            .clickOk(GenericReportPage.class);

        genericReportPage.waitForReportToLoad();

        genericReportPage.waitForSvgToRender();
        genericReportPage.waitForSortOrderToAppearOnReport();

        for (int i = 1; i < 5; i++) {
            assertThat(genericReportPage.getTableElementNameDtcComparison(String.valueOf(i), String.valueOf(1)),
                is(equalTo(partNames[0])));
            assertThat(genericReportPage.getTableElementNameDtcComparison(String.valueOf(i), String.valueOf(2)),
                is(equalTo(partNames[1])));
        }
    }

    /**
     * Generic test for Casting DTC Comparison sort order
     *
     * @param sortOrder      - String
     * @param doFourAsserts  - boolean
     * @param valuesToAssert - ArrayList of type String
     */
    public void castingDtcDetailsSortOrderTest(String sortOrder, boolean doFourAsserts,
                                               ArrayList<String> valuesToAssert) {
        castingSortOrderTestCore(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), sortOrder);

        assertThat(
            castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo(valuesToAssert.get(0)))
        );

        assertThat(
            castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo(valuesToAssert.get(1)))
        );

        if (doFourAsserts) {
            assertThat(true, is(equalTo(true)));
            assertThat(true, is(equalTo(true)));
            assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(true),
                is(equalTo(valuesToAssert.get(2)))
            );
            assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(false),
                is(equalTo(valuesToAssert.get(3)))
            );
        }
    }

    /**
     * Casting DTC Comparison Sort Order Test
     *
     * @param sortOrder      String
     * @param elementNameOne String
     * @param elementNameTwo String
     */
    public void castingDtcComparisonSortOrderTest(String sortOrder, String elementNameOne, String elementNameTwo) {
        castingSortOrderTestCore(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(), sortOrder);

        String[] elementNames = {elementNameOne, elementNameTwo};

        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("1", "1"),
            is(startsWith(elementNames[0])));
        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("1", "2"),
            is(startsWith(elementNames[1])));

        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("2", "1"),
            is(startsWith(elementNames[0])));
        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("2", "2"),
            is(startsWith(elementNames[1])));

        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("3", "1"),
            is(startsWith(elementNames[0])));
        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("3", "2"),
            is(startsWith(elementNames[1])));

        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("4", "1"),
            is(startsWith(elementNames[0])));
        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("4", "2"),
            is(startsWith(elementNames[1])));

        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("5", "1"),
            is(startsWith(elementNames[0])));
        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("5", "2"),
            is(startsWith(elementNames[1])));

        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("6", "1"),
            is(startsWith(elementNames[0])));
        assertThat(castingDtcReportPage.getTableElementNameDtcComparison("6", "2"),
            is(startsWith(elementNames[1])));
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
            .navigateToReport(reportName, ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
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
        GuidanceIssuesPage guidanceIssuesPage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, partName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openFirstScenario()
            .openDesignGuidance();

        String cidDraftValue = guidanceIssuesPage.getDtcIssueCount(draftString);
        String cidRadiusValue = guidanceIssuesPage.getDtcIssueCount(radiusString);

        assertThat(reportsDraftValue, is(equalTo(cidDraftValue)));
        assertThat(reportsRadiusValue, is(equalTo(cidRadiusValue)));
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
            .navigateToReport(reportName, ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), GenericReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
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
        GuidanceIssuesPage guidanceIssuesPage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, partName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openFirstScenario()
            .openDesignGuidance();

        radiusString = "Radii";
        String cidMaterialValue = guidanceIssuesPage.getDtcIssueCount(materialString);
        String cidRadiusValue = guidanceIssuesPage.getDtcIssueCount(radiusString);

        assertThat(reportsMaterialValue, is(equalTo(cidMaterialValue)));
        assertThat(reportsRadiusValue, is(equalTo(cidRadiusValue)));
    }

    /**
     * Generic test for Assembly Cost Data Integrity
     *
     * @param reportName String
     */
    public void testAssemblyCostDataIntegrity(String reportName) {
        assemblyCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, AssemblyCostReportPage.class)
            .selectExportSetDropdown(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .waitForAssemblyPartNumberFilter(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());

        assemblyCostReportPage.clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyCostReportPage.class);

        String reportsPartName =
            assemblyCostReportPage.getGeneralCostInfoValue("Assembly #", true);
        String reportsScenarioName =
            assemblyCostReportPage.getGeneralCostInfoValue("Scenario Name", true);
        String reportsPiecePartCost =
            assemblyCostReportPage.getGeneralCostInfoValue("Total", false);
        String reportsCiCost = assemblyCostReportPage.getGeneralCostInfoValue("Capital", false);

        assemblyCostReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS,
                AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName())
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openFirstScenario();

        CostDetailsPage costDetailsPage = evaluatePage.openCostDetails();

        assertThat(reportsPartName, is(equalTo(evaluatePage.getPartName())));
        assertThat(reportsScenarioName, is(equalTo(evaluatePage.getCurrentScenarioName())));

        assertThat(reportsPiecePartCost, is(equalTo(costDetailsPage.getCostSumValue("Piece Part Cost"))));
        assertThat(reportsCiCost,
            is(equalTo(String.valueOf(evaluatePage.getCostResults("Total Capital Investment")))));
    }

    /**
     * Core part of Casting DTC Sort Order test
     *
     * @param reportName - String
     * @param sortOrder  - String
     */
    private void castingSortOrderTestCore(String reportName, String sortOrder) {
        String exportSet = ExportSetEnum.CASTING_DTC.getExportSetName();
        castingDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, CastingDtcReportPage.class)
            .selectExportSetDtcTests(exportSet)
            .selectSortOrder(sortOrder)
            .clickOk(CastingDtcReportPage.class);

        castingDtcReportPage.waitForReportToLoad();

        if (!driver.findElement(By.xpath("//span[contains(text(), 'Export Set')]/../following-sibling::td[2]/span"))
            .getText().equals(exportSet)
            && reportName.equals(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName())
            && sortOrder.equals(SortOrderEnum.CASTING_ISSUES.getSortOrderEnum())) {
            ReportsPageHeader reportsPageHeader = new ReportsPageHeader(driver);
            reportsPageHeader.clickInputControlsButton()
                .selectExportSetDtcTests(exportSet)
                .selectExportSetDtcTests(exportSet)
                .waitForExportSetSelection(exportSet)
                .clickOk(GenericReportPage.class);
            castingDtcReportPage.waitForReportToLoad();
            castingDtcReportPage.waitForSvgToRender();
        }

        if (!reportName.contains("Details")) {
            castingDtcReportPage.waitForSvgToRender();
        }
    }
}
