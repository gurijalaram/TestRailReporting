package com.ootbreports.newreportstests.scenarioactivity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ScenarioActivityProcessActivityReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.SCENARIO_ACTIVITY_PROCESS_ACTIVITY.getEndpoint();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.SCENARIO_ACTIVITY_PROCESS_ACTIVITY;
    private static final String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 28004)
    @Description("Input Controls - Trending period - Process Activity Report")
    public void testTrendingPeriod() {
        ArrayList<JasperReportSummary> jasperReportSummaries = jasperApiUtils.scenarioActivityReportGenerationTwoTrendingPeriods();
        JasperReportSummary jasperReportSummaryDaily = jasperReportSummaries.get(0);
        JasperReportSummary jasperReportSummaryYearly = jasperReportSummaries.get(1);

        ArrayList<Element> dateElementsDaily = jasperReportSummaryDaily.getReportHtmlPart().getElementsContainingText("Process Name").get(5).children();
        ArrayList<Element> dateElementsYearly = jasperReportSummaryYearly.getReportHtmlPart().getElementsContainingText("Process Name").get(5).children();
        int currentDayOfMonth = LocalDateTime.now().getDayOfMonth();
        assertAll("Grouped Date Axis Assertions",
            () -> assertEquals(dateElementsDaily.get(2).text(), "Jul 29"),
            () -> assertEquals(dateElementsDaily.get(3).text(), "Jul 30"),
            () -> assertEquals(dateElementsDaily.get(4).text(), "Apr 07"),
            () -> assertEquals(dateElementsYearly.get(2).text(), String.format("Aug %s", currentDayOfMonth)),
            () -> assertEquals(dateElementsYearly.get(3).text(), String.format("Aug %s", currentDayOfMonth))
        );

        assertAll("Grouped Trending Period Assertions",
            () -> assertEquals(getTrendingValueAboveChart(jasperReportSummaryDaily), "Daily"),
            () -> assertEquals(getTrendingValueAboveChart(jasperReportSummaryYearly), "Yearly")
        );
    }

    private String getTrendingValueAboveChart(JasperReportSummary jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(3).text();
    }
}
