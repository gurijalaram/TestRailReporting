package com.ootbreports.newreportstests.scenarioactivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.cir.utils.JasperReportUtil;
import com.apriori.cir.utils.ReportComponentsResponse;
import com.apriori.cir.utils.Services;
import com.apriori.cirapi.entity.enums.InputControlsEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

        //Document dailyDoc = Jsoup.parse(jasperReportSummaryDaily.getChartDataRaw());
        ReportComponentsResponse reportComponentsResponse = jasperReportSummaryDaily.getChartDataRaw();
        //dsServices services = reportComponentsResponse.getInfoItemOne().getHcinstancedata().getServices().get(0);
        assertThat(reportComponentsResponse, is(notNullValue()));
        //LinkedHashMap<String, ArrayList<String>> servicesValues = ((LinkedHashMap<String, ArrayList<String>>) jasperReportSummaryDaily.getChartDataRaw().getInfoItemOne().getHcinstancedata().getServices().get(0).getData());
        //Object vals = reportComponentsResponse.getInfoItemOne().getHcinstancedata().getServices().get(0).getData();
        //String values = vals.toString();
        //ArrayList<String> values1 = ((LinkedHashMap<String, String>) vals).get("xCategories");
        //assertThat(vals, is(notNullValue()));
        // sort this out!: ((LinkedHashMap) jasperReportSummaryDaily.getChartDataRaw().getInfoItemOne().getHcinstancedata().getServices().get(0).getData()).get("xCategories");

        /*assertThat(jasperReportSummaryDaily, is(notNullValue()));
        assertThat(jasperReportSummaryYearly, is(notNullValue()));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryDaily), is(equalTo("Daily")));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryYearly), is(equalTo("Yearly")));
        String currentDateTime = currentDateTime1.toString();
        String currentDateTimeOnReportDaily = jasperReportSummaryDaily.getReportHtmlPart().getElementsContainingText("End").get(6).siblingElements().get(2).text();
        assertThat(jasperReportSummaryDaily.getReportHtmlPart().getElementsContainingText("Start").get(6).siblingElements().get(2).text().contains(currentDateTime1.minusYears(10).toString().substring(0, 10)), is(equalTo(true)));
        assertThat(currentDateTimeOnReportDaily.contains(currentDateTime.substring(0, 10)), equalTo(true));
        assertThat(currentDateTimeOnReportDaily.contains(currentDateTime.substring(11, 19)), equalTo(true));

        String currentDateTimeOnReportYearly = jasperReportSummaryYearly.getReportHtmlPart().getElementsContainingText("End").get(6).siblingElements().get(2).text();
        assertThat(jasperReportSummaryYearly.getReportHtmlPart().getElementsContainingText("Start").get(6).siblingElements().get(2).text().contains(currentDateTime1.minusYears(10).toString().substring(0, 10)), is(equalTo(true)));
        assertThat(currentDateTimeOnReportYearly.contains(currentDateTime.substring(0, 10)), equalTo(true));
        assertThat(currentDateTimeOnReportYearly.contains(currentDateTime.substring(11, 19)), equalTo(true));*/

        // chart is svg and thus complex - not all I need seems to be in html
        // return jasperReportSummary;
    }

    private String getTrendingValueAboveChart(JasperReportSummary jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(4).siblingElements().get(2).child(0).text();
    }
}
