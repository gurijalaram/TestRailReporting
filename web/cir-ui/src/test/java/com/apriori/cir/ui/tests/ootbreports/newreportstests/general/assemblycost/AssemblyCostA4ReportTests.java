package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblycost;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class AssemblyCostA4ReportTests extends JasperApiAuthenticationUtil {
    private final String reportsJsonFileName = JasperApiEnum.ASSEMBLY_COST_A4.getEndpoint();
    private final JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.ASSEMBLY_COST;
    private final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("26907")
    @TestRail(id = 26907)
    @Description("Input controls - Currency code - A4 Report")
    public void testCurrency() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.genericAssemblyCostCurrencyTest(
            jasperApiUtils
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3008")
    @TestRail(id = 3008)
    @Description("Verify Export Set drop-down functions correctly - Top-Level - Assembly Cost (A4) Report")
    public void testExportSetDropdownFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.testExportSetName(jasperApiUtils);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLinks({
        @TmsLink("3009"),
        @TmsLink("7622")
    })
    @TestRail(id = {3009, 7622})
    @Description("Verify Assembly Part Number drop-down functions correctly - Assembly Cost (A4) Report")
    public void testAssemblyPartNumberDropdownFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.testAssemblyPartNumberOrScenarioNameDropdown(jasperApiUtils);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3010")
    @TestRail(id = 3010)
    @Description("Verify Scenario Name drop-down functions correctly - Assembly Cost (A4) Report")
    public void testScenarioNameFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.testAssemblyPartNumberOrScenarioNameDropdown(jasperApiUtils);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3002")
    @TestRail(id = 3002)
    @Description("Verify the user can select sub assemblies from within assembly Export Sets - Assembly Cost (A4) Report")
    public void testSubAssemblySelection() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.testSelectAssemblyFunctionality(jasperApiUtils);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3004")
    @TestRail(id = 3004)
    @Description("Verify capital investment cost total when a particular component is used in separate BOM levels - Assembly Cost (A4) Report")
    public void verifyCapitalInvestmentCostTotalFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.verifyCapitalInvestmentCostFunctionality(jasperApiUtils);
    }
}
