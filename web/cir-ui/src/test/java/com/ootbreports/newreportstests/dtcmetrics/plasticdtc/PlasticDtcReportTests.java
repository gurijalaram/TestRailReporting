package com.ootbreports.newreportstests.dtcmetrics.plasticdtc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.inputcontrols.InputControlsTests;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

public class PlasticDtcReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/plasticdtc/PlasticDtcReportRequest");
    private static final String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1366"})
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Report")
    public void testCostMetricInputControlPpc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        List<String> partNames = Collections.singletonList(
            JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            true
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7403"})
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Report")
    public void testCostMetricInputControlFbc() {
        List<String> miscData = Arrays.asList(
            "Cost Metric",
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
        List<String> partNames = Collections.singletonList(
            JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            true
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7380"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Report")
    public void testMassMetricInputControlFinishMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
        List<String> partNames = Collections.singletonList(
            JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            true
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1368"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Report")
    public void testMassMetricInputControlRoughMass() {
        List<String> miscData = Arrays.asList(
            "Mass Metric",
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
        List<String> partNames = Collections.singletonList(
            JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName()
        );
        jasperApiUtils.genericDtcTest(
            miscData,
            partNames,
            true
        );
    }
}
