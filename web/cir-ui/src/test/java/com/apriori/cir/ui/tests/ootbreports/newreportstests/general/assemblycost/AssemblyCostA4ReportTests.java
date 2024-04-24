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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class AssemblyCostA4ReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.ASSEMBLY_COST_A4.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.ASSEMBLY_COST_A4;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
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
        genericAssemblyCostTests.testExportSetName(
            jasperApiUtils
        );
    }
}
