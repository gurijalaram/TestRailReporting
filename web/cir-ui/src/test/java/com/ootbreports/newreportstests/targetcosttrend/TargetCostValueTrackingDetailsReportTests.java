package com.ootbreports.newreportstests.targetcosttrend;

import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class TargetCostValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.TARGET_COST_VALUE_TRACKING_DETAILS.getEndpoint();
    // Export set is not relevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(id = {13889})
    @Description("Input Controls - Currency Code - Target Cost Value Tracking Details Report")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetCostValueTrackingDetailsCurrencyTest();
    }
}
