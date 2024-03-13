package com.apriori.cir.ui.tests.ootbreports.newreportstests.cycletimevaluetracking;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CycleTimeValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.CYCLE_TIME_VALUE_TRACKING.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.CYCLE_TIME_VALUE_TRACKING;
    // Export Set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("25987")
    @TestRail(id = 25987)
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        jasperApiUtils.cycleTimeValueTrackingCurrencyTest();
    }
}
