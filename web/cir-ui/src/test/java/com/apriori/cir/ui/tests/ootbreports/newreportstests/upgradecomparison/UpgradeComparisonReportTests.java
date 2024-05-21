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
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UpgradeComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.UPGRADE_COMPARISON.getEndpoint();
    // Export set name is not relevant for this report
    private String exportSetName = "";
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.UPGRADE_COMPARISON;
    private SoftAssertions softAssertions = new SoftAssertions();
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
    @TmsLink("2375")
    @TestRail(id = 2375)
    @Description("Verify routing change column entries are correct")
    public void verifyRoutingChangeColumnEntriesAreCorrect() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyOnlyUpgradeComparisonTests(CurrencyEnum.USD.getCurrency());

        ArrayList<Element> tableRowElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("style", "height:25px");
        softAssertions.assertThat(tableRowElements.get(0).getElementsByTag("td").get(60).text()).isEqualTo("No");
        softAssertions.assertThat(tableRowElements.get(2).getElementsByTag("td").get(60).text()).isEqualTo("Yes");

        softAssertions.assertAll();
    }
}
