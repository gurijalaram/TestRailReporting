package com.apriori.cir.ui.tests.ootbreports.newreportstests.designoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.ChartDataPoint;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemDesignOutlierIdentification;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DesignOutlierIdentificationReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.DESIGN_OUTLIER_IDENTIFICATION.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.DESIGN_OUTLIER_IDENTIFICATION;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private SoftAssertions softAssertions = new SoftAssertions();
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_257280C.getPartName(),
        JasperCirApiPartsEnum.P_40137441_MLDES_0002_WITHOUT_INITIAL.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1997")
    @TestRail(id = 1997)
    @Description("Verify mass metric - finish mass - Design Outlier Identification Report")
    public void testMassMetricFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7385")
    @TestRail(id = 7385)
    @Description("Verify mass metric - rough mass - Design Outlier Identification Report")
    public void testMassMetricRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13634")
    @TestRail(id = 13634)
    @Description("Validate report details align with aP Pro / CID - Main Report")
    public void testReportDetailsAlignWithApProAndCid() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        String currentExportSet = jasperReportUtil.getInputControls(reportsNameForInputControls)
            .getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.USD.getCurrency());

        JasperReportSummary jasperReportSummary = jasperReportUtil
            .generateJasperReportSummary(jasperApiUtils.getReportRequest());

        List<ChartDataPoint> listOfChartDataPoints = jasperReportSummary.getFirstChartData().getChartDataPoints();
        softAssertions.assertThat(listOfChartDataPoints.get(0).getPartName()).isEqualTo("257280C");
        softAssertions.assertThat(listOfChartDataPoints.get(0).getFullyBurdenedCost()).isEqualTo("101.64");

        softAssertions.assertThat(listOfChartDataPoints.get(1).getPartName()).isEqualTo("40137441.MLDES.0002");
        softAssertions.assertThat(listOfChartDataPoints.get(1).getFullyBurdenedCost()).isEqualTo("1421.78");

        softAssertions.assertThat(listOfChartDataPoints.get(2).getPartName()).isEqualTo("A257280C");
        softAssertions.assertThat(listOfChartDataPoints.get(2).getFullyBurdenedCost()).isEqualTo("158.2");

        softAssertions.assertThat(listOfChartDataPoints.get(3).getPartName()).isEqualTo("CASE_07");
        softAssertions.assertThat(listOfChartDataPoints.get(3).getFullyBurdenedCost()).isEqualTo("10429.19");

        softAssertions.assertThat(listOfChartDataPoints.get(4).getPartName()).isEqualTo("PLASTIC MOULDED CAP THICKPART");
        softAssertions.assertThat(listOfChartDataPoints.get(4).getFullyBurdenedCost()).isEqualTo("1.25");

        softAssertions.assertThat(listOfChartDataPoints.get(5).getPartName()
        ).isEqualTo("VERY LONG NAME 01234567890123456789012345678901234567890123456789");
        softAssertions.assertThat(listOfChartDataPoints.get(5).getFullyBurdenedCost()).isEqualTo("32.08");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13934")
    @TestRail(id = 13934)
    @Description("Input controls - Date ranges (Earliest and Latest export date) - Main Report")
    public void testDateRangeInputControls() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        ResponseWrapper<UpdatedInputControlsRootItemDesignOutlierIdentification> inputControlsSetEarliestExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemDesignOutlierIdentification.class,
                false,
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId(),
                currentDateTime,
                ""
            );

        softAssertions.assertThat(inputControlsSetEarliestExportDate.getResponseEntity().getInputControlState().get(10).getTotalCount())
            .isEqualTo("0");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13935")
    @TestRail(id = 13935)
    @Description("Input controls - Rollup - Main Report")
    public void testInputControlsRollup() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemDesignOutlierIdentification> inputControlsRollup =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemDesignOutlierIdentification.class,
                false,
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetValue,
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsRollup.getResponseEntity().getInputControlState();
        String rollupAName = ExportSetEnum.ROLL_UP_A.getExportSetName();
        softAssertions.assertThat(Integer.parseInt(inputControlStateArrayList.get(10).getTotalCount())).isGreaterThanOrEqualTo(12);
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(rollupAName).getLabel()).isEqualTo(
            rollupAName
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(rollupAName).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(rollupAName).getLabel()).isEqualTo(
            rollupAName
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOption(rollupAName).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(11).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(11).getOptions().get(0).getLabel()).isEqualTo(
            RollupEnum.ROLL_UP_A.getRollupName().concat(" ")
        );
        softAssertions.assertThat(inputControlStateArrayList.get(11).getOptions().get(0).getSelected()).isEqualTo(true);

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()
        ).startsWith(RollupEnum.ROLL_UP_A.getRollupName().substring(0, 9));

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13935")
    @TestRail(id = 13935)
    @Description("Input controls - Export Date - Main Report")
    public void testInputControlsExportDate() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemDesignOutlierIdentification> inputControlsExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemDesignOutlierIdentification.class,
                false,
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetValue,
                ""
            );

        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(10).getTotalCount())
            .isEqualTo("12");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(10)
            .getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getLabel()).isEqualTo(ExportSetEnum.ROLL_UP_A.getExportSetName());
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(10)
            .getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getSelected()).isEqualTo(Boolean.TRUE);

        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(11).getTotalCount())
            .isEqualTo("1");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(11).getOptions().get(0).getLabel())
            .isEqualTo("ROLL-UP A (Initial) ");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(11).getOptions().get(0).getSelected())
            .isEqualTo(Boolean.TRUE);

        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(12).getTotalCount())
            .isEqualTo("1");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(12).getOptions().get(0).getValue())
            .isEqualTo("2024-06-17T08:19:07");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(12).getOptions().get(0).getSelected())
            .isEqualTo(Boolean.TRUE);

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Export Date:").get(6).siblingElements().get(2).text()
        ).isEqualTo("2024-06-17 08:19:07 PDT");

        softAssertions.assertAll();
    }
}
