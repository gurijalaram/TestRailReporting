package com.apriori.cir.ui.tests.ootbreports.newreportstests.targetcosttrend;

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

public class TargetCostTrendReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.TARGET_COST_TREND.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.TARGET_COST_TREND;
    // Export set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("2210")
    @TestRail(id = 2210)
    @Description("Input controls - Currency code")
    public void testCurrencyCodeInputControl() {
        jasperApiUtils.targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(8, 17, 22);
    }
}
