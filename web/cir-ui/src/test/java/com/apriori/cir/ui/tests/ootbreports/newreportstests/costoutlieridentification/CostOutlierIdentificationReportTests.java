package com.apriori.cir.ui.tests.ootbreports.newreportstests.costoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class CostOutlierIdentificationReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.COST_OUTLIER_IDENTIFICATION;
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("13925")
    @TestRail(id = 13925)
    @Description("Input controls - Cost Metric - FBC")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName())
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("29651")
    @TestRail(id = 29651)
    @Description("Input controls - Cost Metric - PPC")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName())
        );
    }
}
