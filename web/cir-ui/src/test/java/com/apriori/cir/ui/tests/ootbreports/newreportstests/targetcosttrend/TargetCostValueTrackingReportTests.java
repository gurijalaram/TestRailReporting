package com.apriori.cir.ui.tests.ootbreports.newreportstests.targetcosttrend;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TargetCostValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.TARGET_COST_VALUE_TRACKING.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.TARGET_COST_VALUE_TRACKING;
    // Export set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("2246")
    @TestRail(id = 2246)
    @Description("Input Controls - Currency Code - Target Quoted Cost Value Tracking Report")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(8, 68, 69);
    }
}
