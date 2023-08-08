package com.ootbreports.newreportstests.dtcmetrics.machiningdtc;

import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.utils.TestRail;
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

public class MachiningDtcDetailsTests extends JasperApiAuthenticationUtil {
    private final List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
        JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
        JasperCirApiPartsEnum.PUNCH_INITIAL.getPartName()
    );
    private static final String reportsJsonFileName = JasperApiEnum.MACHINING_DTC_DETAILS.getEndpoint();
    private static final String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.MACHINING_DTC_DETAILS;
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7416"})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Details Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7417"})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Details Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7396"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7397"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
