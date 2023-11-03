package com.ootbreports.newreportstests.general.assemblycost;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

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
    @TestRail(id = 26907)
    @Description("Input controls - Currency code - A4 Report")
    public void testCurrency() {
        jasperApiUtils.genericAssemblyCostCurrencyTest();
    }
}
