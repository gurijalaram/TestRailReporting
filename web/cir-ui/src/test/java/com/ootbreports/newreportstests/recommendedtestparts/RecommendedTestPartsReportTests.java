package com.ootbreports.newreportstests.recommendedtestparts;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
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
        JasperReportSummary jasperReportSummary = genericProcessGroupTest(ProcessGroupEnum.SHEET_METAL.getProcessGroup());
        String processGroupValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "6").get(0).child(0).text();
        String partNumberValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(8).child(0).text();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(processGroupValue).isEqualTo(ProcessGroupEnum.SHEET_METAL.getProcessGroup());
        softAssertions.assertThat(partNumberValue).isEqualTo(JasperCirApiPartsEnum.SM_CLEVIS_2207240161.getPartName());
    }

    private JasperReportSummary genericProcessGroupTest(String processGroupToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        ReportRequest reportRequest = jasperApiUtils.getReportRequest();

        jasperApiUtils.setReportParameterByName("latestExportDate", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
            .format(LocalDateTime.now()));

        jasperApiUtils.setReportParameterByName("exportSetName", jasperReportUtil.updateInputControls(reportRequest.getParameters())
            .getExportSetName().getOption(exportSetName).getValue());

        jasperApiUtils.setReportParameterByName(Constants.INPUT_CONTROL_NAMES.get("Process Group"), jasperReportUtil.updateInputControls(reportRequest.getParameters())
            .getProcessGroup().getOption(processGroupToSet).getValue());

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }
}
