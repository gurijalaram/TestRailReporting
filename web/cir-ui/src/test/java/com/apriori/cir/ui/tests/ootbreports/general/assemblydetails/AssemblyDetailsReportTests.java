package com.apriori.cir.ui.tests.ootbreports.general.assemblydetails;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.CUSTOMER;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.AssemblySetEnum;
import com.apriori.cir.ui.enums.AssemblyTypeEnum;
import com.apriori.cir.ui.enums.ComponentInfoColumnEnum;
import com.apriori.cir.ui.pageobjects.header.ReportsHeader;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.AssemblyDetailsReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ListNameEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblyDetailsReportTests extends TestBaseUI {

    private AssemblyDetailsReportPage assemblyDetailsReportPage;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private CommonReportTests commonReportTests;
    private String assemblyType = "";

    public AssemblyDetailsReportTests() {
        super();
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(CUSTOMER)
    })
    @TmsLink("1915")
    @TestRail(id = {1915})
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByNavigationAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3060")
    @TestRail(id = {3060})
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1916")
    @TestRail(id = {1916})
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3232")
    @TmsLink("1929")
    @TestRail(id = {3232, 1929})
    @Description("Verify sub total calculations for Sub Sub ASM")
    public void testSubTotalCalculationsSubSubAsm() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(),
            "Available: ",
            "1"
        );

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
            .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .clickOk(AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Cycle Time"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Piece Part Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Fully Burdened Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Capital Investments"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3233")
    @TmsLink("1929")
    @TestRail(id = {3233, 1929})
    @Description("Verify sub total calculations for Top Level")
    public void testSubTotalCalculationsTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(),
            "Available: ",
            "1"
        );

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
            .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .clickOk(AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Cycle Time"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Piece Part Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Fully Burdened Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
            assemblyType,
            "Capital Investments"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1919")
    @TestRail(id = {1919})
    @Description("Ensuring latest export date filter works properly (uses date input field)")
    public void testLatestExportDateFilterUsingInput() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3244")
    @TestRail(id = {3244})
    @Description("Ensuring latest export date filter works properly (using date picker)")
    public void testLatestExportDateFilterUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @Disabled("CID integration not working consistently well")
    @TmsLink("1930")
    @TestRail(id = {1930})
    @Description("Test Export Set with costing failures costing incomplete")
    public void testExportSetWithCostingFailuresCostingIncomplete() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.PISTON_ASSEMBLY.getExportSetName(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        //assemblyDetailsReportPage.openNewCidTabAndFocus(1);

        List<String> columnsToAdd = Arrays.asList(
            ComponentInfoColumnEnum.CYCLE_TIME.getColumnName(),
            ComponentInfoColumnEnum.PIECE_PART_COST.getColumnName(),
            ComponentInfoColumnEnum.FULLY_BURDENED_COST.getColumnName(),
            ComponentInfoColumnEnum.CAPITAL_INVESTMENT.getColumnName()
        );

        ComponentsTablePage componentsPage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, Constants.PISTON_ASSEMBLY_CID_NAME)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openFirstScenario()
            .openComponents()
            .selectTableView();
        /*.openComponentsTable()
        .openColumnsTable()
        .checkColumnSettings(columnsToAdd)
        .selectSaveButton();*/

        /*ArrayList<BigDecimal> cidPartOneValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_ONE.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartTwoValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_TWO.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartThreeValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_THREE.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartFourValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_FOUR.getColumnIndex()
            );

        componentsPage.switchBackToTabOne();
        ArrayList<BigDecimal> reportsPartOneValues = assemblyDetailsReportPage.getValuesByRow(
                ColumnIndexEnum.CIR_PART_ONE.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartTwoValues = assemblyDetailsReportPage.getValuesByRow(
                ColumnIndexEnum.CIR_PART_TWO.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartThreeValues = assemblyDetailsReportPage.getValuesByRow(
                ColumnIndexEnum.CIR_PART_THREE.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartFourValues = assemblyDetailsReportPage.getValuesByRow(
                ColumnIndexEnum.CIR_PART_FOUR.getColumnIndex()
            );

        assertThat(
            cidPartOneValues.equals(reportsPartFourValues),
            is(true)
        );
        assertThat(
            cidPartTwoValues.equals(reportsPartThreeValues),
            is(true)
        );
        assertThat(
            cidPartThreeValues.equals(reportsPartOneValues),
            is(true)
        );
        assertThat(
            cidPartFourValues.equals(reportsPartTwoValues),
            is(true)
        );*/
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1918")
    @TestRail(id = {1918})
    @Description("Verify Export set of a part file is not available for selection")
    public void testAssemblySelectDropdown() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), AssemblyDetailsReportPage.class);

        assertThat(
            assemblyDetailsReportPage.getAssemblyNameFromSetAssemblyDropdown(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName()),
            containsString(Constants.ASSEMBLY_STRING)
        );

        assemblyDetailsReportPage.setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName());
        assemblyDetailsReportPage.waitForCorrectAssemblyInDropdown(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName());
        assertThat(
            assemblyDetailsReportPage.getAssemblyNameFromSetAssemblyDropdown(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName()),
            containsString(Constants.ASSEMBLY_STRING)
        );

        assemblyDetailsReportPage.setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName());
        assemblyDetailsReportPage.waitForCorrectAssemblyInDropdown(AssemblySetEnum.TOP_LEVEL.getAssemblySetName());
        assertThat(
            assemblyDetailsReportPage.getAssemblyNameFromSetAssemblyDropdown(AssemblySetEnum.TOP_LEVEL.getAssemblySetName()),
            containsString(Constants.ASSEMBLY_STRING)
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1920")
    @TestRail(id = {1920})
    @Description("Export set count is correct")
    public void testExportSetSelectionOptions() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1931")
    @TestRail(id = {1931})
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testLinksToComponentCostReport() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        ReportsPageHeader reportsPageHeader = new ReportsPageHeader(driver);
        reportsPageHeader.clickInputControlsButton();
        reportsPageHeader.waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), AssemblyDetailsReportPage.class)
            .clickOk(AssemblyDetailsReportPage.class);

        String partNumberComponent = assemblyDetailsReportPage.getComponentLinkPartNumber();
        assemblyDetailsReportPage.clickComponentLinkAssemblyDetails();

        assertThat(assemblyDetailsReportPage.getReportTitle(),
            is(equalTo(ReportNamesEnum.COMPONENT_COST_INTERNAL_USE.getReportName())));
        assertThat(assemblyDetailsReportPage.getComponentCostPartNumber(), is(equalTo(partNumberComponent)));
        assemblyDetailsReportPage.closeTab();

        String partNumberAssembly = assemblyDetailsReportPage.getAssemblyLinkPartNumber();
        assemblyDetailsReportPage.clickAssemblyLinkAssemblyDetails();
        assertThat(assemblyDetailsReportPage.getReportTitle(),
            is(equalTo(ReportNamesEnum.COMPONENT_COST_INTERNAL_USE.getReportName())));
        assertThat(assemblyDetailsReportPage.getComponentCostPartNumber(), is(equalTo(partNumberAssembly)));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1921")
    @TestRail(id = {1921})
    @Description("Export Set search function works - Assembly Details Report")
    public void testExportSetSearch() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class);

        new ReportsPageHeader(driver).waitForInputControlsLoad()
            .searchForExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        assertThat(assemblyDetailsReportPage.getExportSetOptionCount(), is(equalTo("2")));
        assertThat(assemblyDetailsReportPage.isExportSetVisible(ExportSetEnum.TOP_LEVEL.getExportSetName()), is(true));
        assertThat(assemblyDetailsReportPage.isExportSetVisible(ExportSetEnum.TOP_LEVEL_MULTI_VPE.getExportSetName()), is(true));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7683")
    @TestRail(id = {7683})
    @Description("Verify Created By Filter Search")
    public void testCreatedByFilterSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterSearch(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7684")
    @TestRail(id = {7684})
    @Description("Verify Created By Filter Operation")
    public void testCreatedByFilterOperation() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class);

        String lastModifiedByAvailableCountPreSelection = assemblyDetailsReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available");
        String scenarioNameAvailableCountPreSelection = assemblyDetailsReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        assemblyDetailsReportPage.selectListItem(ListNameEnum.CREATED_BY.getListName(), Constants.NAME_TO_SELECT);

        assemblyDetailsReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.CREATED_BY.getListName(),
            "Selected: ",
            "1"
        );
        assertThat(assemblyDetailsReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.CREATED_BY.getListName(),
            "Selected"
        ), is(equalTo("1")));

        assemblyDetailsReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available: ", "2");

        String lastModifiedByAvailableCountPostSelection = assemblyDetailsReportPage
            .getCountOfListAvailableOrSelectedItems(
                ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available");

        String scenarioNameAvailableCountPostSelection = assemblyDetailsReportPage
            .getCountOfListAvailableOrSelectedItems(
                ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        assertThat(lastModifiedByAvailableCountPreSelection,
            is(not(equalTo(lastModifiedByAvailableCountPostSelection))));
        assertThat(scenarioNameAvailableCountPreSelection,
            is(not(equalTo(scenarioNameAvailableCountPostSelection))));

        assemblyDetailsReportPage.waitForCorrectAssemblyInDropdown(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName());
        assertThat(assemblyDetailsReportPage.getCurrentlySelectedAssembly(),
            is(startsWith(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7685")
    @TestRail(id = {7685})
    @Description("Verify Created By Filter Buttons")
    public void testCreatedByFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            "",
            ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7686")
    @TestRail(id = {7686})
    @Description("Verify Last Modified By Filter Search")
    public void testLastModifiedFilterSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterSearch(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7687")
    @TestRail(id = {7687})
    @Description("Verify Last Modified By Filter Operation")
    public void testLastModifiedFilterOperation() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class);

        String scenarioNameAvailableCountPreSelection = assemblyDetailsReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        assemblyDetailsReportPage.selectListItem(ListNameEnum.LAST_MODIFIED_BY.getListName(), Constants.NAME_TO_SELECT);

        assemblyDetailsReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.LAST_MODIFIED_BY.getListName(),
            "Selected: ",
            "1"
        );
        assertThat(assemblyDetailsReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.LAST_MODIFIED_BY.getListName(),
            "Selected"
        ), is(equalTo("1")));

        String scenarioNameAvailableCountPostSelection = assemblyDetailsReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        assertThat(scenarioNameAvailableCountPreSelection,
            is(not(equalTo(scenarioNameAvailableCountPostSelection))));

        assemblyDetailsReportPage.waitForCorrectAssemblyInDropdown(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName());
        assertThat(assemblyDetailsReportPage.getCurrentlySelectedAssembly(),
            is(startsWith(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7688")
    @TestRail(id = {7688})
    @Description("Verify Last Modified By Filter Buttons")
    public void testLastModifiedFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            "",
            ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7689")
    @TmsLink("1921")
    @TestRail(id = {7689, 1921})
    @Description("Verify Assembly Number Search Criteria")
    public void testAssemblyNumberSearchCriteria() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAssemblyNumberSearchCriteria(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @Disabled("CID integration not working consistently well")
    @TmsLink("1924")
    @TestRail(id = {1924})
    @Description("Verify report figures from CI Design")
    public void testDataIntegrity() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class)
            .clickOk(AssemblyDetailsReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        Map<String, String> reportsValues = new HashMap<>();
        reportsValues.put("Part Name", assemblyDetailsReportPage.getRowFivePartName());
        reportsValues.put("Cycle Time", assemblyDetailsReportPage.getFiguresFromTable("Cycle Time"));
        reportsValues.put("Piece Part Cost", assemblyDetailsReportPage.getFiguresFromTable("Piece Part Cost"));
        reportsValues.put("Fully Burdened Cost",
            assemblyDetailsReportPage.getFiguresFromTable("Fully Burdened Cost"));
        reportsValues.put("Capital Investments",
            assemblyDetailsReportPage.getFiguresFromTable("Capital Investments"));

        //assemblyDetailsReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, reportsValues.get("Part Name"))
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openFirstScenario();

        Map<String, String> cidValues = new HashMap<>();
        cidValues.put("Cycle Time", String.valueOf(evaluatePage.getProcessesResult("Total Cycle Time")));
        cidValues.put("Piece Part Cost", String.valueOf(evaluatePage.getCostResults("Piece Part Cost")));
        cidValues.put("Fully Burdened Cost", String.valueOf(evaluatePage.getCostResults("Fully Burdened Cost")));
        cidValues.put("Capital Investments", String.valueOf(evaluatePage.getCostResults("Total Capital Investment")));

        assertThat(reportsValues.get("Cycle Time"), is(equalTo(cidValues.get("Cycle Time"))));
        assertThat(reportsValues.get("Piece Part Cost"), is(cidValues.get("Piece Part Cost")));
        assertThat(reportsValues.get("Fully Burdened Cost"), is(cidValues.get("Fully Burdened Cost")));
        assertThat(reportsValues.get("Capital Investments").substring(0, 3), is(cidValues.get("Capital Investments")));
    }

    @Test
    @Tag(REPORTS)
    @Disabled("CID integration not working consistently well")
    @TmsLink("1927")
    @TestRail(id = {1927})
    @Description("Validate multiple VPE usage aligns to CID usage")
    public void testMultiVPEAgainstCID() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL_MULTI_VPE.getExportSetName(), AssemblyDetailsReportPage.class);

        assemblyDetailsReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(),
            "Available: ",
            "1"
        );

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
            .setAssembly(AssemblySetEnum.TOP_LEVEL_MULTI_VPE.getAssemblySetName())
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<String> reportsVpeValues = assemblyDetailsReportPage.getAllVpeValuesAssemblyDetailsReport();

        //assemblyDetailsReportPage.openNewCidTabAndFocus(1);

        ComponentsTablePage componentsTablePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS,
                AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName())
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, "Multi VPE")
            .submit(ExplorePage.class)
            .openFirstScenario().openComponents()
            .selectTableView();

        /*ArrayList<String> cidVpeValues = componentsListPage.getVpeValues();

        assertThat(reportsVpeValues.equals(cidVpeValues), is(equalTo(true)));*/
    }
}
