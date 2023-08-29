package com.ootbreports.newreportstests.cycletimevaluetracking;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.CurrencyEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

public class CycleTimeValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getEndpoint();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS;
    // Export Set is not relevant for this report
    private static final String exportSetName = "";
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 26911)
    @Description("Verify Currency Code input control is working correctly")
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
        return jasperApiUtils.genericTestCoreCurrencyAndDateOnly(currencyToUse)
            .getReportHtmlPart().getElementsByAttributeValue("colspan", "6")
            .get(indexToUse).child(0).text();
    }
}
