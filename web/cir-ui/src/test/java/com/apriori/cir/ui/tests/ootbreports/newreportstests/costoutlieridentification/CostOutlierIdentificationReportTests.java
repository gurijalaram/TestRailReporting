package com.apriori.cir.ui.tests.ootbreports.newreportstests.costoutlieridentification;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawData;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class CostOutlierIdentificationReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.COST_OUTLIER_IDENTIFICATION;
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("13925")
    @TestRail(id = 13925)
    @Description("Input controls - Cost Metric - FBC")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName())
        );
    }

    @Test
    @TmsLink("29651")
    @TestRail(id = 29651)
    @Description("Input controls - Cost Metric - PPC")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName())
        );
    }

    @Test
    @TmsLink("1958")
    @TestRail(id = 1958)
    @Description("Percent difference threshold filter works - main report")
    public void testAnnualisedPotentialSavingsThresholdFilter() {
        JasperReportSummaryIncRawDataAsString jasperReportSummary = jasperApiUtils.genericTestCoreRawAsString(
            InputControlsEnum.PERCENT_DIFFERENCE_THRESHOLD.getInputControlId(),
            "100"
        );

        Document jasperReportSummaryHtml = jasperReportSummary.getReportHtmlPart();
        softAssertions.assertThat(jasperReportSummaryHtml.getElementsContainingText("Annualized Potential Savings Threshold:")
            .get(6).siblingElements()
            .get(1).children()
            .get(0).text()
        ).isEqualTo("n/a");
        softAssertions.assertThat(jasperReportSummaryHtml.getElementsContainingText("Percent Difference Threshold:")
            .get(6).siblingElements()
            .get(6).children()
            .get(0).text()
        ).isEqualTo("100.0%");

        int count = 0;
        String[] textArray = jasperReportSummary.getChartDataRawAsString().split(" ");
        for (String s : textArray) {
            if (s.contains("chartUuid")) {
                count += 1;
            }
        }
        softAssertions.assertThat(count).isEqualTo(2);
        softAssertions.assertAll();
    }
}
