package com.ootbreports.newreportstests.costoutlieridentification;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.CostMetricEnum;
import enums.JasperCirApiPartsEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class CostOutlierIdentificationDetailsTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.COST_OUTLIER_IDENTIFICATION_DETAILS;
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.SM_CLEVIS_2207240161.getPartName(),
        JasperCirApiPartsEnum.P_18_1.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = 1954)
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricFbcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = 1954)
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricPpcFunctionality() {
        jasperApiUtils.genericCostMetricCostOutlierDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }
}
