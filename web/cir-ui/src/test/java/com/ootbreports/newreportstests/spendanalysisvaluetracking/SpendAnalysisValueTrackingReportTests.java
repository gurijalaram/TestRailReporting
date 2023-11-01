package com.ootbreports.newreportstests.spendanalysisvaluetracking;

import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

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
    @Tag(REPORTS_API)
    @TestRail(id = 13955)
    @Description("Input controls - Currency code - Main Report")
    public void testCurrencyCode() {
        jasperApiUtils.spendAnalysisReportGenericCurrencyTest(Arrays.asList(5, 4, 2, 9));
    }
}
