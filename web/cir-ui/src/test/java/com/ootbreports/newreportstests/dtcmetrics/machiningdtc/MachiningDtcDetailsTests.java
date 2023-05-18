package com.ootbreports.newreportstests.dtcmetrics.machiningdtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class MachiningDtcDetailsTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/machiningdtc/MachiningDtcDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7416"})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Details Report")
    public void testCostMetricInputControlPpc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7417"})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Details Report")
    public void testCostMetricInputControlFbc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7396"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7397"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
        List<String> partNames = Arrays.asList(
            JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
            JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST.getPartName(),
            JasperCirApiPartsEnum.PUNCH.getPartName()
        );
        jasperApiUtils.genericDtcDetailsTest(
            miscData,
            partNames
        );
    }
}
