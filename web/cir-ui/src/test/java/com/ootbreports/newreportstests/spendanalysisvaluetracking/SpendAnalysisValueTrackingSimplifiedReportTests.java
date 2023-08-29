package com.ootbreports.newreportstests.spendanalysisvaluetracking;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;

public class SpendAnalysisValueTrackingSimplifiedReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.SPEND_ANALYSIS_VALUE_TRACKING_SIMPLIFIED.getEndpoint();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.SPEND_ANALYSIS_VALUE_TRACKING_SIMPLIFIED;
    // Export set is irrelevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 7482)
    @Description("Input Controls - Currency Code - Main Report")
    public void testCurrencyCode() {
        jasperApiUtils.spendAnalysisReportGenericCurrencyTest(Arrays.asList(5, 2, 2, 9));
    }
}
