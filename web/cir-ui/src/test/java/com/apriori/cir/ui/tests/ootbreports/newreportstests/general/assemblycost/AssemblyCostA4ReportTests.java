package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblycost;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AssemblyCostA4ReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.ASSEMBLY_COST_A4.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.ASSEMBLY_COST_A4;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("26907")
    @TestRail(id = 26907)
    @Description("Input controls - Currency code - A4 Report")
    public void testCurrency() {
        jasperApiUtils.genericAssemblyCostCurrencyTest();
    }
}
