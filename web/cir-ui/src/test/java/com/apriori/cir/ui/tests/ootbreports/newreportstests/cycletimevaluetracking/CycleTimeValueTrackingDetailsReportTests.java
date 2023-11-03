package com.apriori.cir.ui.tests.ootbreports.newreportstests.cycletimevaluetracking;


import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.testrail.TestRail;

import com.google.common.base.Stopwatch;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class CycleTimeValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS;
    private Logger logger = LoggerFactory.getLogger(CycleTimeValueTrackingDetailsReportTests.class);
    // Export Set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

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
        return generateReportCurrencyProjectRollupOnly(currencyToUse, RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .getReportHtmlPart().getElementsByAttributeValue("colspan", "6")
            .get(indexToUse).child(0).text();
    }

    private JasperReportSummary generateReportCurrencyProjectRollupOnly(String currencyToSet, String rollupToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(JasperApiAuthenticationUtil.jSessionId);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyToSet);
        jasperApiUtils.setReportParameterByName("exportDate", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String projectRollupValue = inputControls.getProjectRollup().getOption(rollupToSet).getValue();
        jasperApiUtils.setReportParameterByName("projectRollup", projectRollupValue);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }
}
