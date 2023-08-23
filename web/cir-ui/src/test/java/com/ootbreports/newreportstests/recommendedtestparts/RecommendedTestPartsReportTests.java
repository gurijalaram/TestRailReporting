package com.ootbreports.newreportstests.recommendedtestparts;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.cir.models.request.ReportRequest;
import com.apriori.cir.models.response.InputControl;
import com.apriori.cir.utils.JasperReportUtil;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.testrail.TestRail;

import com.google.common.base.Stopwatch;
import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import enums.JasperCirApiPartsEnum;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RecommendedTestPartsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.RECOMMENDED_TEST_PARTS.getEndpoint();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.RECOMMENDED_TEST_PARTS;
    private static final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 14000)
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
        InputControl inputControls = jasperReportUtil.getInputControls(reportsNameForInputControls);

        jasperApiUtils.setReportParameterByName("latestExportDate", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
            .format(LocalDateTime.now()));

        jasperApiUtils.setReportParameterByName("exportSetName", inputControls.getExportSetName().getOption(exportSetName).getValue());

        jasperApiUtils.setReportParameterByName(com.apriori.cirapi.entity.enums.InputControlsEnum.PROCESS_GROUP.getInputControlId(),
            inputControls.getProcessGroup().getOption(processGroupToSet).getValue()
        );

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }
}
