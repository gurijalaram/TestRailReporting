package com.ootbreports.newreportstests.general.basiccostavoidance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

import java.util.ArrayList;

public class BasicCostAvoidanceReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.BASIC_COST_AVOIDANCE.getEndpoint();
    // export set is not relevant for this report
    private static final String exportSetName = "";
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.BASIC_COST_AVOIDANCE;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(testCaseId = {"8948"})
    @Description("Input Controls - Currency Code")
    public void testCurrencyCode() {
        String gbpCurrency = CurrencyEnum.GBP.getCurrency();
        String usdCurrency = CurrencyEnum.USD.getCurrency();

        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnly(gbpCurrency);
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnly(usdCurrency);
        ArrayList<String> usdAssertValues = getAssertValues(usdJasperReportSummary);

        assertThat(gbpAssertValues.get(0), is(equalTo(gbpCurrency)));
        assertThat(usdAssertValues.get(0), is(equalTo(usdCurrency)));
        assertThat(gbpAssertValues.get(0), is(not(usdAssertValues.get(0))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }

    private ArrayList<String> getAssertValues(JasperReportSummary jasperReportSummary) {
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "4").get(5).child(0).text());
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(13).child(0).text());
        return assertValues;
    }
}
