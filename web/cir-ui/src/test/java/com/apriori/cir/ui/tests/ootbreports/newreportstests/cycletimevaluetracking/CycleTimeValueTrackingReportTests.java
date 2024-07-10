package com.apriori.cir.ui.tests.ootbreports.newreportstests.cycletimevaluetracking;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.ChartDataPoint;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlOption;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemCycleTimeValueTracking;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    @Test
    @Tag(JASPER_API)
    @TmsLinks({
        @TmsLink("13700"),
        @TmsLink("2334")
    })
    @TestRail(id = {13700, 2334})
    @Description("Validate report details align with aP Pro / CID - Main Report")
    public void validateReportAlignsWithApProOrCID() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyCycleTimeReport(
            CurrencyEnum.GBP.getCurrency()
        );

        List<ChartDataPoint> chartDataPointList = jasperReportSummary.getFirstChartData().getChartDataPoints();
        softAssertions.assertThat(chartDataPointList.get(0).getProperties().get(0).getValue().toString().startsWith("64.43"))
            .isEqualTo(true);
        softAssertions.assertThat(chartDataPointList.get(0).getProperties().get(1).getValue().toString().startsWith("15402.86"))
            .isEqualTo(true);

        softAssertions.assertThat(chartDataPointList.get(1).getProperties().get(0).getValue().toString().startsWith("15416.53"))
            .isEqualTo(true);
        softAssertions.assertThat(chartDataPointList.get(1).getProperties().get(1).getValue().toString().startsWith("15378.73"))
            .isEqualTo(true);

        softAssertions.assertThat(chartDataPointList.get(2).getProperties().get(0).getValue().toString().startsWith("230.58"))
            .isEqualTo(true);
        softAssertions.assertThat(chartDataPointList.get(2).getProperties().get(1).getValue().toString().startsWith("164.74"))
            .isEqualTo(true);

        softAssertions.assertThat(chartDataPointList.get(3).getProperties().get(0).getValue().toString().startsWith("1525.97"))
            .isEqualTo(true);
        softAssertions.assertThat(chartDataPointList.get(3).getProperties().get(1).getValue().toString().startsWith("194.58"))
            .isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2332")
    @TestRail(id = 2332)
    @Description("Export date lists all available versions from selected export set rollup - Cycle Time Value Tracking Report")
    public void testExportDateInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentProjectRollup = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .getValue();

        ResponseWrapper<UpdatedInputControlsRootItemCycleTimeValueTracking> inputControlsExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCycleTimeValueTracking.class,
                false,
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                ""
            );

        ArrayList<InputControlState> inputControlStates = inputControlsExportDate.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStates.get(0).getTotalCount().equals("12")).isEqualTo(true);
        InputControlOption currencyGbpOption = inputControlStates.get(0).getOptions().get(5);
        softAssertions.assertThat(currencyGbpOption.getValue().equals("GBP")).isEqualTo(true);
        softAssertions.assertThat(currencyGbpOption.getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStates.get(1).getTotalCount().equals("1")).isEqualTo(true);
        InputControlOption rollupOption = inputControlStates.get(1).getOptions().get(0);
        softAssertions.assertThat(rollupOption.getValue().equals(currentProjectRollup)).isEqualTo(true);
        softAssertions.assertThat(rollupOption.getSelected().equals(true)).isEqualTo(true);

        softAssertions.assertThat(inputControlStates.get(2).getTotalCount().equals("2")).isEqualTo(true);
        softAssertions.assertThat(inputControlStates.get(2).getOptions().get(0).getSelected().equals(true)).isEqualTo(true);
        softAssertions.assertThat(inputControlStates.get(2).getOptions().get(1).getSelected().equals(true)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2331")
    @TestRail(id = 2331)
    @Description("Projects rollup drop list - Cycle Time Value Tracking Report")
    public void testProjectNameRollupDropdownList() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentProjectRollup = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .getValue();

        ResponseWrapper<UpdatedInputControlsRootItemCycleTimeValueTracking> inputControlsProjectName =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCycleTimeValueTracking.class,
                false,
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                ""
            );

        ArrayList<InputControlState> inputControlStates = inputControlsProjectName.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStates.get(1).getTotalCount().equals("1")).isEqualTo(true);
        InputControlOption rollupOption = inputControlStates.get(1).getOptions().get(0);
        softAssertions.assertThat(rollupOption.getValue().equals(currentProjectRollup)).isEqualTo(true);
        softAssertions.assertThat(rollupOption.getSelected().equals(true)).isEqualTo(true);

        softAssertions.assertAll();
    }
}
