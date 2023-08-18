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

public class AssemblyCostLetterReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.ASSEMBLY_COST_LETTER.getEndpoint();
    private static final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.ASSEMBLY_COST_LETTER;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = {13712})
    @Description("Input controls - Currency code")
    public void testCurrency() {
        jasperApiUtils.genericAssemblyCostCurrencyTest();
    }
}
