package com.ootbreports.newreportstests.scenariocomparison;

import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

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
    @Tag(REPORTS_API)
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
