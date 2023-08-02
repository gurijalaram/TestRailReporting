package com.ootbreports.newreportstests.costoutlieridentification;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;

public class CostOutlierIdentificationTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION.getEndpoint();
    private static final String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"26909"})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header) - Main Report - Fully Burdened Cost")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName())
        );
    }

    @Test
    @TestRail(testCaseId = {"26910"})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header) - Main Report - Piece Part Cost")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierTest(
            Arrays.asList("Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName())
        );
    }
}
