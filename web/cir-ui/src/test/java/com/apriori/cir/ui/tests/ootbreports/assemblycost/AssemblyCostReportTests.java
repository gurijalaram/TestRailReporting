package com.apriori.cir.ui.tests.ootbreports.assemblycost;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;

import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class AssemblyCostReportTests extends TestBaseUI {

    private CommonReportTests commonReportTests;

    public AssemblyCostReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @Disabled("CID integration not working consistently well")
    @TmsLink("2998")
    @TestRail(id = {2998})
    @Description("Validate report content aligns to aP desktop or CID values (simple case) - Assembly Cost (A4) Report")
    public void testDataIntegrityAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblyCostDataIntegrity(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @Disabled("CID integration not working consistently well")
    @TmsLink("7624")
    @TestRail(id = {7624})
    @Description("Validate report content aligns to aP desktop or CID values (simple case) - Assembly Cost (Letter) Report")
    public void testDataIntegrityAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testAssemblyCostDataIntegrity(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }
}
