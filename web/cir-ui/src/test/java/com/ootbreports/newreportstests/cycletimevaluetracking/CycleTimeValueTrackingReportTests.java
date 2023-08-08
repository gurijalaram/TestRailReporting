package com.ootbreports.newreportstests.cycletimevaluetracking;

import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.utils.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class CycleTimeValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.CYCLE_TIME_VALUE_TRACKING.getEndpoint();
    // Export Set is not relevant for this report
    private static final String exportSetName = "";
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.CYCLE_TIME_VALUE_TRACKING;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(testCaseId = {"25987"})
    @Description("Verify Currency Code input control is working correctly - Main Report")
    public void testCurrencyCode() {
        jasperApiUtils.cycleTimeValueTrackingCurrencyTest();
    }
}
