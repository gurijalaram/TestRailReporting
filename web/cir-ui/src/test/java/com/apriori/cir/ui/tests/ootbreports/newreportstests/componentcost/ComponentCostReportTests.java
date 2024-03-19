package com.apriori.cir.ui.tests.ootbreports.newreportstests.componentcost;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.request.ReportRequest;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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

    @Test
    @Tag(JASPER_API)
    @TmsLink("3324")
    @TestRail(id = 3324)
    @Description("Verify Export Set drop-down functions correctly")
    public void verifyExportSetDropdownWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        // get input controls, check at least 12 export sets are there (my data)
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        List<String> exportSetOptions = inputControls.getExportSetName().getAllOptions();
        softAssertions.assertThat(exportSetOptions.size() >= 12).isEqualTo(true);

        // select top level export set and then check that scenario name and component select are filtered accordingly
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        InputControl inputControlsUpdated = jasperReportUtil.getInputControls(reportsNameForInputControls);
        softAssertions.assertThat(inputControlsUpdated).isNotEqualTo(null);

        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        ReportRequest reportRequest = jasperApiUtils.getReportRequest();
        softAssertions.assertThat(jasperReportSummary).isNotEqualTo(null);

        // can't quite do what we need but we can check a couple of things that roughly ensure what we need to ensure is true
        softAssertions.assertThat(inputControlsUpdated.getExportSetName().getAllOptions().size() > 1).isEqualTo(true);
        softAssertions.assertThat(reportRequest.getParameters().getReportParameterByName("exportSetName").getValue().size()).isEqualTo(1);

        softAssertions.assertThat(inputControlsUpdated.getScenarioName().getAllOptions().size() > 1).isEqualTo(true);
        softAssertions.assertThat(reportRequest.getParameters().getReportParameterByName("scenarioName").getValue().size()).isEqualTo(1);

        softAssertions.assertThat(inputControlsUpdated.getComponentSelect().getAllOptions().size() > 1).isEqualTo(true);
        softAssertions.assertThat(reportRequest.getParameters().getReportParameterByName("componentSelect").getValue().size()).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("3325")
    @TestRail(id = 3325)
    @Description("Verify Component Select drop-down functions correctly")
    public void verifyComponentSelectDropdownWorksCorrectly() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        // get input controls, check at least 12 export sets are there (my data)
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        List<String> exportSetOptions = inputControls.getExportSetName().getAllOptions();
        softAssertions.assertThat(exportSetOptions.size() >= 12).isEqualTo(true);

        // inputControls.getComponentSelect().getAllOptions().size() is 257, assert on this (correct for cloud too?)
        softAssertions.assertThat(inputControls.getComponentSelect().getAllOptions().size() > 200).isEqualTo(true);

        // then select one using this code: inputControls.getComponentSelect().getOption("TOP-LEVEL (Initial)  [assembly]").getValue()
        jasperApiUtils.setReportParameterByName(
            InputControlsEnum.COMPONENT_SELECT.getInputControlId(),
            inputControls.getComponentSelect().getOption("TOP-LEVEL (Initial)  [assembly]").getValue());

        // generate jasper report: genericTestCore
        JasperReportSummary jasperReportSummary = jasperApiUtils.genericTestCore(
            InputControlsEnum.EXPORT_SET_NAME.getInputControlId(),
            inputControls.getExportSetName().getOption(exportSetName).getValue()
        );

        // check scenario name list as below
        ReportRequest reportRequest = jasperApiUtils.getReportRequest();
        softAssertions.assertThat(inputControls.getScenarioName().getAllOptions().size() > 1).isEqualTo(true);
        softAssertions.assertThat(reportRequest.getParameters().getReportParameterByName("scenarioName").getValue().size()).isEqualTo(1);

        // click ok and ensure report generates correctly
        // go and find part number: jasperReportSummary.getReportHtmlPart().getElementsContainingText("Part Number:").get(7).parent().siblingElements().get(1).children().get(0).text()
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Part Number:").get(7).parent().siblingElements().get(1).children().get(0).text()).isEqualTo("TOP-LEVEL");

        softAssertions.assertAll();
    }
}
