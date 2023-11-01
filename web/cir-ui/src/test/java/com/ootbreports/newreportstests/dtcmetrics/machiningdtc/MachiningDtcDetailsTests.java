package com.ootbreports.newreportstests.dtcmetrics.machiningdtc;

import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;

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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class MachiningDtcDetailsTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.MACHINING_DTC_DETAILS.getEndpoint();
    private String exportSetName = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.MACHINING_DTC_DETAILS;
    private JasperApiUtils jasperApiUtils;
    private List<String> partNames = Arrays.asList(
        JasperCirApiPartsEnum.DTCMACHINING_001.getPartName(),
        JasperCirApiPartsEnum.MACHININGDESIGN_TO_COST_INITIAL.getPartName(),
        JasperCirApiPartsEnum.PUNCH_INITIAL.getPartName()
    );

    @BeforeEach
    public void setupGenericMethods() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 7416)
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Details Report")
    public void testCostMetricInputControlPpc() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 7417)
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Details Report")
    public void testCostMetricInputControlFbc() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Cost Metric", CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 7396)
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 7397)
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        jasperApiUtils.genericDtcDetailsTest(
            partNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
