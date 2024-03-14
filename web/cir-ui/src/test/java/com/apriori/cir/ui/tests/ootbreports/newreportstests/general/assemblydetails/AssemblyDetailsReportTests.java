package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblydetails;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS_API;

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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Tag(REPORTS_API)
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
    @Tag(REPORTS_API)
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
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("1933")
    @TestRail(id = 1933)
    @Description("Verify component subassembly report details")
    public void testComponentSubAssemblyReportDetails() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            "assemblySelect",
            "SUB-ASSEMBLY (Initial) [assembly] "
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("459.80")).isEqualTo(true);

        jasperApiUtils = new JasperApiUtils(
            jSessionId,
            ExportSetEnum.SUB_SUB_ASM.getExportSetName(),
            JasperApiEnum.COMPONENT_COST.getEndpoint(),
            CirApiEnum.COMPONENT_COST
        );
        jasperReportSummary = jasperApiUtils.genericTestCore("componentSelect", "10");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().isEmpty()).isEqualTo(false);

        String varianceValue = jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Variance:").get(5).children().get(2).text();
        String percentOfTargetValue = jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("% of Target:").get(5).children().get(2).text();
        String lifetimeCostValue = jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Lifetime Cost:").get(5).children().get(2).text();
        String lifetimeProjectedCostDiffValue = jasperReportSummary.getReportHtmlPart()
            .getElementsContainingText("Lifetime Projected Cost Difference:").get(5).children().get(2).text();

        softAssertions.assertThat(varianceValue).isEqualTo("-");
        softAssertions.assertThat(percentOfTargetValue).isEqualTo("-");
        softAssertions.assertThat(lifetimeCostValue).isEqualTo("849,532.96");
        softAssertions.assertThat(lifetimeProjectedCostDiffValue).isEqualTo("-");

        softAssertions.assertAll();
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("3231")
    @TmsLink("1929")
    @TestRail(id = {3231, 1929})
    @Description("Verify sub total calculations for Sub Assembly")
    public void testSubTotalCalculationsSubAssembly() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("", "");

        ArrayList<BigDecimal> compSubTotalValues = jasperApiUtils.getSubTotalAndTotalValuesAssemblyDetails(
            jasperReportSummary,
            "Component Subtotal",
            Arrays.asList(13, 16, 19, 22)
        );

        ArrayList<BigDecimal> assemblyProcessSubTotalValues = jasperApiUtils.getSubTotalAndTotalValuesAssemblyDetails(
            jasperReportSummary,
            "Assembly Processes Subtotal",
            Arrays.asList(23, 26, 29, 32)
        );

        ArrayList<BigDecimal> expectedTotals = new ArrayList<>();
        expectedTotals.add(compSubTotalValues.get(0).add(assemblyProcessSubTotalValues.get(0)));
        expectedTotals.add(compSubTotalValues.get(1).add(assemblyProcessSubTotalValues.get(1)));
        expectedTotals.add(compSubTotalValues.get(2).add(assemblyProcessSubTotalValues.get(2)));
        expectedTotals.add(compSubTotalValues.get(3).add(assemblyProcessSubTotalValues.get(3)));

        ArrayList<BigDecimal> grandTotalSubTotalValues = jasperApiUtils.getSubTotalAndTotalValuesAssemblyDetails(
            jasperReportSummary,
            "GRAND TOTAL",
            Arrays.asList(23, 26, 29, 32)
        );

        softAssertions.assertThat(
            jasperApiUtils.areValuesAlmostEqual(expectedTotals.get(0),
            grandTotalSubTotalValues.get(0))).isEqualTo(true);

        softAssertions.assertThat(
            jasperApiUtils.areValuesAlmostEqual(expectedTotals.get(1),
            grandTotalSubTotalValues.get(1))).isEqualTo(true);

        softAssertions.assertThat(
            jasperApiUtils.areValuesAlmostEqual(expectedTotals.get(2),
            grandTotalSubTotalValues.get(2))).isEqualTo(true);

        softAssertions.assertThat(
            jasperApiUtils.areValuesAlmostEqual(expectedTotals.get(3),
            grandTotalSubTotalValues.get(3))).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("3067")
    @TmsLink("1929")
    @TestRail(id = {3067, 1929})
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("", "");

        List<BigDecimal> levelOneValues = jasperApiUtils.getAssemblyDetailsValuesLevelOne(jasperReportSummary);

        BigDecimal expectedAssemblyProcessesSubTotal = jasperApiUtils.convertStringToBigDecimalValue(
            jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("style", "height:15px")
                .get(2).children().get(24).children().get(0).text()
        );

        List<BigDecimal> totalValues = jasperApiUtils.getAssemblyDetailsValuesTotals(jasperReportSummary);

        BigDecimal expectedComponentSubtotal = levelOneValues.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedGrandTotal = expectedComponentSubtotal.add(expectedAssemblyProcessesSubTotal);

        softAssertions.assertThat(
            jasperApiUtils.areValuesAlmostEqual(expectedComponentSubtotal, totalValues.get(0)))
            .isEqualTo(true);

        softAssertions.assertThat(
            jasperApiUtils.areValuesAlmostEqual(expectedAssemblyProcessesSubTotal, totalValues.get(1)))
            .isEqualTo(true);

        softAssertions.assertThat(
            jasperApiUtils.areValuesAlmostEqual(expectedGrandTotal, totalValues.get(2)))
            .isEqualTo(true);

        softAssertions.assertAll();
    }
}
