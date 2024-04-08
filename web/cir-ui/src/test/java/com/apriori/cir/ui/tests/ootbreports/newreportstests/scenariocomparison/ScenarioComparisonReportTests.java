package com.apriori.cir.ui.tests.ootbreports.newreportstests.scenariocomparison;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItem;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScenarioComparisonReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SCENARIO_COMPARISON.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.SCENARIO_COMPARISON;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3305")
    @TestRail(id = 3305)
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        SoftAssertions softAssertions = new SoftAssertions();

        String gbpCurrency = CurrencyEnum.GBP.getCurrency();
        String usdCurrency = CurrencyEnum.USD.getCurrency();

        List<String> valuesGbp = currencyTestCore(gbpCurrency);

        List<String> valuesUsd = currencyTestCore(usdCurrency);

        softAssertions.assertThat(valuesGbp.get(0)).isNotEqualTo(valuesUsd.get(0));
        softAssertions.assertThat(valuesGbp.get(0)).isEqualTo(gbpCurrency);
        softAssertions.assertThat(valuesUsd.get(0)).isEqualTo(usdCurrency);

        softAssertions.assertThat(valuesGbp.get(1)).isNotEqualTo(valuesUsd.get(1));

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3248")
    @TmsLink("13709")
    @TestRail(id = {3248, 13709})
    @Description("Verify Component Type input control functions correctly")
    public void verifyComponentTypeInputControlFunctionsCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        UpdatedInputControlsRootItem inputControlsAssemblySelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                "componentType",
                "assembly",
                ""
            );

        String componentTypeOptions = inputControlsAssemblySelected.getInputControlState().get(7).getAllOptions().toString();
        softAssertions.assertThat(componentTypeOptions.contains("assembly")).isEqualTo(true);
        softAssertions.assertThat(componentTypeOptions.contains("part")).isEqualTo(true);
        softAssertions.assertThat(componentTypeOptions.contains("rollup")).isEqualTo(true);
        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(7).getTotalCount()).isEqualTo("3");

        String scenarioNameOptions = inputControlsAssemblySelected.getInputControlState().get(10).getAllOptions().toString();
        softAssertions.assertThat(scenarioNameOptions.contains("Initial")).isEqualTo(true);
        softAssertions.assertThat(scenarioNameOptions.contains("Multi VPE")).isEqualTo(true);
        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(10).getTotalCount()).isEqualTo("2");

        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(8).getTotalCount()).isEqualTo("3");
        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(9).getTotalCount()).isEqualTo("3");

        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(11).getTotalCount()).isEqualTo("8");
        List<String> componentAssemblyOptions = inputControlsAssemblySelected.getInputControlState().get(11).getAllOptions();
        for (String component : componentAssemblyOptions) {
            softAssertions.assertThat(component.contains("[assembly]")).isEqualTo(true);
        }

        UpdatedInputControlsRootItem inputControlsPartSelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                "componentType",
                "part",
                ""
            );

        softAssertions.assertThat(inputControlsPartSelected.getInputControlState().get(10).getTotalCount()).isEqualTo("17");

        softAssertions.assertThat(inputControlsPartSelected.getInputControlState().get(8).getTotalCount()).isEqualTo("10");
        softAssertions.assertThat(inputControlsPartSelected.getInputControlState().get(9).getTotalCount()).isEqualTo("10");

        softAssertions.assertThat(inputControlsPartSelected.getInputControlState().get(11).getTotalCount()).isEqualTo("249");
        List<String> componentPartOptions = inputControlsPartSelected.getInputControlState().get(11).getAllOptions();
        for (String component : componentPartOptions) {
            softAssertions.assertThat(component.contains("[part]")).isEqualTo(true);
        }

        UpdatedInputControlsRootItem inputControlsRollupSelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                "componentType",
                "rollup",
                ""
            );

        softAssertions.assertThat(inputControlsRollupSelected.getInputControlState().get(10).getTotalCount()).isEqualTo("6");

        softAssertions.assertThat(inputControlsRollupSelected.getInputControlState().get(8).getTotalCount()).isEqualTo("6");
        softAssertions.assertThat(inputControlsRollupSelected.getInputControlState().get(9).getTotalCount()).isEqualTo("5");

        softAssertions.assertThat(inputControlsRollupSelected.getInputControlState().get(11).getTotalCount()).isEqualTo("16");
        List<String> componentRollupOptions = inputControlsRollupSelected.getInputControlState().get(11).getAllOptions();
        for (String component : componentRollupOptions) {
            softAssertions.assertThat(component.contains("[rollup]")).isEqualTo(true);
        }

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3246")
    @TestRail(id = 3246)
    @Description("Verify Export Set input control functions correctly")
    public void verifyExportSetInputControlFunctionsCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();

        UpdatedInputControlsRootItem inputControlsExportSetSelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                "exportSetName",
                currentExportSet,
                ""
            );

        ArrayList<InputControlState> inputControlStateList = inputControlsExportSetSelected.getInputControlState();
        softAssertions.assertThat(inputControlStateList.get(7).getTotalCount()).isEqualTo("3");
        softAssertions.assertThat(inputControlStateList.get(8).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateList.get(9).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateList.get(10).getTotalCount()).isEqualTo("19");
        softAssertions.assertThat(inputControlStateList.get(11).getTotalCount()).isEqualTo("273");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3247")
    @TestRail(id = 3247)
    @Description("Verify Scenarios to Compare input control functions correctly")
    public void verifyScenariosToCompareInputControlFunctionsCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentScenarioToCompareID = inputControls.getScenarioToCompareID().getOption("-12 (Bulkload) [part] ").getValue();

        UpdatedInputControlsRootItem inputControlsScenarioToCompareSelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                "scenarioToCompareIDs",
                currentScenarioToCompareID,
                ""
            );

        ArrayList<InputControlState> inputControlStateList = inputControlsScenarioToCompareSelected.getInputControlState();

        // TODO - check if this IC works properly (check TR for what it should do, etc)
        softAssertions.assertThat(inputControlStateList.get(11).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateList.get(8).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateList.get(9).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateList.get(10).getTotalCount()).isEqualTo("19");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3249")
    @TestRail(id = 3249)
    @Description("Verify scenario name input control functions correctly")
    public void verifyScenarioNameInputControlFunctionsCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSetValue = inputControls.getExportSetName().getOption(exportSetName).getValue();

        UpdatedInputControlsRootItem inputControlsScenarioNameSelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                "scenarioName",
                "Initial",
                currentExportSetValue
            );

        softAssertions.assertThat(inputControlsScenarioNameSelected.getInputControlState().get(11).getTotalCount()).isEqualTo("133");

        List<String> scenarioNameOptionsList = inputControlsScenarioNameSelected.getInputControlState().get(11).getAllOptions();
        for (String scenarioName : scenarioNameOptionsList) {
            softAssertions.assertThat(scenarioName.contains("Initial")).isEqualTo(true);
        }

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3304")
    @TestRail(id = 3304)
    @Description("Verify export date input controls functions correctly")
    public void verifyExportDateInputControlsFunctionCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String currentDateTimePlusOneMonth = DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now().plusMonths(1));

        UpdatedInputControlsRootItem inputControlsEarliestExportDateSelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId(),
                currentDateTimePlusOneMonth,
                ""
            );

        ArrayList<InputControlState> earliestInputControlStateList = inputControlsEarliestExportDateSelected.getInputControlState();
        softAssertions.assertThat(earliestInputControlStateList.get(5).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(earliestInputControlStateList.get(8).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(earliestInputControlStateList.get(9).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(earliestInputControlStateList.get(10).getTotalCount()).isEqualTo("19");

        String currentDateTimePlusOneYear = DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now().minusYears(1));
        UpdatedInputControlsRootItem inputControlsLatestExportDateSelected =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
                currentDateTimePlusOneYear,
                ""
            );

        ArrayList<InputControlState> latestInputControlStateList = inputControlsLatestExportDateSelected.getInputControlState();
        softAssertions.assertThat(latestInputControlStateList.get(5).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(latestInputControlStateList.get(8).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(latestInputControlStateList.get(9).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(latestInputControlStateList.get(10).getTotalCount()).isEqualTo("19");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3306")
    @TestRail(id = 3306)
    @Description("Verify Part Number Search Criteria input control works correctly")
    public void verifyPartNumberSearchCriteriaInputControlWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        UpdatedInputControlsRootItem inputControlsPartNumberSearchCriteria =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                InputControlsEnum.PART_NUMBER_SEARCH_CRITERIA.getInputControlId(),
                "-12",
                ""
            );

        InputControlState scenariosToCompareState = inputControlsPartNumberSearchCriteria.getInputControlState().get(11);
        softAssertions.assertThat(scenariosToCompareState.getTotalCount()).isEqualTo("2");
        softAssertions.assertThat(scenariosToCompareState.getOptions().get(0).getSelected()).isEqualTo(true);
        softAssertions.assertThat(scenariosToCompareState.getOptions().get(0).getLabel()).isEqualTo("-12 (Bulkload) [part] ");

        InputControlState scenarioNameState = inputControlsPartNumberSearchCriteria.getInputControlState().get(10);
        softAssertions.assertThat(scenarioNameState.getTotalCount()).isEqualTo("2");
        softAssertions.assertThat(scenarioNameState.getOptions().get(0).getSelected()).isEqualTo(false);
        softAssertions.assertThat(scenarioNameState.getOptions().get(0).getLabel()).isEqualTo("Bulkload");
        softAssertions.assertThat(scenarioNameState.getOptions().get(1).getSelected()).isEqualTo(false);
        softAssertions.assertThat(scenarioNameState.getOptions().get(1).getLabel()).isEqualTo("Final");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3307")
    @TestRail(id = 3307)
    @Description("Verify Created By input control works correctly - Filter Operation - Scenario Comparison Report")
    public void verifyCreatedByInputControlWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String bheganUserValue = inputControls.getCreatedBy().getOption("bhegan").getValue();

        UpdatedInputControlsRootItem inputControlsCreatedBy =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                InputControlsEnum.CREATED_BY.getInputControlId(),
                bheganUserValue,
                ""
            );

        InputControlState createdByState = inputControlsCreatedBy.getInputControlState().get(8);
        softAssertions.assertThat(createdByState.getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(createdByState.getOptions().get(3).getSelected()).isEqualTo(true);
        softAssertions.assertThat(createdByState.getOptions().get(3).getLabel()).isEqualTo("bhegan");

        InputControlState lastModifiedByState = inputControlsCreatedBy.getInputControlState().get(9);
        softAssertions.assertThat(lastModifiedByState.getTotalCount()).isEqualTo("2");
        softAssertions.assertThat(lastModifiedByState.getOptions().get(0).getLabel()).isEqualTo("bhegan");
        softAssertions.assertThat(lastModifiedByState.getOptions().get(1).getLabel()).isEqualTo("scrowe");

        InputControlState scenarioNameState = inputControlsCreatedBy.getInputControlState().get(10);
        softAssertions.assertThat(scenarioNameState.getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(scenarioNameState.getOptions().get(0).getLabel()).isEqualTo("Initial");

        softAssertions.assertThat(inputControlsCreatedBy.getInputControlState().get(11).getTotalCount()).isEqualTo("18");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3349")
    @TestRail(id = 3349)
    @Description("Verify Last Modified By input control works correctly - Filter Operation - Scenario Comparison Report")
    public void verifyLastModifiedByInputControlWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String bheganUserValue = inputControls.getLastModifiedBy().getOption("bhegan").getValue();

        UpdatedInputControlsRootItem inputControlsLastModifiedBy =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                InputControlsEnum.LAST_MODIFIED_BY.getInputControlId(),
                bheganUserValue,
                ""
            );

        softAssertions.assertThat(inputControlsLastModifiedBy.getInputControlState().get(9).getTotalCount()).isEqualTo("12");

        InputControlState scenarioNameState = inputControlsLastModifiedBy.getInputControlState().get(10);
        softAssertions.assertThat(scenarioNameState.getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(scenarioNameState.getOptions().get(0).getSelected()).isEqualTo(false);
        softAssertions.assertThat(scenarioNameState.getOptions().get(0).getLabel()).isEqualTo("Initial");

        softAssertions.assertThat(inputControlsLastModifiedBy.getInputControlState().get(11).getTotalCount()).isEqualTo("18");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7665")
    @TestRail(id = 7665)
    @Description("Verify Created By input control search works - Scenario Comparison Report")
    public void verifyCreatedByInputControlSearchWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        UpdatedInputControlsRootItem inputControlsCreatedBy =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                InputControlsEnum.CREATED_BY.getInputControlId(),
                "bhegan",
                ""
            );

        softAssertions.assertThat(inputControlsCreatedBy.getInputControlState().get(8).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlsCreatedBy.getInputControlState().get(8).getOptions().get(0).getSelected()).isEqualTo(false);
        softAssertions.assertThat(inputControlsCreatedBy.getInputControlState().get(8).getOptions().get(0).getLabel()).isEqualTo("bhegan");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7664")
    @TestRail(id = 7664)
    @Description("Verify Last Modified By input control search works - Scenario Comparison Report")
    public void verifyLastModifiedByInputControlSearchWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        UpdatedInputControlsRootItem inputControlsCreatedBy =
            jasperReportUtil.getInputControlsModified(
                JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC,
                InputControlsEnum.LAST_MODIFIED_BY.getInputControlId(),
                "bhegan",
                ""
            );

        softAssertions.assertThat(inputControlsCreatedBy.getInputControlState().get(9).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlsCreatedBy.getInputControlState().get(9).getOptions().get(0).getSelected()).isEqualTo(false);
        softAssertions.assertThat(inputControlsCreatedBy.getInputControlState().get(9).getOptions().get(0).getLabel()).isEqualTo("bhegan");

        softAssertions.assertAll();
    }

    private List<String> currencyTestCore(String currencyToUse) {
        JasperReportSummary jasperReportSummaryGbp = jasperApiUtils.genericTestCore(
            InputControlsEnum.CURRENCY.getInputControlId(),
            currencyToUse
        );

        String currencySettingValueGBP = jasperReportSummaryGbp.getReportHtmlPart()
            .getElementsContainingText("Currency").get(5).children().get(3).text();

        String currencyValueGBP = jasperReportSummaryGbp.getReportHtmlPart()
            .getElementsContainingText("FULLY BURDENED COST").get(6).parent().children().get(3).text();

        return Arrays.asList(
            currencySettingValueGBP,
            currencyValueGBP
        );
    }
}
