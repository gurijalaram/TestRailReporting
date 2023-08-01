package com.ootbreports.newreportstests.potentialsavingsvaluetracking;

import com.apriori.utils.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class PotentialSavingsValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.POTENTIAL_SAVINGS_VALUE_TRACKING.getEndpoint();
    // Export set is not relevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"13875"})
    @Description("Input controls - Currency code - Main Report")
    public void testCurrencyCode() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(8, 68, 78);
    }
}
