package com.ootbreports.newreportstests.recommendedtestparts;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;

import com.google.common.base.Stopwatch;
import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class RecommendedTestPartsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.RECOMMENDED_TEST_PARTS.getEndpoint();
    private static final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"14000"})
    @Description("Input controls - Test Process Groups")
    public void testProcessGroupSheetMetal() {
        JasperReportSummary jasperReportSummary = genericProcessGroupTest("Process Group", ProcessGroupEnum.ASSEMBLY);
        String processGroupValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "6").get(0).child(0).text();
        String partNumberValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(8).child(0).text();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(processGroupValue).isEqualTo(ProcessGroupEnum.SHEET_METAL.getProcessGroup());
        softAssertions.assertThat(partNumberValue).isEqualTo(JasperCirApiPartsEnum.SM_CLEVIS_2207240161.getPartName());
    }

    private JasperReportSummary genericProcessGroupTest(String keyToSet, ProcessGroupEnum valueToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        JasperApiUtils jasperApiUtils1 = new JasperApiUtils(jSessionId, exportSetName, valueToSet, reportsJsonFileName);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        ReportRequest reportRequest = jasperApiUtils1.getReportRequest();
        jasperApiUtils1.setReportParameterByName("latestExportDate", currentDateTime);
        InputControl inputControls = jasperReportUtil.getInputControls(CirApiEnum.RECOMMENDED_TEST_PARTS);
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        jasperApiUtils1.setReportParameterByName("exportSetName", currentExportSet);
        //String currentProcessGroup = inputControls.getProcessGroup().getOption(keyToSet).getValue();
        //jasperApiUtils1.setReportParameterByName(Constants.INPUT_CONTROL_NAMES.get(keyToSet), currentProcessGroup);
        jasperApiUtils1.setReportParameterByName(Constants.INPUT_CONTROL_NAMES.get(keyToSet), "455662");

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }
}
