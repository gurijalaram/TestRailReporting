package com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.machiningdtc;

import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class MachiningDtcReportTests extends JasperApiAuthenticationUtil {
    private String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private String reportsJsonFileName = JasperApiEnum.MACHINING_DTC.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.MACHINING_DTC;
    private JasperApiUtils jasperApiUtils;
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.P_0362752_CAD_INITIAL.getPartName(),
        JasperCirApiPartsEnum.P_3572871_INITIAL.getPartName(),
        JasperCirApiPartsEnum.P_3572871_ABC.getPartName()
    );

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 3023)
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 7413)
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 3024)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(id = 7393)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
