package com.ootbreports.newreportstests.digitalfactoryperformance;

import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;
import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class DigitalFactoryPerformanceReportTests extends JasperApiAuthenticationUtil {
    private final List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_257280C.getPartName(),
        JasperCirApiPartsEnum.P_40137441_MLDES_0002_WITHOUT_INITIAL.getPartName()
    );
    private static final String reportsJsonFileName = JasperApiEnum.DIGITAL_FACTORY_PERFORMANCE.getEndpoint();
    private static final String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.DIGITAL_FACTORY_PERFORMANCE;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(testCaseId = {"13915"})
    @Description("Input Controls - Currency Code - Main Report")
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
