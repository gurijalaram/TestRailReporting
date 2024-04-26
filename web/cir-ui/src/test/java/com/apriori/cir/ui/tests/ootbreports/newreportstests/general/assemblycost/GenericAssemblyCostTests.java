package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblycost;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemAssemblyCostA4;
import com.apriori.cir.ui.enums.AssemblySetEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import com.google.common.base.Stopwatch;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class GenericAssemblyCostTests {

    private final SoftAssertions softAssertions = new SoftAssertions();

    /**
     * Generic test for currency in Assembly Cost Reports (both A4 and Letter)
     *
     * @param jasperApiUtils - instance of utils class (contains data required)
     */
    public void genericAssemblyCostCurrencyTest(JasperApiUtils jasperApiUtils) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), jasperApiUtils.getExportSetName());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), currentDateTime);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummaryGBP = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());
        timer.stop();
        log.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        String currencyValueGBP = jasperReportSummaryGBP.getReportHtmlPart().getElementsContainingText("Currency").get(6).parent().child(3).text();
        String capInvValueGBP = jasperReportSummaryGBP.getReportHtmlPart().getElementsContainingText("Capital Investments").get(6).parent().child(3).text();

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.USD.getCurrency());
        JasperReportSummary jasperReportSummaryUSD = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());

        String currencyValueUSD = jasperReportSummaryUSD.getReportHtmlPart().getElementsContainingText("Currency").get(6).parent().child(3).text();
        String capInvValueUSD = jasperReportSummaryUSD.getReportHtmlPart().getElementsContainingText("Capital Investments").get(6).parent().child(3).text();

        softAssertions.assertThat(currencyValueGBP).isNotEqualTo(currencyValueUSD);
        softAssertions.assertThat(capInvValueGBP).isNotEqualTo(capInvValueUSD);
        softAssertions.assertAll();
    }

    /**
     * Generic test for export set name (A4 and Letter Reports)
     *
     * @param jasperApiUtils - instance of utils class (contains data required)
     */
    public void testExportSetName(JasperApiUtils jasperApiUtils) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        InputControl inputControls = jasperReportUtil.getInputControls(jasperApiUtils.getReportValueForInputControls());
        String currentExportSet = inputControls.getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyCostA4> inputControlsTopLevelSelected =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyCostA4.class,
                false,
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                "",
                "",
                currentExportSet
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsTopLevelSelected.getResponseEntity().getInputControlState();

        softAssertions.assertThat(inputControlStateArrayList.get(2).getTotalCount()).isEqualTo("5");
        softAssertions.assertThat(inputControlStateArrayList.get(2).getOptions().get(0).getValue()).isEqualTo(ExportSetEnum.PISTON_ASSEMBLY.getExportSetName());
        softAssertions.assertThat(inputControlStateArrayList.get(2).getOptions().get(1).getValue()).isEqualTo(ExportSetEnum.ROLL_UP_A.getExportSetName());
        softAssertions.assertThat(inputControlStateArrayList.get(2).getOptions().get(2).getValue()).isEqualTo(ExportSetEnum.SUB_SUB_ASM.getExportSetName());
        softAssertions.assertThat(inputControlStateArrayList.get(2).getOptions().get(3).getValue()).isEqualTo(ExportSetEnum.TOP_LEVEL.getExportSetName());
        softAssertions.assertThat(inputControlStateArrayList.get(2).getOptions().get(4).getValue()).isEqualTo(ExportSetEnum.TOP_LEVEL_MULTI_VPE.getExportSetName());
        softAssertions.assertThat(inputControlStateArrayList.get(2).getOptions().get(3).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(3).getTotalCount()).isEqualTo("3");
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(0).getValue()).isEqualTo(AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName());
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(1).getValue()).isEqualTo(AssemblySetEnum.SUB_SUB_ASM_SHORT.getAssemblySetName());
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(2).getValue()).isEqualTo(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(2).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(4).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(4).getOptions().get(0).getValue()).isEqualTo("Initial");
        softAssertions.assertThat(inputControlStateArrayList.get(4).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for assembly part number and scenario name dropdown (both A4 and Letter Report)
     *
     * @param jasperApiUtils - instance of utils class (contains data required)
     */
    public void testAssemblyPartNumberOrScenarioNameDropdown(JasperApiUtils jasperApiUtils) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        InputControl inputControls = jasperReportUtil.getInputControls(jasperApiUtils.getReportValueForInputControls());
        jasperApiUtils.setExportSetName(ExportSetEnum.PISTON_ASSEMBLY.getExportSetName());
        String currentExportSet = inputControls.getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyCostA4> inputControlsTopLevelPartSelected =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyCostA4.class,
                false,
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                InputControlsEnum.PART_NUMBER_SEARCH_CRITERIA.getInputControlId(),
                AssemblySetEnum.PISTON_ASSEMBLY.getAssemblySetName(),
                currentExportSet
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsTopLevelPartSelected.getResponseEntity().getInputControlState();

        softAssertions.assertThat(inputControlStateArrayList.get(3).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(0).getValue()).isEqualTo(AssemblySetEnum.PISTON_ASSEMBLY.getAssemblySetName());
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(4).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(4).getOptions().get(0).getValue()).isEqualTo("Initial");
        softAssertions.assertThat(inputControlStateArrayList.get(4).getOptions().get(0).getSelected()).isEqualTo(true);

        jasperApiUtils.setExportSetName(ExportSetEnum.PISTON_ASSEMBLY.getExportSetName());
        JasperReportSummary jasperReportSummaryPistonAssemblySelected = jasperApiUtils.genericTestCore(
            InputControlsEnum.PART_NUMBER_SEARCH_CRITERIA.getInputControlId(),
            inputControls.getPartNumber().getOption(AssemblySetEnum.PISTON_ASSEMBLY.getAssemblySetName()).getValue()
        );

        Document pistonAssemblyReportHtmlPart = jasperReportSummaryPistonAssemblySelected.getReportHtmlPart();

        softAssertions.assertThat(pistonAssemblyReportHtmlPart
            .getElementsContainingText("Assembly #:").get(6).siblingElements().get(2).child(0).text()
        ).isEqualTo("PISTON_ASSEMBLY");

        softAssertions.assertThat(pistonAssemblyReportHtmlPart
            .getElementsContainingText("Scenario Name:").get(6).siblingElements().get(2).child(0).text()
        ).isEqualTo("Initial");

        softAssertions.assertAll();
    }

    /**
     * Generic test for assembly selection (both A4 and Letter report)
     *
     * @param jasperApiUtils - instance of utils class (contains data required)
     */
    public void testSelectAssemblyFunctionality(JasperApiUtils jasperApiUtils) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        InputControl inputControls = jasperReportUtil.getInputControls(jasperApiUtils.getReportValueForInputControls());
        String currentExportSet = inputControls.getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemAssemblyCostA4> inputControlsTopLevelPartSelected =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemAssemblyCostA4.class,
                false,
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(),
                InputControlsEnum.PART_NUMBER_SEARCH_CRITERIA.getInputControlId(),
                AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName(),
                currentExportSet
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsTopLevelPartSelected.getResponseEntity().getInputControlState();

        softAssertions.assertThat(inputControlStateArrayList).isNotEqualTo(null);

        softAssertions.assertThat(inputControlStateArrayList.get(3).getTotalCount()).isEqualTo("3");
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(0).getValue()).isEqualTo(AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName());
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(1).getValue()).isEqualTo(AssemblySetEnum.SUB_SUB_ASM_SHORT.getAssemblySetName());
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(2).getValue()).isEqualTo(AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName());
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOptions().get(0).getSelected()).isEqualTo(true);

        jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(currentExportSet).getValue()
        );
        JasperReportSummary jasperReportSummarySubAssemblySelected = jasperApiUtils.genericTestCore(
            InputControlsEnum.PART_NUMBER_SEARCH_CRITERIA.getInputControlId(),
            AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()
        );

        Document subAssemblyReportHtmlPart = jasperReportSummarySubAssemblySelected.getReportHtmlPart();

        softAssertions.assertThat(subAssemblyReportHtmlPart
            .getElementsContainingText("Assembly #:").get(6).siblingElements().get(2).child(0).text()
        ).isEqualTo("SUB-ASSEMBLY");

        softAssertions.assertThat(subAssemblyReportHtmlPart
            .getElementsContainingText("Scenario Name:").get(6).siblingElements().get(2).child(0).text()
        ).isEqualTo("Initial");

        softAssertions.assertAll();
    }

    /**
     * Generic test for capital investment test
     *
     * @param jasperApiUtils - instance of utils class (contains data required)
     */
    public void verifyCapitalInvestmentCostFunctionality(JasperApiUtils jasperApiUtils) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperApiUtils.getJasperSessionID());
        InputControl inputControls = jasperReportUtil.getInputControls(jasperApiUtils.getReportValueForInputControls());
        String currentExportSet = inputControls.getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue();

        JasperReportSummary jasperReportSummaryTopLevelSelected = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(currentExportSet).getValue()
        );

        Document topLevelReportHtmlPart = jasperReportSummaryTopLevelSelected.getReportHtmlPart();

        softAssertions.assertThat(topLevelReportHtmlPart
            .getElementsContainingText("Assembly #:").get(6).siblingElements().get(2).child(0).text()
        ).isEqualTo("TOP-LEVEL");

        softAssertions.assertThat(topLevelReportHtmlPart
            .getElementsContainingText("Scenario Name:").get(6).siblingElements().get(2).child(0).text()
        ).isEqualTo("Initial");

        softAssertions.assertThat(topLevelReportHtmlPart
            .getElementsContainingText("Capital Investments").get(18).siblingElements().get(1).child(0).text()
        ).isEqualTo("962.80");

        softAssertions.assertAll();
    }
}
