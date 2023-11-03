package com.ootbreports.newreportstests.targetcosttrend;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

public class TargetCostValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.TARGET_COST_VALUE_TRACKING_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.TARGET_COST_VALUE_TRACKING_DETAILS;
    // Export set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 13889)
    @Description("Input Controls - Currency Code - Target Cost Value Tracking Details Report")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(20, 67, 68);
    }
}
