package com.ootbreports.newreportstests.dtcmetrics.machiningdtc;

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

import java.util.Arrays;
import java.util.List;

public class MachiningDtcReportTests extends JasperApiAuthenticationUtil {
    private final List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName(),
        JasperCirApiPartsEnum.P_3572871_INITIAL.getPartName(),
        JasperCirApiPartsEnum.P_3572871_ABC.getPartName()
    );
    private static final String reportsJsonFileName = JasperApiEnum.MACHINING_DTC.getEndpoint();
    private static final String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3023})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7413})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3024})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7393})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
