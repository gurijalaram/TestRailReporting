package com.ootbreports.newreportstests.cycletimevaluetracking;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class CycleTimeValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getEndpoint();
    // Export Set is not relevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"26911"})
    @Description("Verify Currency Code input control is working correctly - Details Report")
    public void testCurrencyCode() {
        SoftAssertions softAssertions = new SoftAssertions();

        for (int i = 6; i < 10; i += 3) {
            String gbpCycleTimeTotalValue = getCycleTimeTotalValue(CurrencyEnum.GBP.getCurrency(), i);

            String usdCycleTimeTotalValue = getCycleTimeTotalValue(CurrencyEnum.USD.getCurrency(), i);

            softAssertions.assertThat(gbpCycleTimeTotalValue).isEqualTo(usdCycleTimeTotalValue);
        }

        softAssertions.assertAll();
    }

    private String getCycleTimeTotalValue(String currencyToUse, int indexToUse) {
        return jasperApiUtils.genericTestCoreCurrencyOnly(currencyToUse)
            .getReportHtmlPart().getElementsByAttributeValue("colspan", "6").get(indexToUse).child(0).text();
    }
}
