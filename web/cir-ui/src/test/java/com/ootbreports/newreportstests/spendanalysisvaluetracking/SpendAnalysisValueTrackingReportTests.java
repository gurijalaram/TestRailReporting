package com.ootbreports.newreportstests.spendanalysisvaluetracking;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.CurrencyEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

public class SpendAnalysisValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.SPEND_ANALYSIS_VALUE_TRACKING.getEndpoint();
    // Export set is irrelevant for this report
    private static final String exportSetName = "";
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.SPEND_ANALYSIS_VALUE_TRACKING;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 13955)
    @Description("Input controls - Currency code - Main Report")
    public void testCurrencyCode() {
        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreProjectRollupAndCurrencyOnly(
            CurrencyEnum.GBP.getCurrency(),
            "AC CYCLE TIME VT 1"
        );

        String gbpCurrencySettingAboveChart = gbpJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(4).child(0).text();
        String gbpInitialApCost = gbpJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("style", "height:25px").get(6).child(5).child(0).text();

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreProjectRollupAndCurrencyOnly(
            CurrencyEnum.USD.getCurrency(),
            "AC CYCLE TIME VT 1"
        );

        String usdCurrencySettingAboveChart = usdJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(4).child(0).text();
        String usdInitialApCost = usdJasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("style", "height:25px").get(6).child(5).child(0).text();

        assertNotEquals(gbpCurrencySettingAboveChart, usdCurrencySettingAboveChart);
        assertNotEquals(gbpInitialApCost, usdInitialApCost);
    }
}
