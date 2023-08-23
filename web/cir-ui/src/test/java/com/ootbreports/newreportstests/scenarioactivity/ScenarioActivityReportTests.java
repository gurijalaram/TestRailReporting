package com.ootbreports.newreportstests.scenarioactivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScenarioActivityReportTests extends JasperApiAuthenticationUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioActivityReportTests.class);

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
        LocalDateTime currentDateTime1 = LocalDateTime.now();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.START_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1.minusYears(10)));
        jasperApiUtils.setReportParameterByName(InputControlsEnum.END_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1));
        JasperReportSummary jasperReportSummaryDaily = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        jasperApiUtils.setReportParameterByName(InputControlsEnum.TRENDING_PERIOD.getInputControlId(), "Yearly");

        timer.reset();
        timer.start();
        jasperApiUtils.setReportParameterByName(InputControlsEnum.START_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1.minusYears(10)));
        jasperApiUtils.setReportParameterByName(InputControlsEnum.END_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1));
        JasperReportSummary jasperReportSummaryYearly = jasperReportUtil.generateJasperReportSummary(jasperApiUtils.getReportRequest());
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        // start date (current date but year as 2013): jasperReportSummaryDaily.getReportHtmlPart().getElementsContainingText("Start").get(6).siblingElements().get(2).text()
        // end date (end date should be current time): jasperReportSummaryDaily.getReportHtmlPart().getElementsContainingText("End").get(6).siblingElements().get(2).text()

        assertThat(jasperReportSummaryDaily, is(notNullValue()));
        assertThat(jasperReportSummaryYearly, is(notNullValue()));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryDaily), is(equalTo("Daily")));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryYearly), is(equalTo("Yearly")));
        assertThat(jasperReportSummaryDaily.getReportHtmlPart().getElementsContainingText("Start").get(6).siblingElements().get(2).text(), is(startsWith(currentDateTime1.minusYears(10).toString().substring(0, 10))));
        assertThat(jasperReportSummaryDaily.getReportHtmlPart().getElementsContainingText("End").get(6).siblingElements().get(2).text(), is(equalTo(currentDateTime1)));

        // chart is svg and thus complex - not all I need seems to be in html
        // return jasperReportSummary;
    }

    private String getTrendingValueAboveChart(JasperReportSummary jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(4).siblingElements().get(2).child(0).text();
    }
}
