package com.ootbreports.newreportstests.potentialsavingsvaluetracking;

import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

public class PotentialSavingsValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.POTENTIAL_SAVINGS_VALUE_TRACKING_DETAILS.getEndpoint();
    // Export set is not relevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(id = 26906)
    @Description("Input Controls - Currency Code - Details Report")
    public void testCurrencyCode() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(22, 71, 95);
    }
}
