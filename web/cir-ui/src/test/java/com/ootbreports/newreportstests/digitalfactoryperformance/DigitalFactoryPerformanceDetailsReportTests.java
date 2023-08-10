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
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.ArrayList;
import java.util.List;

public class DigitalFactoryPerformanceDetailsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.DIGITAL_FACTORY_PERFORMANCE_DETAILS.getEndpoint();
    private static final String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.DIGITAL_FACTORY_PERFORMANCE_DETAILS;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(testCaseId = {"26941"})
    @Description("Input Controls - Currency Code - Details Report")
    public void testCurrencyCode() {
        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnly(CurrencyEnum.GBP.getCurrency());
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnly(CurrencyEnum.USD.getCurrency());
        ArrayList<String> usdAssertValues = getAssertValues(usdJasperReportSummary);

        assertThat(gbpAssertValues.get(0), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(usdAssertValues.get(0), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(gbpAssertValues.get(0), is(not(usdAssertValues.get(0))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(gbpAssertValues.get(1)))));
        assertThat(gbpAssertValues.get(2), is(not(equalTo(gbpAssertValues.get(2)))));
    }

    private ArrayList<String> getAssertValues(JasperReportSummary jasperReportSummary) {
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "6").get(5).child(0).text());
        List<Element> gbpApCostValues = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "3");
        assertValues.add(gbpApCostValues.get(7).text());
        assertValues.add(gbpApCostValues.get(27).text());
        return assertValues;
    }
}
