package com.ootbreports.newreportstests.upgradecomparison;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.CurrencyEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.ArrayList;

public class UpgradeComparisonReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.UPGRADE_COMPARISON.getEndpoint();
    // Export set name is not relevant for this report
    private static final String exportSetName = "";
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.UPGRADE_COMPARISON;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 13952)
    @Description("Input controls - Currency code")
    public void testCurrency() {
        ArrayList<String> gbpAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.GBP.getCurrency(), 3);

        ArrayList<String> usdAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.USD.getCurrency(), 3);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }
}
