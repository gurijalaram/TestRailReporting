package com.ootbreports.newreportstests.scenarioactivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.cir.utils.JasperReportUtil;
import com.apriori.cirapi.entity.enums.InputControlsEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.google.common.base.Stopwatch;
import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ScenarioActivityReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = JasperApiEnum.SCENARIO_ACTIVITY.getEndpoint();
    private static final String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.SCENARIO_ACTIVITY;
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 16195)
    @Description("Input Controls - Trending period")
    public void testTrendingPeriod() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummaryDaily = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        jasperApiUtils.setReportParameterByName(InputControlsEnum.TRENDING_PERIOD.getInputControlId(), "Yearly");

        timer.reset();
        timer.start();
        JasperReportSummary jasperReportSummaryYearly = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        assertThat(jasperReportSummaryDaily, is(notNullValue()));
        assertThat(jasperReportSummaryYearly, is(notNullValue()));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryDaily), is(equalTo("Daily")));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryYearly), is(equalTo("Yearly")));

        // chart is svg and thus complex - not all I need seems to be in html
        // return jasperReportSummary;
    }

    private String getTrendingValueAboveChart(JasperReportSummary jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(4).siblingElements().get(2).child(0).text();
    }
}
