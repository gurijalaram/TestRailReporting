package com.ootbreports.newreportstests.designoutlieridentification;

import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.JasperCirApiPartsEnum;
import enums.MassMetricEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.Arrays;
import java.util.List;

public class DesignOutlierIdentificationDetailsReportTests extends JasperApiAuthenticationUtil {
    private final List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_257280C.getPartName(),
        JasperCirApiPartsEnum.P_40137441_MLDES_0002_WITHOUT_INITIAL.getPartName()
    );
    private static final String reportsJsonFileName = JasperApiEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getEndpoint();
    private static final String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(id = {7387})
    @Description("Verify mass metric - finish mass - Design Outlier Identification Details Report")
    public void testMassMetricFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(id = {7386})
    @Description("Verify mass metric - rough mass - Design Outlier Identification Details Report")
    public void testMassMetricRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            "Mass Metric", MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
