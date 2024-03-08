package com.apriori.cir.ui.tests.ootbreports.newreportstests.costoutlieridentification;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CostOutlierIdentificationDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.COST_OUTLIER_IDENTIFICATION_DETAILS;
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.SM_CLEVIS_2207240161.getPartName(),
        JasperCirApiPartsEnum.P_18_1.getPartName()
    );
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("1954")
    @TestRail(id = 1954)
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("1954")
    @TestRail(id = 1954)
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TmsLink("1965")
    @TestRail(id = 1965)
    @Description("Validate details report generates")
    public void testDetailsReportGenerates() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, JasperApiEnum.COST_OUTLIER_IDENTIFICATION.getEndpoint(), CirApiEnum.COST_OUTLIER_IDENTIFICATION);
        JasperReportSummaryIncRawDataAsString jasperReportSummary = jasperApiUtils.genericTestCoreRawAsString("", "");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().isEmpty()).isEqualTo(false);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("Cost Outlier Identification")).isEqualTo(true);
        softAssertions.assertThat(jasperApiUtils.getChartUuidCount(jasperReportSummary.getChartDataRawAsString())).isEqualTo(2);

        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
        JasperReportSummary jasperReportSummary1 = jasperApiUtils.genericTestCore("", "");
        softAssertions.assertThat(jasperReportSummary1.getReportHtmlPart().toString().contains("Cost Outlier Identification Details")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary1.getReportHtmlPart().toString().contains("Initial")).isEqualTo(true);

        softAssertions.assertAll();
    }
}
