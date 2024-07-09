package com.apriori.cir.ui.tests.ootbreports.newreportstests.cycletimevaluetracking;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.response.ChartData;
import com.apriori.cir.api.models.response.ChartDataPoint;
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

import java.util.List;

public class CycleTimeValueTrackingReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.CYCLE_TIME_VALUE_TRACKING.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.CYCLE_TIME_VALUE_TRACKING;
    // Export Set is not relevant for this report
    private SoftAssertions softAssertions = new SoftAssertions();
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("25987")
    @TestRail(id = 25987)
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        jasperApiUtils.cycleTimeValueTrackingCurrencyTest();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13928")
    @TestRail(id = 13928)
    @Description("Report generation - User overrides")
    public void testReportGenerationUserOverrides() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyCycleTimeReport(
            CurrencyEnum.GBP.getCurrency()
        );

        List<ChartDataPoint> chartDataList = jasperReportSummary.getFirstChartData().getChartDataPoints();

        softAssertions.assertThat(chartDataList.get(0).getProperties().get(1).getValue().toString().startsWith("15402.86"))
            .isEqualTo(true);

        softAssertions.assertThat(chartDataList.get(3).getProperties().get(0).getValue().toString().startsWith("1525.97"))
            .isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2339")
    @TestRail(id = 2339)
    @Description("Validate the reports correct with multiple VPEs export set")
    public void testReportWithMultiVpeData() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyCycleTimeReport(
            CurrencyEnum.GBP.getCurrency()
        );

        List<ChartDataPoint> chartDataList = jasperReportSummary.getFirstChartData().getChartDataPoints();

        softAssertions.assertThat(chartDataList.get(1).getProperties().get(0).getValue().toString().startsWith("15416.53"))
            .isEqualTo(true);

        softAssertions.assertThat(chartDataList.get(2).getProperties().get(1).getValue().toString().startsWith("164.74"))
            .isEqualTo(true);

        softAssertions.assertAll();
    }
}
