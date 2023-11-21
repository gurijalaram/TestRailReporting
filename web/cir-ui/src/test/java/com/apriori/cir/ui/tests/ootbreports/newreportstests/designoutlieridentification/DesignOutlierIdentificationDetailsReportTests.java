package com.apriori.cir.ui.tests.ootbreports.newreportstests.designoutlieridentification;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class DesignOutlierIdentificationDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private List<String> mostCommonPartNames = Arrays.asList(
        JasperCirApiPartsEnum.P_40137441_MLDES_0002.getPartName().substring(0, 19),
        JasperCirApiPartsEnum.CASE_07.getPartName(),
        JasperCirApiPartsEnum.A257280C.getPartName()
    );
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("7387")
    @TestRail(id = 7387)
    @Description("Verify mass metric - finish mass - Design Outlier Identification Details Report")
    public void testMassMetricFinishMass() {
        genericMassMetricTest(MassMetricEnum.FINISH_MASS.getMassMetricName());
    }

    @Test
    @TmsLink("7386")
    @TestRail(id = 7386)
    @Description("Verify mass metric - rough mass - Design Outlier Identification Details Report")
    public void testMassMetricRoughMass() {
        genericMassMetricTest(MassMetricEnum.ROUGH_MASS.getMassMetricName());
    }

    private void genericMassMetricTest(String massMetricToUse) {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("Mass Metric", massMetricToUse);

        String aboveChartMassMetricValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5").get(2)
            .siblingElements().get(9).child(0).text();

        softAssertions.assertThat(aboveChartMassMetricValue).isEqualTo(massMetricToUse);

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "4");

        int j = 4;
        for (int i = 0; i < 3; i++) {
            softAssertions.assertThat(partNumberElements.get(j).child(0).child(0).text()).isEqualTo(mostCommonPartNames.get(i));
            j++;
        }

        softAssertions.assertAll();
    }
}
