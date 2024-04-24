package com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblycost;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemAssemblyCostA4;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.google.common.base.Stopwatch;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

@Data
@Slf4j
public class GenericAssemblyCostTests {

    private final SoftAssertions softAssertions = new SoftAssertions();

    /**
     * Generic test for currency in Assembly Cost Reports (both A4 and Letter)
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

        // fix assertions and move on (quickly yet properly)
        ArrayList<InputControlState> inputControlStateArrayList = inputControlsTopLevelSelected.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList.get(3).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateArrayList.get(3).getOption("- - - 0 0 0-top-level-0").getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(7).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(7).getAllOptions().toString().contains("Initial")).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(8).getTotalCount()).isEqualTo("14");

        JasperReportSummary jasperReportSummaryTopLevelSelected = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(jasperApiUtils.getExportSetName()).getValue()
        );

        softAssertions.assertThat(
            jasperReportSummaryTopLevelSelected.getReportHtmlPart().getElementsContainingText("Part Number:").get(6)
                .siblingElements().get(1).text()
        ).isEqualTo("3538968");

        softAssertions.assertThat(
            jasperReportSummaryTopLevelSelected.getReportHtmlPart().getElementsContainingText("Scenario:").get(6)
                .siblingElements().get(1).text()
        ).isEqualTo("Initial");

        softAssertions.assertAll();
    }
}
