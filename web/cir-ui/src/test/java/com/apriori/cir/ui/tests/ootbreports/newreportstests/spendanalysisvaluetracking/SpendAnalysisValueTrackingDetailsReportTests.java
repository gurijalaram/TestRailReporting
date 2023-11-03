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

public class SpendAnalysisValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SPEND_ANALYSIS_VALUE_TRACKING_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.SPEND_ANALYSIS_VALUE_TRACKING_DETAILS;
    // Export set is irrelevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 26932)
    @Description("Input controls - Currency code - Details Report")
    public void testCurrencyCode() {
        jasperApiUtils.spendAnalysisReportGenericCurrencyTest(Arrays.asList(7, 2, 2, 19));
    }
}
