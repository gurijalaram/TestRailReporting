package com.apriori.cir.ui.tests.ootbreports.newreportstests.upgradecomparison;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UpgradeComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.UPGRADE_COMPARISON.getEndpoint();
    // Export set name is not relevant for this report
    private String exportSetName = "";
    private CirApiEnum reportsNameForInputControls = CirApiEnum.UPGRADE_COMPARISON;
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("13952")
    @TestRail(id = 13952)
    @Description("Input controls - Currency code")
    public void testCurrency() {
        ArrayList<String> gbpAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.GBP.getCurrency(), 3);

        ArrayList<String> usdAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.USD.getCurrency(), 3);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }
}
