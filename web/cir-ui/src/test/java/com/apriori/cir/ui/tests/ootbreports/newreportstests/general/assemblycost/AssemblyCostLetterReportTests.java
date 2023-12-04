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

public class AssemblyCostLetterReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.ASSEMBLY_COST_LETTER.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.ASSEMBLY_COST_LETTER;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("13712")
    @TestRail(id = {13712})
    @Description("Input controls - Currency code")
    public void testCurrency() {
        jasperApiUtils.genericAssemblyCostCurrencyTest();
    }
}
