package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.machiningdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
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
    private CirApiEnum reportsNameForInputControls = CirApiEnum.MACHINING_DTC;
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
    @TmsLink("3026")
    @TestRail(id = 7393)
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        jasperApiUtils.genericDtcCurrencyTest(
            JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName(),
            true,
            false
        );
    }

    /*
    Process group tests (3 needed)
    Process groups: Stock Machining and 2-Model Machining
    One test for Stock Machining Only
    One test for 2-Model Machining Only
    One test for both process groups
     */

    @Test
    @Tag(REPORTS)
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
    @Tag(REPORTS)
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
    @Tag(REPORTS)
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
}
