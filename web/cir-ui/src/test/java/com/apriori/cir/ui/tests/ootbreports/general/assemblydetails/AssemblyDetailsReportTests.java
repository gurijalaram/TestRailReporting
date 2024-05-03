package com.apriori.cir.ui.tests.ootbreports.general.assemblydetails;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.AssemblySetEnum;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.AssemblyDetailsReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
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
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AssemblyDetailsReportTests extends TestBaseUI {

    private AssemblyDetailsReportPage assemblyDetailsReportPage;
    private InputControlsTests inputControlsTests;

    public AssemblyDetailsReportTests() {
        super();
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
}
