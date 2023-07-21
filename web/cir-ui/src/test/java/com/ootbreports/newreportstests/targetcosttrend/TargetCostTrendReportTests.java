package com.ootbreports.newreportstests.targetcosttrend;

import com.apriori.utils.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class TargetCostTrendReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.TARGET_COST_TREND.getEndpoint();
    // Export set is not relevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"2210"})
    @Description("Input controls - Currency code")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetCostTrendCurrencyTest();
    }
}
