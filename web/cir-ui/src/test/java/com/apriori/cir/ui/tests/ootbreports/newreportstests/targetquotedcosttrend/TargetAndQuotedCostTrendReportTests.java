package com.apriori.cir.ui.tests.ootbreports.newreportstests.targetquotedcosttrend;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TargetAndQuotedCostTrendReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.TARGET_QUOTED_COST_TREND.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.TARGET_AND_QUOTED_COST_TREND;
    // Export set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("25838")
    @TestRail(id = 25838)
    @Description("Input controls - Currency code")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(8, 23, 33);
    }
}
