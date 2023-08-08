package com.ootbreports.newreportstests.spendanalysisvaluetracking;

import static org.junit.Assert.assertNotEquals;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class SpendAnalysisValueTrackingSimplifiedReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.SPEND_ANALYSIS_VALUE_TRACKING_SIMPLIFIED.getEndpoint();
    // Export set is irrelevant for this report
    private static final String exportSetName = "";
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.SPEND_ANALYSIS_VALUE_TRACKING_SIMPLIFIED;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(testCaseId = {"7482"})
    @Description("Input Controls - Currency Code - Main Report")
    public void testCurrencyCode() {
        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreProjectRollupAndCurrencyOnly(
            CurrencyEnum.GBP.getCurrency(),
            "AC CYCLE TIME VT 1"
        );

        String gbpCurrencySettingAboveChart = gbpJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(2).text();
        String gbpInitialApCost = gbpJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(50).siblingElements().get(5).text();

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreProjectRollupAndCurrencyOnly(
            CurrencyEnum.USD.getCurrency(),
            "AC CYCLE TIME VT 1"
        );

        String usdCurrencySettingAboveChart = usdJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(2).text();
        String usdInitialApCost = usdJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(50).siblingElements().get(5).text();

        assertNotEquals(gbpCurrencySettingAboveChart, usdCurrencySettingAboveChart);
        assertNotEquals(gbpInitialApCost, usdInitialApCost);
    }
}
