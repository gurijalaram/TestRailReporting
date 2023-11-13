package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.basiccostavoidance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BasicCostAvoidanceReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.BASIC_COST_AVOIDANCE.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.BASIC_COST_AVOIDANCE;
    // Export set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 8948)
    @Description("Input Controls - Currency Code")
    public void testCurrencyCode() {
        String gbpCurrency = CurrencyEnum.GBP.getCurrency();
        String usdCurrency = CurrencyEnum.USD.getCurrency();

        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyLatestCostDateOnly(gbpCurrency);
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyLatestCostDateOnly(usdCurrency);
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