package com.ootbreports.newreportstests.digitalfactoryperformance;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.ArrayList;

public class DigitalFactoryPerformanceReportTests extends JasperApiAuthenticationUtil {
    private static final String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private static final String reportsJsonFileName = JasperApiEnum.DIGITAL_FACTORY_PERFORMANCE.getEndpoint();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.DIGITAL_FACTORY_PERFORMANCE;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 13915)
    @Description("Input Controls - Currency Code - Main Report")
    public void testCurrencyCode() {
        String gbpCurrency = CurrencyEnum.GBP.getCurrency();
        String usdCurrency = CurrencyEnum.USD.getCurrency();

        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnly(gbpCurrency);
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnly(usdCurrency);
        ArrayList<String> usdAssertValues = getAssertValues(usdJasperReportSummary);

        assertAll("Grouped Currency Assertions",
            () -> assertEquals(gbpAssertValues.get(0), gbpCurrency),
            () -> assertEquals(usdAssertValues.get(0), usdCurrency),
            () -> assertNotEquals(gbpAssertValues.get(0), usdAssertValues.get(0)),
            () -> assertEquals(gbpAssertValues.get(1), usdAssertValues.get(1)),
            () -> assertEquals(gbpAssertValues.get(2), usdAssertValues.get(2)),
            () -> assertEquals(gbpAssertValues.get(3), usdAssertValues.get(3))
        );
    }

    private ArrayList<String> getAssertValues(JasperReportSummary jasperReportSummary) {
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "3").get(10).child(0).text());

        assertValues.add(jasperApiUtils.getPartPropertyValueByName(jasperReportSummary, "Material", "f6 7"));
        assertValues.add(jasperApiUtils.getPartPropertyValueByName(jasperReportSummary, "Injection Molding", "f4 4"));
        assertValues.add(jasperApiUtils.getPartPropertyValueByName(jasperReportSummary, "All Data", "f6 4"));

        return assertValues;
    }
}
