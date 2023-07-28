package com.ootbreports.newreportstests.dtcmetrics.plasticdtc;

import static com.apriori.TestSuiteType.TestSuite.REPORTS;

import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.CostMetricEnum;
import enums.JasperCirApiPartsEnum;
import enums.MassMetricEnum;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import utils.JasperApiAuthenticationUtil;

import java.util.Collections;
import java.util.List;

public class PlasticDtcReportTests extends JasperApiAuthenticationUtil {
    private final List<String> partNames = Collections.singletonList(JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName());
    private static final String reportsJsonFileName = JasperApiEnum.PLASTIC_DTC.getEndpoint();
    private static final String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {1366})
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7403})
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7380})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {1368})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
