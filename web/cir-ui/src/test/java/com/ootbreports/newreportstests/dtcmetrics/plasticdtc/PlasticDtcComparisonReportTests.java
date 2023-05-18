package com.ootbreports.newreportstests.dtcmetrics.plasticdtc;

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
import java.util.Collections;
import java.util.List;

public class PlasticDtcComparisonReportTests extends JasperApiAuthenticationUtil {

    private final List<String> partNames = Collections.singletonList(JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName());
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/plasticdtc/PlasticDtcComparisonReportRequest");
    private static final String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7404"})
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Comparison Report")
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
    @TestRail(testCaseId = {"7405"})
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Comparison Report")
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
    @TestRail(testCaseId = {"7383"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Comparison Report")
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
    @TestRail(testCaseId = {"7384"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Comparison Report")
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
