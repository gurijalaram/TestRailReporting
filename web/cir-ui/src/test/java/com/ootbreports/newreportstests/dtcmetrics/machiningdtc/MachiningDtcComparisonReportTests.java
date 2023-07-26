package com.ootbreports.newreportstests.dtcmetrics.machiningdtc;

import com.apriori.testrail.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class MachiningDtcComparisonReportTests extends JasperApiAuthenticationUtil {
    private final List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.DTCMACHINING_001_Toleranced.getPartName(),
        JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
        JasperCirApiPartsEnum.PUNCH_INITIAL.getPartName()
    );
    private static final String reportsJsonFileName = JasperApiEnum.MACHINING_DTC_COMPARISON.getEndpoint();
    private static final String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7414})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7415})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7394})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7395})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Comparison Report ")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
