package com.ootbreports.newreportstests.scenarioactivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.JasperReportSummaryIncRawData;
import com.apriori.cir.enums.CirApiEnum;
import com.apriori.cir.utils.ReportComponentsResponse;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ScenarioActivityReportTests extends JasperApiAuthenticationUtil {

    private static final String reportsJsonFileName = JasperApiEnum.SCENARIO_ACTIVITY.getEndpoint();
    private static final CirApiEnum reportsNameForInputControls = CirApiEnum.SCENARIO_ACTIVITY;
    private static final String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TestRail(id = 16195)
    @Description("Input Controls - Trending Period - Main Report")
    public void testTrendingPeriod() {
        ArrayList<JasperReportSummaryIncRawData> jasperReportSummaries = jasperApiUtils.scenarioActivityReportGenerationTwoTrendingPeriodsIncRawData();
        JasperReportSummaryIncRawData jasperReportSummaryDaily = jasperReportSummaries.get(0);
        JasperReportSummaryIncRawData jasperReportSummaryYearly = jasperReportSummaries.get(1);

        assertThat(jasperReportSummaryDaily, is(notNullValue()));
        assertThat(jasperReportSummaryYearly, is(notNullValue()));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryDaily), is(equalTo("Daily")));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryYearly), is(equalTo("Yearly")));

        int currentDayOfMonth = LocalDateTime.now().getDayOfMonth();
        String currentMonth = getMonthIntAsString();
        assertThat(getXCategories(jasperReportSummaryDaily), is(equalTo("[[2019/07/29, 2019/07/30, 2021/04/07]]")));
        assertThat(getXCategories(jasperReportSummaryYearly), is(equalTo(String.format("[[2018/%s/%s, 2020/%s/%s]]", currentMonth, currentDayOfMonth, currentMonth, currentDayOfMonth))));
    }

    private String getTrendingValueAboveChart(JasperReportSummaryIncRawData jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(4).siblingElements().get(2).child(0).text();
    }

    private String getXCategories(JasperReportSummaryIncRawData jasperReportSummary) {
        ReportComponentsResponse reportComponentsResponse = jasperReportSummary.getChartDataRaw();
        List<Object> serviceItemOneValues = Collections.singletonList(reportComponentsResponse.getInfoItemOne().getHcinstancedata().getServices().get(0).getDataItem());
        return Collections.singletonList(((LinkedHashMap<?, ?>) serviceItemOneValues.get(0)).get("xCategories")).toString();
    }

    private String getMonthIntAsString() {
        String monthValue = "";
        int monthValueInt = LocalDateTime.now().getMonthValue();
        if (monthValueInt < 10) {
            monthValue = "0".concat(String.valueOf(monthValueInt));
        }
        return monthValue;
    }
}
