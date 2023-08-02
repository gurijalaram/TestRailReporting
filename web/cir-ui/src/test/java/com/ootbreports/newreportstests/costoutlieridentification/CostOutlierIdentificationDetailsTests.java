package com.ootbreports.newreportstests.costoutlieridentification;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class CostOutlierIdentificationDetailsTests extends JasperApiAuthenticationUtil {
    private final List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.SM_CLEVIS_2207240161.getPartName(),
        JasperCirApiPartsEnum.P_18_1.getPartName()
    );
    private static final String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getEndpoint();
    private static final String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"1954"})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header) - Details Report - Fully Burdened Cost")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = {"26908"})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header) - Details Report - Piece Part Cost")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }
}
