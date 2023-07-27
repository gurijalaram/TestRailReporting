package com.ootbreports.assemblycost;

import com.apriori.TestBaseUI;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.testrail.TestRail;

import com.navigation.CommonReportTests;
import enums.AssemblySetEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class AssemblyCostReportTests extends TestBaseUI {

    private CommonReportTests commonReportTests;

    public AssemblyCostReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {2995})
    @Description("Validate report is available by navigation - Assembly Cost (A4) Report")
    public void testReportAvailabilityByNavigationAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7132})
    @Description("Validate report is available by navigation - Assembly Cost (Letter) Report")
    public void testReportAvailabilityByNavigationAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {2996})
    @Description("Validate report is available by search - Assembly Cost (A4) Report")
    public void testReportAvailableBySearchAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7130})
    @Description("Validate report is available by search - Assembly Cost (Letter) Report")
    public void testReportAvailableBySearchAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }


    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7129})
    @Description("Validate report is available by library (Assembly Cost A4 Report)")
    public void testReportAvailabilityByLibraryAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = 7131)
    @Description("Validate report is available by library (Assembly Cost Letter Report)")
    public void testReportAvailabilityByLibraryAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {3008})
    @Description("Verify Export Set drop-down functions correctly - Top-Level - Assembly Cost (A4) Report")
    public void testExportSetDropdownFunctionalityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testExportSetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                ExportSetEnum.TOP_LEVEL.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7621})
    @Description("Verify Export Set drop-down functions correctly - Top-Level - Assembly Cost (Letter) Report")
    public void testExportSetDropdownFunctionalityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testExportSetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(),
                ExportSetEnum.TOP_LEVEL.getExportSetName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7622})
    @Description("Verify Export Set drop-down functions correctly - Sub-Assembly - Assembly Cost (A4) Report")
    public void testAssemblySetDropdownFunctionalityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblySetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7623})
    @Description("Verify Export Set drop-down functions correctly - Sub-Assembly - Assembly Cost (Letter) Report")
    public void testAssemblySetDropdownFunctionalityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblySetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(),
                AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3010})
    @Description("Verify Scenario Name drop-down functions correctly - Assembly Cost A4")
    public void testScenarioNameDropdownFunctionalityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testScenarioNameDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                Constants.DEFAULT_SCENARIO_NAME
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7625})
    @Description("Verify Scenario Name drop-down functions correctly - Assembly Cost (Letter) Report")
    public void testScenarioNameDropdownFunctionalityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testScenarioNameDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(),
                Constants.DEFAULT_SCENARIO_NAME
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3002})
    @Description("Verify the user can select sub assemblies from within assembly Export Sets - Assembly Cost (A4) Report")
    public void testSubAssemblySelectionAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testSubAssemblySelectionAssemblyCost(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7626})
    @Description("Verify the user can select sub assemblies from within assembly Export Sets - Assembly Cost (Letter) Report")
    public void testSubAssemblySelectionAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testSubAssemblySelectionAssemblyCost(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    //@Category(ReportsTest.class)
    @TestRail(id = {2998})
    @Description("Validate report content aligns to aP desktop or CID values (simple case) - Assembly Cost (A4) Report")
    public void testDataIntegrityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblyCostDataIntegrity(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    //@Category(ReportsTest.class)
    @TestRail(id = {7624})
    @Description("Validate report content aligns to aP desktop or CID values (simple case) - Assembly Cost (Letter) Report")
    public void testDataIntegrityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblyCostDataIntegrity(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }
}
