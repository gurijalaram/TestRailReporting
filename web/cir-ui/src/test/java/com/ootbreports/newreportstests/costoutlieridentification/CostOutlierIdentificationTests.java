package com.ootbreports.newreportstests.costoutlieridentification;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.CostMetricEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;

public class CostOutlierIdentificationTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION.getEndpoint();
    private static final String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.COST_OUTLIER_IDENTIFICATION;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 26909)
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName())
        );
    }

    @Test
    @TestRail(id = 26910)
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName())
        );
    }
}
