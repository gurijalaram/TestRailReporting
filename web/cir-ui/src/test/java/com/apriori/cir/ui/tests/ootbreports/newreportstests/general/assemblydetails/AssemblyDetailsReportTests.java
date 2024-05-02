package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblydetails;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlOption;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemAssemblyDetails;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemScenarioComparison;
import com.apriori.cir.ui.enums.AssemblySetEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblyDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.ASSEMBLY_DETAILS.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.ASSEMBLY_DETAILS;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1922")
    @TmsLink("14003")
    @TestRail(id = {1922, 14003})
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        jasperApiUtils.genericDtcCurrencyTest(
            ExportSetEnum.TOP_LEVEL.getExportSetName(),
            false,
            false
        );
    }

    @Test
    @Tag(JASPER_API)
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
    @Tag(JASPER_API)
    @TmsLink("1933")
    @TestRail(id = 1933)
    @Description("Verify component subassembly report details")
    public void testComponentSubAssemblyReportDetails() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.ASSEMBLY_SELECT.getInputControlId(),
            "SUB-ASSEMBLY (Initial) [assembly] "
        );

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("459.80")).isEqualTo(true);

        jasperApiUtils = new JasperApiUtils(
            jSessionId,
            ExportSetEnum.SUB_SUB_ASM.getExportSetName(),
            JasperApiEnum.COMPONENT_COST.getEndpoint(),
            JasperApiInputControlsPathEnum.COMPONENT_COST
        );
        jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.COMPONENT_SELECT.getInputControlId(),
            "10"
        );
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
    @Tag(JASPER_API)
    @TmsLinks({
        @TmsLink("3231"),
        @TmsLink("1929")
    })
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
    @Tag(JASPER_API)
    @TmsLink("3205")
    @TestRail(id = 3205)
    @Description("Verify currency code reversion")
    public void testCurrencyCodeReversion() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String assemblyValueToSelect = inputControls.getAssemblySelect()
            .getOption(AssemblySetEnum.TOP_LEVEL.getAssemblySetName().concat(" [assembly] ")).getValue();

        JasperReportSummary jasperReportSummaryGbp = jasperApiUtils.genericTestCore(
            InputControlsEnum.ASSEMBLY_SELECT.getInputControlId(),
            assemblyValueToSelect
        );

        performGbpAssertions(jasperReportSummaryGbp);

        JasperReportSummary jasperReportSummaryUSD = jasperApiUtils.genericTestCore(
            InputControlsEnum.CURRENCY.getInputControlId(),
            CurrencyEnum.USD.getCurrency()
        );

        softAssertions.assertThat(jasperReportSummaryUSD.getReportHtmlPart().getElementsContainingText("Currency:")
            .get(6).siblingElements().get(2).child(0).text()).isEqualTo(CurrencyEnum.USD.getCurrency());
        softAssertions.assertThat(jasperReportSummaryUSD.getReportHtmlPart().getElementsContainingText("Assembly #:")
            .get(4).child(6).child(2).text()).isEqualTo("TOP-LEVEL");
        softAssertions.assertThat(jasperReportSummaryUSD.getReportHtmlPart().toString().contains("1,287.74")).isEqualTo(true);

        jasperReportSummaryGbp = jasperApiUtils.genericTestCore(
            InputControlsEnum.CURRENCY.getInputControlId(),
            CurrencyEnum.GBP.getCurrency()
        );

        performGbpAssertions(jasperReportSummaryGbp);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLinks({
        @TmsLink("3067"),
        @TmsLink("1929")
    })
    @TestRail(id = {3067, 1929})
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String subAssemblyAssemblyValue = inputControls.getAssemblySelect()
            .getOption(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName().concat(" [assembly] ")).getValue();

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.ASSEMBLY_SELECT.getInputControlId(),
            subAssemblyAssemblyValue
        );

        // cycle time sub totals checking
        ArrayList<BigDecimal> ctComponentValuesToSum = new ArrayList<>();
        ctComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("36.77").get(13).text()));
        ctComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("207.96").get(13).text()));
        ctComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("111.02").get(13).text()));
        ctComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("27.29").get(13).text()));
        ctComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("14.74").get(13).text()));
        ctComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("530.45").get(13).text()));
        BigDecimal expectedCTComponentSubTotal = new BigDecimal("0.00");
        for (BigDecimal ctComponentValue : ctComponentValuesToSum) {
            expectedCTComponentSubTotal = expectedCTComponentSubTotal.add(ctComponentValue);
        }
        BigDecimal actualCTComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("928.23").get(19).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCTComponentSubTotal, actualCTComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedCTAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("593.74").get(13).text());
        BigDecimal actualCTAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("593.74").get(28).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCTAssemblyProcessesSubTotal, actualCTAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedCtGrandTotal = expectedCTComponentSubTotal.add(expectedCTAssemblyProcessesSubTotal);
        BigDecimal actualCtGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("1,521.98").get(25).text().replace(",", ""));
        softAssertions.assertThat(areValuesAlmostEqual(expectedCtGrandTotal, actualCtGrandTotal)).isEqualTo(true);

        // piece part cost sub totals checking
        ArrayList<BigDecimal> ppcComponentValuesToSum = new ArrayList<>();
        ppcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("15.05").get(13).text()).multiply(new BigDecimal("2")));
        ppcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("30.88").get(13).text()));
        ppcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("10.80").get(13).text()).multiply(new BigDecimal("2")));
        ppcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(13).text()));
        ppcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("0.88").get(16).text()));
        ppcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("63.39").get(13).text()));
        BigDecimal expectedPpcComponentSubTotal = new BigDecimal("0.00");
        for (BigDecimal ppcComponentValue : ppcComponentValuesToSum) {
            expectedPpcComponentSubTotal = expectedPpcComponentSubTotal.add(ppcComponentValue);
        }
        BigDecimal actualPPCComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("149.25").get(19).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPpcComponentSubTotal, actualPPCComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedPPCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("3.82").get(13).text());
        BigDecimal actualPPCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("3.82").get(15).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPPCAssemblyProcessesSubTotal, actualPPCAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedPPCGrandTotal = expectedPpcComponentSubTotal.add(expectedPPCAssemblyProcessesSubTotal);
        BigDecimal actualPPCGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("153.07").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPPCGrandTotal, actualPPCGrandTotal)).isEqualTo(true);

        // fully burdened cost sub totals checking
        ArrayList<BigDecimal> fbcComponentValuesToSum = new ArrayList<>();
        fbcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("15.05").get(18).text()).multiply(new BigDecimal("2")));
        fbcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("30.89").get(13).text()));
        fbcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("10.80").get(13).text()).multiply(new BigDecimal("2")));
        fbcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(18).text()));
        fbcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("0.88").get(18).text()));
        fbcComponentValuesToSum.add(new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("63.40").get(13).text()));
        BigDecimal expectedFbcComponentSubTotal = new BigDecimal("0.00");
        for (BigDecimal fbcComponentValue : fbcComponentValuesToSum) {
            expectedFbcComponentSubTotal = expectedFbcComponentSubTotal.add(fbcComponentValue);
        }
        BigDecimal actualFBCComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("149.28").get(19).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFbcComponentSubTotal, actualFBCComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedFBCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("3.82").get(30).text());
        BigDecimal actualFBCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("3.82").get(44).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFBCAssemblyProcessesSubTotal, actualFBCAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedFBCGrandTotal = expectedFbcComponentSubTotal.add(expectedFBCAssemblyProcessesSubTotal);
        BigDecimal actualFBCGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("153.10").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFBCGrandTotal, actualFBCGrandTotal)).isEqualTo(true);

        // capital investments sub totals checking
        BigDecimal expectedCIComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("401.18").get(13).text());
        BigDecimal actualCIComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("401.18").get(22).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCIComponentSubTotal, actualCIComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedCIAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("58.62").get(13).text());
        BigDecimal actualCIAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("58.62").get(28).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCIAssemblyProcessesSubTotal, actualCIAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedCIGrandTotal = expectedCIComponentSubTotal.add(expectedCIAssemblyProcessesSubTotal);
        BigDecimal actualCIGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("459.80").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCIGrandTotal, actualCIGrandTotal)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3068")
    @TestRail(id = 3068)
    @Description("Verify Totals calculations for Sub Sub ASM")
    public void verifyTotalsCalculationsForSubSubAsm() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String subSubAsmAssemblyValue = inputControls.getAssemblySelect()
            .getOption(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName().concat(" [assembly] ")).getValue();

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.ASSEMBLY_SELECT.getInputControlId(),
            subSubAsmAssemblyValue
        );

        // cycle time sub totals checking
        BigDecimal ctComponentValueOne = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("36.77").get(13).text());
        BigDecimal ctComponentValueTwo = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("207.96").get(13).text());
        BigDecimal expectedCTComponentSubTotal = ctComponentValueOne.add(ctComponentValueTwo);
        BigDecimal actualCTComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("244.73").get(19).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCTComponentSubTotal, actualCTComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedCTAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("285.72").get(12).text());
        BigDecimal actualCTAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("285.72").get(28).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCTAssemblyProcessesSubTotal, actualCTAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedCtGrandTotal = expectedCTComponentSubTotal.add(expectedCTAssemblyProcessesSubTotal);
        BigDecimal actualCtGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("530.45").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCtGrandTotal, actualCtGrandTotal)).isEqualTo(true);

        // piece part cost sub totals checking
        BigDecimal ppcComponentValueOne = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("15.05").get(13).text()).multiply(new BigDecimal("2"));
        BigDecimal ppcComponentValueTwo = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("30.88").get(13).text());
        BigDecimal expectedPPCComponentSubTotal = ppcComponentValueOne.add(ppcComponentValueTwo);
        BigDecimal actualPPCComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("60.98").get(19).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPPCComponentSubTotal, actualPPCComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedPPCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(13).text());
        BigDecimal actualPPCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(15).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPPCAssemblyProcessesSubTotal, actualPPCAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedPPCGrandTotal = expectedPPCComponentSubTotal.add(expectedPPCAssemblyProcessesSubTotal);
        BigDecimal actualPPCGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("63.39").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPPCGrandTotal, actualPPCGrandTotal)).isEqualTo(true);

        // fully burdened cost sub totals checking
        BigDecimal fbcComponentValueOne = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("15.05").get(15).text()).multiply(new BigDecimal("2"));
        BigDecimal fbcComponentValueTwo = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("30.89").get(13).text());
        BigDecimal expectedFBCComponentSubTotal = fbcComponentValueOne.add(fbcComponentValueTwo);
        BigDecimal actualFBCComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("61.00").get(19).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFBCComponentSubTotal, actualFBCComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedFBCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(30).text());
        BigDecimal actualFBCAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(44).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFBCAssemblyProcessesSubTotal, actualFBCAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedFBCGrandTotal = expectedFBCComponentSubTotal.add(expectedFBCAssemblyProcessesSubTotal);
        BigDecimal actualFBCGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("63.40").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFBCGrandTotal, actualFBCGrandTotal)).isEqualTo(true);

        // capital investments sub totals checking
        BigDecimal expectedCIComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("348.95").get(13).text());
        BigDecimal actualCIComponentSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("348.95").get(22).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCIComponentSubTotal, actualCIComponentSubTotal)).isEqualTo(true);

        BigDecimal expectedCIAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("52.23").get(13).text());
        BigDecimal actualCIAssemblyProcessesSubTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("52.23").get(28).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCIAssemblyProcessesSubTotal, actualCIAssemblyProcessesSubTotal)).isEqualTo(true);

        BigDecimal expectedCIGrandTotal = expectedCIComponentSubTotal.add(expectedCIAssemblyProcessesSubTotal);
        BigDecimal actualCIGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("401.18").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCIGrandTotal, actualCIGrandTotal)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1934")
    @TestRail(id = 1934)
    @Description("Verify Totals calculations for Top Level")
    public void verifyTotalsCalculationsForTopLevel() {
        // Crowe reckons that we just need to assert on total and sub total values - should be the same as ap pro
        // Think about it and see
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3231")
    @TestRail(id = 3231)
    @Description("Verify Sub Total Calculations for Sub Assembly")
    public void verifySubTotalCalculationsForSubAssembly() {
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore("", "");

        BigDecimal ctComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("928.23").get(19).text());
        BigDecimal ctAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("593.74").get(28).text());
        BigDecimal expectedCtGrandTotal = ctComponentSubtotal.add(ctAssemblyProcessesSubtotal);
        BigDecimal actualCtGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("1,521.98").get(25).text().replace(",", ""));
        softAssertions.assertThat(areValuesAlmostEqual(expectedCtGrandTotal, actualCtGrandTotal)).isEqualTo(true);

        BigDecimal ppcComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("149.25").get(19).text());
        BigDecimal ppcAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("3.82").get(13).text());
        BigDecimal expectedPpcGrandTotal = ppcComponentSubtotal.add(ppcAssemblyProcessesSubtotal);
        BigDecimal actualPpcGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("153.07").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPpcGrandTotal, actualPpcGrandTotal)).isEqualTo(true);

        BigDecimal fbcComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("149.28").get(19).text());
        BigDecimal fbcAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("3.82").get(44).text());
        BigDecimal expectedFbcGrandTotal = fbcComponentSubtotal.add(fbcAssemblyProcessesSubtotal);
        BigDecimal actualFbcGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("153.10").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFbcGrandTotal, actualFbcGrandTotal)).isEqualTo(true);

        BigDecimal ciComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("401.18").get(22).text());
        BigDecimal ciAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("58.62").get(28).text());
        BigDecimal expectedCiGrandTotal = ciComponentSubtotal.add(ciAssemblyProcessesSubtotal);
        BigDecimal actualCiGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("459.80").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCiGrandTotal, actualCiGrandTotal)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3232")
    @TestRail(id = 3232)
    @Description("Verify Sub Total Calculations for Sub-Sub-ASM")
    public void verifySubTotalCalculationsForSubSubASM() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String subSubAsmAssemblyValue = inputControls.getAssemblySelect()
            .getOption(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName().concat(" [assembly] ")).getValue();

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.ASSEMBLY_SELECT.getInputControlId(),
            subSubAsmAssemblyValue
        );

        BigDecimal ctComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("244.73").get(19).text());
        BigDecimal ctAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("285.72").get(28).text());
        BigDecimal expectedCtGrandTotal = ctComponentSubtotal.add(ctAssemblyProcessesSubtotal);
        BigDecimal actualCtGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("530.45").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCtGrandTotal, actualCtGrandTotal)).isEqualTo(true);

        BigDecimal ppcComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("60.98").get(19).text());
        BigDecimal ppcAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(13).text());
        BigDecimal expectedPpcGrandTotal = ppcComponentSubtotal.add(ppcAssemblyProcessesSubtotal);
        BigDecimal actualPpcGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("63.39").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPpcGrandTotal, actualPpcGrandTotal)).isEqualTo(true);

        BigDecimal fbcComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("61.00").get(19).text());
        BigDecimal fbcAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2.41").get(44).text());
        BigDecimal expectedFbcGrandTotal = fbcComponentSubtotal.add(fbcAssemblyProcessesSubtotal);
        BigDecimal actualFbcGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("63.40").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFbcGrandTotal, actualFbcGrandTotal)).isEqualTo(true);

        BigDecimal ciComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("348.95").get(22).text());
        BigDecimal ciAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("52.23").get(28).text());
        BigDecimal expectedCiGrandTotal = ciComponentSubtotal.add(ciAssemblyProcessesSubtotal);
        BigDecimal actualCiGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("401.18").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCiGrandTotal, actualCiGrandTotal)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3233")
    @TestRail(id = 3233)
    @Description("Verify Sub Total Calculations for Top Level")
    public void verifySubTotalCalculationsForTopLevel() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String topLevelAssemblyValue = inputControls.getAssemblySelect()
            .getOption(AssemblySetEnum.TOP_LEVEL.getAssemblySetName().concat(" [assembly] ")).getValue();

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.ASSEMBLY_SELECT.getInputControlId(),
            topLevelAssemblyValue
        );

        BigDecimal ctComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("3,587.59").get(19).text().replace(",", ""));
        BigDecimal ctAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("2,435.13").get(28).text().replace(",", ""));
        BigDecimal expectedCtGrandTotal = ctComponentSubtotal.add(ctAssemblyProcessesSubtotal);
        BigDecimal actualCtGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("6,022.72").get(25).text().replace(",", ""));
        softAssertions.assertThat(areValuesAlmostEqual(expectedCtGrandTotal, actualCtGrandTotal)).isEqualTo(true);

        BigDecimal ppcComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("496.70").get(19).text());
        BigDecimal ppcAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("20.30").get(28).text());
        BigDecimal expectedPpcGrandTotal = ppcComponentSubtotal.add(ppcAssemblyProcessesSubtotal);
        BigDecimal actualPpcGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("516.99").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedPpcGrandTotal, actualPpcGrandTotal)).isEqualTo(true);

        BigDecimal fbcComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("496.77").get(19).text());
        BigDecimal fbcAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("20.32").get(28).text());
        BigDecimal expectedFbcGrandTotal = fbcComponentSubtotal.add(fbcAssemblyProcessesSubtotal);
        BigDecimal actualFbcGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("517.08").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedFbcGrandTotal, actualFbcGrandTotal)).isEqualTo(true);

        BigDecimal ciComponentSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("459.80").get(22).text());
        BigDecimal ciAssemblyProcessesSubtotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("503.00").get(28).text());
        BigDecimal expectedCiGrandTotal = ciComponentSubtotal.add(ciAssemblyProcessesSubtotal);
        BigDecimal actualCiGrandTotal = new BigDecimal(jasperReportSummary.getReportHtmlPart().getElementsContainingText("962.80").get(25).text());
        softAssertions.assertThat(areValuesAlmostEqual(expectedCiGrandTotal, actualCiGrandTotal)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7689")
    @TestRail(id = 7689)
    @Description("Verify Assembly Number Search Criteria")
    public void verifyAssemblyNumberSearchCriteria() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyDetails> inputControlsAssemblyNumberSearchCriteria =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyDetails.class,
                false,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                InputControlsEnum.ASSEMBLY_NUMBER_SEARCH_CRITERIA.getInputControlId(),
                "TOP-LEVEL",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsAssemblyNumberSearchCriteria.getResponseEntity()
            .getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList.get(1).getValue()).isEqualTo("TOP-LEVEL");
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getLabel()).isEqualTo(
            "TOP-LEVEL (Initial) [assembly] "
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1921")
    @TestRail(id = 1921)
    @Description("Export Set search function works - Assembly Details Report")
    public void testExportSetSearchFunctionality() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyDetails> inputControlsAssemblyNumberSearchCriteria =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyDetails.class,
                true,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                "",
                "",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsAssemblyNumberSearchCriteria.getResponseEntity()
            .getInputControlState();
        InputControlState exportSetNameInputControlState = inputControlStateArrayList.get(5);
        softAssertions.assertThat(exportSetNameInputControlState.getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(exportSetNameInputControlState.getOptions().get(0).getLabel()).isEqualTo(ExportSetEnum.TOP_LEVEL.getExportSetName());
        softAssertions.assertThat(exportSetNameInputControlState.getOptions().get(0).getSelected()).isEqualTo(false);

        InputControlState createdByInputControlState = inputControlStateArrayList.get(7);
        String bheganNameValue = "Ben Hegan <bhegan>";
        softAssertions.assertThat(createdByInputControlState.getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(createdByInputControlState.getOptions().get(0).getLabel()).isEqualTo(bheganNameValue);
        softAssertions.assertThat(createdByInputControlState.getOptions().get(0).getSelected()).isEqualTo(false);

        InputControlState lastModifiedByInputControlState = inputControlStateArrayList.get(8);
        softAssertions.assertThat(lastModifiedByInputControlState.getTotalCount()).isEqualTo("2");
        softAssertions.assertThat(lastModifiedByInputControlState.getOptions().get(0).getLabel()).isEqualTo(bheganNameValue);
        softAssertions.assertThat(lastModifiedByInputControlState.getOptions().get(0).getSelected()).isEqualTo(false);
        softAssertions.assertThat(lastModifiedByInputControlState.getOptions().get(1).getLabel()).isEqualTo("Stephen Crowe <scrowe>");
        softAssertions.assertThat(lastModifiedByInputControlState.getOptions().get(1).getSelected()).isEqualTo(false);

        softAssertions.assertThat(inputControlStateArrayList.get(10).getTotalCount()).isEqualTo("3");
        List<InputControlOption> inputControlOptionList = inputControlStateArrayList.get(10).getOptions();
        softAssertions.assertThat(inputControlOptionList.get(0).getLabel()).isEqualTo(AssemblySetEnum.SUB_ASSEMBLY_LONG.getAssemblySetName());
        softAssertions.assertThat(inputControlOptionList.get(1).getLabel()).isEqualTo(AssemblySetEnum.SUB_SUB_ASM_LONG.getAssemblySetName());
        softAssertions.assertThat(inputControlOptionList.get(2).getLabel()).isEqualTo(AssemblySetEnum.TOP_LEVEL_LONG.getAssemblySetName());

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7683")
    @TestRail(id = 7683)
    @Description("Verify Created By Filter Search")
    public void verifyCreatedByFilterSearch() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyDetails> inputControlsCreatedBySearch =
            jasperReportUtil.getInputControlsModifiedAssDetCreatedAndLastModifiedBy(
                UpdatedInputControlsRootItemAssemblyDetails.class,
                true,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                "",
                "",
                ""
            );

        softAssertions.assertThat(inputControlsCreatedBySearch.getResponseEntity().getInputControlState().get(7).getTotalCount())
            .isEqualTo("0");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7686")
    @TestRail(id = 7686)
    @Description("Verify Last Modified By Filter Search")
    public void verifyLastModifiedByFilterSearch() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyDetails> inputControlsCreatedBySearch =
            jasperReportUtil.getInputControlsModifiedAssDetCreatedAndLastModifiedBy(
                UpdatedInputControlsRootItemAssemblyDetails.class,
                true,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                "",
                "",
                ""
            );

        softAssertions.assertThat(inputControlsCreatedBySearch.getResponseEntity().getInputControlState().get(7).getTotalCount())
            .isEqualTo("0");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7684")
    @TestRail(id = 7684)
    @Description("Verify Created By Filter Operation")
    public void verifyCreatedByFilterOperation() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String createdByBenValue = inputControls.getCreatedBy()
            .getOption("Ben Hegan <bhegan>").getValue();

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyDetails> inputControlsCreatedBySearch =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyDetails.class,
                false,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                InputControlsEnum.CREATED_BY.getInputControlId(),
                createdByBenValue,
                ""
            );

        InputControlState createdByInputControlState = inputControlsCreatedBySearch.getResponseEntity().getInputControlState().get(7);
        softAssertions.assertThat(createdByInputControlState.getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(createdByInputControlState.getOptions().get(0).getLabel()).isEqualTo("Ben Hegan <bhegan>");
        softAssertions.assertThat(createdByInputControlState.getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7687")
    @TestRail(id = 7687)
    @Description("Verify Last Modified By Filter Operation")
    public void verifyLastModifiedByFilterOperation() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String createdByBenValue = inputControls.getCreatedBy()
            .getOption("Ben Hegan <bhegan>").getValue();

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyDetails> inputControlsCreatedBySearch =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyDetails.class,
                false,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                InputControlsEnum.LAST_MODIFIED_BY.getInputControlId(),
                createdByBenValue,
                ""
            );

        InputControlState createdByInputControlState = inputControlsCreatedBySearch.getResponseEntity().getInputControlState().get(8);
        softAssertions.assertThat(createdByInputControlState.getTotalCount()).isEqualTo("2");
        softAssertions.assertThat(createdByInputControlState.getOptions().get(0).getLabel()).isEqualTo("Ben Hegan <bhegan>");
        softAssertions.assertThat(createdByInputControlState.getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1919")
    @TestRail(id = 1919)
    @Description("Latest Export Date presents correctly filtered export sets (using date input field)")
    public void testLatestExportDateFilterFunctionsCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDateMinusOneYear = formatter.format(LocalDateTime.now(ZoneOffset.UTC).minusYears(1));

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyDetails> inputControlsLatestExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyDetails.class,
                false,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
                currentDateMinusOneYear,
                ""
            );

        softAssertions.assertThat(inputControlsLatestExportDate.getResponseEntity().getInputControlState().get(2).getValue()).isEqualTo(currentDateMinusOneYear.replace(" ", "T"));
        softAssertions.assertThat(inputControlsLatestExportDate.getResponseEntity().getInputControlState().get(5).getTotalCount()).isEqualTo("0");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("1924")
    @TestRail(id = 1924)
    @Description("Verify report figures from CI Design")
    public void testReportFiguresFromCiDesign() {
        JasperReportUtil jasperReportUtil = new JasperReportUtil(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);

        jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(exportSetName).getValue()
        );

        String topLevelAssemblyValue = inputControls.getAssemblySelect()
            .getOption(AssemblySetEnum.TOP_LEVEL.getAssemblySetName().concat(" [assembly] ")).getValue();

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.ASSEMBLY_SELECT.getInputControlId(),
            topLevelAssemblyValue
        );

        List<Element> valueElementList = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("valign", "top");
        softAssertions.assertThat(valueElementList.get(62).text()).isEqualTo("6,022.72");

        softAssertions.assertThat(valueElementList.get(72).text()).isEqualTo("516.99");

        softAssertions.assertThat(valueElementList.get(82).text()).isEqualTo("517.08");

        softAssertions.assertThat(valueElementList.get(92).text()).isEqualTo("962.80");

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Assembly #:")
            .get(4).child(6).child(2).text()).isEqualTo(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());

        List<Element> nameElementList = jasperReportSummary.getReportHtmlPart()
            .getElementsByAttributeValue("class", "_jrHyperLink ReportExecution");
        softAssertions.assertThat(nameElementList.get(6).text()).isEqualTo("SUB-ASSEMBLY");
        softAssertions.assertThat(nameElementList.get(12).text()).isEqualTo("SUB-SUB-ASM");

        softAssertions.assertAll();
    }

    private void performGbpAssertions(JasperReportSummary jasperReportSummary) {
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Currency:")
            .get(6).siblingElements().get(2).child(0).text()).isEqualTo(CurrencyEnum.GBP.getCurrency());
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Assembly #:")
            .get(4).child(6).child(2).text()).isEqualTo("TOP-LEVEL");
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("962.80")).isEqualTo(true);
    }

    private boolean areValuesAlmostEqual(BigDecimal valueOne, BigDecimal valueTwo) {
        BigDecimal largerValue = valueOne.max(valueTwo);
        BigDecimal smallerValue = valueOne.min(valueTwo);
        BigDecimal difference = largerValue.subtract(smallerValue);
        return difference.compareTo(new BigDecimal("0.00")) >= 0 &&
            difference.compareTo(new BigDecimal("0.03")) <= 0;
    }
}
