package com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS_API;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScenarioActivityMaterialActivityReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.SCENARIO_ACTIVITY_MATERIAL_ACTIVITY.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.SCENARIO_ACTIVITY_MATERIAL_ACTIVITY;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TmsLink("28003")
    @TestRail(id = 28003)
    @Description("Input Controls - Trending period - Material Activity Report")
    public void testTrendingPeriod() {
        ArrayList<JasperReportSummary> jasperReportSummaries = jasperApiUtils.scenarioActivityReportGenerationTwoTrendingPeriods();
        JasperReportSummary jasperReportSummaryDaily = jasperReportSummaries.get(0);
        JasperReportSummary jasperReportSummaryYearly = jasperReportSummaries.get(1);

        ArrayList<Element> dateElementsDaily = jasperApiUtils.getElementsForScenarioActivityReportTests(jasperReportSummaryDaily);
        ArrayList<Element> dateElementsYearly = jasperApiUtils.getElementsForScenarioActivityReportTests(jasperReportSummaryYearly);

        String currentDayOfMonth = DateTimeFormatter.ofPattern("dd").format(LocalDateTime.now());
        String currentMonthValue = jasperApiUtils.getCurrentMonthValue();

        assertAll("Grouped Date Axis Assertions",
            () -> assertEquals(dateElementsDaily.get(0).text(), "Jul 29"),
            () -> assertEquals(dateElementsDaily.get(1).text(), "Jul 30"),
            () -> assertEquals(dateElementsDaily.get(2).text(), "Apr 07"),
            () -> assertEquals(dateElementsYearly.get(0).text(), String.format("%s %s", currentMonthValue, currentDayOfMonth)),
            () -> assertEquals(dateElementsYearly.get(1).text(), String.format("%s %s", currentMonthValue, currentDayOfMonth))
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
