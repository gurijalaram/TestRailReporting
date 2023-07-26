package com.ootbreports.newreportstests.targetquotedcosttrend;

import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class TargetAndQuotedCostValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.TARGET_QUOTED_COST_VALUE_TRACKING.getEndpoint();
    // Export set is not relevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(id = {3365})
    @Description("Validate Currency Code Input Control - Target and Quoted Cost Value Tracking Report")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetQuotedCostTrendValueTrackingTest();
    }
}
