package com.ootbreports.newreportstests.digitalfactoryperformance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.ArrayList;

public class DigitalFactoryPerformanceReportTests extends JasperApiAuthenticationUtil {
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
        String gbpCurrency = CurrencyEnum.GBP.getCurrency();
        String usdCurrency = CurrencyEnum.USD.getCurrency();

        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnly(gbpCurrency);
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnly(usdCurrency);
        ArrayList<String> usdAssertValues = getAssertValues(usdJasperReportSummary);

        assertThat(gbpAssertValues.get(0), is(equalTo(gbpCurrency)));
        assertThat(usdAssertValues.get(0), is(equalTo(usdCurrency)));
        assertThat(gbpAssertValues.get(0), is(not(usdAssertValues.get(0))));
        assertThat(gbpAssertValues.get(1), is(equalTo(usdAssertValues.get(1))));
        assertThat(gbpAssertValues.get(2), is(equalTo(usdAssertValues.get(2))));
        assertThat(gbpAssertValues.get(3), is(equalTo(usdAssertValues.get(3))));
    }

    private ArrayList<String> getAssertValues(JasperReportSummary jasperReportSummary) {
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "3").get(10).child(0).text());
        int j = 5;
        for (int i = 0; i < 3; i++) {
            assertValues.add(jasperReportSummary.getFirstChartData().getChartDataPoints().get(i).getProperties().get(j).getValue().toString());
            j = j == 5 ? 9 : 11;
        }
        return assertValues;
    }
}
