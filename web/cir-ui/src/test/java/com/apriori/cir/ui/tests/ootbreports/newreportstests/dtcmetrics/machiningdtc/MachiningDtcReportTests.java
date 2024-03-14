package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.machiningdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MachiningDtcReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.MACHINING_DTC.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.MACHINING_DTC;
    private JasperApiUtils jasperApiUtils;
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName(),
        JasperCirApiPartsEnum.P_3572871_INITIAL.getPartName(),
        JasperCirApiPartsEnum.P_3572871_ABC.getPartName()
    );

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3023")
    @TestRail(id = 3023)
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7413")
    @TestRail(id = 7413)
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3024")
    @TestRail(id = 3024)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7393")
    @TestRail(id = 7393)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3026")
    @TestRail(id = 3026)
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName(),
            true,
            false
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7452")
    @TestRail(id = {7452})
    @Description("Verify process group input control functionality - Stock Machining - Machining DTC Report")
    public void testProcessGroupStockMachiningOnly() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName(),
            JasperCirApiPartsEnum.P_3572871_INITIAL.getPartName()
        );
        jasperApiUtils.genericProcessGroupDtcTest(
            partNames,
            "Process Group", ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7451")
    @TestRail(id = {7451})
    @Description("Verify process group input control functionality - 2 Model Machining - Machining DTC Report")
    public void testProcessGroupTwoModelMachiningOnly() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Process Group", ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup());
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(jasperReportSummary.getChartData().isEmpty()).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().concat("No data available")).isEqualTo(true);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7456")
    @TestRail(id = {7456})
    @Description("Verify process group input control functionality - 2 Model and Stock Machining - Machining DTC Report")
    public void testProcessGroupTwoModelAndStockMachining() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName(),
            JasperCirApiPartsEnum.P_3572871_INITIAL.getPartName()
        );

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("", "");

        SoftAssertions softAssertions = new SoftAssertions();
        for (String partName : partNames) {
            softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().toString().contains(partName)).isEqualTo(true);
        }

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup());
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7460")
    @TestRail(id = {7460})
    @Description("Verify DTC Score Input Control - Low Selection - Machining DTC Report")
    public void testDtcScoreLow() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.PMI_FLATNESS_CREO.getPartName(),
            JasperCirApiPartsEnum.PMI_SYMMETRY_CREO.getPartName(),
            JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7498")
    @TestRail(id = {7498})
    @Description("Verify DTC Score Input Control - Medium Selection - Machining DTC Report")
    public void testDtcScoreMedium() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.MULTIPLE_TURNING_AXIS.getPartName(),
            JasperCirApiPartsEnum.P_3572871_ABC.getPartName(),
            JasperCirApiPartsEnum.P_3572871_INITIAL.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7501")
    @TestRail(id = {7501})
    @Description("Verify DTC Score Input Control - High Selection - Machining DTC Report")
    public void testDtcScoreHigh() {
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.PARTBODY_1.getPartName(),
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericDtcScoreTest(
            true,
            partNames,
            "DTC Score", DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3027")
    @TestRail(id = {3027})
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        jasperApiUtils.genericMinAnnualSpendDtcTest(
            18
        );
    }
}
