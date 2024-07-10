package com.apriori.cir.ui.tests.ootbreports.newreportstests.cycletimevaluetracking;


import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.InputControlOption;
import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemCycleTimeValueTracking;
import com.apriori.cir.api.utils.UpdatedInputControlsRootItemCycleTimeValueTrackingDetails;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.testrail.TestRail;

import com.google.common.base.Stopwatch;
import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CycleTimeValueTrackingDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS;
    private Logger logger = LoggerFactory.getLogger(CycleTimeValueTrackingDetailsReportTests.class);
    private SoftAssertions softAssertions = new SoftAssertions();
    // Export Set is not relevant for this report
    private String exportSetName = "";
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("26911")
    @TestRail(id = 26911)
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        SoftAssertions softAssertions = new SoftAssertions();

        for (int i = 6; i < 10; i += 3) {
            String gbpCycleTimeTotalValue = getCycleTimeTotalValue(CurrencyEnum.GBP.getCurrency(), i);

            String usdCycleTimeTotalValue = getCycleTimeTotalValue(CurrencyEnum.USD.getCurrency(), i);

            softAssertions.assertThat(gbpCycleTimeTotalValue).isEqualTo(usdCycleTimeTotalValue);
        }

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2340")
    @TestRail(id = 2340)
    @Description("Validate export set with incorrect naming convention")
    public void validateExportSetWithIncorrectNamingConvention() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(JasperApiAuthenticationUtil.jSessionId);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.GBP.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String projectRollupValue = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName()).getValue();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_NAME.getInputControlId(), "PROJECT 4");

        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());

        Document jasperReportHtmlPart = jasperReportSummary.getReportHtmlPart();
        String jasperReportHtmlString = jasperReportHtmlPart.toString();
        softAssertions.assertThat(jasperReportHtmlPart.getElementsContainingText("Initial").size()).isEqualTo(8);
        softAssertions.assertThat(jasperReportHtmlPart.getElementsContainingText("Current results").size()).isEqualTo(23);
        softAssertions.assertThat(jasperReportHtmlPart.getElementsContainingText("new results").size()).isEqualTo(11);

        softAssertions.assertThat(jasperReportHtmlString.contains("Initial")).isEqualTo(true);
        softAssertions.assertThat(jasperReportHtmlString.contains("Current results")).isEqualTo(true);
        softAssertions.assertThat(jasperReportHtmlString.contains("new results")).isEqualTo(true);

        softAssertions.assertThat(jasperReportHtmlString.contains("first")).isEqualTo(false);
        softAssertions.assertThat(jasperReportHtmlString.contains("second")).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2341")
    @TestRail(id = 2341)
    @Description("Validate export set with components listed in Base rollup but not Final rollup")
    public void validateExportSetWithBaseNotFinalComponents() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(JasperApiAuthenticationUtil.jSessionId);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.GBP.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String projectRollupValue = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName()).getValue();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_NAME.getInputControlId(), "PROJECT 2");

        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("id", "JR_PAGE_ANCHOR_0_1").get(0).child(1).children().size()).isEqualTo(26);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("Base")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("Final")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("-_4[1]")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("-18_1")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("-5")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("IROBOT_18874")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("-18_1")).isEqualTo(true);

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("-18_1").get(8).text().equals("-18_1")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("-18_1").get(12).text().equals("-18_1")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2343")
    @TestRail(id = 2343)
    @Description("Validate report correct with manually costed items within export set")
    public void validateReportCorrectWithManuallyCostedItems() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(JasperApiAuthenticationUtil.jSessionId);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.GBP.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String projectRollupValue = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName()).getValue();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_NAME.getInputControlId(), "PROJECT 3");

        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());

        List<Element> colSpanSixElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "6");
        softAssertions.assertThat(colSpanSixElements.get(6).child(0).text().equals("-")).isEqualTo(true);
        softAssertions.assertThat(colSpanSixElements.get(10).child(0).text().equals("-")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("2344")
    @TestRail(id = 2344)
    @Description("Validate report correct with duplicate parts within the rollups")
    public void validateReportCorrectWithDuplicateRollupParts() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(JasperApiAuthenticationUtil.jSessionId);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.GBP.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String projectRollupValue = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName()).getValue();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_NAME.getInputControlId(), "PROJECT 2");

        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("-18_1")).isEqualTo(true);
        List<Element> listOf181Elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("-18_1");
        softAssertions.assertThat(listOf181Elements.get(8).text().equals("-18_1")).isEqualTo(true);
        softAssertions.assertThat(listOf181Elements.get(12).text().equals("-18_1")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLinks({
        @TmsLink("31309"),
        @TmsLink("2334")
    })
    @TestRail(id = {31309, 2334})
    @Description("Validate report details align with aP Pro / CID - Details Report")
    public void validateReportAlignsWithApProOrCID() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(JasperApiAuthenticationUtil.jSessionId);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.GBP.getCurrency());
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String projectRollupValue = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName()).getValue();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_NAME.getInputControlId(), "PROJECT 2");

        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("26.55")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("6.00")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("13.38")).isEqualTo(true);
        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("15,402.86")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7627")
    @TestRail(id = 7627)
    @Description("Export date lists all available versions from selected export set rollup - Cycle Time Value Tracking Details Report")
    public void testExportDateInputControl() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentProjectRollup = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .getValue();

        ResponseWrapper<UpdatedInputControlsRootItemCycleTimeValueTrackingDetails> inputControlsExportDate =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCycleTimeValueTrackingDetails.class,
                false,
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                ""
            );

        ArrayList<InputControlState> inputControlStates = inputControlsExportDate.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStates.get(0).getTotalCount().equals("12")).isEqualTo(true);
        InputControlOption currencyGbpOption = inputControlStates.get(0).getOptions().get(5);
        softAssertions.assertThat(currencyGbpOption.getValue().equals("GBP")).isEqualTo(true);
        softAssertions.assertThat(currencyGbpOption.getSelected()).isEqualTo(true);

        softAssertions.assertThat(inputControlStates.get(1).getTotalCount().equals("2")).isEqualTo(true);
        InputControlOption rollupOption = inputControlStates.get(1).getOptions().get(1);
        softAssertions.assertThat(rollupOption.getValue().equals(currentProjectRollup)).isEqualTo(true);
        softAssertions.assertThat(rollupOption.getSelected().equals(true)).isEqualTo(true);

        softAssertions.assertThat(inputControlStates.get(2).getTotalCount().equals("4")).isEqualTo(true);
        InputControlOption projectNameOption = inputControlStates.get(2).getOptions().get(1);
        softAssertions.assertThat(projectNameOption.getValue().equals("PROJECT 2")).isEqualTo(true);
        softAssertions.assertThat(projectNameOption.getSelected().equals(true)).isEqualTo(true);

        softAssertions.assertThat(inputControlStates.get(3).getTotalCount().equals("2")).isEqualTo(true);
        softAssertions.assertThat(inputControlStates.get(3).getOptions().get(0).getSelected().equals(true)).isEqualTo(true);
        softAssertions.assertThat(inputControlStates.get(3).getOptions().get(1).getSelected().equals(true)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("7628")
    @TestRail(id = 7628)
    @Description("Projects rollup drop list - Cycle Time Value Tracking Details Report")
    public void testProjectNameRollupDropdownList() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String currentProjectRollup = inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .getValue();

        ResponseWrapper<UpdatedInputControlsRootItemCycleTimeValueTrackingDetails> inputControlsProjectName =
            jasperReportUtil.getInputControlsModified(
                UpdatedInputControlsRootItemCycleTimeValueTrackingDetails.class,
                false,
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                ""
            );

        ArrayList<InputControlState> inputControlStates = inputControlsProjectName.getResponseEntity().getInputControlState();
        softAssertions.assertThat(inputControlStates.get(1).getTotalCount().equals("2")).isEqualTo(true);
        InputControlOption rollupOption = inputControlStates.get(1).getOptions().get(1);
        softAssertions.assertThat(rollupOption.getValue().equals(currentProjectRollup)).isEqualTo(true);
        softAssertions.assertThat(rollupOption.getSelected().equals(true)).isEqualTo(true);

        softAssertions.assertThat(inputControlStates.get(2).getTotalCount().equals("4")).isEqualTo(true);
        InputControlOption projectNameOption = inputControlStates.get(2).getOptions().get(1);
        softAssertions.assertThat(projectNameOption.getValue().equals("PROJECT 2")).isEqualTo(true);
        softAssertions.assertThat(projectNameOption.getSelected().equals(true)).isEqualTo(true);

        softAssertions.assertAll();
    }

    private String getCycleTimeTotalValue(String currencyToUse, int indexToUse) {
        return generateReportCurrencyProjectRollupOnly(currencyToUse, RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .getReportHtmlPart().getElementsByAttributeValue("colspan", "6")
            .get(indexToUse).child(0).text();
    }

    private JasperReportSummary generateReportCurrencyProjectRollupOnly(String currencyToSet, String rollupToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(JasperApiAuthenticationUtil.jSessionId);

        jasperApiUtils.setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyToSet);
        jasperApiUtils.setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now()));

        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);
        String projectRollupValue = inputControls.getProjectRollup().getOption(rollupToSet).getValue();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }
}
