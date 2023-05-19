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
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class MachiningDtcComparisonReportTests extends JasperApiAuthenticationUtil {
    private final List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.DTCMACHINING_001_Toleranced.getPartName(),
        JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
        JasperCirApiPartsEnum.PUNCH_INITIAL.getPartName()
    );
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/machiningdtc/MachiningDtcComparisonReportRequest");
    private static final String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"7414"})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7415"})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7394"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7395"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Comparison Report ")
    public void testMassMetricInputControlRoughMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            false
        );
    }
}
