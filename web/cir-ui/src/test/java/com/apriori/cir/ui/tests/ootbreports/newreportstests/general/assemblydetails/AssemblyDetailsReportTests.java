package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblydetails;

import java.util.List;
import java.util.stream.Collectors;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
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

public class AssemblyDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.ASSEMBLY_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.ASSEMBLY_DETAILS;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("1922")
    @TestRail(id = 1922)
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        jasperApiUtils.genericDtcCurrencyTest(
            ExportSetEnum.TOP_LEVEL.getExportSetName(),
            false,
            false
        );
    }

    @Test
    @TmsLink("1928")
    @TestRail(id = 1928)
    @Description("Validate report content aligns to aP desktop values (many levels inside BOM)")
    public void testLevelsInsideBOM() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("", "");

        List<Element> bomElementsRefined = jasperReportSummary.getReportHtmlPart().getElementsContainingText("..")
            .stream().filter(element -> element.toString().startsWith("<span")).collect(Collectors.toList());

        softAssertions.assertThat(jasperReportSummary).isNotEqualTo(null);
        softAssertions.assertThat(bomElementsRefined).isNotEqualTo(null);
        for (int i = 0; i < 7; i++) {
            softAssertions.assertThat(bomElementsRefined.get(i).text()).isEqualTo("..1");
        }

        for (int j = 7; j < 10; j++) {
            softAssertions.assertThat(bomElementsRefined.get(j).text()).isEqualTo("....2");
        }

        softAssertions.assertAll();
        // can we call cid via api to make this do exactly what TR says it should do? Talk to Ciene.
    }

    @Test
    @TmsLink("1933")
    @TestRail(id = 1933)
    @Description("Verify component subassembly report details")
    public void testComponentSubAssemblyReportDetails() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("assemblySelect", "SUB-ASSEMBLY (Initial) [assembly] ");

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("459.80")).isEqualTo(true);

        jasperApiUtils = new JasperApiUtils(jSessionId, ExportSetEnum.SUB_SUB_ASM.getExportSetName(), JasperApiEnum.COMPONENT_COST.getEndpoint(), CirApiEnum.COMPONENT_COST);
        jasperReportSummary = jasperApiUtils.genericTestCore("componentSelect", "10");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().isEmpty()).isEqualTo(false);

        String varianceValue = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Variance:").get(5).children().get(2).text();
        String percentOfTargetValue = jasperReportSummary.getReportHtmlPart().getElementsContainingText("% of Target:").get(5).children().get(2).text();
        String lifetimeCostValue = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Lifetime Cost:").get(5).children().get(2).text();
        String lifetimeProjectedCostDiffValue = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Lifetime Projected Cost Difference:").get(5).children().get(2).text();

        softAssertions.assertThat(varianceValue).isEqualTo("-");
        softAssertions.assertThat(percentOfTargetValue).isEqualTo("-");
        softAssertions.assertThat(lifetimeCostValue).isEqualTo("849,532.96");
        softAssertions.assertThat(lifetimeProjectedCostDiffValue).isEqualTo("-");

        softAssertions.assertAll();
    }

    @Test
    @TmsLink("3231")
    @TmsLink("1929")
    @TestRail(id = {3231, 1929})
    @Description("Verify sub total calculations for Sub Assembly")
    public void testSubTotalCalculationsSubAssembly() {

    }
}
