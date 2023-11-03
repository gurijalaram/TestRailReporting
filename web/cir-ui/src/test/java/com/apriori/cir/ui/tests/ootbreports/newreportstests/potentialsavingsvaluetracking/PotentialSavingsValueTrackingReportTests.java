package com.apriori.cir.ui.tests.ootbreports.newreportstests.potentialsavingsvaluetracking;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PotentialSavingsValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.POTENTIAL_SAVINGS_VALUE_TRACKING.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.POTENTIAL_SAVINGS_VALUE_TRACKING;
    // Export set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 13875)
    @Description("Input controls - Currency code - Main Report")
    public void testCurrencyCode() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(8, 68, 78);
    }
}
