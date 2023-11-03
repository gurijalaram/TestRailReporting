package com.apriori.cir.ui.tests.ootbreports.newreportstests.spendanalysisvaluetracking;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SpendAnalysisValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SPEND_ANALYSIS_VALUE_TRACKING.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.SPEND_ANALYSIS_VALUE_TRACKING;
    // Export set is irrelevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 13955)
    @Description("Input controls - Currency code - Main Report")
    public void testCurrencyCode() {
        jasperApiUtils.spendAnalysisReportGenericCurrencyTest(Arrays.asList(5, 4, 2, 9));
    }
}
