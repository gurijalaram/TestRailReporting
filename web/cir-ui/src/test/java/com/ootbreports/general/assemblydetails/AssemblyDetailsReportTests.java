package com.ootbreports.general.assemblydetails;

import static com.apriori.testconfig.TestSuiteType.TestSuite.CUSTOMER;
import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ListNameEnum;
import com.apriori.enums.OperationEnum;
import com.apriori.enums.PropertyEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.ReportsLoginPage;
import com.apriori.pageobjects.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.view.reports.GenericReportPage;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import com.utils.CurrencyEnum;
import enums.AssemblySetEnum;
import enums.AssemblyTypeEnum;
import enums.ComponentInfoColumnEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import utils.Constants;

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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {1915})
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByNavigationAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {3060})
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {1916})
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {1922})
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), AssemblyDetailsReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReportPage.getCapitalInvestmentsGrandTotalFromTable();

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReportPage.getCapitalInvestmentsGrandTotalFromTable();

        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {3205})
    @Description("Verifies that currency change and then reversion works")
    public void testCurrencyCodeReversion() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReportPage.getValueFromTable(
            assemblyType,
            "Grand Total",
            "Capital Investments"
        );
        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReportPage.getValueFromTable(
            assemblyType,
            "Grand Total",
            "Capital Investments"
        );

        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(usdGrandTotal, is(not(equalTo(gbpGrandTotal))));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {3067, 1929})
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .clickOk(AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY_LOWERCASE.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {3068, 1929})
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
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
            .waitForCorrectAssembly(AssemblySetEnum.SUB_SUB_ASM_LOWERCASE.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {1934, 1929})
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
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

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {3231, 1929})
    @Description("Verify sub total calculations for Sub Assembly")
    public void testSubTotalCalculationsSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .clickOk(AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assemblyDetailsReportPage.clickInputControlsButton()
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), AssemblyDetailsReportPage.class)
            .clickOk(AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {3232, 1929})
    @Description("Verify sub total calculations for Sub Sub ASM")
    public void testSubTotalCalculationsSubSubAsm() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {3233, 1929})
    @Description("Verify sub total calculations for Top Level")
    public void testSubTotalCalculationsTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {1919})
    @Description("Ensuring latest export date filter works properly (uses date input field)")
    public void testLatestExportDateFilterUsingInput() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {3244})
    @Description("Ensuring latest export date filter works properly (using date picker)")
    public void testLatestExportDateFilterUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {1930})
    @Description("Test Export Set with costing failures costing incomplete")
    public void testExportSetWithCostingFailuresCostingIncomplete() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {1918})
    @Description("Verify Export set of a part file is not available for selection")
    public void testAssemblySelectDropdown() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {1920})
    @Description("Export set count is correct")
    public void testExportSetSelectionOptions() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(ON_PREM),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {1931})
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testLinksToComponentCostReport() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assemblyDetailsReportPage.clickInputControlsButton()
            .waitForInputControlsLoad()
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {1921})
    @Description("Export Set search function works - Assembly Details Report")
    public void testExportSetSearch() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class);

        assemblyDetailsReportPage.waitForInputControlsLoad()
            .searchForExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        assertThat(assemblyDetailsReportPage.getExportSetOptionCount(), is(equalTo("2")));
        assertThat(assemblyDetailsReportPage.isExportSetVisible(ExportSetEnum.TOP_LEVEL.getExportSetName()), is(true));
        assertThat(assemblyDetailsReportPage.isExportSetVisible(ExportSetEnum.TOP_LEVEL_MULTI_VPE.getExportSetName()), is(true));
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
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
    @Tags({@Tag(REPORTS),
            @Tag(ON_PREM),
            @Tag(CUSTOMER)
    })
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
    //@Tag(REPORTS)
    @TestRail(id = {1924})
    @Description("Verify report figures from CI Design")
    public void testDataIntegrity() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), GenericReportPage.class)
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
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {1928})
    @Description("Validate report content aligns to aP desktop values (many levels inside BOM)")
    public void testLevelsInsideBOM() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class)
            .clickOk(AssemblyDetailsReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assemblyDetailsReportPage.clickInputControlsButton()
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), AssemblyDetailsReportPage.class)
            .clickOk(AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> levelValues =
            assemblyDetailsReportPage.getLevelValues(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType());

        assertThat(levelValues.isEmpty(), is(false));
        for (int i = 0; i < 7; i++) {
            assertThat(levelValues.get(i).compareTo(new BigDecimal("1")), is(0));
        }

        for (int i = 7; i < 10; i++) {
            assertThat(levelValues.get(i).compareTo(new BigDecimal("2")), is(0));
        }
    }

    @Test
    @Tags({@Tag(REPORTS),
            @Tag(CUSTOMER)
    })
    @TestRail(id = {1933})
    @Description("Verify component subassembly report details")
    public void testComponentSubAssemblyReportDetails() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.SUB_SUB_ASM.getExportSetName())
            .waitForExportSetSelection(ExportSetEnum.SUB_SUB_ASM.getExportSetName())
            .selectComponent(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        BigDecimal actualVariance = assemblyDetailsReportPage.getComponentCostReportValue("Variance");
        BigDecimal actualLifetimeCost = assemblyDetailsReportPage.getComponentCostReportValue("Lifetime Cost");
        BigDecimal actualPercentageOfTarget = assemblyDetailsReportPage.getComponentCostReportValue("% of Target");
        BigDecimal actualLifetimeProjectedCostDifference =
            assemblyDetailsReportPage.getComponentCostReportValue("Lifetime Projected Cost");

        assertThat(actualVariance.compareTo(new BigDecimal("79.80")), is(equalTo(0)));
        assertThat(actualPercentageOfTarget.compareTo(new BigDecimal("1596.06")), is(equalTo(0)));
        assertThat(actualLifetimeCost.compareTo(new BigDecimal("2332078.82")), is(equalTo(0)));
        assertThat(actualLifetimeProjectedCostDifference.compareTo(new BigDecimal("2194578.82")),
            is(equalTo(0)));
    }

    @Test
    //@Tag(REPORTS)
    @TestRail(id = {1927})
    @Description("Validate multiple VPE usage aligns to CID usage")
    public void testMultiVPEAgainstCID() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
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
