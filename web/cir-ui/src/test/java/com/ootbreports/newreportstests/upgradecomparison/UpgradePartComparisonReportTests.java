package com.ootbreports.newreportstests.upgradecomparison;

import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.ArrayList;

public class UpgradePartComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.UPGRADE_PART_COMPARISON.getEndpoint();
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.UPGRADE_PART_COMPARISON;
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 13944)
    @Description("Input controls - Currency code")
    public void testCurrency() {
        ArrayList<String> gbpAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.GBP.getCurrency(), 2);

        ArrayList<String> usdAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.USD.getCurrency(), 2);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }
}
