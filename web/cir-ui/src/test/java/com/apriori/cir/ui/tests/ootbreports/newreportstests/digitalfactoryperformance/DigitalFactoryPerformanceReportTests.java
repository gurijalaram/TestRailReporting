package com.apriori.cir.ui.tests.ootbreports.newreportstests.digitalfactoryperformance;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DigitalFactoryPerformanceReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.DIGITAL_FACTORY_PERFORMANCE.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.DIGITAL_FACTORY_PERFORMANCE;
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("28476")
    @TestRail(id = 28476)
    @Description("Input Controls - Currency Code - Main Report")
    public void testCurrencyCode() {
        String gbpCurrency = CurrencyEnum.GBP.getCurrency();
        String usdCurrency = CurrencyEnum.USD.getCurrency();

        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyDigitalFactoryPerfTests(gbpCurrency);
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyDigitalFactoryPerfTests(usdCurrency);
        ArrayList<String> usdAssertValues = getAssertValues(usdJasperReportSummary);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(gbpAssertValues.get(0)).isEqualTo(gbpCurrency);
        softAssertions.assertThat(usdAssertValues.get(0)).isEqualTo(usdCurrency);
        softAssertions.assertThat(gbpAssertValues.get(0)).isNotEqualTo(usdAssertValues.get(0));
        softAssertions.assertThat(gbpAssertValues.get(1)).startsWith(usdAssertValues.get(1).substring(0, 4));
        softAssertions.assertThat(usdAssertValues.get(1)).startsWith(gbpAssertValues.get(1).substring(0, 4));
        softAssertions.assertThat(usdAssertValues.get(2)).isEqualTo(gbpAssertValues.get(2));
        softAssertions.assertThat(usdAssertValues.get(3)).isEqualTo(gbpAssertValues.get(3));
        softAssertions.assertAll();
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
