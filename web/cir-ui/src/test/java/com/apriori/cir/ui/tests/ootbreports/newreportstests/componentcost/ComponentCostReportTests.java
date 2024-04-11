package com.apriori.cir.ui.tests.ootbreports.newreportstests.componentcost;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemComponentCost;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemScenarioComparison;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ComponentCostReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.COMPONENT_COST.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.COMPONENT_COST;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3329")
    @TestRail(id = 3329)
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        jasperApiUtils.genericComponentCostCurrencyTest();
    }

    /*@Test
    @Tag(JASPER_API)
    @TmsLink("3324")
    @TestRail(id = 3324)
    @Description("Verify Export Set drop-down functions correctly")
    public void verifyExportSetDropdownWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();

        /*UpdatedInputControlsRootItemScenarioComparison inputControlsTopLevelSelected =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                "",
                "",
                currentExportSet,
                false
        );

        ArrayList<InputControlState> inputControlStateList = inputControlsTopLevelSelected.getInputControlState();

        softAssertions.assertThat(inputControlStateList.get(3).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateList.get(3).getOption("- - - 0 0 0-top-level-0").getSelected()).isEqualTo(true);
        softAssertions.assertThat(inputControlStateList.get(7).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlStateList.get(7).getAllOptions().toString().contains("Initial")).isEqualTo(true);
        softAssertions.assertThat(inputControlStateList.get(8).getTotalCount()).isEqualTo("14");

        JasperReportSummary jasperReportSummaryTopLevelSelected = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(exportSetName).getValue()
        );

        Document topLevelReportHtmlPart = jasperReportSummaryTopLevelSelected.getReportHtmlPart();

        softAssertions.assertThat(topLevelReportHtmlPart.getElementsContainingText("Part Number:").get(6)
            .siblingElements().get(1).text()
        ).isEqualTo("3538968");

        softAssertions.assertThat(topLevelReportHtmlPart.getElementsContainingText("Scenario:").get(6)
            .siblingElements().get(1).text()
        ).isEqualTo("Initial");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3325")
    @TestRail(id = 3325)
    @Description("Verify Component Select drop-down functions correctly")
    public void verifyComponentSelectDropdownWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentComponentSelected = inputControls.getComponentSelect().getOption("3538968 (Initial)  [part]").getValue();

        UpdatedInputControlsRootItemScenarioComparison inputControlsComponentSelected =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                InputControlsEnum.COMPONENT_SELECT.getInputControlId(),
                currentComponentSelected,
                currentExportSet,
                false
            );

        softAssertions.assertThat(inputControlsComponentSelected.getInputControlState().get(3).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlsComponentSelected.getInputControlState().get(7).getTotalCount()).isEqualTo("1");
        softAssertions.assertThat(inputControlsComponentSelected.getInputControlState().get(7).getOption("Initial").getSelected())
            .isEqualTo(false);

        jasperApiUtils.setReportParameterByName(
            InputControlsEnum.COMPONENT_SELECT.getInputControlId(),
            inputControls.getComponentSelect().getOption("TOP-LEVEL (Initial)  [assembly]").getValue()
        );

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(exportSetName).getValue()
        );

        softAssertions.assertThat(
            jasperReportSummary.getReportHtmlPart().getElementsContainingText("Part Number:").get(7).parent()
                .siblingElements().get(1).children().get(0).text()
        ).isEqualTo("TOP-LEVEL");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3326")
    @TmsLink("13708")
    @TestRail(id = {3326, 13708})
    @Description("Verify Component Type drop-down functions correctly")
    public void verifyComponentTypeDropdownFunctionsCorrect() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        UpdatedInputControlsRootItemScenarioComparison inputControlsAssemblySelected =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                InputControlsEnum.COMPONENT_TYPE.getInputControlId(),
                "assembly",
                "",
                false
            );

        String componentTypeOptions = inputControlsAssemblySelected.getInputControlState().get(4).getAllOptions().toString();
        softAssertions.assertThat(componentTypeOptions.contains("---")).isEqualTo(true);
        softAssertions.assertThat(componentTypeOptions.contains("assembly")).isEqualTo(true);
        softAssertions.assertThat(componentTypeOptions.contains("part")).isEqualTo(true);
        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(4).getTotalCount()).isEqualTo("3");

        String scenarioNameOptions = inputControlsAssemblySelected.getInputControlState().get(7).getAllOptions().toString();
        softAssertions.assertThat(scenarioNameOptions.contains("Initial")).isEqualTo(true);
        softAssertions.assertThat(scenarioNameOptions.contains("Multi VPE")).isEqualTo(true);
        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(7).getTotalCount()).isEqualTo("2");

        softAssertions.assertThat(inputControlsAssemblySelected.getInputControlState().get(8).getTotalCount()).isEqualTo("8");
        List<String> componentAssemblyOptions = inputControlsAssemblySelected.getInputControlState().get(8).getAllOptions();
        for (String component : componentAssemblyOptions) {
            softAssertions.assertThat(component.contains("[assembly]")).isEqualTo(true);
        }

        UpdatedInputControlsRootItemScenarioComparison inputControlsPartSelected =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                InputControlsEnum.COMPONENT_TYPE.getInputControlId(),
                "part",
                "",
                false
            );
        softAssertions.assertThat(inputControlsPartSelected.getInputControlState().get(8).getTotalCount()).isEqualTo("249");
        List<String> componentPartOptions = inputControlsPartSelected.getInputControlState().get(8).getAllOptions();
        for (String component : componentPartOptions) {
            softAssertions.assertThat(component.contains("[part]")).isEqualTo(true);
        }

        // assembly
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        jasperApiUtils.setReportParameterByName(
            InputControlsEnum.COMPONENT_TYPE.getInputControlId(),
            inputControls.getComponentType().getOption("assembly").getValue()
        );

        JasperReportSummary jasperReportSummaryAssemblyOption = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(exportSetName).getValue()
        );

        Document assemblyReportHtmlPart = jasperReportSummaryAssemblyOption.getReportHtmlPart();

        softAssertions.assertThat(assemblyReportHtmlPart.getElementsContainingText("Component Type:").get(6)
            .siblingElements().get(1).text()
        ).isEqualTo("assembly");

        softAssertions.assertThat(assemblyReportHtmlPart.getElementsContainingText("Process Group:").get(6)
            .siblingElements().get(1).text()
        ).isEqualTo("Assembly");

        // part
        jasperApiUtils.setReportParameterByName(
            InputControlsEnum.COMPONENT_TYPE.getInputControlId(),
            inputControls.getComponentType().getOption("part").getValue()
        );

        JasperReportSummary jasperReportSummaryPartOption = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(exportSetName).getValue()
        );

        Document partReportHtmlPart = jasperReportSummaryPartOption.getReportHtmlPart();

        softAssertions.assertThat(partReportHtmlPart.getElementsContainingText("Component Type:").get(6)
            .siblingElements().get(1).text()
        ).isEqualTo("part");

        softAssertions.assertThat(partReportHtmlPart.getElementsContainingText("Process Group:").get(6)
            .siblingElements().get(1).text()
        ).isEqualTo("Sheet Metal");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3327")
    @TestRail(id = 3327)
    @Description("Verify Scenario Name input control functions correctly")
    public void verifyScenarioNameInputControlsFunctionsCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        UpdatedInputControlsRootItemScenarioComparison inputControlsScenarioName =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                InputControlsEnum.SCENARIO_NAME.getInputControlId(),
                "Initial",
                "",
                false
            );

        ArrayList<InputControlState> inputControlStateList = inputControlsScenarioName.getInputControlState();

        softAssertions.assertThat(inputControlStateList.get(3).getTotalCount()).isEqualTo("12");

        List<String> inputControlsComponentList = inputControlStateList.get(8).getAllOptions();
        for (String componentValue : inputControlsComponentList) {
            softAssertions.assertThat(componentValue.contains("Initial")).isEqualTo(true);
        }

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3328")
    @TestRail(id = 3328)
    @Description("Verify Latest Export Date input control functions correctly")
    public void verifyLatestExportDateInputControlFunctionsCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String currentDateTime = DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now().minusYears(1));

        UpdatedInputControlsRootItemScenarioComparison inputControlsLatestExportDate =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
                currentDateTime,
                "",
                false
            );

        ArrayList<InputControlState> inputControlStateList = inputControlsLatestExportDate.getInputControlState();

        softAssertions.assertThat(inputControlStateList.get(3).getTotalCount()).isEqualTo("12");
        softAssertions.assertThat(inputControlStateList.get(5).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateList.get(6).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateList.get(7).getTotalCount()).isEqualTo("0");
        softAssertions.assertThat(inputControlStateList.get(8).getError()).isEqualTo("This field is mandatory so you must enter data.");
        softAssertions.assertThat(inputControlStateList.get(8).getTotalCount()).isEqualTo("0");

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3331")
    @TestRail(id = 3331)
    @Description("Verify created by input control works correctly")
    public void verifyCreatedByInputControlWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentUser = inputControls.getCreatedBy().getOption("Ben Hegan <bhegan>").getValue();

        UpdatedInputControlsRootItemScenarioComparison inputControlsCreatedBy =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                InputControlsEnum.CREATED_BY.getInputControlId(),
                currentUser,
                "",
                false
            );

        ArrayList<InputControlState> inputControlStateList = inputControlsCreatedBy.getInputControlState();

        softAssertions.assertThat(inputControlStateList.get(5).getOptions().get(3).getLabel()).isEqualTo("Ben Hegan <bhegan>");

        softAssertions.assertThat(inputControlStateList.get(5).getOptions().get(3).getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStateList.get(6).getTotalCount()).isEqualTo("2");

        softAssertions.assertThat(inputControlStateList.get(7).getTotalCount()).isEqualTo("1");

        softAssertions.assertThat(inputControlStateList.get(8).getTotalCount()).isEqualTo("18");

        softAssertions.assertAll();
    }*/

    @Test
    @Tag(JASPER_API)
    @TmsLink("3330")
    @TestRail(id = 3330)
    @Description("Input controls - Component Number Search Criteria")
    public void verifyInputControlsComponentNumberSearchCriteria() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        UpdatedInputControlsRootItemComponentCost inputControlsComponentNumberSearchCriteria =
            jasperReportUtil.getInputControlsModified2(
                ReportNamesEnum.COMPONENT_COST.getReportName(),
                InputControlsEnum.COMPONENT_NUMBER.getInputControlId(),
                "3538968",
                "",
                true
            );

        ArrayList<InputControlState> inputControlStateList = inputControlsComponentNumberSearchCriteria.getInputControlState();
        softAssertions.assertThat(inputControlStateList.get(1).getValue()).isEqualTo("3538968");
        softAssertions.assertThat(inputControlStateList.get(8).getOptions().get(0).getLabel()).isEqualTo("3538968 (Initial)  [part]");
        softAssertions.assertThat(inputControlStateList.get(8).getOptions().get(0).getSelected()).isEqualTo(true);

        softAssertions.assertAll();
    }
}
