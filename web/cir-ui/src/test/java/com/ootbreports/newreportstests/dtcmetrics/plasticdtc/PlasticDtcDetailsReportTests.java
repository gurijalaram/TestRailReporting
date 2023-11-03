package com.ootbreports.newreportstests.dtcmetrics.plasticdtc;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.CostMetricEnum;
import enums.JasperCirApiPartsEnum;
import enums.MassMetricEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Collections;
import java.util.List;

public class PlasticDtcDetailsReportTests extends JasperApiAuthenticationUtil {
    private List<String> partNames = Collections.singletonList(JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName());
    private String reportsJsonFileName = JasperApiEnum.PLASTIC_DTC_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.PLASTIC_DTC_DETAILS;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 7406)
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Details Report ")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 7407)
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Details Report ")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(id = 7381)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(id = 7382)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Details Report ")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
