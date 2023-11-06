package com.apriori.cir.ui.tests.ootbreports.newreportstests.digitalfactoryperformance;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DigitalFactoryPerformanceReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.DIGITAL_FACTORY_PERFORMANCE.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.DIGITAL_FACTORY_PERFORMANCE;
    private JasperApiUtils jasperApiUtils;

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

        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyDigitalFactoryPerfTests(gbpCurrency);
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyDigitalFactoryPerfTests(usdCurrency);
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
