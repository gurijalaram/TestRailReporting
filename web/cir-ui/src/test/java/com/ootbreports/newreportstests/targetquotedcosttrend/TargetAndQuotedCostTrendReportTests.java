package com.ootbreports.newreportstests.targetquotedcosttrend;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

public class TargetAndQuotedCostTrendReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.TARGET_QUOTED_COST_TREND.getEndpoint();
    // Export set is not relevant for this report
    private static final String exportSetName = "";
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.TARGET_AND_QUOTED_COST_TREND;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = {25838})
    @Description("Input controls - Currency code")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(8, 23, 33);
    }
}
