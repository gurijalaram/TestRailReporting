package com.apriori.cir.ui.tests.ootbreports.newreportstests.recommendedtestparts;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.request.ReportRequest;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testrail.TestRail;

import com.google.common.base.Stopwatch;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RecommendedTestPartsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.RECOMMENDED_TEST_PARTS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.RECOMMENDED_TEST_PARTS;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiUtils jasperApiUtils;

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

        jasperApiUtils.setReportParameterByName(InputControlsEnum.PROCESS_GROUP.getInputControlId(),
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
