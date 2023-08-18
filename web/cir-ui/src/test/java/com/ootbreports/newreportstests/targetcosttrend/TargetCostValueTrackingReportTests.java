package com.ootbreports.newreportstests.targetcosttrend;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

public class TargetCostValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.TARGET_COST_VALUE_TRACKING.getEndpoint();
    // Export set is not relevant for this report
    private static final String exportSetName = "";
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.TARGET_COST_VALUE_TRACKING;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = {2246})
    @Description("Input Controls - Currency Code - Target Quoted Cost Value Tracking Report")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(8, 68, 69);
    }
}
