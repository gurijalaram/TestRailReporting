package com.apriori.cir.ui.tests.ootbreports.newreportstests.scenariocomparison;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ScenarioComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SCENARIO_COMPARISON.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.SCENARIO_COMPARISON;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 3305)
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        SoftAssertions softAssertions = new SoftAssertions();

        String gbpCurrency = CurrencyEnum.GBP.getCurrency();
        String usdCurrency = CurrencyEnum.USD.getCurrency();

        List<String> valuesGbp = currencyTestCore(gbpCurrency);

        List<String> valuesUsd = currencyTestCore(usdCurrency);

        softAssertions.assertThat(valuesGbp.get(0)).isNotEqualTo(valuesUsd.get(0));
        softAssertions.assertThat(valuesGbp.get(0)).isEqualTo(gbpCurrency);
        softAssertions.assertThat(valuesUsd.get(0)).isEqualTo(usdCurrency);

        softAssertions.assertThat(valuesGbp.get(1)).isNotEqualTo(valuesUsd.get(1));

        softAssertions.assertAll();
    }

    private List<String> currencyTestCore(String currencyToUse) {
        JasperReportSummary jasperReportSummaryGbp = jasperApiUtils.genericTestCore("Currency", currencyToUse);

        String currencySettingValueGBP = jasperReportSummaryGbp.getReportHtmlPart()
            .getElementsContainingText("Currency").get(5).children().get(3).text();

        String currencyValueGBP = jasperReportSummaryGbp.getReportHtmlPart()
            .getElementsContainingText("FULLY BURDENED COST").get(6).parent().children().get(3).text();

        return Arrays.asList(
            currencySettingValueGBP,
            currencyValueGBP
        );
    }
}
