package com.apriori.cir.ui.tests.ootbreports.newreportstests.upgradecomparison;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UpgradeComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.UPGRADE_COMPARISON.getEndpoint();
    // Export set name is not relevant for this report
    private String exportSetName = "";
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.UPGRADE_COMPARISON;
    private final SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13944")
    @TestRail(id = 13944)
    @Description("Input controls - Currency code")
    public void testCurrency() {
        ArrayList<String> gbpAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.GBP.getCurrency(), 3);

        ArrayList<String> usdAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.USD.getCurrency(), 3);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13654")
    @TestRail(id = 13654)
    @Description("Validate report accessibility")
    public void testReportAccessibility() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.GBP.getCurrency());

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "23").get(0).child(0).text()
            .equals("Upgrade Comparison")).isEqualTo(true);
        softAssertions.assertThat(!jasperReportSummary.getChartData().get(0).getChartDataPoints().isEmpty()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13656")
    @TestRail(id = 13656)
    @Description("Validate report display")
    public void testReportDisplay() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.GBP.getCurrency());

        softAssertions.assertThat(jasperReportSummary).isNotEqualTo(null);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()).isEqualTo("ALL PG");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "highcharts_parent_container")
            .toString().contains("div")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByTag("img").get(0).attributes().get("style")
            .contains("width: 280px")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(13).child(0)
            .child(0).text()).isEqualTo("2X1 CAVITY MOLD");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(228).child(0)
            .text()).isEqualTo("-33%");

        softAssertions.assertAll();
    }
}
