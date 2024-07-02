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
import com.apriori.cir.ui.enums.CostMetricEnum;
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
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DesignOutlierIdentificationDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName().substring(0, 19),
        JasperCirApiPartsEnum.CASE_07.getPartName(),
        JasperCirApiPartsEnum.A257280C.getPartName()
    );
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31200")
    @TestRail(id = 31200)
    @Description("Input controls - Mass Metric - Details Report - Finish Mass")
    public void testMassMetricFinishMass() {
        genericMassMetricTest(MassMetricEnum.FINISH_MASS.getMassMetricName());
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31201")
    @TestRail(id = 31201)
    @Description("Input controls - Mass Metric - Details Report - Rough Mass")
    public void testMassMetricRoughMass() {
        genericMassMetricTest(MassMetricEnum.ROUGH_MASS.getMassMetricName());
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("6249")
    @TestRail(id = 6249)
    @Description("Min and max cost filter works - details report")
    public void testMinAndMaxCostFilter() {
        jasperApiUtils.genericTestCore(
            InputControlsEnum.APRIORI_COST_MIN.getInputControlId(),
            "0.95"
        );

        JasperReportSummary jasperReportSummaryBothCostValuesSet = jasperApiUtils.genericTestCore(
            InputControlsEnum.APRIORI_COST_MAX.getInputControlId(),
            "7797"
        );

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart()
            .getElementsContainingText("Minimum aPriori Cost:")
            .get(6).siblingElements()
            .get(5).children()
            .get(0).text()
        ).isEqualTo("0.95");

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart()
            .getElementsContainingText("Maximum aPriori Cost:")
            .get(6).siblingElements()
            .get(1).children()
            .get(0).text()
        ).isEqualTo("7,797.00");

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.P_40137441_MLDES_0002_WITHOUT_INITIAL.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.A257280C.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.P_257280C.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.VERY_LONG_NAME_01234567890123456789012345678901234567890123456789.getPartName())
        ).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("6250")
    @TestRail(id = 6250)
    @Description("Min and max mass filter works")
    public void testMinAndMaxMassFilter() {
        jasperApiUtils.genericTestCore(
            InputControlsEnum.APRIORI_MASS_MIN.getInputControlId(),
            "1"
        );

        JasperReportSummary jasperReportSummaryBothCostValuesSet = jasperApiUtils.genericTestCore(
            InputControlsEnum.APRIORI_MASS_MAX.getInputControlId(),
            "1173"
        );

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart()
            .getElementsContainingText("Minimum aPriori Mass (kg):")
            .get(12).parent().parent().parent().parent().parent().parent().siblingElements()
            .get(3).children()
            .get(0).text()
        ).isEqualTo("1.000");

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart()
            .getElementsContainingText("Maximum aPriori Mass (kg):")
            .get(6).siblingElements()
            .get(4).children()
            .get(0).text()
        ).isEqualTo("1,173.000");

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.P_40137441_MLDES_0002_WITHOUT_INITIAL.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.A257280C.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.P_257280C.getPartName())
        ).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryBothCostValuesSet.getReportHtmlPart().toString()
            .contains(JasperCirApiPartsEnum.VERY_LONG_NAME_01234567890123456789012345678901234567890123456789.getPartName())
        ).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31128")
    @TestRail(id = 31128)
    @Description("Validate report details align with aP Pro / CID - Details Report")
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

        String jasperReportSummaryString = jasperReportSummary.getReportHtmlPart().toString();
        softAssertions.assertThat(jasperReportSummaryString.contains("257280C")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryString.contains("101.64")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryString.contains("40137441.MLDES.0002")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryString.contains("1,421.78")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryString.contains("A257280C")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryString.contains("158.2")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryString.contains("CASE_07")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryString.contains("10,429.19")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryString.contains("PLASTIC MOULDED CAP THICKPART")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryString.contains("1.25")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummaryString.contains(
            "VERY LONG NAME 01234567890123456789012345678901234567890123456789")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummaryString.contains("32.08")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31131")
    @TestRail(id = 31131)
    @Description("Input controls - Date ranges (Earliest and Latest export date) - Details Report")
    public void testDateRangeInputControls() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        ResponseWrapper<UpdatedInputControlsRootItemDesignOutlierIdentification> inputControlsSetEarliestExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemDesignOutlierIdentification.class,
                false,
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
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
    @TmsLink("31137")
    @TestRail(id = 31137)
    @Description("Input controls - Rollup - Details Report")
    public void testInputControlsRollup() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemDesignOutlierIdentification> inputControlsRollup =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemDesignOutlierIdentification.class,
                false,
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetValue,
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsRollup.getResponseEntity().getInputControlState();
        String rollupAName = ExportSetEnum.ROLL_UP_A.getExportSetName();
        softAssertions.assertThat(Integer.parseInt(inputControlStateArrayList.get(9).getTotalCount())).isGreaterThanOrEqualTo(12);
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOption(rollupAName).getLabel()).isEqualTo(
            rollupAName
        );
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOption(rollupAName).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOption(rollupAName).getLabel()).isEqualTo(
            rollupAName
        );
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOption(rollupAName).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(10).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getLabel()).isEqualTo(
            RollupEnum.ROLL_UP_A.getRollupName().concat(" ")
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getSelected()).isEqualTo(true);

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Rollup Name:").get(6).siblingElements().get(2).text()
        ).startsWith(RollupEnum.ROLL_UP_A.getRollupName().substring(0, 9));

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31143")
    @TestRail(id = 31143)
    @Description("Input controls - Export Date - Details Report")
    public void testInputControlsExportDate() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemDesignOutlierIdentification> inputControlsExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemDesignOutlierIdentification.class,
                false,
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetValue,
                ""
            );

        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(9).getTotalCount())
            .isEqualTo("12");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(9)
            .getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getLabel()).isEqualTo(ExportSetEnum.ROLL_UP_A.getExportSetName());
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(9)
            .getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getSelected()).isEqualTo(Boolean.TRUE);

        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(10).getTotalCount())
            .isEqualTo("1");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(10).getOptions().get(0).getLabel())
            .isEqualTo("ROLL-UP A (Initial) ");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(10).getOptions().get(0).getSelected())
            .isEqualTo(Boolean.TRUE);

        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(11).getTotalCount())
            .isEqualTo("1");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(11).getOptions().get(0).getValue())
            .isEqualTo("2024-06-17T08:19:07");
        softAssertions.assertThat(inputControlsExportDate.getResponseEntity().getInputControlState().get(11).getOptions().get(0).getSelected())
            .isEqualTo(Boolean.TRUE);

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Costed Date:").get(6).siblingElements().get(2).text()
        ).isEqualTo("2019-07-30 04:20:20 PDT");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31189")
    @TestRail(id = 31189)
    @Description("Input controls - Export Date - Details Report - Fully Burdened Cost")
    public void testInputControlCostMetricMainReportFullyBurdenedCost() {
        genericCostMetricDesignOutlierDetailsTest(CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName());
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31192")
    @TestRail(id = 31192)
    @Description("Input controls - Export Date - Details Report - Piece Part Cost")
    public void testInputControlCostMetricMainReportPiecePartCost() {
        genericCostMetricDesignOutlierDetailsTest(CostMetricEnum.PIECE_PART_COST.getCostMetricName());
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("31220")
    @TestRail(id = 31220)
    @Description("Input controls - Minimum and Maximum aPriori Cost - Details Report")
    public void testInputControlsMinMaxAprioriCost() {
        jasperApiUtils.genericTestCore(
            InputControlsEnum.CURRENCY.getInputControlId(),
            CurrencyEnum.USD.getCurrency()
        );
        jasperApiUtils.genericTestCore(
            InputControlsEnum.APRIORI_COST_MIN.getInputControlId(),
            "1.26"
        );
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.APRIORI_COST_MAX.getInputControlId(),
            "10429.18"
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("CASE_07")).isEqualTo(false);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("PLASTIC_MOULDED_CAP_THICKPART"))
            .isEqualTo(false);

        softAssertions.assertThat(
                jasperReportSummary.getReportHtmlPart().getElementsContainingText("Minimum aPriori Cost:").get(6).siblingElements().get(5).text())
            .isEqualTo("1.26");
        softAssertions.assertThat(
                jasperReportSummary.getReportHtmlPart().getElementsContainingText("Maximum aPriori Cost:").get(6).siblingElements().get(1).text())
            .isEqualTo("10,429.18");

        softAssertions.assertAll();
    }

    private void genericMassMetricTest(String massMetricToUse) {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.MASS_METRIC.getInputControlId(),
            massMetricToUse
        );

        String aboveChartMassMetricValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(2)
            .siblingElements().get(9).child(0).text();

        softAssertions.assertThat(aboveChartMassMetricValue).isEqualTo(massMetricToUse);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "4");

        int j = 4;
        for (int i = 0; i < 3; i++) {
            softAssertions.assertThat(partNumberElements.get(j).child(0).child(0).text()).isEqualTo(mostCommonPartNames.get(i));
            j++;
        }

        softAssertions.assertAll();
    }

    private void genericCostMetricDesignOutlierDetailsTest(String costMetricToUse) {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(InputControlsEnum.COST_METRIC.getInputControlId(),
            costMetricToUse);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Cost Metric:").get(6).siblingElements().get(8).text()
        ).isEqualTo(costMetricToUse);

        // TODO - assert on changing values - I don't see anything changing in UI

        softAssertions.assertAll();
    }
}
