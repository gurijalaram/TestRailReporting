package com.apriori.cir.ui.tests.ootbreports.newreportstests.upgradecomparison;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemScenarioComparison;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemUpgradePartComparison;
import com.apriori.cir.ui.enums.RollupEnum;
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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UpgradePartComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.UPGRADE_PART_COMPARISON.getEndpoint();
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.UPGRADE_PART_COMPARISON;
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13952")
    @TestRail(id = 13952)
    @Description("Input controls - Currency code")
    public void testCurrency() {
        ArrayList<String> gbpAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.GBP.getCurrency(), 2);

        ArrayList<String> usdAssertValues = jasperApiUtils.generateReportAndGetAssertValues(CurrencyEnum.USD.getCurrency(), 2);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("14068")
    @TestRail(id = 14068)
    @Description("Input controls - Use Latest Export")
    public void testUseLatestExportInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentRollup = inputControls.getRollup().getOption("ALL PG").getValue();
        String currentPartNumber = inputControls.getPartNumber().getOption("2X1 CAVITY MOLD").getValue();

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsUseLatestExportScenario =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PART_NUMBER.getInputControlId(),
                currentPartNumber,
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsUseLatestExportScenario.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList.get(8).getOptions().get(0).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(8).getOptions().get(0).getLabel()).isEqualTo("Scenario");
        softAssertions.assertThat(inputControlStateArrayList.get(9).getTotalCount()).isEqualTo("12");

        // repeat as above for all
        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsUseLatestExportAll =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PART_NUMBER.getInputControlId(),
                currentPartNumber,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "All",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList2 = inputControlsUseLatestExportAll.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList2.get(8).getOptions().get(1).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList2.get(8).getOptions().get(1).getLabel()).isEqualTo("All");
        softAssertions.assertThat(inputControlStateArrayList2.get(9).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(10).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(10).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(11).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(11).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(12).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(12).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(13).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(14).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(14).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateArrayList2.get(15).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList2.get(15).getOptions().get(0).getLabel()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateArrayList2.get(15).getOptions().get(0).getSelected()).isEqualTo(true);

        // repeat as above for no
        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsUseLatestExportNo =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparison(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                false,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.ROLLUP.getInputControlId(),
                currentRollup,
                InputControlsEnum.PART_NUMBER.getInputControlId(),
                currentPartNumber,
                InputControlsEnum.USE_LATEST_EXPORT.getInputControlId(),
                "No",
                ""
            );

        ArrayList<InputControlState> inputControlStateArrayList3 = inputControlsUseLatestExportNo.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList3.get(8).getOptions().get(2).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList3.get(8).getOptions().get(2).getLabel()).isEqualTo("No");
        softAssertions.assertThat(inputControlStateArrayList3.get(9).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateArrayList3.get(10).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList3.get(10).getOptions().get(0).getLabel()).isEqualTo("ALL PG");
        softAssertions.assertThat(inputControlStateArrayList3.get(10).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("13951")
    @TestRail(id = 13951)
    @Description("Input controls - Rollup")
    public void testRollupInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetOne = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();
        String currentExportSetTwo = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();

        ResponseWrapper<UpdatedInputControlsRootItemUpgradePartComparison> inputControlsRollup =
            jasperReportUtil.getInputControlsModifiedUpgradePartComparisonSetTwoExportSets(
                UpdatedInputControlsRootItemUpgradePartComparison.class,
                ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(),
                InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
                currentExportSetOne,
                currentExportSetTwo
            );

        ArrayList<InputControlState> inputControlStateArrayList = inputControlsRollup.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStateArrayList.get(9).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(0).getLabel()).isEqualTo(
            ExportSetEnum.ALL_PG_CURRENT.getExportSetName()
        );
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(0).getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(1).getLabel()).isEqualTo(
            ExportSetEnum.ALL_PG_NEW.getExportSetName()
        );
        softAssertions.assertThat(inputControlStateArrayList.get(9).getOptions().get(1).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateArrayList.get(10).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getLabel()).isEqualTo(
            RollupEnum.ALL_PG.getRollupName()
        );
        softAssertions.assertThat(inputControlStateArrayList.get(10).getOptions().get(0).getSelected()).isEqualTo(false);

        JasperReportSummary jasperReportSummaryBothPgSetsSelected = jasperApiUtils.genericTestCoreSetTwoExportSetsAndAllPgRollup(
            ExportSetEnum.ALL_PG_CURRENT.getExportSetName(),
            ExportSetEnum.ALL_PG_NEW.getExportSetName()
        );

        softAssertions.assertThat(jasperReportSummaryBothPgSetsSelected).isNotEqualTo(null);
        softAssertions.assertThat(jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Rollup:").get(6).siblingElements().get(2).text()).isEqualTo("2X1 CAVITY MOLD");
        String exportSetValueFromChart = jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Export set:").get(6).siblingElements().get(2).text();
        softAssertions.assertThat(exportSetValueFromChart).contains(ExportSetEnum.ALL_PG_CURRENT.getExportSetName());
        softAssertions.assertThat(exportSetValueFromChart).contains(ExportSetEnum.ALL_PG_NEW.getExportSetName());
        softAssertions.assertThat(jasperReportSummaryBothPgSetsSelected.getReportHtmlPart()
            .getElementsContainingText("Part number:").get(6).siblingElements().get(2).text()).contains("2X1 CAVITY MOLD");

        softAssertions.assertAll();
    }
}
