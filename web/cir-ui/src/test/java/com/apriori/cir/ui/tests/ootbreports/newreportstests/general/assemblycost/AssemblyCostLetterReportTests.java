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

public class AssemblyCostLetterReportTests extends JasperApiAuthenticationUtil {
    private final String reportsJsonFileName = JasperApiEnum.ASSEMBLY_COST_LETTER.getEndpoint();
    private final JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.ASSEMBLY_COST;
    private final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13712")
    @TestRail(id = {13712})
    @Description("Input controls - Currency code")
    public void testCurrency() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.genericAssemblyCostCurrencyTest(
            jasperApiUtils
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7621")
    @TestRail(id = 7621)
    @Description("Verify Export Set drop-down functions correctly - Top-Level - Assembly Cost (Letter) Report")
    public void testExportSetDropdownFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.testExportSetName(jasperApiUtils);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLinks({
        @TmsLink("30958"),
        @TmsLink("7623")
    })
    @TestRail(id = {30958, 7623})
    @Description("Verify Assembly Part Number drop-down functions correctly - Assembly Cost (Letter)")
    public void testAssemblyPartNumberDropdownFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.testAssemblyPartNumberOrScenarioNameDropdown(jasperApiUtils);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7625")
    @TestRail(id = 7625)
    @Description("Verify Scenario Name drop-down functions correctly - Assembly Cost (Letter) Report")
    public void testScenarioNameFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.testAssemblyPartNumberOrScenarioNameDropdown(jasperApiUtils);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("30957")
    @TestRail(id = 30957)
    @Description("Verify capital investment cost total when a particular component is used in separate BOM levels - Assembly Cost (Letter)")
    public void verifyCapitalInvestmentCostTotalFunctionality() {
        GenericAssemblyCostTests genericAssemblyCostTests = new GenericAssemblyCostTests();
        genericAssemblyCostTests.verifyCapitalInvestmentCostFunctionality(jasperApiUtils);
    }
}
