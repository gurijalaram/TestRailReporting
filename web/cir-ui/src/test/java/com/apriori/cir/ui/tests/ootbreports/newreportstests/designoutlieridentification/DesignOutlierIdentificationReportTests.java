package com.apriori.cir.ui.tests.ootbreports.newreportstests.designoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class DesignOutlierIdentificationReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.DESIGN_OUTLIER_IDENTIFICATION.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.DESIGN_OUTLIER_IDENTIFICATION;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_257280C.getPartName(),
        JasperCirApiPartsEnum.P_40137441_MLDES_0002_WITHOUT_INITIAL.getPartName()
    );
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1997")
    @TestRail(id = 1997)
    @Description("Verify mass metric - finish mass - Design Outlier Identification Report")
    public void testMassMetricFinishMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7385")
    @TestRail(id = 7385)
    @Description("Verify mass metric - rough mass - Design Outlier Identification Report")
    public void testMassMetricRoughMass() {
        jasperApiUtils.genericDtcTest(
            mostCommonPartNames,
            InputControlsEnum.MASS_METRIC.getInputControlId(), MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }
}
