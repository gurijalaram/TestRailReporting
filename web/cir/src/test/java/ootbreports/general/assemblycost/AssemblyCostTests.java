package ootbreports.general.assemblycost;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.reports.AssemblySetEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;

public class AssemblyCostTests extends TestBase {

    private CommonReportTests commonReportTests;

    public AssemblyCostTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation (Assembly Cost A4 Report)")
    public void testReportAvailabilityByMenuAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation (Assembly Cost Letter Report)")
    public void testReportAvailabilityByMenuAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search (Assembly Cost A4 Report)")
    public void testReportAvailableBySearchAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search (Assembly Cost Letter Report)")
    public void testReportAvailableBySearchAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }


    @Test
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library (Assembly Cost A4 Report)")
    public void testReportAvailabilityByLibraryAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library (Assembly Cost Letter Report)")
    public void testReportAvailabilityByLibraryAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3008")
    @Description("Verify Export Set drop-down functions correctly (Assembly Cost A4 Report)")
    public void testExportSetDropdownFunctionalityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testExportSetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                ExportSetEnum.TOP_LEVEL.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3008")
    @Description("Verify Export Set drop-down functions correctly (Assembly Cost Letter Report)")
    public void testExportSetDropdownFunctionalityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testExportSetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(),
                ExportSetEnum.TOP_LEVEL.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3008")
    @Description("Verify Export Set drop-down functions correctly (Assembly Cost A4 Report)")
    public void testAssemblySetDropdownFunctionalityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblySetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3008")
    @Description("Verify Export Set drop-down functions correctly (Assembly Cost Letter Report)")
    public void testAssemblySetDropdownFunctionalityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblySetDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(),
                AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3010")
    @Description("Verify Scenario Name drop-down functions correctly (Assembly Cost A4 Report)")
    public void testScenarioNameDropdownFunctionalityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testScenarioNameDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                Constants.DEFAULT_SCENARIO_NAME
        );
    }

    @Test
    @TestRail(testCaseId = "3010")
    @Description("Verify Scenario Name drop-down functions correctly (Assembly Cost Letter Report)")
    public void testScenarioNameDropdownFunctionalityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testScenarioNameDropdownAssemblyCost(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(),
                Constants.DEFAULT_SCENARIO_NAME
        );
    }

    @Test
    @TestRail(testCaseId = "3002")
    @Description("Verify the user can select sub assemblies from within assembly Export Sets (Assembly Cost A4 Report)")
    public void testSubAssemblySelectionAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testSubAssemblySelectionAssemblyCost(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3002")
    @Description("Verify the user can select sub assemblies from within assembly Export Sets (Assembly Cost Letter Report)")
    public void testSubAssemblySelectionAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testSubAssemblySelectionAssemblyCost(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @TestRail(testCaseId = "2998")
    @Description("Validate report content aligns to aP desktop values (simple case) (Assembly Cost A4 Report)")
    public void testDataIntegrityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblyCostDataIntegrity(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @TestRail(testCaseId = "2998")
    @Description("Validate report content aligns to aP desktop values (simple case) (Assembly Cost Letter Report)")
    public void testDataIntegrityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblyCostDataIntegrity(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }
}
