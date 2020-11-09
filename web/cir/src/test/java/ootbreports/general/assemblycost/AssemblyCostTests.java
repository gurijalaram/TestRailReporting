package ootbreports.general.assemblycost;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.reports.AssemblySetEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import navigation.CommonReportTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.view.reports.AssemblyCostReportPage;
import testsuites.suiteinterface.CiaCirTestDevTest;

public class AssemblyCostTests extends TestBase {

    private AssemblyCostReportPage assemblyCostReportPage;
    private CommonReportTests commonReportTests;

    public AssemblyCostTests() {
        super();
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName()
        );
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName()
        );
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }


    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3008")
    @Description("Verify Export Set drop-down functions correctly")
    public void testExportSetDropdownFunctionalityAssemblyCostA4() {
        assemblyCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(), AssemblyCostReportPage.class)
                .selectExportSetDropdown(ExportSetEnum.TOP_LEVEL.getExportSetName(), AssemblyCostReportPage.class)
                .waitForAssemblyPartNumberFilter();

        assertThat(assemblyCostReportPage.getAssemblyPartNumberFilterItemCount(), is(equalTo("3")));

        assertThat(assemblyCostReportPage.isAssemblyPartNumberItemDisplayedAndEnabled(
                AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()), is(true));
        assertThat(assemblyCostReportPage.isAssemblyPartNumberItemDisplayedAndEnabled(
                AssemblySetEnum.SUB_SUB_ASM_SHORT.getAssemblySetName()), is(true));
        assertThat(assemblyCostReportPage.isAssemblyPartNumberItemDisplayedAndEnabled(
                AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName()), is(true));
    }

    @Test
    @TestRail(testCaseId = "3008")
    @Description("Verify Export Set drop-down functions correctly")
    public void testExportSetDropdownFunctionalityAssemblyCostLetter() {

    }
}
